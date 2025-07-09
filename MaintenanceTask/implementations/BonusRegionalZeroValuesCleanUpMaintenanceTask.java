/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenancetask;

import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.BonusRegionalValueDAO;
import com.nextlevel.myBusinessSupplier.maintenance.CleanUpMaintenanceTask;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.connection.sql.QueryBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BonusRegionalZeroValuesCleanUpMaintenanceTask extends CleanUpMaintenanceTask {

    private static final int INTERNAL_BATCH_SIZE = 25000;

    private static final String SQL_WHERE_PART =
            " WHERE " + BonusRegionalValueDAO.column_value + " = ? "
            + " AND " + BonusRegionalValueDAO.column_periodEnd + " IS NOT NULL ";

    private static final String SQL_SELECT_VALUES_TO_DELETE =
            "SELECT " + BonusRegionalValueDAO.column_id + " FROM " + BonusRegionalValueDAO.TABLE_NAME
                + SQL_WHERE_PART;

    @Override
    protected List<Long> selectValuesToDelete() throws Exception {
        final List<Long> result = new ArrayList<>();

        CommandProcessor.process(c -> {
            // Select only the first xxx-thousand values
            QueryBuilder queryBuilder = new QueryBuilder(SQL_SELECT_VALUES_TO_DELETE, QueryBuilder.Operator.AND);
            PreparedStatement stmt = queryBuilder.build(c, INTERNAL_BATCH_SIZE, 0);
            stmt.setBigDecimal(1, BigDecimal.ZERO);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                result.add(rs.getLong(1));
            }
            return null;
        });

        return result;
    }

    private static final String SQL_DELETE_VALUES_BY_ID =
            "DELETE FROM " + BonusRegionalValueDAO.TABLE_NAME
            + " WHERE " + BonusRegionalValueDAO.column_id + " IN ";
            // in clause is programmatically added

    @Override
    protected int deleteValuesByIds(final List<Long> ids) throws Exception {
        final String idsList = "(" + StringUtils.join(ids, ",") + ")";
        final String fullSql = SQL_DELETE_VALUES_BY_ID + idsList;

        return CommandProcessor.process(c -> {
            PreparedStatement stmt = c.prepareStatement(fullSql);
            return stmt.executeUpdate();
        });
    }

    private static final String SQL_COUNT_VALUES_TO_DELETE =
            "SELECT count(*) FROM " + BonusRegionalValueDAO.TABLE_NAME
                    + SQL_WHERE_PART;

    @Override
    protected Long countValuesToDelete() throws Exception {
        return CommandProcessor.process(c -> {
            PreparedStatement stmt = c.prepareStatement(SQL_COUNT_VALUES_TO_DELETE);
            stmt.setBigDecimal(1, BigDecimal.ZERO);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        });
    }

    @Override
    protected String getIdentifier() {
        return this.getClass().getSimpleName();
    }
}
