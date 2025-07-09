/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.bo;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import com.nextlevel.fastlane.core.utils.ExternalizableUtil;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;

@XmlRootElement(namespace = "com.nextlevel.fastlane.myBusinessSupplier.bo")
@XmlAccessorType(XmlAccessType.FIELD)
public class MaintenanceTaskConfiguration implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public MaintenanceTaskConfiguration() {
	}

	public MaintenanceTaskConfiguration(Long id, String className, String executionState, java.util.Date lastExecution, String executionType, Integer priority, String description) {

		this.id = id;

		this.className = className;

		this.executionState = executionState;

		this.lastExecution = lastExecution;

		this.executionType = executionType;

		this.priority = priority;

		this.description = description;

	}

	//primary Key

	protected Long id;

	protected String className;

	protected String executionState;

	protected java.util.Date lastExecution;

	protected String executionType;

	protected Integer priority;

	protected String description;

	public void setId(Long id) {
		this.id = id;
	}

	public MaintenanceTaskConfiguration withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public MaintenanceTaskConfiguration withClassName(String className) {
		this.className = className;
		return this;
	}

	public String getClassName() {
		return className;
	}

	public void setExecutionState(String executionState) {
		this.executionState = executionState;
	}

	public MaintenanceTaskConfiguration withExecutionState(String executionState) {
		this.executionState = executionState;
		return this;
	}

	public String getExecutionState() {
		return executionState;
	}

	public void setLastExecution(java.util.Date lastExecution) {
		this.lastExecution = lastExecution;
	}

	public MaintenanceTaskConfiguration withLastExecution(java.util.Date lastExecution) {
		this.lastExecution = lastExecution;
		return this;
	}

	public java.util.Date getLastExecution() {
		return lastExecution;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public MaintenanceTaskConfiguration withExecutionType(String executionType) {
		this.executionType = executionType;
		return this;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public MaintenanceTaskConfiguration withPriority(Integer priority) {
		this.priority = priority;
		return this;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MaintenanceTaskConfiguration withDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			className = (String) in.readObject();

			executionState = (String) in.readObject();

			lastExecution = (java.util.Date) in.readObject();

			executionType = (String) in.readObject();

			priority = ExternalizableUtil.parseInteger(in.readObject());

			description = (String) in.readObject();

			// foreign Keys

		} catch (java.io.OptionalDataException e) {
			//ignore Exception
		} catch (java.io.EOFException e) {
			//ignore Exception
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(ExternalizableUtil.checkNumericNullValue(id));

		out.writeObject(className);

		out.writeObject(executionState);

		out.writeObject(lastExecution);

		out.writeObject(executionType);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(priority));

		out.writeObject(description);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "className: " + className + "\n";
		res += "executionState: " + executionState + "\n";
		res += "lastExecution: " + lastExecution + "\n";
		res += "executionType: " + executionType + "\n";
		res += "priority: " + priority + "\n";
		res += "description: " + description + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MaintenanceTaskConfiguration) || id == null)
			return false;

		MaintenanceTaskConfiguration maintenanceTaskConfiguration = (MaintenanceTaskConfiguration) obj;

		if (this.id.equals(maintenanceTaskConfiguration.id))
			return true;
		else
			return false;

	}

	public String serializeToString() {
		StringBuffer res = new StringBuffer();
		res.append("[");

		if (id != null) {
			res.append(id + ",");
		} else
			res.append(",");

		if (className != null) {
			res.append(className + ",");
		} else
			res.append(",");

		if (executionState != null) {
			res.append(executionState + ",");
		} else
			res.append(",");

		if (lastExecution != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(lastExecution) + ",");
		} else
			res.append(",");

		if (executionType != null) {
			res.append(executionType + ",");
		} else
			res.append(",");

		if (priority != null) {
			res.append(priority + ",");
		} else
			res.append(",");

		if (description != null) {
			res.append(description + ",");
		} else
			res.append(",");

		res.setCharAt(res.length() - 1, ']');
		return res.toString();
	}

	public void deserializeFromString(String data) throws NumberFormatException, ParseException {
		if (data.length() == 0)
			return;
		String actualData = null;
		int pos = 1;//starts with [
		int end = 0;

		//parsing id
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.id = Long.valueOf(actualData);

		//parsing className
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.className = actualData; //String

		//parsing executionState
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.executionState = actualData; //String

		//parsing lastExecution
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.lastExecution = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing executionType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.executionType = actualData; //String

		//parsing priority
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.priority = Integer.valueOf(actualData);

		//parsing description
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.description = actualData; //String

	}

	private int findEndOfSegment(int startPos, String data) {
		if (data.charAt(startPos) != '[') {
			int end = data.indexOf(",", startPos);
			if (end == -1)
				end = data.length() - 1;
			return end;
		}
		//find corresponding ]
		startPos++;
		int numBrace = 1;
		while (numBrace > 0) {
			if (data.charAt(startPos) == '[')
				numBrace++;
			else if (data.charAt(startPos) == ']')
				numBrace--;
			startPos++;
		}
		return startPos;
	}

	public MaintenanceTaskConfiguration clone() {
		MaintenanceTaskConfiguration maintenanceTaskConfiguration = new MaintenanceTaskConfiguration();

		maintenanceTaskConfiguration.setClassName(this.getClassName());
		maintenanceTaskConfiguration.setExecutionState(this.getExecutionState());
		maintenanceTaskConfiguration.setLastExecution(this.getLastExecution());
		maintenanceTaskConfiguration.setExecutionType(this.getExecutionType());
		maintenanceTaskConfiguration.setPriority(this.getPriority());
		maintenanceTaskConfiguration.setDescription(this.getDescription());

		return maintenanceTaskConfiguration;
	}

	@Override
	public int hashCode() {
		if (id == null) {
			return -1;
		}
		return id.intValue();
	}

	public void save() {
		PersistenceHandler.getInstance().save(this);
	}

	public void update() {
		PersistenceHandler.getInstance().update(this);
	}

}
