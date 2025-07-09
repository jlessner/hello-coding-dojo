/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.aepmako.maintenance;

import com.nextlevel.platform.date.DateTool;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public class UC701_MaintenanceProcessDelegate implements JavaDelegate {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            checkMaintenanceWindow();
            queryAllUndoneConfigurations();
            executeConfigurations();
        }
        catch (MaintenanceWindowViolatedException m) {
            writeErrorToSystemLog(m.getMessage());
        }

    }

    protected void writeErrorToSystemLog(String message) {
        LOGGER.error(message);
    }

    private void executeConfigurations() {
    }

    private void queryAllUndoneConfigurations() {
    }

    private void checkMaintenanceWindow() throws MaintenanceWindowViolatedException {

        Calendar now = DateTool.getCurrentCalendar();

        if (isWeekend(now) || isMaintenanceHour(now)) {
            return;
        }

        throw new MaintenanceWindowViolatedException();
    }

    private boolean isWeekend(Calendar calendar) {
        return calendar.get(DAY_OF_WEEK) == SATURDAY
                || calendar.get(DAY_OF_WEEK) == SUNDAY;
    }

    private boolean isMaintenanceHour(final Calendar calendar) {
        int hour = calendar.get(HOUR_OF_DAY);
        return hour < 7 || hour >= 20;
    }
}
