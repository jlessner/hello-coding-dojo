package de.sundn.bars.server.websocket.processing.commands.report;

import de.sundn.bars.server.business.objects.UserRight;
import de.sundn.bars.server.business.objects.businessobjects.ConsUnit;
import de.sundn.bars.server.business.objects.businessobjects.Customer;
import de.sundn.bars.server.business.objects.businessobjects.ExchangeRate;
import de.sundn.bars.server.business.objects.businessobjects.Statement;
import de.sundn.bars.server.business.objects.businessobjects.resourceobjects.StatementState;
import de.sundn.bars.server.objectbroker.BrokerServer;
import de.sundn.bars.server.objectbroker.ExchangeRateBroker;
import de.sundn.bars.server.websocket.processing.ErrorCode;
import de.sundn.bars.server.websocket.processing.ResponseCode;
import de.sundn.bars.server.websocket.processing.commands.AbstractWithStaticDataBrokerManipulationWebsocketTest;
import de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate.ExchangeRateType;
import de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate.LoadExchangeRatesCommand;
import de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate.LoadExchangeRatesResponse;
import de.sundn.bars.server.websocket.processing.websocketmessagehandling.WebsocketMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoadExchangeRatesCommandExecutorTest extends AbstractWithStaticDataBrokerManipulationWebsocketTest {

  private static final String TARGET_CURRENCY = "USD";
  private static final String SOURCE_CURRENCY = "EUR";
  private String statementKey1;
  private String statementKey2;

  @BeforeEach
  void setupUserRights() {
    setSessionUserRights(Set.of(UserRight.CREATE_REPORT));
  }

  @BeforeEach
  void setupTestData() {
    Customer customer = customerBuilder().persist();
    ConsUnit consUnit = consUnitBuilder()
            .withName(IRRELEVANT_CONS_UNIT_NAME)
            .withSpreadingSchemeKey(CORPORATE_SPREADING_SCHEME_KEY)
            .withCustomerKey(customer.getCustomerInfo().getCustomerKey())
            .withCurrencyIsoCode(SOURCE_CURRENCY)
            .persist();
    customer.getConsUnits().add(consUnit);
    Statement s1 = statementBuilder()
            .withStatementDate(LocalDate.of(2019, 12, 31))
            .withStatementState(StatementState.STATE_APPROVED)
            .withConsUnitKey(consUnit.getConsUnitInfo().getConsUnitKey())
            .persist();
    Statement s2 = statementBuilder()
            .withStatementDate(LocalDate.of(2020, 12, 31))
            .withStatementState(StatementState.STATE_APPROVED)
            .withConsUnitKey(consUnit.getConsUnitInfo().getConsUnitKey())
            .persist();
    consUnit.getStatementList().add(s1);
    consUnit.getStatementList().add(s2);
    statementKey1 = s1.getStatementInfo().getStatementKey();
    statementKey2 = s2.getStatementInfo().getStatementKey();
    setSessionCustomer(customer);
  }

  private static void createExchangeRate(LocalDate validFrom, LocalDate validTo, double factor) {
    ExchangeRate exchangeRate = exchangeRateBuilder()
            .withCcyIsoCodeSource(SOURCE_CURRENCY)
            .withCcyIsoCodeTarget(TARGET_CURRENCY)
            .withValidFrom(validFrom)
            .withValidTo(validTo)
            .withFactor(factor)
            .build();
    ExchangeRateBroker broker = BrokerServer.getInstance().getExchangeRateBroker();
    broker.saveForTestsONLY(exchangeRate);
    commit();
  }

  @Test
  void testReturnCurrentRatesForGivenStatements() {
    createExchangeRate(LocalDate.now().minusDays(10), null, 1.1);
    LoadExchangeRatesCommand command = new LoadExchangeRatesCommand(
            TARGET_CURRENCY,
            List.of(statementKey1, statementKey2),
            ExchangeRateType.CURRENT
    );
    WebsocketMessage<LoadExchangeRatesResponse> response = executeCommand(command);
    assertTrue(response.responseCode().isPresent());
    assertEquals(ResponseCode.OK, response.responseCode().get());
    assertNotNull(response.payload());
    HashMap<String, Double> exchangeRates = response.payload().exchangeRates();
    assertNotNull(exchangeRates);
    assertEquals(2, exchangeRates.size());
    assertEquals(1.1, exchangeRates.get(statementKey1));
    assertEquals(1.1, exchangeRates.get(statementKey2));
  }

  @Test
  void testReturnHistoricalRatesOnStatementDates() {
    createExchangeRate(
            LocalDate.of(2019, 1, 1),
            LocalDate.of(2019, 12, 31),
            1.2
    );
    createExchangeRate(
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 12, 31),
            2.2
    );
    LoadExchangeRatesCommand command = new LoadExchangeRatesCommand(
            TARGET_CURRENCY,
            List.of(statementKey1, statementKey2),
            ExchangeRateType.HISTORICAL
    );
    WebsocketMessage<LoadExchangeRatesResponse> response = executeCommand(command);

    assertTrue(response.responseCode().isPresent());
    assertEquals(ResponseCode.OK, response.responseCode().get());
    assertNotNull(response.payload());

    HashMap<String, Double> exchangeRates = response.payload().exchangeRates();
    assertNotNull(exchangeRates);
    assertEquals(2, exchangeRates.size());
    assertEquals(1.2, exchangeRates.get(statementKey1));
    assertEquals(2.2, exchangeRates.get(statementKey2));
  }

  @Test
  void testReturnRatesForMostRecentStatementDate() {
    createExchangeRate(
            LocalDate.of(2019, 1, 1),
            LocalDate.of(2019, 12, 31),
            1.3
    );
    createExchangeRate(
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 12, 31),
            2.3
    );
    LoadExchangeRatesCommand command = new LoadExchangeRatesCommand(
            TARGET_CURRENCY,
            List.of(statementKey1, statementKey2),
            ExchangeRateType.MOST_RECENT_STATEMENT_DATE
    );

    WebsocketMessage<LoadExchangeRatesResponse> response = executeCommand(command);

    assertTrue(response.responseCode().isPresent());
    assertEquals(ResponseCode.OK, response.responseCode().get());
    assertNotNull(response.payload());

    HashMap<String, Double> exchangeRates = response.payload().exchangeRates();
    assertNotNull(exchangeRates);
    assertEquals(2, exchangeRates.size());
    assertEquals(2.3, exchangeRates.get(statementKey1));
    assertEquals(2.3, exchangeRates.get(statementKey2));
  }

  @Test
  void testFailWhenStatementNotInSession() {
    LoadExchangeRatesCommand command = new LoadExchangeRatesCommand(
            TARGET_CURRENCY,
            List.of("NON_EXISTING_STATEMENT_KEY"),
            ExchangeRateType.CURRENT
    );
    WebsocketMessage<?> response = executeCommand(command);
    assertErrorResponse(response, ResponseCode.BAD_REQUEST, ErrorCode.REQUEST_INVALID);
  }
}