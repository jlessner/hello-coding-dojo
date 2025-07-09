/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenancetask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nextlevel.fastlane.core.persistence.dao.UtilDAO;
import com.nextlevel.fastlane.myBusinessSupplier.BusinessPartnerRoleEnum;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.contract.bo.AddDirectDebitPosition;
import com.nextlevel.fastlane.myBusinessSupplier.contract.bo.DirectDebitPositionServiceResult;
import com.nextlevel.fastlane.myBusinessSupplier.contract.bo.NetEntryListWrapper;
import com.nextlevel.fastlane.myBusinessSupplier.contract.manual.ManualDirectDebitPositionService;
import com.nextlevel.fastlane.myBusinessSupplier.directdebit.DirectDebitPositionSourceEnum;
import com.nextlevel.fastlane.myBusinessSupplier.directdebit.DirectDebitPositionStateEnum;
import com.nextlevel.fastlane.myBusinessSupplier.exception.ValidationFailedException;
import com.nextlevel.fastlane.myBusinessSupplier.financial.context.FinancialContextTypeEnum;
import com.nextlevel.fastlane.persistence.financial.dao.AccountingDocumentDAO;
import com.nextlevel.fastlane.persistence.financial.dao.EntryDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ContractDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ContractPartnerDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.DirectDebitPositionDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.DirectDebitPositionEntryMappingDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.FinancialAccountDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.FinancialAccountOwnershipDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.PaymentMethodApplicationDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.ContractPartnerPVO;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.connection.CommandProcessor;

public class CreateDirectDebitPositionsInMassMaintenanceTask implements MaintenanceTask {
	public final ManualDirectDebitPositionService manualDirectDebitPositionService = new ManualDirectDebitPositionService();
	private static final String CONTRACT_COLUMN_ALIAS = "contract";
	private static final String FINANCIAL_ACCOUNT_DEBITOR_COLUMN_ALIAS = "account";
	private static final String ENTRY_SUBJECT_COLUMN_ALIAS = "entrysubject";
	private static final String ENTRY_ACCOUNTINGDOCUMENT_COLUMN_ALIAS = "entryaccountingdocument";
	private static final String ACCOUNTINGDOCUMENT_TYPE_COLUMN_ALIAS = "accountingdocumenttype";
	private static final String ACCOUNTINGDOCUMENT_SUBJECT_COLUMN_ALIAS = "accountingdocumentsubject";
	private static final String ENTRY_ID_COLUMN_ALIAS = "entryid";
	private static final String WITH_CONTRACT_ALIAS = "cont_cte";
	private static final String WITH_ACCOUNT_ALIAS = "acc_cte";
	private static final String WITH_ENTRY_ALIAS = "entry_cte";
	private static final Logger LOGGER = LogManager.getLogger();
	Iterator<AddDirectDebitPosition> dueDirectDebitPositions = null;

	private final PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();
	private final ContractPartnerDAO contractPartnerDAO = new ContractPartnerDAO();

