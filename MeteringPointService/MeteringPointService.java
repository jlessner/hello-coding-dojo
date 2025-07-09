/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery;

import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.CANCELED;
import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.ENDED;
import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.GPKE_APPROVED;
import static com.nextlevel.fastlane.myBusinessSupplier.tool.MBSCollectionTool.getAndExpectExactlyOneElement;
import static com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum.CONTRACT_SUPPLY_BEGIN_DATE_VALUESOURCE;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsAny;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import com.nextlevel.fastlane.financial.bo.TimeSlice;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ContractAdditional;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ControllableResource;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.fastlane.myBusinessSupplier.bo.TechnicalResource;
import com.nextlevel.fastlane.myBusinessSupplier.exception.MBSException;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ContractAdditionalDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ContractDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.PointOfDeliveryDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.ContractPVO;
import com.nextlevel.myBusinessSupplier.aepmako.enums.domain.entity.nachrichten.Meldepunkttyp;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.date.DateTool;
import com.nextlevel.platform.edi.enums.MeterPointType;
import com.nextlevel.platform.exception.ExceptionTool;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;

public class MeteringPointService {

	private static final PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

	public static PointOfDelivery getMarketLocation(final ContractPVO contract) throws Exception {
		return getMarketLocation(persistenceHandler.getContractFromPVO(contract));
	}

	public static PointOfDelivery getMarketLocation(final Contract contract) {
		// why do we even need this method?
		try {
			final Contract reloaded = persistenceHandler.loadContract(contract.getId());
			final Long maLoId = reloaded.getPointOfDelivery();
			if (maLoId != null) {
				final PointOfDelivery maLo = persistenceHandler.loadPointOfDelivery(maLoId);
				if (isMarketLocation(maLo)) {
					return maLo;
				}

			}
			return getSinglePointOfDelivery(reloaded, MeterPointType.Z30).orElse(null);
		} catch (Exception e) {
			ExceptionTool.throwAsRuntimeException(e);
		}
		return null;
	}

	public static PointOfDelivery getMeteringLocation(final ContractPVO contract) throws Exception {
		return getMeteringLocation(persistenceHandler.getContractFromPVO(contract));
	}

	public static PointOfDelivery getMeteringLocation(final Contract contract) {
		return getSinglePointOfDelivery(contract, MeterPointType.Z31).orElse(null);
	}

