/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.myBusinessSupplier.maintenance;

public class MaintenanceEntryProcessingFailedException extends Exception {

	private final String entryIdentifier;

	public MaintenanceEntryProcessingFailedException(final String entryIdentifier, final Throwable cause) {
		super(cause);
		this.entryIdentifier = entryIdentifier;
	}

	public String getEntryIdentifier() {
		return this.entryIdentifier;
	}
}
