package view;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import control.IController;
import dungeon.ReadonlyDungeonModel;

/**
 * Implementation of the view.
 */
public class GraphicsView extends JFrame implements IView {

  private final JMenuBar mb;
  private JTextField row;
  private JTextField col;
  private JTextField interc;
  private JTextField wrap;
  private JTextField treasurePercent;
  private JTextField otyughNum;
  private JMenuItem quitButton;
  private JMenuItem restartButton;
  private JMenuItem resetButton;
  private JMenuItem playerStatsButton;
  private JLabel display;
  private JPanel pane;
  private JFrame f;

  private String def_row;
  private String def_col;
  private String def_interc;
  private String def_wrap;
  private String def_tp;
  private String def_otyugh;

  private DungeonPanel dungeonPanel;

  /**
   * Constructor for Dungeon View.
   *
   * @param dungeonModel Read only model of Dungeon.
   */
  public GraphicsView(ReadonlyDungeonModel dungeonModel) {
    super();
    this.setTitle("Dungeon Master");
    this.setSize(550, 550);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());

    def_row = String.valueOf(dungeonModel.getRows());
    def_col = String.valueOf(dungeonModel.getCols());
    def_interc = String.valueOf(dungeonModel.getInterconnectivity());
    def_wrap = String.valueOf(dungeonModel.getWrapping());
    def_tp = String.valueOf(dungeonModel.getTreasurePercent());
    def_otyugh = String.valueOf(dungeonModel.getOtyughNum());

    mb = new JMenuBar();
    this.setJMenuBar(mb);

    menuSetup();
    dungeonSetup(dungeonModel);

    resetFocus();
    this.pack();
  }

  @Override
  public void dungeonSetup(ReadonlyDungeonModel dungeonModel) {
    this.getContentPane().removeAll();

    dungeonPanel = new DungeonPanel(dungeonModel);
    this.setPreferredSize(new Dimension(450, 450));
    this.setLocationRelativeTo(null);
    this.add(dungeonPanel);
    this.getContentPane().add(new JScrollPane(dungeonPanel));

    display = new JLabel("Game Begins! Start exploring the dungeon!");
    display.setPreferredSize(new Dimension(300, 50));
    this.add(display, BorderLayout.SOUTH);
  }

  private void menuSetup() {
    JMenu menutab = new JMenu("Dungeon Settings");

    playerStatsButton = new JMenuItem("Player Stats");
    playerStatsButton.setActionCommand("Player Stats Button");

    resetButton = new JMenuItem("Reset Game");
    resetButton.setActionCommand("Reset Button");

    restartButton = new JMenuItem("New Game - Change Settings");
    restartButton.setActionCommand("Restart Button");

    quitButton = new JMenuItem("Exit Game");
    quitButton.setActionCommand("Exit Button");

    menutab.add(playerStatsButton);
    menutab.add(resetButton);
    menutab.add(restartButton);
    menutab.add(quitButton);

    mb.add(menutab);
  }

  @Override
  public int inputOptionPane() {
    f = new JFrame();
    pane = new JPanel();
    pane.setLayout(new GridLayout(0, 2, 2, 2));

    options();

    pane.add(new JLabel("Enter number of rows: "));
    pane.add(row);

    pane.add(new JLabel("Enter number of columns: "));
    pane.add(col);

    pane.add(new JLabel("Enter interconnectivity: "));
    pane.add(interc);

    pane.add(new JLabel("Enter treasure percentage: "));
    pane.add(treasurePercent);

    pane.add(new JLabel("Do you want wrapping? Enter true or false: "));
    pane.add(wrap);

    pane.add(new JLabel("Enter number of otyughs: "));
    pane.add(otyughNum);

    int option = JOptionPane.showConfirmDialog(f, pane, "Game Settings",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

    return option;
  }

  @Override
  public void setInps(ReadonlyDungeonModel dungeonModel) {
    def_row = String.valueOf(dungeonModel.getRows());
    def_col = String.valueOf(dungeonModel.getCols());
    def_interc = String.valueOf(dungeonModel.getInterconnectivity());
    def_wrap = String.valueOf(dungeonModel.getWrapping());
    def_tp = String.valueOf(dungeonModel.getTreasurePercent());
    def_otyugh = String.valueOf(dungeonModel.getOtyughNum());
  }

  @Override
  public void options() {
    row = new JTextField(def_row, 5);
    row.setText(row.getText());
    col = new JTextField(def_col, 5);
    col.setText(col.getText());
    interc = new JTextField(def_interc, 5);
    interc.setText(interc.getText());
    treasurePercent = new JTextField(def_tp, 5);
    treasurePercent.setText(treasurePercent.getText());
    wrap = new JTextField(def_wrap, 5);
    wrap.setText(wrap.getText());
    otyughNum = new JTextField(def_otyugh, 5);
    otyughNum.setText(otyughNum.getText());
  }

  @Override
  public int getRowInp() {
    return Integer.parseInt(row.getText());
  }

  @Override
  public int getColsInp() {
    return Integer.parseInt(col.getText());
  }

  @Override
  public int getInterconnectInp() {
    return Integer.parseInt(col.getText());
  }

  @Override
  public int getTreasurePercentInp() {
    return Integer.parseInt(treasurePercent.getText());
  }

  @Override
  public boolean getWrapInp() {
    return Boolean.parseBoolean(wrap.getText());
  }

  @Override
  public int getNumOtyughInp() {
    return Integer.parseInt(otyughNum.getText());
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String error) {
    System.out.println("Error: " + error);
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void addActionListener(ActionListener listener) {
    playerStatsButton.addActionListener(listener);
    resetButton.addActionListener(listener);
    restartButton.addActionListener(listener);
    quitButton.addActionListener(listener);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void setEchoOutput(String s) {
    display.setText(s);
  }

  @Override
  public void clearInputString() {
    display.setText("");
  }

  @Override
  public void updatePanel(IController listener) {
    dungeonPanel.change();
    dungeonPanel.addMListener(listener);
  }

  @Override
  public int giantEnc() {
    f = new JFrame();
    pane = new JPanel();
    pane.setLayout(new GridLayout(0, 1, 2, 2));


    pane.add(new JLabel("Press YES to trade 4 arrows for a 100% effective "
            + "Giant killing potion"));
    pane.add(new JLabel("Press NO to take risk and pass the Giant."));

    int option = JOptionPane.showConfirmDialog(f, pane, "Game Settings",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    return option;
  }

  @Override
  public void addClickListener(IController listener) {
    dungeonPanel.addMListener(listener);
  }

}
