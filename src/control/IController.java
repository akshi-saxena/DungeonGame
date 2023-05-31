package control;

import dungeon.Dungeon;

/**
 * The controller interface for the dungeon. The function here have been
 * designed to give control to the controller.
 */

public interface IController {

  /**
   * Execute a single game given a Dungeon Model.
   *
   * @param d a non-null dungeon model
   */
  void playGame(Dungeon d);

  /**
   * Execute a single game with view.
   */
  void playGUIGame();

  /**
   * Handles cell click.
   */
  void handleCellClick(int ind);
}
