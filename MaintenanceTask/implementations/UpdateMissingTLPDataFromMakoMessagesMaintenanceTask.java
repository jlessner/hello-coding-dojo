/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.aepmako.maintenanceTask;

import static aep.makocloud.GeliGasGeschaeftsdatenanfrageSchnittstellenTyp.GELI_GAS_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE;
import static aep.makocloud.GeliGasLieferbeginnSchnittstellenTyp.GELI_GAS_LIEFERBEGINN_LFN_BESTAETIGUNG;
import static aep.makocloud.GeliGasStammdatenEmpfangSchnittstellenTyp.GELI_GAS_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE;
import static aep.makocloud.GpkeGeschaeftsdatenanfrageSchnittstellenTyp.GPKE_STROM_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE;
import static aep.makocloud.GpkeLieferbeginnSchnittstellenTyp.GPKE_STROM_LIEFERBEGINN_LFN_BESTAETIGUNG;
import static aep.makocloud.GpkeStammdatenEmpfangSchnittstellenTyp.GPKE_STROM_EMPFANG_SDA_AENDERUNG_PROGNOSEGRUNDLAGE_LF_UPDATE_STAMMDATEN_ANFRAGE;
import static aep.makocloud.GpkeStammdatenEmpfangSchnittstellenTyp.GPKE_STROM_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE;
import static aep.makocloud.GpkeStammdatenEmpfangSchnittstellenTyp.GPKE_STROM_EMPFANG_SDA_NICHT_BILREL_AENDERUNG_VOM_NB_LF_UPDATE_STAMMDATEN_ANFRAGE;
import static com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum.MIGRATE_TLP_DATA_FROM_AEP;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nextlevel.fastlane.configuration.GlobalPropertiesAdmin;
import com.nextlevel.fastlane.myBusinessSupplier.PVOMapper;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.commons.AepMakoMessageFactory;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.commons.mapping.CollectionMappingUtils;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.masterdata.received.detection.mapping.GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrageToMasterDataMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.masterdata.received.detection.mapping.GpkeStromEmpfangSdaAenderungPrognosegrundlageLfUpdateStammdatenAnfrageToMasterDataMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.masterdata.received.detection.mapping.GpkeStromEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrageToMasterDataMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.masterdata.received.detection.mapping.GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrageToMasterDataMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplybegin.mapping.GeliGasLieferbeginnLfnBestaetigungToSupplyDetailsMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplybegin.mapping.GeliLieferbeginnLfnBestaetigungToSupplyBeginDateMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplybegin.mapping.GpkeLieferbeginnLfnBestaetigungToPowerConsumptionPrognosisMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplybegin.mapping.GpkeLieferbeginnLfnBestaetigungToSupplyBeginDateMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplybegin.mapping.GpkeLieferbeginnLfnBestaetigungToSupplyDetailsMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplydatarequest.mapping.updateanfrage.GeliGasGeschaeftsdatenanfrageLfUpdateAnfrageToSupplyDetailsMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplydatarequest.mapping.updateanfrage.GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrageToPowerConsumptionPrognosisMapper;
import com.nextlevel.fastlane.myBusinessSupplier.aepmako.supplydatarequest.mapping.updateanfrage.GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrageToSupplyDetailsMapper;
import com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.GpkeProcess;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosisResolved;
import com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyBegin;
import com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails;
import com.nextlevel.fastlane.myBusinessSupplier.contract.consumptionprognosis.PowerConsumptionPrognosisTool;
import com.nextlevel.fastlane.myBusinessSupplier.contract.manual.ManualPowerConsumptionPrognosisService;
import com.nextlevel.fastlane.myBusinessSupplier.sql.QueryBuilderHelper;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.AepMakoMessageDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.AepMakoMessagePVO;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.configuration.GlobalProperty;
import com.nextlevel.platform.connection.CommandProcessor;

