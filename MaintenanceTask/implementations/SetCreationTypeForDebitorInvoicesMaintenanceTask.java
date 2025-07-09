/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.maintenancetask;

import static com.nextlevel.platform.financial.debitor.DebitorAccountType.InvoiceExpenses;
import static com.nextlevel.platform.financial.debitor.DebitorAccountType.InvoiceRLMExpenses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.enums.ContractTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.enums.DebitorInvoiceCreationTypeEnum;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.DebitorInvoiceDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.dao.DebitorInvoiceItemDAO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.DebitorInvoiceItemPVO;
import com.nextlevel.fastlane.persistence.myBusinessSupplier.pvo.DebitorInvoicePVO;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceEntryProcessingFailedException;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTask;
import com.nextlevel.myBusinessSupplier.maintenance.MaintenanceTaskSetupFailedException;
import com.nextlevel.platform.connection.CommandProcessor;
import com.nextlevel.platform.date.DateTool;

// @formatter:off

public class SetCreationTypeForDebitorInvoicesMaintenanceTask implements MaintenanceTask {

    private static final Logger LOG = LogManager.getLogger(SetCreationTypeForDebitorInvoicesMaintenanceTask.class);

    private final DebitorInvoiceDAO invoiceDAO = new DebitorInvoiceDAO();
    private final DebitorInvoiceItemDAO itemDAO = new DebitorInvoiceItemDAO();

    private Iterator<DebitorInvoicePVO> invoices;

    /*
        select * from fl_debitorinvoice where creationType_ is NULL;

        big-hammer select

        i.e. LSW B2C ~ 2,5 Million Invoices including Projections

     */
    private static final String SELECT_INVOICES_WITHOUT_CREATION_TYPE =
            "SELECT * FROM " + DebitorInvoiceDAO.TABLE_NAME +
            " WHERE " + DebitorInvoiceDAO.column_creationType + " IS NULL";

