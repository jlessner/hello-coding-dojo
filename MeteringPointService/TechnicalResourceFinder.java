package com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery;

import java.util.Date;
import java.util.List;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.StorageDataOfTechnicalResource;
import com.nextlevel.fastlane.myBusinessSupplier.bo.TechnicalResource;
import com.nextlevel.fastlane.myBusinessSupplier.bo.TechnicalResourceConsumptionType;
import com.nextlevel.fastlane.myBusinessSupplier.bo.UsageDataOfTechnicalResource;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;

public class TechnicalResourceFinder {
	public static TechnicalResource getTechnicalResourceByControllableResource(Long controllableResourceId, Date date) {
		TechnicalResource pattern = new TechnicalResource().withControllableResourceId(controllableResourceId);
		List<TechnicalResource> technicalResources = PersistenceHandler.getInstance().listTechnicalResource(pattern);
		return TimeSliceTool.tryGetCurrent(technicalResources, date);
	}

	public static TechnicalResource getTechnicalResource(String identification, Date date) {
		TechnicalResource pattern = new TechnicalResource().withIdentification(identification);
		List<TechnicalResource> technicalResources = PersistenceHandler.getInstance().listTechnicalResource(pattern);
		return TimeSliceTool.tryGetCurrent(technicalResources, date);
	}

	public static List<TechnicalResource> getTechnicalRessources(Long marketLocationId, Long meteringLocationId, Date pivotDate) {
		TechnicalResource pattern = new TechnicalResource().withMaloId(marketLocationId).withMeloId(meteringLocationId);
		List<TechnicalResource> technicalResourceList = PersistenceHandler.getInstance().listTechnicalResource(pattern);
		return TimeSliceTool.tryGetCurrentList(technicalResourceList, pivotDate);
	}

	public static List<UsageDataOfTechnicalResource> listUsageDataOfTechnicalResourceByTechnicalResourceId(Long id) {
		return PersistenceHandler.getInstance().listUsageDataOfTechnicalResource(new UsageDataOfTechnicalResource().withTechnicalResourceId(
				id));
	}

	public static UsageDataOfTechnicalResource getUsageDataOfTechnicalResourceByTechnicalResourceId(Long id, Date pivotDate) {
		return TimeSliceTool.tryGetCurrent(listUsageDataOfTechnicalResourceByTechnicalResourceId(id), pivotDate);
	}

	public static List<StorageDataOfTechnicalResource> listStorageDataOfTechnicalResourceByTechnicalResourceId(Long id) {
		return PersistenceHandler.getInstance()
				.listStorageDataOfTechnicalResource(new StorageDataOfTechnicalResource().withTechnicalResourceId(
						id));
	}

	public static StorageDataOfTechnicalResource getStorageDataOfTechnicalResourceByTechnicalResourceId(Long id, Date pivotDate) {
		return TimeSliceTool.tryGetCurrent(listStorageDataOfTechnicalResourceByTechnicalResourceId(id), pivotDate);
	}

	public static List<TechnicalResourceConsumptionType> listTechnicalResourceConsumptionTypesByTechnicalResourceId(Long id) {
		return PersistenceHandler.getInstance()
				.listTechnicalResourceConsumptionType(new TechnicalResourceConsumptionType().withTechnicalResourceId(
						id));
	}
}