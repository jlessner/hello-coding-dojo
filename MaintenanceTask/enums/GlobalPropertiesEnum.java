/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.myBusinessSupplier;

import static com.nextlevel.myBusinessSupplier.RegExEnum.COR_E_OR_1;
import static com.nextlevel.myBusinessSupplier.RegExEnum.CRON_EXPRESSION_5;
import static com.nextlevel.myBusinessSupplier.RegExEnum.CRON_EXPRESSION_6;
import static com.nextlevel.myBusinessSupplier.RegExEnum.DECIMAL_FORMAT;
import static com.nextlevel.myBusinessSupplier.RegExEnum.DETECT_OR_UTF_8;
import static com.nextlevel.myBusinessSupplier.RegExEnum.HEX_CODE;
import static com.nextlevel.myBusinessSupplier.RegExEnum.IBAN_LIST;
import static com.nextlevel.myBusinessSupplier.RegExEnum.INTERVAL_MAX_SIX_NUMBERS;
import static com.nextlevel.myBusinessSupplier.RegExEnum.IP_ADDRESS_OR_DNS_NAME;
import static com.nextlevel.myBusinessSupplier.RegExEnum.ISO_8601;
import static com.nextlevel.myBusinessSupplier.RegExEnum.LOCAL_LANGUAGE;
import static com.nextlevel.myBusinessSupplier.RegExEnum.METER_READINGS;
import static com.nextlevel.myBusinessSupplier.RegExEnum.NAME;
import static com.nextlevel.myBusinessSupplier.RegExEnum.NUMBER_LIST_OR_LAST_DAY_MONTH;
import static com.nextlevel.myBusinessSupplier.RegExEnum.NUMBER_RANGE_DEFINITION;
import static com.nextlevel.myBusinessSupplier.RegExEnum.PHONE_NUMBER;
import static com.nextlevel.myBusinessSupplier.RegExEnum.RATING_CLASS_LIST;
import static com.nextlevel.myBusinessSupplier.RegExEnum.RPT_DESIGN_NUMBER;
import static com.nextlevel.myBusinessSupplier.RegExEnum.UTF8;
import static com.nextlevel.myBusinessSupplier.RegExEnum.ZERO;
import static com.nextlevel.myBusinessSupplier.globalproperties.GlobalPropertySource.ENVIRONMENT_OR_DATABASE;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildDateReverse;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildDecimalNumbers;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildEnumValueValidator;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildFixedValueList;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildGermanDate;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildImapOrImaps;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildIntegerPositiveOrNone;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildServerPortRange;
import static com.nextlevel.myBusinessSupplier.globalproperties.ValueValidatorFactory.buildText255;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.advanced.RegexValidator.buildRegexValidator;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.SingleDecimalValidator.DECIMAL_POSITIVE_ZERO;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.SingleIntegerValidator.INTEGER_ANY;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.SingleIntegerValidator.INTEGER_NEGATIVE;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.SingleIntegerValidator.INTEGER_NEGATIVE_ZERO;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.SingleIntegerValidator.INTEGER_POSITIVE;
import static com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.SingleIntegerValidator.INTEGER_POSITIVE_ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nextlevel.fastlane.core.autostart.impl.ServerIsAllowedToStartSafetyCheck;
import com.nextlevel.myBusinessSupplier.constants.AdvancePayHeightInvoiceAcceptModeEnum;
import com.nextlevel.myBusinessSupplier.constants.AdvancePayPlanHeightCalculationMethodEnum;
import com.nextlevel.myBusinessSupplier.constants.DebitPositionPostingDateCalculationMode;
import com.nextlevel.myBusinessSupplier.constants.SectionEnum;
import com.nextlevel.myBusinessSupplier.constants.processes.WeekDay;
import com.nextlevel.myBusinessSupplier.constants.reliefenergyprice2023.ReliefAmountStretchModeEnum;
import com.nextlevel.myBusinessSupplier.globalproperties.GlobalPropertySource;
import com.nextlevel.myBusinessSupplier.globalproperties.ValueValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.DefaultValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.advanced.ChainValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.advanced.DateTimeValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.advanced.GermanPostalCodeValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.advanced.WriteDirectoryValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.lists.AccountingCharacteristicByILNValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.lists.FixedValuesListValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.lists.IbanPrefixValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.lists.TypeListValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.BooleanValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.SingleStringValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.PositiveIntegerIntervalValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.RangeDecimalValidator;
import com.nextlevel.myBusinessSupplier.globalproperties.validators.primitive.numbers.RangeIntegerValidator;
import com.nextlevel.platform.configuration.GlobalPropertyRegistry;
import com.nextlevel.platform.financial.journal.JournalCreator;
import com.nextlevel.platform.financial.tool.numberGenerator.aggregatedEntries.AggregatedEntriesNumberGenerator;
import com.nextlevel.serviceruntimewrapper.ServiceRuntimeConfigurationProvider;

public enum GlobalPropertiesEnum {

	RELIEF_AMOUNT_STRETCH_MODE(buildEnumValueValidator(ReliefAmountStretchModeEnum.class), ReliefAmountStretchModeEnum.NONE.name()),
	RELIEF_AMOUNT_START_OF_MONTH_OFFSET_IN_BUSINESS_DAYS(INTEGER_ANY, "0"),
	ACTIVITI_TIME_NEXT_START(DateTimeValidator.buildGermanDateTime()),
	PREFERRED_DIRECT_DEBIT_TYPE(buildRegexValidator(COR_E_OR_1)),
	IGNORE_DUPLICATE_CONTRACTS(BooleanValidator.TRUE_FALSE_NULL),

	ENET_IMPORT_FOLDER(WriteDirectoryValidator.getInstance()),
	ENET_ELECTRICITY_URL(),
	ENET_GAS_URL(),
	DYNAMIC_TARIFF_COST_ALLOCATION_CSV_IMPORT_FOLDER(WriteDirectoryValidator.getInstance()),
	REFERENCE_ADDRESSES_IMPORT_FOLDER(WriteDirectoryValidator.getInstance()),
	BANK_CODE_DATA_FOLDER(WriteDirectoryValidator.getInstance()),

	REFERENCE_ADDRESSES_ARCHIV_BASIC_AUTHENTICATION(BooleanValidator.TRUE_FALSE_NULL),
	REFERENCE_ADDRESSES_ARCHIV_DOWNLOAD_URL(DefaultValidator.getInstance()),
	REFERENCE_ADDRESSES_ARCHIV_PASSWORD(DefaultValidator.getInstance()),
	REFERENCE_ADDRESSES_ARCHIV_USER(DefaultValidator.getInstance()),

