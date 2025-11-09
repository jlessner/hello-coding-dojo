package vendingmachine;

public class Coin {
  public final CoinValue value;

  public Coin(CoinValue value) {
    this.value = value;
  }

  public int valueInCent() {
    return value.inCent();
  }
}
