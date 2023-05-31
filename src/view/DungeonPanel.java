package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import control.IController;
import dungeon.ReadonlyDungeonModel;
import location.Direction;
import location.Smell;
import location.Sound;

class DungeonPanel extends JPanel {

  private final ReadonlyDungeonModel model;
  private JPanel mazeArea;
  private JLabel[] labels;

  private int rows;
  private int cols;
  private ArrayList<Integer> visitedIds;


  public DungeonPanel(ReadonlyDungeonModel model) {
    super();
    this.model = model;

    visitedIds = new ArrayList<>();

    mazeArea = new JPanel();
    displayDungeon();

    this.add(mazeArea);
  }

  private void displayDungeon() {

    rows = model.getRows();
    cols = model.getCols();

    mazeArea.setLayout(new GridLayout(rows, cols));

    labels = new JLabel[(rows) * (cols)];

    for (int i = 0; i < (rows) * (cols); i++) {
      labels[i] = new JLabel();
    }

    try {
      for (int ind = 0; ind < (rows * cols); ind++) {
        String dir = "";
        dir = getDir(dir, ind);

        BufferedImage myPicture = ImageIO.read(getClass().getResourceAsStream("/" + dir + ".png"));

        if (model.getPlayerCopy().getLoc().getCaveId()
                == model.getCavesList().get(ind).getCaveId()) {
          BufferedImage newBase = addSmell(myPicture);
          BufferedImage newBase1 = addSound(newBase);
          BufferedImage combined = addItems(newBase1, ind);
          labels[model.getCavesList().get(ind).getCaveId()].setIcon(new ImageIcon(combined));
          visitedIds.add(model.getCavesList().get(ind).getCaveId());
        } else {
          BufferedImage newBase = addItems(myPicture, ind);
          BufferedImage combined = addBlank(newBase);
          labels[model.getCavesList().get(ind).getCaveId()].setIcon(new ImageIcon(combined));
        }
      }

      for (int i = 0; i < rows * cols; i++) {
        if (visitedIds.contains(model.getCavesList().get(i).getCaveId())
                && model.getCavesList().get(i).getCaveId()
                != model.getPlayerCopy().getLoc().getCaveId()) {
          BufferedImage myPicture = ImageIO.read(getClass().getResourceAsStream("/"
                  + getDir("", i) + ".png"));
          BufferedImage combined = addItems(myPicture, i);
          labels[model.getCavesList().get(i).getCaveId()].setIcon(new ImageIcon(combined));
          visitedIds.add(model.getCavesList().get(i).getCaveId());
        }
        mazeArea.add(labels[i]);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getDir(String dir, int ind) {

    if (model.getCavesList().get(ind).getDirectionList().containsKey(Direction.NORTH)) {
      dir += "N";
    }
    if (model.getCavesList().get(ind).getDirectionList().containsKey(Direction.EAST)) {
      dir += "E";
    }
    if (model.getCavesList().get(ind).getDirectionList().containsKey(Direction.SOUTH)) {
      dir += "S";
    }
    if (model.getCavesList().get(ind).getDirectionList().containsKey(Direction.WEST)) {
      dir += "W";
    }

    return dir;
  }

  private BufferedImage addItems(BufferedImage img, int ind) throws IOException {
    BufferedImage combined = img;
    if (model.getCavesList().get(ind).getCreaturePresent()) {
      if (model.getCavesList().get(ind).getOtyugh().getHealth() > 0) {
        combined = overlay(combined, "/otyugh.png", 1);
      }
    }
    if (!model.getCavesList().get(ind).getTreasureList().isEmpty()) {
      for (int j = 0; j < model.getCavesList().get(ind).getTreasureList().size(); j++) {
        if (model.getCavesList().get(ind).getTreasureList().get(j).name()
                .equals("SAPPHIRE")) {
          combined = overlay(combined, "/emerald.png", 1);
        } else if (model.getCavesList().get(ind).getTreasureList().get(j).name()
                .equals("RUBY")) {
          combined = overlay(combined, "/ruby.png", 1);
        } else if (model.getCavesList().get(ind).getTreasureList().get(j).name()
                .equals("DIAMOND")) {
          combined = overlay(combined, "/diamond.png", 1);
        }
      }
    }
    if (model.getCavesList().get(ind).isThiefPresent()) {
      combined = overlay(combined, "/thief.png", 1);
    }
    if (model.getCavesList().get(ind).isPitPresent()) {
      combined = overlay(combined, "/pit.png", 1);
    }
    if (model.getCavesList().get(ind).getCaveId() == model.getPlayerCopy().getLoc().getCaveId()) {
      combined = overlay(combined, "/player.png", 1);
    }
    if (model.getCavesList().get(ind).getCaveId() == model.getGiantCopy().getLoc().getCaveId()) {
      if (model.getGiantCopy().getHealth() == 100) {
        combined = overlay(combined, "/giant.png", 1);
      }
    }
    if (model.getCavesList().get(ind).getArrow() > 0) {
      combined = overlay(combined, "/arrow-white.png", 1);
    }
    return combined;
  }

  /**
   * Piazza post by Prof. Clark: https://piazza.com/class/kt0jcw0x7h955a?cid=1471
   */
  private BufferedImage overlay(BufferedImage starting, String fpath, int offset)
          throws IOException {
    BufferedImage overlay = ImageIO.read(getClass().getResourceAsStream(fpath));
    int w = Math.max(starting.getWidth(), overlay.getWidth());
    int h = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(overlay, offset, offset, null);
    return combined;
  }

  private BufferedImage addSmell(BufferedImage img) throws IOException {
    BufferedImage combined = img;
    if (model.detectSmell() == Smell.HIGH) {
      combined = overlay(combined, "/stench02.png", 1);
    } else if (model.detectSmell() == Smell.LOW) {
      combined = overlay(combined, "/stench01.png", 1);
    }
    return combined;
  }

  private BufferedImage addSound(BufferedImage img) throws IOException {
    BufferedImage combined = img;
    if (model.detectPit() == Sound.HIGH) {
      combined = overlay(combined, "/wind2.png", 1);
    } else if (model.detectPit() == Sound.LOW) {
      combined = overlay(combined, "/wind.png", 1);
    }
    return combined;
  }

  private BufferedImage addBlank(BufferedImage img) throws IOException {
    img = overlay(img, "/blank.png", 0);
    return img;
  }

  void change() {

    mazeArea.removeAll();
    displayDungeon();

    mazeArea.removeAll();
    for (int i = 0; i < rows * cols; i++) {
      mazeArea.add(labels[i]);
    }

    mazeArea.validate();
    mazeArea.repaint();
  }


  void addMListener(IController listener) {
    for (int i = 0; i < rows * cols; i++) {
      labels[i].addMouseListener(new MouseListener(listener, i));
    }
  }

}