	TESTSYSTEM(BooleanValidator.TRUE_FALSE_NULL),
	TEST_REVENUE_PROJECTION_USED_CONTRACT_NUMBERS(),
	REVENUE_PROJECTION_MAX_THREAD_COUNT(INTEGER_POSITIVE, "1"),
	REVENUE_PROJECTION_CONTRACT_NUMBERS_PATH(WriteDirectoryValidator.getInstance()),
	REVENUE_PROJECTION_NO_PROJECTION(BooleanValidator.TRUE_FALSE_NULL),
	REVENUE_PROJECTION_CALCULATE_ADDITIONAL_CONSUMPTIONS(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	REVENUE_PROJECTION_ERROR_WAITING_PERIOD(INTEGER_POSITIVE_ZERO, "3"),
	REVENUE_PROJECTION_MAX_NUMBER_OF_ATTEMPT(INTEGER_POSITIVE, "3"),
	REVENUE_PROJECTION_VALUES_TABLE_CLEANUP_THRESHOLD_IN_DAYS(INTEGER_POSITIVE_ZERO, "0"),

	REVENUE_PROJECTION_DYNAMIC_COSTS_DEFAULT_VALUE(DECIMAL_POSITIVE_ZERO, "0"),

	MAIL_FROM(buildRegexValidator(NAME)),
	MAIL_SENDER_NAME(buildRegexValidator(NAME)),
	MAIL_SENDER_FIRST_NAME(buildRegexValidator(NAME)),
	MAIL_SENDER_LIST(),
	MAIL_QUEUE_BATCH_SIZE(INTEGER_POSITIVE_ZERO, "50"),
	MAIL_SHOW_MAX_RESULTS(INTEGER_POSITIVE_ZERO, "50"),
	SUPPORT_7X24_PHONE(buildRegexValidator(PHONE_NUMBER)),
	CENTRAL_CONTACT_PHONE(buildRegexValidator(PHONE_NUMBER)),
	CENTRAL_CONTACT_FAX(buildRegexValidator(PHONE_NUMBER)),
	CENTRAL_CONTACT_EMAIL(),

	SMTP_HOST(buildRegexValidator(IP_ADDRESS_OR_DNS_NAME)),
	MAIL_USER(),
	MAIL_PASSWORD(),

	MAIL_LETTER_PDF_PREVIEW_DIRECTORY_NAME(SingleStringValidator.ANY, "mailLetterPreview"),
	SMTP_DEBUG(BooleanValidator.TRUE_FALSE_NULL),
	SMTP_PORT(INTEGER_POSITIVE),
	SMTP_USE_TLS(BooleanValidator.TRUE_FALSE_NULL),
	SMTP_USE_SSL(BooleanValidator.TRUE_FALSE_NULL),
	SEND_MAIL_OVER_FILESYSTEM(BooleanValidator.TRUE_FALSE_NULL),
	SEND_MAIL_OVER_FILESYSTEM_FOLDER(WriteDirectoryValidator.getInstance()),

	LETTER_PRINTER_FOR_POSTAL_DELIVERY__EMAIL_ADDRESS(),
	LETTER_PRINTER_FOR_POSTAL_DELIVERY__REDIRECTION_PATH(
			new ChainValidator().withOperator(ChainValidator.Operator.OR).add(WriteDirectoryValidator.getInstance())
					.add(SingleStringValidator.EMPTY)),
	ARCHIVE_ONLY_EMAIL_ADDRESS(),

	FORCE_NEW_VERSION_NUMBERS(BooleanValidator.TRUE_FALSE_NULL),

	BALANCING_GROUP_IDENTIFIER(),
	BALANCING_GROUP_IDENTIFIER_GAS(),
	BALANCING_GROUP_IDENTIFIER_GAS_THE(),

	BALANCING_GROUP_IDENTIFIER_GAS_NETCONNECT_H(),
	BALANCING_GROUP_IDENTIFIER_GAS_NETCONNECT_L(),
	BALANCING_GROUP_IDENTIFIER_GAS_GASPOOL_H(),
	BALANCING_GROUP_IDENTIFIER_GAS_GASPOOL_L(),
	BALANCING_GROUP_IDENTIFIER_GAS_THE_H(),
	BALANCING_GROUP_IDENTIFIER_GAS_THE_L(),

	EMAIL_QUEUE_INTERVAL(INTEGER_POSITIVE),
	MAIL_STORAGE_PAGE_SIZE(INTEGER_POSITIVE, "50"),
	OUTBOX_FLUSH_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_6)),
	IMAP_ENABLED(BooleanValidator.TRUE_FALSE_NULL),
	IMAP_STORE_PROTOCOL(buildImapOrImaps()),// imap or imaps
	IMAP_STORE(buildImapOrImaps()),

	IMAP_HOST(buildRegexValidator(IP_ADDRESS_OR_DNS_NAME)),
	IMAP_PORT(buildServerPortRange()),
	IMAP_FOLDER_PATH(),
	IMAP_USER(),
	IMAP_PASSWORD(),
	IMAP_RECONNECT_MINUTES(INTEGER_POSITIVE, "1"),
	IMAP_CONNECTION_TTL(INTEGER_POSITIVE, "60"),
	IMAP_FAILED_EMAILS_SUBFOLDER(),
	IMAP_MAX_RETRIES(INTEGER_POSITIVE, "60"),
	CONTRACT_DETECT_REGEX(),
	CONTRACT_DETECT_GROUP(buildRegexValidator(ZERO)),
	BUSINESS_PARTNER_NUMBER_DETECT_REGEX(),
	BUSINESS_PARTNER_NUMBER_DETECT_GROUP(INTEGER_POSITIVE_ZERO),
	TRY_TO_REPAIR_CONTRACT_DETECT_REGEX(),
	TRY_TO_REPAIR_CONTRACT_DETECT_GROUP(buildRegexValidator(ZERO)),
	BDM_CONNECT_RETRY_COUNT(INTEGER_POSITIVE),

	// if BDM_METHOD_CALL_TIMEOUT should differ from default SR_METHOD_TIMEOUT
	BDM_METHOD_CALL_TIMEOUT(INTEGER_POSITIVE, "15000"),

	// if BDM_MAX_DATA_SIZE should differ from default SR_CLIENT_MAX_MESSAGE_SIZE
	BDM_MAX_DATA_SIZE(INTEGER_POSITIVE),

	// ServiceRuntime

	// should be used instead of BDM_HOST and BDM_PORT: "[BDM_HOST]:[BDM_PORT]"
	SR_SEED(DefaultValidator.getInstance(), null,
			ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_SEED, ENVIRONMENT_OR_DATABASE),
	SR_DNS_USAGE(BooleanValidator.TRUE_FALSE_NULL, null, ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_SEED_DNS),
	// should be used instead of BDM_CLIENT
	SR_HOST_NAME(buildRegexValidator(IP_ADDRESS_OR_DNS_NAME), null,
			ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_HOST_NAME),
	SR_SERVER_PORT(INTEGER_POSITIVE, null, ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_SERVER_PORT),
	// should be used instead of BDM_CLIENT_PORT, use "0" to tell ServiceRuntime it should choose a free port
	SR_CLIENT_PORT(INTEGER_POSITIVE, null, ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_CLIENT_PORT),
	// should be used instead of BDM_METHOD_CALL_TIMEOUT
	SR_METHOD_TIMEOUT(INTEGER_POSITIVE_ZERO, null, ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_METHOD_TIMEOUT),
	// should be used instead of BDM_MAX_DATA_SIZE
	SR_CLIENT_MAX_MESSAGE_SIZE(INTEGER_POSITIVE_ZERO, null,
			ServiceRuntimeConfigurationProvider.GLOBAL_PROPERTY_KEY_SERVICE_RUNTIME_CLIENT_MAX_MESSAGE_SIZE),

	// ELASTICSEARCH

	ELASTICSEARCH_HOST(new ChainValidator()
			.withOperator(ChainValidator.Operator.OR)
			.add(buildRegexValidator(IP_ADDRESS_OR_DNS_NAME))
			.add(new FixedValuesListValidator().withAcceptedValues("localhost")), ENVIRONMENT_OR_DATABASE),
	ELASTICSEARCH_PORT(buildServerPortRange(), "9300"),
	ELASTICSEARCH_CACHE_MAXSIZE(new RangeIntegerValidator(0, 999999), "500"),
	ELASTICSEARCH_CACHE_MAXIDLE(new RangeIntegerValidator(0, 9999), "24"),
	ELASTICSEARCH_CACHE_MAXIDLE_UNIT(
			buildFixedValueList("NANOSECONDS", "MICROSECONDS", "MILLISECONDS", "SECONDS", "MINUTES", "HOURS", "DAYS"), "HOURS"),
	ELASTICSEARCH_CACHE_DISABLE(BooleanValidator.TRUE_FALSE_NULL),
	ELASTICSEARCH_LOAD_PROFILE_LIMIT(INTEGER_POSITIVE_ZERO, "1000"),

	EMAIL_ACCESS_WITH_REST(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),

	//"^linear$|^useFallbackIfNeeded$|^onlyDefined$|^onlyFallback"
	LOADPROFILE_ACCURACY(buildFixedValueList("linear", "useFallbackIfNeeded", "onlyDefined", "onlyFallback"), "useFallbackIfNeeded"),
	LOADPROFILE_SAVE_ONE_VALUE_PER_DAY(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),
	LOADPROFILE_UPLOAD_WITHOUT_BDM(BooleanValidator.TRUE_FALSE_NULL),

	ADVANCEPAYPLAN_POSSIBLE_PAYMENT_DATE(buildRegexValidator(NUMBER_LIST_OR_LAST_DAY_MONTH), "1,15"),
	ADVANCEPAYPLAN_HEIGHT_MAX_REDUCTION_PERCENTAGE(new RangeIntegerValidator(0, 100), "10"),
	ADVANCEPAYPLAN_MAX_REDUCTIONS_PER_YEAR(INTEGER_POSITIVE_ZERO, "2"),
	ADVANCEPAYPLAN_MINIMUM_BUSINESS_DAYS_BEFORE_NEXT_OCCURENCE(INTEGER_POSITIVE_ZERO, "5"),
	ADVANCEPAYPLAN_MINIMUM_DAYS_BEFORE_NEXT_OCCURENCE(INTEGER_POSITIVE_ZERO, "14"),
	ADVANCEPAYPLAN_POSTINGDATE_CALCULATION_MODE(buildEnumValueValidator(DebitPositionPostingDateCalculationMode.class),
			DebitPositionPostingDateCalculationMode.DEFAULT.name()),
	ADVANCEPAYPLAN_MINIMUM_DAYS_AFTER_SUPPLY_BEGIN(INTEGER_POSITIVE_ZERO, "14"),

	ADVANCEPAYPLAN_HEIGHT_CALCULATION_METHOD(buildEnumValueValidator(AdvancePayPlanHeightCalculationMethodEnum.class),
			AdvancePayPlanHeightCalculationMethodEnum.includingFutureTariffChange.name()),

	ADVANCEPAYPLAN_HEIGHT_INVOICE_CALCULATION_MODE(buildEnumValueValidator(AdvancePayHeightInvoiceAcceptModeEnum.class),
			AdvancePayHeightInvoiceAcceptModeEnum.DEFAULT.name()),

	ADVANCE_PAYPLAN_HEIGHT_INTERVAL_PRIVATE_ELECTRICITY(new PositiveIntegerIntervalValidator(), "1, 900"),
	ADVANCE_PAYPLAN_HEIGHT_INTERVAL_BUSINESS_ELECTRICITY(new PositiveIntegerIntervalValidator(), "1, 900"),
	ADVANCE_PAYPLAN_HEIGHT_INTERVAL_PRIVATE_GAS(new PositiveIntegerIntervalValidator(), "1, 900"),
	ADVANCE_PAYPLAN_HEIGHT_INTERVAL_BUSINESS_GAS(new PositiveIntegerIntervalValidator(), "1, 900"),

	// JOURNAL

	//JournalExportFormat
	JOURNAL_EXPORT_FORMAT(buildFixedValueList("SAP", "CSV", "Excel", "IDOCXML")),
	JOURNAL_MANDANTOR(),
	JOURNAL_ACCOUNTINGAREA(),
	JOURNAL_FOLDERPATH(WriteDirectoryValidator.getInstance()),
	JOURNAL_FILENAME_MANDANT(),

	JOURNAL_JOURNALHEAD_ENTRIES_TYPE(),
	JOURNAL_JOURNALHEAD_GENERAL_LEDGER_INTERFACE(),

	JOURNAL_EXPORT_VALUE_FORMAT(buildRegexValidator(DECIMAL_FORMAT)),    // Excel
	JOURNAL_FIELD_MAPPING(),  // Excel

	// JOURNAL_IDOC, mandatory

	JOURNAL_GLOBAL_ACCOUNTINGAREA(),  // XXX Gilt nur für IDoc - sollte dann JOURNAL_IDOC_... heißen
	JOURNAL_IDOC_TECH_LOGICALSYSTEM(),
	JOURNAL_IDOC_BUSINESS_RECEIPT_TYPE(),
	JOURNAL_IDOC_BUSINESS_USER(),
	JOURNAL_IDOC_BUSINESS_COST_AREA(),
	JOURNAL_IDOC_BELNR_NUMBER(DefaultValidator.getInstance(), "000[fffffff]{5100000-5199999}"),

	// JOURNAL_IDOC, optional

	JOURNAL_B2B_CHANNEL(),    // XXX Gilt nur für IDoc - sollte dann JOURNAL_IDOC_... heißen
	JOURNAL_IDOC_KOSTL_IN_AUFNR(BooleanValidator.TRUE_FALSE_NULL),

	JOURNAL_IDOC_GSBER_VALUE(),
	JOURNAL_IDOC_SEGMENT_VALUE(DefaultValidator.getInstance(), "1"),
	JOURNAL_IDOC_AWTYP_VALUE(DefaultValidator.getInstance(), ""),
	// ProfitCenter, SAP-short: PRCTR
	JOURNAL_PROFIT_CENTER_FIXED(),    // XXX Gilt nur für IDoc - sollte dann JOURNAL_IDOC_... heißen
	JOURNAL_IDOC_UNIT_NAME_MAPPING(),
	JOURNAL_IDOC_VATDATE_VALUE(),

	// JOURNAL_IDOC_EDIDC40

	JOURNAL_IDOC_EDIDC40_DOCNUM(DefaultValidator.getInstance(), "[ffffffffffffffff]"),
	JOURNAL_IDOC_EDIDC40_SNDPOR_VALUE(DefaultValidator.getInstance(), ""),
	JOURNAL_IDOC_EDIDC40_SNDPRT_VALUE(DefaultValidator.getInstance(), ""),
	JOURNAL_IDOC_EDIDC40_SNDPRN_VALUE(DefaultValidator.getInstance(), ""),
	JOURNAL_IDOC_EDIDC40_RCVPOR_VALUE(DefaultValidator.getInstance(), ""),
	JOURNAL_IDOC_EDIDC40_RCVPRT_VALUE(DefaultValidator.getInstance(), ""),
	JOURNAL_IDOC_EDIDC40_RCVPRN_VALUE(DefaultValidator.getInstance(), ""),

	// EDM

	EDM_EXPORT_CHANNEL(),
	EDM_EXPORT_CLIENT_NUMBER(buildText255()),

	CLIENT_NAME(),

	B2B_IP_PORT(DefaultValidator.getInstance(), "localhost:8080", ENVIRONMENT_OR_DATABASE),
	B2B_MONITORING_PATH(), //used from ui
	B2B_SEARCH_SUFFIX(), //used from ui
	B2B_URL_TESTGENERATOR(), // http://localhost:8080/b2bbp-engine/StartupEngine?channel=INBOUND_DIVIDING
	B2B_USER(DefaultValidator.getInstance(), "admin"),
	B2B_PASSWORD(), // there is a default value of the password, but I do not want to write this into the code
	B2B_OUTBOUND_CHANNEL(),
	B2B_VALIDATE_OUTBOUND_EDI(BooleanValidator.TRUE_FALSE_NULL,
			Boolean.FALSE.toString()), // if true all Edis are validated in B2B before sending

	SUPPLY_BEGIN_ONLY_FIRST_OF_MONTH(BooleanValidator.TRUE_FALSE_NULL),
	SUPPLY_BEGIN_FIRST_PAY_PLAN_ONE_MONTH_LATER(BooleanValidator.TRUE_FALSE_NULL),

	NUMBER_RANGE_BUSINESS_PARTNER_NUMBER(
			buildRegexValidator(NUMBER_RANGE_DEFINITION)), //this regex does NOT cover all use-cases, see: NumberGeneratorTest
	NUMBER_RANGE_CONTRACT_NUMBER(),
	NUMBER_RANGE_INTEREST_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION)),
	NUMBER_RANGE_BOOKING_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION)),
	NUMBER_RANGE_INVOICE_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION)),
	NUMBER_RANGE_INVOICE_REVERSAL_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION)),
	NUMBER_RANGE_INVOICE_DRAFT_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION)),
	NUMBER_RANGE_INVOICE_INFORMATION_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION), "A[JJ][ffffffff]"),
	NUMBER_RANGE_SEPA_MANDATE_NUMBER(buildRegexValidator(NUMBER_RANGE_DEFINITION)),

	OWN_BUSINESS_PARTNER_NUMBERS(),

	SEPA_MANDATE_VALIDATOR(buildFixedValueList("ON", "OFF")),
	SEPA_CREATE_COLLECTIVE_MANDATE(BooleanValidator.TRUE_FALSE_NULL),
	SEPA_ACTIVATE_BY_BANK_ACCOUNT_CREATION(BooleanValidator.TRUE_FALSE_NULL),

	SYSTEM_LOCALE(buildRegexValidator(LOCAL_LANGUAGE)), // Amtssprache

	NUMBER_RETURNED_SQL_ROWS(INTEGER_POSITIVE, "100"),

	SQL_EXECUTION_DATASOURCE(),

	DEBITOR_INVOICE_MAX_DAYS(INTEGER_POSITIVE),
	// yearly, halfyearly, quarterly invoices
	DEBITOR_INVOICE_METER_READING_WINDOW_BEGIN(INTEGER_ANY),
	DEBITOR_INVOICE_METER_READING_WINDOW_END(INTEGER_POSITIVE),
	// single monthly invoices only
	DEBITOR_INVOICE_METER_READING_WINDOW_MONTHLY_BEGIN(INTEGER_ANY),
	DEBITOR_INVOICE_METER_READING_WINDOW_MONTHLY_END(INTEGER_POSITIVE),

	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_DATE_BEGIN(INTEGER_NEGATIVE_ZERO),
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_DATE_END(INTEGER_POSITIVE_ZERO),
	// single monthly invoices only
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_DATE_MONTHLY_BEGIN(INTEGER_NEGATIVE_ZERO),
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_DATE_MONTHLY_END(INTEGER_POSITIVE_ZERO),
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_PERIOD_BEGIN(INTEGER_NEGATIVE_ZERO),
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_PERIOD_END(INTEGER_POSITIVE_ZERO),
	// single monthly invoices only
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_PERIOD_MONTHLY_BEGIN(INTEGER_NEGATIVE_ZERO),
	DEBITOR_INVOICE_METER_READING_WINDOW_GRID_OPERATOR_PERIOD_MONTHLY_END(INTEGER_POSITIVE_ZERO),

	DEBITOR_INVOICE_DISABLE_METER_READING_REMINDER_PROCESS(BooleanValidator.TRUE_FALSE_NULL),

	DEBITOR_INVOICE_ONLY_END_OF_YEAR(BooleanValidator.TRUE_FALSE_NULL),
	DEBITOR_INVOICE_ONLY_END_OF_MONTH(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	// Stichtagsabrechnung
	DEBITOR_INVOICE_FIXED_DATE_SETTLEMENT(BooleanValidator.TRUE_FALSE_NULL),
	DEBITOR_INVOICE_GRID_OPERATOR_TRUNK_YEAR_BUFFER(INTEGER_POSITIVE_ZERO),
	DEBITOR_INVOICE_GRID_OPERATOR_INVOICE_RELEVANT_METER_READING_SOURCES(new TypeListValidator().withType(SingleStringValidator.NOT_EMPTY)),
	DEBITOR_INVOICE_ROUNDING_PRECISION_BASE_FEE_DAY_UNIT_PRICE(INTEGER_POSITIVE),
	DEBITOR_INVOICE_ROUNDING_PRECISION_BASE_FEE_MONTH_UNIT_PRICE(INTEGER_POSITIVE),
	DEBITOR_INVOICE_ROUNDING_PRECISION_POWER_CONSUMPTION_UNIT_PRICE(INTEGER_POSITIVE),

	//9 is max scale in DB
	DEBITOR_INVOICE_READING_SCALE(new RangeIntegerValidator(0, 9), "0"),
	DEBITOR_INVOICE_CONSUMPTION_SCALE(new RangeIntegerValidator(0, 9), "0"),
	DEBITOR_INVOICE_CONSUMPTION_SCALE_RLM(new RangeIntegerValidator(0, 9), "9"),
	DEBITOR_INVOICE_ROUNDING_MODE(buildEnumValueValidator(RoundingMode.class), "HALF_UP"),

	DEBITOR_INVOICE_INCLUDE_ADDITIONAL_READINGS(BooleanValidator.TRUE_FALSE_NULL, null,
			"ADD_NON_INVOICE_RELEVANT_READINGS_TO_DEBITOR_INVOICE_PDF"),

	DEBITOR_INVOICE_USE_EXPENSE_SLICES_FOR_SERVICES(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),

	DEBITOR_INVOICE_EXECUTION_DISABLE_PROGNOSIS_SEND_TO_VNB(BooleanValidator.TRUE_FALSE_NULL),
	DEBITOR_INVOICE_EEG_PAYBACK_ITEM_SUBJECT(SingleStringValidator.NOT_EMPTY, "Gutschrift EEG-Umlage"),
	DEBITOR_INVOICE_EEG_TARIFF_IDS_WITH_PAYBACK_ITEM_ENABLED(new TypeListValidator().withType(SingleStringValidator.NUMERIC)),

	PAYPLAN_OFFSET(INTEGER_POSITIVE),
	CREDIT_TRANSFER_OFFSET(INTEGER_POSITIVE),

	//Undocumented property: set wild card
	COLLAPSE_INVOIC_CREDIT_CHARGEBACK(),

	DEBITOR_INVOICE_ADVERTISING(),

	DEBITOR_INVOICE_REMOVE_DEPRECATED_VALUES(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	NO_CONTENT_PRODUCTION_THREAD(BooleanValidator.TRUE_FALSE_NULL),

	//BaseFeeCalculationMode
	BASE_FEE_CALCULATION_MODE(buildFixedValueList("MONTH_OR_DAY", "ALWAYS_DAYS")),

	MINIMUM_INVOICING_INTERVAL_CHANGE_PERIOD(INTEGER_POSITIVE),

	BACKWARD_DEBIT_POSITION(BooleanValidator.TRUE_FALSE_NULL),

	CREDIT_TRANSFER_IMMEDIATELY(BooleanValidator.TRUE_FALSE_NULL),

	SUPPLY_BEGIN_MASTER_DATA_CHANGE_RESPONSE_DELAY(INTEGER_POSITIVE_ZERO, "5"),

	ACTIVITI_PROCESS_AUTO_DEPLOYMENT(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),
	// be careful, if true, be sure that no background process is started twice
	ACTIVITI_FORCE_STARTUP(BooleanValidator.TRUE_FALSE_NULL),

	ENTRY_TO_COVER_SELECTOR_DEBITOR_ACCOUNT(buildFixedValueList("NoCoveringEntryToCoverSelector", "OnlyExactValueEntryToCoverSelector",
			"FromEarliestToLatestEntryToCoverSelector", "FromLatestToEarliestEntryToCoverSelector",
			"EntryToCoverSelectorEntryToCoverSelector")),

	PRODUCE_CONTENT_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_5), "* * * * *"),

	LOADPROFILE_IMPORT_FOLDER(WriteDirectoryValidator.getInstance()),

	ACTIVITI_JOB_EXECUTOR_MAX_POOL_SIZE(INTEGER_POSITIVE),

	ACTIVITI_JOB_EXECUTOR_CORE_POOL_SIZE(INTEGER_POSITIVE),
	ACTIVITI_JOB_EXECUTOR_QUEUE_SIZE(INTEGER_POSITIVE),

	PAYBACK_INTERVAL_BETWEEN_EXECUTION_AND_DUE_DATE(INTEGER_ANY, "-1"),
	PAYBACK_INTERVAL_BETWEEN_EXPORT_AND_EXECUTION_DATE_UNDERFLOW(INTEGER_POSITIVE_ZERO, "1"),

	SET_POST_ADR_IN_DIRECT_DEBIT_MSG_FOR_IBAN_PREFIX(IbanPrefixValidator.getInstance(), "GB,GI"),

	DIRECT_DEBIT_MESSAGE_MAX_ENTRIES(INTEGER_POSITIVE_ZERO, "500"),
	DIRECT_DEBIT_MESSAGE_SUBJECT_SPECIFIC(),
	DIRECT_DEBIT_MESSAGE_EXPORT_PATH(WriteDirectoryValidator.getInstance()),
	DIRECT_DEBIT_MESSAGE_EXPORT_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_6),
			"5/20 * ? * * *"), // Every 20 minutes starting at :15 minutes after the hour

	DIRECT_DEBIT_POSITION_EXECUTION_OFFSET(INTEGER_POSITIVE_ZERO, "3"),

	DEBITOR_CREDIT_TRANSFER_MESSAGE_EXPORT_PATH(WriteDirectoryValidator.getInstance()),
	DEBITOR_CREDIT_TRANSFER_MESSAGE_EXPORT_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_6),
			"10/20 * ? * * *"), // Every 20 minutes starting at :20 minutes after the hour

	DISABLE_GLOBAL_PROPERTY_VALIDATION(),
	CREDITOR_CREDIT_TRANSFER_MESSAGE_EXPORT_PATH(WriteDirectoryValidator.getInstance()),
	CREDITOR_CREDIT_TRANSFER_MESSAGE_EXPORT_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_6),
			"15/20 * ? * * *"), // Every 20 minutes starting at :25 minutes after the hour

	SECTIONS_DISPLAY_UI(BooleanValidator.TRUE_FALSE_NULL),

	SECTIONS_ENABLE_ELECTRICITY(BooleanValidator.TRUE_FALSE_NULL, null, SectionEnum.Electricity.getEnablerGlobalPropertyKey()),

	SECTIONS_ENABLE_GAS(BooleanValidator.TRUE_FALSE_NULL, null, SectionEnum.Gas.getEnablerGlobalPropertyKey()),

	SECTIONS_ENABLE_MSB(BooleanValidator.TRUE_FALSE_NULL, null, SectionEnum.MSB.getEnablerGlobalPropertyKey()),

	CONTRACT_APPLICATION_DATE_VALUESOURCE(buildFixedValueList("ADDITIONAL1", "ADDITIONAL2", "ADDITIONAL3")),

	SUPPLYBEGIN_MAX_DELAY_MONTHS(INTEGER_POSITIVE_ZERO, "12"),

	SUPPLY_BEGIN_PRIVATE_TARIFF_MAKO_DELAY_DAYS(INTEGER_POSITIVE_ZERO),

	ACCOUNTING_PERIOD_OPENING_OFFSET(INTEGER_POSITIVE_ZERO, "365"),

	BONUS_SAZ_TRANSFER_SUBJECT(buildRegexValidator(UTF8)),

	BONUS_TAX_CALCULATION_DATE_INVOICE(
			buildFixedValueList("CREATION_DATE", "POSTING_DATE", "DUE_DATE", "CONTRACT_CONCLUSION_DATE", "PERIOD_END", "FIXED_TAX_RATE_19"),
			"FIXED_TAX_RATE_19"),
	BONUS_TAX_CALCULATION_DATE_IMMEDIATE(
			buildFixedValueList("CREATION_DATE", "POSTING_DATE", "DUE_DATE", "CONTRACT_CONCLUSION_DATE", "FIXED_TAX_RATE_19"),
			"FIXED_TAX_RATE_19"),

	FORCE_BONUS_ON_CONTRACT_IMPORT(BooleanValidator.TRUE_FALSE_NULL),

	TARIFF_CALCULATOR_TARIFF_ID_FOR_COMPARISON(),
	TARIFF_CALCULATOR_ALLOWED_TARIFF_IDS(),
	// Days tariff successor is available in portal before next possible cancellation date
	TARIFF_SUCCESSOR_AVAILABLE_DAYS(),
	TARIFF_DYNAMIC_COSTS_SWITCH_BACK_TO_PROFILE_ON_CHANGE(BooleanValidator.TRUE_FALSE_NULL),

	ALLOWED_STA_FILE_IBANS(buildRegexValidator(IBAN_LIST)),

	STAFILE_IMPORT_USE_VALUTADATE(BooleanValidator.TRUE_FALSE, Boolean.FALSE.toString()),

	STAFILE_IMPORT_USE_DEBITOR_CLEARING_CHAIN(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),

	PORTAL_URL(ENVIRONMENT_OR_DATABASE),
	PORTAL_URL_LOCAL_ACCESS(ENVIRONMENT_OR_DATABASE), // if external Portal URL cannot be accessed internal

	@Deprecated // service Endpoint is not published in the portal any more
	PORTAL_UPDATE_MASTERDATA(BooleanValidator.TRUE_FALSE_NULL), // true or false
	PORTAL_URL_COMPLETE(),
	PORTAL_CREATE_ACCOUNT_ON_IMPORT(BooleanValidator.TRUE_FALSE_NULL), // true or false
	PORTAL_TOKEN_CANCELLATION(DefaultValidator.getInstance(), "localhost:4200/cancellation/start?token="),
	PORTAL_TOKEN_SUPPLYBEGIN(DefaultValidator.getInstance(), "localhost:4200/supplybegin/start?token="),

	CUSTOMER_COMMUNICATION_CONSENT_WORDING__DEFAULT_TEXT(),

	DUNNING_IGNORE_DEBIT_ACCOUNT_TYPE(),

	INVOIC_VALIDATION_AB1_BRUTTOMAXAMOUNT(INTEGER_POSITIVE),
	INVOIC_VALIDATION_MAX_INVOICE_CONSUMPTION(INTEGER_POSITIVE),
	INVOIC_VALIDATION_MMM_RCH_CANCELLATION_BORDER_DATE(buildDateReverse()),

	@Deprecated // use column tariffname instead
	CONTRACT_IMPORT_DEFAULT_TARIFF(),

	@Deprecated // use column tariffname instead
	CONTRACT_IMPORT_DEFAULT_TARIFF_GAS(),

	CONTRACT_IMPORT_MAIL_DUPLICATE_NOTIFICATION(DefaultValidator.getInstance(), "TASK"),

	VERIVOX_EXPORT_TARIFF(),

	TARIFF_APPLICATION_ACCEPT_EMPTY_PRICES(BooleanValidator.TRUE_FALSE_NULL),

	OPERATOR_COMPLAINT_TELEPHONE_NUMBER(buildRegexValidator(PHONE_NUMBER)),
	OPERATOR_COMPLAINT_TELEPHONE_ADDITION(SingleStringValidator.ALPHANUMERIC),
	OPERATOR_COMPLAINT_MAIL_ADDRESS(),
	OPERATOR_COMPLAINT_CONTACT_PERSON_MAKO(),

	// number of days after supplyBeginn when the contract import bonus is payed out. Default is 60
	VERIVOX_DIRECT_BONUS_DAYS(INTEGER_POSITIVE),
	VERIVOX_DIRECT_BONUS_DAYS_EXTERN(INTEGER_POSITIVE),

	CHECK24_DIRECT_BONUS_DAYS(INTEGER_POSITIVE),
	CHECK24_DIRECT_BONUS_DAYS_EXTERN(INTEGER_POSITIVE),

	@Deprecated
	INTERNAL_PLATFORM_DIRECT_BONUS_DAYS(),
	@Deprecated
	INTERNAL_PLATFORM_DIRECT_BONUS_DAYS_EXTERN(),

	CONTRACT_IMPORT_DISABLE_PRICE_VALIDATION(BooleanValidator.TRUE_FALSE_NULL),

	ACQUISITION_PRICE_COMPUTATION_DATE_OFFSET(INTEGER_POSITIVE_ZERO),

	// if true and supplyBeginDate is in past, then the number of days are calculated from now. if false its calculated from periodStart which
	// is the default
	DIRECT_BONUS_DAYS_BACKWARD_FROM_NOW(BooleanValidator.TRUE_FALSE_NULL),

	DIRECT_BONUS_MINIMUM_DAYS_IN_FUTURE(INTEGER_POSITIVE_ZERO, "10"),

	NEXT_POSSIBLE_CANCELLATION_DATE_EXPORT_RESULT_PATH(WriteDirectoryValidator.getInstance()),

	VERIVOX_EXPORT_RESULT_PATH(WriteDirectoryValidator.getInstance()),
	VERIVOX_EXPORT_IGNORE_GRID_ILNS(),
	VERIVOX_EXPORT_EXPORT_PLZ_WITH_STREETS(GermanPostalCodeValidator.getInstance()),
	VERIVOX_EXPORT_GRID_OPERATOR_FORMAT(BooleanValidator.TRUE_FALSE_NULL),
	VERIVOX_EXPORT_EXPORT_DATE(buildGermanDate()),
	ACQUISITION_CALCULATE_FOR_SUPPLYBEGINN_DATE(BooleanValidator.TRUE_FALSE_NULL),
	ACQUISITION_PRICE_INFORMATION_DISTINCT_PRICES(BooleanValidator.TRUE_FALSE_NULL),

	// Format: ([+-]?)(\\d+)[YJMDT];([+-]?)(\\d+)[YJMDT]
	PROGNOSIS_DURATION_SEARCH_METERREADINGS(buildRegexValidator(METER_READINGS)),
	PROGNOSIS_DURATION_IGNORE_METERREADINGS_ELECTRICITY(buildRegexValidator(METER_READINGS)),
	PROGNOSIS_DURATION_IGNORE_METERREADINGS_GAS(buildRegexValidator(METER_READINGS)),

	PROGNOSIS_ADAPT_FOR_EACH_METERREADING(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),
	METERREADING_ESTIMATION_RECALCULATION_THRESHOLD_IN_DAYS(INTEGER_POSITIVE_ZERO, "9999"),

	@Deprecated //deprecation due to changes in MYBS-2310
	MAX_PROGNOSIS_DEVIATION_RATE(new RangeDecimalValidator(0.0, 1.0)),  // possible values: [0, 0.1, 0.2, 0.n, 0.9, 1]

	PORTAL_RESTRICT_CREATE_READINGS_TO_INVOICE_DAYS(BooleanValidator.TRUE_FALSE_NULL),

	PORTAL_FILTER_DUPPLICATED_READINGS(BooleanValidator.TRUE_FALSE_NULL),

	PORTAL_READING_TYP_INTERVALS(buildRegexValidator(INTERVAL_MAX_SIX_NUMBERS)),

	MAIL_RECIPIENTS_REMADV_INFO(),

	B2B_TO_MBS_ROUTING_OFFSET(INTEGER_POSITIVE),

	MASTERDATA_EXPORT_RESULT_PATH(WriteDirectoryValidator.getInstance()),

	PAY_PLAN_FIXED_OCCURENCES_PER_YEAR(new RangeIntegerValidator(1, 12)),

	CREATE_ACCOUNTING_DOC_REQUIRED(BooleanValidator.TRUE_FALSE_NULL),

	FILE_CRAWLER_CONTRACT_IMPORT_PATH(WriteDirectoryValidator.getInstance()),
	FILE_CRAWLER_CONTRACT_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),

	FILE_CRAWLER_ACCOUNT_STATEMENTS_IMPORT_PATH(),
	FILE_CRAWLER_ACCOUNT_STATEMENTS_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),

	FILE_CRAWLER_TASK_IMPORT_PATH(),
	FILE_CRAWLER_TASK_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),
	@Deprecated
	FILE_CRAWLER_MALO_IMPORT_PATH(SingleStringValidator.ALPHANUMERIC),
	FILE_CRAWLER_MALO_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),

	FILE_CRAWLER_DEGREEDAY_IMPORT_PATH(DefaultValidator.getInstance(), "DegreeDayData"),
	FILE_CRAWLER_DEGREEDAY_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),
	DEGREEDAY_IMPORT_URL(),
	CREATE_TASK_FOR_DEGREEDAY_IMPORT_WITH_MEASURING_FAILURE(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),

	FILE_CRAWLER_EDI_IMPORT_PATH(WriteDirectoryValidator.getInstance()),
	FILE_CRAWLER_EDI_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),

	EFLOW_IMPORT_PATH(WriteDirectoryValidator.getInstance()),
	EFLOW_INTERVAL(INTEGER_POSITIVE, "5000"),
	EFLOW_SENDER__EMAIL_ADDRESS(),
	EFLOW_RECEIVER__EMAIL_ADDRESS(),

	BACK_DEBIT_CHANGE_PAYMENT_METHOD(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	//BlockLevelEnum
	BACK_DEBIT_BLOCK_LEVEL(buildFixedValueList("AccountingDocumentLevel", "ContractLevel", "BusinessPartnerLevel"), "ContractLevel"),

	//BlockDurationEnum
	BACK_DEBIT_BLOCK_ENDDATE_DURATION(buildFixedValueList("DEFAULT", "UNLIMITED"), "DEFAULT"),
	BACK_DEBIT_OVERRIDE_INFOTASK_CREATION(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	BACK_DEBIT_MINIMUM_BLOCK_DURATION_IN_DAYS(INTEGER_ANY, "14"), //use integer because other regex do not work as intented

	DONT_SEND_PAYMENT_METHOD_CHANGED_MAIL(BooleanValidator.TRUE_FALSE_NULL),

	CHANGE_PAYMENT_TYPE_INFORM_CUSTOMER_SEND_MAIL(BooleanValidator.TRUE_FALSE, Boolean.FALSE.toString()),

	@Deprecated
	REQUEST_MALO_IDS_ON_START(BooleanValidator.TRUE_FALSE_NULL),

	ALLOWED_SERVER_PATH_AND_HOSTNAME(DefaultValidator.getInstance(), null, ServerIsAllowedToStartSafetyCheck.PROPERTY_KEY),
	ALLOWED_SERVER_PATH_AND_CANONICAL_HOSTNAME(DefaultValidator.getInstance(), null,
			ServerIsAllowedToStartSafetyCheck.PROPERTY_KEY_CANONICAL),

	ALLOWED_SERVER_PATH_AND_HOSTNAME_PREFIX_FILTER(DefaultValidator.getInstance(), null,
			ServerIsAllowedToStartSafetyCheck.PREFIX_PROPERTY_KEY),

	IGNORE_TENANT_IMPORTER_VALIDATION(BooleanValidator.TRUE_FALSE_NULL),
	TENANT_IMPORTER_IMPORT_TO_CANCELD(BooleanValidator.TRUE_FALSE_NULL),

	//LOADPROFILE PATTERNS
	GAS_CONSUMPTION_CALCULATION_LOADPROFILE_PATTERNS(DefaultValidator.getInstance(), "HK.*"),

	// EDI PROCESSING

	EDI_OUTBOUND_ONLINE(BooleanValidator.TRUE_FALSE_NULL),
	INBOUND_EDI_QUEUE_CRON_EXPRESSION(buildRegexValidator(
			CRON_EXPRESSION_5)),  // XXX Should be EDI_INBOUND_QUEUE_CRON_EXPRESSION or EDI_INBOUND_CRON_EXPRESSION for consistency
	EDI_OUTBOUND_IMMEDIATE(BooleanValidator.TRUE_FALSE_NULL),
	OUTBOUND_EDI_QUEUE_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_5)), // XXX Rename for consistency
	EDI_INBOUND_IMMEDIATE(BooleanValidator.TRUE_FALSE_NULL),
	TEST_EDI_WITH_EXCEPTION(BooleanValidator.TRUE_FALSE_NULL),
	// mbs-8758: Kommasepariert die Prüfidentifikatoren der Nutznachricht (die Nachricht, die ins System gekommen ist) enthalten, für die eine Aufgabe erzeugt werden soll
	EDI_OUTBOUND_APERAK_CLARIFY(),

	// not documented
	EDI_QUEUE_THREAD_POOL_SIZE(INTEGER_POSITIVE),
	EDI_QUEUE_THREAD_POOL_SIZE_INVOIC(INTEGER_POSITIVE),
	EDI_QUEUE_THREAD_POOL_SIZE_INVOIC_PARTNERILN(INTEGER_POSITIVE, "1"),
	EDI_QUEUE_THREAD_TIMEOUT(INTEGER_POSITIVE),

	EDI_QUEUE_BATCH_SIZE(INTEGER_POSITIVE, "100"),

	EDI_OUTBOUND_IGNORED_INCIDENTS(new TypeListValidator().withType(SingleStringValidator.NUMERIC)),
	EDI_INBOUND_IGNORED_INCIDENTS(new TypeListValidator().withType(SingleStringValidator.NUMERIC)),

	// EDI PROCESSING END

	REMADV_REJECTED_CREDITORINVOICES_ALWAYS(BooleanValidator.TRUE_FALSE_NULL),

	REMADV_UNPAYED_REMINDER_DAYS(INTEGER_POSITIVE_ZERO, "7"),

	REMADV_REVERSAL_CHECK_BEHAVIOUR(buildFixedValueList("EXCEPTION", "ADAPT", "IGNORE"), "EXCEPTION"),

	DEBITORINVOICE_DUEDATE_OFFSET_SLP_DEBT(INTEGER_POSITIVE, "14"),

	DEBITORINVOICE_DUEDATE_OFFSET_SLP_CREDIT(INTEGER_POSITIVE, "14"),

	DEBITORINVOICE_DUEDATE_OFFSET_RLM_DEBT(INTEGER_POSITIVE, "14"),

	DEBITORINVOICE_DUEDATE_OFFSET_RLM_CREDIT(INTEGER_POSITIVE, "14"),

	DEBITORINVOICE_USE_PERIODEND_AS_POSTINGDATE(BooleanValidator.TRUE_FALSE_NULL),
	DEBITORINVOICE_GENERATE_INVOIC(BooleanValidator.TRUE_FALSE_NULL),
	DEBITORINVOICE_GENERATE_INVOIC_EMAIL(),
	DEBITORINVOICE_GENERATE_INVOIC_TYPE(),
	DEBITORINVOICE_GENERATE_INVOIC_IDENTIFIER(),
	DEBITORINVOICE_GENERATE_INVOIC_ADDITIONAL_COSTS_ARTICLE_NUMBER(DefaultValidator.getInstance(), "2220001000888"),

	DUNNING_PDF_PAYMENT_REMINDER(buildRegexValidator(RPT_DESIGN_NUMBER)),
	DUNNING_PDF_FIRST_LETTER(buildRegexValidator(RPT_DESIGN_NUMBER)),
	DUNNING_PDF_SECOND_LETTER(buildRegexValidator(RPT_DESIGN_NUMBER)),

	// used in financial
	AGGREGATED_ENTRIES_NUMBER(DefaultValidator.getInstance(), "AE[ffffff]{100000-200000}",
			AggregatedEntriesNumberGenerator.AGGREGATED_ENTRIES_NUMBER),
	// used in financial
	JOURNAL_AGGREGATION_TIME_CONFIG(buildFixedValueList("ALL", "ACCOUNTING_PERIOD", "POSTING_DATE"), null,
			JournalCreator.JOURNAL_AGGREGATION_TIME_CONFIG),

	REQOTE_MSB_INVOICING(BooleanValidator.TRUE_FALSE_NULL),

	DEBITOR_INVOICE_AUTOMATIC_REBATE_LOWER_LEVEL(buildDecimalNumbers()),
	DEBITOR_INVOICE_AUTOMATIC_REBATE_UPPER_LEVEL(buildDecimalNumbers()),

	DEBITOR_INVOICE_ALLOW_FALLBACK_GASFACTORS(BooleanValidator.TRUE_FALSE_NULL),

	CREDITOR_INVOICE_VALIDATION_QUEUE_THREAD_POOL_SIZE(INTEGER_POSITIVE),

	IMPORT_REGIONAL_PRICE_DISABLE_VALIDATION(BooleanValidator.TRUE_FALSE_NULL),
	@Deprecated // not used anymore
	OLD_MASTER_DATA_PROCESS(),

	TRADING_CERTIFICATE_SUBMIT_DEADLINE_OFFSET(INTEGER_POSITIVE),

	SALUTATION_PRIORITY_IN_REPORTING(new TypeListValidator().withType(SingleStringValidator.ALPHA)),

	CONTRACT_REVOCATION_DEADLINE_OFFSET(INTEGER_POSITIVE, "14"),

	CONTRACT_SUPPLY_BEGIN_DATE_VALUESOURCE(buildFixedValueList("ADDITIONAL1", "ADDITIONAL2", "ADDITIONAL3")),

	SUPPLY_BEGIN_DECLINED_REMINDER_INTERVAL(buildRegexValidator(ISO_8601), "P2D"),

	SUPPLY_BEGIN_DECLINED_REMINDER(INTEGER_POSITIVE_ZERO, "1"),

	CANCELLATION_DECLINED_REMINDER_INTERVAL(buildRegexValidator(ISO_8601), "P2D"),

	CANCELLATION_DECLINED_REMINDER(INTEGER_POSITIVE_ZERO, "1"),

	RATING_SCORE_CLASS_WHITELIST(buildRegexValidator(RATING_CLASS_LIST)),

	PORTAL_INFO_DISPLAY_IN_REPORTING_DOCUMENTS(BooleanValidator.TRUE_FALSE_NULL),

	REQUEST_GASFACTORS_OFFSET(INTEGER_NEGATIVE_ZERO, "0"),

	// these reminder properties are used in MeterReadingReminderForDebitorInvoice process
	METER_READING_WINDOW_REMINDER_MONTHLY_BEGIN(INTEGER_NEGATIVE_ZERO),
	METER_READING_WINDOW_REMINDER_MONTHLY_END(INTEGER_ANY),

	METER_READING_WINDOW_REMINDER_YEARLY_BEGIN(INTEGER_NEGATIVE_ZERO),
	METER_READING_WINDOW_REMINDER_YEARLY_END(INTEGER_ANY),

	METER_READING_WINDOW_REMINDER_FINAL_BEGIN(INTEGER_ANY),
	METER_READING_WINDOW_REMINDER_FINAL_END(INTEGER_ANY),

	METER_READING_MONTHLY_REMINDER1_INTERVAL_DAYS(INTEGER_POSITIVE_ZERO),
	METER_READING_MONTHLY_REMINDER2_INTERVAL_DAYS(INTEGER_POSITIVE_ZERO),
	METER_READING_MONTHLY_REMINDER3_INTERVAL_DAYS(buildIntegerPositiveOrNone()),

	METER_READING_YEARLY_REMINDER1_INTERVAL_DAYS(INTEGER_POSITIVE_ZERO),
	METER_READING_YEARLY_REMINDER2_INTERVAL_DAYS(INTEGER_POSITIVE_ZERO),
	METER_READING_YEARLY_REMINDER3_INTERVAL_DAYS(buildIntegerPositiveOrNone()),

	METER_READING_FINAL_REMINDER1_INTERVAL_DAYS(INTEGER_POSITIVE_ZERO),
	METER_READING_FINAL_REMINDER2_INTERVAL_DAYS(INTEGER_POSITIVE_ZERO),
	METER_READING_FINAL_REMINDER3_INTERVAL_DAYS(buildIntegerPositiveOrNone()),

	METER_READING_STARTEND_DUE_CALCULATION_FORMAT(new FixedValuesListValidator().withAcceptedValues("WT", "KT"), "WT"),

	FILE_CRAWLER_USE_ENCODINGS(buildRegexValidator(DETECT_OR_UTF_8), "detect"),

	CONTENT_SIZE_THRESHOLD_FOR_RUN_MONITORING(new RangeIntegerValidator(1, 100), "10"),

	CREDITOR_INVOIC_ACCEPT_DUPLICATE_PROCESSING(BooleanValidator.TRUE_FALSE_NULL),

	EDI_REFERENCE_NUMBER_PREFIX(SingleStringValidator.ALPHANUMERIC),
	EDI_TRANSACTION_NUMBER_PREFIX(SingleStringValidator.ALPHANUMERIC),
	EDI_DOCUMENT_NUMBER_PREFIX(SingleStringValidator.ALPHANUMERIC),

	PRICE_ADJUSTMENT_ACCEPT_CHANGEDATE_IN_PAST(BooleanValidator.TRUE_FALSE_NULL),

	REGIONAL_PRICES_DELETE_DATA_ON_IMPORT(BooleanValidator.TRUE_FALSE_NULL),

	PORTAL_PROVIDE_RLM_CONTRACT(BooleanValidator.TRUE_FALSE_NULL),
	//PortalRegisterUserApproachEnum
	PORTAL_REGISTER_USER_APPROACH(buildFixedValueList("MANUALLY_BY_USER", "AUTOMATICALLY_WITH_INITIAL_PASSWORD")),

	EDI_TESTGENERATOR_ROUTING(BooleanValidator.TRUE_FALSE_NULL),
	EDI_TESTGENERATOR_ROUTING_URL(SingleStringValidator.ALPHANUMERIC),

	SYSTEM_PROPERTIES(),
	MBS_LAST_START(),
	MBS_LAST_STOP(),

	DEBITOR_INVOICE_DO_NOT_GROUP_SERVICES(BooleanValidator.TRUE_FALSE_NULL),
	DEBITOR_INVOICE_DO_NOT_GROUP_EXPENSES(BooleanValidator.TRUE_FALSE_NULL),
	NO_DEBITOR_ASSIGNMENT(BooleanValidator.TRUE_FALSE_NULL),
	UPDATE_GRID_OPERATOR_PIVOT_DATE(buildGermanDate()),
	UPDATE_GRID_OPERATOR_BY_HISTORY_FROM_DATE(BooleanValidator.TRUE_FALSE_NULL),
	UPDATE_GRID_OPERATOR_BY_HISTORY_FROM_LAST_ENET(BooleanValidator.TRUE_FALSE_NULL),
	UPDATE_GRID_OPERATOR_BY_PROGRESSION_FROM_DATE(BooleanValidator.TRUE_FALSE_NULL),
	UPDATE_GRID_OPERATOR_BY_PROGRESSION_FROM_LAST_ENET(BooleanValidator.TRUE_FALSE_NULL),

	FORCE_MAINTENANCE_PROCESS(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	SKIP_MAINTENANCE_PROCESS(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	MIGRATE_TLP_DATA_FROM_EDI(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),
	MIGRATE_TLP_DATA_FROM_AEP(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),
	MAXIMUM_DAYS_INVOICE_ALLOWED_IN_FUTURE(INTEGER_POSITIVE, "14"),

	KILL_METERREADING_REQUESTS(BooleanValidator.TRUE_FALSE_NULL),
	JOURNAL_TEMPORARY_FOLDERPATH(WriteDirectoryValidator.getInstance()),
	FILE_CRAWLER_PLZ_BONUS_IMPORT_PATH(),
	FILE_CRAWLER_PLZ_BONUS_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),

	DELETE_OLD_BONUS_CONFIGURATION(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),

	PROGNOSIS_ACCEPT_VNB_RESPONSE_LOWER_BOUND_PERCENTAGE(INTEGER_POSITIVE_ZERO, "10"),
	PROGNOSIS_ACCEPT_VNB_RESPONSE_UPPER_BOUND_PERCENTAGE(INTEGER_POSITIVE_ZERO, "50"),
	PROGNOSIS_SEND_TO_VNB_LOWER_BOUND_PERCENTAGE(INTEGER_POSITIVE_ZERO, "10"),
	PROGNOSIS_SEND_TO_VNB_UPPER_BOUND_PERCENTAGE(INTEGER_POSITIVE_ZERO, "50"),

	DUPLICATE_CONTRACT_LOCKED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	REMADV_COMDIS_REASON_TEXT(DefaultValidator.getInstance(), "Erneute Ablehnung entsprechend der bilateralen Klärung."),

	INVOICE_TRIGGER_DEFAULT(buildFixedValueList("AUTOMATIC", "MANUAL"), "AUTOMATIC"), // default value used from InvoiceTriggerEnum
	ADD_UNB_AS_CONTRACTPARTNER(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	FINISH_TASKS_SERVLET_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()), // see FinishTasksAutoStart
	FINISH_TASKS_SERVLET_INFO(DefaultValidator.getInstance(), ""), // see FinishTasksAutoStart
	FINISH_TASKS_SERVLET_LAST_RUN_START(),
	FINISH_TASKS_SERVLET_LAST_RUN_END(),

	CANCEL_TASKS_SERVLET_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()), // see CancelTaskServlet
	CANCEL_TASKS_SERVLET_INFO(DefaultValidator.getInstance(), ""), // see CancelTaskServlet
	CANCEL_TASKS_SERVLET_LAST_RUN_START(),
	CANCEL_TASKS_SERVLET_LAST_RUN_END(),

	APPLICATION_HEADER_COLOR(buildRegexValidator(HEX_CODE)), //used from ui only
	ADD_ITEMS_TO_RESULTS_PER_PAGE(new TypeListValidator().withType(INTEGER_ANY)), //used from ui only
	APPLICATION_HEADER_NAME(SingleStringValidator.ALPHANUMERIC), //used from ui only
	B2B_IP_PORT_EXTERN(), //used from ui only
	REACTIVATE_SUPPLY_BEGIN(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	REACTIVATE_SUPPLY_BEGIN_IDS(new TypeListValidator().withType(SingleStringValidator.ALPHANUMERIC)),
	GRID_COST_LIMIT_GAS(),
	GRID_GAS_FALLBACK_CALORIFIC_VALUE(INTEGER_POSITIVE, "10"),
	GRID_GAS_FALLBACK_CORRECTION_FACTOR(INTEGER_POSITIVE, "1"),
	DISABLE_CUSTOMER_NOTIFICATION_CANCELLATION(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	DISABLE_CUSTOMER_UI_CLOSEDOWN_DECOMMISSIONING(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	ALLOWED_MASTERPAGE_VERSION_PATTERN(),
	ALLOWED_MASTERPAGE_VERSION(),
	ALLOWED_REPORTS_VERSION_PATTERN(),
	ALLOWED_REPORTS_VERSION(),
	METERNUMBER_VALIDATE_REGEX(DefaultValidator.getInstance(), "([0-9a-zA-Z]|[äöüÄÖÜ]|-){1,70}"),

	ACTIVITI_MIGRATION_AUTOSTART_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	ACTIVITI_MIGRATION_SERVLET_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	ACTIVITI_MIGRATION_VALUES(DefaultValidator.getInstance(), ""),
	FINANCIALACCOUNTS_FOR_TRANSIT_ASSIGNMENT(new TypeListValidator().withType(INTEGER_ANY)),

	UPDATE_MARKET_AREA_2021_SEPTEMBER_CONTRACTS_AUTOSTART_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	CANCELLATION_DECLINED_CLP_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	CANCELLATION_DECLINED_CLP_TOKEN_TIME_TO_LIVE(INTEGER_POSITIVE, "48"),
	CANCELLATION_DECLINED_CLP_REMINDER_MAX_REPETITION(INTEGER_POSITIVE, "2"),
	CANCELLATION_DECLINED_CLP_REMINDER_WAIT_DELAY(buildRegexValidator(ISO_8601), "PT48H"),
	CANCELLATION_DECLINED_CLP_CREATE_TASK_MAX_REPETITIONS_REACHED(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),
	CANCELLATION_DECLINED_CLP_RETRIES(INTEGER_POSITIVE_ZERO, "2"),
	CANCELLATION_DECLINED_CLP_CREATE_TASK_MAX_RETRIES_REACHED(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),

	CANCELLATION_DECLINED_MULTIPLE_CANCELLATIONS_LOCK_CONTRACT(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	PERMISSION_CONFIGURATION_EXPORT_PATH(WriteDirectoryValidator.getInstance()),

	ACTIVITI_TERMINATION_SERVICE_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	ACTIVITI_TERMINATION_SERVICE_VALUES(DefaultValidator.getInstance(), ""),

	DATABASE_INITIALIZED(BooleanValidator.TRUE_FALSE_NULL),

	SUPPLY_BEGIN_DECLINED_SUPPLY_BEGIN_IN_PROGRESS_RETRY(BooleanValidator.TRUE_FALSE, Boolean.FALSE.toString()),
	SUPPLY_BEGIN_DECLINED_SUPPLY_BEGIN_IN_PROGRESS_CREATE_TASK(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),

	SUPPLY_BEGIN_EOG_MAX_PASSED_DAYS(INTEGER_POSITIVE_ZERO, "30"),
	SUPPLY_BEGIN_EOG_SKIP_BASE_SUPPLIER_CHECK(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	// SAP_XXX Properties are only used for internal showcase and are not documented
	SAP_ENABLE(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	SAP_NO_AUTH(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	SAP_USER(),
	SAP_PASSWORD(),
	SAP_URL(),

	REMADV_EXECUTION_WEEKDAYS(WeekDay.createValueValidator()),
	REMADV_COLLECT_INVOICS_DAYS_AHEAD(INTEGER_POSITIVE_ZERO, "8"),
	REMADV_INVOICS_PAYMENTDATE_DAYS_IN_FUTURE(INTEGER_POSITIVE_ZERO, "3"),

	ACCOUNTING_CHARACTERISTIC_BY_ILN(new AccountingCharacteristicByILNValidator()),

	IST_VERSTEUERUNG_ON(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	DEBIT_ORDER_CREATION_COVERED_VALUE_CONDITION(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	DEBIT_ORDER_CREATION_INVOICE_ENTRY_CONDITION(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	DEBIT_ORDER_CREATION_POSTING_DATE_CONDITION_FROM(DateTimeValidator.buildDateReverse()),
	DEBIT_ORDER_CREATION_POSTING_DATE_CONDITION_TO(DateTimeValidator.buildDateReverse()),
	GLOBAL_PROPERTIES_DOCUMENTATION_URL(DefaultValidator.getInstance(), "https://mbs.next-level-help.org/mbs_customizing_gp.html"),

	UPDATE_MARKET_AREA_SERVICE_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	UPDATE_MARKET_AREA_SERVICE_VALUES(DefaultValidator.getInstance(), ""),
	UPDATE_MARKET_AREA_SERVICE_LAST_RUN_START(),
	UPDATE_MARKET_AREA_SERVICE_LAST_RUN_END(),
	UPDATE_MARKET_AREA_SERVICE_PIVOT_DATE(buildGermanDate(), "01.07.2021"),

	MARKETLOCATION_ADDRESS_VALIDATION(buildFixedValueList("DEFAULT", "VNB", "BILATERAL"), "DEFAULT"),

	METER_READING_ESTIMATION_YEAR(new RangeIntegerValidator(0, 2999)),
	DEBIT_POSITION_CREATION_DAYS_IN_ADVANCE(INTEGER_POSITIVE_ZERO, "5"),
	REMADV_NUMBER_RANGE(DefaultValidator.getInstance(), ""),

	AEP_AUTHORIZATION_SERVER_URL(DefaultValidator.getInstance(), "NOT_CONFIGURED", ENVIRONMENT_OR_DATABASE),
	AEP_AUTHORIZATION_JWT_URL_PATTERN(DefaultValidator.getInstance(), "/**/sso/**;/**/oauth/**"),
	AEP_CORS_WHITELIST_SERVER_URLS(),
	PASSWORD_ENCODER(),
	EEG_APPORTIONMENT_COST_VALUE(new ChainValidator().withOperator(ChainValidator.Operator.OR).add(SingleStringValidator.EMPTY)
			.add(DECIMAL_POSITIVE_ZERO)),
	CREATE_DEBITOR_INVOICE_ITEM_ZERO_CONSUMPTION_BASED(new TypeListValidator().withType(SingleStringValidator.NUMERIC)),

	UPDATE_WRONG_ACCOUNTING_GRID_SERVICE_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	UPDATE_WRONG_ACCOUNTING_GRID_UPDATE_OLD_VALUES(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	UPDATE_WRONG_ACCOUNTING_GRID_SERVICE_LAST_RUN_START(),
	UPDATE_WRONG_ACCOUNTING_GRID_SERVICE_LAST_RUN_END(),
	UPDATE_WRONG_ACCOUNTING_GRID_AEP_MAKO_CLOUD_SERVICE_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	UPDATE_WRONG_ACCOUNTING_GRID_AEP_MAKO_CLOUD_UPDATE_OLD_VALUES(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	UPDATE_WRONG_ACCOUNTING_GRID_AEP_MAKO_CLOUD_SERVICE_LAST_RUN_START(),
	UPDATE_WRONG_ACCOUNTING_GRID_AEP_MAKO_CLOUD_SERVICE_LAST_RUN_END(),
	SPRING_SECURITY_AUTOSTART_CHECK_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),
	SPRING_SECURITY_AUTOSTART_TEST_HTTP_REQUEST_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.TRUE.toString()),

	FIX_METER_READING_DATES_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	FIX_METER_READING_DATES_LAST_RUN_START(),
	FIX_METER_READING_DATES_LAST_RUN_END(),
	USE_AEP_MAKO_CLOUD(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),

	METER_READING_PLAUSIBILITY_TASK_CREATION(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),

	MAKO1022_CHECK_ONETIME_PROCESS_EXECUTED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	MAINTENANCE_TASK_REVERT_ACCOUNTING_DOCUMENTS_FILE_PATH(),

	DEBITOR_INVOICE_COMPARE_RELIEF_AMOUNT_TO_COSTS_SUPPRESSED(),

	RELIEF_PERIOD_END(buildGermanDate(), "31.12.2023"),

	FILE_CRAWLER_REGIONAL_PRICES_IMPORT_PATH(),
	FILE_CRAWLER_REGIONAL_PRICES_INTERVAL(new RangeIntegerValidator(5000, Integer.MAX_VALUE), "5000"),

	RELIEF_CSV_EXPORT_PATH_GAS(),
	RELIEF_CSV_EXPORT_PATH_ELECTRICITY(),

	RELIEF_CSV_EXPORT_MIN_AGE(INTEGER_POSITIVE_ZERO, "14"),

	AEP_MAKO_MIGRATION_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	AEP_MAKO_MIGRATION_SERVICE_LAST_RUN_START(),
	AEP_MAKO_MIGRATION_SERVICE_LAST_RUN_END(),

	DISTRIBUTION_PARTNER_CONTRACT_CONFIRMATION_IN_WAITING(),
	AEP_MAKO_HEALTH_CRON_EXPRESSION(buildRegexValidator(CRON_EXPRESSION_6), "0/5 * * * * ?"),
	AEP_MAKO_VALIDATE_OUTGOING_MESSAGES_INCLUDE(DefaultValidator.getInstance()),
	AEP_MAKO_VALIDATE_OUTGOING_MESSAGES_EXCLUDE(DefaultValidator.getInstance()),
	AEP_MAKO_DISABLE_WRITE_WITH_JSON_VIEW(BooleanValidator.TRUE_FALSE_NULL),
	CLEARING_CHAIN_SECONDARY_CLAIM_ACCOUNT_TYPES_WHITE_LIST(new TypeListValidator().withType(INTEGER_POSITIVE), "11,12,13"),
	DEBITOR_INVOICE_DEBIT_POSITION_VALIDATION_EXCEPTION_SUPPRESSED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	ACCEPT_SUPPLY_END_REQUEST_E01_BY_ILN(),
	INTERIM_METER_READING_VALID_NEXT_DAY(BooleanValidator.TRUE_FALSE, Boolean.TRUE.toString()),
	TARIFF_IDS_FOR_MIGRATION_CONTRACTS_TO_TLP,
	BUSINESS_PARTNER_CONTRACTS_FOR_REATTACHING(new TypeListValidator()),
	DEBITOR_INVOICE_CREATION_FOR_DYNAMIC_COSTS_OFFSET_IN_BUSINESS_DAYS(INTEGER_POSITIVE_ZERO, "10"),

	RESEND_REQOTE_FOR_REJECTED_QUOTES_PROCESS_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	RESEND_REQOTE_FOR_REJECTED_QUOTES_PROCESS_START(),
	RESEND_REQOTE_FOR_REJECTED_QUOTES_PROCESS_END(),
	BUSINESS_DAYS_FOR_ORDERS_PROGNOSIS_DUE_DATE(INTEGER_NEGATIVE, "-26"),
	TERMINATE_DUPLICATE_MASTER_DATA_SYNCHRO_MESSAGES_AUTOSTART_ENABLED(BooleanValidator.TRUE_FALSE_NULL, Boolean.FALSE.toString()),
	TARIFF_CHANGE_MAX_MONTHS_IN_FUTURE(INTEGER_POSITIVE_ZERO, "9");

	private static final Logger LOGGER = LogManager.getLogger();
	private final String defaultValue;
	private final String key;
	private final ValueValidator validatorType;
	private final GlobalPropertySource source;

	GlobalPropertiesEnum() {
		this.validatorType = DefaultValidator.getInstance();
		this.defaultValue = null;
		this.key = this.name();
		this.source = GlobalPropertySource.DATABASE;
	}

	GlobalPropertiesEnum(final ValueValidator validatorType) {
		this.validatorType = validatorType;
		this.defaultValue = null;
		this.key = this.name();
		this.source = GlobalPropertySource.DATABASE;
	}

	GlobalPropertiesEnum(GlobalPropertySource source) {
		this.validatorType = DefaultValidator.getInstance();
		this.defaultValue = null;
		this.key = this.name();
		this.source = source;
	}

	GlobalPropertiesEnum(final ValueValidator validatorType, final String defaultValue) {
		this.validatorType = validatorType;
		this.defaultValue = defaultValue;
		this.key = this.name();
		this.source = GlobalPropertySource.DATABASE;
	}

	GlobalPropertiesEnum(final ValueValidator validatorTypefinal, String defaultValue, GlobalPropertySource source) {
		this.validatorType = validatorTypefinal;
		this.defaultValue = defaultValue;
		this.key = this.name();
		this.source = source;
	}

	GlobalPropertiesEnum(final ValueValidator validatorType, final String defaultValue, final String key) {
		this.validatorType = validatorType;
		this.defaultValue = defaultValue;
		this.key = key;
		this.source = GlobalPropertySource.DATABASE;
	}

	GlobalPropertiesEnum(final ValueValidator validatorType, final String defaultValue, final String key, GlobalPropertySource source) {
		this.validatorType = validatorType;
		this.defaultValue = defaultValue;
		this.key = key;
		this.source = source;
	}

	GlobalPropertiesEnum(ValueValidator validatorType, GlobalPropertySource source) {
		this.validatorType = validatorType;
		this.defaultValue = null;
		this.key = this.name();
		this.source = source;

	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public String getKey() {
		return this.key;
	}

	public GlobalPropertySource getSource() {
		return this.source;
	}

	public String getValue() {
		return getValue(false);
	}

	public boolean isSet() {
		return StringUtils.isNotBlank(getValue(false));
	}

	public ValueValidator getValidatorType() {
		return this.validatorType;
	}

	public String getValue(boolean forceExceptionIfEmpty) {
		String result = getValueFromRightSource(getKey());
		if (result == null) {
			if (forceExceptionIfEmpty) {
				throw new IllegalArgumentException("Global Property with key=" + this.key + " is not set (value=null).");
			} else {
				return getDefaultValue();
			}
		}
		return result;
	}

	public String getValue(String defaultValue) {
		String result = getValueFromRightSource(getKey());
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	private String getValueFromRightSource(String globalPropertyKey) {
		if (source == ENVIRONMENT_OR_DATABASE) {
			String valueFromSystemEnv = System.getenv(globalPropertyKey);
			if (StringUtils.isNotEmpty(valueFromSystemEnv)) {
				return valueFromSystemEnv;
			}
		}
		return GlobalPropertyRegistry.getInstance().getGlobalPropertyValue(globalPropertyKey);
	}

	public Integer getValueAsInteger(Integer fallback) {
		try {
			Integer result = getValueAsInteger();
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			// NOOP
		}
		return fallback;
	}

	public Integer getValueAsInteger() {
		String value = this.getValue();
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				handleParseError(this.key, value, e);
			}
		}
		return null;
	}

	public Double getValueAsDouble(Double fallback) {
		try {
			Double result = getValueAsDouble();
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			// NOOP
		}
		return fallback;
	}

	public Double getValueAsDouble() {
		String value = this.getValue();
		if (value != null) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				handleParseError(this.key, value, e);
			}
		}
		return null;
	}

	public Date getValueAsDate(SimpleDateFormat format) {
		Validate.notNull(format, "Given parameter 'format' cannot be NULL");
		String value = this.getValue();
		if (value != null) {
			try {
				return format.parse(value);
			} catch (Exception e) {
				handleParseError(this.key, value, e);
			}
		}
		return null;
	}

	public List<String> getValueAsSeparatedList(String delimiter) {
		Validate.notNull(delimiter, "Given parameter 'delimiter' cannot be NULL, should be something like ',' or ';'");
		final List<String> results = new ArrayList<>();
		String globalPropertyValue = this.getValue();
		if (globalPropertyValue != null) {
			final String[] strings = globalPropertyValue.split("\\s*" + delimiter + "\\s*");
			for (String value : strings) {
				if (!value.isEmpty()) {
					results.add(value);
				}
			}
		}
		return results;
	}

	public Boolean getValueAsBoolean() {
		return GlobalPropertyRegistry.isGlobalPropertyBooleanValueTrue(this.getValue());
	}

	public BigDecimal getValueAsBigDecimal() {
		return GlobalPropertyRegistry.getInstance().getGlobalPropertyBigDecimalOrNull(this.getKey());
	}

	public BigDecimal getValueAsBigDecimal(BigDecimal fallback) {
		return GlobalPropertyRegistry.getInstance().getGlobalPropertyBigDecimalWithFallback(this.getKey(), fallback);
	}

	public boolean isKeyEqualTo(String key) {
		return this.key.equalsIgnoreCase(key);
	}

	private void handleParseError(String key, String readValue, Exception e) throws IllegalArgumentException {
		final String msg = "Cannot parse given value=" + readValue + " for GP=" + key;
		LOGGER.error(msg);
		throw new IllegalArgumentException(msg, e);
	}

	public static GlobalPropertiesEnum lookup(String key) {
		return lookup(key, false);
	}

	public static GlobalPropertiesEnum lookup(String key, boolean forceExceptionIfUnknown) {
		Stream<GlobalPropertiesEnum> stream = Arrays.stream(values());
		GlobalPropertiesEnum result = stream
				.filter(item -> item.isKeyEqualTo(key))
				.findAny()
				.orElse(null);
		if (result == null && forceExceptionIfUnknown) {
			throw new IllegalArgumentException("Unknown property with key=" + key);
		}
		return result;
	}

	public static String lookupValue(String key, boolean forceExceptionIfUnknown) {
		GlobalPropertiesEnum result = lookup(key, forceExceptionIfUnknown);
		if (result != null) {
			return result.getValue();
		}
		return null;
	}
}
