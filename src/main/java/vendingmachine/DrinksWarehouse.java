package vendingmachine;

import java.util.HashMap;
import java.util.Map;

public class DrinksWarehouse {
  Map<String, DrinksSlot> slots = new HashMap<>();

  public DrinksSlot findSlot(String slotID) throws UnknownSlotException {
    DrinksSlot slot = slots.get(slotID);
    if (slot == null) {
      throw new UnknownSlotException();
    }
    return slot;
  }

  public void add(DrinksSlot slot) {
    slots.put(slot.id, slot);
  }
}
