package control.commands;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This command implements player picking an arrow.
 */
public class PickArrow implements DungeonCommandController {

  @Override
  public String goCommand(Dungeon d) {
    StringBuilder sb = new StringBuilder();
    if (d == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    try {
      d.arrowPick();
      sb.append("\n").append(d.getPlayerCopy().getName()).append(" has total arrows: ")
              .append(d.getPlayerCopy().getTotalArrows());
    } catch (IllegalArgumentException e) {
      sb.append("No arrows at location");
    }
    return sb.toString();
  }

}
