package player;

import java.util.Map;

import location.Location;
import location.Treasure;

/**
 * Represents the player. Has treasure collected and location.
 */
public interface Player {

  /**
   * Gets the player's name.
   */
  public String getName();

  /**
   * Gets the player's current location.
   */
  Location getLoc();

  /**
   * Sets the player's current location.
   */
  void setLoc(Location l);

  /**
   * Gets the player's collected treasure.
   */
  Map<Treasure, Integer> getTreasureCollected();

  /**
   * Player picks the treasure.
   */
  void pickTreasure();

  /**
   * Player shoots arrow.
   */
  void shootArrows();

  /**
   * Updates arrow count.
   */
  void updateArrows();

  /**
   * Gets the total arrow count with player.
   */
  int getTotalArrows();

  /**
   * Returns -1 if player is dead.
   * Returns 0 if player has 50% chance of survival.
   * Returns 1 if player is alive.
   * Returns 2 if player is alive and escapes Otyugh.
   * Returns 3 if player falls in pit.
   */
  int isAlive();

  /**
   * Clears treasure collected by player.
   */
  void clearTreasure();

}
