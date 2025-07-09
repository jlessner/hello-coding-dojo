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
public class StorageDataOfTechnicalResource extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public StorageDataOfTechnicalResource() {
	}

	public StorageDataOfTechnicalResource(Long id, Long technicalResourceId, java.math.BigDecimal ratedCapacityIn, java.math.BigDecimal ratedCapacityOut, java.math.BigDecimal storageCapacity, String storageType) {

		this.id = id;

		this.technicalResourceId = technicalResourceId;

		this.ratedCapacityIn = ratedCapacityIn;

		this.ratedCapacityOut = ratedCapacityOut;

		this.storageCapacity = storageCapacity;

		this.storageType = storageType;

	}

	//primary Key

	protected Long id;

	protected Long technicalResourceId;

	protected java.math.BigDecimal ratedCapacityIn;

	protected java.math.BigDecimal ratedCapacityOut;

	protected java.math.BigDecimal storageCapacity;

	protected String storageType;

	public void setId(Long id) {
		this.id = id;
	}

	public StorageDataOfTechnicalResource withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setTechnicalResourceId(Long technicalResourceId) {
		this.technicalResourceId = technicalResourceId;
	}

	public StorageDataOfTechnicalResource withTechnicalResourceId(Long technicalResourceId) {
		this.technicalResourceId = technicalResourceId;
		return this;
	}

	public Long getTechnicalResourceId() {
		return technicalResourceId;
	}

	public void setRatedCapacityIn(java.math.BigDecimal ratedCapacityIn) {
		this.ratedCapacityIn = ratedCapacityIn;
	}

	public StorageDataOfTechnicalResource withRatedCapacityIn(java.math.BigDecimal ratedCapacityIn) {
		this.ratedCapacityIn = ratedCapacityIn;
		return this;
	}

	public java.math.BigDecimal getRatedCapacityIn() {
		return ratedCapacityIn;
	}

	public void setRatedCapacityOut(java.math.BigDecimal ratedCapacityOut) {
		this.ratedCapacityOut = ratedCapacityOut;
	}

	public StorageDataOfTechnicalResource withRatedCapacityOut(java.math.BigDecimal ratedCapacityOut) {
		this.ratedCapacityOut = ratedCapacityOut;
		return this;
	}

	public java.math.BigDecimal getRatedCapacityOut() {
		return ratedCapacityOut;
	}

	public void setStorageCapacity(java.math.BigDecimal storageCapacity) {
		this.storageCapacity = storageCapacity;
	}

	public StorageDataOfTechnicalResource withStorageCapacity(java.math.BigDecimal storageCapacity) {
		this.storageCapacity = storageCapacity;
		return this;
	}

	public java.math.BigDecimal getStorageCapacity() {
		return storageCapacity;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public StorageDataOfTechnicalResource withStorageType(String storageType) {
		this.storageType = storageType;
		return this;
	}

	public String getStorageType() {
		return storageType;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			technicalResourceId = ExternalizableUtil.parseLong(in.readObject());

			final Object ratedCapacityInTemp = in.readObject();
			ratedCapacityIn = (ratedCapacityInTemp != null && !"".equals(ratedCapacityInTemp)) ? new java.math.BigDecimal(ratedCapacityInTemp.toString()) : null;

			final Object ratedCapacityOutTemp = in.readObject();
			ratedCapacityOut = (ratedCapacityOutTemp != null && !"".equals(ratedCapacityOutTemp)) ? new java.math.BigDecimal(ratedCapacityOutTemp.toString()) : null;

			final Object storageCapacityTemp = in.readObject();
			storageCapacity = (storageCapacityTemp != null && !"".equals(storageCapacityTemp)) ? new java.math.BigDecimal(storageCapacityTemp.toString()) : null;

			storageType = (String) in.readObject();

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

		out.writeObject(ExternalizableUtil.checkNumericNullValue(technicalResourceId));

		out.writeObject(ratedCapacityIn != null ? ratedCapacityIn.toPlainString() : null);

		out.writeObject(ratedCapacityOut != null ? ratedCapacityOut.toPlainString() : null);

		out.writeObject(storageCapacity != null ? storageCapacity.toPlainString() : null);

		out.writeObject(storageType);

		super.writeExternal(out);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "technicalResourceId: " + technicalResourceId + "\n";
		res += "ratedCapacityIn: " + ratedCapacityIn + "\n";
		res += "ratedCapacityOut: " + ratedCapacityOut + "\n";
		res += "storageCapacity: " + storageCapacity + "\n";
		res += "storageType: " + storageType + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof StorageDataOfTechnicalResource) || id == null)
			return false;

		StorageDataOfTechnicalResource storageDataOfTechnicalResource = (StorageDataOfTechnicalResource) obj;

		if (this.id.equals(storageDataOfTechnicalResource.id))
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

		if (technicalResourceId != null) {
			res.append(technicalResourceId + ",");
		} else
			res.append(",");

		if (ratedCapacityIn != null) {
			res.append(ratedCapacityIn.toPlainString() + ",");
		} else
			res.append(",");

		if (ratedCapacityOut != null) {
			res.append(ratedCapacityOut.toPlainString() + ",");
		} else
			res.append(",");

		if (storageCapacity != null) {
			res.append(storageCapacity.toPlainString() + ",");
		} else
			res.append(",");

		if (storageType != null) {
			res.append(storageType + ",");
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

		//parsing technicalResourceId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.technicalResourceId = Long.valueOf(actualData);

		//parsing ratedCapacityIn
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.ratedCapacityIn = new java.math.BigDecimal(actualData);

		//parsing ratedCapacityOut
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.ratedCapacityOut = new java.math.BigDecimal(actualData);

		//parsing storageCapacity
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.storageCapacity = new java.math.BigDecimal(actualData);

		//parsing storageType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.storageType = actualData; //String

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

	public StorageDataOfTechnicalResource clone() {
		StorageDataOfTechnicalResource storageDataOfTechnicalResource = new StorageDataOfTechnicalResource();

		storageDataOfTechnicalResource.setTechnicalResourceId(this.getTechnicalResourceId());
		storageDataOfTechnicalResource.setRatedCapacityIn(this.getRatedCapacityIn());
		storageDataOfTechnicalResource.setRatedCapacityOut(this.getRatedCapacityOut());
		storageDataOfTechnicalResource.setStorageCapacity(this.getStorageCapacity());
		storageDataOfTechnicalResource.setStorageType(this.getStorageType());

		storageDataOfTechnicalResource.setPeriodStart(this.getPeriodStart());
		storageDataOfTechnicalResource.setPeriodEnd(this.getPeriodEnd());

		return storageDataOfTechnicalResource;
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
