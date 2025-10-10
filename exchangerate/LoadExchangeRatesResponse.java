package de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate;

import de.sundn.bars.server.websocket.processing.commands.Response;

import java.util.HashMap;

/**
 * Response for the LoadExchangeRatesCommand containing a list of exchange rates.
 */
public record LoadExchangeRatesResponse(HashMap<String, Double> exchangeRates) implements Response {
}