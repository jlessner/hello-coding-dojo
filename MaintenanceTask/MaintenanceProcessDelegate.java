/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenance;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nextlevel.fastlane.configuration.GlobalPropertiesAdmin;
import com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum;
import com.nextlevel.platform.configuration.GlobalProperty;

/**
 * executes implementations of MaintenanceTasks between 20 and 7 o Clock and on weekends
 * If Job is done, SKIP_MAINTENANCE_PROCESS = true is set, otherwise setup is executed each day.
 * If MaintenanceProcess should be reused, set SKIP_MAINTENANCE_PROCESS = false with liquibase
 *
 * @deprecated remove with release 145
 */
@Deprecated
public class MaintenanceProcessDelegate implements JavaDelegate {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		LOGGER.info("Starting execution of MaintenanceProcess.");
		setGlobalPropertyToTrue();
		LOGGER.info("End of execution of MaintenanceProcess.");

	}

	private void setGlobalPropertyToTrue() {
		try {
			final GlobalPropertiesAdmin globalPropertiesAdmin = new GlobalPropertiesAdmin();
			final GlobalProperty globalProperty = new GlobalProperty();
			globalProperty.setPropertyKey(GlobalPropertiesEnum.SKIP_MAINTENANCE_PROCESS.getKey());
			globalProperty.setVal("true");
			globalPropertiesAdmin.saveWithLogEntry(globalProperty);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
