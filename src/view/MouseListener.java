/*
 * Code from: Canvas PDP Course - Module 11 - Lesson 10 Summary - MVCZip file
 */

package view;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import control.IController;

/**
 * A simple button listener.
 */
public class MouseListener extends MouseAdapter {

  private IController listener;
  private int ind;

  public MouseListener(IController listener, int i) {
    this.listener = listener;
    this.ind = i;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    listener.handleCellClick(ind);
  }

}
