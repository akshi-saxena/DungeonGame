package player;

import java.util.Map;
import java.util.TreeMap;

import location.Cave;
import location.Location;
import location.LocationType;
import location.Treasure;

/**
 * This class represents a Player. It offers all the operations mandated by the
 * Player interface. It has a list of treasures collected and a current location.
 */
public class PlayerImpl implements Player {

  private String name;
  private Location loc;
  private Map<Treasure, Integer> treasureCollected;
  private int totalArrows;

  /**
   * Constructor for the player.
   *
   * @param name  Name of the player
   * @param start Start location of the player
   */
  public PlayerImpl(String name, Location start) {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Name can't be null");
    }
    this.name = name;
    this.loc = start;
    treasureCollected = new TreeMap<>();
    this.treasureCollected.put(Treasure.DIAMOND, 0);
    this.treasureCollected.put(Treasure.RUBY, 0);
    this.treasureCollected.put(Treasure.SAPPHIRE, 0);
    this.totalArrows = 3;
  }

  /**
   * Copy Constructor of Player.
   *
   * @param p Player
   */
  public PlayerImpl(Player p) {
    this.name = p.getName();
    this.loc = p.getLoc();
    this.treasureCollected = p.getTreasureCollected();
    this.totalArrows = p.getTotalArrows();
  }

  @Override
  public Location getLoc() {
    Location l = new Cave(this.loc);
    return l;
  }

  @Override
  public void setLoc(Location l) {
    this.loc = l;
  }

  @Override
  public void pickTreasure() {
    if (loc.getLocType() == LocationType.CAVE) {
      for (Treasure t : loc.getTreasureList()) {
        int c = this.treasureCollected.get(t);
        this.treasureCollected.put(t, c + 1);
      }
      loc.clearTreasureList();
    }
  }

  @Override
  public void clearTreasure() {
    treasureCollected.clear();
    this.treasureCollected.put(Treasure.DIAMOND, 0);
    this.treasureCollected.put(Treasure.RUBY, 0);
    this.treasureCollected.put(Treasure.SAPPHIRE, 0);
  }

  @Override
  public Map<Treasure, Integer> getTreasureCollected() {
    return new TreeMap<>(this.treasureCollected);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void shootArrows() {
    this.totalArrows--;
  }

  @Override
  public void updateArrows() {
    this.totalArrows++;
  }

  @Override
  public int getTotalArrows() {
    return totalArrows;
  }

  @Override
  public int isAlive() {
    if (loc.getCreaturePresent()) {
      if (loc.getOtyugh().getHealth() == 100) {
        return -1;
      } else if (loc.getOtyugh().getHealth() == 50) {
        return 0;
      }
    }
    return 1;
  }


}
