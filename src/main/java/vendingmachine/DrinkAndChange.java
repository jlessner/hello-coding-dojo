package vendingmachine;

import java.util.Collection;

public class DrinkAndChange {
  public final Drink boughtDrink;
  public final Collection<Coin> change;

  public DrinkAndChange(Drink boughtDrink, Collection<Coin> change) {
    this.boughtDrink = boughtDrink;
    this.change = change;
  }
}
