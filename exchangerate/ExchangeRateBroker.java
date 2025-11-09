package de.sundn.bars.server.objectbroker;

import de.sundn.bars.server.business.objects.businessobjects.ExchangeRate;
import de.sundn.bars.server.business.objects.businessobjects.ExchangeRateList;
import org.jetbrains.annotations.NotNull;
import pm.pride.PreparedInsert;
import pm.pride.ResultIterator;
import pm.pride.WhereCondition;

import java.sql.SQLException;
import java.time.LocalDate;

public class ExchangeRateBroker extends AbstractBrokerPriDE<ExchangeRate, ExchangeRateDBA> {

  @Override
  protected @NotNull ExchangeRateDBA dba(ExchangeRate entity) {
    return new ExchangeRateDBA(entity);
  }

  @Override
  protected @NotNull ExchangeRate entity() {
    return new ExchangeRate();
  }

  /**
   * Saves the given {@link ExchangeRate} object for testing purposes.
   * An ArchUnit Tests checks if this method is only used in tests!
   *
   * @param exchangeRate The {@link ExchangeRate} object to be saved.
   */
  public void saveForTestsONLY(ExchangeRate exchangeRate) {
    createOrUpdate(exchangeRate);
  }

  /**
   * Saves the given {@link ExchangeRate} objects for testing purposes.
   * An ArchUnit Tests checks if this method is only used in tests!
   *
   * @param exchangeRates The {@link ExchangeRate} objects to be saved.
   */
  public void saveForTestsONLY(ExchangeRateList exchangeRates) {
    try (PreparedInsert insert = new PreparedInsert(ExchangeRateDBA.red)) {
      for (ExchangeRate exchangeRate : exchangeRates) {
        insert.addBatch(exchangeRate);
      }
      insert.executeBatch();
    } catch (SQLException | ReflectiveOperationException x) {
      throw new PFWException(x);
    }
  }

  public ExchangeRateList queryAll() {
    try (ResultIterator resultIterator = dba().queryAll()) {
      ExchangeRateList exchangeRateList = new ExchangeRateList();
      exchangeRateList.addAll(resultIterator.toList(ExchangeRate.class));
      return exchangeRateList;
    } catch (SQLException sql) {
      throw new PFWException(sql);
    }
  }

  public ExchangeRate queryBySourceAndTargetAndDate(String sourceCurrencyUnitIsoCode,
                                                    String targetCurrencyUnitIsoCode,
                                                    LocalDate localDate) {
    WhereCondition where = new WhereCondition(ExchangeRateDBA.COL_CCY_ISO_CODE_SOURCE, sourceCurrencyUnitIsoCode)
            .and(ExchangeRateDBA.COL_CCY_ISO_CODE_TARGET, targetCurrencyUnitIsoCode)
            .and(ExchangeRateDBA.COL_VALID_FROM, WhereCondition.Operator.LESSEQUAL, localDate)
            .and(new WhereCondition(ExchangeRateDBA.COL_VALID_TO, WhereCondition.Operator.GREATEREQUAL, localDate)
                    .or(ExchangeRateDBA.COL_VALID_TO, null));
    try (ResultIterator resultIterator = dba().query(where)) {
      return resultIterator.isNull() ? null : resultIterator.getObject(ExchangeRate.class);
    } catch (SQLException sql) {
      throw new PFWException(sql);
    }
  }

}
