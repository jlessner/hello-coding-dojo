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

import com.nextlevel.fastlane.financial.bo.*;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;

@XmlRootElement(namespace = "com.nextlevel.fastlane.myBusinessSupplier.bo")
@XmlAccessorType(XmlAccessType.FIELD)
public class MaintenanceTaskLogEntry extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public MaintenanceTaskLogEntry() {
	}

	public MaintenanceTaskLogEntry(Long id, Long maintenanceTaskConfiguration, Long count, java.util.Date retentionPeriodEnd, String errorLog, String log) {

		this.id = id;

		this.maintenanceTaskConfiguration = maintenanceTaskConfiguration;

		this.count = count;

		this.retentionPeriodEnd = retentionPeriodEnd;

		this.errorLog = errorLog;

		this.log = log;

	}

	//primary Key

	protected Long id;

	protected Long maintenanceTaskConfiguration;

	protected Long count;

	protected java.util.Date retentionPeriodEnd;

	protected String errorLog;

	protected String log;

	public void setId(Long id) {
		this.id = id;
	}

	public MaintenanceTaskLogEntry withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setMaintenanceTaskConfiguration(Long maintenanceTaskConfiguration) {
		this.maintenanceTaskConfiguration = maintenanceTaskConfiguration;
	}

	public MaintenanceTaskLogEntry withMaintenanceTaskConfiguration(Long maintenanceTaskConfiguration) {
		this.maintenanceTaskConfiguration = maintenanceTaskConfiguration;
		return this;
	}

	public Long getMaintenanceTaskConfiguration() {
		return maintenanceTaskConfiguration;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public MaintenanceTaskLogEntry withCount(Long count) {
		this.count = count;
		return this;
	}

	public Long getCount() {
		return count;
	}

	public void setRetentionPeriodEnd(java.util.Date retentionPeriodEnd) {
		this.retentionPeriodEnd = retentionPeriodEnd;
	}

	public MaintenanceTaskLogEntry withRetentionPeriodEnd(java.util.Date retentionPeriodEnd) {
		this.retentionPeriodEnd = retentionPeriodEnd;
		return this;
	}

	public java.util.Date getRetentionPeriodEnd() {
		return retentionPeriodEnd;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public MaintenanceTaskLogEntry withErrorLog(String errorLog) {
		this.errorLog = errorLog;
		return this;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public MaintenanceTaskLogEntry withLog(String log) {
		this.log = log;
		return this;
	}

	public String getLog() {
		return log;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			maintenanceTaskConfiguration = ExternalizableUtil.parseLong(in.readObject());

			count = ExternalizableUtil.parseLong(in.readObject());

			retentionPeriodEnd = (java.util.Date) in.readObject();

			errorLog = (String) in.readObject();

			log = (String) in.readObject();

			super.readExternal(in);

			// foreign Keys

		} catch (java.io.OptionalDataException e) {
			//ignore Exception
		} catch (java.io.EOFException e) {
			//ignore Exception
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(ExternalizableUtil.checkNumericNullValue(id));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(maintenanceTaskConfiguration));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(count));

		out.writeObject(retentionPeriodEnd);

		out.writeObject(errorLog);

		out.writeObject(log);

		super.writeExternal(out);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "maintenanceTaskConfiguration: " + maintenanceTaskConfiguration + "\n";
		res += "count: " + count + "\n";
		res += "retentionPeriodEnd: " + retentionPeriodEnd + "\n";
		res += "errorLog: " + errorLog + "\n";
		res += "log: " + log + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MaintenanceTaskLogEntry) || id == null)
			return false;

		MaintenanceTaskLogEntry maintenanceTaskLogEntry = (MaintenanceTaskLogEntry) obj;

		if (this.id.equals(maintenanceTaskLogEntry.id))
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

		if (maintenanceTaskConfiguration != null) {
			res.append(maintenanceTaskConfiguration + ",");
		} else
			res.append(",");

		if (count != null) {
			res.append(count + ",");
		} else
			res.append(",");

		if (retentionPeriodEnd != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(retentionPeriodEnd) + ",");
		} else
			res.append(",");

		if (errorLog != null) {
			res.append(errorLog + ",");
		} else
			res.append(",");

		if (log != null) {
			res.append(log + ",");
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

		//parsing maintenanceTaskConfiguration
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.maintenanceTaskConfiguration = Long.valueOf(actualData);

		//parsing count
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.count = Long.valueOf(actualData);

		//parsing retentionPeriodEnd
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.retentionPeriodEnd = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing errorLog
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.errorLog = actualData; //String

		//parsing log
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.log = actualData; //String

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

	public MaintenanceTaskLogEntry clone() {
		MaintenanceTaskLogEntry maintenanceTaskLogEntry = new MaintenanceTaskLogEntry();

		maintenanceTaskLogEntry.setMaintenanceTaskConfiguration(this.getMaintenanceTaskConfiguration());
		maintenanceTaskLogEntry.setCount(this.getCount());
		maintenanceTaskLogEntry.setRetentionPeriodEnd(this.getRetentionPeriodEnd());
		maintenanceTaskLogEntry.setErrorLog(this.getErrorLog());
		maintenanceTaskLogEntry.setLog(this.getLog());

		maintenanceTaskLogEntry.setPeriodStart(this.getPeriodStart());
		maintenanceTaskLogEntry.setPeriodEnd(this.getPeriodEnd());

		return maintenanceTaskLogEntry;
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