	public static PointOfDelivery getNetLocation(final Contract contract) {
		try {
			Map<PointOfDelivery, List<PointOfDelivery>> pointOfDeliveryListMap = new PointOfDeliveryDAO().listPointOfDeliveries(contract);
			return pointOfDeliveryListMap.values()
					.stream()
					.flatMap(List::stream)
					.filter(MeteringPointService::isNetLocation)
					.findFirst()
					.orElse(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<PointOfDelivery> listMeteringLocations(PointOfDelivery marketLocation) {
		return persistenceHandler.listPointOfDelivery(new PointOfDelivery().withParent(marketLocation.getId()));
	}

	public static Optional<PointOfDelivery> getSinglePointOfDelivery(final Contract contract, final MeterPointType type) {
		Validate.notNull(contract, "Parameter 'contract' must not be NULL");
		try {
			List<PointOfDelivery> pods = listPointOfDeliveries(contract, type);

			// Fallback Pre-Interimsmako
			if ((pods.isEmpty() || pods.get(0).getId() == null) && contract.getPointOfDelivery() != null) {
				return Optional.ofNullable(persistenceHandler.loadPointOfDelivery(contract.getPointOfDelivery()));
			} else if ((pods.isEmpty() || pods.get(0).getId() == null) && contract.getPointOfDelivery() == null) {
				return Optional.empty();
			}

			return Optional.ofNullable(pods.get(0));
		} catch (Exception e) {
			throw new MBSException(contract, e);
		}
	}

	public static List<PointOfDelivery> listPointOfDeliveries(Contract contract, final MeterPointType type) {
		try {
			if (contract == null || (contract.getId() == null && contract.getContractNr() == null)) {
				return Collections.emptyList();
			} else {
				if (contract.getId() != null) {
					contract = persistenceHandler.loadContract(contract.getId());
				} else {
					final Contract pattern = new Contract().withContractNr(contract.getContractNr());
					contract = getAndExpectExactlyOneElement(persistenceHandler.listContract(pattern));
				}
			}

			Map<PointOfDelivery, List<PointOfDelivery>> pointOfDeliveryListMap = new PointOfDeliveryDAO().listPointOfDeliveries(contract);
			return pointOfDeliveryListMap.values()
					.stream()
					.flatMap(List::stream)
					.filter(p -> isOfType(p, type))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new MBSException(contract, e);
		}
	}

	// quick-fix for pre-aep / aep world
	private static boolean isOfType(final PointOfDelivery pointOfDelivery, final MeterPointType type) {
		if (type == null) {
			return false;
		}
		if (type == MeterPointType.Z31) {
			// MELO
			return isMeteringLocation(pointOfDelivery);
		}
		if (type == MeterPointType.Z30) {
			// MALO
			return isMarketLocation(pointOfDelivery);
		}
		//... else: usually not requested in current code
		return false;
	}

	public static boolean isNetLocation(final PointOfDelivery pointOfDelivery) {
		final String type = pointOfDelivery.getMeteringPointType();
		return Meldepunkttyp.NETZLOKATION.name().equalsIgnoreCase(type);
	}

	public static boolean isMeteringLocation(final PointOfDelivery pointOfDelivery) {
		final String type = pointOfDelivery.getMeteringPointType();
		final boolean isZ31 = MeterPointType.Z31.getValue().equalsIgnoreCase(type);
		final boolean isMessLokation = Meldepunkttyp.MESSLOKATION.name().equalsIgnoreCase(type);
		return isZ31 || isMessLokation;
	}

	public static boolean isMarketLocation(final PointOfDelivery pointOfDelivery) {
		final String type = pointOfDelivery.getMeteringPointType();
		final boolean isZ30 = MeterPointType.Z30.getValue().equalsIgnoreCase(type);
		final boolean isMarktLokation = Meldepunkttyp.MARKTLOKATION.name().equalsIgnoreCase(type);
		return isZ30 || isMarktLokation;
	}

	public static PointOfDelivery getMarketLocationByMeteringLocation(PointOfDelivery meteringLocation) throws Exception {
		// @formatter:off
		final String query = "SELECT malo.* FROM " + PointOfDeliveryDAO.TABLE_NAME + " AS melo"
				+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS malo ON malo." + PointOfDeliveryDAO.column_id + " = melo." + PointOfDeliveryDAO.column_parent
				+ " WHERE melo." + PointOfDeliveryDAO.column_id + " = ? ";
		// @formatter:on

		return CommandProcessor.process(c -> {
			final PreparedStatement pstmt = c.prepareStatement(query);
			pstmt.setLong(1, meteringLocation.getId());
			final ResultSet res = pstmt.executeQuery();

			final List<PointOfDelivery> result = new ArrayList<>();
			while (res.next()) {
				final PointOfDelivery pointOfDelivery = persistenceHandler
						.getPointOfDeliveryFromPVO(new PointOfDeliveryDAO().buildPointOfDeliveryPVO(res));
				result.add(pointOfDelivery);
			}

			if (result.size() != 1) {
				throw new Exception("Expected exactly 1 marketLocation for meteringLocation "
						+ meteringLocation.getMeteringPoint() + " but found " + result.size());
			}

			return result.get(0);
		});
	}

	/**
	 * SQL execution performance is bad for this one. If you know which MeteringPointType is present use one of the other methods.
	 *
	 * @param pointOfDelivery
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static Contract getContractByPointOfDelivery(PointOfDelivery pointOfDelivery) throws Exception {
		List<Contract> contracts = getContractsByPointOfDelivery(pointOfDelivery);
		if (contracts.size() == 1) {
			return contracts.get(0);
		} else if (contracts.size() == 0) {
			throw new Exception("no contract found for PointOfDelivery " + pointOfDelivery.getId());
		} else {
			throw new Exception("more than one contract found for PointOfDelivery " + pointOfDelivery.getId());
		}
	}

	/**
	 * SQL execution performance is bad for this one. If you know which MeteringPointType is present use one of the other methods.
	 *
	 * @param pointOfDelivery
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static List<Contract> getContractsByPointOfDelivery(PointOfDelivery pointOfDelivery) throws Exception {
		Validate.notNull(pointOfDelivery, "PointOfDelivery not set");
		Validate.notNull(pointOfDelivery.getId(), "PointOfDelivery-Id not set");

		return CommandProcessor.process(connection -> {
			List<Contract> contracts = new ArrayList<>();
			String query = "SELECT contract.* FROM " + ContractDAO.TABLE_NAME + " AS contract "
					+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS malo ON malo." + PointOfDeliveryDAO.column_id + " = contract."
					+ ContractDAO.column_PointOfDelivery
					+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS melo ON melo." + PointOfDeliveryDAO.column_parent + " = malo."
					+ PointOfDeliveryDAO.column_id
					+ " WHERE malo." + PointOfDeliveryDAO.column_id + " = ? OR melo." + PointOfDeliveryDAO.column_id + " = ? ";

			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setLong(1, pointOfDelivery.getId());
			pstmt.setLong(2, pointOfDelivery.getId());
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				ContractPVO pvo = new ContractDAO().buildContractPVO(res);
				contracts.add(persistenceHandler.getContractFromPVO(pvo));
			}

			return contracts;
		});
	}

	public static List<Contract> listContractsByMarketLocation(String marketLocation) throws Exception {
		Validate.isTrue(StringUtils.isNotBlank(marketLocation), "MarketLocation must be set");
		return new ArrayList<>(listContractsWithPointOfDelivery(marketLocation, MeterPointType.Z30).keySet());
	}

	public static List<Contract> listContractsByMeteringLocation(String meteringLocation) throws Exception {
		Validate.isTrue(StringUtils.isNotBlank(meteringLocation), "MarketLocation must be set");
		return new ArrayList<>(listContractsWithPointOfDelivery(meteringLocation, MeterPointType.Z31).keySet());
	}

	public static Contract getContractByMarketLocation(String marketLocation, Date date) throws Exception {
		List<Contract> contracts = listContractsByMarketLocation(marketLocation);

		return getContractFromList(date, contracts, marketLocation);
	}

	public static Contract getContractByMarketLocation(String marketLocation, TimeSlice timeSlice) throws Exception {
		List<Contract> contracts = listContractsByMarketLocation(marketLocation);

		return getContractFromList(timeSlice, contracts, marketLocation);
	}

	public static Contract getContractByMeteringLocation(String meteringLocation, Date date) throws Exception {
		List<Contract> contracts = listContractsByMeteringLocation(meteringLocation);

		return getContractFromList(date, contracts, meteringLocation);
	}

	public static Contract getContractByMeteringLocation(String meteringLocation, TimeSlice timeSlice) throws Exception {
		List<Contract> contracts = listContractsByMeteringLocation(meteringLocation);

		return getContractFromList(timeSlice, contracts, meteringLocation);
	}

	public static Contract getContractByMeteringPoint(PointOfDelivery pointOfDelivery) throws Exception {
		String meteringPointType = pointOfDelivery.getMeteringPointType();
		switch (Meldepunkttyp.valueOf(meteringPointType)) {
			case MARKTLOKATION:
				return BaseContractFinder.getContractByMarketLocation(pointOfDelivery);
			case MESSLOKATION:
				return BaseContractFinder.getContractByMeteringLocation(pointOfDelivery);
			case NETZLOKATION:
				// TODO: load by NetzLocation
				return BaseContractFinder.getContractByMeteringLocation(pointOfDelivery);
			default:
				throw new IllegalArgumentException("Invalid metering point type: " + meteringPointType);
		}
	}

	private static Contract getContractFromList(Date date, List<Contract> contracts, String meteringPoint) throws Exception {
		List<Contract> possibleContracts = TimeSliceTool.tryGetCurrentList(contracts, date);
		if (possibleContracts.size() == 1) {
			return possibleContracts.get(0);
		} else if (possibleContracts.isEmpty()) {
			return findContractByAdditional(date, contracts);
		} else {
			throw new IllegalStateException("More than one point of delivery for meteringPoint " + meteringPoint + " at date " + date);
		}
	}

	private static Contract getContractFromList(TimeSlice timeSlice, List<Contract> contracts, String meteringPoint) throws Exception {
		List<Contract> possibleContracts = contracts.stream()
				.filter(c -> TimeSliceTool.isPeriodStartBeforePeriodEnd(c))
				.filter(c -> TimeSliceTool.overlaps(c, timeSlice))
				.collect(toList());

		if (possibleContracts.size() == 1) {
			return possibleContracts.get(0);
		} else if (possibleContracts.isEmpty()) {
			return findContractByAdditional(timeSlice, contracts);
		} else {
			throw new IllegalStateException(
					"More than one point of delivery for meteringPoint " + meteringPoint + " within time slice (" + timeSlice + ")");
		}
	}

	public static PointOfDelivery getNewestPointOfDeliveries(String meteringPoint, MeterPointType meteringPointType, Date date)
			throws Exception {
		Map<Contract, PointOfDelivery> contracts = listContractsWithPointOfDelivery(meteringPoint, meteringPointType);
		if (contracts.size() == 1) {
			return contracts.values().iterator().next();
		} else if (contracts.size() > 1) {
			Date newestDate = null;
			PointOfDelivery newestPoint = null;
			for (Contract contract : contracts.keySet()) {
				if (contract.getPeriodStart() != null
						&& (DateTool.equals(date, contract.getPeriodStart()) || contract.getPeriodStart().before(date))) {
					if (contract.getPeriodEnd() == null) {
						return contracts.get(contract);
					}

					if (newestPoint == null) {
						newestDate = contract.getPeriodEnd();
						newestPoint = contracts.get(contract);
					} else {
						if (newestDate != null && newestDate.before(contract.getPeriodEnd())) {
							newestDate = contract.getPeriodEnd();
							newestPoint = contracts.get(contract);
						}
					}
				}
			}
			return newestPoint;
		}
		return null;
	}

	private static Map<Contract, PointOfDelivery> listContractsWithPointOfDelivery(String meteringPoint, MeterPointType meteringPointType)
			throws Exception {
		String fromJoinPart;
		String pointOfDeliveryAlias;

		if (meteringPointType == MeterPointType.Z30) {
			pointOfDeliveryAlias = "malo";
			fromJoinPart = " FROM " + PointOfDeliveryDAO.TABLE_NAME + " AS malo "
					+ " LEFT JOIN " + ContractDAO.TABLE_NAME + " AS contract ON contract." + ContractDAO.column_PointOfDelivery + " = malo."
					+ PointOfDeliveryDAO.column_id;
		} else {
			pointOfDeliveryAlias = "melo";
			fromJoinPart = " FROM " + PointOfDeliveryDAO.TABLE_NAME + " AS melo "
					+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS malo ON malo." + PointOfDeliveryDAO.column_id + " = melo."
					+ PointOfDeliveryDAO.column_parent
					+ " LEFT JOIN " + ContractDAO.TABLE_NAME + " AS contract ON contract." + ContractDAO.column_PointOfDelivery + " = malo."
					+ PointOfDeliveryDAO.column_id;
		}

		return listContractsWithPointOfDelivery(meteringPoint, meteringPointType, fromJoinPart, pointOfDeliveryAlias);
	}

	private static Map<Contract, PointOfDelivery> listContractsWithPointOfDelivery(String meteringPoint, MeterPointType meteringPointType,
			String fromJoinPart, String pointOfDeliveryTable) throws Exception {
		return CommandProcessor.process(connection -> {
			ContractDAO contractDAO = new ContractDAO();
			contractDAO.setTableName("contract");
			PointOfDeliveryDAO pointOfDeliveryDAO = new PointOfDeliveryDAO();
			pointOfDeliveryDAO.setTableName(pointOfDeliveryTable);

			Map<Contract, PointOfDelivery> contracts = new HashMap<>();

			String query = "SELECT 1 " + contractDAO.getAllColumnsWithAlias() + pointOfDeliveryDAO.getAllColumnsWithAlias()
					+ fromJoinPart
					+ " WHERE " + pointOfDeliveryTable + "." + PointOfDeliveryDAO.column_meteringPoint + " = ? ";

			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, meteringPoint);
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				Contract contract = PersistenceHandler.getInstance().getContractFromPVO(contractDAO.buildContractPVO(res));
				PointOfDelivery pointOfDelivery
						= PersistenceHandler.getInstance().getPointOfDeliveryFromPVO(pointOfDeliveryDAO.buildPointOfDeliveryPVO(res));

				if (StringUtils.equals(pointOfDelivery.getMeteringPointType(), meteringPointType.getValue())
						|| StringUtils.equalsIgnoreCase(pointOfDelivery.getMeteringPointType(), meteringPointType.getDescription())) {
					contracts.put(contract, pointOfDelivery);
				}
			}

			return contracts;
		});
	}

	/**
	 * SQL execution performance is bad for this one. If you know which MeteringPointType is present use one of the other methods.
	 *
	 * @param meteringPoint
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static List<Contract> listContractsByMeteringPoint(String meteringPoint) throws Exception {
		Validate.isTrue(StringUtils.isNotBlank(meteringPoint), "MeteringPoint must be set");

		return CommandProcessor.process(connection -> {
			List<Contract> contracts = new ArrayList<>();
			String query = "SELECT DISTINCT contract.* FROM " + ContractDAO.TABLE_NAME + " AS contract "
					+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS malo ON malo." + PointOfDeliveryDAO.column_id + " = contract."
					+ ContractDAO.column_PointOfDelivery
					+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS melo_or_nelo ON melo_or_nelo." + PointOfDeliveryDAO.column_parent
					+ " = malo." + PointOfDeliveryDAO.column_id
					+ " WHERE malo." + PointOfDeliveryDAO.column_meteringPoint + " = ? OR melo_or_nelo."
					+ PointOfDeliveryDAO.column_meteringPoint + " = ? ";

			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, meteringPoint);
			pstmt.setString(2, meteringPoint);
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				ContractPVO pvo = new ContractDAO().buildContractPVO(res);
				contracts.add(PersistenceHandler.getInstance().getContractFromPVO(pvo));
			}

			return contracts;
		});
	}

	public static Contract getContractByControllableResource(String identification, Date date) throws Exception {
		Validate.isTrue(StringUtils.isNotBlank(identification), "identification must be set");
		Validate.notNull(date, "date must be set");

		ControllableResource controllableResource = getControllableResources(identification, date);
		if (controllableResource == null) {
			return null;
		}

		TechnicalResource technicalResource =
				TechnicalResourceFinder.getTechnicalResourceByControllableResource(controllableResource.getId(), date);
		if (technicalResource == null) {
			return null;
		}

		List<Contract> contracts = listContractsByMarketLocationId(technicalResource.getMaloId());
		return TimeSliceTool.tryGetCurrent(contracts, date);
	}

	public static Contract getContractByTechnicalResource(String identification, Date date) throws Exception {
		Validate.isTrue(StringUtils.isNotBlank(identification), "identification must be set");
		Validate.notNull(date, "date must be set");

		TechnicalResource technicalResource = TechnicalResourceFinder.getTechnicalResource(identification, date);
		if (technicalResource == null) {
			return null;
		}

		List<Contract> contracts = listContractsByMarketLocationId(technicalResource.getMaloId());
		return TimeSliceTool.tryGetCurrent(contracts, date);
	}

	private static List<Contract> listContractsByMarketLocationId(Long marketLocationId) throws Exception {
		return CommandProcessor.process(connection -> {
			List<Contract> contracts = new ArrayList<>();
			String query = "SELECT DISTINCT contract.* FROM " + ContractDAO.TABLE_NAME + " AS contract "
					+ " WHERE contract." + ContractDAO.column_PointOfDelivery + " = ? ";

			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setLong(1, marketLocationId);
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				ContractPVO pvo = new ContractDAO().buildContractPVO(res);
				contracts.add(PersistenceHandler.getInstance().getContractFromPVO(pvo));
			}

			return contracts;
		});
	}

	public static ControllableResource getControllableResources(String identification, Date date) {
		ControllableResource pattern = new ControllableResource().withIdentification(identification);
		List<ControllableResource> controllableResources = PersistenceHandler.getInstance().listControllableResource(pattern);
		return TimeSliceTool.tryGetCurrent(controllableResources, date);
	}

	public static PointOfDelivery getPointOfDelivery(String meteringPoint, MeterPointType meterPointType, Date date) throws Exception {
		Map<Contract, PointOfDelivery> contracts = listContractsWithPointOfDelivery(meteringPoint, meterPointType);
		if (contracts.size() == 1) {
			return contracts.values().iterator().next();
		}

		Set<Contract> availableContracts = contracts.keySet();
		if (availableContracts != null && !availableContracts.isEmpty()) {
			List<Contract> possibleContracts = TimeSliceTool.tryGetCurrentList(availableContracts, date);
			if (possibleContracts.size() > 1) {
				possibleContracts = possibleContracts.stream()
						.filter(contract -> equalsAny(contract.getState(), GPKE_APPROVED.toString(), CANCELED.toString(), ENDED.toString()))
						.collect(toList());
			}

			if (possibleContracts.size() == 1) {
				return contracts.get(possibleContracts.get(0));
			} else if (possibleContracts.size() > 1) {
				throw new IllegalStateException("More than one point of delivery for " + meteringPoint + " at date " + date);
			} else {
				Contract contract = findContractByAdditional(date, availableContracts);
				return contracts.get(contract);
			}
		}
		return null;
	}

	public static Contract findContractByAdditional(Date supplyBeginDate, Collection<Contract> contracts) throws Exception {
		Contract contract = null;
		final String valueSourceName = CONTRACT_SUPPLY_BEGIN_DATE_VALUESOURCE.getValue();

		if (valueSourceName != null) {
			Map<ContractAdditional, Contract> contractAdditionals = new ContractAdditionalDAO().listContractAdditional(contracts);
			Optional<ContractAdditional> foundAddition = contractAdditionals.keySet().stream()
					.filter(additional -> isAfterValue(additional, valueSourceName, supplyBeginDate)).findFirst();
			if (foundAddition.isPresent()) {
				contract = contractAdditionals.get(foundAddition.get());
			}
		}
		return contract;
	}

	private static Contract findContractByAdditional(TimeSlice timeSlice, Collection<Contract> contracts) throws Exception {
		Contract contract = null;
		final String valueSourceName = CONTRACT_SUPPLY_BEGIN_DATE_VALUESOURCE.getValue();

		if (valueSourceName != null) {
			Map<ContractAdditional, Contract> contractAdditionals = new ContractAdditionalDAO().listContractAdditional(contracts);
			Optional<ContractAdditional> foundAddition = contractAdditionals.keySet().stream()
					.filter(additional -> isAfterValue(additional, valueSourceName, timeSlice)).findFirst();
			if (foundAddition.isPresent()) {
				contract = contractAdditionals.get(foundAddition.get());
			}
		}
		return contract;
	}

	private static boolean isAfterValue(ContractAdditional additional, String valueSourceName, Date pivot) {
		Date additionalDate = null;
		if (StringUtils.equals(valueSourceName, "ADDITIONAL1")) {
			additionalDate = additional.getDateAdditional1();
		} else if (StringUtils.equals(valueSourceName, "ADDITIONAL2")) {
			additionalDate = additional.getDateAdditional2();
		} else if (StringUtils.equals(valueSourceName, "ADDITIONAL3")) {
			additionalDate = additional.getDateAdditional3();
		}

		Date pivotWoMillis = DateTool.removeMilliseconds(pivot);
		Date additionalDateWoMillis = DateTool.removeMilliseconds(additionalDate);
		return additionalDate != null && (pivotWoMillis.after(additionalDateWoMillis) || pivotWoMillis.equals(additionalDateWoMillis));
	}

	private static boolean isAfterValue(ContractAdditional additional, String valueSourceName, TimeSlice timeSlice) {
		Date additionalDate = null;
		if (StringUtils.equals(valueSourceName, "ADDITIONAL1")) {
			additionalDate = additional.getDateAdditional1();
		} else if (StringUtils.equals(valueSourceName, "ADDITIONAL2")) {
			additionalDate = additional.getDateAdditional2();
		} else if (StringUtils.equals(valueSourceName, "ADDITIONAL3")) {
			additionalDate = additional.getDateAdditional3();
		}

		Date periodEndWoMillis = DateTool.removeMilliseconds(timeSlice.getPeriodEnd());
		Date additionalDateWoMillis = DateTool.removeMilliseconds(additionalDate);
		return additionalDate != null && (periodEndWoMillis.after(additionalDateWoMillis) || periodEndWoMillis
				.equals(additionalDateWoMillis));
	}

}