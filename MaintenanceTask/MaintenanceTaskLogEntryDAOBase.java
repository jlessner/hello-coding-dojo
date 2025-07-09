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

import com.nextlevel.fastlane.myBusinessSupplier.bo.log.LogMaintenanceTaskLogEntry;

import org.apache.log4j.Logger;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.MaintenanceTaskLogEntryPVO;

public class MaintenanceTaskLogEntryDAOBase implements DAOBase<MaintenanceTaskLogEntryPVO> {

	// TODO: Replace by com.nextlevel.platform.connection.enums.ColumnAliasType from connection >= 2.39
	private enum ColumnAliasType {
		COLUMNNAME, TABLE_AND_COLUMNNAME, HASH;
	}

	private static final String LOGLOC = "com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.MaintenanceTaskLogEntryDAOBase: ";
	protected Logger logger = Logger.getLogger(this.getClass());
	protected boolean doAuditLog = true;

	protected final String datasourceAlias = "fastlane";

	public static final String TABLE_NAME = "FL_MAINTENANCETASKLOGENTRY";

	public static final String TABLE_NAME_HISTORY = "LOG_MAINTENANCETASKLOGENTRY";

	private static final String DEFAULT_TABLE_NAME = "";

	private String tableName = DEFAULT_TABLE_NAME;

	// If true: generate short alias names to work with Oracle DB
	private boolean useShortAlias = false;

	static {
		StatementHandler.addSqlStatement(TABLE_NAME, "INSERT", "INSERT INTO " + TABLE_NAME + "(PERIODSTART_, PERIODEND_, MAINTENANCETASKCONFIGURATION_, COUNT_, RETENTIONPERIODEND_, ERRORLOG_, LOG_) values (?,?,?,?,?,?,?)");

		StatementHandler.addSqlStatement(TABLE_NAME_HISTORY, "INSERT", "INSERT INTO " + TABLE_NAME_HISTORY
				+ "(PERIODSTART_, PERIODEND_, MAINTENANCETASKCONFIGURATION_, COUNT_, RETENTIONPERIODEND_, ERRORLOG_, LOG_, id_, CREATED_LOG_, USER_LOG_, TYPEOFOPERATION_LOG_) values (?,?,?,?,?,?,?,?,?,?,?)");

		StatementHandler.addSqlStatement(TABLE_NAME_HISTORY, "LIST", "SELECT * FROM " + TABLE_NAME_HISTORY + " WHERE ID_= ?");

		StatementHandler.addSqlStatement(TABLE_NAME, "UPDATE", "UPDATE " + TABLE_NAME + " SET PERIODSTART_ = ?, PERIODEND_ = ?, MAINTENANCETASKCONFIGURATION_ = ?, COUNT_ = ?, RETENTIONPERIODEND_ = ?, ERRORLOG_ = ?, LOG_ = ? WHERE ID_= ? ");

		StatementHandler.addSqlStatement(TABLE_NAME, "DELETE", "DELETE FROM FL_MAINTENANCETASKLOGENTRY WHERE ID_ = ?");
		StatementHandler.addSqlStatement(TABLE_NAME_HISTORY, "DELETE", "DELETE FROM LOG_MAINTENANCETASKLOGENTRY WHERE ID_ = ?");
		StatementHandler.addSqlStatement(TABLE_NAME, "LOAD", "SELECT * FROM FL_MAINTENANCETASKLOGENTRY WHERE ID_ = ?");
		StatementHandler.addSqlStatement(TABLE_NAME, "LIST", "SELECT * FROM FL_MAINTENANCETASKLOGENTRY");
	}

	private static final String AS_QUOTE = " AS \"";

	public static final String column_periodStart = "periodStart_";
	public static final String column_periodEnd = "periodEnd_";
	public static final String column_id = "id_";
	public static final String column_maintenanceTaskConfiguration = "maintenanceTaskConfiguration_";
	public static final String column_count = "count_";
	public static final String column_retentionPeriodEnd = "retentionPeriodEnd_";
	public static final String column_errorLog = "errorLog_";
	public static final String column_log = "log_";

