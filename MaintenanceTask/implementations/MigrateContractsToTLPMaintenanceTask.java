/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenancetask;

import static com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum.TARIFF_IDS_FOR_MIGRATION_CONTRACTS_TO_TLP;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import com.nextlevel.fastlane.configuration.GlobalPropertiesAdmin;
import com.nextlevel.fastlane.core.persistence.dao.PersistMode;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ContractTypeConfiguration;
import com.nextlevel.fastlane.myBusinessSupplier.enums.ContractTypeEnum;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.ContractDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.TariffApplicationDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.ContractPVO;
import com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.configuration.GlobalProperty;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.financial.timeslice.TimeSliceTool;

// @formatter:off

public class MigrateContractsToTLPMaintenanceTask implements MaintenanceTask {

    private static final Logger LOG = LogManager.getLogger(MigrateContractsToTLPMaintenanceTask.class);

    private static final PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

    private final ContractDAO contractDAO = new ContractDAO();

    private Iterator<ContractPVO> contracts;

    @Override
    public void setup() throws MaintenanceTaskSetupFailedException {
       final List<ContractPVO> result;

        try {
            result = selectRelevantContracts();
            /*
                it is not ideal, but we need to check the following conditions that are not easily expressable in SQL

                1) if #ContractTypeConfiguration == 1 AND type != TLP -> migrate
                2) if #ContractTypeConfiguration == 1 AND type = TLP -> do nothing (remove contract from result)
                3) if #ContractTypeConfiguration > 1 AND none is TLP -> migration
                4) if #ContractTypeConfiguration > 1 AND at least one is TLP -> do nothing (remove contract from result)
             */
            result.removeIf(contractRequiresNoMigration());
        } catch (Exception e) {
            throw new MaintenanceTaskSetupFailedException(this, e);
        }

        LOG.info(String.format("Start migration of %s contracts to TLP", result.size()));
        this.contracts = result.iterator();
    }

    @Override
    public void processNextEntry() throws MaintenanceEntryProcessingFailedException {
        final ContractPVO currentContract = this.contracts.next();

        try {
            // if we get here, we know that no contract-type configuration is TLP
            final List<ContractTypeConfiguration> configurations = listContractTypeConfigurations(currentContract);
            final ContractTypeConfiguration newest = TimeSliceTool.getNewest(configurations);
            // overwrite, do not slice
            newest.setType(ContractTypeEnum.TLP.name());
            persistenceHandler.update(newest);
        } catch (Exception e) {
            throw new MaintenanceEntryProcessingFailedException(currentContract.getContractNr(), e);
        }
    }

    @Override
    public boolean hasNextEntry() {
        return this.contracts.hasNext();
    }

    @Override
    public void cleanUp() {
        // new function introduced here: if this specific GP is not set, we do not execute this task anymore
        try {
            final GlobalPropertiesAdmin globalPropertiesAdmin = new GlobalPropertiesAdmin();
            final GlobalProperty globalProperty = new GlobalProperty();
            globalProperty.setPropertyKey(GlobalPropertiesEnum.TARIFF_IDS_FOR_MIGRATION_CONTRACTS_TO_TLP.getKey());
            globalProperty.setVal("");
            globalPropertiesAdmin.saveWithLogEntry(globalProperty);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
            select * from fl_contract as con
            left join fl_tariffapplication as tapp on tapp.contract_ = con.id_
            where tapp.tariff_ in (...);
     */
    private static final String SELECT_CONTRACTS_WITH_CONFIGURED_TARIFFS =
            "SELECT * FROM " + ContractDAO.TABLE_NAME + " AS con "
            + " LEFT JOIN " + TariffApplicationDAO.TABLE_NAME + " AS tapp ON tapp." + TariffApplicationDAO.column_Contract + " = con. " + ContractDAO.column_id
            + " WHERE tapp." + TariffApplicationDAO.column_Tariff + " IN ";

    private List<ContractPVO> selectRelevantContracts() throws Exception {
        if(TARIFF_IDS_FOR_MIGRATION_CONTRACTS_TO_TLP.isSet()) {
            final List<String> tariffIds = TARIFF_IDS_FOR_MIGRATION_CONTRACTS_TO_TLP.getValueAsSeparatedList(",");
            return CommandProcessor.process(c -> {
                final List<ContractPVO> result = new ArrayList<>();
                final String inClause = "(" + StringUtils.join(tariffIds, ", ") + ")";
                final PreparedStatement stmt = c.prepareStatement(SELECT_CONTRACTS_WITH_CONFIGURED_TARIFFS + inClause);
                final ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    final ContractPVO pvo = contractDAO.buildContractPVO(rs);
                    if(pvo != null && pvo.getId() != null) {
                        result.add(pvo);
                    }
                }
                return result;
            });
        }
        return Collections.emptyList();
    }

    /*
        return "true" if contract does NOT need migration
     */
    private Predicate<ContractPVO> contractRequiresNoMigration() {
        return contractPVO -> {
            if(StringUtils.isNotEmpty(contractPVO.getType())) {
                // contract was not migrated on server-start might be a different issue
                // we do not expect this to happen, but we also do not "fix" it here
                LOG.error(String.format("Contract %s still has a type set in contract.type: %s",
                        contractPVO.getContractNr(), contractPVO.getType()));
                return true;
            }

            final List<ContractTypeConfiguration> configurations = listContractTypeConfigurations(contractPVO);

            if(CollectionUtils.isEmpty(configurations)) {
                // same as before, this should not happen and if it does, we not fix it here
                LOG.error(String.format("Contract %s has no ContractTypeConfigurations",
                        contractPVO.getContractNr()));
                return true;
            }

            if(configurations.size() == 1) {
                // current and only slice is TLP, no migration needed
                return ContractTypeEnum.isEqualTo(configurations.get(0).getType(), ContractTypeEnum.TLP);
            } else {
                // if at least one slice is TLP, we do not migrate
                // even if the newest slice is not TLP, but an older is, we do not migrate (not an expected situation)
                return configurations.stream().anyMatch(c -> ContractTypeEnum.isEqualTo(c.getType(), ContractTypeEnum.TLP));
            }
        };
    }

    private List<ContractTypeConfiguration> listContractTypeConfigurations(final ContractPVO contractPVO) {
        return persistenceHandler.listContractTypeConfiguration(
                new ContractTypeConfiguration().withContract(contractPVO.getId()), PersistMode.FLAT);
    }
}
