package vendingmachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CashBox {
  Collection<Coin> deposit = new ArrayList<>();
  List<CoinsSlot> coinsSlots = new ArrayList<>();

  public void addCoin(Coin coin) throws OverpaidException {
    CoinsSlot slot = findMatchingSlot(coin);
    slot.add(coin);
    deposit.add(coin);
  }

  private CoinsSlot findMatchingSlot(Coin coin) {
    return coinsSlots
            .stream()
            .filter(slot -> slot.matchingFor(coin))
            .findFirst()
            .orElseThrow();
  }

  public TotalDepositAmount getTotalDepositAmount() {
    int sum = deposit.stream().mapToInt(Coin::valueInCent).sum();
    return new TotalDepositAmount(sum);
  }

  public Collection<Coin> calculateChange(int priceToPay) {
    if (priceToPay < getTotalDepositAmount().totalInCent) {
      return null;
    }
    return new  ArrayList<>();
  }

  public void clearDeposit() {
    deposit.clear();
  }
  
  public void fillUp(Collection<Coin> coins) throws FillUpException, OverpaidException {
    for (Coin coin : coins) {
      findMatchingSlot(coin).add(coin);
    }
  }

  public void add(CoinsSlot slot) {
    coinsSlots.add(slot);
  }
}
