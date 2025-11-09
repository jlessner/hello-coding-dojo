package de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate;

import de.sundn.bars.server.authorization.RequiresRights;
import de.sundn.bars.server.business.data.StaticDataBroker;
import de.sundn.bars.server.business.execution.ExchangeRateLoader;
import de.sundn.bars.server.business.objects.UserRight;
import de.sundn.bars.server.business.objects.businessobjects.*;
import de.sundn.bars.server.util.InvalidOperationException;
import de.sundn.bars.server.websocket.processing.commands.AbstractCommandExecutor;
import de.sundn.bars.server.websocket.processing.commands.CommandProcessingException;
import de.sundn.bars.server.websocket.processing.commands.consolidationunit.ConsUnitNotFoundException;
import de.sundn.bars.server.websocket.processing.commands.sessioninfo.SessionInfo;
import de.sundn.bars.server.websocket.processing.commands.statement.StatementInSessionFinder;
import de.sundn.bars.server.websocket.processing.session.SessionCustomer;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Executor for the LoadExchangeRatesCommand.
 * Loads exchange rates for the specified statements and target currency.
 */
@RequiresRights(userRights = UserRight.CREATE_REPORT)
public class LoadExchangeRatesCommandExecutor extends AbstractCommandExecutor<LoadExchangeRatesCommand, LoadExchangeRatesResponse> {

  @Override
  public @NotNull LoadExchangeRatesResponse execute(@NotNull LoadExchangeRatesCommand command, @NotNull SessionInfo sessionInfo) throws CommandProcessingException, InvalidOperationException {
    SessionCustomer sessionCustomer = sessionInfo.getUserSession().getSessionCustomer();
    assertSessionCustomerNotNull(sessionCustomer);
    String targetCurrency = command.targetCurrency();
    List<String> statementKeys = command.statementKeys();
    HashMap<String, Double> exchangeRates = null;
    StatementList statements = StatementInSessionFinder.findStatements(sessionCustomer, statementKeys);
    switch (command.exchangeRateType()) {
      case CURRENT -> exchangeRates = retrieveCurrentExchangeRates(targetCurrency, statements, sessionCustomer);
      case HISTORICAL -> exchangeRates = retrieveHistoricalExchangeRates(targetCurrency, statements, sessionCustomer);
      case MOST_RECENT_STATEMENT_DATE ->
              exchangeRates = retrieveMostRecentStatementDateExchangeRates(targetCurrency, statements, sessionCustomer);
    }
    return new LoadExchangeRatesResponse(Objects.requireNonNullElseGet(exchangeRates, HashMap::new));
  }

  private static HashMap<String, Double> retrieveHistoricalExchangeRates(String targetCurrency,
                                                                         StatementList statements,
                                                                         SessionCustomer sessionCustomer)
          throws ConsUnitNotFoundException, ExchangeRateNotFoundException {
    return retrieveExchangeRates(targetCurrency, statements, sessionCustomer,
            statement -> statement.getStatementInfo().getStatementDate());
  }

  private static HashMap<String, Double> retrieveCurrentExchangeRates(String targetCurrency,
                                                                      StatementList statements,
                                                                      SessionCustomer sessionCustomer)
          throws ConsUnitNotFoundException, ExchangeRateNotFoundException {
    return retrieveExchangeRates(targetCurrency, statements, sessionCustomer,
            _ -> LocalDate.now());
  }

  private static HashMap<String, Double> retrieveMostRecentStatementDateExchangeRates(String targetCurrency,
                                                                                      StatementList statements,
                                                                                      SessionCustomer sessionCustomer)
          throws ConsUnitNotFoundException, ExchangeRateNotFoundException {
    LocalDate mostRecentDate = retrieveMostRecentStatementDate(statements);
    return retrieveExchangeRates(targetCurrency, statements, sessionCustomer,
            _ -> mostRecentDate);
  }

  private static HashMap<String, Double> retrieveExchangeRates(String targetCurrencyIsoCode,
                                                               StatementList statements,
                                                               SessionCustomer sessionCustomer,
                                                               Function<Statement, LocalDate> dateResolver)
          throws ConsUnitNotFoundException, ExchangeRateNotFoundException {
    HashMap<String, Double> exchangeRates = new HashMap<>();
    for (Statement statement : statements) {
      LocalDate date = dateResolver.apply(statement);
      String sourceCurrencyIsoCode = retrieveSourceCurrencyIsoCode(statement, sessionCustomer);
      CurrencyHashMap currencies = StaticDataBroker.getInstance().getCurrencyHashMap();
      Currency sourceCurrency = currencies.get(sourceCurrencyIsoCode);
      Currency targetCurrency = currencies.get(targetCurrencyIsoCode);
      ExchangeRate exchangeRate = ExchangeRateLoader.loadExchangeRate(
              sourceCurrency,
              targetCurrency,
              date
      );
      assertExchangeRateNotNull(exchangeRate, sourceCurrencyIsoCode, targetCurrencyIsoCode, date);
      exchangeRates.put(statement.getStatementInfo().getStatementKey(), exchangeRate.getFactor());
    }
    return exchangeRates;
  }

  private static String retrieveSourceCurrencyIsoCode(Statement statement, SessionCustomer sessionCustomer) throws ConsUnitNotFoundException {
    ConsUnit consUnit = sessionCustomer.getCustomer().getConsUnits().find(statement.getStatementInfo().getConsUnitKey());
    assertConsUnitNotNull(consUnit);
    return consUnit.getConsUnitInfo().getCurrencyIsoCode();
  }

  private static LocalDate retrieveMostRecentStatementDate(StatementList statements) {
    return statements.stream()
            .map(statement -> statement.getStatementInfo().getStatementDate())
            .max(LocalDate::compareTo)
            .orElse(LocalDate.now());
  }

  private static void assertExchangeRateNotNull(ExchangeRate exchangeRate,
                                                String sourceCurrency,
                                                String targetCurrency,
                                                LocalDate date) throws ExchangeRateNotFoundException {
    if (exchangeRate == null) {
      throw new ExchangeRateNotFoundException(sourceCurrency, targetCurrency, date);
    }
  }
}
