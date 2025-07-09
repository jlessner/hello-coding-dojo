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
public class DataOfControllableResource extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public DataOfControllableResource() {
	}

	public DataOfControllableResource(Long id, Long contralableResourceId, String capacityControlChannel, String controllableRessourceMsb, String controllableRessourceMsbType) {

		this.id = id;

		this.contralableResourceId = contralableResourceId;

		this.capacityControlChannel = capacityControlChannel;

		this.controllableRessourceMsb = controllableRessourceMsb;

		this.controllableRessourceMsbType = controllableRessourceMsbType;

	}

	//primary Key

	protected Long id;

	protected Long contralableResourceId;

	protected String capacityControlChannel;

	protected String controllableRessourceMsb;

	protected String controllableRessourceMsbType;

	public void setId(Long id) {
		this.id = id;
	}

	public DataOfControllableResource withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setContralableResourceId(Long contralableResourceId) {
		this.contralableResourceId = contralableResourceId;
	}

	public DataOfControllableResource withContralableResourceId(Long contralableResourceId) {
		this.contralableResourceId = contralableResourceId;
		return this;
	}

	public Long getContralableResourceId() {
		return contralableResourceId;
	}

	public void setCapacityControlChannel(String capacityControlChannel) {
		this.capacityControlChannel = capacityControlChannel;
	}

	public DataOfControllableResource withCapacityControlChannel(String capacityControlChannel) {
		this.capacityControlChannel = capacityControlChannel;
		return this;
	}

	public String getCapacityControlChannel() {
		return capacityControlChannel;
	}

	public void setControllableRessourceMsb(String controllableRessourceMsb) {
		this.controllableRessourceMsb = controllableRessourceMsb;
	}

	public DataOfControllableResource withControllableRessourceMsb(String controllableRessourceMsb) {
		this.controllableRessourceMsb = controllableRessourceMsb;
		return this;
	}

	public String getControllableRessourceMsb() {
		return controllableRessourceMsb;
	}

	public void setControllableRessourceMsbType(String controllableRessourceMsbType) {
		this.controllableRessourceMsbType = controllableRessourceMsbType;
	}

	public DataOfControllableResource withControllableRessourceMsbType(String controllableRessourceMsbType) {
		this.controllableRessourceMsbType = controllableRessourceMsbType;
		return this;
	}

	public String getControllableRessourceMsbType() {
		return controllableRessourceMsbType;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			contralableResourceId = ExternalizableUtil.parseLong(in.readObject());

			capacityControlChannel = (String) in.readObject();

			controllableRessourceMsb = (String) in.readObject();

			controllableRessourceMsbType = (String) in.readObject();

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

		out.writeObject(ExternalizableUtil.checkNumericNullValue(contralableResourceId));

		out.writeObject(capacityControlChannel);

		out.writeObject(controllableRessourceMsb);

		out.writeObject(controllableRessourceMsbType);

		super.writeExternal(out);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "contralableResourceId: " + contralableResourceId + "\n";
		res += "capacityControlChannel: " + capacityControlChannel + "\n";
		res += "controllableRessourceMsb: " + controllableRessourceMsb + "\n";
		res += "controllableRessourceMsbType: " + controllableRessourceMsbType + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof DataOfControllableResource) || id == null)
			return false;

		DataOfControllableResource dataOfControllableResource = (DataOfControllableResource) obj;

		if (this.id.equals(dataOfControllableResource.id))
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

		if (contralableResourceId != null) {
			res.append(contralableResourceId + ",");
		} else
			res.append(",");

		if (capacityControlChannel != null) {
			res.append(capacityControlChannel + ",");
		} else
			res.append(",");

		if (controllableRessourceMsb != null) {
			res.append(controllableRessourceMsb + ",");
		} else
			res.append(",");

		if (controllableRessourceMsbType != null) {
			res.append(controllableRessourceMsbType + ",");
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

		//parsing contralableResourceId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.contralableResourceId = Long.valueOf(actualData);

		//parsing capacityControlChannel
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.capacityControlChannel = actualData; //String

		//parsing controllableRessourceMsb
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.controllableRessourceMsb = actualData; //String

		//parsing controllableRessourceMsbType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.controllableRessourceMsbType = actualData; //String

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

	public DataOfControllableResource clone() {
		DataOfControllableResource dataOfControllableResource = new DataOfControllableResource();

		dataOfControllableResource.setContralableResourceId(this.getContralableResourceId());
		dataOfControllableResource.setCapacityControlChannel(this.getCapacityControlChannel());
		dataOfControllableResource.setControllableRessourceMsb(this.getControllableRessourceMsb());
		dataOfControllableResource.setControllableRessourceMsbType(this.getControllableRessourceMsbType());

		dataOfControllableResource.setPeriodStart(this.getPeriodStart());
		dataOfControllableResource.setPeriodEnd(this.getPeriodEnd());

		return dataOfControllableResource;
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
