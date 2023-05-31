import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import control.DungeonController;
import control.DungeonCommandController;
import control.IController;
import control.commands.Move;
import control.commands.PickArrow;
import control.commands.PickTreasure;
import control.commands.Shoot;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import randomizer.MockRandomizer;
import view.IView;
import view.MockView;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Controller of dungeon game.
 */
public class DungeonControllerTest {

  private Dungeon m;

  @Before
  public void setup() {
    m = new DungeonImpl("Harry", 4, 5,
            2, 30, false, new MockRandomizer(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModel() {
    m = null;
    StringReader input = new StringReader("S 1 EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
  }

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    StringReader input = new StringReader("S 1 EAST");
    FailingAppendable append = new FailingAppendable();
    IController controller = new DungeonController(input, append);
    controller.playGame(m);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullInp() {
    StringReader input = null;
    Appendable gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullOp() {
    StringReader input = new StringReader("S 1 EAST");
    Appendable gameLog = null;
    IController c = new DungeonController(input, gameLog);
  }

  @Test
  public void testInvalidInputMPS() {
    StringReader input = new StringReader("abc");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "You hear extremely loud wind howling. There's an opening nearby.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Unknown command abc\n"
            + "You hear extremely loud wind howling. There's an opening nearby.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }


  @Test
  public void testShoot() {
    StringReader input = new StringReader("S 1 EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testInvalidShoot() {
    StringReader input = new StringReader("S -1 EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Invalid inputs given\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testInvalidShootZeroDist() {
    StringReader input = new StringReader("S 0 EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Invalid inputs given\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testShootMiss() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    StringReader input = new StringReader("S 1 SOUTH");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: You missed\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testShootNoArrow() {
    StringReader input = new StringReader("S 1 EAST S 1 EAST S 1 EAST S 1 EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: You missed\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: You have no more arrows to shoot.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testMoveAllDirections() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    StringReader input = new StringReader("M SOUTH M EAST S 1 NORTH "
            + "S 1 NORTH M NORTH M SOUTH M WEST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 7\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 2,\tEAST node 8,\tWEST node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 7\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 2,\tEAST node 8,\tWEST node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Smell detected.\n"
            + "Current Location: 7\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 2,\tEAST node 8,\tWEST node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 2\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 7,\tEAST node 3,\tWEST node 1,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Smell detected.\n"
            + "Current Location: 7\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 2,\tEAST node 8,\tWEST node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testInvalidMove() {
    StringReader input = new StringReader("M est");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: EAST node 2,\tWEST node 0,\tSOUTH node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter directionInvalid direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: EAST node 2,\tWEST node 0,\tSOUTH node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testMove() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    StringReader input = new StringReader("M SOUTH");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testPick() {
    StringReader input = new StringReader("P T P");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Treasure and Arrows present. What do you want to pick?- T/A?\n"
            + "Harry has treasure: RUBY: 0, SAPPHIRE: 8, DIAMOND: 0, \n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Arrows found. Picking arrows.\n"
            + "Harry has total arrows: 11\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testPickWhenNoTreasureOrArrows() {
    StringReader input = new StringReader("S 1 EAST S 1 EAST M EAST P");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Smell detected.\n"
            + "Current Location: 2\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 7,\tEAST node 3,\tWEST node 1,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "No treasure or arrows\n"
            + "Smell detected.\n"
            + "Current Location: 2\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 7,\tEAST node 3,\tWEST node 1,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testInvalidPick() {
    StringReader input = new StringReader("P TA");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Treasure and Arrows present. What do you want to pick?- T/A?\n"
            + "Invalid Input\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testPlayerKilled() {
    StringReader input = new StringReader("M EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Bad luck! You were killed by an Otyugh", gameLog.toString());
  }

  @Test
  public void testQuit() {
    StringReader input = new StringReader("S 1 EAST P Q");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Treasure and Arrows present. What do you want to pick?- T/A?\n"
            + "Quitting", gameLog.toString());
  }

  @Test
  public void testPlayerReachEnd() {
    m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);
    StringReader input = new StringReader("P A M SOUTH M EAST M EAST " +
            "M SOUTH M EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "You hear distant wind howling. There's an opening nearby.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Treasure and Arrows present. What do you want to pick?- T/A?\n"
            + "Harry has total arrows: 7\n"
            + "You hear distant wind howling. There's an opening nearby.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "You hear extremely loud wind howling. There's an opening nearby.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 11,\tNORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "You hear distant wind howling. There's an opening nearby.\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 7\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 12,\tNORTH node 2,\tEAST node 8,\tWEST node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 8\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 13,\tNORTH node 3,\tEAST node 9,\tWEST node 7,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Smell detected.\n"
            + "Current Location: 13\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 18,\tNORTH node 8,\tEAST node 14,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Hurray! Reached End! Game Over.", gameLog.toString());
  }

  @Test
  public void testExtraCommandsAfterKill() {
    StringReader input = new StringReader("M SOUTH P");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Bad luck! You were killed by an Otyugh", gameLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveGoNull() {
    DungeonCommandController dc = new Move("WEST");
    dc.goCommand(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootGoNull() {
    DungeonCommandController dc = new Shoot(1, "WEST");
    dc.goCommand(null);

    dc = new PickTreasure();
    dc.goCommand(null);

    dc = new PickArrow();
    dc.goCommand(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickTreasureGoNull() {
    DungeonCommandController dc = new PickTreasure();
    dc.goCommand(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickArrowGoNull() {
    DungeonCommandController dc = new PickArrow();
    dc.goCommand(null);
  }

  @Test
  public void testMoveInvalidGo() {
    DungeonCommandController dc = new Move("WESh");
    assertEquals("Invalid direction", dc.goCommand(m));
  }

  @Test
  public void testShootInvalidGo() {
    DungeonCommandController dc = new Shoot(-1, "EAST");
    assertEquals("Invalid inputs given", dc.goCommand(m));
  }

  @Test
  public void testPickArrowGo() {
    DungeonCommandController dc = new PickArrow();
    assertEquals("\n"
            + "Harry has total arrows: 11", dc.goCommand(m));
  }

  @Test
  public void testPickTreasureGo() {
    DungeonCommandController dc = new PickTreasure();
    assertEquals("\n"
            + "Harry has treasure: RUBY: 0, SAPPHIRE: 8, DIAMOND: 0, ", dc.goCommand(m));
  }

  @Test
  public void testGameEndPit() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);

    StringReader input = new StringReader("M SOUTH S 1 WEST S 1 WEST M WEST M SOUTH M EAST M EAST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter distance: \n"
            + "Enter direction: Otyugh hit!\n"
            + "Smell detected.\n"
            + "Current Location: 6\n"
            + "You are in a CAVE\n"
            + "Possible paths: NORTH node 1,\tEAST node 7,\tWEST node 5,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "You hear distant wind howling. There's an opening nearby.\n"
            + "Current Location: 5\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 10,\tNORTH node 0,\tEAST node 6,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "You hear extremely loud wind howling. There's an opening nearby.\n"
            + "Current Location: 10\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 15,\tNORTH node 5,\tEAST node 11,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter directionOh no! You ran into a thief and lost all your treasure!\n"
            + "You fell into a pit", gameLog.toString());
  }

  @Test
  public void testGiantEncounterPlayKilled() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);

    StringReader input = new StringReader("M WEST");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "You don't have enough arrows for a potion. "
            + "Try your luck and pass the GiantYou were killed by giant!", gameLog.toString());
  }

  @Test
  public void testGiantEncounterBattleWin() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);

    StringReader input = new StringReader("P A M WEST Y");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Treasure and Arrows present. What do you want to pick?- T/A?\n"
            + "Harry has total arrows: 11\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Do you want to trade 4 arrows for a 100% effective Giant killing potion "
            + "or take risk and pass the Giant? Enter Y or NYou defeated a Giant!\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 0\n"
            + "You are in a TUNNEL\n"
            + "Possible paths: SOUTH node 5,\tEAST node 1,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?", gameLog.toString());
  }

  @Test
  public void testGiantEncounterBattle50() {
    Dungeon m = new DungeonImpl("Harry", 4, 5, 3,
            60, false, new MockRandomizer(4), 5);

    StringReader input = new StringReader("P A M WEST N");
    StringBuilder gameLog = new StringBuilder();
    IController c = new DungeonController(input, gameLog);
    c.playGame(m);
    assertEquals("\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Treasure and Arrows present. What do you want to pick?- T/A?\n"
            + "Harry has total arrows: 11\n"
            + "Terribly high smell detected.\n"
            + "Current Location: 1\n"
            + "You are in a CAVE\n"
            + "Possible paths: SOUTH node 6,\tEAST node 2,\tWEST node 0,\t\n"
            + "Move, Pickup, or Shoot (M-P-S)?\n"
            + "Enter direction\n"
            + "Do you want to trade 4 arrows for a 100% effective "
            + "Giant killing potion or take risk and pass the Giant? "
            + "Enter Y or N"
            + "You were killed by giant!", gameLog.toString());
  }

  @Test
  public void testGraphicView() {

    StringBuilder log = new StringBuilder();
    IView view = new MockView(log);
    Dungeon model = new DungeonImpl(new DungeonImpl("Rimuru", 4, 5, 3,
            60, false, new MockRandomizer(4), 5));
    IController controller = new DungeonController(view, model);
    controller.playGUIGame();

    assertEquals("KeyListener added\n"
            + "ActionListener added\n"
            + "ClickListener added\n"
            + "make visible called\n", log.toString());
  }


}
