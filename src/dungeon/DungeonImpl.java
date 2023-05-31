package dungeon;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import location.Cave;
import location.Creature;
import location.Direction;
import location.Giant;
import location.Location;
import location.LocationType;
import location.Smell;
import location.Sound;
import location.Treasure;
import player.Player;
import player.PlayerImpl;
import randomizer.RandomGenerator;

/**
 * This class represents a Dungeon. It offers all the operations mandated by the
 * Dungeon interface.
 */
public class DungeonImpl implements Dungeon {

  private int rows;
  private int columns;
  private int interconnectivity;
  private int treasurePercent;
  private int treasurePer;
  private boolean wrapping;
  private boolean hit;
  private List<Edge> dungeon = new ArrayList<>();
  private final List<Edge> allEdges = new ArrayList<>();
  private List<Location> cavesList = new ArrayList<>();
  private List<Location> newCaveList;
  private Map<Integer, List<Integer>> coordMap;
  private Location start;
  private Location end;
  private Player p;
  private Creature g;
  private RandomGenerator rand;
  private int outyughNum;

  private Dungeon modelCopy;


  /**
   * Constructor for dungeonImpl class. Creates a dungeon graph.
   *
   * @param name              Name of the Player
   * @param rows              Number of rows
   * @param cols              Number of cols
   * @param interconnectivity Degree of interconnectivity.
   * @param treasurePercent   Percent of caves having treasure.
   * @param wrap              Wrapping turned on or off
   * @param r                 Randomizer
   * @param outyughNum        Number of Otyughs
   */
  public DungeonImpl(String name, int rows, int cols, int interconnectivity,
                     int treasurePercent, boolean wrap, RandomGenerator r, int outyughNum) {

    if (rows < 0 || cols < 0 || interconnectivity < 0 || treasurePercent < 0) {
      throw new IllegalArgumentException("Parameters can't be negative");
    }

    this.rows = rows;
    this.columns = cols;
    this.interconnectivity = interconnectivity;
    this.treasurePercent = treasurePercent;
    this.wrapping = wrap;
    this.rand = r;
    this.outyughNum = outyughNum;

    createNodes(rows, cols);

    createDungeon();
    createDirectionList();

    coordMap = new HashMap<>();
    mapToCoord();

    setLocation();
    addTreasure();
    addArrows();
    setStartEndNodes();

    addOutyugh();

    addPit();
    addThief();

    g = new Giant(getGiantStart());

    this.hit = false;

    p = new PlayerImpl(name, start);

    makeCopy();

  }

  /**
   * Copy Constructor of Dungeon.
   */
  public DungeonImpl(Dungeon d) {
    this.rows = d.getRows();
    this.columns = d.getCols();
    this.interconnectivity = d.getInterconnectivity();
    this.treasurePercent = d.getTreasurePercent();
    this.wrapping = d.getWrapping();
    this.rand = d.getRandom();
    this.outyughNum = d.getOtyughNum();
    this.start = d.getStart();
    this.end = d.getEnd();
    cavesList = d.getCavesList();
    newCaveList = d.getNewCaveList();
    this.dungeon = d.getDungeon();

    d.getPlayerCopy().clearTreasure();
    this.coordMap = d.getCoordMap();

    cavesList = d.getCavesList();

    g = d.getGiantCopy();

    this.hit = d.getHit();

    p = new PlayerImpl(d.getPlayerCopy().getName(), d.getStart());
  }

