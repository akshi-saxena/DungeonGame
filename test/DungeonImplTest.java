import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Location;
import location.LocationType;
import location.Smell;
import location.Treasure;
import randomizer.MockRandomizer;
import randomizer.RandomGenerator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * All tests for the dungeon implementation.
 */
public class DungeonImplTest {

  private Dungeon d1;
  private Dungeon d2;

  @Before
  public void setUp() throws Exception {
    d1 = newDungeon("Harry", 4, 5, 2, 30,
            true, new MockRandomizer(4), 5);
    d2 = newDungeon("Draco", 4, 5, 2, 30,
            false, new MockRandomizer(4), 7);
  }

  private Dungeon newDungeon(String name, int rows, int cols, int inter, int trPercent,
                             boolean wrap, RandomGenerator r, int otyughCount) {
    return new DungeonImpl(name, rows, cols, inter, trPercent, wrap, r, otyughCount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidArgs() {
    newDungeon("Harry", -4, 5, 2, 30,
            true, new MockRandomizer(4), 5);
    newDungeon("Harry", 4, -5, 2, 30,
            true, new MockRandomizer(4), 5);
    newDungeon("Harry", 4, 5, -2, 30,
            true, new MockRandomizer(4), 5);
    newDungeon("Harry", 4, 5, 2, -30,
            true, new MockRandomizer(4), 5);
    newDungeon("Harry", 4, 5, 2, 30,
            true, new MockRandomizer(4), 100);
  }


  @Test(expected = IllegalArgumentException.class)
  public void invalidDungeon() {
    newDungeon("Harry", 2, 3, 2, 30,
            true, new MockRandomizer(4), 5);
  }

  @Test
  public void testDungeonDesc() {
    assertEquals("Dungeon Nodes:\n" +
            "0-SOUTH-5\t0-EAST-1\t1-SOUTH-6\t1-EAST-2\t1-WEST-0\t2-SOUTH-7\t2-EAST-3\t2-WEST-1\t" +
            "3-EAST-4\t3-WEST-2\t4-WEST-3\t5-NORTH-0\t5-SOUTH-10\t5-EAST-6\t6-NORTH-1\t6-EAST-7\t" +
            "6-WEST-5\t7-NORTH-2\t7-EAST-8\t7-WEST-6\t8-EAST-9\t8-WEST-7\t9-WEST-8\t10-NORTH-5\t" +
            "10-SOUTH-15\t10-EAST-11\t11-EAST-12\t11-WEST-10\t12-EAST-13\t12-WEST-11\t" +
            "13-EAST-14\t13-WEST-12\t14-WEST-13\t15-NORTH-10\t15-EAST-16\t16-EAST-17\t" +
            "16-WEST-15\t17-EAST-18\t17-WEST-16\t18-EAST-19\t18-WEST-17\t19-WEST-18\t\n" +
            "[(0, 1), (1, 2), (2, 3), (3, 4), (5, 6), (6, 7), (7, 8), (8, 9), (10, 11), " +
            "(11, 12), (12, 13), (13, 14), (15, 16), (16, 17), (17, 18), (18, 19), (0, 5), " +
            "(5, 10), (10, 15), (1, 6), (2, 7)]\n" +
            "Start Node: 1\n" +
            "End Node: 14\n" +
            "\n" +
            "Treasures:\n" +
            "Node1 has DIAMOND\tDIAMOND\tDIAMOND\tDIAMOND\t\n" +
            "Arrows: \n" +
            "Node0 has 8 arrows.\n" +
            "Node1 has 8 arrows.\n" +
            "Creatures:\n" +
            "2 has Otyugh\n" +
            "4 has Otyugh\n" +
            "5 has Otyugh\n" +
            "6 has Otyugh\n" +
            "7 has Otyugh\n" +
            "9 has Otyugh\n" +
            "14 has Otyugh\n", d2.toString());
  }

  @Test
  public void playerReachesEnd() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    d1.arrowPick();
    d1.playMove("SOUTH");
    d1.shootArrow(1, "WEST");
    d1.shootArrow(1, "WEST");
    d1.playMove("WEST");
    d1.playMove("SOUTH");
    d1.playMove("SOUTH");
    d1.playMove("EAST");
    d1.playMove("EAST");
    d1.playMove("EAST");
    d1.shootArrow(1, "EAST");
    d1.shootArrow(1, "EAST");
    d1.playMove("EAST");
    assertEquals(d1.getPlayerCopy().getLoc(), d1.getEnd());
  }

  @Test
  public void testStartCave() {
    assertEquals(LocationType.CAVE, d1.getStart().getLocType());
    assertEquals(LocationType.CAVE, d2.getStart().getLocType());
  }

  @Test
  public void testEndCave() {
    assertEquals(LocationType.CAVE, d1.getEnd().getLocType());
    assertEquals(LocationType.CAVE, d2.getEnd().getLocType());
  }

  @Test
  public void testTreasurePercent() {
    int count = 1;
    for (Location l : d1.getCavesList()) {
      if (!l.getTreasureList().isEmpty()) {
        count += 1;
      }
    }
    assertTrue((int) Math.ceil((30 / 100.0) * count) ==
            (int) Math.ceil((d1.getTreasureAmt() / 100.0) * count));

    count = 1;
    for (Location l : d2.getCavesList()) {
      if (!l.getTreasureList().isEmpty()) {
        count += 1;
      }
    }
    assertTrue((int) Math.ceil((30 / 100.0) * count) ==
            (int) Math.ceil((d2.getTreasureAmt() / 100.0) * count));
  }

  @Test
  public void pickTreasure() {
    Map<Treasure, Integer> trLi = new TreeMap<>();
    trLi.put(Treasure.DIAMOND, 0);
    trLi.put(Treasure.RUBY, 0);
    trLi.put(Treasure.SAPPHIRE, 0);
    assertEquals(trLi, d1.getPlayerCopy().getTreasureCollected());

    d1.treasurePick();

    trLi.put(Treasure.DIAMOND, trLi.get(Treasure.DIAMOND) + 4);

    assertEquals(trLi, d1.getPlayerCopy().getTreasureCollected());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noPickTreasureAtTunnel() {
    d1.playMove("WEST");
    assertEquals("{RUBY=0, SAPPHIRE=0, DIAMOND=0}",
            d1.getPlayerCopy().getTreasureCollected().toString());
    d1.treasurePick();
  }


  @Test
  public void playMoveAllDirections() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    d1.playMove("SOUTH");
    assertEquals(6, d1.getPlayerCopy().getLoc().getCaveId());
    d1.playMove("EAST");
    assertEquals(7, d1.getPlayerCopy().getLoc().getCaveId());
    d1.shootArrow(1, "WEST");
    d1.shootArrow(1, "WEST");
    d1.playMove("WEST");
    assertEquals(6, d1.getPlayerCopy().getLoc().getCaveId());
    d1.playMove("NORTH");
    assertEquals(1, d1.getPlayerCopy().getLoc().getCaveId());
  }


  @Test
  public void testMonsterAdded() {
    int count = 1;
    for (Location l : d1.getCavesList()) {
      if (l.getCreaturePresent()) {
        count++;
      }
    }
    assertEquals(5, count);

    count = 1;
    for (Location l : d2.getCavesList()) {
      if (l.getCreaturePresent()) {
        count++;
      }
    }
    assertEquals(7, count);
  }

  @Test
  public void testArrowTreasureFreq() {
    int arrowsInDungeon = 0;
    for (Location l : d2.getCavesList()) {
      if (l.getArrow() > 0) {
        arrowsInDungeon++;
      }
    }

    int treasureInDungeon = 0;
    for (Location l : d2.getCavesList()) {
      if (!l.getTreasureList().isEmpty()) {
        treasureInDungeon += 1;
      }
    }

    int count = 1;
    for (Location l : d2.getCavesList()) {
      if (!l.getTreasureList().isEmpty()) {
        count += 1;
      }
    }

    //Frequency of arrows/total caves and tunnel
    int x = arrowsInDungeon / (d2.getCavesList().size());

    //Frequency of treasure/caves in dungeon
    int y = treasureInDungeon / (count);
    assertEquals(x, y);
  }

  @Test
  public void testArrowStart() {
    assertEquals(3, d1.getPlayerCopy().getTotalArrows());
  }

  @Test
  public void testArrowPick() {
    assertEquals(3, d1.getPlayerCopy().getTotalArrows());
    d1.arrowPick();
    assertEquals(11, d1.getPlayerCopy().getTotalArrows());
  }

  @Test
  public void testArrowCaveTunnel() {
    int caveCount = 0;
    int tunnelCount = 0;
    for (Location l : d2.getCavesList()) {
      if (l.getArrow() > 0 && l.getLocType() == LocationType.CAVE) {
        caveCount++;
      }
      if (l.getArrow() > 0 && l.getLocType() == LocationType.TUNNEL) {
        tunnelCount++;
      }
    }
    assertEquals(1, caveCount);
    assertEquals(1, tunnelCount);
  }

  @Test
  public void testMove() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);