    @Override
    public void setup() throws MaintenanceTaskSetupFailedException {
        try {
            invoices = CommandProcessor.process(c -> {
                final List<DebitorInvoicePVO> result = new ArrayList<>();
                final PreparedStatement stmt = c.prepareStatement(SELECT_INVOICES_WITHOUT_CREATION_TYPE);
                final ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    final DebitorInvoicePVO pvo = invoiceDAO.buildDebitorInvoicePVO(rs);
                    if(pvo != null) {
                        result.add(pvo);
                    }
                }
                LOG.info(String.format("Start set creationType of %s invoices", result.size()));
                return result.iterator();
            });
        } catch (Exception e) {
            throw new MaintenanceTaskSetupFailedException(this, e);
        }
    }

    @Override
    public void processNextEntry() throws MaintenanceEntryProcessingFailedException {
        final DebitorInvoicePVO currentInvoice = this.invoices.next();
        try {
            Optional<DebitorInvoiceCreationTypeEnum> creationType = Optional.empty();
            final List<DebitorInvoiceItemPVO> items = selectItemIndicators(currentInvoice);
            final List<String> log = new ArrayList<>();
            log.add(String.format("Try indication for invoice %s", currentInvoice.getIdentifier()));
            if(CollectionUtils.isNotEmpty(items)) {
                /*
                    if we get expenses booked in 60 or 61 we can identify the invoice type by

                    60 -> SLP
                    61 -> RLM

                    Currently, we can safely assume that either 60 or 61 is available, never both

                    However, we might not have 60 or 61 at all, i.e. in case of 52 (Regionale Preise)
                    Here, expenses are all-inclusive and 52 can be used for both SLP / RLM.
                    In this case, we check in an additional step the contract-type
                 */

               final long countSLPExpenses = items.stream()
                        .filter(i -> InvoiceExpenses.getId() == i.getCreditAccountTypeId()
                                || InvoiceExpenses.getId() == i.getDebitAccountTypeId()).count();
               final long countRLMExpenses = items.stream()
                        .filter(i -> InvoiceRLMExpenses.getId() == i.getCreditAccountTypeId()
                                || InvoiceRLMExpenses.getId() == i.getDebitAccountTypeId()).count();
               log.add(String.format("Found %s items in total with %s SLP indicators and %s RLM indicators", items.size(), countSLPExpenses, countRLMExpenses));
               if(countSLPExpenses > 0 && countRLMExpenses == 0) {
                   creationType = Optional.of(DebitorInvoiceCreationTypeEnum.SLP);
                   log.add("Indication via items results: SLP");
               } else if(countSLPExpenses == 0 && countRLMExpenses > 0) {
                   creationType = Optional.of(DebitorInvoiceCreationTypeEnum.RLM);
                   log.add("Indication via items results: RLM");
               } else {
                   log.add("Indication via items not clear, try contract-type");
               }
            } else {
                log.add("No items for indication found, try contract-type");
            }

            if(!creationType.isPresent()) {
                final Date creationDate = currentInvoice.getCreationDate() != null
                        ? new Date(currentInvoice.getCreationDate().getTime())
                        : DateTool.getCurrent();
                final ContractTypeEnum contractType = ContractTypeEnum.getType(new Contract().withId(currentInvoice.getContract()), creationDate);

                if(contractType != null) {
                    log.add(String.format("Found contract-type %s for contract %s at date %s",
                            contractType.name(), currentInvoice.getContract(), DateTool.germanDateFormat().format(creationDate)));
                    if (contractType == ContractTypeEnum.RLM) {
                        creationType = Optional.of(DebitorInvoiceCreationTypeEnum.RLM);
                        log.add("Indication via contract-type results: RLM");
                    } else if (contractType == ContractTypeEnum.SLP) {
                        creationType = Optional.of(DebitorInvoiceCreationTypeEnum.SLP);
                        log.add("Indication via contract-type results: SLP");
                    } else if (contractType == ContractTypeEnum.TLP) {
                        creationType = Optional.of(DebitorInvoiceCreationTypeEnum.TLP);
                        log.add("Indication via contract-type results: TLP");
                    }
                } else {
                    log.add(String.format("Found no valid contract-type for contract %s at date %s",
                           currentInvoice.getContract(), DateTool.germanDateFormat().format(creationDate)));
                }
            }

            if(creationType.isPresent()) {
                currentInvoice.setCreationType(creationType.get().name());
                this.invoiceDAO.update(currentInvoice);
            } else {
                final String message = String.format("No creation type assignable: %s", StringUtils.join(log, "\n"));
                LOG.debug(message);
                throw new Exception(message);
            }

        } catch (Exception e) {
            throw new MaintenanceEntryProcessingFailedException(currentInvoice.getIdentifier(), e);
        }
    }

    @Override
    public boolean hasNextEntry() {
        if(this.invoices != null) {
            return this.invoices.hasNext();
        }
        return false;
    }

    @Override
    public int batchSize() {
        return 10000;
    }

    /*
        select * from fl_debitorinvoiceitem
        where invoiceId_ = ?
        and (debitAccountTypeId_ in (60, 61) or creditAccountType_ in (60, 61));
     */
    private static final String SELECT_ITEMS_FOR_INVOICE =
            "SELECT * FROM " + DebitorInvoiceItemDAO.TABLE_NAME
            + " WHERE " + DebitorInvoiceItemDAO.column_invoiceId + " = ? " +
            " AND (" + DebitorInvoiceItemDAO.column_debitAccountTypeId + " IN (?, ?) " +
            " OR " + DebitorInvoiceItemDAO.column_creditAccountTypeId + " IN (?, ?))";

    private List<DebitorInvoiceItemPVO> selectItemIndicators(final DebitorInvoicePVO invoice) throws Exception {
        return CommandProcessor.process(c -> {
            final List<DebitorInvoiceItemPVO> result = new ArrayList<>();
            final PreparedStatement stmt = c.prepareStatement(SELECT_ITEMS_FOR_INVOICE);
            stmt.setLong(1, invoice.getId());
            stmt.setLong(2, InvoiceExpenses.getId());
            stmt.setLong(3, InvoiceRLMExpenses.getId());
            stmt.setLong(4, InvoiceExpenses.getId());
            stmt.setLong(5, InvoiceRLMExpenses.getId());

            final ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                final DebitorInvoiceItemPVO pvo = itemDAO.buildDebitorInvoiceItemPVO(rs);
                if(pvo != null) {
                    result.add(pvo);
                }
            }
            return result;
        });
    }
}
