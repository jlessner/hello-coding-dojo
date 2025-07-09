package com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ContractDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.PointOfDeliveryDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.ContractPVO;
import com.nextlevel.platform.connection.CommandProcessor;

/**
 * Name of this class has to be different from existing class ContractFinder - possible future merge of both classes
 */
public class BaseContractFinder {

	public static Contract getContractByMeteringLocation(PointOfDelivery meteringLocation) throws Exception {
		// @formatter:off
		String query = "SELECT contract.* FROM " + PointOfDeliveryDAO.TABLE_NAME + " AS melo"
				+ " LEFT JOIN " + PointOfDeliveryDAO.TABLE_NAME + " AS malo ON malo." + PointOfDeliveryDAO.column_id + " = melo." + PointOfDeliveryDAO.column_parent
				+ " LEFT JOIN " + ContractDAO.TABLE_NAME + " AS contract ON contract." + ContractDAO.column_PointOfDelivery + " = malo." + PointOfDeliveryDAO.column_id
				+ " WHERE melo." + PointOfDeliveryDAO.column_id + " = ? "
				+ " AND contract." + ContractDAO.column_id + " IS NOT NULL ";
		// @formatter:on

		return getContractByPointOfDeliveryId(meteringLocation, query);
	}

	public static Contract getContractByMarketLocation(PointOfDelivery marketLocation) throws Exception {
		// @formatter:off
		String query = "SELECT contract.* FROM " + ContractDAO.TABLE_NAME + " AS contract "
				+ " WHERE contract." + ContractDAO.column_PointOfDelivery + " = ? ";
		// @formatter:on

		return getContractByPointOfDeliveryId(marketLocation, query);
	}

	private static Contract getContractByPointOfDeliveryId(PointOfDelivery pointOfDelivery, String query)
			throws Exception, SQLException {
		Validate.notNull(pointOfDelivery.getId(), "Id must be set.");
		return CommandProcessor.process(connection -> {
			ResultSet res = executeQuery(pointOfDelivery, query, connection);
			List<Contract> contracts = extractContractsFromResultSet(res);
			return tryGetOneContractWithException(pointOfDelivery, contracts);
		});
	}

	private static Contract tryGetOneContractWithException(PointOfDelivery pointOfDelivery, List<Contract> contracts) throws Exception {
		if (contracts.size() != 1) {
			throw new Exception(
					"Number of contracts found for PointOfDelivery with ID: " + pointOfDelivery.getId() + " was not one but "
							+ contracts.size());
		}

		return contracts.get(0);
	}

	private static List<Contract> extractContractsFromResultSet(ResultSet res) throws SQLException, IOException {
		ContractDAO contractDAO = new ContractDAO();
		List<Contract> contracts = new ArrayList<>();
		while (res.next()) {
			ContractPVO contractPVO = contractDAO.buildContractPVO(res);
			contracts.add(PersistenceHandler.getInstance().getContractFromPVO(contractPVO));
		}
		return contracts;
	}

	private static ResultSet executeQuery(PointOfDelivery pointOfDelivery, String query, Connection connection) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setLong(1, pointOfDelivery.getId());
		ResultSet res = pstmt.executeQuery();
		return res;
	}
}