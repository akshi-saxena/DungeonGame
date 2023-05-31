import java.io.InputStreamReader;

import control.DungeonController;
import control.IController;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import randomizer.ActualRandomizer;
import view.GraphicsView;
import view.IView;

/**
 * Driver for the game. User interacts with this.
 */
public class GameDriver {

  /**
   * Main method of Driver.
   */
  public static void main(String[] args) {

    if (args.length == 0) {
      String name = "Rimuru";
      int rows = 4;
      int cols = 5;
      int interconnect = 3;
      int treasurePercentage = 60;
      boolean wrap = false;
      int outyughNum = 5;

      Dungeon model = new DungeonImpl(name, rows, cols, interconnect,
              treasurePercentage, wrap, new ActualRandomizer(), outyughNum);

      IView view = new GraphicsView(model);

      IController controller = new DungeonController(view, model);
      controller.playGUIGame();

    } else if (args.length > 0 && args.length < 6) {

      throw new IllegalArgumentException("Invalid command line arguments given. "
              + "Please provide correct arguments");
    } else {

      try {
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int interconnect = Integer.parseInt(args[2]);
        int treasurePercentage = Integer.parseInt(args[3]);
        boolean wrap = Boolean.parseBoolean(args[4]);
        int outyughNum = Integer.parseInt(args[5]);

        String name = "Rimuru";

        Dungeon d = new DungeonImpl(name, rows, cols, interconnect,
                treasurePercentage, wrap, new ActualRandomizer(), outyughNum);

        System.out.println("Game Starts\n");

        Readable input = new InputStreamReader(System.in);
        Appendable output = System.out;
        new DungeonController(input, output).playGame(d);

      } catch (IllegalArgumentException e) {
        System.out.println("Not a valid argument");
      }
    }

  }
}
