package com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ControllableResource;
import com.nextlevel.fastlane.myBusinessSupplier.bo.DataOfControllableResource;
import com.nextlevel.fastlane.myBusinessSupplier.bo.TechnicalResource;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;

public class ControllableResourceFinder {
	public static List<ControllableResource> getControllableResources(Long marketLocationId, Long meteringLocationId, Date pivotDate) {
		List<TechnicalResource> technicalResources =
				TechnicalResourceFinder.getTechnicalRessources(marketLocationId, meteringLocationId, pivotDate);
		List<ControllableResource> controllableResources = new LinkedList<>();

		for (TechnicalResource technicalResource :
				technicalResources) {
			if (technicalResource.getControllableResourceId() != null) {
				try {
					controllableResources.add(
							PersistenceHandler.getInstance().loadControllableResource(technicalResource.getControllableResourceId()));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return controllableResources;
	}

	public static List<DataOfControllableResource> listDataOfControllableRessourceByControllableResourceId(Long id) {
		return PersistenceHandler.getInstance().listDataOfControllableResource(new DataOfControllableResource().withContralableResourceId(
				id));
	}

	public static DataOfControllableResource getDataOfControllableRessource(Long controllableResourceId, Date pivotDate) {
		return TimeSliceTool.tryGetCurrent(listDataOfControllableRessourceByControllableResourceId(controllableResourceId), pivotDate);
	}
}