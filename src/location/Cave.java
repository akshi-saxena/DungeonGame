package location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents a Cave. It offers all the operations mandated by the
 * Location interface. Cave is a node in the dungeon graph. Cave can have treasure.
 */
public class Cave implements Location {
  private int caveId;
  private List<Treasure> treasureList;
  private Map<Direction, Location> directionList;
  private LocationType locType;
  private boolean isVisited;
  private int level;
  private int arrow;
  private Creature c;
  private boolean creaturePresent;
  private boolean isPitPresent;
  private boolean isThiefPresent;

  /**
   * Constructor for a cave.
   *
   * @param id ID of the cave.
   */
  public Cave(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("Invalid id");
    }
    this.caveId = id;
    this.isVisited = false;
    this.arrow = 0;
    this.c = null;
    this.creaturePresent = false;
    this.isPitPresent = false;
    this.isThiefPresent = false;
    this.directionList = new TreeMap<>();
    this.treasureList = new ArrayList<>();
  }

  /**
   * Copy constructor of cave.
   */
  public Cave(Location cave) {
    this.caveId = cave.getCaveId();
    this.isVisited = cave.getVisited();
    this.arrow = cave.getArrow();
    if (cave.getCreaturePresent()) {
      this.c = cave.getOtyugh();
    } else {
      this.c = null;
    }
    this.creaturePresent = cave.getCreaturePresent();
    this.isPitPresent = cave.isPitPresent();
    this.isThiefPresent = cave.isThiefPresent();
    this.locType = cave.getLocType();
    this.treasureList = cave.getTreasureList();
    this.directionList = cave.getDirectionList();
  }

  @Override
  public void setVisited(boolean visited) {
    isVisited = visited;
  }

  @Override
  public void setTreasure(int r) {
    treasureList.add(Treasure.values()[r]);
  }

  @Override
  public void setArrows(int r) {
    for (int i = 0; i < r; i++) {
      arrow++;
    }
  }

  @Override
  public void decArrowCount() {
    this.arrow--;
  }

  @Override
  public int getArrow() {
    return arrow;
  }


  @Override
  public boolean getVisited() {
    return isVisited;
  }

  @Override
  public int getCaveId() {
    return caveId;
  }

  @Override
  public void addOtyugh() {
    this.c = new Otyugh();
    this.creaturePresent = true;
  }

  @Override
  public Creature getOtyugh() {
    Creature cnew = new Otyugh(c);
    return cnew;
  }

  @Override
  public void hitOutyugh() {
    if (this.c.getHealth() > 0) {
      this.c.hit();
    }
    if (this.c.getHealth() == 0) {
      creaturePresent = false;
    }
  }

  @Override
  public void clearTreasureList() {
    treasureList.clear();
  }

  @Override
  public List<Treasure> getTreasureList() {
    List<Treasure> t = new ArrayList<>();
    t.addAll(treasureList);
    return t;
  }

  @Override
  public void setLocType(LocationType loc) {
    this.locType = loc;
  }

  @Override
  public LocationType getLocType() {
    return locType;
  }

  @Override
  public void setDirectionList(Direction dir, Location cave) {
    directionList.put(dir, cave);
  }

  @Override
  public Map<Direction, Location> getDirectionList() {
    Map<Direction, Location> copyDirList = new HashMap<>();
    copyDirList.putAll(directionList);
    return copyDirList;
  }

  @Override
  public boolean getCreaturePresent() {
    return creaturePresent;
  }

  @Override
  public boolean isPitPresent() {
    return isPitPresent;
  }

  @Override
  public void setPitPresent(boolean pitPresent) {
    isPitPresent = pitPresent;
  }

  @Override
  public void setThiefPresent(boolean thiefPresent) {
    this.isThiefPresent = true;
  }

  @Override
  public boolean isThiefPresent() {
    return isThiefPresent;
  }

  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public void setLevel(int src) {
    this.level = src;
  }

}
