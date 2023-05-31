package control;

import control.commands.KillGiant;
import control.commands.Move;
import control.commands.PassGiant;
import control.commands.PickArrow;
import control.commands.PickTreasure;
import control.commands.Shoot;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Direction;
import location.Smell;
import location.Sound;
import randomizer.ActualRandomizer;
import view.ButtonListener;
import view.IView;
import view.KeyboardListener;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is the implementation of command controller that calls the commands
 * depending on the input given by user.
 */
public class DungeonController implements IController {

  private Appendable out;
  private Scanner scan;


  private IView view;
  private Dungeon model;

  /**
   * Constructor for the text based game controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public DungeonController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);

    this.view = null;
    this.model = null;
  }

  /**
   * Constructor for the GUI controller.
   *
   * @param v view
   * @param m model
   */
  public DungeonController(IView v, Dungeon m) {
    this.view = v;
    this.model = m;

    this.out = null;
    this.scan = null;
  }


  @Override
  public void playGame(Dungeon d) {
    boolean moved = false;

    if (d == null) {
      throw new IllegalArgumentException("The model cannot be null");
    }

    DungeonCommandController cmd = null;
    DungeonCommandController cmd1 = null;

    try {
      while (true) {
        if (d.playerKillOrNot() == -1) {
          out.append("\nBad luck! You were killed by an Otyugh");
          break;
        } else if (d.playerKillOrNot() == 0) {
          out.append("\nHurray! Reached End! Game Over.");
          break;
        } else if (d.playerKillOrNot() == 2) {
          out.append("\nInjured otyugh was in your location. You escaped.");
        } else if (d.playerKillOrNot() == -3) {
          out.append("\nYou fell into a pit");
          break;
        }

        if (moved && d.checkGiantEncounter()) {
          String inp;
          if (d.getPlayerCopy().getTotalArrows() >= 4) {
            out.append("\nDo you want to trade 4 arrows for a 100% effective "
                    + "Giant killing potion or take risk and pass the Giant? Enter Y or N");
            inp = scan.next();
          } else {
            out.append("\nYou don't have enough arrows for a potion. "
                    + "Try your luck and pass the Giant");
            inp = "N";
          }

          switch (inp) {
            case "Y":
              cmd1 = new KillGiant();
              break;
            case "N":
              cmd1 = new PassGiant();
              break;
            default:
              out.append("\nInvalid Input");
              break;
          }

          if (cmd1 != null) {
            out.append(cmd1.goCommand(d));
            cmd1 = null;
          }

          if (d.getGiantCopy().getHealth() == 100) {
            return;
          }
        }

        if (d.detectPit() == Sound.HIGH) {
          out.append("\nYou hear extremely loud wind howling. There's an opening nearby.");
        } else if (d.detectPit() == Sound.LOW) {
          out.append("\nYou hear distant wind howling. There's an opening nearby.");
        }

        if (d.detectSmell() == Smell.HIGH) {
          out.append("\nTerribly high smell detected.");
        } else if (d.detectSmell() == Smell.LOW) {
          out.append("\nSmell detected.");
        }
        locDesc(d);

        out.append("\nMove, Pickup, or Shoot (M-P-S)?");

        if (!scan.hasNext()) {
          break;
        }

        String in = scan.next();

        String pick = "";

        switch (in) {
          case "Q":
            out.append("Quitting");
            return;
          case "M":
            out.append("\nEnter direction");
            cmd = new Move(scan.next());
            moved = true;
            break;

          case "P":
            if (!d.getPlayerCopy().getLoc().getTreasureList().isEmpty()
                    && d.getPlayerCopy().getLoc().getArrow() > 0) {
              out.append("\nTreasure and Arrows present. What do you want to pick?- T/A?");

              pick = scan.next();

              switch (pick) {
                case "T":
                  cmd = new PickTreasure();
                  break;
                case "A":
                  cmd = new PickArrow();
                  break;
                case "Q":
                  out.append("\nQuitting");
                  return;
                default:
                  out.append("\nInvalid Input");
                  break;
              }
            } else if (d.getPlayerCopy().getLoc().getArrow() > 0
                    && d.getPlayerCopy().getLoc().getTreasureList().isEmpty()) {
              out.append("\nArrows found. Picking arrows.");
              cmd = new PickArrow();
            } else if (!d.getPlayerCopy().getLoc().getTreasureList().isEmpty()
                    && d.getPlayerCopy().getLoc().getArrow() == 0) {
              out.append("\nTreasure found. Picking treasure.");
              cmd = new PickTreasure();
            } else {
              out.append("\nNo treasure or arrows");
            }
            break;

          case "S":
            out.append("\nEnter distance: ");
            int dist;
            try {
              dist = Integer.parseInt(scan.next());
            } catch (NumberFormatException e) {
              out.append("\nInvalid distance");
              break;
            }
            out.append("\nEnter direction: ");
            String direction = scan.next();
            cmd = new Shoot(dist, direction);
            break;

          default:
            out.append(String.format("\nUnknown command %s", in));
            cmd = null;
            break;
        }

        if (cmd != null) {
          out.append(cmd.goCommand(d));
          cmd = null;
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append failed");
    } catch (IllegalArgumentException exception) {
      throw new IllegalArgumentException("Invalid input");
    }
    scan.close();
  }

  private void locDesc(Dungeon d) throws IOException {
    out.append("\nCurrent Location: ").append(String.valueOf(d.getPlayerCopy().getLoc()
            .getCaveId()));
    out.append("\nYou are in a ").append(d.getPlayerCopy().getLoc().getLocType().name());
    out.append("\nPossible paths: ");
    for (Direction dir : d.getPlayerCopy().getLoc().getDirectionList().keySet()) {
      out.append(dir.name()).append(" node ")
              .append(String.valueOf(d.getPlayerCopy().getLoc().getDirectionList()
                      .get(dir).getCaveId())).append(",\t");
    }
  }

  @Override
  public void playGUIGame() {
    configureKeyBoardListener();
    configureButtonListener();
    view.addClickListener(this);
    view.makeVisible();
  }

  private String doa = "";

  private void configureKeyBoardListener() {
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    AtomicBoolean shoot = new AtomicBoolean(false);
    AtomicInteger dist = new AtomicInteger();


    keyPresses.put(KeyEvent.VK_T, () -> {
      deadOrAlive();
      String op;
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 0)) {
        DungeonCommandController comd = new PickTreasure();
        op = comd.goCommand(model);
        view.updatePanel(this);
        view.setEchoOutput(op);
      } else {
        op = doa;
        op += "\nCan't play any moves.";
        view.setEchoOutput(op);
      }
    });

    keyPresses.put(KeyEvent.VK_A, () -> {
      deadOrAlive();
      String op;
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 0)) {
        DungeonCommandController comd = new PickArrow();
        op = comd.goCommand(model);
        view.updatePanel(this);
        view.setEchoOutput(op);
      } else {
        op = doa;
        op += "\nCan't play any moves.";
        view.setEchoOutput(op);
      }

    });

    keyPresses.put(KeyEvent.VK_1, () -> {
      shoot.set(true);
      dist.set(1);
    });
    keyPresses.put(KeyEvent.VK_2, () -> {
      shoot.set(true);
      dist.set(2);
    });
    keyPresses.put(KeyEvent.VK_3, () -> {
      shoot.set(true);
      dist.set(3);
    });
    keyPresses.put(KeyEvent.VK_4, () -> {
      shoot.set(true);
      dist.set(4);
    });
    keyPresses.put(KeyEvent.VK_5, () -> {
      shoot.set(true);
      dist.set(5);
    });

    keyPresses.put(KeyEvent.VK_UP, () -> {
      deadOrAlive();
      String op;
      DungeonCommandController comd;
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 0)) {
        if (shoot.get()) {
          comd = new Shoot(dist.intValue(), "NORTH");
          op = comd.goCommand(model);
          shoot.set(false);
          view.updatePanel(this);
        } else {
          comd = new Move("NORTH");
          op = comd.goCommand(model);
          if (op.equals("")) {
            op += "\nYou moved North. Current Loc: ";
            op += model.getCoordMap().get(model.getPlayerCopy().getLoc().getCaveId());
            deadOrAlive();
            op += doa;
          }
          view.updatePanel(this);
          if (!giantEnc().isEmpty()) {
            op = giantEnc();
            view.updatePanel(this);
          }
        }
        view.setEchoOutput(op);
      } else {
        op = doa;
        op += "\nCan't play any moves.";
        view.setEchoOutput(op);
      }
    }
    );

    keyPresses.put(KeyEvent.VK_DOWN, () -> {
      deadOrAlive();
      String op;
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 0)) {
        if (shoot.get()) {
          DungeonCommandController comd = new Shoot(dist.intValue(), "SOUTH");
          op = comd.goCommand(model);
          shoot.set(false);
          view.updatePanel(this);
        } else {
          DungeonCommandController comd = new Move("SOUTH");
          op = comd.goCommand(model);
          if (op.equals("")) {
            op += "\nPlayer moved South. Current Loc: ";
            op += model.getCoordMap().get(model.getPlayerCopy().getLoc().getCaveId());
            deadOrAlive();
            op += doa;
          }
          view.updatePanel(this);
          if (!giantEnc().isEmpty()) {
            op = giantEnc();
            view.updatePanel(this);
          }
        }
        view.setEchoOutput(op);
      } else {
        op = doa;
        op += "\nCan't play any moves.";
        view.setEchoOutput(op);
      }
    }
    );

    keyPresses.put(KeyEvent.VK_LEFT, () -> {
      deadOrAlive();
      String op;
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 0)) {
        if (shoot.get()) {
          DungeonCommandController comd = new Shoot(dist.intValue(), "WEST");
          op = comd.goCommand(model);
          shoot.set(false);
          view.updatePanel(this);
        } else {
          DungeonCommandController comd = new Move("WEST");
          op = comd.goCommand(model);
          if (op.equals("")) {
            op += "\nPlayer moved West. Current Loc: ";
            op += model.getCoordMap().get(model.getPlayerCopy().getLoc().getCaveId());
            deadOrAlive();
            op += doa;
          }
          view.updatePanel(this);
          if (!giantEnc().isEmpty()) {
            op = giantEnc();
            view.updatePanel(this);
          }
        }
        view.setEchoOutput(op);
      } else {
        op = doa;
        op += "\nCan't play any moves.";
        view.setEchoOutput(op);
      }
    }
    );

    keyPresses.put(KeyEvent.VK_RIGHT, () -> {
      deadOrAlive();
      String op;
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 0)) {
        if (shoot.get()) {
          DungeonCommandController comd = new Shoot(dist.intValue(), "EAST");
          op = comd.goCommand(model);
          shoot.set(false);
          view.updatePanel(this);
        } else {
          DungeonCommandController comd = new Move("EAST");
          op = comd.goCommand(model);
          if (op.equals("")) {
            op += "\nPlayer moved East. Current Loc: ";
            op += model.getCoordMap().get(model.getPlayerCopy().getLoc().getCaveId());
            deadOrAlive();
            op += doa;
          }
          view.updatePanel(this);
          if (!giantEnc().isEmpty()) {
            op = giantEnc();
            view.updatePanel(this);
          }
        }
        view.setEchoOutput(op);
      } else {
        op = doa;
        op += "\nCan't play any moves.";
        view.setEchoOutput(op);
      }
    });

    KeyboardListener kbd = new KeyboardListener();
    kbd.setKeyPressedMap(keyPresses);
    view.addKeyListener(kbd);

  }


  private void configureButtonListener() {
    Map<String, Runnable> buttonClickedMap = new HashMap<>();
    ButtonListener buttonListener = new ButtonListener();

    buttonClickedMap.put("Reset Button", () -> {

      doa = "";
      view.addClickListener(this);

      model.makeCopy();
      this.model = model.getModelCopy();

      view.dungeonSetup(model);

      String op = "Resetting Game...";
      view.setEchoOutput(op);
      view.resetFocus();

    });

    buttonClickedMap.put("Player Stats Button", () -> {

      String op = "Treasure Collected:";
      op += model.getPlayerCopy().getTreasureCollected().toString();
      op += "\nTotal Arrows:";
      op += model.getPlayerCopy().getTotalArrows();

      view.clearInputString();
      view.setEchoOutput(op);
      view.resetFocus();

    });

    buttonClickedMap.put("Restart Button", () -> {

      view.setInps(this.model);

      int res = view.inputOptionPane();
      //OK_OPTION
      if (res == 0) {
        getInps();

        view.dungeonSetup(model);
        view.addClickListener(this);

        String op = "Starting New Game...";
        view.setEchoOutput(op);
        view.resetFocus();
      }

    });

    buttonClickedMap.put("Exit Button", () -> System.exit(0));

    buttonListener.setButtonClickedActionMap(buttonClickedMap);
    this.view.addActionListener(buttonListener);
  }

  private void getInps() {

    try {
      int rows = view.getRowInp();
      int cols = view.getColsInp();
      int interconnect = view.getInterconnectInp();
      int treasurePercentage = view.getTreasurePercentInp();
      boolean wrapping = view.getWrapInp();
      int numOtyugh = view.getNumOtyughInp();

      this.model = new DungeonImpl("Rimuru", rows, cols, interconnect,
              treasurePercentage, wrapping, new ActualRandomizer(), numOtyugh);

      model.setRows(rows);
      model.setColumns(cols);
      model.setInterconnectivity(interconnect);
      model.setOutyughNum(numOtyugh);
      model.setWrapping(wrapping);
      model.setTreasurePercent(treasurePercentage);

    } catch (IllegalArgumentException e) {
      view.showErrorMessage("Invalid Inputs");
    }

  }

  private void deadOrAlive() {
    if (model.playerKillOrNot() == -1) {
      doa = "Bad luck! You were killed by an Otyugh! Game Over!";
    } else if (model.playerKillOrNot() == 0) {
      doa = "Hurray! You reached the end! Game Over!";
    } else if (model.playerKillOrNot() == 2) {
      //escaped Otyugh
      doa = "e";
    } else if (model.playerKillOrNot() == -3) {
      doa = "Oh no! You fell into a pit! Game Over!";
    } else if (model.playerKillOrNot() == 1) {
      doa = "";
    }
  }

  private String giantEnc() {
    DungeonCommandController comd;
    String op = "";
    if (model.checkGiantEncounter() && model.getGiantCopy().getHealth() == 100) {
      if (model.getPlayerCopy().getTotalArrows() >= 4) {
        //Player chose to trade arrows.
        int r = view.giantEnc();
        if (r == 0) {
          comd = new KillGiant();
          op += comd.goCommand(model);
        } else if (r == 1) {
          op += "50% survival chance... ";
          comd = new PassGiant();
          op += comd.goCommand(model);
        }
      } else {
        op += "You don't have enough arrows to trade for a giant killing potion. "
                + "\n50% survival chance... ";
        comd = new PassGiant();
        op += comd.goCommand(model);
      }
    }
    return op;
  }

  @Override
  public void handleCellClick(int ind) {
    try {
      int curr = model.getPlayerCopy().getLoc().getCaveId();
      String dir = "";

      if (model.getCoordMap().get(ind).get(0) == model.getCoordMap().get(curr).get(0)) {
        if (model.getCoordMap().get(ind).get(1) > model.getCoordMap().get(curr).get(1)) {
          dir = "EAST";
        } else if (model.getCoordMap().get(ind).get(1) < model.getCoordMap().get(curr).get(1)) {
          dir = "WEST";
        }
      } else if (model.getCoordMap().get(ind).get(1) == model.getCoordMap().get(curr).get(1)) {
        if (model.getCoordMap().get(ind).get(0) > model.getCoordMap().get(curr).get(0)) {
          dir = "SOUTH";
        } else if (model.getCoordMap().get(ind).get(0) < model.getCoordMap().get(curr).get(0)) {
          dir = "NORTH";
        }
      }

      String op = "";
      if (doa.equals("") || doa.equals("e")
              || (model.checkGiantEncounter()) && model.getGiantCopy().getHealth() == 0) {
        DungeonCommandController comd = new Move(dir);
        op = comd.goCommand(model);
        if (op.equals("")) {
          op += "\nPlayer moved " + dir;
          op += doa;
        }
        view.updatePanel(this);
        if (!giantEnc().isEmpty()) {
          op = giantEnc();
          view.updatePanel(this);
        }
      } else {
        op = doa;
        op += "\nCan't play anymore moves!";
      }
      view.setEchoOutput(op);

    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }


}
