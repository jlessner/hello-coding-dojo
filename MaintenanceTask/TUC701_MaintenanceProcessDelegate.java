/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.aepmako.maintenance;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TUC701_MaintenanceProcessDelegate extends UC701_MaintenanceProcessDelegate {

    private String logMessage = null;

    protected void writeErrorToSystemLog(String message) {
        logMessage = message;
    }


    void assertLogContains(String expectedMessage) {
        assertTrue(logMessage.contains(expectedMessage));
    }

    void assertNoLog() {
        assertNull(logMessage);
    }

}
