/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo;

import java.io.Serializable;

public class MaintenanceTaskConfigurationPVO implements Serializable {

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

	private String className;

	public static final int _maxLengthClassName = 255;
	public static final int _minLengthClassName = 0;

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	private String executionState;

	public static final int _maxLengthExecutionState = 255;
	public static final int _minLengthExecutionState = 0;

	public void setExecutionState(String executionState) {
		this.executionState = executionState;
	}

	public String getExecutionState() {
		return executionState;
	}

	private java.sql.Timestamp lastExecution;

	public static final int _maxLengthLastExecution = 255;
	public static final int _minLengthLastExecution = 0;

	public void setLastExecution(java.sql.Timestamp lastExecution) {
		this.lastExecution = lastExecution;
	}

	public java.sql.Timestamp getLastExecution() {
		return lastExecution;
	}

	private String executionType;

	public static final int _maxLengthExecutionType = 255;
	public static final int _minLengthExecutionType = 0;

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getExecutionType() {
		return executionType;
	}

	private Integer priority;

	public static final int _maxLengthPriority = 255;
	public static final int _minLengthPriority = 0;

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getPriority() {
		return priority;
	}

	private String description;

	public static final int _maxLengthDescription = 255;
	public static final int _minLengthDescription = 0;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
