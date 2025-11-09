package de.sundn.bars.server.business.position.positioncalculator;

import de.sundn.bars.server.business.execution.ExchangeRateLoader;
import de.sundn.bars.server.business.objects.businessobjects.Currency;
import de.sundn.bars.server.business.objects.businessobjects.ExchangeRate;
import de.sundn.bars.server.objectbroker.AbstractWithBrokerTest;
import de.sundn.bars.server.objectbroker.BrokerServer;
import de.sundn.bars.server.objectbroker.ExchangeRateBroker;
import de.sundn.bars.server.objectbroker.ExchangeRatesCache;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateLoaderTest extends AbstractWithBrokerTest {

    private static ExchangeRatesCache exchangeRatesCache;

    @BeforeAll
    static void initExchangeRatesCacheAndBackup() {
      exchangeRatesCache = new ExchangeRatesCache(databaseConnection);
      exchangeRatesCache.initializeBroker();
      exchangeRatesCache.fillBackups();
    }

    @BeforeEach
    void clearExchangeRates() throws Exception {
        ExchangeRatesCache.clearExchangeRateTable();
        commit();
    }

    @AfterAll
    static void refillExchangeRates() throws Exception {
        ExchangeRatesCache.clearExchangeRateTable();
        exchangeRatesCache.refillExchangeRates();
        commit();
    }

    private static final String EUR = Currency.CURRENCY_ISO_CODE_EUR;
    private static final String USD = "USD";
    private static final String GBP = "GBP";
    private static final LocalDate TEST_DATE = LocalDate.of(2025, 1, 15);

    private Currency ccy(String iso) {
        return new Currency(iso);
    }

    private void createExchangeRate(String targetCurrency, LocalDate validFrom, LocalDate validTo, double factor, int parity) {
        ExchangeRate exchangeRate = exchangeRateBuilder()
                .withCcyIsoCodeSource(ExchangeRateLoaderTest.EUR)
                .withCcyIsoCodeTarget(targetCurrency)
                .withValidFrom(validFrom)
                .withValidTo(validTo)
                .withFactor(factor)
                .withParity(parity)
                .build();
        ExchangeRateBroker broker = BrokerServer.getInstance().getExchangeRateBroker();
        broker.saveForTestsONLY(exchangeRate);
        commit();
    }

    @Test
    void testLoadExchangeRateMode_SameCurrencies() {
        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(USD), TEST_DATE);

        assertNotNull(result);
        assertEquals(USD, result.getCcyIsoCodeSource());
        assertEquals(USD, result.getCcyIsoCodeTarget());
        assertEquals(1.0, result.getFactor());
        assertEquals(1, result.getParity());
    }

    @Test
    void testLoadExchangeRate_EurToOtherCurrency() {
        createExchangeRate(USD, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 1.2, 1);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(EUR), ccy(USD), TEST_DATE);

        assertNotNull(result);
        assertEquals(EUR, result.getCcyIsoCodeSource());
        assertEquals(USD, result.getCcyIsoCodeTarget());
        assertEquals(1.2, result.getFactor());
        assertEquals(1, result.getParity());
        assertEquals(LocalDate.of(2025, 1, 1), result.getValidFrom());
        assertEquals(LocalDate.of(2025, 12, 31), result.getValidTo());
    }

    @Test
    void testLoadExchangeRate_OtherCurrencyToEur() {
        createExchangeRate(USD, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 1.2, 1);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(EUR), TEST_DATE);

        assertNotNull(result);
        assertEquals(USD, result.getCcyIsoCodeSource());
        assertEquals(EUR, result.getCcyIsoCodeTarget());
        assertEquals(1.0 / 1.2, result.getFactor(), 0.0001);
        assertEquals(1, result.getParity());
        assertEquals(LocalDate.of(2025, 1, 1), result.getValidFrom());
        assertEquals(LocalDate.of(2025, 12, 31), result.getValidTo());
    }

    @Test
    void testLoadExchangeRate_NonEuroToNonEuroCurrencies() {
        createExchangeRate(USD, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 1.2, 1);
        createExchangeRate(GBP, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 0.9, 1);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(GBP), TEST_DATE);

        assertNotNull(result);
        assertEquals(USD, result.getCcyIsoCodeSource());
        assertEquals(GBP, result.getCcyIsoCodeTarget());
        assertEquals(0.9 / 1.2, result.getFactor(), 0.0001);
        assertEquals(1, result.getParity());
        assertEquals(LocalDate.of(2025, 1, 1), result.getValidFrom());
        assertEquals(LocalDate.of(2025, 12, 31), result.getValidTo());
    }

    @Test
    void testLoadExchangeRate_NonEuroToNonEuroCurrencies_DifferentValidDates() {
        createExchangeRate(USD, LocalDate.of(2025, 1, 5), LocalDate.of(2025, 11, 30), 1.2, 1);
        createExchangeRate(GBP, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 0.9, 1);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(GBP), TEST_DATE);

        assertNotNull(result);
        assertEquals(USD, result.getCcyIsoCodeSource());
        assertEquals(GBP, result.getCcyIsoCodeTarget());
        assertEquals(0.9 / 1.2, result.getFactor(), 0.0001);
        assertEquals(LocalDate.of(2025, 1, 5), result.getValidFrom());
        assertEquals(LocalDate.of(2025, 11, 30), result.getValidTo());
    }

    @Test
    void testLoadExchangeRate_EurToOtherCurrency_RateNotFound() {
        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(EUR), ccy(USD), TEST_DATE);

        assertNull(result);
    }

    @Test
    void testLoadExchangeRate_OtherCurrencyToEur_RateNotFound() {
        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(EUR), TEST_DATE);

        assertNull(result);
    }

    @Test
    void testLoadExchangeRate_NonEuroToNonEuro_SourceRateNotFound() {
        createExchangeRate(GBP, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 0.9, 1);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(GBP), TEST_DATE);

        assertNull(result);
    }

    @Test
    void testLoadExchangeRate_NonEuroToNonEuro_TargetRateNotFound() {
        createExchangeRate(USD, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 1.2, 1);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(USD), ccy(GBP), TEST_DATE);

        assertNull(result);
    }

    @Test
    void testLoadExchangeRate_WithParity() {
        createExchangeRate(USD, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 1.2, 100);

        ExchangeRate result = ExchangeRateLoader.loadExchangeRate(ccy(EUR), ccy(USD), TEST_DATE);

        assertNotNull(result);
        assertEquals(EUR, result.getCcyIsoCodeSource());
        assertEquals(USD, result.getCcyIsoCodeTarget());
        assertEquals(1.2, result.getFactor());
        assertEquals(100, result.getParity());
    }
}
