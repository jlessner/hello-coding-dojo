package com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery;

import java.util.Date;
import java.util.List;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ProductData;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;

public class ProductDataFinder {
	public static List<ProductData> listCurrentProductDataByPointOfDeliveryId(Long id, Date pivotDate) {
		return TimeSliceTool.tryGetCurrentList(listProductDataByPointOfDeliveryId(id), pivotDate);
	}

	public static List<ProductData> listProductDataByPointOfDeliveryId(Long id) {
		return PersistenceHandler.getInstance().listProductData(new ProductData().withPointOfDeliveryId(id));
	}

	public static List<ProductData> listProductDataByControllableResourceId(Long id, Date pivotDate) {
		return TimeSliceTool.tryGetCurrentList(listProductDataByControllableResourceId(id), pivotDate);
	}

	public static List<ProductData> listProductDataByControllableResourceId(Long id) {
		return PersistenceHandler.getInstance().listProductData(new ProductData().withControllableResourceId(id));
	}
}