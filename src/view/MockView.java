package view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.IOException;

import control.IController;
import dungeon.ReadonlyDungeonModel;


/**
 * Mock View for testing the controller.
 */
public class MockView implements IView {

  private Appendable log;

  public MockView(Appendable log) {
    this.log = log;
  }


  @Override
  public void dungeonSetup(ReadonlyDungeonModel dungeonModel) {
    try {
      log.append("In view: sets up dungeon");
    } catch (IOException e) {
      // do nothing
    }
  }


  @Override
  public void addKeyListener(KeyListener listener) {
    try {
      log.append("KeyListener added\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void addActionListener(ActionListener listener) {
    try {
      log.append("ActionListener added\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void addClickListener(IController listener) {
    try {
      log.append("ClickListener added\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void setEchoOutput(String s) {
    try {
      log.append("Output from Controller to display in view:\n").append(s);
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void clearInputString() {
    try {
      log.append("Cleared string");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void updatePanel(IController listener) {
    try {
      log.append("Dungeon panel updated\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void options() {
    try {
      log.append("New Game Options\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public int inputOptionPane() {
    return 0;
  }

  @Override
  public int getRowInp() {
    return 0;
  }

  @Override
  public int getColsInp() {
    return 0;
  }

  @Override
  public int getInterconnectInp() {
    return 0;
  }

  @Override
  public int getTreasurePercentInp() {
    return 0;
  }

  @Override
  public boolean getWrapInp() {
    return false;
  }

  @Override
  public int getNumOtyughInp() {
    return 0;
  }

  @Override
  public int giantEnc() {
    return 0;
  }


  @Override
  public void makeVisible() {
    try {
      log.append("make visible called\n");
    } catch (IOException e) {
      // do nothing
    }
  }


  @Override
  public void resetFocus() {
    try {
      log.append("controller added\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void showErrorMessage(String error) {
    try {
      log.append("controller added\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void setInps(ReadonlyDungeonModel d) {
    try {
      log.append("inputs set\n");
    } catch (IOException e) {
      // do nothing
    }
  }


}
