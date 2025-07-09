/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo;

import java.io.Serializable;

public class MaintenanceTaskLogEntryPVO implements Serializable {

	//primary Key
	private Long id;

	public static final int _maxLengthId = 255;
	public static final int _minLengthId = 0;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	private Long maintenanceTaskConfiguration;

	public static final int _maxLengthMaintenanceTaskConfiguration = 255;
	public static final int _minLengthMaintenanceTaskConfiguration = 0;

	public void setMaintenanceTaskConfiguration(Long maintenanceTaskConfiguration) {
		this.maintenanceTaskConfiguration = maintenanceTaskConfiguration;
	}

	public Long getMaintenanceTaskConfiguration() {
		return maintenanceTaskConfiguration;
	}

	private Long count;

	public static final int _maxLengthCount = 255;
	public static final int _minLengthCount = 0;

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getCount() {
		return count;
	}

	private java.sql.Timestamp retentionPeriodEnd;

	public static final int _maxLengthRetentionPeriodEnd = 255;
	public static final int _minLengthRetentionPeriodEnd = 0;

	public void setRetentionPeriodEnd(java.sql.Timestamp retentionPeriodEnd) {
		this.retentionPeriodEnd = retentionPeriodEnd;
	}

	public java.sql.Timestamp getRetentionPeriodEnd() {
		return retentionPeriodEnd;
	}

	private String errorLog;

	public static final int _maxLengthErrorLog = 255;
	public static final int _minLengthErrorLog = 0;

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public String getErrorLog() {
		return errorLog;
	}

	private String log;

	public static final int _maxLengthLog = 255;
	public static final int _minLengthLog = 0;

	public void setLog(String log) {
		this.log = log;
	}

	public String getLog() {
		return log;
	}

	private java.sql.Timestamp periodStart;

	public static final int _maxLengthPeriodStart = 255;
	public static final int _minLengthPeriodStart = 0;

	public void setPeriodStart(java.sql.Timestamp periodStart) {
		this.periodStart = periodStart;
	}

	public java.sql.Timestamp getPeriodStart() {
		return periodStart;
	}

	private java.sql.Timestamp periodEnd;

	public static final int _maxLengthPeriodEnd = 255;
	public static final int _minLengthPeriodEnd = 0;

	public void setPeriodEnd(java.sql.Timestamp periodEnd) {
		this.periodEnd = periodEnd;
	}

	public java.sql.Timestamp getPeriodEnd() {
		return periodEnd;
	}

}
