package de.sundn.bars.server.business.execution;

import de.sundn.bars.server.business.objects.businessobjects.Currency;
import de.sundn.bars.server.business.objects.businessobjects.ExchangeRate;
import de.sundn.bars.server.objectbroker.BrokerServer;
import de.sundn.bars.server.objectbroker.ExchangeRateBroker;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.time.LocalDate;

import static de.sundn.bars.server.business.objects.businessobjects.Currency.CURRENCY_ISO_CODE_EUR;

public class ExchangeRateLoader {

  private ExchangeRateLoader() {
    // Functional class
  }

  public static ExchangeRate loadExchangeRate(Currency sourceCurrency, Currency targetCurrency, LocalDate date) throws ExchangeRateMissingException {
    return calculateExchangeRate(sourceCurrency, targetCurrency, date);
  }

  private static ExchangeRate calculateExchangeRate(Currency sourceCurrency, Currency targetCurrency, LocalDate date) throws ExchangeRateMissingException {
    ExchangeRateBroker broker = BrokerServer.getInstance().getExchangeRateBroker();
    Connection connection = BrokerServer.getInstance().getConnection();
    try {
      if (sourceCurrency.equals(targetCurrency)) {
        return exchangeRateForEqualCurrencies(sourceCurrency);
      }
      if (sourceCurrency.checkIfEuroCurrency()) {
        return exchangeRateFromEuro(targetCurrency, broker, date);
      }
      if (targetCurrency.checkIfEuroCurrency()) {
        return exchangeRateToEuro(sourceCurrency, broker, date);
      }
      return exchangeRateForOtherCurrencies(sourceCurrency, targetCurrency, date, broker);
    } finally {
      BrokerServer.getInstance().closeConnection(connection);
    }
  }

  private static ExchangeRate createExchangeRate(String sourceCurrency, String destCurrency) {
    ExchangeRate rate = new ExchangeRate();
    rate.setCcyIsoCodeSource(sourceCurrency);
    rate.setCcyIsoCodeTarget(destCurrency);
    rate.setParity(1);
    return rate;
  }

  private static @NotNull ExchangeRate exchangeRateForEqualCurrencies(Currency currency) {
    ExchangeRate rate = createExchangeRate(currency.getCurrencyIsoCode(), currency.getCurrencyIsoCode());
    rate.setFactor(1.0);
    return rate;
  }

  private static ExchangeRate exchangeRateToEuro(Currency sourceCurrency, ExchangeRateBroker broker, LocalDate date) throws ExchangeRateMissingException {
    ExchangeRate rate = createExchangeRate(sourceCurrency.getCurrencyIsoCode(), CURRENCY_ISO_CODE_EUR);
    ExchangeRate sourceRate = loadEuroExchangeRateFromDB(broker, sourceCurrency, date);
    rate.setFactor(1.0 / sourceRate.getFactor());
    rate.setValidFrom(sourceRate.getValidFrom());
    rate.setValidTo(sourceRate.getValidTo());
    return rate;
  }

  private static ExchangeRate exchangeRateFromEuro(Currency destCurrency, ExchangeRateBroker broker, LocalDate date) throws ExchangeRateMissingException {
    ExchangeRate rate = createExchangeRate(CURRENCY_ISO_CODE_EUR, destCurrency.getCurrencyIsoCode());
    ExchangeRate destinationRate = loadEuroExchangeRateFromDB(broker, destCurrency, date);
    rate.setFactor(destinationRate.getFactor());
    rate.setValidFrom(destinationRate.getValidFrom());
    rate.setValidTo(destinationRate.getValidTo());
    rate.setParity(destinationRate.getParity());
    return rate;
  }

  private static ExchangeRate exchangeRateForOtherCurrencies(Currency sourceCurrency,
                                                             Currency destCurrency,
                                                             LocalDate date,
                                                             ExchangeRateBroker broker) throws ExchangeRateMissingException {
    ExchangeRate rate = createExchangeRate(sourceCurrency.getCurrencyIsoCode(), destCurrency.getCurrencyIsoCode());
    ExchangeRate sourceRate;
    ExchangeRate destinationRate;
    // none is euro (get two rates against euro)
    sourceRate = loadEuroExchangeRateFromDB(broker, sourceCurrency, date);
    destinationRate = loadEuroExchangeRateFromDB(broker, destCurrency, date);

    rate.setFactor(destinationRate.getFactor() / sourceRate.getFactor());
    rate.setValidFrom(calculateValidFromDate(sourceRate, destinationRate));
    if (sourceRate.getValidTo() == null) {
      rate.setValidTo(destinationRate.getValidTo());
    } else if (destinationRate.getValidTo() == null) {
      rate.setValidTo(sourceRate.getValidTo());
    } else {
      rate.setValidTo(calculateValidToDate(destinationRate, sourceRate));
    }
    return rate;
  }

  private static LocalDate calculateValidToDate(ExchangeRate destinationRate, ExchangeRate sourceRate) {
    boolean sourceRateAfterDestinationRate = sourceRate.getValidTo().isAfter(destinationRate.getValidTo());
    return sourceRateAfterDestinationRate ? destinationRate.getValidTo() : sourceRate.getValidTo();
  }

  private static LocalDate calculateValidFromDate(ExchangeRate sourceRate, ExchangeRate destinationRate) {
    boolean sourceRateAfterDestinationRate = sourceRate.getValidFrom().isAfter(destinationRate.getValidFrom());
    return sourceRateAfterDestinationRate ? sourceRate.getValidFrom() : destinationRate
            .getValidFrom();
  }

  private static ExchangeRate loadEuroExchangeRateFromDB(ExchangeRateBroker broker,
                                                         Currency targetCurrency,
                                                         LocalDate date) throws ExchangeRateMissingException {
    ExchangeRate exchangeRate = broker.queryBySourceAndTargetAndDate(CURRENCY_ISO_CODE_EUR, targetCurrency.getCurrencyIsoCode(), date);
    assertExchangeRateNotNull(exchangeRate,targetCurrency.getCurrencyIsoCode(), date);
    return exchangeRate;
  }

  private static void assertExchangeRateNotNull(ExchangeRate rate, String currencyIsoCode, LocalDate date) throws ExchangeRateMissingException {
    if (rate == null) {
      throw new ExchangeRateMissingException(String.format("Exchange rate from EUR to %s on date %s not found", currencyIsoCode, date));
    }
  }
}