	@Override
	public void setup() throws MaintenanceTaskSetupFailedException {
		final List<AddDirectDebitPosition> directDebitPositionsToCreate = new ArrayList<>();
		final List<SqlReportElement> invoiceObjects = new ArrayList<>();
		try {
			CommandProcessor.process(connection -> {
				PreparedStatement pstm = connection.prepareStatement(getSQL());

				ResultSet resultSet = pstm.executeQuery();
				while (resultSet.next()) {
					try {
						SqlReportElement sqlReportElement = new SqlReportElement()
								.withContract(UtilDAO.getLong(resultSet, CONTRACT_COLUMN_ALIAS))
								.withEntry(UtilDAO.getLong(resultSet, ENTRY_ID_COLUMN_ALIAS))
								.withAccountingDocument(UtilDAO.getLong(resultSet, ENTRY_ACCOUNTINGDOCUMENT_COLUMN_ALIAS))
								.withEntrySubject(resultSet.getString(ENTRY_SUBJECT_COLUMN_ALIAS))
								.withAccountingDocumentType(resultSet.getString(ACCOUNTINGDOCUMENT_TYPE_COLUMN_ALIAS))
								.withAccountingDocumentSubject(resultSet.getString(ACCOUNTINGDOCUMENT_SUBJECT_COLUMN_ALIAS));

						Contract contract = persistenceHandler.loadContract(sqlReportElement.getContract());
						ContractPartnerPVO contractPartnerPVO = new ContractPartnerPVO();
						contractPartnerPVO.setContract(contract.getId());
						contractPartnerPVO.setRole(BusinessPartnerRoleEnum.Customer.getId());
						List<ContractPartnerPVO> contractPartnerPVOS = contractPartnerDAO.list(contractPartnerPVO);
						Optional<ContractPartnerPVO> debitor = contractPartnerPVOS.stream().findFirst();
						if (debitor.isPresent()) {
							Long debitorId = debitor.get().getBusinessPartner();
							sqlReportElement.setDebitor(debitorId);

							if (sqlReportElement.getAccountingDocumentType()
									.equals(FinancialContextTypeEnum.Debitor_Invoice_Accounting.getAccountingDocumentTypeId())) {
								invoiceObjects.add(sqlReportElement);
							} else if (sqlReportElement.getAccountingDocumentType()
									.equals(FinancialContextTypeEnum.Debitor_AdvancePaymentRequest.getAccountingDocumentTypeId())) {
								AddDirectDebitPosition addDirectDebitPosition = new AddDirectDebitPosition()
										.withContract(sqlReportElement.getContract())
										.withDebitor(sqlReportElement.getDebitor());
								addDirectDebitPosition.setSource(DirectDebitPositionSourceEnum.PAYPLAN_POSITION_CREATION.getDataBaseKey());
								addDirectDebitPosition.addNetEntries(new NetEntryListWrapper().withId(sqlReportElement.getEntry()));
								directDebitPositionsToCreate.add(addDirectDebitPosition);
							}
						} else {
							LOGGER.error(String.format("For contract %s there was no debitor found."), contract.getContractNr());
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
				}
				directDebitPositionsToCreate.addAll(findEqualPositionsWithDifferentEntries(invoiceObjects));
				return null;
			});
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			dueDirectDebitPositions = directDebitPositionsToCreate.iterator();
		}
	}

	@Override
	public void processNextEntry() throws MaintenanceEntryProcessingFailedException {
		AddDirectDebitPosition addDirectDebitPosition = dueDirectDebitPositions.next();
		try {
			DirectDebitPositionServiceResult directDebitPositionServiceResult =
					manualDirectDebitPositionService.addDirectDebitPosition(addDirectDebitPosition);
			ValidationFailedException.failByValidationErrors(directDebitPositionServiceResult.getValidationResult(),
					"Failed to create DirectDebitPosition due to ValidationResults.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new MaintenanceEntryProcessingFailedException(getErrorMessageWithContractAndEntries(addDirectDebitPosition), e);
		}
	}

	private String getErrorMessageWithContractAndEntries(AddDirectDebitPosition addDirectDebitPosition) {
		String entriesIds =
				addDirectDebitPosition.getNetEntries().stream().map(entry -> entry.getId().toString()).collect(Collectors.joining(","));
		return String.format("Contract id: %s with entries: [%s]", addDirectDebitPosition.getContract().toString(), entriesIds);
	}

	private List<AddDirectDebitPosition> findEqualPositionsWithDifferentEntries(List<SqlReportElement> invoiceObjects) {
		ArrayList<AddDirectDebitPosition> addDirectDebitPositions = new ArrayList<>();
		while (CollectionUtils.isNotEmpty(invoiceObjects)) {
			SqlReportElement sqlReportElement = invoiceObjects.stream().findFirst().get();
			invoiceObjects.remove(sqlReportElement);
			List<SqlReportElement> equalEntries = invoiceObjects.stream()
					.filter(o -> o.getAccountingDocumentType().equals(sqlReportElement.getAccountingDocumentType()))
					.filter(o -> o.getContract().equals(sqlReportElement.getContract()))
					.filter(o -> o.getDebitor().equals(sqlReportElement.getDebitor()))
					.filter(o -> o.getEntrySubject().equals(sqlReportElement.getEntrySubject()))
					.filter(o -> o.getAccountingDocument().equals(sqlReportElement.getAccountingDocument()))
					.filter(o -> o.getAccountingDocumentSubject().equals(sqlReportElement.getAccountingDocumentSubject()))
					.collect(Collectors.toList());
			List<NetEntryListWrapper> netEntryListWrappers = new ArrayList<>();
			netEntryListWrappers.addAll(
					equalEntries.stream().map(e -> new NetEntryListWrapper().withId(e.getEntry())).collect(Collectors.toList()));
			netEntryListWrappers.add(new NetEntryListWrapper().withId(sqlReportElement.getEntry()));
			invoiceObjects.removeAll(equalEntries);

			AddDirectDebitPosition addDirectDebitPosition = new AddDirectDebitPosition()
					.withNetEntries(netEntryListWrappers)
					.withContract(sqlReportElement.getContract())
					.withDebitor(sqlReportElement.getDebitor())
					.withSource(DirectDebitPositionSourceEnum.DEBITOR_INVOICE_CREATION.getDataBaseKey());
			addDirectDebitPositions.add(addDirectDebitPosition);
		}
		return addDirectDebitPositions;
	}

	@Override
	public boolean hasNextEntry() {
		return dueDirectDebitPositions.hasNext();
	}

	//with cont_cte as (select CONT.id_
	//                  from FL_CONTRACT as CONT
	//                           left join fl_paymentmethodapplication AS PAY on PAY.contract_contract_ = CONT.id_
	//                  where PAY.paymenmethod_paymenmethod_ = 20
	//                    and (PAY.periodend_ is null or PAY.periodend_ >= current_timestamp)),
	//     acc_cte as (select ACC.id_, ACC.debit_, ACC.credit_, OWN.contract_contract_
	//                 from fl_financialaccount as ACC
	//                          left join fl_financiaaccountownershi as OWN on OWN.financaccoun_account_ = ACC.id_
	//                 where ACC.type_ = 1
	//                   and ((ACC.credit_ is null and ACC.debit_ is not null) or (ACC.credit_ is not null and ACC.debit_ is null) or (ACC.debit_ != ACC.credit_))
	//                 group by ACC.id_, OWN.contract_contract_
	//                 ),
	//     entry_cte as (select ENTRY.accountingdocument_,
	//                          ENTRY.subject_,
	//                          ENTRY.value_,
	//                          ENTRY.coveredvaluedebit_  as covered,
	//                          ENTRY.financaccoun_debit_ as finacc,
	//                          ENTRY.id_
	//                   from FL_ENTRY as ENTRY
	//                            left join fl_financialaccount as DACC on DACC.id_ = ENTRY.financaccoun_debit_
	//                            left join fl_financialaccount as CACC on CACC.id_ = ENTRY.financaccoun_credit_
	//                   where DACC.type_ = 1
	//                     and CACC.type_ = 2
	//                     and ENTRY.stack_ is null
	//                     and ENTRY.tax_ is null
	//                     and ENTRY.creationDate_ > '2023-09-10'
	//                     and ENTRY.coveredvaluedebit_ != ENTRY.value_
	//                   UNION
	//                   select ENTRY.accountingdocument_,
	//                          ENTRY.subject_,
	//                          ENTRY.value_,
	//                          ENTRY.coveredvaluedebit_  as covered,
	//                          ENTRY.financaccoun_debit_ as finacc,
	//                          ENTRY.id_
	//                   from FL_ENTRY as ENTRY
	//                            left join fl_financialaccount as DACC on DACC.id_ = ENTRY.financaccoun_debit_
	//                            left join fl_financialaccount as CACC on CACC.id_ = ENTRY.financaccoun_credit_
	//                   where DACC.type_ = 1
	//                     and CACC.type_ = 5
	//                     and ENTRY.stack_ is null
	//                     and ENTRY.tax_ is null
	//                     and ENTRY.creationDate_ > '2023-09-10'
	//                     and ENTRY.coveredvaluedebit_ != ENTRY.value_
	//                   union
	//                   select ENTRY.accountingdocument_,
	//                          ENTRY.subject_,
	//                          ENTRY.value_,
	//                          ENTRY.coveredvaluecredit_  as covered,
	//                          ENTRY.financaccoun_credit_ as finacc,
	//                          ENTRY.id_
	//                   from FL_ENTRY as ENTRY
	//                            left join fl_financialaccount as DACC on DACC.id_ = ENTRY.financaccoun_debit_
	//                            left join fl_financialaccount as CACC on CACC.id_ = ENTRY.financaccoun_credit_
	//                   where DACC.type_ = 5
	//                     and CACC.type_ = 1
	//                     and ENTRY.stack_ is null
	//                     and ENTRY.tax_ is null
	//                     and ENTRY.creationDate_ > '2023-09-10'
	//                     and ENTRY.coveredvaluecredit_ != ENTRY.value_)
	//select distinct  cont_cte.id_ as contract, ACC.id_ as debitAccount, ACC.debit_, ACC.credit_, entry.id_ as entryid, entry.subject_ as entrysubject, entry.accountingdocument_ as entryaccountingdocument, DOC.type_ as accountingdocumenttype, DOC.subject_ as accountingdocumentsubject
	//	from acc_cte as ACC
	//         inner join cont_cte on ACC.contract_contract_ = cont_cte.id_
	//         inner join entry_cte as entry on entry.finacc = ACC.id_
	//         inner join FL_accountingdocument as DOC on DOC.id_ = entry.accountingdocument_
	//         left join fl_direcdebitpositentrymappi as MAPP on entry.id_ = mapp.netentry_
	//         left join fl_directdebitposition as dpo on mapp.directdebitposition_ = dpo .id_
	//	where dpo.id_ is null or (dpo.id_ is not null and dpo.state_ not in ('OPEN', 'ERROR'));
	private String getSQL() {
		String sql =
				"with " + WITH_CONTRACT_ALIAS + " as ("
						+ "select CONT." + ContractDAO.column_id + " from " + ContractDAO.TABLE_NAME + " as CONT "
						+ "left join " + PaymentMethodApplicationDAO.TABLE_NAME + " AS PAY on PAY."
						+ PaymentMethodApplicationDAO.column_contract + " = CONT." + ContractDAO.column_id
						+ " where PAY." + PaymentMethodApplicationDAO.column_paymentMethod + " = 20 "
						+ "and (PAY." + PaymentMethodApplicationDAO.column_periodEnd + " is null or PAY."
						+ PaymentMethodApplicationDAO.column_periodEnd + " >= current_timestamp) "
						+ ")"
						+ ", "
						+ WITH_ACCOUNT_ALIAS + " as ("
						+ "select ACC." + FinancialAccountDAO.column_id + ", OWN." + FinancialAccountOwnershipDAO.column_contract + " from "
						+ FinancialAccountDAO.TABLE_NAME + " as ACC "
						+ "left join " + FinancialAccountOwnershipDAO.TABLE_NAME + " as OWN on OWN."
						+ FinancialAccountOwnershipDAO.column_account + " = ACC." + FinancialAccountDAO.column_id
						+ " where ACC." + FinancialAccountDAO.column_Type + " = 1 "
						+ "and ((ACC." + FinancialAccountDAO.column_credit + " is null and ACC." + FinancialAccountDAO.column_debit
						+ " is not null) or "
						+ "(ACC." + FinancialAccountDAO.column_credit + " is not null and ACC." + FinancialAccountDAO.column_debit
						+ " is null) or (ACC." + FinancialAccountDAO.column_debit + " != ACC." + FinancialAccountDAO.column_credit + "))"
						+ "group by ACC." + FinancialAccountDAO.column_id + " , OWN." + FinancialAccountOwnershipDAO.column_contract
						+ ")"
						+ ", "
						+ WITH_ENTRY_ALIAS + " as ("
						+ "select ENTRY." + EntryDAO.column_accountingDocument + ", ENTRY." + EntryDAO.column_id + ", ENTRY."
						+ EntryDAO.column_subject + ", ENTRY." + EntryDAO.column_value
						+ ", ENTRY." + EntryDAO.column_coveredValueDebit + " as covered, ENTRY." + EntryDAO.column_debit + " as finacc "
						+ "from " + EntryDAO.TABLE_NAME + " as ENTRY "
						+ "left join " + FinancialAccountDAO.TABLE_NAME + " as DACC on DACC." + FinancialAccountDAO.column_id + " = ENTRY."
						+ EntryDAO.column_debit
						+ " left join " + FinancialAccountDAO.TABLE_NAME + " as CACC on CACC." + FinancialAccountDAO.column_id + " = ENTRY."
						+ EntryDAO.column_credit
						+ " where "
						+ "DACC." + FinancialAccountDAO.column_Type + " = 1 "
						+ "and CACC." + FinancialAccountDAO.column_Type + " = 2 "
						+ "and ENTRY." + EntryDAO.column_Stack + " is null "
						+ "and ENTRY." + EntryDAO.column_tax + " is null "
						+ "and ENTRY." + EntryDAO.column_creationDate + " > '2023-09-10' "
						+ "and ENTRY." + EntryDAO.column_coveredValueDebit + " != ENTRY." + EntryDAO.column_value
						+ " UNION "
						+ "select ENTRY." + EntryDAO.column_accountingDocument + ", ENTRY." + EntryDAO.column_id + ", ENTRY."
						+ EntryDAO.column_subject + ", ENTRY." + EntryDAO.column_value
						+ ", ENTRY." + EntryDAO.column_coveredValueDebit + " as covered, ENTRY." + EntryDAO.column_debit + " as finacc "
						+ "from " + EntryDAO.TABLE_NAME + " as ENTRY "
						+ "left join " + FinancialAccountDAO.TABLE_NAME + " as DACC on DACC." + FinancialAccountDAO.column_id + " = ENTRY."
						+ EntryDAO.column_debit
						+ " left join " + FinancialAccountDAO.TABLE_NAME + " as CACC on CACC." + FinancialAccountDAO.column_id + " = ENTRY."
						+ EntryDAO.column_credit
						+ " where "
						+ "DACC." + FinancialAccountDAO.column_Type + " = 1 "
						+ "and CACC." + FinancialAccountDAO.column_Type + " = 5 "
						+ "and ENTRY." + EntryDAO.column_Stack + " is null "
						+ "and ENTRY." + EntryDAO.column_tax + " is null "
						+ "and ENTRY." + EntryDAO.column_creationDate + " > '2023-09-10' "
						+ "and ENTRY." + EntryDAO.column_coveredValueDebit + " != ENTRY." + EntryDAO.column_value
						+ " union "
						+ "select ENTRY." + EntryDAO.column_accountingDocument + ", ENTRY." + EntryDAO.column_id + ", ENTRY."
						+ EntryDAO.column_subject + ", ENTRY." + EntryDAO.column_value
						+ ", ENTRY." + EntryDAO.column_coveredValueCredit + " as covered, ENTRY." + EntryDAO.column_credit + " as finacc "
						+ "from " + EntryDAO.TABLE_NAME + " as ENTRY "
						+ "left join " + FinancialAccountDAO.TABLE_NAME + " as DACC on DACC." + FinancialAccountDAO.column_id + " = ENTRY."
						+ EntryDAO.column_debit
						+ " left join " + FinancialAccountDAO.TABLE_NAME + " as CACC on CACC." + FinancialAccountDAO.column_id + " = ENTRY."
						+ EntryDAO.column_credit
						+ " where "
						+ "DACC." + FinancialAccountDAO.column_Type + " = 5 "
						+ "and CACC." + FinancialAccountDAO.column_Type + " = 1 "
						+ "and ENTRY." + EntryDAO.column_Stack + " is null "
						+ "and ENTRY." + EntryDAO.column_tax + " is null "
						+ "and ENTRY." + EntryDAO.column_creationDate + " > '2023-09-10' "
						+ "and ENTRY." + EntryDAO.column_coveredValueCredit + " != ENTRY." + EntryDAO.column_value
						+ ") "
						+ "select distinct " + WITH_CONTRACT_ALIAS + "." + ContractDAO.column_id + " as " + CONTRACT_COLUMN_ALIAS + ", ACC."
						+ FinancialAccountDAO.column_id + " as " + FINANCIAL_ACCOUNT_DEBITOR_COLUMN_ALIAS
						+ ", entry." + EntryDAO.column_subject + " as " + ENTRY_SUBJECT_COLUMN_ALIAS + ", entry."
						+ EntryDAO.column_accountingDocument + " as "
						+ ENTRY_ACCOUNTINGDOCUMENT_COLUMN_ALIAS + ", entry." + EntryDAO.column_id + " as " + ENTRY_ID_COLUMN_ALIAS
						+ ",DOC." + AccountingDocumentDAO.column_type + " as " + ACCOUNTINGDOCUMENT_TYPE_COLUMN_ALIAS + ", DOC."
						+ AccountingDocumentDAO.column_subject + " as "
						+ ACCOUNTINGDOCUMENT_SUBJECT_COLUMN_ALIAS
						+ " from " + WITH_ACCOUNT_ALIAS + " as ACC "
						+ "inner join " + WITH_CONTRACT_ALIAS + " on ACC." + FinancialAccountOwnershipDAO.column_contract + " = "
						+ WITH_CONTRACT_ALIAS + "." + ContractDAO.column_id
						+ " inner join " + WITH_ENTRY_ALIAS + " as entry on entry.finacc = ACC." + FinancialAccountDAO.column_id
						+ " inner join " + AccountingDocumentDAO.TABLE_NAME + " as DOC on DOC." + AccountingDocumentDAO.column_id
						+ " = entry." + EntryDAO.column_accountingDocument
						+ " left join " + DirectDebitPositionEntryMappingDAO.TABLE_NAME + " as ddpem on entry." + EntryDAO.column_id
						+ " = ddpem." + DirectDebitPositionEntryMappingDAO.column_netEntry
						+ " left join " + DirectDebitPositionDAO.TABLE_NAME + " as dpo on ddpem."
						+ DirectDebitPositionEntryMappingDAO.column_directDebitPosition + " = dpo." + DirectDebitPositionDAO.column_id
						+ " where dpo." + DirectDebitPositionDAO.column_id + " is null or (dpo." + DirectDebitPositionDAO.column_id
						+ " is not null and dpo." + DirectDebitPositionDAO.column_state + " not in ('"
						+ DirectDebitPositionStateEnum.OPEN.getDataBaseKey() + "', '"
						+ DirectDebitPositionStateEnum.ERROR.getDataBaseKey() + "'))"
						+ ";";
		return sql;
	}

	private class SqlReportElement {
		private Long contract;
		private Long debitor;
		private Long entry;
		private Long accountingDocument;
		private String entrySubject;
		private String accountingDocumentType;
		private String accountingDocumentSubject;

		private SqlReportElement(Long contract, Long debitor, Long entry, Long accountingDocument, String entrySubject,
				String accountingDocumentType, String accountingDocumentSubject) {
			this.contract = contract;
			this.debitor = debitor;
			this.entry = entry;
			this.accountingDocument = accountingDocument;
			this.entrySubject = entrySubject;
			this.accountingDocumentType = accountingDocumentType;
			this.accountingDocumentSubject = accountingDocumentSubject;
		}

		private SqlReportElement() {

		}

		private Long getContract() {
			return contract;
		}

		private void setContract(Long contract) {
			this.contract = contract;
		}

		private SqlReportElement withContract(Long contract) {
			this.contract = contract;
			return this;
		}

		private Long getDebitor() {
			return debitor;
		}

		private void setDebitor(Long debitor) {
			this.debitor = debitor;
		}

		private SqlReportElement withDebitor(Long debitor) {
			this.debitor = debitor;
			return this;
		}

		private Long getEntry() {
			return entry;
		}

		private void setEntry(Long entry) {
			this.entry = entry;
		}

		private SqlReportElement withEntry(Long entry) {
			this.entry = entry;
			return this;
		}

		private Long getAccountingDocument() {
			return accountingDocument;
		}

		private void setAccountingDocument(Long accountingDocument) {
			this.accountingDocument = accountingDocument;
		}

		private SqlReportElement withAccountingDocument(Long accountingDocument) {
			this.accountingDocument = accountingDocument;
			return this;
		}

		private String getEntrySubject() {
			return entrySubject;
		}

		private void setEntrySubject(String entrySubject) {
			this.entrySubject = entrySubject;
		}

		private SqlReportElement withEntrySubject(String entrySubject) {
			this.entrySubject = entrySubject;
			return this;
		}

		private String getAccountingDocumentType() {
			return accountingDocumentType;
		}

		private void setAccountingDocumentType(String accountingDocumentType) {
			this.accountingDocumentType = accountingDocumentType;
		}

		private SqlReportElement withAccountingDocumentType(String accountingDocumentType) {
			this.accountingDocumentType = accountingDocumentType;
			return this;
		}

		private String getAccountingDocumentSubject() {
			return accountingDocumentSubject;
		}

		private void setAccountingDocumentSubject(String accountingDocumentSubject) {
			this.accountingDocumentSubject = accountingDocumentSubject;
		}

		private SqlReportElement withAccountingDocumentSubject(String accountingDocumentSubject) {
			this.accountingDocumentSubject = accountingDocumentSubject;
			return this;
		}
	}
}
