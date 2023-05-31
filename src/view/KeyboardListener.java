/*
 * Code from: Canvas PDP Course - Module 11 - Lesson 10 Summary MVCExample.zip
 */

package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * This class represents a keyboard listener. It is configurable by the
 * controller that instantiates it.
 * This class implements the KeyListener interface, so that its object can be
 * used as a valid key listener for Java Swing.
 */
public class KeyboardListener implements KeyListener {
  private Map<Character, Runnable> keyTypedMap;
  private Map<Integer, Runnable> keyPressedMap;
  private Map<Integer, Runnable> keyReleasedMap;

  /**
   * Default constructor.
   */
  public KeyboardListener() {
    keyTypedMap = null;
    keyPressedMap = null;
    keyReleasedMap = null;
  }

  public void setKeyTypedMap(Map<Character, Runnable> map) {
    keyTypedMap = map;
  }

  public void setKeyPressedMap(Map<Integer, Runnable> map) {
    keyPressedMap = map;
  }

  public void setKeyReleasedMap(Map<Integer, Runnable> map) {
    keyReleasedMap = map;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // do nothing
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (keyPressedMap.containsKey(e.getKeyCode())) {
      keyPressedMap.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    //do nothing
  }
}
