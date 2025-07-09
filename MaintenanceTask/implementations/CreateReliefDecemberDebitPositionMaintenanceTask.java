/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenancetask;

import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.CANCELED;
import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.ENDED;
import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.GPKE_APPROVED;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nextlevel.fastlane.financial.bo.AccountingDocument;
import com.nextlevel.fastlane.financial.bo.TimeSlice;
import com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.enums.ContractTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.financial.account.ExtendedFinancialAccountDAO;
import com.nextlevel.fastlane.myBusinessSupplier.financial.context.FinancialContextTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.financial.division.FinancialDivisionEnum;
import com.nextlevel.fastlane.myBusinessSupplier.financial.transfer.TaxIncludedTaxedTransferExecutor;
import com.nextlevel.fastlane.persistence.financial.dao.EntryDAO;
import com.nextlevel.fastlane.persistence.financial.dao.EntryDAOBase;
import com.nextlevel.fastlane.persistence.financial.pvo.EntryPVO;
import com.nextlevel.fastlane.persistence.financial.pvo.FinancialAccountPVO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ReliefDecember2022DAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.ReliefDecember2022PVO;
import com.nextlevel.myBusinessSupplier.constants.SectionEnum;
import com.nextlevel.myBusinessSupplier.constants.reliefdecember2022.ReliefDecember2022StateEnum;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.date.DateTool;
import com.nextlevel.platform.exception.ExceptionTool;
import com.nextlevel.platform.financial.account.GlobalAccountType;
import com.nextlevel.platform.financial.context.FinancialContext;
import com.nextlevel.platform.financial.context.FinancialContextResult;
import com.nextlevel.platform.financial.debitor.DebitorAccountType;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;
import com.nextlevel.platform.financial.transfer.Transfer;
import com.nextlevel.platform.financial.vo.Money;

public class CreateReliefDecemberDebitPositionMaintenanceTask implements MaintenanceTask {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Date PIVOT_DATE = DateTool.getDate(1, Calendar.DECEMBER, 2022);
	private static final ReliefDecember2022DAO RELIEF_DAO = new ReliefDecember2022DAO();

	Iterator<ReliefDecember2022PVO> dueReliefsIterator = null;

	@Override
	public void setup() throws MaintenanceTaskSetupFailedException {
		final List<ReliefDecember2022PVO> dueReliefs = RELIEF_DAO.listDue();
		dueReliefsIterator = dueReliefs.iterator();
		LOGGER.info("Found {} due reliefs to process.", dueReliefs.size());
	}

	@Override
	public void processNextEntry() throws MaintenanceEntryProcessingFailedException {
		ReliefDecember2022PVO currentRelief = dueReliefsIterator.next();
		try {
			final Contract contract = PersistenceHandler.getInstance().loadContract(currentRelief.getContract());

			CommandProcessor.processInNewConnection(c -> {
				currentRelief.setExceptionLog(null);
				currentRelief.setValidationLog(null);

				final String validationResult = validate(contract);
				if (StringUtils.isNotEmpty(validationResult)) {
					currentRelief.setValidationLog(validationResult);
				}

				final AccountingDocument accountingDocument = createEntriesAndCoverage(currentRelief.getReliefAmount(), contract);

				currentRelief.setAccountingDocument(accountingDocument.getId());
				currentRelief.setState(ReliefDecember2022StateEnum.DONE.name());
				RELIEF_DAO.update(currentRelief);
				return null;
			});
		} catch (Exception e) {
			currentRelief.setState(ReliefDecember2022StateEnum.ERROR.name());
			currentRelief.setExceptionLog(StringUtils.substring(e.getMessage(), 0, 999));
			try {
				RELIEF_DAO.update(currentRelief);
			} catch (Exception ex) {
				//exception is thrown anyway - just log failed update
				LOGGER.error("Cannot set relief state to ERROR for relief with ID " + currentRelief.getId());
			}
			throw new MaintenanceEntryProcessingFailedException("Relief with ID " + currentRelief.getId() +
					" for contract with ID " + currentRelief.getContract() + " number " + currentRelief.getContractNr(), e);
		}
	}

	@Override
	public boolean hasNextEntry() {
		return dueReliefsIterator.hasNext();
	}

	@Override
	public boolean repeatTask() {
		return true;
	}

