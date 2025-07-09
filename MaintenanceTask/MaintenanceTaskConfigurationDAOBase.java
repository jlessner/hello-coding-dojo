/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.persistence.myBusinessSupplier.dao;

import com.nextlevel.fastlane.core.persistence.dao.TypeOfOperation;
import com.nextlevel.fastlane.core.usermgmt.FastlaneUserManagement;
import com.nextlevel.platform.usermanagement.UserManagementException;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.connection.DatabaseTool;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import com.nextlevel.platform.connection.ConnectionManager;
import com.nextlevel.platform.connection.StatementHandler;
import com.nextlevel.fastlane.core.persistence.dao.DAOBase;
import com.nextlevel.fastlane.core.persistence.dao.UtilDAO;
import org.apache.commons.lang3.StringUtils;
import com.nextlevel.platform.date.DateTool;

import com.nextlevel.fastlane.myBusinessSupplier.bo.log.LogMaintenanceTaskConfiguration;

import org.apache.log4j.Logger;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.MaintenanceTaskConfigurationPVO;

public class MaintenanceTaskConfigurationDAOBase implements DAOBase<MaintenanceTaskConfigurationPVO> {

	// TODO: Replace by com.nextlevel.platform.connection.enums.ColumnAliasType from connection >= 2.39
	private enum ColumnAliasType {
		COLUMNNAME, TABLE_AND_COLUMNNAME, HASH;
	}

	private static final String LOGLOC = "com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.MaintenanceTaskConfigurationDAOBase: ";
	protected Logger logger = Logger.getLogger(this.getClass());
	protected boolean doAuditLog = true;

	protected final String datasourceAlias = "fastlane";

	public static final String TABLE_NAME = "FL_MAINTENATASKCONFIGUR";

	public static final String TABLE_NAME_HISTORY = "LOG_MAINTENATASKCONFIGUR";

	private static final String DEFAULT_TABLE_NAME = "";

	private String tableName = DEFAULT_TABLE_NAME;

	// If true: generate short alias names to work with Oracle DB
	private boolean useShortAlias = false;

	static {
		StatementHandler.addSqlStatement(TABLE_NAME, "INSERT", "INSERT INTO " + TABLE_NAME + "(CLASSNAME_, EXECUTIONSTATE_, LASTEXECUTION_, EXECUTIONTYPE_, PRIORITY_, DESCRIPTION_) values (?,?,?,?,?,?)");

		StatementHandler.addSqlStatement(TABLE_NAME_HISTORY, "INSERT", "INSERT INTO " + TABLE_NAME_HISTORY
				+ "(CLASSNAME_, EXECUTIONSTATE_, LASTEXECUTION_, EXECUTIONTYPE_, PRIORITY_, DESCRIPTION_, id_, CREATED_LOG_, USER_LOG_, TYPEOFOPERATION_LOG_) values (?,?,?,?,?,?,?,?,?,?)");

		StatementHandler.addSqlStatement(TABLE_NAME_HISTORY, "LIST", "SELECT * FROM " + TABLE_NAME_HISTORY + " WHERE ID_= ?");

		StatementHandler.addSqlStatement(TABLE_NAME, "UPDATE", "UPDATE " + TABLE_NAME + " SET CLASSNAME_ = ?, EXECUTIONSTATE_ = ?, LASTEXECUTION_ = ?, EXECUTIONTYPE_ = ?, PRIORITY_ = ?, DESCRIPTION_ = ? WHERE ID_= ? ");

		StatementHandler.addSqlStatement(TABLE_NAME, "DELETE", "DELETE FROM FL_MAINTENATASKCONFIGUR WHERE ID_ = ?");
		StatementHandler.addSqlStatement(TABLE_NAME_HISTORY, "DELETE", "DELETE FROM LOG_MAINTENATASKCONFIGUR WHERE ID_ = ?");
		StatementHandler.addSqlStatement(TABLE_NAME, "LOAD", "SELECT * FROM FL_MAINTENATASKCONFIGUR WHERE ID_ = ?");
		StatementHandler.addSqlStatement(TABLE_NAME, "LIST", "SELECT * FROM FL_MAINTENATASKCONFIGUR");
	}

