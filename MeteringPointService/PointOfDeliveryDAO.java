/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.persistence.myBusinessSupplier.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.platform.connection.CommandProcessor;

public class PointOfDeliveryDAO extends PointOfDeliveryDAOBase {

	public Map<PointOfDelivery, List<PointOfDelivery>> listPointOfDeliveries(Contract contract) throws Exception {
		return CommandProcessor.process(connection -> {
			PointOfDeliveryDAO marketDAO = new PointOfDeliveryDAO();
			marketDAO.setTableName("marketlocation");
			PointOfDeliveryDAO meteringDAO = new PointOfDeliveryDAO();
			meteringDAO.setTableName("meteringlocation");

			String query = "SELECT DISTINCT 1" + marketDAO.getAllColumnsWithAlias() + meteringDAO.getAllColumnsWithAlias()
					+ " FROM " + ContractDAO.TABLE_NAME + " AS contract "
					+ " LEFT JOIN " + TABLE_NAME + " AS marketlocation ON marketlocation." + column_id + " = contract."
					+ ContractDAO.column_PointOfDelivery
					+ " LEFT JOIN " + TABLE_NAME + " AS meteringlocation ON meteringlocation." + column_parent + " = marketlocation."
					+ column_id
					+ " WHERE contract." + ContractDAO.column_id + " = ? ";

			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setLong(1, contract.getId());
			ResultSet res = pstmt.executeQuery();

			PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();
			PointOfDelivery market = null;
			List<PointOfDelivery> meteringLocs = new ArrayList<>();
			while (res.next()) {
				if (market == null) {
					market = persistenceHandler.getPointOfDeliveryFromPVO(marketDAO.buildPointOfDeliveryPVO(res));
				}

				PointOfDelivery metering = persistenceHandler.getPointOfDeliveryFromPVO(meteringDAO.buildPointOfDeliveryPVO(res));
				meteringLocs.add(metering);
			}

			Map<PointOfDelivery, List<PointOfDelivery>> result = new HashMap<>();
			result.put(market, meteringLocs);
			return result;
		});
	}
}