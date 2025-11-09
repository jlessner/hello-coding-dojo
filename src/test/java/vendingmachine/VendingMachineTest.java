package vendingmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static vendingmachine.CoinValue.FiftyCent;
import static vendingmachine.CoinValue.OneEuro;

public class VendingMachineTest {
  private static final String DRINKSLOT_ID = "A1";
  private static VendingMachine vendingMachine;
  private static Drink drink = new Drink();

  @BeforeEach
  public void setup() throws Exception{
    vendingMachine = new VendingMachine();
    vendingMachine.configureCoinsSlot(new CoinsSlot(OneEuro, 2));
    vendingMachine.configureCoinsSlot(new CoinsSlot(FiftyCent, 2));
    vendingMachine.configureDrinksSlot(new DrinksSlot(DRINKSLOT_ID, OneEuro.inCent, 1));

    drink = new Drink();
    vendingMachine.fillUp(DRINKSLOT_ID, drink);
  }

  @Test
  void testMinimalSuccessCase() throws Exception{
    // Given
    vendingMachine.insertCoin(new Coin(OneEuro));

    // When
    DrinkAndChange drinkAndChange = vendingMachine.buyDrink(DRINKSLOT_ID);

    // Then
    assertEquals(drink, drinkAndChange.boughtDrink);
    assertEquals(0, drinkAndChange.change.size());
  }

  @Test
  void testDrinksSlotEmpty() throws Exception {
    vendingMachine.insertCoin(new Coin(OneEuro));
    vendingMachine.buyDrink(DRINKSLOT_ID);

    vendingMachine.insertCoin(new Coin(OneEuro));
    assertThrows(SlotEmptyException.class,() -> vendingMachine.buyDrink(DRINKSLOT_ID));
  }

  @Test
  void testInsufficiantPayment() throws Exception {
    vendingMachine.insertCoin(new Coin(FiftyCent));
    assertThrows(InsufficientPaymentException.class,() -> vendingMachine.buyDrink(DRINKSLOT_ID));
  }

  @Test
  void testNoChange() throws Exception {
    vendingMachine.insertCoin(new Coin(OneEuro));
    vendingMachine.insertCoin(new Coin(FiftyCent));
    assertThrows(CantChangeException.class,() -> vendingMachine.buyDrink(DRINKSLOT_ID));
  }

}
