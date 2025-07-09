/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.persistence.myBusinessSupplier.dao;

import static com.nextlevel.fastlane.myBusinessSupplier.AccountingCharacteristicTool.NONE;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.nextlevel.fastlane.myBusinessSupplier.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import com.nextlevel.fastlane.core.exceptions.PersistenceHandlerException;
import com.nextlevel.fastlane.core.persistence.dao.TypeOfOperation;
import com.nextlevel.fastlane.myBusinessSupplier.block.BlockLevelEnum;
import com.nextlevel.fastlane.myBusinessSupplier.block.BlockTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.fastlane.myBusinessSupplier.contract.InvoiceTriggerEnum;
import com.nextlevel.fastlane.myBusinessSupplier.enums.ChangeTriggerQueueTypeOfEntityEnum;
import com.nextlevel.fastlane.myBusinessSupplier.enums.ContractTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.enums.SelfReadingTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.tool.ChangeTriggerQueueTool;
import com.nextlevel.fastlane.persistence.financial.dao.AccountingCharacteristicDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.ContractDAOHook;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.ContractPVO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.PointOfDeliveryPVO;
import com.nextlevel.myBusinessSupplier.api.dto.ContractMetaDataDTO;
import com.nextlevel.myBusinessSupplier.constants.SectionEnum;
import com.nextlevel.platform.connection.Command;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.connection.DatabaseTool;
import com.nextlevel.platform.connection.sql.QueryBuilder;
import com.nextlevel.platform.connection.sql.QueryBuilder.Operator;
import com.nextlevel.platform.date.DateTool;
import com.nextlevel.platform.exception.ExceptionTool;
import com.nextlevel.platform.financial.Id;

public class ContractDAO extends ContractDAOBase {

	private static final LazyInitializer<List<ContractDAOHook>> hooks
			= new LazyInitializer<List<ContractDAOHook>>() {
		@Override
		protected List<ContractDAOHook> initialize() {
			final ServiceLoader<ContractDAOHook> foundHooks = ServiceLoader.load(ContractDAOHook.class);
			return StreamSupport.stream(foundHooks.spliterator(), false).collect(Collectors.toList());
		}
	};

	private static void setDefaultInvoicingInformationInterval(ContractPVO contractPVO) {

		if (!ContractTypeEnum.isEqualTo(contractPVO.getType(), ContractTypeEnum.RLM)
				&& contractPVO.getInvoicingInformationInterval() == null) {
			if (ContractCommunicationTypeEnum.LETTER.name().equals(contractPVO.getCommunicationType())) {
				contractPVO.setInvoicingInformationInterval(InvoicingInformationIntervalEnum.NONE.name());
			} else if (ContractCommunicationTypeEnum.EMAIL.name().equals(contractPVO.getCommunicationType())) {
				contractPVO.setInvoicingInformationInterval(InvoicingInformationIntervalEnum.MONTHLY.name());
			}
		}

	}

