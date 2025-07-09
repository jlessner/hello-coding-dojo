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
public class UsageDataOfTechnicalResource extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public UsageDataOfTechnicalResource() {
	}

	public UsageDataOfTechnicalResource(Long id, Long technicalResourceId, java.math.BigDecimal ratedCapacityIn, String thermalUse, String eMobilityType, String categoryEnWG, String classificationEnWG, String otherTechnicalDevices,
			String commissioningDateEnWG) {

		this.id = id;

		this.technicalResourceId = technicalResourceId;

		this.ratedCapacityIn = ratedCapacityIn;

		this.thermalUse = thermalUse;

		this.eMobilityType = eMobilityType;

		this.categoryEnWG = categoryEnWG;

		this.classificationEnWG = classificationEnWG;

		this.otherTechnicalDevices = otherTechnicalDevices;

		this.commissioningDateEnWG = commissioningDateEnWG;

	}

	//primary Key

	protected Long id;

	protected Long technicalResourceId;

	protected java.math.BigDecimal ratedCapacityIn;

	protected String thermalUse;

	protected String eMobilityType;

	protected String categoryEnWG;

	protected String classificationEnWG;

	protected String otherTechnicalDevices;

	protected String commissioningDateEnWG;

	public void setId(Long id) {
		this.id = id;
	}

	public UsageDataOfTechnicalResource withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setTechnicalResourceId(Long technicalResourceId) {
		this.technicalResourceId = technicalResourceId;
	}

	public UsageDataOfTechnicalResource withTechnicalResourceId(Long technicalResourceId) {
		this.technicalResourceId = technicalResourceId;
		return this;
	}

	public Long getTechnicalResourceId() {
		return technicalResourceId;
	}

	public void setRatedCapacityIn(java.math.BigDecimal ratedCapacityIn) {
		this.ratedCapacityIn = ratedCapacityIn;
	}

	public UsageDataOfTechnicalResource withRatedCapacityIn(java.math.BigDecimal ratedCapacityIn) {
		this.ratedCapacityIn = ratedCapacityIn;
		return this;
	}

	public java.math.BigDecimal getRatedCapacityIn() {
		return ratedCapacityIn;
	}

	public void setThermalUse(String thermalUse) {
		this.thermalUse = thermalUse;
	}

	public UsageDataOfTechnicalResource withThermalUse(String thermalUse) {
		this.thermalUse = thermalUse;
		return this;
	}

	public String getThermalUse() {
		return thermalUse;
	}

	public void setEMobilityType(String eMobilityType) {
		this.eMobilityType = eMobilityType;
	}

	public UsageDataOfTechnicalResource withEMobilityType(String eMobilityType) {
		this.eMobilityType = eMobilityType;
		return this;
	}

	public String getEMobilityType() {
		return eMobilityType;
	}

	public void setCategoryEnWG(String categoryEnWG) {
		this.categoryEnWG = categoryEnWG;
	}

	public UsageDataOfTechnicalResource withCategoryEnWG(String categoryEnWG) {
		this.categoryEnWG = categoryEnWG;
		return this;
	}

	public String getCategoryEnWG() {
		return categoryEnWG;
	}

	public void setClassificationEnWG(String classificationEnWG) {
		this.classificationEnWG = classificationEnWG;
	}

	public UsageDataOfTechnicalResource withClassificationEnWG(String classificationEnWG) {
		this.classificationEnWG = classificationEnWG;
		return this;
	}

	public String getClassificationEnWG() {
		return classificationEnWG;
	}

	public void setOtherTechnicalDevices(String otherTechnicalDevices) {
		this.otherTechnicalDevices = otherTechnicalDevices;
	}

	public UsageDataOfTechnicalResource withOtherTechnicalDevices(String otherTechnicalDevices) {
		this.otherTechnicalDevices = otherTechnicalDevices;
		return this;
	}

	public String getOtherTechnicalDevices() {
		return otherTechnicalDevices;
	}

	public void setCommissioningDateEnWG(String commissioningDateEnWG) {
		this.commissioningDateEnWG = commissioningDateEnWG;
	}

	public UsageDataOfTechnicalResource withCommissioningDateEnWG(String commissioningDateEnWG) {
		this.commissioningDateEnWG = commissioningDateEnWG;
		return this;
	}

	public String getCommissioningDateEnWG() {
		return commissioningDateEnWG;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			technicalResourceId = ExternalizableUtil.parseLong(in.readObject());

			final Object ratedCapacityInTemp = in.readObject();
			ratedCapacityIn = (ratedCapacityInTemp != null && !"".equals(ratedCapacityInTemp)) ? new java.math.BigDecimal(ratedCapacityInTemp.toString()) : null;

			thermalUse = (String) in.readObject();

			eMobilityType = (String) in.readObject();

			categoryEnWG = (String) in.readObject();

			classificationEnWG = (String) in.readObject();

			otherTechnicalDevices = (String) in.readObject();

			commissioningDateEnWG = (String) in.readObject();

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

		out.writeObject(thermalUse);

		out.writeObject(eMobilityType);

		out.writeObject(categoryEnWG);

		out.writeObject(classificationEnWG);

		out.writeObject(otherTechnicalDevices);

		out.writeObject(commissioningDateEnWG);

		super.writeExternal(out);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "technicalResourceId: " + technicalResourceId + "\n";
		res += "ratedCapacityIn: " + ratedCapacityIn + "\n";
		res += "thermalUse: " + thermalUse + "\n";
		res += "eMobilityType: " + eMobilityType + "\n";
		res += "categoryEnWG: " + categoryEnWG + "\n";
		res += "classificationEnWG: " + classificationEnWG + "\n";
		res += "otherTechnicalDevices: " + otherTechnicalDevices + "\n";
		res += "commissioningDateEnWG: " + commissioningDateEnWG + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof UsageDataOfTechnicalResource) || id == null)
			return false;

		UsageDataOfTechnicalResource usageDataOfTechnicalResource = (UsageDataOfTechnicalResource) obj;

		if (this.id.equals(usageDataOfTechnicalResource.id))
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

		if (thermalUse != null) {
			res.append(thermalUse + ",");
		} else
			res.append(",");

		if (eMobilityType != null) {
			res.append(eMobilityType + ",");
		} else
			res.append(",");

		if (categoryEnWG != null) {
			res.append(categoryEnWG + ",");
		} else
			res.append(",");

		if (classificationEnWG != null) {
			res.append(classificationEnWG + ",");
		} else
			res.append(",");

		if (otherTechnicalDevices != null) {
			res.append(otherTechnicalDevices + ",");
		} else
			res.append(",");

		if (commissioningDateEnWG != null) {
			res.append(commissioningDateEnWG + ",");
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

		//parsing thermalUse
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.thermalUse = actualData; //String

		//parsing eMobilityType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.eMobilityType = actualData; //String

		//parsing categoryEnWG
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.categoryEnWG = actualData; //String

		//parsing classificationEnWG
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.classificationEnWG = actualData; //String

		//parsing otherTechnicalDevices
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.otherTechnicalDevices = actualData; //String

		//parsing commissioningDateEnWG
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.commissioningDateEnWG = actualData; //String

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

	public UsageDataOfTechnicalResource clone() {
		UsageDataOfTechnicalResource usageDataOfTechnicalResource = new UsageDataOfTechnicalResource();

		usageDataOfTechnicalResource.setTechnicalResourceId(this.getTechnicalResourceId());
		usageDataOfTechnicalResource.setRatedCapacityIn(this.getRatedCapacityIn());
		usageDataOfTechnicalResource.setThermalUse(this.getThermalUse());
		usageDataOfTechnicalResource.setEMobilityType(this.getEMobilityType());
		usageDataOfTechnicalResource.setCategoryEnWG(this.getCategoryEnWG());
		usageDataOfTechnicalResource.setClassificationEnWG(this.getClassificationEnWG());
		usageDataOfTechnicalResource.setOtherTechnicalDevices(this.getOtherTechnicalDevices());
		usageDataOfTechnicalResource.setCommissioningDateEnWG(this.getCommissioningDateEnWG());

		usageDataOfTechnicalResource.setPeriodStart(this.getPeriodStart());
		usageDataOfTechnicalResource.setPeriodEnd(this.getPeriodEnd());

		return usageDataOfTechnicalResource;
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
