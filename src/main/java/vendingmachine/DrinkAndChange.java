package vendingmachine;

import java.util.Collection;

public class DrinkAndChange {
  public final Drink boughtDrink;
  public final Collection<Coin> change;
  public final Errorcode error;

  public DrinkAndChange(Drink boughtDrink, Collection<Coin> change) {
    this.boughtDrink = boughtDrink;
    this.change = change;
    this.error = null;
  }

  public DrinkAndChange(Errorcode error) {
    this.boughtDrink = null;
    this.change = null;
    this.error = error;
  }
}
