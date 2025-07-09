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

import com.nextlevel.fastlane.financial.bo.*;

import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;

@XmlRootElement(namespace = "com.nextlevel.fastlane.myBusinessSupplier.bo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Contract extends TimeSlice implements Externalizable, StringSerializable {

	private static final transient String _dateFormatString_ = "yyyyMMddHHmm";

	static final long serialVersionUID = 1L;

	public Contract() {
	}

	public Contract(Long id, Integer oldId, String section, String contractNr, java.util.Date inBoxDate, java.util.Date contractConclusionDate, Integer desiredAdvancePayEveryNMonth, Boolean useNextPossibleCancellationDate,
			java.util.Date cancellationDateTo, String cancellationCreationReason, java.util.Date cancellationDateCollected, Boolean cancellationCalculateFromSignatureDate, java.util.Date changeDate,
			java.math.BigDecimal consumptionHTCustomerDeclaration, String voltageLevelCustomerDeclaration, java.util.Date customerDeclarationDate, java.util.Date supplierOldCancelledDate,
			java.math.BigDecimal lastYearConsumptionSupplierOld, Boolean welcomeMailSent, Integer lastInventoryListMonth, Integer invoicingInterval, String invoicingInformationInterval,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner> contractPartners, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication> tariffApplications,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan> advancePayPlans, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage> ediMessages,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage> aepMakoMessages, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice> creditorInvoice,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice> debitorInvoice, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval> invoicingIntervals,
			java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis> powerConsumptionPrognosis, java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration> contractTypeConfiguration,
			java.util.Date nextInvoiceDate, Integer oldSupplyId, com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason lockReason, String resellerId, java.util.Date customerSignatureDate, java.util.Date lockDate,
			String stateAddition, String type, Long contractAdditionalId, String virtualIban, String communicationType, java.util.Date extraordinaryCancellationDate, AccountingCharacteristic accountingCharacteristic,
			com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector businessSector, com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger invoiceTrigger, String selfReadingType, java.math.BigDecimal ratingScore,
			java.util.Date msbCycleReadingStart, java.util.Date msbCycleReadingEnd, Boolean suspendAccountingCharacteristicCheck, String boniPruefIdentNr) {

		this.id = id;

		this.oldId = oldId;

		this.section = section;

		this.contractNr = contractNr;

		this.inBoxDate = inBoxDate;

		this.contractConclusionDate = contractConclusionDate;

		this.desiredAdvancePayEveryNMonth = desiredAdvancePayEveryNMonth;

		this.useNextPossibleCancellationDate = useNextPossibleCancellationDate;

		this.cancellationDateTo = cancellationDateTo;

		this.cancellationCreationReason = cancellationCreationReason;

		this.cancellationDateCollected = cancellationDateCollected;

		this.cancellationCalculateFromSignatureDate = cancellationCalculateFromSignatureDate;

		this.changeDate = changeDate;

		this.consumptionHTCustomerDeclaration = consumptionHTCustomerDeclaration;

		this.voltageLevelCustomerDeclaration = voltageLevelCustomerDeclaration;

		this.customerDeclarationDate = customerDeclarationDate;

		this.supplierOldCancelledDate = supplierOldCancelledDate;

		this.lastYearConsumptionSupplierOld = lastYearConsumptionSupplierOld;

		this.welcomeMailSent = welcomeMailSent;

		this.lastInventoryListMonth = lastInventoryListMonth;

		this.invoicingInterval = invoicingInterval;

		this.invoicingInformationInterval = invoicingInformationInterval;

		this.contractPartners = contractPartners;

		this.tariffApplications = tariffApplications;

		this.advancePayPlans = advancePayPlans;

		this.ediMessages = ediMessages;

		this.aepMakoMessages = aepMakoMessages;

		this.creditorInvoice = creditorInvoice;

		this.debitorInvoice = debitorInvoice;

		this.invoicingIntervals = invoicingIntervals;

		this.powerConsumptionPrognosis = powerConsumptionPrognosis;

		this.contractTypeConfiguration = contractTypeConfiguration;

		this.nextInvoiceDate = nextInvoiceDate;

		this.oldSupplyId = oldSupplyId;

		this.lockReason = lockReason;

		this.resellerId = resellerId;

		this.customerSignatureDate = customerSignatureDate;

		this.lockDate = lockDate;

		this.stateAddition = stateAddition;

		this.type = type;

		this.contractAdditionalId = contractAdditionalId;

		this.virtualIban = virtualIban;

		this.communicationType = communicationType;

		this.extraordinaryCancellationDate = extraordinaryCancellationDate;

		this.accountingCharacteristic = accountingCharacteristic;

		this.businessSector = businessSector;

		this.invoiceTrigger = invoiceTrigger;

		this.selfReadingType = selfReadingType;

		this.ratingScore = ratingScore;

		this.msbCycleReadingStart = msbCycleReadingStart;

		this.msbCycleReadingEnd = msbCycleReadingEnd;

		this.suspendAccountingCharacteristicCheck = suspendAccountingCharacteristicCheck;

		this.boniPruefIdentNr = boniPruefIdentNr;

	}

	//primary Key

	protected Long id;

	protected Integer oldId;

	protected String section;

	protected String contractNr;

	protected java.util.Date inBoxDate;

	protected java.util.Date contractConclusionDate;

	protected Integer desiredAdvancePayEveryNMonth;

	protected Boolean useNextPossibleCancellationDate;

	protected java.util.Date cancellationDateTo;

	protected String cancellationCreationReason;

	protected java.util.Date cancellationDateCollected;

	protected Boolean cancellationCalculateFromSignatureDate;

	protected java.util.Date changeDate;

	protected java.math.BigDecimal consumptionHTCustomerDeclaration;

	protected String voltageLevelCustomerDeclaration;

	protected java.util.Date customerDeclarationDate;

	protected java.util.Date supplierOldCancelledDate;

	protected java.math.BigDecimal lastYearConsumptionSupplierOld;

	protected Boolean welcomeMailSent;

	protected Integer lastInventoryListMonth;

	protected Integer invoicingInterval;

	protected String invoicingInformationInterval;

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner> contractPartners = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication> tariffApplications = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan> advancePayPlans = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage> ediMessages = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage> aepMakoMessages = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice> creditorInvoice = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice> debitorInvoice = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval> invoicingIntervals = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis> powerConsumptionPrognosis = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis>();

	@XmlElement(type = com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration.class)
	protected java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration> contractTypeConfiguration = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration>();

	protected java.util.Date nextInvoiceDate;

	protected Integer oldSupplyId;

	protected com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason lockReason;

	protected String resellerId;

	protected java.util.Date customerSignatureDate;

	protected java.util.Date lockDate;

	protected String stateAddition;

	protected String type;

	protected Long contractAdditionalId;

	protected String virtualIban;

	protected String communicationType;

	protected java.util.Date extraordinaryCancellationDate;

	protected AccountingCharacteristic accountingCharacteristic;

	protected com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector businessSector;

	protected com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger invoiceTrigger;

	protected String selfReadingType;

	protected java.math.BigDecimal ratingScore;

	protected java.util.Date msbCycleReadingStart;

	protected java.util.Date msbCycleReadingEnd;

	protected Boolean suspendAccountingCharacteristicCheck;

	protected String boniPruefIdentNr;

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	public Long contractAccount;

	// foreign Key
	//public otherwise mate mapping dont works here

	public String state;

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	public Long pointOfDelivery;

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	// foreign Key
	//public otherwise mate mapping dont works here

	public void setId(Long id) {
		this.id = id;
	}

	public Contract withId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public Contract withOldId(Integer oldId) {
		this.oldId = oldId;
		return this;
	}

	public Integer getOldId() {
		return oldId;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Contract withSection(String section) {
		this.section = section;
		return this;
	}

	public String getSection() {
		return section;
	}

	public void setContractNr(String contractNr) {
		this.contractNr = contractNr;
	}

	public Contract withContractNr(String contractNr) {
		this.contractNr = contractNr;
		return this;
	}

	public String getContractNr() {
		return contractNr;
	}

	public void setInBoxDate(java.util.Date inBoxDate) {
		this.inBoxDate = inBoxDate;
	}

	public Contract withInBoxDate(java.util.Date inBoxDate) {
		this.inBoxDate = inBoxDate;
		return this;
	}

	public java.util.Date getInBoxDate() {
		return inBoxDate;
	}

	public void setContractConclusionDate(java.util.Date contractConclusionDate) {
		this.contractConclusionDate = contractConclusionDate;
	}

	public Contract withContractConclusionDate(java.util.Date contractConclusionDate) {
		this.contractConclusionDate = contractConclusionDate;
		return this;
	}

	public java.util.Date getContractConclusionDate() {
		return contractConclusionDate;
	}

	public void setDesiredAdvancePayEveryNMonth(Integer desiredAdvancePayEveryNMonth) {
		this.desiredAdvancePayEveryNMonth = desiredAdvancePayEveryNMonth;
	}

	public Contract withDesiredAdvancePayEveryNMonth(Integer desiredAdvancePayEveryNMonth) {
		this.desiredAdvancePayEveryNMonth = desiredAdvancePayEveryNMonth;
		return this;
	}

	public Integer getDesiredAdvancePayEveryNMonth() {
		return desiredAdvancePayEveryNMonth;
	}

	public void setUseNextPossibleCancellationDate(Boolean useNextPossibleCancellationDate) {
		this.useNextPossibleCancellationDate = useNextPossibleCancellationDate;
	}

	public Contract withUseNextPossibleCancellationDate(Boolean useNextPossibleCancellationDate) {
		this.useNextPossibleCancellationDate = useNextPossibleCancellationDate;
		return this;
	}

	public Boolean getUseNextPossibleCancellationDate() {
		return useNextPossibleCancellationDate;
	}

	public void setCancellationDateTo(java.util.Date cancellationDateTo) {
		this.cancellationDateTo = cancellationDateTo;
	}

	public Contract withCancellationDateTo(java.util.Date cancellationDateTo) {
		this.cancellationDateTo = cancellationDateTo;
		return this;
	}

	public java.util.Date getCancellationDateTo() {
		return cancellationDateTo;
	}

	public void setCancellationCreationReason(String cancellationCreationReason) {
		this.cancellationCreationReason = cancellationCreationReason;
	}

	public Contract withCancellationCreationReason(String cancellationCreationReason) {
		this.cancellationCreationReason = cancellationCreationReason;
		return this;
	}

	public String getCancellationCreationReason() {
		return cancellationCreationReason;
	}

	public void setCancellationDateCollected(java.util.Date cancellationDateCollected) {
		this.cancellationDateCollected = cancellationDateCollected;
	}

	public Contract withCancellationDateCollected(java.util.Date cancellationDateCollected) {
		this.cancellationDateCollected = cancellationDateCollected;
		return this;
	}

	public java.util.Date getCancellationDateCollected() {
		return cancellationDateCollected;
	}

	public void setCancellationCalculateFromSignatureDate(Boolean cancellationCalculateFromSignatureDate) {
		this.cancellationCalculateFromSignatureDate = cancellationCalculateFromSignatureDate;
	}

	public Contract withCancellationCalculateFromSignatureDate(Boolean cancellationCalculateFromSignatureDate) {
		this.cancellationCalculateFromSignatureDate = cancellationCalculateFromSignatureDate;
		return this;
	}

	public Boolean getCancellationCalculateFromSignatureDate() {
		return cancellationCalculateFromSignatureDate;
	}

	public void setChangeDate(java.util.Date changeDate) {
		this.changeDate = changeDate;
	}

	public Contract withChangeDate(java.util.Date changeDate) {
		this.changeDate = changeDate;
		return this;
	}

	public java.util.Date getChangeDate() {
		return changeDate;
	}

	public void setConsumptionHTCustomerDeclaration(java.math.BigDecimal consumptionHTCustomerDeclaration) {
		this.consumptionHTCustomerDeclaration = consumptionHTCustomerDeclaration;
	}

	public Contract withConsumptionHTCustomerDeclaration(java.math.BigDecimal consumptionHTCustomerDeclaration) {
		this.consumptionHTCustomerDeclaration = consumptionHTCustomerDeclaration;
		return this;
	}

	public java.math.BigDecimal getConsumptionHTCustomerDeclaration() {
		return consumptionHTCustomerDeclaration;
	}

	public void setVoltageLevelCustomerDeclaration(String voltageLevelCustomerDeclaration) {
		this.voltageLevelCustomerDeclaration = voltageLevelCustomerDeclaration;
	}

	public Contract withVoltageLevelCustomerDeclaration(String voltageLevelCustomerDeclaration) {
		this.voltageLevelCustomerDeclaration = voltageLevelCustomerDeclaration;
		return this;
	}

	public String getVoltageLevelCustomerDeclaration() {
		return voltageLevelCustomerDeclaration;
	}

	public void setCustomerDeclarationDate(java.util.Date customerDeclarationDate) {
		this.customerDeclarationDate = customerDeclarationDate;
	}

	public Contract withCustomerDeclarationDate(java.util.Date customerDeclarationDate) {
		this.customerDeclarationDate = customerDeclarationDate;
		return this;
	}

	public java.util.Date getCustomerDeclarationDate() {
		return customerDeclarationDate;
	}

	public void setSupplierOldCancelledDate(java.util.Date supplierOldCancelledDate) {
		this.supplierOldCancelledDate = supplierOldCancelledDate;
	}

	public Contract withSupplierOldCancelledDate(java.util.Date supplierOldCancelledDate) {
		this.supplierOldCancelledDate = supplierOldCancelledDate;
		return this;
	}

	public java.util.Date getSupplierOldCancelledDate() {
		return supplierOldCancelledDate;
	}

	public void setLastYearConsumptionSupplierOld(java.math.BigDecimal lastYearConsumptionSupplierOld) {
		this.lastYearConsumptionSupplierOld = lastYearConsumptionSupplierOld;
	}

	public Contract withLastYearConsumptionSupplierOld(java.math.BigDecimal lastYearConsumptionSupplierOld) {
		this.lastYearConsumptionSupplierOld = lastYearConsumptionSupplierOld;
		return this;
	}

	public java.math.BigDecimal getLastYearConsumptionSupplierOld() {
		return lastYearConsumptionSupplierOld;
	}

	public void setWelcomeMailSent(Boolean welcomeMailSent) {
		this.welcomeMailSent = welcomeMailSent;
	}

	public Contract withWelcomeMailSent(Boolean welcomeMailSent) {
		this.welcomeMailSent = welcomeMailSent;
		return this;
	}

	public Boolean getWelcomeMailSent() {
		return welcomeMailSent;
	}

	public void setLastInventoryListMonth(Integer lastInventoryListMonth) {
		this.lastInventoryListMonth = lastInventoryListMonth;
	}

	public Contract withLastInventoryListMonth(Integer lastInventoryListMonth) {
		this.lastInventoryListMonth = lastInventoryListMonth;
		return this;
	}

	public Integer getLastInventoryListMonth() {
		return lastInventoryListMonth;
	}

	public void setInvoicingInterval(Integer invoicingInterval) {
		this.invoicingInterval = invoicingInterval;
	}

	public Contract withInvoicingInterval(Integer invoicingInterval) {
		this.invoicingInterval = invoicingInterval;
		return this;
	}

	public Integer getInvoicingInterval() {
		return invoicingInterval;
	}

	public void setInvoicingInformationInterval(String invoicingInformationInterval) {
		this.invoicingInformationInterval = invoicingInformationInterval;
	}

	public Contract withInvoicingInformationInterval(String invoicingInformationInterval) {
		this.invoicingInformationInterval = invoicingInformationInterval;
		return this;
	}

	public String getInvoicingInformationInterval() {
		return invoicingInformationInterval;
	}

	public void setContractPartners(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner> contractPartners) {
		this.contractPartners = contractPartners;
	}

	public Contract withContractPartners(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner> contractPartners) {
		this.contractPartners = contractPartners;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner> getContractPartners() {
		return contractPartners;
	}

	public Contract addContractPartners(com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner value) {
		this.contractPartners.add(value);
		return this;
	}

	public void setTariffApplications(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication> tariffApplications) {
		this.tariffApplications = tariffApplications;
	}

	public Contract withTariffApplications(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication> tariffApplications) {
		this.tariffApplications = tariffApplications;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication> getTariffApplications() {
		return tariffApplications;
	}

	public Contract addTariffApplications(com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication value) {
		this.tariffApplications.add(value);
		return this;
	}

	public void setAdvancePayPlans(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan> advancePayPlans) {
		this.advancePayPlans = advancePayPlans;
	}

	public Contract withAdvancePayPlans(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan> advancePayPlans) {
		this.advancePayPlans = advancePayPlans;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan> getAdvancePayPlans() {
		return advancePayPlans;
	}

	public Contract addAdvancePayPlans(com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan value) {
		this.advancePayPlans.add(value);
		return this;
	}

	public void setEdiMessages(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage> ediMessages) {
		this.ediMessages = ediMessages;
	}

	public Contract withEdiMessages(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage> ediMessages) {
		this.ediMessages = ediMessages;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage> getEdiMessages() {
		return ediMessages;
	}

	public Contract addEdiMessages(com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage value) {
		this.ediMessages.add(value);
		return this;
	}

	public void setAepMakoMessages(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage> aepMakoMessages) {
		this.aepMakoMessages = aepMakoMessages;
	}

	public Contract withAepMakoMessages(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage> aepMakoMessages) {
		this.aepMakoMessages = aepMakoMessages;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage> getAepMakoMessages() {
		return aepMakoMessages;
	}

	public Contract addAepMakoMessages(com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage value) {
		this.aepMakoMessages.add(value);
		return this;
	}

	public void setCreditorInvoice(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice> creditorInvoice) {
		this.creditorInvoice = creditorInvoice;
	}

	public Contract withCreditorInvoice(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice> creditorInvoice) {
		this.creditorInvoice = creditorInvoice;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice> getCreditorInvoice() {
		return creditorInvoice;
	}

	public Contract addCreditorInvoice(com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice value) {
		this.creditorInvoice.add(value);
		return this;
	}

	public void setDebitorInvoice(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice> debitorInvoice) {
		this.debitorInvoice = debitorInvoice;
	}

	public Contract withDebitorInvoice(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice> debitorInvoice) {
		this.debitorInvoice = debitorInvoice;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice> getDebitorInvoice() {
		return debitorInvoice;
	}

	public Contract addDebitorInvoice(com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice value) {
		this.debitorInvoice.add(value);
		return this;
	}

	public void setInvoicingIntervals(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval> invoicingIntervals) {
		this.invoicingIntervals = invoicingIntervals;
	}

	public Contract withInvoicingIntervals(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval> invoicingIntervals) {
		this.invoicingIntervals = invoicingIntervals;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval> getInvoicingIntervals() {
		return invoicingIntervals;
	}

	public Contract addInvoicingIntervals(com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval value) {
		this.invoicingIntervals.add(value);
		return this;
	}

	public void setPowerConsumptionPrognosis(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis> powerConsumptionPrognosis) {
		this.powerConsumptionPrognosis = powerConsumptionPrognosis;
	}

	public Contract withPowerConsumptionPrognosis(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis> powerConsumptionPrognosis) {
		this.powerConsumptionPrognosis = powerConsumptionPrognosis;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis> getPowerConsumptionPrognosis() {
		return powerConsumptionPrognosis;
	}

	public Contract addPowerConsumptionPrognosis(com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis value) {
		this.powerConsumptionPrognosis.add(value);
		return this;
	}

	public void setContractTypeConfiguration(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration> contractTypeConfiguration) {
		this.contractTypeConfiguration = contractTypeConfiguration;
	}

	public Contract withContractTypeConfiguration(java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration> contractTypeConfiguration) {
		this.contractTypeConfiguration = contractTypeConfiguration;
		return this;
	}

	public java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration> getContractTypeConfiguration() {
		return contractTypeConfiguration;
	}

	public Contract addContractTypeConfiguration(com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration value) {
		this.contractTypeConfiguration.add(value);
		return this;
	}

	public void setNextInvoiceDate(java.util.Date nextInvoiceDate) {
		this.nextInvoiceDate = nextInvoiceDate;
	}

	public Contract withNextInvoiceDate(java.util.Date nextInvoiceDate) {
		this.nextInvoiceDate = nextInvoiceDate;
		return this;
	}

	public java.util.Date getNextInvoiceDate() {
		return nextInvoiceDate;
	}

	public void setOldSupplyId(Integer oldSupplyId) {
		this.oldSupplyId = oldSupplyId;
	}

	public Contract withOldSupplyId(Integer oldSupplyId) {
		this.oldSupplyId = oldSupplyId;
		return this;
	}

	public Integer getOldSupplyId() {
		return oldSupplyId;
	}

	public void setLockReason(com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason lockReason) {
		this.lockReason = lockReason;
	}

	public Contract withLockReason(com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason lockReason) {
		this.lockReason = lockReason;
		return this;
	}

	public com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason getLockReason() {
		return lockReason;
	}

	public void setResellerId(String resellerId) {
		this.resellerId = resellerId;
	}

	public Contract withResellerId(String resellerId) {
		this.resellerId = resellerId;
		return this;
	}

	public String getResellerId() {
		return resellerId;
	}

	public void setCustomerSignatureDate(java.util.Date customerSignatureDate) {
		this.customerSignatureDate = customerSignatureDate;
	}

	public Contract withCustomerSignatureDate(java.util.Date customerSignatureDate) {
		this.customerSignatureDate = customerSignatureDate;
		return this;
	}

	public java.util.Date getCustomerSignatureDate() {
		return customerSignatureDate;
	}

	public void setLockDate(java.util.Date lockDate) {
		this.lockDate = lockDate;
	}

	public Contract withLockDate(java.util.Date lockDate) {
		this.lockDate = lockDate;
		return this;
	}

	public java.util.Date getLockDate() {
		return lockDate;
	}

	public void setStateAddition(String stateAddition) {
		this.stateAddition = stateAddition;
	}

	public Contract withStateAddition(String stateAddition) {
		this.stateAddition = stateAddition;
		return this;
	}

	public String getStateAddition() {
		return stateAddition;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Contract withType(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setContractAdditionalId(Long contractAdditionalId) {
		this.contractAdditionalId = contractAdditionalId;
	}

	public Contract withContractAdditionalId(Long contractAdditionalId) {
		this.contractAdditionalId = contractAdditionalId;
		return this;
	}

	public Long getContractAdditionalId() {
		return contractAdditionalId;
	}

	public void setVirtualIban(String virtualIban) {
		this.virtualIban = virtualIban;
	}

	public Contract withVirtualIban(String virtualIban) {
		this.virtualIban = virtualIban;
		return this;
	}

	public String getVirtualIban() {
		return virtualIban;
	}

	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}

	public Contract withCommunicationType(String communicationType) {
		this.communicationType = communicationType;
		return this;
	}

	public String getCommunicationType() {
		return communicationType;
	}

	public void setExtraordinaryCancellationDate(java.util.Date extraordinaryCancellationDate) {
		this.extraordinaryCancellationDate = extraordinaryCancellationDate;
	}

	public Contract withExtraordinaryCancellationDate(java.util.Date extraordinaryCancellationDate) {
		this.extraordinaryCancellationDate = extraordinaryCancellationDate;
		return this;
	}

	public java.util.Date getExtraordinaryCancellationDate() {
		return extraordinaryCancellationDate;
	}

	public void setAccountingCharacteristic(AccountingCharacteristic accountingCharacteristic) {
		this.accountingCharacteristic = accountingCharacteristic;
	}

	public Contract withAccountingCharacteristic(AccountingCharacteristic accountingCharacteristic) {
		this.accountingCharacteristic = accountingCharacteristic;
		return this;
	}

	public AccountingCharacteristic getAccountingCharacteristic() {
		return accountingCharacteristic;
	}

	public void setBusinessSector(com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector businessSector) {
		this.businessSector = businessSector;
	}

	public Contract withBusinessSector(com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector businessSector) {
		this.businessSector = businessSector;
		return this;
	}

	public com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector getBusinessSector() {
		return businessSector;
	}

	public void setInvoiceTrigger(com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger invoiceTrigger) {
		this.invoiceTrigger = invoiceTrigger;
	}

	public Contract withInvoiceTrigger(com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger invoiceTrigger) {
		this.invoiceTrigger = invoiceTrigger;
		return this;
	}

	public com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger getInvoiceTrigger() {
		return invoiceTrigger;
	}

	public void setSelfReadingType(String selfReadingType) {
		this.selfReadingType = selfReadingType;
	}

	public Contract withSelfReadingType(String selfReadingType) {
		this.selfReadingType = selfReadingType;
		return this;
	}

	public String getSelfReadingType() {
		return selfReadingType;
	}

	public void setRatingScore(java.math.BigDecimal ratingScore) {
		this.ratingScore = ratingScore;
	}

	public Contract withRatingScore(java.math.BigDecimal ratingScore) {
		this.ratingScore = ratingScore;
		return this;
	}

	public java.math.BigDecimal getRatingScore() {
		return ratingScore;
	}

	public void setMsbCycleReadingStart(java.util.Date msbCycleReadingStart) {
		this.msbCycleReadingStart = msbCycleReadingStart;
	}

	public Contract withMsbCycleReadingStart(java.util.Date msbCycleReadingStart) {
		this.msbCycleReadingStart = msbCycleReadingStart;
		return this;
	}

	public java.util.Date getMsbCycleReadingStart() {
		return msbCycleReadingStart;
	}

	public void setMsbCycleReadingEnd(java.util.Date msbCycleReadingEnd) {
		this.msbCycleReadingEnd = msbCycleReadingEnd;
	}

	public Contract withMsbCycleReadingEnd(java.util.Date msbCycleReadingEnd) {
		this.msbCycleReadingEnd = msbCycleReadingEnd;
		return this;
	}

	public java.util.Date getMsbCycleReadingEnd() {
		return msbCycleReadingEnd;
	}

	public void setSuspendAccountingCharacteristicCheck(Boolean suspendAccountingCharacteristicCheck) {
		this.suspendAccountingCharacteristicCheck = suspendAccountingCharacteristicCheck;
	}

	public Contract withSuspendAccountingCharacteristicCheck(Boolean suspendAccountingCharacteristicCheck) {
		this.suspendAccountingCharacteristicCheck = suspendAccountingCharacteristicCheck;
		return this;
	}

	public Boolean getSuspendAccountingCharacteristicCheck() {
		return suspendAccountingCharacteristicCheck;
	}

	public void setBoniPruefIdentNr(String boniPruefIdentNr) {
		this.boniPruefIdentNr = boniPruefIdentNr;
	}

	public Contract withBoniPruefIdentNr(String boniPruefIdentNr) {
		this.boniPruefIdentNr = boniPruefIdentNr;
		return this;
	}

	public String getBoniPruefIdentNr() {
		return boniPruefIdentNr;
	}

	public static final int _maxLengthContractAccount = 255;
	public static final int _minLengthContractAccount = 0;

	public Contract setContractAccount(Long contractAccount) {
		this.contractAccount = contractAccount;
		return this;
	}

	public Contract withContractAccount(Long contractAccount) {
		this.contractAccount = contractAccount;
		return this;
	}

	public Long getContractAccount() {
		return contractAccount;
	}

	public static final int _maxLengthState = 255;
	public static final int _minLengthState = 0;

	public Contract setState(String state) {
		this.state = state;
		return this;
	}

	public Contract withState(String state) {
		this.state = state;
		return this;
	}

	public String getState() {
		return state;
	}

	public static final int _maxLengthPointOfDelivery = 255;
	public static final int _minLengthPointOfDelivery = 0;

	public Contract setPointOfDelivery(Long pointOfDelivery) {
		this.pointOfDelivery = pointOfDelivery;
		return this;
	}

	public Contract withPointOfDelivery(Long pointOfDelivery) {
		this.pointOfDelivery = pointOfDelivery;
		return this;
	}

	public Long getPointOfDelivery() {
		return pointOfDelivery;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			Object o = null;

			id = ExternalizableUtil.parseLong(in.readObject());

			oldId = ExternalizableUtil.parseInteger(in.readObject());

			section = (String) in.readObject();

			contractNr = (String) in.readObject();

			inBoxDate = (java.util.Date) in.readObject();

			contractConclusionDate = (java.util.Date) in.readObject();

			desiredAdvancePayEveryNMonth = ExternalizableUtil.parseInteger(in.readObject());

			useNextPossibleCancellationDate = (Boolean) in.readObject();

			cancellationDateTo = (java.util.Date) in.readObject();

			cancellationCreationReason = (String) in.readObject();

			cancellationDateCollected = (java.util.Date) in.readObject();

			cancellationCalculateFromSignatureDate = (Boolean) in.readObject();

			changeDate = (java.util.Date) in.readObject();

			final Object consumptionHTCustomerDeclarationTemp = in.readObject();
			consumptionHTCustomerDeclaration = (consumptionHTCustomerDeclarationTemp != null && !"".equals(consumptionHTCustomerDeclarationTemp)) ? new java.math.BigDecimal(consumptionHTCustomerDeclarationTemp.toString()) : null;

			voltageLevelCustomerDeclaration = (String) in.readObject();

			customerDeclarationDate = (java.util.Date) in.readObject();

			supplierOldCancelledDate = (java.util.Date) in.readObject();

			final Object lastYearConsumptionSupplierOldTemp = in.readObject();
			lastYearConsumptionSupplierOld = (lastYearConsumptionSupplierOldTemp != null && !"".equals(lastYearConsumptionSupplierOldTemp)) ? new java.math.BigDecimal(lastYearConsumptionSupplierOldTemp.toString()) : null;

			welcomeMailSent = (Boolean) in.readObject();

			lastInventoryListMonth = ExternalizableUtil.parseInteger(in.readObject());

			invoicingInterval = ExternalizableUtil.parseInteger(in.readObject());

			invoicingInformationInterval = (String) in.readObject();

			contractPartners = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner>) in.readObject();

			tariffApplications = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication>) in.readObject();

			advancePayPlans = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan>) in.readObject();

			ediMessages = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage>) in.readObject();

			aepMakoMessages = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage>) in.readObject();

			creditorInvoice = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice>) in.readObject();

			debitorInvoice = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice>) in.readObject();

			invoicingIntervals = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval>) in.readObject();

			powerConsumptionPrognosis = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis>) in.readObject();

			contractTypeConfiguration = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration>) in.readObject();

			nextInvoiceDate = (java.util.Date) in.readObject();

			oldSupplyId = ExternalizableUtil.parseInteger(in.readObject());

			lockReason = (com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason) in.readObject();

			resellerId = (String) in.readObject();

			customerSignatureDate = (java.util.Date) in.readObject();

			lockDate = (java.util.Date) in.readObject();

			stateAddition = (String) in.readObject();

			type = (String) in.readObject();

			contractAdditionalId = ExternalizableUtil.parseLong(in.readObject());

			virtualIban = (String) in.readObject();

			communicationType = (String) in.readObject();

			extraordinaryCancellationDate = (java.util.Date) in.readObject();

			accountingCharacteristic = (AccountingCharacteristic) in.readObject();

			businessSector = (com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector) in.readObject();

			invoiceTrigger = (com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger) in.readObject();

			selfReadingType = (String) in.readObject();

			final Object ratingScoreTemp = in.readObject();
			ratingScore = (ratingScoreTemp != null && !"".equals(ratingScoreTemp)) ? new java.math.BigDecimal(ratingScoreTemp.toString()) : null;

			msbCycleReadingStart = (java.util.Date) in.readObject();

			msbCycleReadingEnd = (java.util.Date) in.readObject();

			suspendAccountingCharacteristicCheck = (Boolean) in.readObject();

			boniPruefIdentNr = (String) in.readObject();

			super.readExternal(in);

			// foreign Keys

			this.contractAccount = ExternalizableUtil.parseLong(in.readObject());

			this.state = (String) in.readObject();

			this.pointOfDelivery = ExternalizableUtil.parseLong(in.readObject());

		} catch (java.io.OptionalDataException e) {
			//ignore Exception
		} catch (java.io.EOFException e) {
			//ignore Exception
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(ExternalizableUtil.checkNumericNullValue(id));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(oldId));

		out.writeObject(section);

		out.writeObject(contractNr);

		out.writeObject(inBoxDate);

		out.writeObject(contractConclusionDate);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(desiredAdvancePayEveryNMonth));

		out.writeObject(useNextPossibleCancellationDate);

		out.writeObject(cancellationDateTo);

		out.writeObject(cancellationCreationReason);

		out.writeObject(cancellationDateCollected);

		out.writeObject(cancellationCalculateFromSignatureDate);

		out.writeObject(changeDate);

		out.writeObject(consumptionHTCustomerDeclaration != null ? consumptionHTCustomerDeclaration.toPlainString() : null);

		out.writeObject(voltageLevelCustomerDeclaration);

		out.writeObject(customerDeclarationDate);

		out.writeObject(supplierOldCancelledDate);

		out.writeObject(lastYearConsumptionSupplierOld != null ? lastYearConsumptionSupplierOld.toPlainString() : null);

		out.writeObject(welcomeMailSent);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(lastInventoryListMonth));

		out.writeObject(ExternalizableUtil.checkNumericNullValue(invoicingInterval));

		out.writeObject(invoicingInformationInterval);

		out.writeObject(contractPartners);

		out.writeObject(tariffApplications);

		out.writeObject(advancePayPlans);

		out.writeObject(ediMessages);

		out.writeObject(aepMakoMessages);

		out.writeObject(creditorInvoice);

		out.writeObject(debitorInvoice);

		out.writeObject(invoicingIntervals);

		out.writeObject(powerConsumptionPrognosis);

		out.writeObject(contractTypeConfiguration);

		out.writeObject(nextInvoiceDate);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(oldSupplyId));

		out.writeObject(lockReason);

		out.writeObject(resellerId);

		out.writeObject(customerSignatureDate);

		out.writeObject(lockDate);

		out.writeObject(stateAddition);

		out.writeObject(type);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(contractAdditionalId));

		out.writeObject(virtualIban);

		out.writeObject(communicationType);

		out.writeObject(extraordinaryCancellationDate);

		out.writeObject(accountingCharacteristic);

		out.writeObject(businessSector);

		out.writeObject(invoiceTrigger);

		out.writeObject(selfReadingType);

		out.writeObject(ratingScore != null ? ratingScore.toPlainString() : null);

		out.writeObject(msbCycleReadingStart);

		out.writeObject(msbCycleReadingEnd);

		out.writeObject(suspendAccountingCharacteristicCheck);

		out.writeObject(boniPruefIdentNr);

		super.writeExternal(out);

		// foreign Keys

		out.writeObject(ExternalizableUtil.checkNumericNullValue(contractAccount));

		out.writeObject(state);

		out.writeObject(ExternalizableUtil.checkNumericNullValue(pointOfDelivery));

	}

	public String toString() {
		String res = "";

		res += "id: " + id + "\n";
		res += "oldId: " + oldId + "\n";
		res += "section: " + section + "\n";
		res += "contractNr: " + contractNr + "\n";
		res += "inBoxDate: " + inBoxDate + "\n";
		res += "contractConclusionDate: " + contractConclusionDate + "\n";
		res += "desiredAdvancePayEveryNMonth: " + desiredAdvancePayEveryNMonth + "\n";
		res += "useNextPossibleCancellationDate: " + useNextPossibleCancellationDate + "\n";
		res += "cancellationDateTo: " + cancellationDateTo + "\n";
		res += "cancellationCreationReason: " + cancellationCreationReason + "\n";
		res += "cancellationDateCollected: " + cancellationDateCollected + "\n";
		res += "cancellationCalculateFromSignatureDate: " + cancellationCalculateFromSignatureDate + "\n";
		res += "changeDate: " + changeDate + "\n";
		res += "consumptionHTCustomerDeclaration: " + consumptionHTCustomerDeclaration + "\n";
		res += "voltageLevelCustomerDeclaration: " + voltageLevelCustomerDeclaration + "\n";
		res += "customerDeclarationDate: " + customerDeclarationDate + "\n";
		res += "supplierOldCancelledDate: " + supplierOldCancelledDate + "\n";
		res += "lastYearConsumptionSupplierOld: " + lastYearConsumptionSupplierOld + "\n";
		res += "welcomeMailSent: " + welcomeMailSent + "\n";
		res += "lastInventoryListMonth: " + lastInventoryListMonth + "\n";
		res += "invoicingInterval: " + invoicingInterval + "\n";
		res += "invoicingInformationInterval: " + invoicingInformationInterval + "\n";
		res += "contractPartners: " + contractPartners + "\n";
		res += "tariffApplications: " + tariffApplications + "\n";
		res += "advancePayPlans: " + advancePayPlans + "\n";
		res += "ediMessages: " + ediMessages + "\n";
		res += "aepMakoMessages: " + aepMakoMessages + "\n";
		res += "creditorInvoice: " + creditorInvoice + "\n";
		res += "debitorInvoice: " + debitorInvoice + "\n";
		res += "invoicingIntervals: " + invoicingIntervals + "\n";
		res += "powerConsumptionPrognosis: " + powerConsumptionPrognosis + "\n";
		res += "contractTypeConfiguration: " + contractTypeConfiguration + "\n";
		res += "nextInvoiceDate: " + nextInvoiceDate + "\n";
		res += "oldSupplyId: " + oldSupplyId + "\n";
		res += "lockReason: " + lockReason + "\n";
		res += "resellerId: " + resellerId + "\n";
		res += "customerSignatureDate: " + customerSignatureDate + "\n";
		res += "lockDate: " + lockDate + "\n";
		res += "stateAddition: " + stateAddition + "\n";
		res += "type: " + type + "\n";
		res += "contractAdditionalId: " + contractAdditionalId + "\n";
		res += "virtualIban: " + virtualIban + "\n";
		res += "communicationType: " + communicationType + "\n";
		res += "extraordinaryCancellationDate: " + extraordinaryCancellationDate + "\n";
		res += "accountingCharacteristic: " + accountingCharacteristic + "\n";
		res += "businessSector: " + businessSector + "\n";
		res += "invoiceTrigger: " + invoiceTrigger + "\n";
		res += "selfReadingType: " + selfReadingType + "\n";
		res += "ratingScore: " + ratingScore + "\n";
		res += "msbCycleReadingStart: " + msbCycleReadingStart + "\n";
		res += "msbCycleReadingEnd: " + msbCycleReadingEnd + "\n";
		res += "suspendAccountingCharacteristicCheck: " + suspendAccountingCharacteristicCheck + "\n";
		res += "boniPruefIdentNr: " + boniPruefIdentNr + "\n";

		res += "contractAccount: " + contractAccount + "\n";

		res += "state: " + state + "\n";

		res += "pointOfDelivery: " + pointOfDelivery + "\n";

		res += "periodStart: " + periodStart + "\n";
		res += "periodEnd: " + periodEnd + "\n";

		return res;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Contract) || id == null)
			return false;

		Contract contract = (Contract) obj;

		if (this.id.equals(contract.id))
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

		if (oldId != null) {
			res.append(oldId + ",");
		} else
			res.append(",");

		if (section != null) {
			res.append(section + ",");
		} else
			res.append(",");

		if (contractNr != null) {
			res.append(contractNr + ",");
		} else
			res.append(",");

		if (inBoxDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(inBoxDate) + ",");
		} else
			res.append(",");

		if (contractConclusionDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(contractConclusionDate) + ",");
		} else
			res.append(",");

		if (desiredAdvancePayEveryNMonth != null) {
			res.append(desiredAdvancePayEveryNMonth + ",");
		} else
			res.append(",");

		if (useNextPossibleCancellationDate != null) {
			res.append(useNextPossibleCancellationDate + ",");
		} else
			res.append(",");

		if (cancellationDateTo != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(cancellationDateTo) + ",");
		} else
			res.append(",");

		if (cancellationCreationReason != null) {
			res.append(cancellationCreationReason + ",");
		} else
			res.append(",");

		if (cancellationDateCollected != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(cancellationDateCollected) + ",");
		} else
			res.append(",");

		if (cancellationCalculateFromSignatureDate != null) {
			res.append(cancellationCalculateFromSignatureDate + ",");
		} else
			res.append(",");

		if (changeDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(changeDate) + ",");
		} else
			res.append(",");

		if (consumptionHTCustomerDeclaration != null) {
			res.append(consumptionHTCustomerDeclaration.toPlainString() + ",");
		} else
			res.append(",");

		if (voltageLevelCustomerDeclaration != null) {
			res.append(voltageLevelCustomerDeclaration + ",");
		} else
			res.append(",");

		if (customerDeclarationDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(customerDeclarationDate) + ",");
		} else
			res.append(",");

		if (supplierOldCancelledDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(supplierOldCancelledDate) + ",");
		} else
			res.append(",");

		if (lastYearConsumptionSupplierOld != null) {
			res.append(lastYearConsumptionSupplierOld.toPlainString() + ",");
		} else
			res.append(",");

		if (welcomeMailSent != null) {
			res.append(welcomeMailSent + ",");
		} else
			res.append(",");

		if (lastInventoryListMonth != null) {
			res.append(lastInventoryListMonth + ",");
		} else
			res.append(",");

		if (invoicingInterval != null) {
			res.append(invoicingInterval + ",");
		} else
			res.append(",");

		if (invoicingInformationInterval != null) {
			res.append(invoicingInformationInterval + ",");
		} else
			res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner> contractPartnersList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner>) contractPartners;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner entry : contractPartnersList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (contractPartnersList != null && contractPartnersList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication> tariffApplicationsList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication>) tariffApplications;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication entry : tariffApplicationsList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (tariffApplicationsList != null && tariffApplicationsList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan> advancePayPlansList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan>) advancePayPlans;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan entry : advancePayPlansList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (advancePayPlansList != null && advancePayPlansList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage> ediMessagesList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage>) ediMessages;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage entry : ediMessagesList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (ediMessagesList != null && ediMessagesList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage> aepMakoMessagesList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage>) aepMakoMessages;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage entry : aepMakoMessagesList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (aepMakoMessagesList != null && aepMakoMessagesList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice> creditorInvoiceList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice>) creditorInvoice;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice entry : creditorInvoiceList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (creditorInvoiceList != null && creditorInvoiceList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice> debitorInvoiceList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice>) debitorInvoice;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice entry : debitorInvoiceList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (debitorInvoiceList != null && debitorInvoiceList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval> invoicingIntervalsList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval>) invoicingIntervals;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval entry : invoicingIntervalsList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (invoicingIntervalsList != null && invoicingIntervalsList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis> powerConsumptionPrognosisList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis>) powerConsumptionPrognosis;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis entry : powerConsumptionPrognosisList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (powerConsumptionPrognosisList != null && powerConsumptionPrognosisList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		res.append("[");

		java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration> contractTypeConfigurationList = (java.util.List<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration>) contractTypeConfiguration;
		for (com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration entry : contractTypeConfigurationList) {
			if (entry != null)
				res.append(entry.serializeToString() + ",");
			else
				res.append(",");
		}
		if (contractTypeConfigurationList != null && contractTypeConfigurationList.size() > 0)
			res.setCharAt(res.length() - 1, ']');
		else
			res.append(']');
		res.append(",");

		if (nextInvoiceDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(nextInvoiceDate) + ",");
		} else
			res.append(",");

		if (oldSupplyId != null) {
			res.append(oldSupplyId + ",");
		} else
			res.append(",");

		if (lockReason != null)
			res.append(lockReason.serializeToString() + ",");
		else
			res.append(new com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason().serializeToString() + ",");

		if (resellerId != null) {
			res.append(resellerId + ",");
		} else
			res.append(",");

		if (customerSignatureDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(customerSignatureDate) + ",");
		} else
			res.append(",");

		if (lockDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(lockDate) + ",");
		} else
			res.append(",");

		if (stateAddition != null) {
			res.append(stateAddition + ",");
		} else
			res.append(",");

		if (type != null) {
			res.append(type + ",");
		} else
			res.append(",");

		if (contractAdditionalId != null) {
			res.append(contractAdditionalId + ",");
		} else
			res.append(",");

		if (virtualIban != null) {
			res.append(virtualIban + ",");
		} else
			res.append(",");

		if (communicationType != null) {
			res.append(communicationType + ",");
		} else
			res.append(",");

		if (extraordinaryCancellationDate != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(extraordinaryCancellationDate) + ",");
		} else
			res.append(",");

		if (accountingCharacteristic != null)
			res.append(accountingCharacteristic.serializeToString() + ",");
		else
			res.append(new AccountingCharacteristic().serializeToString() + ",");

		if (businessSector != null)
			res.append(businessSector.serializeToString() + ",");
		else
			res.append(new com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector().serializeToString() + ",");

		if (invoiceTrigger != null)
			res.append(invoiceTrigger.serializeToString() + ",");
		else
			res.append(new com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger().serializeToString() + ",");

		if (selfReadingType != null) {
			res.append(selfReadingType + ",");
		} else
			res.append(",");

		if (ratingScore != null) {
			res.append(ratingScore.toPlainString() + ",");
		} else
			res.append(",");

		if (msbCycleReadingStart != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(msbCycleReadingStart) + ",");
		} else
			res.append(",");

		if (msbCycleReadingEnd != null) {
			res.append(new SimpleDateFormat(_dateFormatString_).format(msbCycleReadingEnd) + ",");
		} else
			res.append(",");

		if (suspendAccountingCharacteristicCheck != null) {
			res.append(suspendAccountingCharacteristicCheck + ",");
		} else
			res.append(",");

		if (boniPruefIdentNr != null) {
			res.append(boniPruefIdentNr + ",");
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

		//parsing oldId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.oldId = Integer.valueOf(actualData);

		//parsing section
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.section = actualData; //String

		//parsing contractNr
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.contractNr = actualData; //String

		//parsing inBoxDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.inBoxDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing contractConclusionDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.contractConclusionDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing desiredAdvancePayEveryNMonth
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.desiredAdvancePayEveryNMonth = Integer.valueOf(actualData);

		//parsing useNextPossibleCancellationDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.useNextPossibleCancellationDate = Boolean.valueOf(actualData);

		//parsing cancellationDateTo
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.cancellationDateTo = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing cancellationCreationReason
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.cancellationCreationReason = actualData; //String

		//parsing cancellationDateCollected
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.cancellationDateCollected = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing cancellationCalculateFromSignatureDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.cancellationCalculateFromSignatureDate = Boolean.valueOf(actualData);

		//parsing changeDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.changeDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing consumptionHTCustomerDeclaration
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.consumptionHTCustomerDeclaration = new java.math.BigDecimal(actualData);

		//parsing voltageLevelCustomerDeclaration
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.voltageLevelCustomerDeclaration = actualData; //String

		//parsing customerDeclarationDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.customerDeclarationDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing supplierOldCancelledDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.supplierOldCancelledDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing lastYearConsumptionSupplierOld
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.lastYearConsumptionSupplierOld = new java.math.BigDecimal(actualData);

		//parsing welcomeMailSent
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.welcomeMailSent = Boolean.valueOf(actualData);

		//parsing lastInventoryListMonth
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.lastInventoryListMonth = Integer.valueOf(actualData);

		//parsing invoicingInterval
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.invoicingInterval = Integer.valueOf(actualData);

		//parsing invoicingInformationInterval
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.invoicingInformationInterval = actualData; //String

		//parsing contractPartners
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.contractPartners = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner>();
		int pos2contractPartners = 1;
		int end2contractPartners = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2contractPartners < actualData.length()) {
				String actualData2 = null;
				end2contractPartners = findEndOfSegment(pos2contractPartners, actualData);
				actualData2 = actualData.substring(pos2contractPartners, end2contractPartners);

				com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.ContractPartner();
				obj.deserializeFromString(actualData2);
				contractPartners.add(obj);
				pos2contractPartners = end2contractPartners + 1;
			}

		//parsing tariffApplications
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.tariffApplications = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication>();
		int pos2tariffApplications = 1;
		int end2tariffApplications = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2tariffApplications < actualData.length()) {
				String actualData2 = null;
				end2tariffApplications = findEndOfSegment(pos2tariffApplications, actualData);
				actualData2 = actualData.substring(pos2tariffApplications, end2tariffApplications);

				com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.TariffApplication();
				obj.deserializeFromString(actualData2);
				tariffApplications.add(obj);
				pos2tariffApplications = end2tariffApplications + 1;
			}

		//parsing advancePayPlans
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.advancePayPlans = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan>();
		int pos2advancePayPlans = 1;
		int end2advancePayPlans = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2advancePayPlans < actualData.length()) {
				String actualData2 = null;
				end2advancePayPlans = findEndOfSegment(pos2advancePayPlans, actualData);
				actualData2 = actualData.substring(pos2advancePayPlans, end2advancePayPlans);

				com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.AdvancePayPlan();
				obj.deserializeFromString(actualData2);
				advancePayPlans.add(obj);
				pos2advancePayPlans = end2advancePayPlans + 1;
			}

		//parsing ediMessages
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.ediMessages = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage>();
		int pos2ediMessages = 1;
		int end2ediMessages = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2ediMessages < actualData.length()) {
				String actualData2 = null;
				end2ediMessages = findEndOfSegment(pos2ediMessages, actualData);
				actualData2 = actualData.substring(pos2ediMessages, end2ediMessages);

				com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.EdiMessage();
				obj.deserializeFromString(actualData2);
				ediMessages.add(obj);
				pos2ediMessages = end2ediMessages + 1;
			}

		//parsing aepMakoMessages
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.aepMakoMessages = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage>();
		int pos2aepMakoMessages = 1;
		int end2aepMakoMessages = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2aepMakoMessages < actualData.length()) {
				String actualData2 = null;
				end2aepMakoMessages = findEndOfSegment(pos2aepMakoMessages, actualData);
				actualData2 = actualData.substring(pos2aepMakoMessages, end2aepMakoMessages);

				com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.AepMakoMessage();
				obj.deserializeFromString(actualData2);
				aepMakoMessages.add(obj);
				pos2aepMakoMessages = end2aepMakoMessages + 1;
			}

		//parsing creditorInvoice
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.creditorInvoice = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice>();
		int pos2creditorInvoice = 1;
		int end2creditorInvoice = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2creditorInvoice < actualData.length()) {
				String actualData2 = null;
				end2creditorInvoice = findEndOfSegment(pos2creditorInvoice, actualData);
				actualData2 = actualData.substring(pos2creditorInvoice, end2creditorInvoice);

				com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.CreditorInvoice();
				obj.deserializeFromString(actualData2);
				creditorInvoice.add(obj);
				pos2creditorInvoice = end2creditorInvoice + 1;
			}

		//parsing debitorInvoice
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.debitorInvoice = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice>();
		int pos2debitorInvoice = 1;
		int end2debitorInvoice = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2debitorInvoice < actualData.length()) {
				String actualData2 = null;
				end2debitorInvoice = findEndOfSegment(pos2debitorInvoice, actualData);
				actualData2 = actualData.substring(pos2debitorInvoice, end2debitorInvoice);

				com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.DebitorInvoice();
				obj.deserializeFromString(actualData2);
				debitorInvoice.add(obj);
				pos2debitorInvoice = end2debitorInvoice + 1;
			}

		//parsing invoicingIntervals
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.invoicingIntervals = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval>();
		int pos2invoicingIntervals = 1;
		int end2invoicingIntervals = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2invoicingIntervals < actualData.length()) {
				String actualData2 = null;
				end2invoicingIntervals = findEndOfSegment(pos2invoicingIntervals, actualData);
				actualData2 = actualData.substring(pos2invoicingIntervals, end2invoicingIntervals);

				com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.InvoicingInterval();
				obj.deserializeFromString(actualData2);
				invoicingIntervals.add(obj);
				pos2invoicingIntervals = end2invoicingIntervals + 1;
			}

		//parsing powerConsumptionPrognosis
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.powerConsumptionPrognosis = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis>();
		int pos2powerConsumptionPrognosis = 1;
		int end2powerConsumptionPrognosis = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2powerConsumptionPrognosis < actualData.length()) {
				String actualData2 = null;
				end2powerConsumptionPrognosis = findEndOfSegment(pos2powerConsumptionPrognosis, actualData);
				actualData2 = actualData.substring(pos2powerConsumptionPrognosis, end2powerConsumptionPrognosis);

				com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.PowerConsumptionPrognosis();
				obj.deserializeFromString(actualData2);
				powerConsumptionPrognosis.add(obj);
				pos2powerConsumptionPrognosis = end2powerConsumptionPrognosis + 1;
			}

		//parsing contractTypeConfiguration
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		this.contractTypeConfiguration = new java.util.ArrayList<com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration>();
		int pos2contractTypeConfiguration = 1;
		int end2contractTypeConfiguration = 0;
		if (actualData.length() > 2)//its [] if empty
			while (pos2contractTypeConfiguration < actualData.length()) {
				String actualData2 = null;
				end2contractTypeConfiguration = findEndOfSegment(pos2contractTypeConfiguration, actualData);
				actualData2 = actualData.substring(pos2contractTypeConfiguration, end2contractTypeConfiguration);

				com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration obj = new com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration();
				obj.deserializeFromString(actualData2);
				contractTypeConfiguration.add(obj);
				pos2contractTypeConfiguration = end2contractTypeConfiguration + 1;
			}

		//parsing nextInvoiceDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.nextInvoiceDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing oldSupplyId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.oldSupplyId = Integer.valueOf(actualData);

		//parsing lockReason
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason lockReason = new com.nextlevel.fastlane.myBusinessSupplier.bo.ContractLockReason();
		lockReason.deserializeFromString(actualData);
		this.lockReason = lockReason;

		//parsing resellerId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.resellerId = actualData; //String

		//parsing customerSignatureDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.customerSignatureDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing lockDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.lockDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing stateAddition
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.stateAddition = actualData; //String

		//parsing type
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.type = actualData; //String

		//parsing contractAdditionalId
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.contractAdditionalId = Long.valueOf(actualData);

		//parsing virtualIban
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.virtualIban = actualData; //String

		//parsing communicationType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.communicationType = actualData; //String

		//parsing extraordinaryCancellationDate
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.extraordinaryCancellationDate = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing accountingCharacteristic
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		AccountingCharacteristic accountingCharacteristic = new AccountingCharacteristic();
		accountingCharacteristic.deserializeFromString(actualData);
		this.accountingCharacteristic = accountingCharacteristic;

		//parsing businessSector
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector businessSector = new com.nextlevel.fastlane.myBusinessSupplier.bo.BusinessSector();
		businessSector.deserializeFromString(actualData);
		this.businessSector = businessSector;

		//parsing invoiceTrigger
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger invoiceTrigger = new com.nextlevel.fastlane.myBusinessSupplier.bo.InvoiceTrigger();
		invoiceTrigger.deserializeFromString(actualData);
		this.invoiceTrigger = invoiceTrigger;

		//parsing selfReadingType
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.selfReadingType = actualData; //String

		//parsing ratingScore
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.ratingScore = new java.math.BigDecimal(actualData);

		//parsing msbCycleReadingStart
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.msbCycleReadingStart = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing msbCycleReadingEnd
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.msbCycleReadingEnd = new SimpleDateFormat(_dateFormatString_).parse(actualData);

		//parsing suspendAccountingCharacteristicCheck
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.suspendAccountingCharacteristicCheck = Boolean.valueOf(actualData);

		//parsing boniPruefIdentNr
		if (data.length() <= pos)
			return;
		end = findEndOfSegment(pos, data);
		actualData = data.substring(pos, end);
		pos = end + 1;

		if (actualData.length() > 0)
			this.boniPruefIdentNr = actualData; //String

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

	public Contract clone() {
		Contract contract = new Contract();

		contract.setOldId(this.getOldId());
		contract.setSection(this.getSection());
		contract.setContractNr(this.getContractNr());
		contract.setInBoxDate(this.getInBoxDate());
		contract.setContractConclusionDate(this.getContractConclusionDate());
		contract.setDesiredAdvancePayEveryNMonth(this.getDesiredAdvancePayEveryNMonth());
		contract.setUseNextPossibleCancellationDate(this.getUseNextPossibleCancellationDate());
		contract.setCancellationDateTo(this.getCancellationDateTo());
		contract.setCancellationCreationReason(this.getCancellationCreationReason());
		contract.setCancellationDateCollected(this.getCancellationDateCollected());
		contract.setCancellationCalculateFromSignatureDate(this.getCancellationCalculateFromSignatureDate());
		contract.setChangeDate(this.getChangeDate());
		contract.setConsumptionHTCustomerDeclaration(this.getConsumptionHTCustomerDeclaration());
		contract.setVoltageLevelCustomerDeclaration(this.getVoltageLevelCustomerDeclaration());
		contract.setCustomerDeclarationDate(this.getCustomerDeclarationDate());
		contract.setSupplierOldCancelledDate(this.getSupplierOldCancelledDate());
		contract.setLastYearConsumptionSupplierOld(this.getLastYearConsumptionSupplierOld());
		contract.setWelcomeMailSent(this.getWelcomeMailSent());
		contract.setLastInventoryListMonth(this.getLastInventoryListMonth());
		contract.setInvoicingInterval(this.getInvoicingInterval());
		contract.setInvoicingInformationInterval(this.getInvoicingInformationInterval());
		contract.setContractPartners(this.getContractPartners());
		contract.setTariffApplications(this.getTariffApplications());
		contract.setAdvancePayPlans(this.getAdvancePayPlans());
		contract.setEdiMessages(this.getEdiMessages());
		contract.setAepMakoMessages(this.getAepMakoMessages());
		contract.setCreditorInvoice(this.getCreditorInvoice());
		contract.setDebitorInvoice(this.getDebitorInvoice());
		contract.setInvoicingIntervals(this.getInvoicingIntervals());
		contract.setPowerConsumptionPrognosis(this.getPowerConsumptionPrognosis());
		contract.setContractTypeConfiguration(this.getContractTypeConfiguration());
		contract.setNextInvoiceDate(this.getNextInvoiceDate());
		contract.setOldSupplyId(this.getOldSupplyId());
		contract.setLockReason(this.getLockReason());
		contract.setResellerId(this.getResellerId());
		contract.setCustomerSignatureDate(this.getCustomerSignatureDate());
		contract.setLockDate(this.getLockDate());
		contract.setStateAddition(this.getStateAddition());
		contract.setType(this.getType());
		contract.setContractAdditionalId(this.getContractAdditionalId());
		contract.setVirtualIban(this.getVirtualIban());
		contract.setCommunicationType(this.getCommunicationType());
		contract.setExtraordinaryCancellationDate(this.getExtraordinaryCancellationDate());
		contract.setAccountingCharacteristic(this.getAccountingCharacteristic());
		contract.setBusinessSector(this.getBusinessSector());
		contract.setInvoiceTrigger(this.getInvoiceTrigger());
		contract.setSelfReadingType(this.getSelfReadingType());
		contract.setRatingScore(this.getRatingScore());
		contract.setMsbCycleReadingStart(this.getMsbCycleReadingStart());
		contract.setMsbCycleReadingEnd(this.getMsbCycleReadingEnd());
		contract.setSuspendAccountingCharacteristicCheck(this.getSuspendAccountingCharacteristicCheck());
		contract.setBoniPruefIdentNr(this.getBoniPruefIdentNr());

		contract.setContractAccount(this.getContractAccount());
		contract.setState(this.getState());
		contract.setPointOfDelivery(this.getPointOfDelivery());

		contract.setPeriodStart(this.getPeriodStart());
		contract.setPeriodEnd(this.getPeriodEnd());

		return contract;
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
