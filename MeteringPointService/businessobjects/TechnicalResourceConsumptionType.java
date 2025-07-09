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
public class TechnicalResourceConsumptionType implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public TechnicalResourceConsumptionType() {
	}

	public TechnicalResourceConsumptionType(Long id, String qualifier, Long technicalResourceId) {

		this.id = id;

		this.qualifier = qualifier;

		this.technicalResourceId = technicalResourceId;

	}

	//primary Key

	protected Long id;

	protected String qualifier;

	protected Long technicalResourceId;

	public void setId(Long id) {
		this.id = id;
	}

	public TechnicalResourceConsumptionType withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public TechnicalResourceConsumptionType withQualifier(String qualifier) {
		this.qualifier = qualifier;
		return this;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setTechnicalResourceId(Long technicalResourceId) {
		this.technicalResourceId = technicalResourceId;
	}

	public TechnicalResourceConsumptionType withTechnicalResourceId(Long technicalResourceId) {
		this.technicalResourceId = technicalResourceId;
		return this;
	}

	public Long getTechnicalResourceId() {
		return technicalResourceId;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			qualifier = (String) in.readObject();

			technicalResourceId = ExternalizableUtil.parseLong(in.readObject());

			// foreign Keys

		} catch (java.io.OptionalDataException e) {
			//ignore Exception
		} catch (java.io.EOFException e) {
			//ignore Exception
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(ExternalizableUtil.checkNumericNullValue(id));

		out.writeObject(qualifier);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(technicalResourceId));

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "qualifier: " + qualifier + "\n";
		res += "technicalResourceId: " + technicalResourceId + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TechnicalResourceConsumptionType) || id == null)
			return false;

		TechnicalResourceConsumptionType technicalResourceConsumptionType = (TechnicalResourceConsumptionType) obj;

		if (this.id.equals(technicalResourceConsumptionType.id))
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

		if (qualifier != null) {
			res.append(qualifier + ",");
		} else
			res.append(",");

		if (technicalResourceId != null) {
			res.append(technicalResourceId + ",");
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

		//parsing qualifier
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.qualifier = actualData; //String

		//parsing technicalResourceId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.technicalResourceId = Long.valueOf(actualData);

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

	public TechnicalResourceConsumptionType clone() {
		TechnicalResourceConsumptionType technicalResourceConsumptionType = new TechnicalResourceConsumptionType();

		technicalResourceConsumptionType.setQualifier(this.getQualifier());
		technicalResourceConsumptionType.setTechnicalResourceId(this.getTechnicalResourceId());

		return technicalResourceConsumptionType;
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
