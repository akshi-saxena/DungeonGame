package dungeon;

import java.util.List;
import java.util.Map;

import location.Creature;
import location.Location;
import location.Smell;
import location.Sound;
import player.Player;


/**
 * Read only interface for Dungeon Model.
 */
public interface ReadonlyDungeonModel {

  /**
   * Get end location.
   */
  Location getEnd();

  /**
   * Get start location.
   */
  Location getStart();

  /**
   * Get list of caves.
   */
  List<Location> getCavesList();

  /**
   * Gets the player description.
   */
  Player getPlayerCopy();

  /**
   * Gives high, low or none depending on the distance of player from the monster.
   */
  Smell detectSmell();

  /**
   * Gives high, low or none depending on the distance of player from the pit.
   */
  Sound detectPit();

  /**
   * Returns -1 if player is dead.
   * Returns 0 if player is alive and reached end.
   * Returns 1 if player is alive.
   * Returns 2 if player is alive and escaped an Otyugh.
   */
  int playerKillOrNot();


  /**
   * Checks if giant encountered by player.
   */
  boolean checkGiantEncounter();

  /**
   * Copy of Giant.
   */
  Creature getGiantCopy();

  /**
   * Get rows.
   */
  int getRows();

  /**
   * Get columns.
   */
  int getCols();

  /**
   * Get coordinates.
   */
  Map<Integer, List<Integer>> getCoordMap();

  /**
   * Get interconnectivity.
   */
  int getInterconnectivity();

  /**
   * Get treasure percent.
   */
  int getTreasurePercent();

  /**
   * Get wrapping.
   */
  boolean getWrapping();

  /**
   * Get number of Otyugh.
   */
  int getOtyughNum();

  void makeCopy();
}
