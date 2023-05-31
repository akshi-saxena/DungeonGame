package control.commands;

import control.DungeonCommandController;
import dungeon.Dungeon;
import location.Treasure;

/**
 * This command implements player picking treasure.
 */
public class PickTreasure implements DungeonCommandController {

  @Override
  public String goCommand(Dungeon d) {
    StringBuilder sb = new StringBuilder();
    if (d == null) {
      throw new IllegalArgumentException("model cannot be null");
    }

    try {
      d.treasurePick();
      sb.append("\n").append(d.getPlayerCopy().getName()).append(" has treasure: ");
      for (Treasure t : d.getPlayerCopy().getTreasureCollected().keySet()) {
        sb.append(t.name()).append(": ")
                .append(String.valueOf(d.getPlayerCopy().getTreasureCollected().get(t)))
                .append(", ");
      }
    } catch (IllegalArgumentException e) {
      sb.append("No treasure at location");
    }
    return sb.toString();
  }

}
