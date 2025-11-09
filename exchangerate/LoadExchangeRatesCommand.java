package de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate;

import de.sundn.bars.server.business.execution.KeyGenerator;
import de.sundn.bars.server.business.objects.businessobjects.Currency;
import de.sundn.bars.server.websocket.processing.commands.Command;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * The LoadExchangeRatesCommand class represents a command object used to load exchange rates for a list of statementKeys.
 * It contains the target currency and statements for which exchange rates should be loaded.
 * Is executed by the {@link LoadExchangeRatesCommandExecutor}.
 */
public record LoadExchangeRatesCommand(
        @Size(max = Currency.CURRENCY_ISO_CODE_MAX_LENGTH) String targetCurrency,
        List<@Size(min = KeyGenerator.KEY_LENGTH, max = KeyGenerator.KEY_LENGTH) String> statementKeys,
        ExchangeRateType exchangeRateType
) implements Command<LoadExchangeRatesResponse> {
}