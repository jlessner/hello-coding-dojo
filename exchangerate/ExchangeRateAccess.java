package de.sundn.bars.server.business.position.positioncalculator;

import de.sundn.bars.server.business.objects.businessobjects.Currency;
import de.sundn.bars.server.business.objects.businessobjects.ExchangeRate;
import de.sundn.bars.server.objectbroker.BrokerServer;
import de.sundn.bars.server.objectbroker.ExchangeRateBroker;

import java.sql.Connection;
import java.time.LocalDate;

public class ExchangeRateAccess {

  private ExchangeRateAccess() {
    // Functional class
  }

  private static ExchangeRate getEuroExchangeRate(ExchangeRateBroker broker,
                                                  String destCurrency, LocalDate date) {

    String isoCodeEuro = Currency.CURRENCY_ISO_CODE_EUR;

    return broker.queryBySourceAndTargetAndDate(isoCodeEuro, destCurrency, date);
  }

  public static ExchangeRate getExchangeRate(String sourceCurrency, String destCurrency, boolean euroMode) {
    return getExchangeRate(sourceCurrency, destCurrency, LocalDate.now(), euroMode);
  }

  public static ExchangeRate getExchangeRate(String sourceCurrency, String destCurrency, LocalDate date) {
    return getExchangeRate(sourceCurrency, destCurrency, date, false);
  }

  public static ExchangeRate getExchangeRate(String sourceCurrency, String destCurrency, LocalDate date, boolean euroMode) {
    if (euroMode) {
      return getExchangeRateEuroMode(sourceCurrency, destCurrency, date);
    } else {
      return getExchangeRateNormalMode(sourceCurrency, destCurrency, date);
    }
  }

  private static ExchangeRate getExchangeRateNormalMode(String sourceCurrency, String destCurrency, LocalDate date) {

    ExchangeRate rate1 = null;
    ExchangeRate rate2 = null;
    ExchangeRate result = new ExchangeRate();

    ExchangeRateBroker broker = BrokerServer.getInstance().getExchangeRateBroker();
    Connection connection = BrokerServer.getInstance().getConnection();

    try {
      result.setCcyIsoCodeSource(sourceCurrency);
      result.setCcyIsoCodeTarget(destCurrency);
      result.setParity(1);

      if (sourceCurrency.equals(destCurrency)) {
        result.setFactor(1.0);
        return result;
      }

      if (sourceCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR) || destCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR)) {
        // one is euro
        if (sourceCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR)) {
          rate1 = getEuroExchangeRate(broker, destCurrency, date);
        } else {
          rate1 = getEuroExchangeRate(broker, sourceCurrency, date);
        }

        if (rate1 == null) {
          return null;
        }

        if (sourceCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR)) {
          result.setFactor(rate1.getFactor());
          result.setValidFrom(rate1.getValidFrom());
          result.setValidTo(rate1.getValidTo());
          result.setParity(rate1.getParity());
        } else {
          result.setFactor(1.0 / rate1.getFactor());
          result.setValidFrom(rate1.getValidFrom());
          result.setValidTo(rate1.getValidTo());
          // result.setParity(rate1.getParity());
        }
      } else {
        // none is euro (get two rates against euro)
        rate1 = getEuroExchangeRate(broker, sourceCurrency, date);

        if (rate1 == null) {
          return null;
        }

        rate2 = getEuroExchangeRate(broker, destCurrency, date);

        if (rate2 == null) {
          return null;
        }

        // build resulting rate
        result.setFactor(rate2.getFactor() / rate1.getFactor());
        result.setValidFrom(rate1.getValidFrom().isAfter(rate2.getValidFrom()) ? rate1.getValidFrom() : rate2
                .getValidFrom());
        result.setValidTo(rate1.getValidTo().isAfter(rate2.getValidTo()) ? rate2.getValidTo() : rate1.getValidTo());
      }

    } finally {
      BrokerServer.getInstance().closeConnection(connection);
    }

    return result;
  }

  private static ExchangeRate getExchangeRateEuroMode(String sourceCurrency, String destCurrency, LocalDate date) {

    ExchangeRate rate1 = null;
    ExchangeRate rate2 = null;
    ExchangeRate result = new ExchangeRate();

    ExchangeRateBroker broker = BrokerServer.getInstance().getExchangeRateBroker();
    Connection connection = BrokerServer.getInstance().getConnection();

    try {
      result.setCcyIsoCodeSource(sourceCurrency);
      result.setCcyIsoCodeTarget(destCurrency);
      result.setParity(1);

      if (sourceCurrency.equals(destCurrency)) {
        result.setFactor(1.0);
        return result;
      }
      if (sourceCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR) || destCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR)) {
        // one is euro, the other is outer euro (otherwise will will return
        // above)
        if (sourceCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR)) {
          rate1 = getEuroExchangeRate(broker, destCurrency, date);
        } else {
          rate1 = getEuroExchangeRate(broker, sourceCurrency, date);
        }

        if (rate1 == null) {
          return null;
        }

        if (sourceCurrency.equals(Currency.CURRENCY_ISO_CODE_EUR)) {
          result.setFactor(rate1.getFactor());
          result.setValidFrom(rate1.getValidFrom());
          result.setValidTo(rate1.getValidTo());
          result.setParity(rate1.getParity());
        } else {
          result.setFactor(1.0 / rate1.getFactor());
          result.setValidFrom(rate1.getValidFrom());
          result.setValidTo(rate1.getValidTo());
          // result.setParity(rate1.getParity());
        }
      } else {
        // none is euro (get two rates against euro)
        rate1 = getEuroExchangeRate(broker, sourceCurrency, date);

        if (rate1 == null) {
          return null;
        }

        rate2 = getEuroExchangeRate(broker, destCurrency, date);

        if (rate2 == null) {
          return null;
        }

        // build resulting rate
        result.setFactor(rate2.getFactor() / rate1.getFactor());
        result.setValidFrom(rate1.getValidFrom().isAfter(rate2.getValidFrom()) ? rate1.getValidFrom() : rate2
                .getValidFrom());
        if (rate1.getValidTo() == null) {
          result.setValidTo(rate2.getValidTo());
        } else if (rate2.getValidTo() == null) {
          result.setValidTo(rate1.getValidTo());
        } else {
          result.setValidTo(rate1.getValidTo().isAfter(rate2.getValidTo()) ? rate2.getValidTo() : rate1.getValidTo());
        }
      }

    } finally {
      BrokerServer.getInstance().closeConnection(connection);
    }

    return result;
  }

}