import aep.makocloud.core.entities.prognosedaten.enums.Prognosebasis;
import aep.makocloud.lf.geschaeftsdatenanfrage.schnittstellen.GeliGasGeschaeftsdatenanfrageLfUpdateAnfrage;
import aep.makocloud.lf.geschaeftsdatenanfrage.schnittstellen.GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrage;
import aep.makocloud.lf.lieferbeginn.fachobjekte.VerbrauchNetznutzung;
import aep.makocloud.lf.lieferbeginn.schnittstellen.GeliGasLieferbeginnLfnBestaetigung;
import aep.makocloud.lf.lieferbeginn.schnittstellen.GpkeStromLieferbeginnLfnBestaetigung;
import aep.makocloud.lf.stammdaten.empfang.fachobjekte.MarktlokationsdatenStammdatenaenderungEmpfangBilanzierungsrelevantVomNbAbhaengigGas;
import aep.makocloud.lf.stammdaten.empfang.fachobjekte.MarktlokationsdatenStammdatenaenderungEmpfangNichtBilanzierungsrelevantVomNb;
import aep.makocloud.lf.stammdaten.empfang.fachobjekte.PrognosegrundlageStammdatenaenderungEmpfangBilanzierungsrelevantVomNbAbhaengigGas;
import aep.makocloud.lf.stammdaten.empfang.fachobjekte.StammdatenaenderungEmpfangBilanzierungsrelevantVomNbAbhaengigGas;
import aep.makocloud.lf.stammdaten.empfang.fachobjekte.StammdatenaenderungEmpfangNichtBilanzierungsrelevantVomNb;
import aep.makocloud.lf.stammdaten.empfang.fachobjekte.VerbrauchStammdatenaenderungEmpfangNichtBilanzierungsrelevantVomNb;
import aep.makocloud.lf.stammdaten.empfang.schnittstellen.GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage;
import aep.makocloud.lf.stammdaten.empfang.schnittstellen.GpkeStromEmpfangSdaAenderungPrognosegrundlageLfUpdateStammdatenAnfrage;
import aep.makocloud.lf.stammdaten.empfang.schnittstellen.GpkeStromEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage;
import aep.makocloud.lf.stammdaten.empfang.schnittstellen.GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrage;

