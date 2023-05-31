package control.commands;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This command implements killing of giant (moving monster).
 */
public class KillGiant implements DungeonCommandController {

  @Override
  public String goCommand(Dungeon d) {
    if (d == null) {
      throw new IllegalArgumentException("model cannot be null");
    }

    StringBuilder sb = new StringBuilder();
    sb.append(d.defeatGiant());
    return sb.toString();
  }
}
