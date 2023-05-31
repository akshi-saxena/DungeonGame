package control.commands;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This command implements shooting an arrow in a given direction at a specific distance.
 */
public class Shoot implements DungeonCommandController {
  private int dist;
  private String dir;

  /**
   * Constructor.
   *
   * @param dist the direction to move.
   * @param dir  the direction to move.
   */
  public Shoot(int dist, String dir) {
    this.dist = dist;
    this.dir = dir;
  }

  @Override
  public String goCommand(Dungeon d) {
    StringBuilder sb = new StringBuilder();
    if (d == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    try {
      d.shootArrow(this.dist, this.dir);
      if (d.getHit()) {
        sb.append("Otyugh hit!");
      } else {
        sb.append("You missed");
      }
    } catch (IllegalArgumentException e) {
      sb.append(e.getMessage());
    }
    return sb.toString();
  }
}
