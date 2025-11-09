package vendingmachine;

import java.util.LinkedList;
import java.util.Queue;

public class CoinsSlot {
  final CoinValue coinValue;
  final int capacity;
  final Queue<Coin> stock = new LinkedList<>();

  public CoinsSlot(CoinValue coinValue, int capacity) {
    this.coinValue = coinValue;
    this.capacity = capacity;
  }

  public void add(Coin coin) throws OverpaidException {
	  assert coin.value == coinValue;
    if (stock.size() == capacity) {
      throw new OverpaidException();
    }
    stock.add(coin);
  }

  public boolean matchingFor(Coin coin) {
    return coinValue == coin.value;
  }
}
