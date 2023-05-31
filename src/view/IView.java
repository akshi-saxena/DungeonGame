package view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import control.IController;
import dungeon.ReadonlyDungeonModel;

/**
 * A view for Dungeon Game - displays the game board and provide visual interface
 * for users.
 */
public interface IView {

  void makeVisible();

  void showErrorMessage(String error);

  void addKeyListener(KeyListener listener);

  void addActionListener(ActionListener listener);

  void resetFocus();

  void setEchoOutput(String s);

  void clearInputString();

  void updatePanel(IController listener);

  void dungeonSetup(ReadonlyDungeonModel dungeonModel);

  int inputOptionPane();

  void options();

  int getRowInp();

  int getColsInp();

  int getInterconnectInp();

  int getTreasurePercentInp();

  boolean getWrapInp();

  int getNumOtyughInp();

  int giantEnc();

  void addClickListener(IController listener);

  void setInps(ReadonlyDungeonModel d);
}
