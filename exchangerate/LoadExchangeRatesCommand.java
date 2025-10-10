package de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate;

import de.sundn.bars.server.websocket.processing.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The LoadExchangeRatesCommand class represents a command object used to load exchange rates for a list of statementKeys.
 * It contains the target currency and statements for which exchange rates should be loaded.
 * Is executed by the {@link LoadExchangeRatesCommandExecutor}.
 */
public record LoadExchangeRatesCommand(
        @NotNull String targetCurrency,
        @NotNull List<String> statementKeys,
        @NotNull ExchangeRateType exchangeRateType
) implements Command<LoadExchangeRatesResponse> {
}