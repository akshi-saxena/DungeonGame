package location;

import java.util.List;
import java.util.Map;

/**
 * Represents the location in the dungeon. It can be a cave or a tunnel.
 */
public interface Location {

  /**
   * Gets cave ID.
   */
  int getCaveId();

  /**
   * Sets treasure in the cave.
   */
  void setTreasure(int r);

  /**
   * Gets treasure list in a cave.
   */
  List<Treasure> getTreasureList();

  /**
   * Clears treasure in the cave.
   */
  void clearTreasureList();

  /**
   * Sets location type (tunnel or cave) of the cave.
   */
  void setLocType(LocationType loc);

  /**
   * Gets location type (tunnel or cave) of the cave.
   */
  LocationType getLocType();

  /**
   * Sets the cave as visited or not for BST.
   */
  void setVisited(boolean visited);

  /**
   * Gets if the cave is visited or not for BST.
   */
  boolean getVisited();

  /**
   * Sets the neighbours of the cave in the BST.
   */
  void setDirectionList(Direction dir, Location cave);

  /**
   * Gets the neighbours of the cave in the BST.
   */
  Map<Direction, Location> getDirectionList();

  /**
   * Sets the Arrow count at a location.
   */
  void setArrows(int r);

  /**
   * Gets the Arrow count in a location.
   */
  int getArrow();

  /**
   * Adds the Otyugh at the location.
   */
  void addOtyugh();

  /**
   * Gets the Otyugh at the location.
   */
  Creature getOtyugh();

  /**
   * Gets if creature is present at the location.
   */
  boolean getCreaturePresent();

  /**
   * Decreases the health of the Otyugh.
   */
  void hitOutyugh();

  /**
   * Decreases the arrow count.
   */
  void decArrowCount();

  /**
   * Adds a pit to the cave.
   */
  void setPitPresent(boolean pitPresent);

  /**
   * Returns true if pit is present else false.
   */
  boolean isPitPresent();

  /**
   * Adds a thief to the cave.
   */
  void setThiefPresent(boolean thiefPresent);

  /**
   * Returns true if thief is present else false.
   */
  boolean isThiefPresent();

  /**
   * Get level.
   */
  int getLevel();

  /**
   * Set level.
   */
  void setLevel(int src);
}
