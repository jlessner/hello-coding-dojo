/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenancetask;

import static com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum.MAINTENANCE_TASK_REVERT_ACCOUNTING_DOCUMENTS_FILE_PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.nextlevel.fastlane.financial.bo.AccountingDocument;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.bpm.core.StackTraceToString;
import com.nextlevel.platform.financial.PersistenceHandlerFinancial;
import com.nextlevel.platform.financial.accountingdocuments.AccountingDocumentReverter;

/**
 * 1) MAINTENANCE_TASK_REVERT_ACCOUNTING_DOCUMENTS_FILE_PATH must be set (not null or empty)
 * 2) Content of MAINTENANCE_TASK_REVERT_ACCOUNTING_DOCUMENTS_FILE_PATH must be readable file (assuming complete path including filename)
 * 3) Per Line of this file, we assume one ID of any AccountingDocument that is not reverted (IDs are given one per Line, no comma/semicolons...)
 * 4) there will be NO further validations
 * 5) Each found ID will be loaded and AccountingDocumentReverter will be called
 * 6) Exceptions thrown by AccountingDocumentReverter will be caught
 * (!) You must be really, really sure that you want to use this step
 */
public class RevertAccountingDocumentsMaintenanceTask implements MaintenanceTask {

	private static final Logger LOG = LogManager.getLogger(RevertAccountingDocumentsMaintenanceTask.class);
	private static final PersistenceHandlerFinancial persistenceHandlerFinancial = PersistenceHandlerFinancial.getInstance();

	/*package internal for test laziness*/ Iterator<Long> accountingDocumentsToRevert;

	@Override
	public void setup() throws MaintenanceTaskSetupFailedException {
		this.accountingDocumentsToRevert = readIDsFromFile().iterator();
	}

	@Override
	public void processNextEntry() throws MaintenanceEntryProcessingFailedException {
		if (this.accountingDocumentsToRevert.hasNext()) {
			final Long currentId = this.accountingDocumentsToRevert.next();
			final Optional<AccountingDocument> currentOpt = tryLoadNonRevertedDocument(currentId);
			if (currentOpt.isPresent()) {
				final AccountingDocument current = currentOpt.get();
				try {
					// execute() create financialContext transaction
					new AccountingDocumentReverter(current).execute();
					logSuccess(current);
				} catch (Exception e) {
					logException(current, e);
					throw new MaintenanceEntryProcessingFailedException(createIdentifier(current), e);
				}
			} else {
				logSkipped(currentId);
			}
		}
	}

	@Override
	public boolean hasNextEntry() {
		if (this.accountingDocumentsToRevert != null) {
			return this.accountingDocumentsToRevert.hasNext();
		}
		return false;
	}

	private List<Long> readIDsFromFile() throws MaintenanceTaskSetupFailedException {
		final List<Long> result = new ArrayList<>();
		final String configuredFilePath = MAINTENANCE_TASK_REVERT_ACCOUNTING_DOCUMENTS_FILE_PATH.getValue();
		if (StringUtils.isNotEmpty(configuredFilePath)) {
			final List<String> allLines;
			try {
				final Path path = Paths.get(configuredFilePath);
				LOG.info(String.format("Try to read from path: %s", path.toAbsolutePath()));
				allLines = Files.readAllLines(path);
			} catch (Exception e) {
				LOG.error(String.format("Could not read configuration from given file %s \n please check GP %s ",
						configuredFilePath, MAINTENANCE_TASK_REVERT_ACCOUNTING_DOCUMENTS_FILE_PATH.getKey()), e);
				throw new MaintenanceTaskSetupFailedException(this, e);
			}
			if (CollectionUtils.isNotEmpty(allLines)) {
				for (String currentLine : allLines) {
					try {
						final Long parsed = Long.valueOf(currentLine);
						result.add(parsed);
					} catch (Exception e) {
						LOG.error(String.format("invalid NumberFormat of line in configured file %s \n please check this value %s",
								configuredFilePath, currentLine), e);
						throw new MaintenanceTaskSetupFailedException(this, e);
					}
				}
			}
		}

		return result.stream().distinct().collect(Collectors.toList());
	}

	private Optional<AccountingDocument> tryLoadNonRevertedDocument(final Long id) {
		try {
			final AccountingDocument loaded = persistenceHandlerFinancial.loadAccountingDocument(id);
			final boolean isReverted = Boolean.TRUE.equals(loaded.getIsReverted());
			if (!isReverted) {
				return Optional.of(loaded);
			}
		} catch (Exception e) {
			LOG.warn(String.format("Could not load AccountingDocument %s due to exception %s (first 400 chars)",
					id, stripException(e)));
		}

		return Optional.empty();
	}

	private static void logSuccess(final AccountingDocument accountingDocument) {
		LOG.info(String.format("MaintenanceProcess successfully reverted %s", createIdentifier(accountingDocument)));
	}

	private static void logException(final AccountingDocument accountingDocument, final Exception e) {
		LOG.warn(String.format("MaintenanceProcess could not revert %s due to exception (first 400 chars) %s",
				createIdentifier(accountingDocument), stripException(e)));
	}

	private static void logSkipped(final Long id) {
		LOG.warn(String.format("MaintenanceProcess skipped given id %s: it is either unknown or already reverted", id));
	}

	private static String createIdentifier(final AccountingDocument accountingDocument) {
		return String.format("AccountingDocument %s with Subject %s",
				accountingDocument.getId(), accountingDocument.getSubject());
	}

	private static String stripException(final Exception e) {
		return StackTraceToString.toString(e, 400);
	}
}
