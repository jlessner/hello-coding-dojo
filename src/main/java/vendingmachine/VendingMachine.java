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

  public DrinkAndChange buyDrink(String slotID) {
    DrinksSlot slot = warehouse.findSlot(slotID);
    if (slot != null) {
      if (slot.paymentSufficient(cashbox.getTotalDepositAmount())) {
        if (slot.isEmpty()) {
          return new DrinkAndChange(Errorcode.SlotEmpty);
        }
        Collection<Coin> change = cashbox.calculateChange(slot.getPriceInCent());
        if (change == null) {
          return new DrinkAndChange(Errorcode.CantChange);
        }
        cashbox.clearDeposit();
        Drink drink = slot.poll();
        return new DrinkAndChange(drink, change);
      }
      else {
        return new DrinkAndChange(Errorcode.InsufficientPayment);
      }
    }
    else {
      return new DrinkAndChange(Errorcode.UnknownSlot);
    }
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
