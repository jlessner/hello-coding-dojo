package vendingmachine;

import java.util.LinkedList;
import java.util.Queue;

public class DrinksSlot {
  final int priceInCent;
  final int capacity;
  final String id;
  final Queue<Drink> stock = new LinkedList<>();

  public boolean paymentSufficient(TotalDepositAmount totalDepositAmount) {
    return totalDepositAmount.totalInCent >= priceInCent;
  }

  public boolean isEmpty() {
    return stock.isEmpty();
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
