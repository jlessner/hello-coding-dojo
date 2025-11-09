package de.sundn.bars.server.websocket.processing.commands.reporting.exchangerate;

import de.sundn.bars.server.websocket.processing.commands.DataTransferObject;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum ExchangeRateType implements DataTransferObject {
  CURRENT(1),
  HISTORICAL(2),
  MOST_RECENT_STATEMENT_DATE(3);

  public final int key;

  ExchangeRateType(int value) {
    this.key = value;
  }

  @Nullable
  public static ExchangeRateType byKey(int key) {
    return Arrays.stream(ExchangeRateType.values())
            .filter(exchangeRateType -> exchangeRateType.key == key)
            .findFirst()
            .orElse(null);
  }
}