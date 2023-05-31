package dungeon;

import java.util.List;
import java.util.Map;

import location.Creature;
import location.Location;
import location.Smell;
import location.Sound;
import player.Player;
import randomizer.RandomGenerator;

/**
 * Represents the dungeon as a graph. Main interface that interacts with driver.
 * Adds treasure to caves.
 */
public interface Dungeon extends ReadonlyDungeonModel {

  /**
   * Location of player is updated through this.
   *
   * @param dir Direction to move
   */
  String playMove(String dir);

  /**
   * Pick the treasure at current location.
   */
  void treasurePick();

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
   * Player picks arrow.
   */
  void arrowPick();

  /**
   * Player shoots arrow.
   */
  void shootArrow(int distance, String direction);

  /**
   * Returns -3 if player is falls in pit.
   * Returns -1 if player is dead.
   * Returns 0 if player is alive and reached end.
   * Returns 1 if player is alive.
   * Returns 2 if player is alive and escaped an Otyugh.
   */
  int playerKillOrNot();

  /**
   * Returns true if Otyugh was hit else false.
   */
  boolean getHit();

  /**
   * Gets the treasure percentage.
   */
  int getTreasureAmt();

  /**
   * Checks if giant encountered by player.
   */
  boolean checkGiantEncounter();

  /*
   * Kills Giant.
   */
  String defeatGiant();

  /**
   * Copy of Giant.
   */
  Creature getGiantCopy();

  /**
   * Pass the giant with 50% chance of survival.
   */
  String passGiant();

  /**
   * Get coordinates of dungeon.
   */
  Map<Integer, List<Integer>> getCoordMap();


  /**
   * Get copy of Model for reuse.
   */
  Dungeon getModelCopy();


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
   * Get randomizer.
   */
  RandomGenerator getRandom();

  /**
   * Get number of Otyugh.
   */
  int getOtyughNum();

  /**
   * Get starting copy of caveList.
   */
  List<Location> getNewCaveList();

  /**
   * Get list of edges of dungeon.
   */
  List<Edge> getDungeon();

  void setRows(int rows);

  void setColumns(int columns);

  void setInterconnectivity(int interconnectivity);

  void setTreasurePercent(int treasurePercent);

  void setWrapping(boolean wrapping);

  void setOutyughNum(int outyughNum);

  void makeCopy();

}
