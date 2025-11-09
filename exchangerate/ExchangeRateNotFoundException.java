package de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate;

import de.sundn.bars.server.util.InvalidOperationException;

import java.time.LocalDate;

public class ExchangeRateNotFoundException extends InvalidOperationException {
  public ExchangeRateNotFoundException(String sourceCurrency, String targetCurrency, LocalDate date) {
    super("No exchange rate found for " + sourceCurrency + " to " + targetCurrency + " on " + date);
  }
}