	private static final String AS_QUOTE = " AS \"";

	public static final String column_id = "id_";
	public static final String column_className = "className_";
	public static final String column_executionState = "executionState_";
	public static final String column_lastExecution = "lastExecution_";
	public static final String column_executionType = "executionType_";
	public static final String column_priority = "priority_";
	public static final String column_description = "description_";

	public String getAllColumns() {
		final StringBuilder result = new StringBuilder();

		result.append(',');
		result.append(this.tableName);
		result.append(column_id);
		result.append(',');
		result.append(this.tableName);
		result.append(column_className);
		result.append(',');
		result.append(this.tableName);
		result.append(column_executionState);
		result.append(',');
		result.append(this.tableName);
		result.append(column_lastExecution);
		result.append(',');
		result.append(this.tableName);
		result.append(column_executionType);
		result.append(',');
		result.append(this.tableName);
		result.append(column_priority);
		result.append(',');
		result.append(this.tableName);
		result.append(column_description);

		return result.toString();
	}

	public String getAllColumnsWithAlias() {
		final StringBuilder result = new StringBuilder();

		result.append(',');
		result.append(this.tableName);
		result.append(column_id);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_id));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_className);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_className));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_executionState);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_executionState));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_lastExecution);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_lastExecution));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_executionType);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_executionType));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_priority);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_priority));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_description);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_description));
		result.append("\"");

		return result.toString();
	}

	public String getColumnAlias(final String columnName) {
		return getColumnAlias(columnName, getColumnAliasType());
	}

	private ColumnAliasType getColumnAliasType() {
		if (this.useShortAlias) {
			return ColumnAliasType.HASH;
		} else if (!DEFAULT_TABLE_NAME.equals(tableName)) {
			return ColumnAliasType.TABLE_AND_COLUMNNAME;
		}

		return ColumnAliasType.COLUMNNAME;
	}

	public String getColumnAlias(final String columnName, final ColumnAliasType columnAliasType) {
		final String alias;
		switch (columnAliasType) {
		case TABLE_AND_COLUMNNAME:
			alias = this.tableName + columnName;
			break;
		case HASH:
			alias = Integer.toHexString(TABLE_NAME.hashCode()) + Integer.toHexString(columnName.hashCode());
			break;
		default:
			alias = columnName;
			break;
		}
		return alias;
	}

	public void setUseShortColumnAlias(boolean useShortAlias) {
		this.useShortAlias = useShortAlias;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName + ".";
	}

	@Override
	public void save(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO) throws ClassNotFoundException, SQLException {
		insert(maintenanceTaskConfigurationPVO, null);
	}

	@Override
	public void save(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, Connection con) throws ClassNotFoundException, SQLException {
		insert(maintenanceTaskConfigurationPVO, con);
	}

	protected void onInsert(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, Connection con) throws SQLException {
	}

	protected void setValues(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, PreparedStatement pstmt) throws SQLException {

		if (maintenanceTaskConfigurationPVO.getClassName() == null)
			pstmt.setObject(1, null);
		else
			pstmt.setString(1, maintenanceTaskConfigurationPVO.getClassName());

		if (maintenanceTaskConfigurationPVO.getExecutionState() == null)
			pstmt.setObject(2, null);
		else
			pstmt.setString(2, maintenanceTaskConfigurationPVO.getExecutionState());

		if (maintenanceTaskConfigurationPVO.getLastExecution() == null)
			pstmt.setObject(3, null);
		else
			pstmt.setTimestamp(3, maintenanceTaskConfigurationPVO.getLastExecution());

		if (maintenanceTaskConfigurationPVO.getExecutionType() == null)
			pstmt.setObject(4, null);
		else
			pstmt.setString(4, maintenanceTaskConfigurationPVO.getExecutionType());

		if (maintenanceTaskConfigurationPVO.getPriority() == null)
			pstmt.setObject(5, null);
		else
			pstmt.setInt(5, maintenanceTaskConfigurationPVO.getPriority());

		if (maintenanceTaskConfigurationPVO.getDescription() == null)
			pstmt.setObject(6, null);
		else
			pstmt.setString(6, maintenanceTaskConfigurationPVO.getDescription());

	}

	private void insert(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, Connection con) throws SQLException, ClassNotFoundException {

		boolean connectionCreated = false;

		PreparedStatement pstmt = null;

		try {

			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				try {
					con = new ConnectionManager().getConnection(datasourceAlias);
					connectionCreated = true;
				} catch (ClassNotFoundException e) {
					throw new SQLException("cannot get Connection", e);
				}
			}

			onInsert(maintenanceTaskConfigurationPVO, con);
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME, "INSERT", "ID_");

			setValues(maintenanceTaskConfigurationPVO, pstmt);

			pstmt.execute();

			long id = handler.getGeneratedIdFromStatementAsLong();
			maintenanceTaskConfigurationPVO.setId(id);

			if (doAuditLog) {
				insertIntoHistory(maintenanceTaskConfigurationPVO, TypeOfOperation.INSERT, con);
			}

		} catch (SQLException sql) {
			logger.error("Error inserting MaintenanceTaskConfigurationPVO", sql);
			logger.info(LOGLOC + maintenanceTaskConfigurationPVO.toString());
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(null, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
	}

	public void insertIntoHistory(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, TypeOfOperation type, Connection con) throws SQLException, ClassNotFoundException {

		if (maintenanceTaskConfigurationPVO.getId() == null)
			throw new IllegalArgumentException("cannot save an entry into History without primary Key value");

		PreparedStatement pstmt = null;

		try {
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME_HISTORY, "INSERT");

			setValues(maintenanceTaskConfigurationPVO, pstmt);

			pstmt.setLong(7, maintenanceTaskConfigurationPVO.getId());

			Date logCreationDate = CommandProcessor.getLogCreationDate();
			if (logCreationDate == null) {
				logCreationDate = DateTool.getCurrent();
			}
			pstmt.setTimestamp(8, new Timestamp(logCreationDate.getTime()));

			String activeUser = CommandProcessor.getLogUser();
			if (activeUser == null) {
				try {
					activeUser = new FastlaneUserManagement().getActiveUser();
				} catch (UserManagementException e) {
					activeUser = "system";
				}
			}
			pstmt.setString(9, activeUser);
			pstmt.setInt(10, type.getId());

			pstmt.execute();

		} catch (SQLException sql) {
			logger.error("Error inserting histroy for MaintenanceTaskConfigurationPVO", sql);
			logger.info(LOGLOC + maintenanceTaskConfigurationPVO.toString());
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(null, pstmt, logger);
		}
	}

	protected void onUpdate(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, Connection con) throws SQLException {
	}

	@Override
	public final boolean update(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO) throws SQLException, ClassNotFoundException {
		return update(maintenanceTaskConfigurationPVO, null);
	}

	public final boolean update(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, Connection con) throws SQLException, ClassNotFoundException {
		boolean connectionCreated = false;
		PreparedStatement pstmt = null;
		boolean updated = false;
		try {
			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}

			onUpdate(maintenanceTaskConfigurationPVO, con);
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME, "UPDATE");

			setValues(maintenanceTaskConfigurationPVO, pstmt);

			pstmt.setLong(7, maintenanceTaskConfigurationPVO.getId());

			int up = pstmt.executeUpdate();
			if (up != 0) {
				updated = true;
			}

			if (doAuditLog) {
				insertIntoHistory(maintenanceTaskConfigurationPVO, TypeOfOperation.UPDATE, con);
			}
		} catch (SQLException sql) {
			logger.error("Error updating MaintenanceTaskConfigurationPVO", sql);
			logger.info(LOGLOC + maintenanceTaskConfigurationPVO.toString());
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(null, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}

		return updated;
	}

	@Override
	public void delete(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO) throws ClassNotFoundException, SQLException {
		delete(maintenanceTaskConfigurationPVO.getId());
	}

	public void delete(Long id) throws ClassNotFoundException, SQLException {
		Connection con = null;
		delete(id, con);
	}

	public void delete(Long id, Connection con) throws ClassNotFoundException, SQLException {
		boolean connectionCreated = false;
		try {
			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}

			if (doAuditLog) {
				insertIntoHistory(load(id, con), TypeOfOperation.DELETE, con);
			}

			StatementHandler handler = new StatementHandler(con);
			executeDeleteStatement(id, handler, TABLE_NAME);
		} catch (SQLException sql) {
			logger.error("Error deleting MaintenanceTaskConfigurationPVO", sql);
			throw sql;
		} catch (IOException e) {
			logger.error("Error deleting MaintenanceTaskConfigurationPVO", e);
			throw new SQLException(e);
		} finally {
			if (connectionCreated && con != null)
				con.close();
		}
	}

	/**
	 * This method provides a GDPR compliant delete method. This means, that not only the entity is deleted from the database
	 * but also all corresponding audit log entries!
	 * <p>
	 * <b>CAUTION:</b> this method also deletes <b>ALL</b> corresponding audit log entries!
	 */
	public void deleteEntityAndAllAuditLogs(Long id, Connection con) throws ClassNotFoundException, SQLException {
		boolean connectionCreated = false;
		try {
			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}

			StatementHandler handler = new StatementHandler(con);
			executeDeleteStatement(id, handler, TABLE_NAME);
			executeDeleteStatement(id, handler, TABLE_NAME_HISTORY);
		} catch (SQLException sql) {
			logger.error("Error deleting MaintenanceTaskConfigurationPVO", sql);
			throw sql;
		} finally {
			if (connectionCreated && con != null)
				con.close();
		}
	}

	private void executeDeleteStatement(Long id, StatementHandler handler, String tableName) throws SQLException {
		try (PreparedStatement pstmt = handler.getPreparedStatement(tableName, "DELETE")) {
			pstmt.setLong(1, id);
			pstmt.execute();
		}
	}

	public MaintenanceTaskConfigurationPVO load(Long id) throws ClassNotFoundException, SQLException, IOException {
		return load(id, null);
	}

	public MaintenanceTaskConfigurationPVO load(Long id, Connection con) throws ClassNotFoundException, SQLException, IOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO = null;
		boolean connectionCreated = false;
		setUseShortColumnAlias(false);
		try {

			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME, "LOAD");

			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				maintenanceTaskConfigurationPVO = buildMaintenanceTaskConfigurationPVO(rs, ColumnAliasType.COLUMNNAME);
				break;
			}
		} catch (SQLException sql) {
			logger.error("Error loading MaintenanceTaskConfigurationPVO", sql);
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return maintenanceTaskConfigurationPVO;
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskConfigurationPVO, false);
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, Connection con) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskConfigurationPVO, false, con);
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskConfigurationPVO, like, null, null);
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, Connection con) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskConfigurationPVO, like, null, con);
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskConfigurationPVO, like, sqlWherePart, null);
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart, Connection con) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskConfigurationPVO, like, sqlWherePart, con, null);
	}

	public PreparedStatement addValuesFromFilterObjectAndPrepareStatement(String sql, MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart, Connection con, Integer limit) throws SQLException {
		return addValuesFromFilterObjectAndPrepareStatement(sql, maintenanceTaskConfigurationPVO, like, sqlWherePart, con, limit, 0);
	}

	public PreparedStatement addValuesFromFilterObjectAndPrepareStatement(String sql, MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart, Connection con, Integer limit, Integer offset)
			throws SQLException {
		PreparedStatement pstmt = null;

		boolean first = true;

		if (maintenanceTaskConfigurationPVO != null) {

			if (maintenanceTaskConfigurationPVO.getId() != null && maintenanceTaskConfigurationPVO.getId() != -1) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_id + " = ? ";
			}

			if (maintenanceTaskConfigurationPVO.getClassName() != null && maintenanceTaskConfigurationPVO.getClassName().length() > 0) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				if (like)
					sql += column_className + " LIKE ? ";
				else
					sql += column_className + " = ? ";
			}

			if (maintenanceTaskConfigurationPVO.getExecutionState() != null && maintenanceTaskConfigurationPVO.getExecutionState().length() > 0) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				if (like)
					sql += column_executionState + " LIKE ? ";
				else
					sql += column_executionState + " = ? ";
			}

			if (maintenanceTaskConfigurationPVO.getLastExecution() != null) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_lastExecution + " = ? ";
			}

			if (maintenanceTaskConfigurationPVO.getExecutionType() != null && maintenanceTaskConfigurationPVO.getExecutionType().length() > 0) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				if (like)
					sql += column_executionType + " LIKE ? ";
				else
					sql += column_executionType + " = ? ";
			}

			if (maintenanceTaskConfigurationPVO.getPriority() != null && maintenanceTaskConfigurationPVO.getPriority() != -1) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_priority + " = ? ";
			}

			if (maintenanceTaskConfigurationPVO.getDescription() != null && maintenanceTaskConfigurationPVO.getDescription().length() > 0) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				if (like)
					sql += column_description + " LIKE ? ";
				else
					sql += column_description + " = ? ";
			}

		}
		if (sqlWherePart != null) {
			if (!first)
				sql += "AND ";
			else
				sql += "WHERE ";
			sql += sqlWherePart;
		}

		if (limit != null) {
			if (!StringUtils.containsIgnoreCase(sql, "ORDER BY")) {
				sql += " ORDER BY id_";
			}
			sql = new StatementHandler(con).addLimitAndOffset(sql, limit, offset);
		}

		pstmt = con.prepareStatement(sql);
		if (maintenanceTaskConfigurationPVO != null) {
			int pos = 1;

			if (maintenanceTaskConfigurationPVO.getId() != null && maintenanceTaskConfigurationPVO.getId() != -1) {

				pstmt.setLong(pos, maintenanceTaskConfigurationPVO.getId());
				pos++;
			}

			if (maintenanceTaskConfigurationPVO.getClassName() != null && maintenanceTaskConfigurationPVO.getClassName().length() > 0) {
				if (like)
					pstmt.setString(pos, "%" + maintenanceTaskConfigurationPVO.getClassName() + "%");
				else
					pstmt.setString(pos, maintenanceTaskConfigurationPVO.getClassName());
				pos++;
			}

			if (maintenanceTaskConfigurationPVO.getExecutionState() != null && maintenanceTaskConfigurationPVO.getExecutionState().length() > 0) {
				if (like)
					pstmt.setString(pos, "%" + maintenanceTaskConfigurationPVO.getExecutionState() + "%");
				else
					pstmt.setString(pos, maintenanceTaskConfigurationPVO.getExecutionState());
				pos++;
			}

			if (maintenanceTaskConfigurationPVO.getLastExecution() != null) {

				pstmt.setTimestamp(pos, maintenanceTaskConfigurationPVO.getLastExecution());
				pos++;
			}

			if (maintenanceTaskConfigurationPVO.getExecutionType() != null && maintenanceTaskConfigurationPVO.getExecutionType().length() > 0) {
				if (like)
					pstmt.setString(pos, "%" + maintenanceTaskConfigurationPVO.getExecutionType() + "%");
				else
					pstmt.setString(pos, maintenanceTaskConfigurationPVO.getExecutionType());
				pos++;
			}

			if (maintenanceTaskConfigurationPVO.getPriority() != null && maintenanceTaskConfigurationPVO.getPriority() != -1) {

				pstmt.setInt(pos, maintenanceTaskConfigurationPVO.getPriority());
				pos++;
			}

			if (maintenanceTaskConfigurationPVO.getDescription() != null && maintenanceTaskConfigurationPVO.getDescription().length() > 0) {
				if (like)
					pstmt.setString(pos, "%" + maintenanceTaskConfigurationPVO.getDescription() + "%");
				else
					pstmt.setString(pos, maintenanceTaskConfigurationPVO.getDescription());
				pos++;
			}

		}
		return pstmt;
	}

	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart, Connection con, Integer limit) throws ClassNotFoundException, SQLException,
			IOException {
		return list(maintenanceTaskConfigurationPVO, like, sqlWherePart, con, limit, 0);
	}

	public List<MaintenanceTaskConfigurationPVO> list(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart, Connection con, Integer limit, Integer offset) {
		ArrayList<MaintenanceTaskConfigurationPVO> maintenanceTaskConfigurationPVOList = new ArrayList<MaintenanceTaskConfigurationPVO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean connectionCreated = false;
		setUseShortColumnAlias(false);
		String sql = "SELECT * FROM " + TABLE_NAME + " ";
		try {
			try {

				if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
					con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
				}

				if (con == null) {
					con = new ConnectionManager().getConnection(datasourceAlias);
					connectionCreated = true;
				}

				pstmt = addValuesFromFilterObjectAndPrepareStatement(sql, maintenanceTaskConfigurationPVO, like, sqlWherePart, con, limit, offset);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO2 = buildMaintenanceTaskConfigurationPVO(rs, ColumnAliasType.COLUMNNAME);
					maintenanceTaskConfigurationPVOList.add(maintenanceTaskConfigurationPVO2);
				}
			} finally {
				DatabaseTool.closeQuietly(rs, pstmt, logger);
				if (connectionCreated && con != null)
					con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return maintenanceTaskConfigurationPVOList;
	}

	public Long count(MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO, boolean like, String sqlWherePart, Connection con) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean connectionCreated = false;
		String sql = "SELECT COUNT(*) as number FROM " + TABLE_NAME + " ";
		try {
			try {

				if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
					con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
				}

				if (con == null) {
					con = new ConnectionManager().getConnection(datasourceAlias);
					connectionCreated = true;
				}

				pstmt = addValuesFromFilterObjectAndPrepareStatement(sql, maintenanceTaskConfigurationPVO, like, sqlWherePart, con, null);

				rs = pstmt.executeQuery();
				rs.next();
				return rs.getLong("number");
			} finally {
				DatabaseTool.closeQuietly(rs, pstmt, logger);
				if (connectionCreated && con != null)
					con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list() throws ClassNotFoundException, SQLException, IOException {
		List<MaintenanceTaskConfigurationPVO> ret = null;
		Connection con = null;
		try {
			con = new ConnectionManager().getConnection(datasourceAlias);
			ret = list(con);
		} finally {
			if (con != null)
				con.close();
		}

		return ret;
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> list(Connection con) throws ClassNotFoundException, SQLException, IOException {
		List<MaintenanceTaskConfigurationPVO> maintenancetaskconfigurationpvoList = new ArrayList<MaintenanceTaskConfigurationPVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean connectionCreated = false;
		setUseShortColumnAlias(false);
		try {
			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME, "LIST");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MaintenanceTaskConfigurationPVO maintenancetaskconfigurationpvo = buildMaintenanceTaskConfigurationPVO(rs, ColumnAliasType.COLUMNNAME);
				maintenancetaskconfigurationpvoList.add(maintenancetaskconfigurationpvo);
			}
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return maintenancetaskconfigurationpvoList;
	}

	@Override
	public List<MaintenanceTaskConfigurationPVO> select(String sql) throws ClassNotFoundException, SQLException, IOException {
		List<MaintenanceTaskConfigurationPVO> maintenancetaskconfigurationpvoList = new ArrayList<MaintenanceTaskConfigurationPVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean connectionCreated = false;
		try {
			if (con == null && com.nextlevel.platform.connection.CommandProcessor.getSharedConnection() != null) {
				con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();
			}

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MaintenanceTaskConfigurationPVO maintenancetaskconfigurationpvo = buildMaintenanceTaskConfigurationPVO(rs);
				maintenancetaskconfigurationpvoList.add(maintenancetaskconfigurationpvo);
			}
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return maintenancetaskconfigurationpvoList;
	}

	public List<LogMaintenanceTaskConfiguration> listLog(Long id) throws ClassNotFoundException, SQLException, IOException {
		ArrayList<LogMaintenanceTaskConfiguration> list = new ArrayList<LogMaintenanceTaskConfiguration>();
		boolean connectionCreated = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = com.nextlevel.platform.connection.CommandProcessor.getSharedConnection();

			if (con == null) {
				con = new ConnectionManager().getConnection(datasourceAlias);
				connectionCreated = true;
			}
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME_HISTORY, "LIST");

			pstmt.setLong(1, id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				LogMaintenanceTaskConfiguration logMaintenanceTaskConfiguration = buildLogMaintenanceTaskConfiguration(rs, ColumnAliasType.COLUMNNAME);
				list.add(logMaintenanceTaskConfiguration);
			}
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return list;
	}

	public Long getId(final ResultSet rs) throws SQLException, IOException {
		return getId(rs, getColumnAliasType());
	}

	private Long getId(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return UtilDAO.getLong(rs, getColumnAlias(column_id, columnAliasType));

	}

	public String getClassName(final ResultSet rs) throws SQLException, IOException {
		return getClassName(rs, getColumnAliasType());
	}

	private String getClassName(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getString(getColumnAlias(column_className, columnAliasType));

	}

	public String getExecutionState(final ResultSet rs) throws SQLException, IOException {
		return getExecutionState(rs, getColumnAliasType());
	}

	private String getExecutionState(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getString(getColumnAlias(column_executionState, columnAliasType));

	}

	public Timestamp getLastExecution(final ResultSet rs) throws SQLException, IOException {
		return getLastExecution(rs, getColumnAliasType());
	}

	private Timestamp getLastExecution(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getTimestamp(getColumnAlias(column_lastExecution, columnAliasType));

	}

	public String getExecutionType(final ResultSet rs) throws SQLException, IOException {
		return getExecutionType(rs, getColumnAliasType());
	}

	private String getExecutionType(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getString(getColumnAlias(column_executionType, columnAliasType));

	}

	public Integer getPriority(final ResultSet rs) throws SQLException, IOException {
		return getPriority(rs, getColumnAliasType());
	}

	private Integer getPriority(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return UtilDAO.getInteger(rs, getColumnAlias(column_priority, columnAliasType));

	}

	public String getDescription(final ResultSet rs) throws SQLException, IOException {
		return getDescription(rs, getColumnAliasType());
	}

	private String getDescription(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getString(getColumnAlias(column_description, columnAliasType));

	}

	public MaintenanceTaskConfigurationPVO buildMaintenanceTaskConfigurationPVO(final ResultSet rs) throws SQLException, IOException {
		return buildMaintenanceTaskConfigurationPVO(rs, getColumnAliasType());
	}

	protected MaintenanceTaskConfigurationPVO buildMaintenanceTaskConfigurationPVO(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {
		MaintenanceTaskConfigurationPVO maintenanceTaskConfigurationPVO = new MaintenanceTaskConfigurationPVO();

		maintenanceTaskConfigurationPVO.setId(getId(rs, columnAliasType));
		maintenanceTaskConfigurationPVO.setClassName(getClassName(rs, columnAliasType));
		maintenanceTaskConfigurationPVO.setExecutionState(getExecutionState(rs, columnAliasType));
		maintenanceTaskConfigurationPVO.setLastExecution(getLastExecution(rs, columnAliasType));
		maintenanceTaskConfigurationPVO.setExecutionType(getExecutionType(rs, columnAliasType));
		maintenanceTaskConfigurationPVO.setPriority(getPriority(rs, columnAliasType));
		maintenanceTaskConfigurationPVO.setDescription(getDescription(rs, columnAliasType));

		return maintenanceTaskConfigurationPVO;
	}

	public LogMaintenanceTaskConfiguration buildLogMaintenanceTaskConfiguration(final ResultSet rs) throws SQLException, IOException {
		return buildLogMaintenanceTaskConfiguration(rs, getColumnAliasType());
	}

	public LogMaintenanceTaskConfiguration buildLogMaintenanceTaskConfiguration(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {
		LogMaintenanceTaskConfiguration result = new LogMaintenanceTaskConfiguration();

		result.setId(getId(rs, columnAliasType));
		result.setClassName(getClassName(rs, columnAliasType));
		result.setExecutionState(getExecutionState(rs, columnAliasType));
		result.setLastExecution(getLastExecution(rs, columnAliasType));
		result.setExecutionType(getExecutionType(rs, columnAliasType));
		result.setPriority(getPriority(rs, columnAliasType));
		result.setDescription(getDescription(rs, columnAliasType));

		result.setIdLog(UtilDAO.getLong(rs, "ID_LOG_"));
		result.setCreatedLog(UtilDAO.getDate(rs, "CREATED_LOG_"));
		result.setUserLog(rs.getString("USER_LOG_"));
		result.setTypeOfOperation(TypeOfOperation.get(UtilDAO.getInteger(rs, "TYPEOFOPERATION_LOG_")));

		return result;
	}

}
