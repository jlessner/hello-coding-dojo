/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.myBusinessSupplier.maintenance;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class CleanUpMaintenanceTask implements MaintenanceTask {

    protected static final Logger LOG = LogManager.getLogger(CleanUpMaintenanceTask.class);

    protected int batchCount;

    @Override
    public void setup() throws MaintenanceTaskSetupFailedException {
        this.batchCount = 0;
    }

    /*
        Remember: we do not want simply delete-by-condition
        but select-by-delete-condition and then delete-by-id to integrate batching
     */
    @Override
    public void processNextEntry() throws MaintenanceEntryProcessingFailedException {
        final List<Long> delete;
        try {
           delete = selectValuesToDelete();
        } catch (Exception e) {
            LOG.error(String.format("error while selection of IDs to delete in %s",
                    getIdentifier()), e);
            throw new MaintenanceEntryProcessingFailedException(getIdentifier() + " (select)", e);
        }
        try {
            int deleted = 0;
            if (CollectionUtils.isNotEmpty(delete)) {
                // WARN there is a max-number of IDs in an IN-clause: it must never be larger than ~36.000
                deleted = deleteValuesByIds(delete);
                this.batchCount++;
            }
            LOG.info(String.format("(%s / %s) Found %s values to delete, actual result is %s",
                    getIdentifier(), this.batchCount, delete.size(), deleted));
        } catch (Exception e) {
            LOG.error(String.format("error while deleting of IDs in %s",
                    getIdentifier()), e);
            throw new MaintenanceEntryProcessingFailedException(getIdentifier() + " (delete)", e);
        }
    }

    @Override
    public boolean hasNextEntry() {
        try {
            final long count = countValuesToDelete();
            LOG.info(String.format("(%s / %s) remaining values to delete %s",
                    getIdentifier(), this.batchCount, count));
            return count > 0L;
        } catch (Exception e) {
            LOG.error(String.format("error while trying to determine count, skip execution of this clean-up step: %s",
                    getIdentifier()), e);
            return false;
        }
    }

    @Override
    public int batchSize() {
        // execute commit everytime processNextEntry is called
        return 1;
    }

    @Override
    public boolean repeatTask() {
        return true;
    }

    protected abstract Long countValuesToDelete() throws Exception;

    protected abstract List<Long> selectValuesToDelete() throws Exception;

    protected abstract int deleteValuesByIds(final List<Long> ids) throws Exception;

    protected abstract String getIdentifier();
}
