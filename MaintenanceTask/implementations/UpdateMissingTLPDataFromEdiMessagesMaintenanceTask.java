/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.aepmako.maintenanceTask;

import static com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum.MIGRATE_TLP_DATA_FROM_EDI;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nextlevel.fastlane.configuration.GlobalPropertiesAdmin;
import com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails;
import com.nextlevel.fastlane.myBusinessSupplier.sql.QueryBuilderHelper;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.EdiMessageDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.EdiMessagePVO;
import com.nextlevel.myBusinessSupplier.aepmako.enums.core.entities.meldepunktdaten.TimeRowTypeEnum;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.configuration.GlobalProperty;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.edi.path.EdiPathParserException;
import com.nextlevel.platform.edi.path.EdiPathProcessor;
import com.nextlevel.platform.edi.util.EdiUnaData;
import com.nextlevel.platform.edi.util.EdiUtils;

import aep.makocloud.core.entities.profildaten.enums.Bilanzierungsverfahren;

public class UpdateMissingTLPDataFromEdiMessagesMaintenanceTask implements MaintenanceTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateMissingTLPDataFromMakoMessagesMaintenanceTask.class);
	private ResultSet resultSet;
	private boolean firstIteration = true;
	private List<SupplyDetails> currentSupplyDetailsList;
	private Long currentContractId;
	EdiPathProcessor ediPathProcessor = new EdiPathProcessor();

	@Override
	public void setup() throws MaintenanceTaskSetupFailedException {
		if (Boolean.FALSE.equals(MIGRATE_TLP_DATA_FROM_EDI.getValueAsBoolean())) {
			throw new MaintenanceTaskSetupFailedException(this,
					new Exception(
							"Skip UpdateMissingTLPDataFromEdiMessagesMaintenanceTask as global property MIGRATE_TLP_DATA_FROM_EDI is set to FALSE."));
		}

		try {
			this.resultSet = selectRelevantMessages();
		} catch (Exception e) {
			throw new MaintenanceTaskSetupFailedException(this, e);
		}

		LOGGER.info("Start migration of TLP data from Edi");
	}

	@Override
	public boolean hasNextEntry() {
		try {
			return resultSet != null && resultSet.next();
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public void cleanUp() {
		MaintenanceTask.super.cleanUp();

		try {
			final GlobalPropertiesAdmin globalPropertiesAdmin = new GlobalPropertiesAdmin();
			final GlobalProperty globalProperty = new GlobalProperty();
			globalProperty.setPropertyKey(MIGRATE_TLP_DATA_FROM_EDI.getKey());
			globalProperty.setVal("false");
			globalPropertiesAdmin.saveWithLogEntry(globalProperty);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		LOGGER.info("Finish migration of TLP data from EdiMessages");
	}

	@Override
	public int batchSize() {
		return 1000;
	}

	private ResultSet selectRelevantMessages() throws Exception {
		return CommandProcessor.process(c -> {
			final PreparedStatement stmt = c.prepareStatement(QueryBuilderHelper.wrapCount(getSql()));
			LOGGER.info("Execute statement: {}", stmt);
			return stmt.executeQuery();
		});
	}

	@Override
	public void processNextEntry() throws MaintenanceEntryProcessingFailedException {

		String idForException = "not identified";
		try {
			if (firstIteration) {
				final int count = resultSet.getInt(1);
				LOGGER.info("Found {} EdiMessages to process.", count);
				firstIteration = false;
			}
			final EdiMessagePVO currentEdiMessage = new EdiMessageDAO().buildEdiMessagePVO(resultSet);
			if (currentEdiMessage != null && currentEdiMessage.getId() != null) {
				idForException = currentEdiMessage.getId().toString();

				if (!currentEdiMessage.getContract().equals(currentContractId)) {
					currentContractId = currentEdiMessage.getContract();
					currentSupplyDetailsList = UpdateSupplyDetailsHelper.getSupplyDetailsListByContractId(currentContractId);
				}

				if (currentEdiMessage.getIncidentIdentifier().equals(11002L)) {
					updateDataFromSupplyBegin(currentEdiMessage);
				} else if (currentEdiMessage.getIncidentIdentifier().equals(11112L)) {
					updateRateOfTemperatureDependence(currentEdiMessage);
				} else if (currentEdiMessage.getIncidentIdentifier().equals(11126L)) {
					updateTimeRowTypes(currentEdiMessage);
				}
			}

		} catch (Exception e) {
			LOGGER.error("AepMakoMessage id: " + idForException, e);
			throw new MaintenanceEntryProcessingFailedException(idForException, e);
		}
	}

	private void updateDataFromSupplyBegin(EdiMessagePVO currentEdiMessage) throws Exception {
		Date pivotDate = getPivotDateFromSupplyBegin(currentEdiMessage);
		SupplyDetails supplyDetails = new SupplyDetails();
		mapTimeRowTypes(currentEdiMessage, supplyDetails);

		BigDecimal rateOfTemperatureDependence = getRateOfTemperatureDependence(currentEdiMessage);
		supplyDetails.setRateOfTemperatureDependence(rateOfTemperatureDependence);

		List<String> listOfSegmentZ08 = getListOfSegment(currentEdiMessage.getMessage(), "SEQ", "Z08", "UNT");

		if (CollectionUtils.isNotEmpty(listOfSegmentZ08)) {
			String profilscharCode = parseString(listOfSegmentZ08.get(0), "{CCI[3+0=\"Z12\"]{CAV[1]+1+0}}");
			supplyDetails.setLoadProfileGroup(profilscharCode);

			String loadProfileType = parseString(listOfSegmentZ08.get(0), "CCI[1+0=\"Z03\"]+3+0");
			if ("E01".equals(loadProfileType)) {
				supplyDetails.setLoadProfileType(Bilanzierungsverfahren.SYNTHETISCHES_VERFAHREN.name());
			} else if ("Z10".equals(loadProfileType)) {
				supplyDetails.setLoadProfileType(Bilanzierungsverfahren.ANALYTISCHES_VERFAHREN.name());
			}

			String profilCode = parseString(listOfSegmentZ08.get(0), "{CCI[1+0=\"Z03\"]{CAV[1]+1+0}");
			supplyDetails.setLoadProfile(profilCode);

			String thermalType = parseString(listOfSegmentZ08.get(0), "CCI[1+0=\"Z99\"]+1+0");
			if (thermalType == null) {
				thermalType = parseString(listOfSegmentZ08.get(0), "CCI[1+0=\"ZA0\"]+1+0");
			}
			if (thermalType != null) {
				String climateZone = parseString(listOfSegmentZ08.get(0), "CCI[1+0=\"" + thermalType + "\"]+3+0");
				String thermalProvider = parseString(listOfSegmentZ08.get(0), "CCI[1+0=\"" + thermalType + "\"]+3+1");
				String thermalDefinition = parseString(listOfSegmentZ08.get(0), "CCI[1+0=\"" + thermalType + "\"]+3+2");

				ThermalDataMapper thermalDataMapper = ThermalDataMapper.INSTANCE;
				supplyDetails.setThermalType(thermalDataMapper.mapThermalType(thermalType).name());
				supplyDetails.setClimateZone(climateZone);
				supplyDetails.setThermalProvider(thermalDataMapper.mapThermalProvider(thermalProvider).name());
				supplyDetails.setThermalDefinition(thermalDataMapper.mapThermalDefinition(thermalDefinition).name());
			}
		}
		UpdateSupplyDetailsHelper.updateSupplyDetailsFromSupplyBegin(currentSupplyDetailsList, supplyDetails, pivotDate);
	}

	private Date getPivotDateFromMasterDataChange(EdiMessagePVO currentEdiMessage) throws Exception {
		return parseDate(currentEdiMessage.getMessage(), "DTM[1+0=\"157\"]+1+1");
	}

	private Date getPivotDateFromSupplyBegin(EdiMessagePVO currentEdiMessage) throws Exception {
		return parseDate(currentEdiMessage.getMessage(), "DTM[1+0=\"158\"]+1+1");
	}

	private BigDecimal getRateOfTemperatureDependence(EdiMessagePVO currentEdiMessage) throws IOException, EdiPathParserException {
		List<String> listOfSegment = getListOfSegment(currentEdiMessage.getMessage(), "SEQ", "Z01", "UNT");
		return parseBigDecimal(listOfSegment.get(0), "{CCI[3+0=\"E17\"]{CAV[1+0=\"Z22\"]+1+3}}");
	}

	private void updateRateOfTemperatureDependence(EdiMessagePVO currentEdiMessage) throws Exception {
		BigDecimal rateOfTemperatureDependence = getRateOfTemperatureDependence(currentEdiMessage);
		Date pivotDate = getPivotDateFromMasterDataChange(currentEdiMessage);

		UpdateSupplyDetailsHelper.updateRateOfTemperatureDependence(currentSupplyDetailsList, pivotDate, rateOfTemperatureDependence);
	}

	private void updateTimeRowTypes(EdiMessagePVO currentEdiMessage) throws Exception {
		Date pivotDate = getPivotDateFromMasterDataChange(currentEdiMessage);
		SupplyDetails supplyDetails = new SupplyDetails();
		mapTimeRowTypes(currentEdiMessage, supplyDetails);

		UpdateSupplyDetailsHelper.updateTimeRowTypes(currentSupplyDetailsList, supplyDetails, pivotDate);
	}

	private void mapTimeRowTypes(EdiMessagePVO currentEdiMessage, SupplyDetails supplyDetails)
			throws IOException, EdiPathParserException {
		List<String> listOfSegment = getListOfSegment(currentEdiMessage.getMessage(), "CCI", "15", "UNT");
		String timeRowType1 = parseString(listOfSegment.get(0), "CCI[1+0=\"15\"]+3+0");
		if ("Z21".equals(timeRowType1)) {
			supplyDetails.setTimeRowType1(TimeRowTypeEnum.SUMMENZEITREIHENTYP.name());
		}
		supplyDetails.setTimeRowTypeCode1(parseString(listOfSegment.get(0), "{CCI[1+0=\"15\"]{CAV[1]+1+0}}"));
		if (listOfSegment.size() > 1) {
			String timeRowType2 = parseString(listOfSegment.get(1), "CCI[1+0=\"15\"]+3+0");
			if ("Z21".equals(timeRowType2)) {
				supplyDetails.setTimeRowType2(TimeRowTypeEnum.SUMMENZEITREIHENTYP.name());
			}
			supplyDetails.setTimeRowTypeCode2(parseString(listOfSegment.get(1), "{CCI[1+0=\"15\"]{CAV[1]+1+0}}"));
		}
	}

	private List<String> getListOfSegment(String ediMessage, String segmentStart, String group, String segmentEnd)
			throws IOException, EdiPathParserException {
		List<String> result = new ArrayList<>();
		Object oSeqs = ediPathProcessor.process(ediMessage, segmentStart + ".." + segmentEnd, false);
		if (oSeqs == null) {
			return new ArrayList<>();
		} else {
			Object seqs;
			if (oSeqs instanceof String) {
				seqs = new ArrayList<>();
				((List) seqs).add(oSeqs);
			} else {
				seqs = oSeqs;
			}

			Iterator var8 = ((List) seqs).iterator();

			while (var8.hasNext()) {
				String seq = (String) var8.next();
				if (StringUtils.equals(parseString(seq, segmentStart + "+1+0"), group)) {
					result.add(seq);
				}
			}

			return result;
		}
	}

	protected String parseString(String ediMessage, String ediPath) throws IOException, EdiPathParserException {
		return EdiUtils.unescapeChars((String) ediPathProcessor.process(ediMessage, ediPath, false));
	}

	private BigDecimal parseBigDecimal(String ediMessage, String ediPath) throws IOException, EdiPathParserException {
		String value = EdiUtils.unescapeChars((String) ediPathProcessor.process(ediMessage, ediPath, false));
		EdiUnaData ediUnaData = new EdiUnaData(ediMessage);
		return StringUtils.isNotBlank(value) ? EdiUtils.getValue(value, ediUnaData) : null;
	}

	private Date parseDate(String ediMessage, String ediPath) throws Exception {
		String beginDate = this.parseString(ediMessage, ediPath);
		return EdiUtils.createDate(beginDate);
	}

	private String getSql() {
		// @formatter:off
		return "select (select c.section_  from fl_contract c where c.id_ = fe.contract_) section__\n"
				+ ",fe.*\n"
				+ "from fl_edimessage fe \n"
				+ "where state_ = 'SUCCESS'\n"
				+ "and type_ = 'UTILMD'\n"
				+ "and contract_ is not null\n"
				+ "and msgdate_ >= '2020-10-01 00:00:00'\n"
				+ "and (incidentidentifier_ = 11002 and (message_ like '%CAV+E14%' or message_ like '%CCI+++Z12%' or message_ like  '%CCI+15++Z21%' or message_ like '%CAV+Z22%') -- (TLP/TEP) or Profilschar or Zeitreihentypen or Verbrauchsaufteilung \n"
				+ "or   incidentidentifier_ = 11112 and message_ like '%CAV+Z22%' -- Verbrauchsaufteilung\n"
				+ "or   incidentidentifier_ = 11126 and message_ like '%CCI+15++Z21%' -- Zeitreihentypen\n"
				+ ")\n"
				+ "order by fe.contract_ , fe.id_ " ;
		// @formatter:on
	}

}
