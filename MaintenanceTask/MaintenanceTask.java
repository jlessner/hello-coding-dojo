/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.myBusinessSupplier.maintenance;

public interface MaintenanceTask {

	void setup() throws MaintenanceTaskSetupFailedException;

	void processNextEntry() throws MaintenanceEntryProcessingFailedException;

	boolean hasNextEntry();

	default boolean repeatTask() {
		return false;
	}

	default void cleanUp() {
		// do nothing//
	}

	default int batchSize() {
		return 0;
	}
}