	private String validate(final Contract contract) {
		final StringBuilder validationResults = new StringBuilder();

		if (!SectionEnum.Gas.equals(SectionEnum.fromString(contract.getSection()))) {
			validationResults.append("Der Vertrag ist kein Gas Vertrag. ");
		}

		if (!ContractTypeEnum.isContractSLP(contract)) {
			validationResults.append("Der Vertrag ist kein SLP Vertrag. ");
		}

		final List<ContractStateEnum> acceptedContractStates = Arrays.asList(GPKE_APPROVED, ENDED, CANCELED);
		if (!acceptedContractStates.contains(ContractStateEnum.valueOf(contract.getState()))) {
			validationResults.append("Der Vertrag ist nicht in Belieferung, gekündigt oder im Kündigungsprozess. ");
		}

		if (!TimeSliceTool.isInSlice(PIVOT_DATE, contract)) {
			validationResults.append("Der Belieferungszeitraum des Vertrages enthält nicht den 01.12.2022. ");
		}

		final List<EntryPVO> listDecemberAdvancePays = listDecemberAdvancePays(contract);
		if (!listDecemberAdvancePays.isEmpty()) {
			final String entryIds = listDecemberAdvancePays.stream()
					.map(EntryPVO::getId)
					.map(Object::toString)
					.collect(Collectors.joining(", "));

			validationResults.append("Der Vertrag enthält bereits Abschlagsbuchungen für Dezember: ");
			validationResults.append(entryIds);
		}

		return validationResults.toString();
	}

	private List<EntryPVO> listDecemberAdvancePays(Contract contract) {
		try {
			final ExtendedFinancialAccountDAO accountDAO = new ExtendedFinancialAccountDAO();
			final FinancialAccountPVO debitorAccount = accountDAO.getAccount(contract, DebitorAccountType.Debitor);
			final FinancialAccountPVO advancePayAccount = accountDAO.getAccount(contract, DebitorAccountType.AdvancePay);

			final TimeSlice december2022 = TimeSliceTool.getCurrentMonth(PIVOT_DATE);

			final EntryPVO pattern = new EntryPVO();
			pattern.setDebit(debitorAccount.getId());
			pattern.setCredit(advancePayAccount.getId());
			final String notRevertedFilter = EntryDAOBase.column_isRevertedBy + " is null ";

			return new EntryDAO().list(pattern, false, notRevertedFilter).stream()
					//filter in code instead of sql for pragmatic reasons:
					.filter(e -> TimeSliceTool.overlaps(TimeSliceTool.valueOf(e.getPeriodStart(), e.getPeriodEnd()), december2022))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	private AccountingDocument createEntriesAndCoverage(final BigDecimal reliefAmount, final Contract contract) {
		return FinancialContext.process(FinancialContextTypeEnum.Debitor_Emergency_Aid, c -> {
			final ExtendedFinancialAccountDAO accountDAO = new ExtendedFinancialAccountDAO();
			final FinancialAccountPVO reliefAccount = accountDAO.getAccount(FinancialDivisionEnum.General,
					GlobalAccountType.ErdgasWaermeSoforthilfegesetz);
			final FinancialAccountPVO debitorAccount = accountDAO.getAccount(contract, DebitorAccountType.Debitor);
			final FinancialAccountPVO advancePayAccount = accountDAO.getAccount(contract, DebitorAccountType.AdvancePay);

			final String entrySubject = "Soforthilfe Dezember Abschlag 2022";
			final TimeSlice entrySlice = TimeSliceTool.getCurrentMonth(PIVOT_DATE);

			final Transfer scholzToDebitorEntry = new Transfer(reliefAccount, debitorAccount, Money.valueOf(reliefAmount));
			scholzToDebitorEntry.setSubject(entrySubject);
			scholzToDebitorEntry.setPeriod(entrySlice);
			scholzToDebitorEntry.setAcceptZeroValue(true);
			final Collection<EntryPVO> scholzToDebitorEntries = scholzToDebitorEntry.execute();

			final TaxIncludedTaxedTransferExecutor debitorToAdvancePayEntry = new TaxIncludedTaxedTransferExecutor(
					debitorAccount, advancePayAccount, Money.valueOf(reliefAmount), contract);
			debitorToAdvancePayEntry.setSubject(entrySubject);
			debitorToAdvancePayEntry.setAcceptZeroValue(true);
			debitorToAdvancePayEntry.addEntriesToCover(scholzToDebitorEntries);
			debitorToAdvancePayEntry.setPeriod(entrySlice);
			debitorToAdvancePayEntry.execute();

			return new FinancialContextResult<>(FinancialContext.getAccountingDocument())
					.setExternalReference(entrySubject)
					.setSubject(entrySubject)
					.setPeriodStart(entrySlice.getPeriodStart())
					.setPeriodEnd(entrySlice.getPeriodEnd());
		});
	}
}