  @Override
  public void makeCopy() {
    newCaveList = new ArrayList<>();
    newCaveList.addAll(cavesList);
    modelCopy = new DungeonImpl(this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Dungeon Nodes:\n");
    for (Location l : cavesList) {
      for (Direction d : l.getDirectionList().keySet()) {
        sb.append(l.getCaveId()).append("-").append(d.toString())
                .append("-").append(l.getDirectionList().get(d).getCaveId() + "\t");
      }
    }

    sb.append("\n").append(dungeon.toString());
    sb.append("\nStart Node: ").append(start.getCaveId());
    sb.append("\nEnd Node: ").append(end.getCaveId());

    sb.append("\n\nTreasures:");
    for (Location l : cavesList) {
      if (!l.getTreasureList().isEmpty()) {
        sb.append("\nNode").append(l.getCaveId()).append(" has ");
      }
      for (Treasure t : l.getTreasureList()) {
        sb.append(t.name()).append("\t");
      }
    }
    sb.append("\nArrows: ");
    for (Location l : cavesList) {
      if (l.getArrow() > 0) {
        sb.append("\nNode").append(l.getCaveId()).append(" has ")
                .append(l.getArrow()).append(" arrows.");
      }
    }


    sb.append("\nCreatures:\n");
    for (Location l : cavesList) {
      if (l.getCreaturePresent()) {
        if (l.getOtyugh().getHealth() > 0) {
          sb.append(l.getCaveId()).append(" has Otyugh").append("\n");
        }
      }
    }

    return sb.toString();
  }

  @Override
  public String playMove(String dir) {
    StringBuilder sb = new StringBuilder();

    sb.append(playerMove(dir));

    if (checkThiefEncounter()) {
      sb.append("Oh no! You ran into a thief and lost all your treasure!");
    }

    if (playerKillOrNot() == 1 && g.getHealth() == 100 && !checkGiantEncounter()) {
      moveGiant();
    }

    return sb.toString();
  }

  private String playerMove(String dir) {
    StringBuilder sb = new StringBuilder();
    if (!EnumSet.allOf(Direction.class).contains(Direction.valueOf(dir))) {
      throw new IllegalArgumentException("Wrong direction");
    }
    switch (Direction.valueOf(dir)) {
      case EAST:
        if (p.getLoc().getDirectionList().containsKey(Direction.EAST)) {
          p.setLoc(p.getLoc().getDirectionList().get(Direction.EAST));
        } else {
          sb.append("Not a valid move");
        }
        break;
      case WEST:
        if (p.getLoc().getDirectionList().containsKey(Direction.WEST)) {
          p.setLoc(p.getLoc().getDirectionList().get(Direction.WEST));
        } else {
          sb.append("Not a valid move");
        }
        break;
      case SOUTH:
        if (p.getLoc().getDirectionList().containsKey(Direction.SOUTH)) {
          p.setLoc(p.getLoc().getDirectionList().get(Direction.SOUTH));
        } else {
          sb.append("Not a valid move");
        }
        break;
      case NORTH:
        if (p.getLoc().getDirectionList().containsKey(Direction.NORTH)) {
          p.setLoc(p.getLoc().getDirectionList().get(Direction.NORTH));
        } else {
          sb.append("Not a valid move");
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid String");
    }
    return sb.toString();
  }

  private boolean checkThiefEncounter() {
    if (p.getLoc().isThiefPresent()) {
      p.clearTreasure();
      return true;
    }
    return false;
  }

  private void moveGiant() {
    List dirList = new ArrayList<Direction>();
    g.getLoc().getDirectionList().forEach((k, v) -> {
      dirList.add(k);
    });
    rand.shuffleList(dirList);

    List<Location> caveLi = new ArrayList<>(cavesList);

    Location l;
    do {
      rand.shuffleList(caveLi);
      int r = rand.getVal(0, caveLi.size() - 1);
      l = caveLi.get(r);
      g.setLoc(caveLi.get(r));
    }
    while (l.isPitPresent() || l.getCreaturePresent());

  }

  @Override
  public boolean checkGiantEncounter() {
    return p.getLoc().getCaveId() == g.getLoc().getCaveId();
  }

  @Override
  public String defeatGiant() {
    StringBuilder sb = new StringBuilder();
    g.hit();
    for (int i = 0; i < 4; i++) {
      p.shootArrows();
    }
    sb.append("You defeated a Giant!");
    return sb.toString();
  }

  @Override
  public String passGiant() {
    StringBuilder sb = new StringBuilder();
    boolean alive = rand.getVal(0, 1) == 1;
    if (alive) {
      g.hit();
      sb.append("You defeated a Giant!");
      return sb.toString();
    } else {
      sb.append("You were killed by giant!");
    }
    return sb.toString();
  }

  @Override
  public int playerKillOrNot() {
    boolean alive;
    if (p.isAlive() == 0) {
      alive = rand.getVal(0, 1) == 1;
    } else if (p.isAlive() == -1) {
      alive = false;
    } else {
      alive = true;
    }

    if (!alive) {
      return -1;
    }
    if (p.getLoc().getCaveId() == getEnd().getCaveId() && alive) {
      return 0;
    }
    if (p.getLoc().getCreaturePresent()) {
      if (p.getLoc().getOtyugh().getHealth() == 50) {
        return 2;
      }
    }
    if (p.getLoc().isPitPresent()) {
      return -3;
    }

    return 1;
  }

  @Override
  public void treasurePick() {
    if (p.getLoc().getTreasureList().isEmpty()) {
      throw new IllegalArgumentException("No treasure at location");
    } else {
      p.pickTreasure();
    }
  }

  @Override
  public void arrowPick() {
    Location curr = p.getLoc();
    for (Location l : cavesList) {
      if (l.getCaveId() == p.getLoc().getCaveId()) {
        curr = l;
      }
    }

    if (curr.getArrow() > 0) {
      while (curr.getArrow() != 0) {
        p.updateArrows();
        curr.decArrowCount();
      }
    } else {
      throw new IllegalArgumentException("No arrows present");
    }

  }

  @Override
  public Player getPlayerCopy() {
    Player pnew = new PlayerImpl(p);
    return pnew;
  }

  @Override
  public Creature getGiantCopy() {
    Creature gnew = new Giant(g);
    return gnew;
  }

  private void mapToCoord() {
    int ind = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        List<Integer> temp = new ArrayList<>();
        temp.add(i);
        temp.add(j);
        coordMap.put(ind++, temp);
      }
    }
  }

