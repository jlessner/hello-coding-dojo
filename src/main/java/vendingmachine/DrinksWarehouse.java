package vendingmachine;

import java.util.HashMap;
import java.util.Map;

public class DrinksWarehouse {
  Map<String, DrinksSlot> slots = new HashMap<>();

  public DrinksSlot findSlot(String slotID) {
    return slots.get(slotID);
  }

  public void add(DrinksSlot slot) {
    slots.put(slot.id, slot);
  }
}
