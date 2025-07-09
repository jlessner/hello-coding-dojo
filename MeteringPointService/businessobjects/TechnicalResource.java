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
public class TechnicalResource extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public TechnicalResource() {
	}

	public TechnicalResource(Long id, String identification, Long meloId, Long maloId, Long neloId, Long controllableResourceId) {

		this.id = id;

		this.identification = identification;

		this.meloId = meloId;

		this.maloId = maloId;

		this.neloId = neloId;

		this.controllableResourceId = controllableResourceId;

	}

	//primary Key

	protected Long id;

	protected String identification;

	protected Long meloId;

	protected Long maloId;

	protected Long neloId;

	protected Long controllableResourceId;

	public void setId(Long id) {
		this.id = id;
	}

	public TechnicalResource withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public TechnicalResource withIdentification(String identification) {
		this.identification = identification;
		return this;
	}

	public String getIdentification() {
		return identification;
	}

	public void setMeloId(Long meloId) {
		this.meloId = meloId;
	}

	public TechnicalResource withMeloId(Long meloId) {
		this.meloId = meloId;
		return this;
	}

	public Long getMeloId() {
		return meloId;
	}

	public void setMaloId(Long maloId) {
		this.maloId = maloId;
	}

	public TechnicalResource withMaloId(Long maloId) {
		this.maloId = maloId;
		return this;
	}

	public Long getMaloId() {
		return maloId;
	}

	public void setNeloId(Long neloId) {
		this.neloId = neloId;
	}

	public TechnicalResource withNeloId(Long neloId) {
		this.neloId = neloId;
		return this;
	}

	public Long getNeloId() {
		return neloId;
	}

	public void setControllableResourceId(Long controllableResourceId) {
		this.controllableResourceId = controllableResourceId;
	}

	public TechnicalResource withControllableResourceId(Long controllableResourceId) {
		this.controllableResourceId = controllableResourceId;
		return this;
	}

	public Long getControllableResourceId() {
		return controllableResourceId;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			identification = (String) in.readObject();

			meloId = ExternalizableUtil.parseLong(in.readObject());

			maloId = ExternalizableUtil.parseLong(in.readObject());

			neloId = ExternalizableUtil.parseLong(in.readObject());

			controllableResourceId = ExternalizableUtil.parseLong(in.readObject());

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

		out.writeObject(identification);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(meloId));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(maloId));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(neloId));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(controllableResourceId));

		super.writeExternal(out);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "identification: " + identification + "\n";
		res += "meloId: " + meloId + "\n";
		res += "maloId: " + maloId + "\n";
		res += "neloId: " + neloId + "\n";
		res += "controllableResourceId: " + controllableResourceId + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TechnicalResource) || id == null)
			return false;

		TechnicalResource technicalResource = (TechnicalResource) obj;

		if (this.id.equals(technicalResource.id))
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

		if (identification != null) {
			res.append(identification + ",");
		} else
			res.append(",");

		if (meloId != null) {
			res.append(meloId + ",");
		} else
			res.append(",");

		if (maloId != null) {
			res.append(maloId + ",");
		} else
			res.append(",");

		if (neloId != null) {
			res.append(neloId + ",");
		} else
			res.append(",");

		if (controllableResourceId != null) {
			res.append(controllableResourceId + ",");
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

		//parsing identification
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.identification = actualData; //String

		//parsing meloId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.meloId = Long.valueOf(actualData);

		//parsing maloId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.maloId = Long.valueOf(actualData);

		//parsing neloId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.neloId = Long.valueOf(actualData);

		//parsing controllableResourceId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.controllableResourceId = Long.valueOf(actualData);

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

	public TechnicalResource clone() {
		TechnicalResource technicalResource = new TechnicalResource();

		technicalResource.setIdentification(this.getIdentification());
		technicalResource.setMeloId(this.getMeloId());
		technicalResource.setMaloId(this.getMaloId());
		technicalResource.setNeloId(this.getNeloId());
		technicalResource.setControllableResourceId(this.getControllableResourceId());

		technicalResource.setPeriodStart(this.getPeriodStart());
		technicalResource.setPeriodEnd(this.getPeriodEnd());

		return technicalResource;
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
