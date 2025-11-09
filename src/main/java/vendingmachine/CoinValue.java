package vendingmachine;

public enum CoinValue {
  TenCent(10),
  TwentyCent(20),
  FiftyCent(50),
  OneEuro(100),
  TwoEuro(200);

  final int inCent;

  CoinValue(int inCent) {
    this.inCent = inCent;
  }

  int inCent() {
    return inCent;
  }
}
