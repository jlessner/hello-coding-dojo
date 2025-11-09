package vendingmachine;

import java.util.Collection;

public class VendingMachine {
  final CashBox cashbox = new CashBox();
  final DrinksWarehouse warehouse = new DrinksWarehouse();

  /************** Methods for Customer Interface *********************/

  public TotalDepositAmount insertCoin(Coin coin) throws OverpaidException {
    cashbox.addCoin(coin);
    return cashbox.getTotalDepositAmount();
  }

  public DrinkAndChange buyDrink(String slotID) throws UnknownSlotException, InsufficientPaymentException, SlotEmptyException, CantChangeException {
    DrinksSlot slot = warehouse.findSlot(slotID);
    slot.checkDrinkAvailability(cashbox.getTotalDepositAmount());
    Collection<Coin> change = cashbox.calculateChange(slot.getPriceInCent());
    cashbox.clearDeposit();
    Drink drink = slot.poll();
    return new DrinkAndChange(drink, change);
  }

  /************** Methods for Maintenance Interface *********************/

  public void fillUp(Collection<Coin> coins) throws FillUpException, OverpaidException {
    cashbox.fillUp(coins);
  }

  public void fillUp(String slotId, Drink drink) throws FillUpException, UnknownSlotException {
    warehouse.findSlot(slotId).fillUp(drink);
  }

  /************* Internal configuration functionality ******************/

  public void configureDrinksSlot(DrinksSlot slot) {
    warehouse.add(slot);
  }

  public void configureCoinsSlot(CoinsSlot slot) {
    cashbox.add(slot);
  }

}
