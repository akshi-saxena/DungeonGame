package control.commands;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This command implements move of the player in NORTH, SOUTH, EAST or WEST direction.
 */
public class Move implements DungeonCommandController {

  private String dir;

  /**
   * Constructor.
   *
   * @param dir the direction to move.
   */
  public Move(String dir) {
    this.dir = dir;
  }

  @Override
  public String goCommand(Dungeon d) {
    StringBuilder sb = new StringBuilder();
    if (d == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    try {
      sb.append(d.playMove(dir));
    } catch (IllegalArgumentException e) {
      sb.append("Invalid direction");
    }
    return sb.toString();

  }

}
