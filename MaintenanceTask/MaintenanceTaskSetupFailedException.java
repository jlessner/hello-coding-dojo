/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.myBusinessSupplier.maintenance;

public class MaintenanceTaskSetupFailedException extends Exception {

	public MaintenanceTaskSetupFailedException(final MaintenanceTask failedTask, final Throwable cause) {
		super("setup failed for task " + failedTask.getClass().getSimpleName() + " with cause '" + cause.getMessage() + "'", cause);
	}
}