public class UpdateMissingTLPDataFromMakoMessagesMaintenanceTask implements MaintenanceTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateMissingTLPDataFromMakoMessagesMaintenanceTask.class);
	private static final PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();
	private ResultSet resultSet;
	private boolean firstIteration = true;

	private List<SupplyDetails> currentSupplyDetailsList;

	private Long currentContractId;

	@Override
	public void setup() throws MaintenanceTaskSetupFailedException {
		if (Boolean.FALSE.equals(MIGRATE_TLP_DATA_FROM_AEP.getValueAsBoolean())) {
			throw new MaintenanceTaskSetupFailedException(this,
					new Exception(
							"Skip UpdateMissingTLPDataFromEdiMessagesMaintenanceTask as global property MIGRATE_TLP_DATA_FROM_EDI is set to FALSE."));
		}

		try {
			this.resultSet = selectRelevantMessages();
		} catch (Exception e) {
			throw new MaintenanceTaskSetupFailedException(this, e);
		}

		LOGGER.info("Start migration of TLP data from AepMakoMessages");
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
			globalProperty.setPropertyKey(MIGRATE_TLP_DATA_FROM_AEP.getKey());
			globalProperty.setVal("false");
			globalPropertiesAdmin.saveWithLogEntry(globalProperty);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		LOGGER.info("Finish migration of TLP data from AepMakoMessages");
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
				LOGGER.info("Found {} AepMakoMessages to process.", count);
				firstIteration = false;
			}

			final AepMakoMessagePVO pvo = new AepMakoMessageDAO().buildAepMakoMessagePVO(resultSet);
			if (pvo != null && pvo.getId() != null) {
				idForException = pvo.getId().toString();
				AepMakoMessage currentMessage = PVOMapper.getInstance().getAepMakoMessageFromPVO(pvo);

				//result set is ordered by contract id
				//prevent unnecessary loading of supply details
				if (!currentMessage.getContract().equals(currentContractId)) {
					currentContractId = currentMessage.getContract();
					currentSupplyDetailsList = UpdateSupplyDetailsHelper.getSupplyDetailsListByContractId(currentContractId);
				}

				if (GPKE_STROM_EMPFANG_SDA_NICHT_BILREL_AENDERUNG_VOM_NB_LF_UPDATE_STAMMDATEN_ANFRAGE.name()
						.equals(currentMessage.getMessageType())) {
					updateRateOfTemperatureDependence(currentMessage);
				} else if (GPKE_STROM_EMPFANG_SDA_AENDERUNG_PROGNOSEGRUNDLAGE_LF_UPDATE_STAMMDATEN_ANFRAGE.name()
						.equals(currentMessage.getMessageType())) {
					updateTimeRowType(currentMessage);
				} else if (GPKE_STROM_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE.name()
						.equals(currentMessage.getMessageType())) {
					updatePrognosisDataElectricity(currentMessage);
				} else if (GELI_GAS_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE.name()
						.equals(currentMessage.getMessageType())) {
					updatePrognosisDataGas(currentMessage);
				} else if (GPKE_STROM_LIEFERBEGINN_LFN_BESTAETIGUNG.name().equals(currentMessage.getMessageType())) {
					updateSupplyBeginDataElectricity(currentMessage);
				} else if (GPKE_STROM_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE.name().equals(currentMessage.getMessageType())) {
					updateSupplyBeginMasterdataSyncElectricity(currentMessage);
				} else if (GELI_GAS_LIEFERBEGINN_LFN_BESTAETIGUNG.name().equals(currentMessage.getMessageType())) {
					updateSupplyBeginDataGas(currentMessage);
				} else if (GELI_GAS_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE.name().equals(currentMessage.getMessageType())) {
					updateSupplyBeginMasterdataSyncGas(currentMessage);
				}
				LOGGER.info("AepMakoMessage with id {} has been processed.", currentMessage.getId());
			}
		} catch (Exception e) {
			LOGGER.error("AepMakoMessage id: " + idForException, e);
			throw new MaintenanceEntryProcessingFailedException(idForException, e);
		}

	}

	/**
	 * 55002 - GPKE_STROM_LIEFERBEGINN_LFN_BESTAETIGUNG
	 */
	private void updateSupplyBeginDataElectricity(AepMakoMessage currentMessage) throws Exception {
		GpkeLieferbeginnLfnBestaetigungToSupplyDetailsMapper mapper = GpkeLieferbeginnLfnBestaetigungToSupplyDetailsMapper.INSTANCE;
		GpkeLieferbeginnLfnBestaetigungToSupplyBeginDateMapper dateMapper = GpkeLieferbeginnLfnBestaetigungToSupplyBeginDateMapper.INSTANCE;

		GpkeStromLieferbeginnLfnBestaetigung request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GpkeStromLieferbeginnLfnBestaetigung.class);

		Date supplyBeginDate = dateMapper.getBeginDate(request).getDate();

		Contract contract = persistenceHandler.loadContract(currentMessage.getContract());
		PointOfDelivery pointOfDelivery = persistenceHandler.loadPointOfDelivery(contract.getPointOfDelivery());
		SupplyDetails supplyDetails = mapper.toSupplyDetails(request, pointOfDelivery);
		updateSupplyDetails(supplyDetails, supplyBeginDate);

		GpkeLieferbeginnLfnBestaetigungToPowerConsumptionPrognosisMapper prognosisMapper =
				GpkeLieferbeginnLfnBestaetigungToPowerConsumptionPrognosisMapper.INSTANCE;

		PowerConsumptionPrognosis currentPowerConsumptionPrognosis =
				UpdateSupplyDetailsHelper.getNetPrognosis(contract, supplyBeginDate);

		VerbrauchNetznutzung verbrauchsdaten = prognosisMapper.getVerbrauchsdaten(request);

		if (verbrauchsdaten != null && currentPowerConsumptionPrognosis != null) {
			PowerConsumptionPrognosisResolved powerConsumptionPrognosisResolved =
					PowerConsumptionPrognosisTool.convertToPowerConsumptionPrognosisResolved(currentPowerConsumptionPrognosis);
			prognosisMapper.updatePowerConsumption(verbrauchsdaten, powerConsumptionPrognosisResolved);

			PowerConsumptionPrognosisTool.convertToPowerConsumptionPrognosis(powerConsumptionPrognosisResolved).save();
		}
	}

	/**
	 * 44002 - GELI_GAS_LIEFERBEGINN_LFN_BESTAETIGUNG
	 */
	private void updateSupplyBeginDataGas(AepMakoMessage currentMessage) throws Exception {
		GeliGasLieferbeginnLfnBestaetigungToSupplyDetailsMapper mapper = GeliGasLieferbeginnLfnBestaetigungToSupplyDetailsMapper.INSTANCE;
		GeliLieferbeginnLfnBestaetigungToSupplyBeginDateMapper dateMapper = GeliLieferbeginnLfnBestaetigungToSupplyBeginDateMapper.INSTANCE;

		GeliGasLieferbeginnLfnBestaetigung request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GeliGasLieferbeginnLfnBestaetigung.class);

		Date supplyBeginDate = dateMapper.getBeginDate(request).getDate();

		Contract contract = persistenceHandler.loadContract(currentMessage.getContract());
		PointOfDelivery pointOfDelivery = persistenceHandler.loadPointOfDelivery(contract.getPointOfDelivery());
		SupplyDetails supplyDetails = mapper.toSupplyDetails(request, pointOfDelivery);
		updateSupplyDetails(supplyDetails, supplyBeginDate);
	}

	private void updateSupplyDetails(SupplyDetails supplyDetailsFromMessage, Date pivotDate) {
		UpdateSupplyDetailsHelper.updateSupplyDetailsFromSupplyBegin(currentSupplyDetailsList, supplyDetailsFromMessage, pivotDate);
	}

	/**
	 * 44035 - GELI_GAS_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE
	 */
	private void updateSupplyBeginMasterdataSyncGas(AepMakoMessage currentMessage) throws Exception {
		GeliGasGeschaeftsdatenanfrageLfUpdateAnfrageToSupplyDetailsMapper mapper =
				GeliGasGeschaeftsdatenanfrageLfUpdateAnfrageToSupplyDetailsMapper.INSTANCE;

		GpkeProcess gpkeProcess = persistenceHandler.loadGpkeProcess(currentMessage.getGpkeProcess());
		SupplyBegin pattern = new SupplyBegin().withSupplyBeginProcess(gpkeProcess);
		List<SupplyBegin> supplyBegins = persistenceHandler.listSupplyBegin(pattern);

		GeliGasGeschaeftsdatenanfrageLfUpdateAnfrage request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GeliGasGeschaeftsdatenanfrageLfUpdateAnfrage.class);

		Date supplyBeginDate = supplyBegins.get(0).getDeliveryDate();

		Contract contract = persistenceHandler.loadContract(currentMessage.getContract());
		PointOfDelivery pointOfDelivery = persistenceHandler.loadPointOfDelivery(contract.getPointOfDelivery());
		SupplyDetails supplyDetails = mapper.toSupplyDetails(request, pointOfDelivery);
		updateSupplyDetails(supplyDetails, supplyBeginDate);
	}

	/**
	 * 55035 - GPKE_STROM_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE
	 */
	private void updateSupplyBeginMasterdataSyncElectricity(AepMakoMessage currentMessage) throws Exception {
		GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrageToSupplyDetailsMapper mapper =
				GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrageToSupplyDetailsMapper.INSTANCE;

		GpkeProcess gpkeProcess = persistenceHandler.loadGpkeProcess(currentMessage.getGpkeProcess());
		SupplyBegin pattern = new SupplyBegin().withSupplyBeginProcess(gpkeProcess);
		List<SupplyBegin> supplyBegins = persistenceHandler.listSupplyBegin(pattern);

		GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrage request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrage.class);

		Date supplyBeginDate = supplyBegins.get(0).getDeliveryDate();

		Contract contract = persistenceHandler.loadContract(currentMessage.getContract());
		PointOfDelivery pointOfDelivery = persistenceHandler.loadPointOfDelivery(contract.getPointOfDelivery());
		SupplyDetails supplyDetails = mapper.toSupplyDetails(request, pointOfDelivery);
		updateSupplyDetails(supplyDetails, supplyBeginDate);

		GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrageToPowerConsumptionPrognosisMapper prognosisMapper =
				GpkeStromGeschaeftsdatenanfrageLfUpdateAnfrageToPowerConsumptionPrognosisMapper.INSTANCE;

		PowerConsumptionPrognosisResolved powerConsumptionPrognosis = new PowerConsumptionPrognosisResolved();
		prognosisMapper.updatePowerConsumption(prognosisMapper.getVerbrauchsdaten(request),
				powerConsumptionPrognosis);
		PowerConsumptionPrognosis currentPowerConsumptionPrognosis =
				UpdateSupplyDetailsHelper.getNetPrognosis(contract, supplyBeginDate);

		if (currentPowerConsumptionPrognosis != null) {
			currentPowerConsumptionPrognosis.setUnit(powerConsumptionPrognosis.getUnit());
			currentPowerConsumptionPrognosis.setConsumptionQualifier(powerConsumptionPrognosis.getConsumptionQualifier());
			currentPowerConsumptionPrognosis.save();
		}
	}

	/**
	 * 55123 - GPKE_STROM_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE
	 */
	private void updatePrognosisDataElectricity(AepMakoMessage currentMessage) throws IOException {
		GpkeStromEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrageToMasterDataMapper mapper =
				GpkeStromEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrageToMasterDataMapper.INSTANCE;

		GpkeStromEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GpkeStromEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage.class);

		SupplyDetails supplyDetailsFromMessage = new SupplyDetails();
		mapper.updateSupplyDetails(request, supplyDetailsFromMessage);

		currentSupplyDetailsList.stream()
				.filter(s -> UpdateSupplyDetailsHelper.isCurrentOrLater(s, mapper.getValidFromDate(request).getDate()))
				.forEach(supplyDetails -> {
					if (supplyDetailsFromMessage.getPrognosisType() != null) {
						supplyDetails.setPrognosisType(supplyDetailsFromMessage.getPrognosisType());
					}
					if (supplyDetailsFromMessage.getUsageType() != null) {
						supplyDetails.setUsageType(supplyDetailsFromMessage.getUsageType());
					}
					if (supplyDetailsFromMessage.getLoadProfileGroup() != null) {
						supplyDetails.setLoadProfileGroup(supplyDetailsFromMessage.getLoadProfileGroup());
					}
					//until now LoadProfileGroup has been mapped to loadProfileDefinition , which is wrong
					//loadProfileDefinition will be set to null to fix this error
					supplyDetails.setLoadProfileDefinition(null);
					supplyDetails.save();
				});

		PowerConsumptionPrognosis powerConsumptionPrognosis = new PowerConsumptionPrognosis();
		mapper.updatePowerConsumptionPrognosis(request, powerConsumptionPrognosis);
		PowerConsumptionPrognosis currentPowerConsumptionPrognosis =
				new ManualPowerConsumptionPrognosisService().getPowerConsumptionPrognosisForDate(currentMessage.getContract(),
						mapper.getValidFromDate(request).getDate());

		if (currentPowerConsumptionPrognosis != null) {
			currentPowerConsumptionPrognosis.setUnit(powerConsumptionPrognosis.getUnit());
			currentPowerConsumptionPrognosis.setConsumptionQualifier(powerConsumptionPrognosis.getConsumptionQualifier());
			currentPowerConsumptionPrognosis.save();
		}
	}

	/**
	 * 44123 - GELI_GAS_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE
	 */
	private void updatePrognosisDataGas(AepMakoMessage currentMessage) throws IOException {
		GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrageToMasterDataMapper mapper =
				GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrageToMasterDataMapper.INSTANCE;

		GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage.class);

		Prognosebasis prognosebasis = Optional.ofNullable(request)
				.map(GeliGasEmpfangSdaBilrelAenderungVomNbMitAbhaengigkeitenLfUpdateStammdatenAnfrage::getStammdaten)
				.map(StammdatenaenderungEmpfangBilanzierungsrelevantVomNbAbhaengigGas::getMarktlokationsdaten)
				.map(CollectionMappingUtils::chooseFirst)
				.map(MarktlokationsdatenStammdatenaenderungEmpfangBilanzierungsrelevantVomNbAbhaengigGas::getPrognosegrundlage)
				.map(PrognosegrundlageStammdatenaenderungEmpfangBilanzierungsrelevantVomNbAbhaengigGas::getPrognosebasis)
				.orElse(null);

		SupplyDetails supplyDetailsFromMessage = new SupplyDetails();
		mapper.updateSupplyDetails(request, supplyDetailsFromMessage);

		currentSupplyDetailsList.stream()
				.filter(s -> UpdateSupplyDetailsHelper.isCurrentOrLater(s, mapper.getValidFromDate(request).getDate()))
				.forEach(supplyDetails -> {
					if (prognosebasis != null) {
						supplyDetails.setPrognosisType(prognosebasis.name());
					}
					if (supplyDetailsFromMessage.getClimateZone() != null) {
						supplyDetails.setClimateZone(supplyDetailsFromMessage.getClimateZone());
					}
					if (supplyDetailsFromMessage.getThermalDefinition() != null) {
						supplyDetails.setThermalDefinition(supplyDetailsFromMessage.getThermalDefinition());
					}
					if (supplyDetailsFromMessage.getThermalProvider() != null) {
						supplyDetails.setThermalProvider(supplyDetailsFromMessage.getThermalProvider());
					}
					if (supplyDetailsFromMessage.getThermalType() != null) {
						supplyDetails.setThermalType(supplyDetailsFromMessage.getThermalType());
					}
					if (supplyDetailsFromMessage.getLoadProfileGroup() != null) {
						supplyDetails.setLoadProfileGroup(supplyDetailsFromMessage.getLoadProfileGroup());
					}
					if (supplyDetailsFromMessage.getLoadProfile() != null) {
						supplyDetails.setLoadProfile(supplyDetailsFromMessage.getLoadProfile());
					}
					if (supplyDetailsFromMessage.getLoadProfileType() != null) {
						supplyDetails.setLoadProfileType(supplyDetailsFromMessage.getLoadProfileType());
					}
					if (supplyDetailsFromMessage.getLoadProfileDefinition() != null) {
						supplyDetails.setLoadProfileDefinition(supplyDetailsFromMessage.getLoadProfileDefinition());
					}
					supplyDetails.save();
				});
	}

	/**
	 * 55126 - GPKE_STROM_EMPFANG_SDA_AENDERUNG_PROGNOSEGRUNDLAGE_LF_UPDATE_STAMMDATEN_ANFRAGE
	 */
	private void updateTimeRowType(AepMakoMessage currentMessage) throws IOException {
		GpkeStromEmpfangSdaAenderungPrognosegrundlageLfUpdateStammdatenAnfrageToMasterDataMapper mapper =
				GpkeStromEmpfangSdaAenderungPrognosegrundlageLfUpdateStammdatenAnfrageToMasterDataMapper.INSTANCE;

		GpkeStromEmpfangSdaAenderungPrognosegrundlageLfUpdateStammdatenAnfrage request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GpkeStromEmpfangSdaAenderungPrognosegrundlageLfUpdateStammdatenAnfrage.class);

		SupplyDetails supplyDetailsFromMessage = new SupplyDetails();
		mapper.applyChanges(request, supplyDetailsFromMessage);

		UpdateSupplyDetailsHelper.updateTimeRowTypes(currentSupplyDetailsList, supplyDetailsFromMessage,
				mapper.getValidFromDate(request).getDate());
	}

	/**
	 * 55112 - GPKE_STROM_EMPFANG_SDA_NICHT_BILREL_AENDERUNG_VOM_NB_LF_UPDATE_STAMMDATEN_ANFRAGE
	 */
	private void updateRateOfTemperatureDependence(AepMakoMessage currentMessage) throws IOException {
		GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrageToMasterDataMapper mapper =
				GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrageToMasterDataMapper.INSTANCE;

		GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrage request =
				AepMakoMessageFactory.loadMessagePayload(currentMessage,
						GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrage.class);

		BigDecimal rateOfTemeratureDependence = Optional.ofNullable(request)
				.map(GpkeStromEmpfangSdaNichtBilrelAenderungVomNbLfUpdateStammdatenAnfrage::getStammdaten)
				.map(StammdatenaenderungEmpfangNichtBilanzierungsrelevantVomNb::getMarktlokationsdaten)
				.map(CollectionMappingUtils::chooseFirst)
				.map(MarktlokationsdatenStammdatenaenderungEmpfangNichtBilanzierungsrelevantVomNb::getVerbrauch)
				.map(VerbrauchStammdatenaenderungEmpfangNichtBilanzierungsrelevantVomNb::getAnteilTemperaturabhaengigkeit)
				.orElse(null);

		UpdateSupplyDetailsHelper.updateRateOfTemperatureDependence(currentSupplyDetailsList, mapper.getValidFromDate(request).getDate(),
				rateOfTemeratureDependence);
	}

	private String getSql() {
		// @formatter:off
		return "select * from FL_AEPMAKOMESSAGE \n"
				+ "where (messagetype_ = 'GPKE_STROM_EMPFANG_SDA_AENDERUNG_PROGNOSEGRUNDLAGE_LF_UPDATE_STAMMDATEN_ANFRAGE' and payload_ like '%\"prognosebasis\":%'\n"
				+ "or messagetype_ = 'GPKE_STROM_EMPFANG_SDA_NICHT_BILREL_AENDERUNG_VOM_NB_LF_UPDATE_STAMMDATEN_ANFRAGE' and payload_ like '%\"anteilTemperaturabhaengigkeit\":%'\n"
				+ "or messagetype_ = 'GPKE_STROM_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE' and payload_ like '%tagesparameterabhaengigesLastprofil%'\n"
				+ "or messagetype_ = 'GELI_GAS_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE' and payload_ like '%\"prognosebasis\":%'\n"
				+ "or messagetype_ = 'GELI_GAS_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE' and payload_ like '%\"klimazoneTemperaturmessstelle\"%'\n"
				+ "or messagetype_ = 'GELI_GAS_EMPFANG_SDA_BILREL_AENDERUNG_VOM_NB_MIT_ABHAENGIGKEITEN_LF_UPDATE_STAMMDATEN_ANFRAGE' and payload_ like '%\"codevergebendeStelle\":%'\n"
				+ "or messagetype_ = 'GPKE_STROM_LIEFERBEGINN_LFN_BESTAETIGUNG' and payload_ like '%tagesparameterabhaengigesLastprofil%'\n"
				+ "or messagetype_ = 'GPKE_STROM_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE' and payload_ like '%tagesparameterabhaengigesLastprofil%'\n"
				+ "or messagetype_ = 'GELI_GAS_LIEFERBEGINN_LFN_BESTAETIGUNG' and payload_ like '%klimazoneTemperaturmessstelle%'\n"
				+ "or messagetype_ = 'GELI_GAS_LIEFERBEGINN_LFN_BESTAETIGUNG' and payload_ like '%\"codevergebendeStelle\"%'\n"
				+ "or messagetype_ = 'GELI_GAS_GESCHAEFTSDATENANFRAGE_LF_UPDATE_ANFRAGE' and payload_ like '%klimazoneTemperaturmessstelle%'\n"
				+ ") and state_ = 'SUCCESS'\n"
				+ "order by contract_, id_ asc" ;
		// @formatter:on
	}

}