	@Override
	protected void onInsert(ContractPVO contractPVO, Connection con) throws SQLException {
		adaptNextInvoiceDate(contractPVO);
		ensureCommunicationTypeValidity(contractPVO);
		setDefaultInvoicingInformationInterval(contractPVO);
		setDefaultInvoiceTrigger(contractPVO);
		setAccountingCharacteristicFallback(contractPVO);
		setDefaultSelfReadingType(contractPVO);
		addContractNumberToChangeTriggerQueue(contractPVO, TypeOfOperation.INSERT, ChangeTriggerQueueTypeOfEntityEnum.CONTRACT);
		AccountingCharacteristicSetup.setAccountingCharacteristic(contractPVO);
		try {
			hooks.get().forEach((ConsumerWithException<ContractDAOHook>) h -> h.onInsert(contractPVO));
		} catch (Exception e) {
			logger.error("Error during execution of ContractDAOHook#onInsert.", e);
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	@Override
	protected void onUpdate(ContractPVO contractPVO, Connection con) throws SQLException {
		adaptNextInvoiceDate(contractPVO);
		ensureCommunicationTypeValidity(contractPVO);
		setDefaultInvoicingInformationInterval(contractPVO);
		setDefaultInvoiceTrigger(contractPVO);
		setAccountingCharacteristicFallback(contractPVO);
		validateAccountingCharacteristic(contractPVO);
		setDefaultSelfReadingType(contractPVO);
		addContractNumberToChangeTriggerQueue(contractPVO, TypeOfOperation.UPDATE, ChangeTriggerQueueTypeOfEntityEnum.CONTRACT);
		AccountingCharacteristicSetup.setAccountingCharacteristic(contractPVO);
		try {
			hooks.get().forEach((ConsumerWithException<ContractDAOHook>) h -> h.onUpdate(contractPVO));
		} catch (Exception e) {
			logger.error("Error during execution of ContractDAOHook#onUpdate.", e);
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	private void validateAccountingCharacteristic(final ContractPVO contract) {
		throwExceptionIfNone(contract.getContractNr(), contract.getAccountingCharacteristic());
	}

	private static void throwExceptionIfNone(final String contractNumber, final Long characteristicId) {
		Validate.isTrue(!NONE.getId().equals(characteristicId),
				contractNumber + ": AccountingCharacteristic NONE is not allowed for contracts, only for financialAccounts.");
	}

	private void setDefaultInvoiceTrigger(ContractPVO contractPVO) {
		if (StringUtils.isBlank(contractPVO.getInvoiceTrigger())) {
			final InvoiceTriggerEnum defaultTrigger = InvoiceTriggerEnum.getDefaultTrigger();
			contractPVO.setInvoiceTrigger(defaultTrigger.name());
		}
	}

	private void setDefaultSelfReadingType(ContractPVO contractPVO) {
		if (StringUtils.isBlank(contractPVO.getSelfReadingType())) {
			contractPVO.setSelfReadingType(SelfReadingTypeEnum.getDefaultAsString());
		}
	}

	public static void setAccountingCharacteristicFallback(Contract contract) {
		if (contract.getAccountingCharacteristic() == null) {
			contract.setAccountingCharacteristic(AccountingCharacteristicDAO.DEFAULT);
		} else {
			throwExceptionIfNone(contract.getContractNr(), contract.getAccountingCharacteristic().getId());
		}
	}

	public static void setAccountingCharacteristicFallback(ContractPVO contract) {
		if (Id.isNotSet(contract.getAccountingCharacteristic())) {
			contract.setAccountingCharacteristic(AccountingCharacteristicDAO.DEFAULT.getId());
		} else {
			throwExceptionIfNone(contract.getContractNr(), contract.getAccountingCharacteristic());
		}
	}

	private void adaptNextInvoiceDate(ContractPVO contractPVO) {
		if (contractPVO.getNextInvoiceDate() != null) {
			contractPVO.setNextInvoiceDate(
					new Timestamp(DateTool.getStartOfDay(new Date(contractPVO.getNextInvoiceDate().getTime())).getTime()));
		}

	}

	public List<Contract> getContracts(final List<String> contractNumbers) throws Exception {
		if (contractNumbers == null || contractNumbers.isEmpty()) {
			return Collections.emptyList();
		}
		return CommandProcessor.process(connection -> {
			String query = "SELECT * FROM " + TABLE_NAME + " ";
			final QueryBuilder queryBuilder = new QueryBuilder(query, Operator.AND);

			final QueryBuilder contractQuery = new QueryBuilder("", Operator.AND);
			final String contractColumn = TABLE_NAME + "." + column_contractNr;
			contractQuery.addCaseSensitiveInListCondition(contractNumbers, contractColumn);
			queryBuilder.addQueryBuilderCondition(contractQuery);

			PreparedStatement pstmt = queryBuilder.build(connection);

			List<Contract> result = new ArrayList<>();
			ResultSet res = pstmt.executeQuery();
			while (res.next()) {
				ContractPVO contract = buildContractPVO(res);
				result.add(PersistenceHandler.getInstance().getContractFromPVO(contract));
			}
			return result;
		});
	}

	public int getOccurencePerYear(int desiredAdvancePayEveryNMonth) {
		if (12 % desiredAdvancePayEveryNMonth > 0) {
			logger.error("Desired advance pay interval of " + desiredAdvancePayEveryNMonth + " does not fit into one year.");
		}

		return 12 / desiredAdvancePayEveryNMonth - 1;
	}

	/**
	 * @param now the first of the next month is checked
	 */
	public List<String> listMeteringPointsWithoutInventoryListAcknowledge(final Calendar now) throws Exception {
		return CommandProcessor.process(connection -> {
			List<String> missingMeteringPoints = new ArrayList<>();

			Calendar startOfMonthCal = (Calendar) now.clone();
			startOfMonthCal.set(Calendar.DAY_OF_MONTH, 1);

			startOfMonthCal.add(Calendar.MONTH, 1);// check next month the 1st

			DateFormat format = new SimpleDateFormat("yyyyMM");

			int lastInventoryListMonth = Integer.parseInt(format.format(startOfMonthCal.getTime()));

			java.util.Date startOfMonth = DateTool.getStartOfDay(startOfMonthCal.getTime());

			String selectStatement = "SELECT " + PointOfDeliveryDAOBase.column_meteringPoint + " FROM " + ContractDAO.TABLE_NAME
					+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " ON " + PointOfDeliveryDAO.TABLE_NAME + "."
					+ PointOfDeliveryDAOBase.column_id + " = " + ContractDAO.TABLE_NAME + "." + column_PointOfDelivery
					+ " WHERE (" + ContractDAO.TABLE_NAME + "." + ContractDAO.column_lastInventoryListMonth + " is null"
					+ " OR " + ContractDAO.TABLE_NAME + "." + ContractDAO.column_lastInventoryListMonth + " != ? " + ")"
					+ " AND " + PointOfDeliveryDAOBase.column_meteringPoint + " is not null"
					+ " AND " + ContractDAO.TABLE_NAME + "." + ContractDAO.column_periodStart + " < ?"
					+ " AND ( " + ContractDAO.TABLE_NAME + "." + ContractDAO.column_periodEnd + " is null"
					+ " OR " + ContractDAO.TABLE_NAME + "." + ContractDAO.column_periodEnd + " > ?)"
					+ " AND " + ContractDAO.TABLE_NAME + "." + ContractDAO.column_State + " = '" + ContractStateEnum.GPKE_APPROVED + "'"
					+ " AND " + ContractDAO.TABLE_NAME + "." + ContractDAO.column_section + " = ? ";

			ResultSet rs;
			final PreparedStatement pstmt = connection.prepareStatement(selectStatement);
			pstmt.setInt(1, lastInventoryListMonth);
			pstmt.setTimestamp(2, new Timestamp(startOfMonth.getTime()));
			pstmt.setTimestamp(3, new Timestamp(startOfMonth.getTime()));
			pstmt.setString(4, SectionEnum.Gas.name());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				missingMeteringPoints.add(new PointOfDeliveryDAO().getMeteringPoint(rs));
			}

			return missingMeteringPoints;
		});
	}

	/**
	 * @param invoicingIntervals "1" or "1,3,6,12"<
	 */
	public List<Contract> listAllPendingForInvoice(
			final long nextInvoiceDateBeforeInMilliseconds,
			final List<String> invoicingIntervals) throws Exception {
		return listAllPendingForInvoice(nextInvoiceDateBeforeInMilliseconds, invoicingIntervals, Optional.empty());
	}

	/**
	 * @param invoicingIntervals "1" or "1,3,6,12"<
	 * @param triggerTypeFilter  Can be used to filter contracts based on {@link InvoiceTriggerEnum}.
	 */
	public List<Contract> listAllPendingForInvoice(
			final long nextInvoiceDateBeforeInMilliseconds,
			final List<String> invoicingIntervals,
			final Optional<InvoiceTriggerEnum> triggerTypeFilter)
			throws Exception {
		String __ = "\n";
		String sql = "SELECT * FROM " + TABLE_NAME
				+ __ + "LEFT JOIN " + DebitorInvoiceDAO.TABLE_NAME + " AS INVOICE ON INVOICE." + DebitorInvoiceDAO.column_Contract + " = "
				+ TABLE_NAME + "." + column_id
				+ __ + " AND INVOICE." + DebitorInvoiceDAO.column_periodEnd + " >= " + column_nextInvoiceDate
				+ __ + " AND INVOICE." + DebitorInvoiceDAO.column_Type + " NOT IN ('"
				+ InvoiceTypeEnum.INVOICING_INFORMATION.getInvoiceTypeName()
				+ "', '" + InvoiceTypeEnum.PROJECTION.getInvoiceTypeName() + "') "
				+ __ + "LEFT JOIN " + ContractPartnerDAO.TABLE_NAME + " AS contractPartner ON " + TABLE_NAME + "." + column_id
				+ " = contractPartner." + ContractPartnerDAO.column_Contract
				+ __ + " AND contractPartner." + ContractPartnerDAO.column_Role + " = " + BusinessPartnerRoleEnum.Customer.getId()
				+ __ + "LEFT JOIN " + BlockDAO.TABLE_NAME + " AS blockCon ON " + TABLE_NAME + "." + column_id + " = blockCon."
				+ BlockDAO.column_contract
				+ __ + " AND blockCon." + BlockDAO.column_type + " = ?"
				+ __ + " AND blockCon." + BlockDAO.column_level + " = ?"
				+ __ + " AND blockCon." + BlockDAO.column_periodEnd + " >= ?"
				+ __ + "LEFT JOIN " + BlockDAO.TABLE_NAME + " AS blockBup ON contractPartner." + ContractPartnerDAO.column_BusinessPartner
				+ " = blockBup." + BlockDAO.column_businessPartner
				+ __ + " AND blockBup." + BlockDAO.column_type + " = ?"
				+ __ + " AND blockBup." + BlockDAO.column_level + " = ?"
				+ __ + " AND blockBup." + BlockDAO.column_periodEnd + " >= ?"
				+ __ + "WHERE " + ContractDAO.column_nextInvoiceDate + " < ?"
				+ __ + " AND INVOICE." + DebitorInvoiceDAOBase.column_id + " IS NULL"
				+ __ + " AND blockCon." + BlockDAO.column_id + " IS NULL"
				+ __ + " AND blockBup." + BlockDAO.column_id + " IS NULL"
				+ __ + " AND " + ContractDAO.column_invoiceTrigger + " != '" + InvoiceTriggerEnum.MANUAL.name() + "'";
		// default
		if (invoicingIntervals.size() == 1) {
			sql = sql + __ + " AND " + ContractDAO.column_invoicingInterval + " = ?";
		}
		// Stichtagsabrechnung
		else {
			// "?,?,?,..."
			final String invoicingIntervalPlaceholder
					= String.join(",", Collections.nCopies(invoicingIntervals.size(), "?"));
			sql = sql + __ + " AND " + ContractDAO.column_invoicingInterval + " IN (" + invoicingIntervalPlaceholder + ")";
		}

		if (triggerTypeFilter.isPresent()) {
			sql = sql + __ + " AND " + ContractDAO.column_invoiceTrigger + " = '" + triggerTypeFilter.get().name() + "'";
		}

		String finalSql = sql;
		return CommandProcessor.process((Command<List<Contract>, Exception>) connection -> {
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			List<Contract> contracts = new ArrayList<>();

			int parameterIndex = 1;
			try {
				statement = connection.prepareStatement(finalSql);
				statement.setString(parameterIndex++, BlockTypeEnum.DebitorInvoiceBlock.name());
				statement.setString(parameterIndex++, BlockLevelEnum.ContractLevel.name());
				statement.setTimestamp(parameterIndex++, new Timestamp(DateTool.getStartOfToday().getTime()));
				statement.setString(parameterIndex++, BlockTypeEnum.DebitorInvoiceBlock.name());
				statement.setString(parameterIndex++, BlockLevelEnum.BusinessPartnerLevel.name());
				statement.setTimestamp(parameterIndex++, new Timestamp(DateTool.getStartOfToday().getTime()));
				statement.setTimestamp(parameterIndex++, new Timestamp(nextInvoiceDateBeforeInMilliseconds));
				for (String invoicingInterval : invoicingIntervals) {
					statement.setInt(parameterIndex++, Integer.parseInt(invoicingInterval));
				}

				resultSet = statement.executeQuery();

				while (resultSet.next()) {
					// contractIds.add(rs.getLong(1));
					Contract contract = convertToContract(resultSet);
					contracts.add(contract);
				}
			} catch (Exception e) {
				DatabaseTool.closeQuietly(resultSet, statement, logger);
				throw ExceptionTool.asRuntimeException(e);
			}
			return contracts;
		});
	}

	private Contract convertToContract(ResultSet rs) throws SQLException, IOException, PersistenceHandlerException {
		PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		ContractPVO contractPvo = buildContractPVO(rs);
		return persistenceHandler.getContractFromPVO(contractPvo);
	}

	public ContractPVO loadContractByContractNumber(String contractNumber) throws ClassNotFoundException, SQLException, IOException {
		Validate.validState(contractNumber != null, "parameter contractNumber must not be null");
		ContractPVO contract = new ContractPVO();
		contract.setContractNr(contractNumber);
		List<ContractPVO> contracts = list(contract);
		if (contracts == null || contracts.size() == 0) {
			logger.info("no contract found for contractNumber " + contractNumber);
			return null;
		} else if (contracts.size() > 1) {
			logger.info("more than one contract found for contractNumber " + contractNumber);
			return null;
		}
		return contracts.get(0);
	}

	public static List<ContractMetaDataDTO> listContractsWithData() {
		try {
			return CommandProcessor.process(connection -> {
				final String contractNumber = TABLE_NAME + "." + column_contractNr;
				final String periodStart = TABLE_NAME + "." + column_periodStart;
				final String periodEnd = TABLE_NAME + "." + column_periodEnd;

				final String malo = "malo";
				final String maloField = malo + "." + PointOfDeliveryDAO.column_meteringPoint;
				final String melo = "melo";
				final String meloField = melo + "." + PointOfDeliveryDAO.column_meteringPoint;

				final String meterNumber = MeterDAO.TABLE_NAME + "." + MeterDAO.column_number;

				final String businessPartnerNumber =
						BusinessPartnerDAO.TABLE_NAME + "." + BusinessPartnerDAO.column_businessPartnerNumber;
				final String firstName = BusinessPartnerDAO.TABLE_NAME + "." + BusinessPartnerDAO.column_firstName;
				final String lastName = BusinessPartnerDAO.TABLE_NAME + "." + BusinessPartnerDAO.column_name;

				final String street = AddressDAO.TABLE_NAME + "." + AddressDAO.column_street;
				final String houseNumber = AddressDAO.TABLE_NAME + "." + AddressDAO.column_housenumber;
				final String houseNumberAddition = AddressDAO.TABLE_NAME + "." + AddressDAO.column_housenumberAddition;
				final String postalCode = AddressDAO.TABLE_NAME + "." + AddressDAO.column_postalCode;
				final String city = AddressDAO.TABLE_NAME + "." + AddressDAO.column_city;
				final String cityAddition = AddressDAO.TABLE_NAME + "." + AddressDAO.column_cityAddition;

				//@formatter:off
				final String sql =
						"select " +
							contractNumber + ", " +
							periodStart + ", " +
							periodEnd + ", " +
							maloField + " as " + malo + ", " +
							meloField + " as " + melo + ", " +
							meterNumber + ", " +
							businessPartnerNumber + ", " +
							firstName + ", " +
							lastName + ", " +
							street + ", " +
							houseNumber + ", " +
							houseNumberAddition + ", " +
							postalCode + ", " +
							city + ", " +
							cityAddition +

						" from " + TABLE_NAME +

						" left join " + PointOfDeliveryDAOBase.TABLE_NAME + " AS malo on " +
							TABLE_NAME + "." + column_PointOfDelivery + " = " +
							"malo." + PointOfDeliveryDAOBase.column_id +

						" left join " + PointOfDeliveryDAOBase.TABLE_NAME + " AS melo on " +
							"melo." + PointOfDeliveryDAO.column_parent + " = " +
							"malo." + PointOfDeliveryDAOBase.column_id +

						" left join " + AddressDAOBase.TABLE_NAME + " on " +
							"malo." + PointOfDeliveryDAOBase.column_address + " = " +
							AddressDAOBase.TABLE_NAME + "." + AddressDAOBase.column_id +

						" left join " + MeterDAO.TABLE_NAME + " on " +
							"melo." + PointOfDeliveryDAO.column_id + " = " +
							MeterDAO.TABLE_NAME + "." + MeterDAO.column_PointOfDelivery +

						" left join " + ContractAccountDAOBase.TABLE_NAME + " on " +
							ContractDAOBase.TABLE_NAME + "." + ContractDAO.column_ContractAccount + " = " +
							ContractAccountDAOBase.TABLE_NAME + "." + ContractAccountDAOBase.column_id +

						" left join " + BusinessPartnerDAOBase.TABLE_NAME + " on " +
							ContractAccountDAO.TABLE_NAME + "." + ContractAccountDAO.column_owner + " = " +
							BusinessPartnerDAOBase.TABLE_NAME + "." + BusinessPartnerDAOBase.column_id + ";";
				//@formatter:on

				final PreparedStatement pstmt = connection.prepareStatement(sql);
				final ResultSet resultSet = pstmt.executeQuery();

				final List<ContractMetaDataDTO> result = new ArrayList<>();
				while (resultSet.next()) {
					final ContractMetaDataDTO data = new ContractMetaDataDTO();
					data.setBusinessPartnerNumber(resultSet.getString(getColumnName(businessPartnerNumber)));
					data.setContractNumber(resultSet.getString(getColumnName(contractNumber)));
					data.setPeriodStart(resultSet.getDate(getColumnName(periodStart)));
					data.setPeriodEnd(resultSet.getDate(getColumnName(periodEnd)));
					data.setMalo(resultSet.getString(malo));
					data.setMelo(resultSet.getString(melo));
					data.setMeterNumber(resultSet.getString(getColumnName(meterNumber)));
					data.setFirstName(resultSet.getString(getColumnName(firstName)));
					data.setLastName(resultSet.getString(getColumnName(lastName)));
					data.setStreet(resultSet.getString(getColumnName(street)));
					data.setHouseNumber(join(resultSet.getString(getColumnName(houseNumber)), " ",
							resultSet.getString(getColumnName(houseNumberAddition))).trim());
					data.setPostalCode(resultSet.getString(getColumnName(postalCode)));
					data.setCity(resultSet.getString(getColumnName(city)));
					data.setCityAdditional(resultSet.getString(getColumnName(cityAddition)));

					result.add(data);

				}

				return result;
			});
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static String getColumnName(String name) {
		return name.substring(name.indexOf('.') + 1); //fl_businesspartner.number_ => number_
	}

	/**
	 * Converts the contract's {@link Contract#getCommunicationType()} result
	 * into a {@link ContractCommunicationTypeEnum} value,
	 * generously accepting an empty String as a <code>null</code>.
	 *
	 * @param contract optional
	 * @return communicaton type of contract
	 */
	public static Optional<ContractCommunicationTypeEnum> retrieveCommunicationTypeFromContract(final Contract contract) {
		if (contract == null) {
			return Optional.empty();
		}

		final String communicationTypeText = contract.getCommunicationType();
		if (StringUtils.isEmpty(communicationTypeText)) {
			return Optional.empty();
		}

		try {
			return Optional.of(ContractCommunicationTypeEnum.valueOf(communicationTypeText));
		} catch (Exception exce) {
			throw new IllegalStateException(
					MessageFormat.format(
							"Unknown communicationType text \"{0}\" in Contract (ID={1})!",
							communicationTypeText, contract.getId()),
					exce);
		}
	}

	private void ensureCommunicationTypeValidity(ContractPVO contractPVO) {
		final String commTypeTextDefault = ContractCommunicationTypeEnum.EMAIL.name();

		final String commTypeText = contractPVO.getCommunicationType();

		try {
			ContractCommunicationTypeEnum commType = ContractCommunicationTypeEnum.valueOf(commTypeText);
		} catch (NullPointerException nullPointerExce) {
			logger.warn(MessageFormat.format("CommunicationType is null! contractPVO={0}", contractPVO));
			// throw new IllegalArgumentException("CommunicationType is null!");
			contractPVO.setCommunicationType(commTypeTextDefault);
		} catch (IllegalArgumentException illegalArgumentExce) {
			logger.warn(
					MessageFormat.format(
							"CommunicationType has invalid text value: \"{0}\"! contractPVO={1}",
							commTypeText, contractPVO),
					illegalArgumentExce);
			// throw new IllegalArgumentException("CommunicationType has invalid text value!");
			contractPVO.setCommunicationType(commTypeTextDefault);
		}
	}

	public Contract getContractByContractNumber(String inputContractNumber) {

		final List<Contract> results = listContractByContractNumber(inputContractNumber);
		Validate.isTrue(results.size() == 1, "Found more than one or none, but ContractNumber is unique.");

		return results.get(0);
	}

	public List<Contract> listContractByContractNumber(String inputContractNumber) {
		Validate.notBlank(inputContractNumber, "InputContractNumber for searching should be set.");

		final Contract searchPattern = new Contract();
		searchPattern.setContractNr(inputContractNumber);

		return PersistenceHandler.getInstance().listContract(searchPattern);
	}

	public boolean contractNumberExist(final String contractNumber) {
		try {
			final ContractPVO search = new ContractPVO();
			search.setContractNr(contractNumber);
			return CommandProcessor.process(connection -> new ContractDAO().count(search, false, null, connection)) > 0;
		} catch (SQLException e) {
			throw new PersistenceHandlerException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public List<ContractPVO> select(String sql) {
		try {
			return super.select(sql);
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	public void addContractNumberToChangeTriggerQueue(ContractPVO contractPVO, TypeOfOperation typeOfOperation,
			ChangeTriggerQueueTypeOfEntityEnum typeOfEntity) {
		ChangeTriggerQueueTool.changeTriggerQueueSaveTool(contractPVO.getContractNr(), typeOfOperation.getId(), typeOfEntity.getId());
	}
}
