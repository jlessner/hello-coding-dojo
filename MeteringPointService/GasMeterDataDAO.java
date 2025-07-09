/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.persistence.myBusinessSupplier.dao;

import static com.nextlevel.platform.edi.enums.ObisNumberEnum.Label.CalorificValue;
import static com.nextlevel.platform.edi.enums.ObisNumberEnum.Label.CorrectionFactor;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import com.nextlevel.fastlane.financial.bo.TimeSlice;
import com.nextlevel.fastlane.myBusinessSupplier.PVOMapper;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.GasMeterData;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery.BaseContractFinder;
import com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery.MeteringPointService;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.GasMeterDataPVO;
import com.nextlevel.myBusinessSupplier.aepmako.enums.domain.entity.nachrichten.Meldepunkttyp;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.connection.StatementHandler;
import com.nextlevel.platform.edi.enums.ObisNumberEnum;
import com.nextlevel.platform.exception.ExceptionTool;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;

public class GasMeterDataDAO extends GasMeterDataDAOBase {

	public static List<GasMeterData> listUncancelledGasMeterData(PointOfDelivery pointOfDelivery, ObisNumberEnum.Label gasFactorType) {
		try {
			validateParameters(pointOfDelivery, gasFactorType);

			final String sql = "SELECT * FROM " + TABLE_NAME + //
					" WHERE " //
					+ column_cancellationEdi + " is null " //
					+ " AND " + column_pointOfDelivery + " = ? ;";

			return CommandProcessor.process(c -> {
				try {
					final PreparedStatement statement = c.prepareStatement(sql);
					statement.setLong(1, pointOfDelivery.getId());

					final ResultSet resultSet = statement.executeQuery();

					return parseResultSet(resultSet, gasFactorType);
				} catch (Exception e) {
					throw ExceptionTool.asRuntimeException(e);
				}
			});
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	public static List<GasMeterData> listGasMeterData(PointOfDelivery malo, PointOfDelivery melo, String status, Integer limit,
			Integer offset) {
		try {
			Validate.notNull(malo);
			Validate.notNull(malo.getId());
			Validate.notNull(melo);
			Validate.notNull(melo.getId());

			return CommandProcessor.process(c -> {
				final StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("SELECT * FROM " + TABLE_NAME);
				sqlBuilder.append(" WHERE (" + column_pointOfDelivery + " = ? OR " + column_pointOfDelivery + " = ?) ");
				if (StringUtils.equals(status, "cancelled")) {
					sqlBuilder.append(" AND " + column_cancellationEdi + " is not null ");
				} else if (StringUtils.equals(status, "uncancelled")) {
					sqlBuilder.append(" AND " + column_cancellationEdi + " is null ");
				}
				String sql = sqlBuilder.toString();
				if (limit != null) {
					sql = new StatementHandler(c).addLimitAndOffset(sql, limit, offset);
				}

				try {
					final PreparedStatement statement = c.prepareStatement(sql);
					statement.setLong(1, malo.getId());
					statement.setLong(2, melo.getId());

					final ResultSet resultSet = statement.executeQuery();

					return parseResultSetByQuery(resultSet);
				} catch (Exception e) {
					throw ExceptionTool.asRuntimeException(e);
				}
			});
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	public static int countGasMeterData(PointOfDelivery malo, PointOfDelivery melo, String status) {
		try {
			Validate.notNull(malo);
			Validate.notNull(malo.getId());
			Validate.notNull(melo);
			Validate.notNull(melo.getId());
			final String resultName = "numberOfRows";
			final StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("SELECT COUNT(id_) as " + resultName);
			sqlBuilder.append(" FROM " + TABLE_NAME);
			sqlBuilder.append(" WHERE (" + column_pointOfDelivery + " = ? OR " + column_pointOfDelivery + " = ?) ");
			if (StringUtils.equals(status, "cancelled")) {
				sqlBuilder.append(" AND " + column_cancellationEdi + " is not null ");
			} else if (StringUtils.equals(status, "uncancelled")) {
				sqlBuilder.append(" AND " + column_cancellationEdi + " is null ");
			}
			sqlBuilder.append(";");

			return CommandProcessor.process(c -> {
				try {
					final PreparedStatement statement = c.prepareStatement(sqlBuilder.toString());
					statement.setLong(1, malo.getId());
					statement.setLong(2, melo.getId());

					final ResultSet resultSet = statement.executeQuery();

					resultSet.next();
					return resultSet.getInt(resultName);
				} catch (Exception e) {
					throw ExceptionTool.asRuntimeException(e);
				}
			});
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	public static List<GasMeterData> loadGasFactors(PointOfDelivery pointOfDelivery, ObisNumberEnum.Label gasMeterDataType,
			TimeSlice consumptionSlice) {
		try {
			Contract contract = getContract(pointOfDelivery);
			return loadGasFactors(contract, gasMeterDataType, consumptionSlice);
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	public static List<GasMeterData> loadGasFactors(Contract contract, ObisNumberEnum.Label gasMeterDataType,
			TimeSlice consumptionSlice) {
		try {
			final PointOfDelivery meteringPoint = MeteringPointService.getMeteringLocation(contract);
			return loadGasFactorsForMeteringLocation(meteringPoint, null, gasMeterDataType, consumptionSlice, null, null, null);
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	public static List<GasMeterData> loadGasFactorsByQuery(Contract contract,
			TimeSlice consumptionSlice, String status, Integer limit, Integer offset) {
		try {
			final PointOfDelivery malo = MeteringPointService.getMarketLocation(contract);
			final PointOfDelivery melo = MeteringPointService.getMeteringLocation(contract);
			return loadGasFactorsForMeteringLocation(malo, melo, null, consumptionSlice, status, limit, offset);
		} catch (Exception e) {
			throw ExceptionTool.asRuntimeException(e);
		}
	}

	private static List<GasMeterData> loadGasFactorsForMeteringLocation(PointOfDelivery malo, PointOfDelivery melo,
			ObisNumberEnum.Label gasMeterDataType, TimeSlice consumptionSlice, String status, Integer limit, Integer offset) {

		List<GasMeterData> gasMeterDataList = null;
		if (ObjectUtils.allNotNull(gasMeterDataType)) {
			gasMeterDataList = GasMeterDataDAO.listUncancelledGasMeterData(malo, gasMeterDataType);
		} else {
			gasMeterDataList = GasMeterDataDAO.listGasMeterData(malo, melo, status, limit, offset);
		}
		return (gasMeterDataList != null) ? gasMeterDataList.stream()
				.filter(meterData -> TimeSliceTool.overlaps(meterData, consumptionSlice))
				.collect(toList()) : gasMeterDataList;
	}

	private static Contract getContract(PointOfDelivery pointOfDelivery) throws Exception {
		final String meteringPointType = pointOfDelivery.getMeteringPointType();

		Contract contract = null;
		if (Meldepunkttyp.MARKTLOKATION.name().equals(meteringPointType)) {
			contract = BaseContractFinder.getContractByMarketLocation(pointOfDelivery);
		} else if (Meldepunkttyp.MESSLOKATION.name().equals(meteringPointType)) {
			contract = BaseContractFinder.getContractByMeteringLocation(pointOfDelivery);
		} else {
			throw new IllegalArgumentException("Invalid metering point type: " + meteringPointType);
		}
		return contract;
	}

	private static List<GasMeterData> parseResultSet(ResultSet resultSet, ObisNumberEnum.Label gasFactorType)
			throws SQLException, IOException {
		final List<GasMeterData> result = new ArrayList<>();
		while (resultSet.next()) {
			final GasMeterDataPVO gasMeterDataPVO = new GasMeterDataDAO().buildGasMeterDataPVO(resultSet);
			if (ObisNumberEnum.fromValue(gasMeterDataPVO.getObisNumber()).hasLabel(gasFactorType)) {
				GasMeterData gasMeterData = PVOMapper.getInstance().getGasMeterDataFromPVO(gasMeterDataPVO);
				// if (gasMeterData.getPeriodEnd() != null) {
				// gasMeterData.setPeriodEnd(DateTool.getEndOfMonth(gasMeterData.getPeriodEnd()));
				// }
				result.add(gasMeterData);
			}
		}
		return result;
	}

	private static List<GasMeterData> parseResultSetByQuery(ResultSet resultSet)
			throws SQLException, IOException {
		final List<GasMeterData> result = new ArrayList<>();
		while (resultSet.next()) {
			final GasMeterDataPVO gasMeterDataPVO = new GasMeterDataDAO().buildGasMeterDataPVO(resultSet);
			GasMeterData gasMeterData = PVOMapper.getInstance().getGasMeterDataFromPVO(gasMeterDataPVO);
			result.add(gasMeterData);
		}
		return result;
	}

	private static void validateParameters(PointOfDelivery pod, ObisNumberEnum.Label gasFactorType) {
		Validate.notNull(pod);
		Validate.notNull(pod.getId());
		Validate.notNull(gasFactorType);
		final boolean isValidGasFactor = CalorificValue.equals(gasFactorType) || CorrectionFactor.equals(gasFactorType);
		Validate.isTrue(isValidGasFactor, "Parameter gasFactorType must be " + CalorificValue + " or " + CorrectionFactor);
	}

}