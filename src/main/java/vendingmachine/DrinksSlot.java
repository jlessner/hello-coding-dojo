package vendingmachine;

import java.util.LinkedList;
import java.util.Queue;

public class DrinksSlot {
  final int priceInCent;
  final int capacity;
  final String id;
  final Queue<Drink> stock = new LinkedList<>();

  public void checkDrinkAvailability(TotalDepositAmount totalDepositAmount) throws InsufficientPaymentException, SlotEmptyException {
    checkSufficientPayment(totalDepositAmount);
    checkSufficientStock();
  }

  public void checkSufficientPayment(TotalDepositAmount totalDepositAmount) throws InsufficientPaymentException {
    if (totalDepositAmount.totalInCent < priceInCent) {
      throw new InsufficientPaymentException();
    }
  }

  public void checkSufficientStock() throws SlotEmptyException {
    if (stock.isEmpty()) {
      throw new SlotEmptyException();
    }
  }

  public Drink poll() {
    return stock.poll();
  }

  public int getPriceInCent() {
    return priceInCent;
  }

  public DrinksSlot(String id, int priceInCent, int capacity) {
    this.id = id;
    this.priceInCent = priceInCent;
    this.capacity = capacity;
  }

  public void fillUp(Drink drink) {
    stock.add(drink);
  }
}
