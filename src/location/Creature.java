package location;

/**
 * Represents the monster in the dungeon.
 */
public interface Creature {

  /**
   * Gets health of the creature.
   */
  int getHealth();

  /**
   * Decreases the health of the Otyugh.
   */
  void hit();

  /**
   * Gets the player's current location.
   */
  default Location getLoc() {
    return null;
  }

  /**
   * Sets the player's current location.
   */
  default void setLoc(Location l) {
    //do nothing
  }
}
