package control;

import dungeon.Dungeon;

/**
 * This interface represents commands in the controller.
 */
public interface DungeonCommandController {
  /**
   * Starting point for the controller.
   *
   * @param d the model to use
   */
  String goCommand(Dungeon d);
}
