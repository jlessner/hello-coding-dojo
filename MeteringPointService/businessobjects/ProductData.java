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
public class ProductData extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public ProductData() {
	}

	public ProductData(Long id, String code, String powerCurveCode, String clientILN, String clientType, String definitionCode, String definitionCodeType, Long pointOfDeliveryId, Long controllableResourceId) {

		this.id = id;

		this.code = code;

		this.powerCurveCode = powerCurveCode;

		this.clientILN = clientILN;

		this.clientType = clientType;

		this.definitionCode = definitionCode;

		this.definitionCodeType = definitionCodeType;

		this.pointOfDeliveryId = pointOfDeliveryId;

		this.controllableResourceId = controllableResourceId;

	}

	//primary Key

	protected Long id;

	protected String code;

	protected String powerCurveCode;

	protected String clientILN;

	protected String clientType;

	protected String definitionCode;

	protected String definitionCodeType;

	protected Long pointOfDeliveryId;

	protected Long controllableResourceId;

	public void setId(Long id) {
		this.id = id;
	}

	public ProductData withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ProductData withCode(String code) {
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setPowerCurveCode(String powerCurveCode) {
		this.powerCurveCode = powerCurveCode;
	}

	public ProductData withPowerCurveCode(String powerCurveCode) {
		this.powerCurveCode = powerCurveCode;
		return this;
	}

	public String getPowerCurveCode() {
		return powerCurveCode;
	}

	public void setClientILN(String clientILN) {
		this.clientILN = clientILN;
	}

	public ProductData withClientILN(String clientILN) {
		this.clientILN = clientILN;
		return this;
	}

	public String getClientILN() {
		return clientILN;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public ProductData withClientType(String clientType) {
		this.clientType = clientType;
		return this;
	}

	public String getClientType() {
		return clientType;
	}

	public void setDefinitionCode(String definitionCode) {
		this.definitionCode = definitionCode;
	}

	public ProductData withDefinitionCode(String definitionCode) {
		this.definitionCode = definitionCode;
		return this;
	}

	public String getDefinitionCode() {
		return definitionCode;
	}

	public void setDefinitionCodeType(String definitionCodeType) {
		this.definitionCodeType = definitionCodeType;
	}

	public ProductData withDefinitionCodeType(String definitionCodeType) {
		this.definitionCodeType = definitionCodeType;
		return this;
	}

	public String getDefinitionCodeType() {
		return definitionCodeType;
	}

	public void setPointOfDeliveryId(Long pointOfDeliveryId) {
		this.pointOfDeliveryId = pointOfDeliveryId;
	}

	public ProductData withPointOfDeliveryId(Long pointOfDeliveryId) {
		this.pointOfDeliveryId = pointOfDeliveryId;
		return this;
	}

	public Long getPointOfDeliveryId() {
		return pointOfDeliveryId;
	}

	public void setControllableResourceId(Long controllableResourceId) {
		this.controllableResourceId = controllableResourceId;
	}

	public ProductData withControllableResourceId(Long controllableResourceId) {
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

			code = (String) in.readObject();

			powerCurveCode = (String) in.readObject();

			clientILN = (String) in.readObject();

			clientType = (String) in.readObject();

			definitionCode = (String) in.readObject();

			definitionCodeType = (String) in.readObject();

			pointOfDeliveryId = ExternalizableUtil.parseLong(in.readObject());

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

		out.writeObject(code);

		out.writeObject(powerCurveCode);

		out.writeObject(clientILN);

		out.writeObject(clientType);

		out.writeObject(definitionCode);

		out.writeObject(definitionCodeType);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(pointOfDeliveryId));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(controllableResourceId));

		super.writeExternal(out);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "code: " + code + "\n";
		res += "powerCurveCode: " + powerCurveCode + "\n";
		res += "clientILN: " + clientILN + "\n";
		res += "clientType: " + clientType + "\n";
		res += "definitionCode: " + definitionCode + "\n";
		res += "definitionCodeType: " + definitionCodeType + "\n";
		res += "pointOfDeliveryId: " + pointOfDeliveryId + "\n";
		res += "controllableResourceId: " + controllableResourceId + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ProductData) || id == null)
			return false;

		ProductData productData = (ProductData) obj;

		if (this.id.equals(productData.id))
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

		if (code != null) {
			res.append(code + ",");
		} else
			res.append(",");

		if (powerCurveCode != null) {
			res.append(powerCurveCode + ",");
		} else
			res.append(",");

		if (clientILN != null) {
			res.append(clientILN + ",");
		} else
			res.append(",");

		if (clientType != null) {
			res.append(clientType + ",");
		} else
			res.append(",");

		if (definitionCode != null) {
			res.append(definitionCode + ",");
		} else
			res.append(",");

		if (definitionCodeType != null) {
			res.append(definitionCodeType + ",");
		} else
			res.append(",");

		if (pointOfDeliveryId != null) {
			res.append(pointOfDeliveryId + ",");
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

		//parsing code
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.code = actualData; //String

		//parsing powerCurveCode
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.powerCurveCode = actualData; //String

		//parsing clientILN
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.clientILN = actualData; //String

		//parsing clientType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.clientType = actualData; //String

		//parsing definitionCode
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.definitionCode = actualData; //String

		//parsing definitionCodeType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.definitionCodeType = actualData; //String

		//parsing pointOfDeliveryId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.pointOfDeliveryId = Long.valueOf(actualData);

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

	public ProductData clone() {
		ProductData productData = new ProductData();

		productData.setCode(this.getCode());
		productData.setPowerCurveCode(this.getPowerCurveCode());
		productData.setClientILN(this.getClientILN());
		productData.setClientType(this.getClientType());
		productData.setDefinitionCode(this.getDefinitionCode());
		productData.setDefinitionCodeType(this.getDefinitionCodeType());
		productData.setPointOfDeliveryId(this.getPointOfDeliveryId());
		productData.setControllableResourceId(this.getControllableResourceId());

		productData.setPeriodStart(this.getPeriodStart());
		productData.setPeriodEnd(this.getPeriodEnd());

		return productData;
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