    d1.playMove("SOUTH");
    assertEquals(6, d1.getPlayerCopy().getLoc().getCaveId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMove() {
    d2.playMove("blah");
  }

  @Test
  public void otyughNotInStart() {
    assertFalse(d1.getStart().getCreaturePresent());
    assertFalse(d2.getStart().getCreaturePresent());
  }

  @Test
  public void otyughInEnd() {
    assertTrue(d1.getEnd().getCreaturePresent());
    assertTrue(d2.getEnd().getCreaturePresent());
  }

  @Test
  public void testOtyughOnlyCave() {
    int caveCount = 0;
    int tunnelCount = 0;
    for (Location l : d2.getCavesList()) {
      if (l.getCreaturePresent() && l.getLocType() == LocationType.CAVE) {
        caveCount++;
      }
      if (l.getCreaturePresent() && l.getLocType() == LocationType.TUNNEL) {
        tunnelCount++;
      }
    }

    assertEquals(7, caveCount);
    assertEquals(0, tunnelCount);
  }

  @Test
  public void testOtyughSmell() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    assertEquals(Smell.HIGH, d1.detectSmell());
    d1.playMove("SOUTH");

    d1.shootArrow(1, "WEST");
    d1.shootArrow(1, "WEST");
    d1.playMove("WEST");
    assertEquals(Smell.NONE, d1.detectSmell());
    d1.playMove("EAST");
    assertEquals(Smell.LOW, d1.detectSmell());

  }