	public String getAllColumns() {
		final StringBuilder result = new StringBuilder();

		result.append(',');
		result.append(this.tableName);
		result.append(column_periodStart);
		result.append(',');
		result.append(this.tableName);
		result.append(column_periodEnd);
		result.append(',');
		result.append(this.tableName);
		result.append(column_id);
		result.append(',');
		result.append(this.tableName);
		result.append(column_maintenanceTaskConfiguration);
		result.append(',');
		result.append(this.tableName);
		result.append(column_count);
		result.append(',');
		result.append(this.tableName);
		result.append(column_retentionPeriodEnd);
		result.append(',');
		result.append(this.tableName);
		result.append(column_errorLog);
		result.append(',');
		result.append(this.tableName);
		result.append(column_log);

		return result.toString();
	}

	public String getAllColumnsWithAlias() {
		final StringBuilder result = new StringBuilder();

		result.append(',');
		result.append(this.tableName);
		result.append(column_periodStart);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_periodStart));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_periodEnd);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_periodEnd));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_id);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_id));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_maintenanceTaskConfiguration);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_maintenanceTaskConfiguration));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_count);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_count));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_retentionPeriodEnd);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_retentionPeriodEnd));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_errorLog);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_errorLog));
		result.append("\"");
		result.append(',');
		result.append(this.tableName);
		result.append(column_log);
		result.append(AS_QUOTE);
		result.append(getColumnAlias(column_log));
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
	public void save(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO) throws ClassNotFoundException, SQLException {
		insert(maintenanceTaskLogEntryPVO, null);
	}

	@Override
	public void save(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, Connection con) throws ClassNotFoundException, SQLException {
		insert(maintenanceTaskLogEntryPVO, con);
	}

	protected void onInsert(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, Connection con) throws SQLException {
	}

	protected void setValues(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, PreparedStatement pstmt) throws SQLException {

		if (maintenanceTaskLogEntryPVO.getPeriodStart() == null)
			pstmt.setObject(1, null);
		else
			pstmt.setTimestamp(1, maintenanceTaskLogEntryPVO.getPeriodStart());

		if (maintenanceTaskLogEntryPVO.getPeriodEnd() == null)
			pstmt.setObject(2, null);
		else
			pstmt.setTimestamp(2, maintenanceTaskLogEntryPVO.getPeriodEnd());

		if (maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration() == null)
			pstmt.setObject(3, null);
		else
			pstmt.setLong(3, maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration());

		if (maintenanceTaskLogEntryPVO.getCount() == null)
			pstmt.setObject(4, null);
		else
			pstmt.setLong(4, maintenanceTaskLogEntryPVO.getCount());

		if (maintenanceTaskLogEntryPVO.getRetentionPeriodEnd() == null)
			pstmt.setObject(5, null);
		else
			pstmt.setTimestamp(5, maintenanceTaskLogEntryPVO.getRetentionPeriodEnd());

		if (maintenanceTaskLogEntryPVO.getErrorLog() == null)
			pstmt.setObject(6, null);
		else
			pstmt.setString(6, maintenanceTaskLogEntryPVO.getErrorLog());

		if (maintenanceTaskLogEntryPVO.getLog() == null)
			pstmt.setObject(7, null);
		else
			pstmt.setString(7, maintenanceTaskLogEntryPVO.getLog());

	}

	private void insert(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, Connection con) throws SQLException, ClassNotFoundException {

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

			onInsert(maintenanceTaskLogEntryPVO, con);
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME, "INSERT", "ID_");

			setValues(maintenanceTaskLogEntryPVO, pstmt);

			pstmt.execute();

			long id = handler.getGeneratedIdFromStatementAsLong();
			maintenanceTaskLogEntryPVO.setId(id);

			if (doAuditLog) {
				insertIntoHistory(maintenanceTaskLogEntryPVO, TypeOfOperation.INSERT, con);
			}

		} catch (SQLException sql) {
			logger.error("Error inserting MaintenanceTaskLogEntryPVO", sql);
			logger.info(LOGLOC + maintenanceTaskLogEntryPVO.toString());
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(null, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
	}

	public void insertIntoHistory(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, TypeOfOperation type, Connection con) throws SQLException, ClassNotFoundException {

		if (maintenanceTaskLogEntryPVO.getId() == null)
			throw new IllegalArgumentException("cannot save an entry into History without primary Key value");

		PreparedStatement pstmt = null;

		try {
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME_HISTORY, "INSERT");

			setValues(maintenanceTaskLogEntryPVO, pstmt);

			pstmt.setLong(8, maintenanceTaskLogEntryPVO.getId());

			Date logCreationDate = CommandProcessor.getLogCreationDate();
			if (logCreationDate == null) {
				logCreationDate = DateTool.getCurrent();
			}
			pstmt.setTimestamp(9, new Timestamp(logCreationDate.getTime()));

			String activeUser = CommandProcessor.getLogUser();
			if (activeUser == null) {
				try {
					activeUser = new FastlaneUserManagement().getActiveUser();
				} catch (UserManagementException e) {
					activeUser = "system";
				}
			}
			pstmt.setString(10, activeUser);
			pstmt.setInt(11, type.getId());

			pstmt.execute();

		} catch (SQLException sql) {
			logger.error("Error inserting histroy for MaintenanceTaskLogEntryPVO", sql);
			logger.info(LOGLOC + maintenanceTaskLogEntryPVO.toString());
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(null, pstmt, logger);
		}
	}

	protected void onUpdate(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, Connection con) throws SQLException {
	}

	@Override
	public final boolean update(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO) throws SQLException, ClassNotFoundException {
		return update(maintenanceTaskLogEntryPVO, null);
	}

	public final boolean update(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, Connection con) throws SQLException, ClassNotFoundException {
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

			onUpdate(maintenanceTaskLogEntryPVO, con);
			StatementHandler handler = new StatementHandler(con);
			pstmt = handler.getPreparedStatement(TABLE_NAME, "UPDATE");

			setValues(maintenanceTaskLogEntryPVO, pstmt);

			pstmt.setLong(8, maintenanceTaskLogEntryPVO.getId());

			int up = pstmt.executeUpdate();
			if (up != 0) {
				updated = true;
			}

			if (doAuditLog) {
				insertIntoHistory(maintenanceTaskLogEntryPVO, TypeOfOperation.UPDATE, con);
			}
		} catch (SQLException sql) {
			logger.error("Error updating MaintenanceTaskLogEntryPVO", sql);
			logger.info(LOGLOC + maintenanceTaskLogEntryPVO.toString());
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(null, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}

		return updated;
	}

	@Override
	public void delete(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO) throws ClassNotFoundException, SQLException {
		delete(maintenanceTaskLogEntryPVO.getId());
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
			logger.error("Error deleting MaintenanceTaskLogEntryPVO", sql);
			throw sql;
		} catch (IOException e) {
			logger.error("Error deleting MaintenanceTaskLogEntryPVO", e);
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
			logger.error("Error deleting MaintenanceTaskLogEntryPVO", sql);
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

	public MaintenanceTaskLogEntryPVO load(Long id) throws ClassNotFoundException, SQLException, IOException {
		return load(id, null);
	}

	public MaintenanceTaskLogEntryPVO load(Long id, Connection con) throws ClassNotFoundException, SQLException, IOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO = null;
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
				maintenanceTaskLogEntryPVO = buildMaintenanceTaskLogEntryPVO(rs, ColumnAliasType.COLUMNNAME);
				break;
			}
		} catch (SQLException sql) {
			logger.error("Error loading MaintenanceTaskLogEntryPVO", sql);
			throw sql;
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return maintenanceTaskLogEntryPVO;
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, false);
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, Connection con) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, false, con);
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, like, null, null);
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, Connection con) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, like, null, con);
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, like, sqlWherePart, null);
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart, Connection con) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, like, sqlWherePart, con, null);
	}

	public PreparedStatement addValuesFromFilterObjectAndPrepareStatement(String sql, MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart, Connection con, Integer limit) throws SQLException {
		return addValuesFromFilterObjectAndPrepareStatement(sql, maintenanceTaskLogEntryPVO, like, sqlWherePart, con, limit, 0);
	}

	public PreparedStatement addValuesFromFilterObjectAndPrepareStatement(String sql, MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart, Connection con, Integer limit, Integer offset)
			throws SQLException {
		PreparedStatement pstmt = null;

		boolean first = true;

		if (maintenanceTaskLogEntryPVO != null) {

			if (maintenanceTaskLogEntryPVO.getId() != null && maintenanceTaskLogEntryPVO.getId() != -1) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_id + " = ? ";
			}

			if (maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration() != null && maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration() != -1) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_maintenanceTaskConfiguration + " = ? ";
			}

			if (maintenanceTaskLogEntryPVO.getCount() != null && maintenanceTaskLogEntryPVO.getCount() != -1) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_count + " = ? ";
			}

			if (maintenanceTaskLogEntryPVO.getRetentionPeriodEnd() != null) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				sql += column_retentionPeriodEnd + " = ? ";
			}

			if (maintenanceTaskLogEntryPVO.getErrorLog() != null && maintenanceTaskLogEntryPVO.getErrorLog().length() > 0) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				if (like)
					sql += column_errorLog + " LIKE ? ";
				else
					sql += column_errorLog + " = ? ";
			}

			if (maintenanceTaskLogEntryPVO.getLog() != null && maintenanceTaskLogEntryPVO.getLog().length() > 0) {
				if (!first)
					sql += "AND ";
				else {
					sql += "WHERE ";
					first = false;
				}
				if (like)
					sql += column_log + " LIKE ? ";
				else
					sql += column_log + " = ? ";
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
		if (maintenanceTaskLogEntryPVO != null) {
			int pos = 1;

			if (maintenanceTaskLogEntryPVO.getId() != null && maintenanceTaskLogEntryPVO.getId() != -1) {

				pstmt.setLong(pos, maintenanceTaskLogEntryPVO.getId());
				pos++;
			}

			if (maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration() != null && maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration() != -1) {

				pstmt.setLong(pos, maintenanceTaskLogEntryPVO.getMaintenanceTaskConfiguration());
				pos++;
			}

			if (maintenanceTaskLogEntryPVO.getCount() != null && maintenanceTaskLogEntryPVO.getCount() != -1) {

				pstmt.setLong(pos, maintenanceTaskLogEntryPVO.getCount());
				pos++;
			}

			if (maintenanceTaskLogEntryPVO.getRetentionPeriodEnd() != null) {

				pstmt.setTimestamp(pos, maintenanceTaskLogEntryPVO.getRetentionPeriodEnd());
				pos++;
			}

			if (maintenanceTaskLogEntryPVO.getErrorLog() != null && maintenanceTaskLogEntryPVO.getErrorLog().length() > 0) {
				if (like)
					pstmt.setString(pos, "%" + maintenanceTaskLogEntryPVO.getErrorLog() + "%");
				else
					pstmt.setString(pos, maintenanceTaskLogEntryPVO.getErrorLog());
				pos++;
			}

			if (maintenanceTaskLogEntryPVO.getLog() != null && maintenanceTaskLogEntryPVO.getLog().length() > 0) {
				if (like)
					pstmt.setString(pos, "%" + maintenanceTaskLogEntryPVO.getLog() + "%");
				else
					pstmt.setString(pos, maintenanceTaskLogEntryPVO.getLog());
				pos++;
			}

		}
		return pstmt;
	}

	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart, Connection con, Integer limit) throws ClassNotFoundException, SQLException, IOException {
		return list(maintenanceTaskLogEntryPVO, like, sqlWherePart, con, limit, 0);
	}

	public List<MaintenanceTaskLogEntryPVO> list(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart, Connection con, Integer limit, Integer offset) {
		ArrayList<MaintenanceTaskLogEntryPVO> maintenanceTaskLogEntryPVOList = new ArrayList<MaintenanceTaskLogEntryPVO>();

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

				pstmt = addValuesFromFilterObjectAndPrepareStatement(sql, maintenanceTaskLogEntryPVO, like, sqlWherePart, con, limit, offset);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO2 = buildMaintenanceTaskLogEntryPVO(rs, ColumnAliasType.COLUMNNAME);
					maintenanceTaskLogEntryPVOList.add(maintenanceTaskLogEntryPVO2);
				}
			} finally {
				DatabaseTool.closeQuietly(rs, pstmt, logger);
				if (connectionCreated && con != null)
					con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return maintenanceTaskLogEntryPVOList;
	}

	public Long count(MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO, boolean like, String sqlWherePart, Connection con) {
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

				pstmt = addValuesFromFilterObjectAndPrepareStatement(sql, maintenanceTaskLogEntryPVO, like, sqlWherePart, con, null);

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
	public List<MaintenanceTaskLogEntryPVO> list() throws ClassNotFoundException, SQLException, IOException {
		List<MaintenanceTaskLogEntryPVO> ret = null;
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
	public List<MaintenanceTaskLogEntryPVO> list(Connection con) throws ClassNotFoundException, SQLException, IOException {
		List<MaintenanceTaskLogEntryPVO> maintenancetasklogentrypvoList = new ArrayList<MaintenanceTaskLogEntryPVO>();
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
				MaintenanceTaskLogEntryPVO maintenancetasklogentrypvo = buildMaintenanceTaskLogEntryPVO(rs, ColumnAliasType.COLUMNNAME);
				maintenancetasklogentrypvoList.add(maintenancetasklogentrypvo);
			}
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return maintenancetasklogentrypvoList;
	}

	@Override
	public List<MaintenanceTaskLogEntryPVO> select(String sql) throws ClassNotFoundException, SQLException, IOException {
		List<MaintenanceTaskLogEntryPVO> maintenancetasklogentrypvoList = new ArrayList<MaintenanceTaskLogEntryPVO>();
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
				MaintenanceTaskLogEntryPVO maintenancetasklogentrypvo = buildMaintenanceTaskLogEntryPVO(rs);
				maintenancetasklogentrypvoList.add(maintenancetasklogentrypvo);
			}
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return maintenancetasklogentrypvoList;
	}

	public List<LogMaintenanceTaskLogEntry> listLog(Long id) throws ClassNotFoundException, SQLException, IOException {
		ArrayList<LogMaintenanceTaskLogEntry> list = new ArrayList<LogMaintenanceTaskLogEntry>();
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
				LogMaintenanceTaskLogEntry logMaintenanceTaskLogEntry = buildLogMaintenanceTaskLogEntry(rs, ColumnAliasType.COLUMNNAME);
				list.add(logMaintenanceTaskLogEntry);
			}
		} finally {
			DatabaseTool.closeQuietly(rs, pstmt, logger);
			if (connectionCreated && con != null)
				con.close();
		}
		return list;
	}

	public Timestamp getPeriodStart(final ResultSet rs) throws SQLException, IOException {
		return getPeriodStart(rs, getColumnAliasType());
	}

	private Timestamp getPeriodStart(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getTimestamp(getColumnAlias(column_periodStart, columnAliasType));

	}

	public Timestamp getPeriodEnd(final ResultSet rs) throws SQLException, IOException {
		return getPeriodEnd(rs, getColumnAliasType());
	}

	private Timestamp getPeriodEnd(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getTimestamp(getColumnAlias(column_periodEnd, columnAliasType));

	}

	public Long getId(final ResultSet rs) throws SQLException, IOException {
		return getId(rs, getColumnAliasType());
	}

	private Long getId(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return UtilDAO.getLong(rs, getColumnAlias(column_id, columnAliasType));

	}

	public Long getMaintenanceTaskConfiguration(final ResultSet rs) throws SQLException, IOException {
		return getMaintenanceTaskConfiguration(rs, getColumnAliasType());
	}

	private Long getMaintenanceTaskConfiguration(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return UtilDAO.getLong(rs, getColumnAlias(column_maintenanceTaskConfiguration, columnAliasType));

	}

	public Long getCount(final ResultSet rs) throws SQLException, IOException {
		return getCount(rs, getColumnAliasType());
	}

	private Long getCount(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return UtilDAO.getLong(rs, getColumnAlias(column_count, columnAliasType));

	}

	public Timestamp getRetentionPeriodEnd(final ResultSet rs) throws SQLException, IOException {
		return getRetentionPeriodEnd(rs, getColumnAliasType());
	}

	private Timestamp getRetentionPeriodEnd(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getTimestamp(getColumnAlias(column_retentionPeriodEnd, columnAliasType));

	}

	public String getErrorLog(final ResultSet rs) throws SQLException, IOException {
		return getErrorLog(rs, getColumnAliasType());
	}

	private String getErrorLog(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getString(getColumnAlias(column_errorLog, columnAliasType));

	}

	public String getLog(final ResultSet rs) throws SQLException, IOException {
		return getLog(rs, getColumnAliasType());
	}

	private String getLog(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {

		return rs.getString(getColumnAlias(column_log, columnAliasType));

	}

	public MaintenanceTaskLogEntryPVO buildMaintenanceTaskLogEntryPVO(final ResultSet rs) throws SQLException, IOException {
		return buildMaintenanceTaskLogEntryPVO(rs, getColumnAliasType());
	}

	protected MaintenanceTaskLogEntryPVO buildMaintenanceTaskLogEntryPVO(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {
		MaintenanceTaskLogEntryPVO maintenanceTaskLogEntryPVO = new MaintenanceTaskLogEntryPVO();

		maintenanceTaskLogEntryPVO.setPeriodStart(getPeriodStart(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setPeriodEnd(getPeriodEnd(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setId(getId(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setMaintenanceTaskConfiguration(getMaintenanceTaskConfiguration(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setCount(getCount(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setRetentionPeriodEnd(getRetentionPeriodEnd(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setErrorLog(getErrorLog(rs, columnAliasType));
		maintenanceTaskLogEntryPVO.setLog(getLog(rs, columnAliasType));

		return maintenanceTaskLogEntryPVO;
	}

	public LogMaintenanceTaskLogEntry buildLogMaintenanceTaskLogEntry(final ResultSet rs) throws SQLException, IOException {
		return buildLogMaintenanceTaskLogEntry(rs, getColumnAliasType());
	}

	public LogMaintenanceTaskLogEntry buildLogMaintenanceTaskLogEntry(final ResultSet rs, final ColumnAliasType columnAliasType) throws SQLException, IOException {
		LogMaintenanceTaskLogEntry result = new LogMaintenanceTaskLogEntry();

		result.setPeriodStart(getPeriodStart(rs, columnAliasType));
		result.setPeriodEnd(getPeriodEnd(rs, columnAliasType));
		result.setId(getId(rs, columnAliasType));
		result.setMaintenanceTaskConfiguration(getMaintenanceTaskConfiguration(rs, columnAliasType));
		result.setCount(getCount(rs, columnAliasType));
		result.setRetentionPeriodEnd(getRetentionPeriodEnd(rs, columnAliasType));
		result.setErrorLog(getErrorLog(rs, columnAliasType));
		result.setLog(getLog(rs, columnAliasType));

		result.setIdLog(UtilDAO.getLong(rs, "ID_LOG_"));
		result.setCreatedLog(UtilDAO.getDate(rs, "CREATED_LOG_"));
		result.setUserLog(rs.getString("USER_LOG_"));
		result.setTypeOfOperation(TypeOfOperation.get(UtilDAO.getInteger(rs, "TYPEOFOPERATION_LOG_")));

		return result;
	}

}
