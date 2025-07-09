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
public class PointOfDelivery implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	public PointOfDelivery() {
	}

	public PointOfDelivery(Long id, String meteringPoint, String meteringPointType, com.nextlevel.fastlane.myBusinessSupplier.bo.Address address, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract> contracts,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter> meters, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails> supplyDetails,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition> netUsageInvoicingPosition, String section, Long parent, String classification, String transmissionType, String prognosisVote) {

		this.id = id;

		this.meteringPoint = meteringPoint;

		this.meteringPointType = meteringPointType;

		this.address = address;

		this.contracts = contracts;

		this.meters = meters;

		this.supplyDetails = supplyDetails;

		this.netUsageInvoicingPosition = netUsageInvoicingPosition;

		this.section = section;

		this.parent = parent;

		this.classification = classification;

		this.transmissionType = transmissionType;

		this.prognosisVote = prognosisVote;

	}

	//primary Key

	protected Long id;

	protected String meteringPoint;

	protected String meteringPointType;

	protected com.nextlevel.fastlane.myBusinessSupplier.bo.Address address;

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.Contract.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract> contracts = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.Meter.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter> meters = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails> supplyDetails = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition> netUsageInvoicingPosition = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition>();

	protected String section;

	protected Long parent;

	protected String classification;

	protected String transmissionType;

	protected String prognosisVote;

	// foreign Key
	//public otherwise mate mapping dont works here

	public void setId(Long id) {
		this.id = id;
	}

	public PointOfDelivery withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setMeteringPoint(String meteringPoint) {
		this.meteringPoint = meteringPoint;
	}

	public PointOfDelivery withMeteringPoint(String meteringPoint) {
		this.meteringPoint = meteringPoint;
		return this;
	}

	public String getMeteringPoint() {
		return meteringPoint;
	}

	public void setMeteringPointType(String meteringPointType) {
		this.meteringPointType = meteringPointType;
	}

	public PointOfDelivery withMeteringPointType(String meteringPointType) {
		this.meteringPointType = meteringPointType;
		return this;
	}

	public String getMeteringPointType() {
		return meteringPointType;
	}

	public void setAddress(com.nextlevel.fastlane.myBusinessSupplier.bo.Address address) {
		this.address = address;
	}

	public PointOfDelivery withAddress(com.nextlevel.fastlane.myBusinessSupplier.bo.Address address) {
		this.address = address;
		return this;
	}

	public com.nextlevel.fastlane.myBusinessSupplier.bo.Address getAddress() {
		return address;
	}

	public void setContracts(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract> contracts) {
		this.contracts = contracts;
	}

	public PointOfDelivery withContracts(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract> contracts) {
		this.contracts = contracts;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract> getContracts() {
		return contracts;
	}

	public PointOfDelivery addContracts(com.nextlevel.fastlane.myBusinessSupplier.bo.Contract value) {
		this.contracts.add(value);
		return this;
	}

	public void setMeters(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter> meters) {
		this.meters = meters;
	}

	public PointOfDelivery withMeters(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter> meters) {
		this.meters = meters;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter> getMeters() {
		return meters;
	}

	public PointOfDelivery addMeters(com.nextlevel.fastlane.myBusinessSupplier.bo.Meter value) {
		this.meters.add(value);
		return this;
	}

	public void setSupplyDetails(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails> supplyDetails) {
		this.supplyDetails = supplyDetails;
	}

	public PointOfDelivery withSupplyDetails(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails> supplyDetails) {
		this.supplyDetails = supplyDetails;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails> getSupplyDetails() {
		return supplyDetails;
	}

	public PointOfDelivery addSupplyDetails(com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails value) {
		this.supplyDetails.add(value);
		return this;
	}

	public void setNetUsageInvoicingPosition(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition> netUsageInvoicingPosition) {
		this.netUsageInvoicingPosition = netUsageInvoicingPosition;
	}

	public PointOfDelivery withNetUsageInvoicingPosition(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition> netUsageInvoicingPosition) {
		this.netUsageInvoicingPosition = netUsageInvoicingPosition;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition> getNetUsageInvoicingPosition() {
		return netUsageInvoicingPosition;
	}

	public PointOfDelivery addNetUsageInvoicingPosition(com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition value) {
		this.netUsageInvoicingPosition.add(value);
		return this;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public PointOfDelivery withSection(String section) {
		this.section = section;
		return this;
	}

	public String getSection() {
		return section;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public PointOfDelivery withParent(Long parent) {
		this.parent = parent;
		return this;
	}

	public Long getParent() {
		return parent;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public PointOfDelivery withClassification(String classification) {
		this.classification = classification;
		return this;
	}

	public String getClassification() {
		return classification;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	public PointOfDelivery withTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
		return this;
	}

	public String getTransmissionType() {
		return transmissionType;
	}

	public void setPrognosisVote(String prognosisVote) {
		this.prognosisVote = prognosisVote;
	}

	public PointOfDelivery withPrognosisVote(String prognosisVote) {
		this.prognosisVote = prognosisVote;
		return this;
	}

	public String getPrognosisVote() {
		return prognosisVote;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			meteringPoint = (String) in.readObject();

			meteringPointType = (String) in.readObject();

			address = (com.nextlevel.fastlane.myBusinessSupplier.bo.Address) in.readObject();

			contracts = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract>) in.readObject();

			meters = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter>) in.readObject();

			supplyDetails = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails>) in.readObject();

			netUsageInvoicingPosition = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition>) in.readObject();

			section = (String) in.readObject();

			parent = ExternalizableUtil.parseLong(in.readObject());

			classification = (String) in.readObject();

			transmissionType = (String) in.readObject();

			prognosisVote = (String) in.readObject();

			// foreign Keys

		} catch (java.io.OptionalDataException e) {
			//ignore Exception
		} catch (java.io.EOFException e) {
			//ignore Exception
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(ExternalizableUtil.checkNumericNullValue(id));

		out.writeObject(meteringPoint);

		out.writeObject(meteringPointType);

		out.writeObject(address);

		out.writeObject(contracts);

		out.writeObject(meters);

		out.writeObject(supplyDetails);

		out.writeObject(netUsageInvoicingPosition);

		out.writeObject(section);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(parent));

		out.writeObject(classification);

		out.writeObject(transmissionType);

		out.writeObject(prognosisVote);

		// foreign Keys

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "meteringPoint: " + meteringPoint + "\n";
		res += "meteringPointType: " + meteringPointType + "\n";
		res += "address: " + address + "\n";
		res += "contracts: " + contracts + "\n";
		res += "meters: " + meters + "\n";
		res += "supplyDetails: " + supplyDetails + "\n";
		res += "netUsageInvoicingPosition: " + netUsageInvoicingPosition + "\n";
		res += "section: " + section + "\n";
		res += "parent: " + parent + "\n";
		res += "classification: " + classification + "\n";
		res += "transmissionType: " + transmissionType + "\n";
		res += "prognosisVote: " + prognosisVote + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof PointOfDelivery) || id == null)
			return false;

		PointOfDelivery pointOfDelivery = (PointOfDelivery) obj;

		if (this.id.equals(pointOfDelivery.id))
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

		if (meteringPoint != null) {
			res.append(meteringPoint + ",");
		} else
			res.append(",");

		if (meteringPointType != null) {
			res.append(meteringPointType + ",");
		} else
			res.append(",");

		if (address != null)
			res.append(address.serializeToString() + ",");
		else
			res.append(new com.nextlevel.fastlane.myBusinessSupplier.bo.Address().serializeToString() + ",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract> contractsList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract>) contracts;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.Contract entry : contractsList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (contractsList != null && contractsList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter> metersList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter>) meters;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.Meter entry : metersList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (metersList != null && metersList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails> supplyDetailsList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails>) supplyDetails;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails entry : supplyDetailsList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (supplyDetailsList != null && supplyDetailsList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition> netUsageInvoicingPositionList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition>) netUsageInvoicingPosition;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition entry : netUsageInvoicingPositionList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (netUsageInvoicingPositionList != null && netUsageInvoicingPositionList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		if (section != null) {
			res.append(section + ",");
		} else
			res.append(",");

		if (parent != null) {
			res.append(parent + ",");
		} else
			res.append(",");

		if (classification != null) {
			res.append(classification + ",");
		} else
			res.append(",");

		if (transmissionType != null) {
			res.append(transmissionType + ",");
		} else
			res.append(",");

		if (prognosisVote != null) {
			res.append(prognosisVote + ",");
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

		//parsing meteringPoint
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.meteringPoint = actualData; //String

		//parsing meteringPointType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.meteringPointType = actualData; //String

		//parsing address
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		com.nextlevel.fastlane.myBusinessSupplier.bo.Address address = new com.nextlevel.fastlane.myBusinessSupplier.bo.Address();
		address.deserializeFromString(actualData);
		this.address = address;

		//parsing contracts
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.contracts = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.Contract>();
		int pos2contracts = 1;
		int end2contracts = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2contracts < actualData.length()) {
				String actualData2 = null;
				end2contracts = findEndOfSegment(pos2contracts, actualData);
				actualData2 = actualData.substring(pos2contracts, end2contracts);

				com.nextlevel.fastlane.myBusinessSupplier.bo.Contract obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.Contract();
				obj.deserializeFromString(actualData2);
				contracts.add(obj);
				pos2contracts = end2contracts + 1;
			}

		//parsing meters
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.meters = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.Meter>();
		int pos2meters = 1;
		int end2meters = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2meters < actualData.length()) {
				String actualData2 = null;
				end2meters = findEndOfSegment(pos2meters, actualData);
				actualData2 = actualData.substring(pos2meters, end2meters);

				com.nextlevel.fastlane.myBusinessSupplier.bo.Meter obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.Meter();
				obj.deserializeFromString(actualData2);
				meters.add(obj);
				pos2meters = end2meters + 1;
			}

		//parsing supplyDetails
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.supplyDetails = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails>();
		int pos2supplyDetails = 1;
		int end2supplyDetails = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2supplyDetails < actualData.length()) {
				String actualData2 = null;
				end2supplyDetails = findEndOfSegment(pos2supplyDetails, actualData);
				actualData2 = actualData.substring(pos2supplyDetails, end2supplyDetails);

				com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.SupplyDetails();
				obj.deserializeFromString(actualData2);
				supplyDetails.add(obj);
				pos2supplyDetails = end2supplyDetails + 1;
			}

		//parsing netUsageInvoicingPosition
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.netUsageInvoicingPosition = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition>();
		int pos2netUsageInvoicingPosition = 1;
		int end2netUsageInvoicingPosition = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2netUsageInvoicingPosition < actualData.length()) {
				String actualData2 = null;
				end2netUsageInvoicingPosition = findEndOfSegment(pos2netUsageInvoicingPosition, actualData);
				actualData2 = actualData.substring(pos2netUsageInvoicingPosition, end2netUsageInvoicingPosition);

				com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.NetUsageInvoicingPosition();
				obj.deserializeFromString(actualData2);
				netUsageInvoicingPosition.add(obj);
				pos2netUsageInvoicingPosition = end2netUsageInvoicingPosition + 1;
			}

		//parsing section
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.section = actualData; //String

		//parsing parent
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.parent = Long.valueOf(actualData);

		//parsing classification
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.classification = actualData; //String

		//parsing transmissionType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.transmissionType = actualData; //String

		//parsing prognosisVote
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.prognosisVote = actualData; //String

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

	public PointOfDelivery clone() {
		PointOfDelivery pointOfDelivery = new PointOfDelivery();

		pointOfDelivery.setMeteringPoint(this.getMeteringPoint());
		pointOfDelivery.setMeteringPointType(this.getMeteringPointType());
		pointOfDelivery.setAddress(this.getAddress());
		pointOfDelivery.setContracts(this.getContracts());
		pointOfDelivery.setMeters(this.getMeters());
		pointOfDelivery.setSupplyDetails(this.getSupplyDetails());
		pointOfDelivery.setNetUsageInvoicingPosition(this.getNetUsageInvoicingPosition());
		pointOfDelivery.setSection(this.getSection());
		pointOfDelivery.setParent(this.getParent());
		pointOfDelivery.setClassification(this.getClassification());
		pointOfDelivery.setTransmissionType(this.getTransmissionType());
		pointOfDelivery.setPrognosisVote(this.getPrognosisVote());

		return pointOfDelivery;
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