  @Override
  public Map<Integer, List<Integer>> getCoordMap() {
    return new HashMap<>(coordMap);
  }


  private void createNodes(int rows, int columns) {
    for (int i = 0; i < rows * columns; i++) {
      Location l = new Cave(i);
      this.cavesList.add(new Cave(l));
    }
  }

  // creates all edges in grid
  private void createEdges() {

    int[][] grid = new int[rows][columns];
    int[] nums = new int[rows * columns];

    for (int i = 0; i < rows * columns; i++) {
      nums[i] = cavesList.get(i).getCaveId();
    }

    int c = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        grid[i][j] = nums[c++];
      }
    }


    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        try {
          allEdges.add(new Edge(grid[i][j], grid[i][j + 1], rand.getNextInt(1, 15)));
          allEdges.add(new Edge(grid[i][j], grid[i + 1][j], rand.getNextInt(1, 15)));
          if (i == 0 && wrapping) {
            allEdges.add(new Edge(grid[0][j], grid[rows - 1][j], rand.getNextInt(1, 15)));
          }
          if (j == 0 && wrapping) {
            allEdges.add(new Edge(grid[i][0], grid[i][columns - 1], rand.getNextInt(1, 15)));
          }
        } catch (IndexOutOfBoundsException e) {
          continue;
        }
      }
    }

  }

  private void createDungeon() {
    createEdges();
    dungeon = Kruskal.kruskalAlgo(allEdges, rows * columns);
    interconnectivity();
  }

  private void interconnectivity() {
    List<Edge> remaining = allEdges;
    remaining.removeAll(dungeon);

    if (interconnectivity > remaining.size()) {
      throw new IllegalArgumentException("Interconnectivity more than remaining edge");
    }

    int count = 1;
    for (Edge i : remaining) {
      dungeon.add(i);
      if (count == interconnectivity) {
        break;
      }
      count++;
    }
  }

  private void addTreasure() {
    int count = 0;
    for (Location l : cavesList) {
      if (l.getLocType() == LocationType.CAVE) {
        count++;
      }
    }

    treasurePer = rand.getNextInt(treasurePercent, 100);
    int treasureAmt = (int) Math.ceil((treasurePer / 100.0) * count);

    rand.shuffleList(cavesList);

    int counts = 0;
    for (int j = 0; j < cavesList.size(); j++) {
      Location c = cavesList.get(j);
      if (c.getLocType() == LocationType.CAVE) {
        int r = rand.getNextInt(1, 3);

        for (int i = 0; i < r; i++) {
          int pick = rand.getVal(0, Treasure.values().length - 1);
          c.setTreasure(pick);
        }

      }
      if (counts == treasureAmt) {
        break;
      }
      counts++;
    }
  }

  private void addArrows() {
    List<Location> caveLi = new ArrayList<>(cavesList);
    rand.shuffleList(caveLi);

    int arrowAmt = (int) Math.ceil((treasurePer / 100.0) * caveLi.size());

    int counts = 0;
    for (Location l : caveLi) {
      int r = rand.getNextInt(1, 3);
      l.setArrows(r);
      if (counts == arrowAmt) {
        break;
      }
      counts++;
    }
  }

  private void addOutyugh() {
    List<Location> caveLi = new ArrayList<>(cavesList);

    int count = 0;
    for (int i = 0; i < caveLi.size(); i++) {
      if (caveLi.get(i).getLocType() == LocationType.CAVE) {
        count++;
      }
      if (i == caveLi.size()) {
        cavesList.get(i).addOtyugh();
      }
    }

    if (outyughNum > count) {
      throw new IllegalArgumentException("Too many Otyughs");
    }

    count = 0;
    for (int i = 0; i < caveLi.size() - 1; i++) {
      if (cavesList.get(i).getLocType() == LocationType.CAVE
              && cavesList.get(i).getCaveId() != getStart().getCaveId()) {
        cavesList.get(i).addOtyugh();
        count++;
      }
      if (count == outyughNum - 1) {
        break;
      }
    }
  }

  private Location getGiantStart() {
    List<Location> caveLi = new ArrayList<>(cavesList);
    for (Location c : cavesList) {
      if (c.getCreaturePresent()
              || c.getCaveId() == getStart().getCaveId()
              || c.getCaveId() == getEnd().getCaveId()) {
        caveLi.remove(c);
      }
    }
    rand.shuffleList(caveLi);
    return caveLi.get(0);
  }

  private void addPit() {
    List<Location> caveLi = new ArrayList<>(cavesList);
    for (Location c : cavesList) {
      if (c.getCreaturePresent()
              || c.getCaveId() == getStart().getCaveId()
              || c.getCaveId() == getEnd().getCaveId()) {
        caveLi.remove(c);
      }
    }
    rand.shuffleList(caveLi);
    int r = rand.getVal(0, caveLi.size() - 1);
    caveLi.get(r).setPitPresent(true);
  }

  private void addThief() {
    List<Location> caveLi = new ArrayList<>(cavesList);
    caveLi.removeIf(c -> c.getCreaturePresent() || c.getCaveId() == getStart().getCaveId()
            || c.getCaveId() == getEnd().getCaveId());

    rand.shuffleList(caveLi);
    int r = rand.getVal(0, caveLi.size() - 1);
    caveLi.get(r).setThiefPresent(true);
  }

  private void createDirectionList() {

    Map<Integer, Location> caveMap = new HashMap<>();
    for (Location l : cavesList) {
      caveMap.put(l.getCaveId(), l);
    }

    for (Edge e : dungeon) {
      Location s = caveMap.get(e.getSrc());
      Location d = caveMap.get(e.getDest());
      int diff = e.getDest() - e.getSrc();

      if (s.getCaveId() == e.getSrc()) {
        if (diff == 1) {
          s.setDirectionList(Direction.EAST, d);
          d.setDirectionList(Direction.WEST, s);
        } else if (diff == Math.abs(columns - 1)) {
          s.setDirectionList(Direction.WEST, d);
          d.setDirectionList(Direction.EAST, s);
        } else if (diff == columns) {
          s.setDirectionList(Direction.SOUTH, d);
          d.setDirectionList(Direction.NORTH, s);
        } else if (diff > columns) {
          s.setDirectionList(Direction.NORTH, d);
          d.setDirectionList(Direction.SOUTH, s);
        }
      }
    }

  }

  private void setLocation() {
    for (Location c : cavesList) {
      if (c.getDirectionList().size() == 2) {
        c.setLocType(LocationType.TUNNEL);
      } else {
        c.setLocType(LocationType.CAVE);
      }
    }
  }

  /**
   * https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
   */
  private void setStartEndNodes() {
    List<Location> startList = new ArrayList<Location>();
    for (Location l : cavesList) {
      if (l.getLocType() != LocationType.TUNNEL) {
        startList.add(l);
      }
    }
    rand.shuffleList(startList);
    this.end = null;

    for (Location i : startList) {
      this.start = i;
      LinkedList<Location> queue = new LinkedList<>();

      Location src_loc = i;
      src_loc.setVisited(true);
      queue.add(i);

      while (queue.size() != 0) {
        src_loc = queue.poll();

        Iterator<Location> it = new ArrayList<Location>(
                src_loc.getDirectionList().values()).listIterator();

        while (it.hasNext()) {
          Location n = it.next();
          if (!n.getVisited()) {
            n.setVisited(true);
            queue.add(n);
            n.setLevel(src_loc.getLevel() + 1);
          }
        }
      }

      for (Location c : cavesList) {
        c.setVisited(false);
      }

      for (Location c : cavesList) {
        if (c.getLevel() >= 5 && c.getLocType() != LocationType.TUNNEL) {
          this.end = c;
          break;
        }
      }

      if (this.end != null) {
        break;
      }

    }

    if (this.end == null) {
      throw new IllegalArgumentException("No possible path of length at least 5.");
    }
  }


  @Override
  public Smell detectSmell() {
    Smell s = Smell.NONE;
    for (Map.Entry<Direction, Location> entry : p.getLoc().getDirectionList().entrySet()) {
      Location loc = entry.getValue();
      if (loc.getCreaturePresent()) {
        s = Smell.HIGH;
        return s;
      } else {
        AtomicInteger c = new AtomicInteger();
        loc.getDirectionList().forEach((d, l) -> {
          if (l.getCreaturePresent()) {
            c.addAndGet(1);
          }
        });
        if (c.get() == 1) {
          s = Smell.LOW;
        } else if (c.get() > 1) {
          s = Smell.HIGH;
        }
      }
    }
    return s;
  }

  @Override
  public Sound detectPit() {
    for (Map.Entry<Direction, Location> entry : p.getLoc().getDirectionList().entrySet()) {
      Location loc = entry.getValue();
      if (loc.isPitPresent()) {
        return Sound.HIGH;
      } else {
        AtomicInteger c = new AtomicInteger();
        loc.getDirectionList().forEach((d, l) -> {
          if (l.isPitPresent()) {
            c.addAndGet(1);
          }
        });
        if (c.get() == 1) {
          return Sound.LOW;
        } else if (c.get() > 1) {
          return Sound.HIGH;
        }
      }
    }
    return Sound.NONE;
  }

  @Override
  public void shootArrow(int distance, String dir) {

    Direction direction = Direction.valueOf(dir);
    if (distance <= 0 || !EnumSet.allOf(Direction.class).contains(direction)) {
      throw new IllegalArgumentException("Invalid inputs given");
    }

    hit = false;
    Location caveWithArrow = p.getLoc();

    Map<Direction, Direction> oppositeLocation = new HashMap<>();
    oppositeLocation.put(Direction.NORTH, Direction.SOUTH);
    oppositeLocation.put(Direction.SOUTH, Direction.NORTH);
    oppositeLocation.put(Direction.EAST, Direction.WEST);
    oppositeLocation.put(Direction.WEST, Direction.EAST);

    if (p.getTotalArrows() > 0) {

      p.shootArrows();


      if (p.getLoc().getDirectionList().containsKey(direction)) {

        caveWithArrow = p.getLoc().getDirectionList().get(direction);

        do {

          if (caveWithArrow.getLocType() == LocationType.CAVE) {
            distance--;
            if (distance == 0 && caveWithArrow.getCreaturePresent()) {
              hit = true;
              caveWithArrow.hitOutyugh();
              return;
            } else {
              caveWithArrow = caveWithArrow.getDirectionList().get(direction);
            }
          } else if (caveWithArrow.getLocType() == LocationType.TUNNEL) {
            for (Map.Entry<Direction, Location> entry :
                    caveWithArrow.getDirectionList().entrySet()) {
              Direction k = entry.getKey();
              Location v = entry.getValue();
              if (k != oppositeLocation.get(direction)) {
                caveWithArrow = caveWithArrow.getDirectionList().get(k);
                direction = k;
                if (caveWithArrow.getLocType() == LocationType.CAVE) {
                  distance--;
                  if (distance == 0 && caveWithArrow.getCreaturePresent()) {
                    hit = true;
                    caveWithArrow.hitOutyugh();
                    return;
                  } else {
                    throw new IllegalArgumentException("You missed");
                  }
                }

              }
            }
          }

        }
        while (distance > 0);

      }

    } else {
      throw new IllegalArgumentException("You have no more arrows to shoot.");
    }
  }

  @Override
  public boolean getHit() {
    return hit;
  }

  @Override
  public Location getEnd() {
    Location l = end;
    return new Cave(l);
  }

  @Override
  public Location getStart() {
    Location l = start;
    return new Cave(l);
  }

  @Override
  public List<Location> getCavesList() {
    return new ArrayList<Location>(cavesList);
  }

  @Override
  public int getTreasureAmt() {
    return treasurePer;
  }

  @Override
  public int getRows() {
    return rows;
  }

  @Override
  public int getCols() {
    return columns;
  }

  @Override
  public RandomGenerator getRandom() {
    return this.rand;
  }

  @Override
  public List<Edge> getDungeon() {
    return new ArrayList<>(dungeon);
  }

  @Override
  public List<Location> getNewCaveList() {
    return new ArrayList<>(newCaveList);
  }

  @Override
  public int getOtyughNum() {
    return outyughNum;
  }

  @Override
  public boolean getWrapping() {
    return wrapping;
  }

  @Override
  public int getTreasurePercent() {
    return treasurePercent;
  }

  @Override
  public int getInterconnectivity() {
    return interconnectivity;
  }

  @Override
  public Dungeon getModelCopy() {
    return modelCopy;
  }

  @Override
  public void setRows(int rows) {
    this.rows = rows;
  }

  @Override
  public void setColumns(int columns) {
    this.columns = columns;
  }

  @Override
  public void setInterconnectivity(int interconnectivity) {
    this.interconnectivity = interconnectivity;
  }

  @Override
  public void setTreasurePercent(int treasurePercent) {
    this.treasurePercent = treasurePercent;
  }

  @Override
  public void setWrapping(boolean wrapping) {
    this.wrapping = wrapping;
  }

  @Override
  public void setOutyughNum(int outyughNum) {
    this.outyughNum = outyughNum;
  }

}