  @Test
  public void testOtyughKill() {
    assertEquals(Smell.HIGH, d1.detectSmell());
    d1.shootArrow(1, "EAST");
    d1.shootArrow(1, "EAST");
    d1.playMove("EAST");
    assertFalse(d1.getPlayerCopy().getLoc().getCreaturePresent());
  }

  @Test
  public void testPlayerNotKillWhenHealth50() {
    assertEquals(Smell.HIGH, d1.detectSmell());
    d1.shootArrow(1, "EAST");
    d1.playMove("EAST");
    assertTrue(d1.getPlayerCopy().getLoc().getCreaturePresent());
    assertEquals(50, d1.getPlayerCopy().getLoc().getOtyugh().getHealth());
    assertEquals(2, d1.playerKillOrNot());
  }

  @Test
  public void testPlayerDead() {
    d1.playMove("EAST");
    assertEquals(-1, d1.playerKillOrNot());
  }

  @Test
  public void testGiantMove() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    assertEquals(0, d1.getGiantCopy().getLoc().getCaveId());
    d1.playMove("SOUTH");
    assertEquals(1, d1.getGiantCopy().getLoc().getCaveId());
  }

  @Test
  public void testPitAdded() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    int r = 0;
    for (Location l: d1.getCavesList()) {
      if (l.isPitPresent()) {
        r = l.getCaveId();
      }
    }
    assertEquals(12, r);
  }

  @Test
  public void testThiefAdded() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    int r = 0;
    for (Location l: d1.getCavesList()) {
      if (l.isThiefPresent()) {
        r = l.getCaveId();
      }
    }
    assertEquals(12, r);
  }

  @Test
  public void playerDeadByPit() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    d1.playMove("SOUTH");
    d1.shootArrow(1, "WEST");
    d1.playMove("WEST");
    d1.playMove("SOUTH");
    d1.playMove("EAST");
    d1.playMove("EAST");
    assertEquals(-3, d1.playerKillOrNot());
  }

  @Test
  public void playerEncounterThief() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    d1.treasurePick();
    assertEquals("{RUBY=0, SAPPHIRE=4, DIAMOND=0}",
            d1.getPlayerCopy().getTreasureCollected().toString());
    d1.playMove("SOUTH");
    d1.shootArrow(1, "WEST");
    d1.playMove("WEST");
    d1.playMove("SOUTH");
    d1.playMove("EAST");
    d1.playMove("EAST");
    assertEquals("{RUBY=0, SAPPHIRE=0, DIAMOND=0}",
            d1.getPlayerCopy().getTreasureCollected().toString());
  }

  @Test
  public void playerEncounterGiant() {
    Dungeon d1 = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);

    d1.playMove("WEST");
    assertTrue(d1.checkGiantEncounter());
  }

}