package control.commands;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This command implements passing a giant. Player has 50% chance survival.
 */
public class PassGiant implements DungeonCommandController {

  @Override
  public String goCommand(Dungeon d) {
    StringBuilder sb = new StringBuilder();
    sb.append(d.passGiant());
    return sb.toString();
  }
}
