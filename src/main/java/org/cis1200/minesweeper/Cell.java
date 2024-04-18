package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.event.*;

class Cell extends JButton {
    private boolean isFlagged;
    private boolean isMine;
    private boolean isRevealed;
    private int adjacentMines;
    private ActionListener flagListener;

    public Cell() {
        super();
        this.isMine = false;
        this.isRevealed = false;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void incrementAdjacentMines() {
        if (!isMine) {
            adjacentMines++;
        }
    }
    public boolean isMine() {
        return isMine;
    }

    public void reveal() {
        if (isRevealed) {
            return;
        }

        isRevealed = true;
        setText(isMine ? "*" : Integer.toString(adjacentMines));
        setEnabled(false);
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public String toFileString() {
        return (isMine ? "M" : "E") + (isRevealed ? "R" : "H");
    }

    public void fromFileString(String str) {
        if (str.length() != 2) {
            return;
        }
        isMine = str.charAt(0) == 'M';
        isRevealed = str.charAt(1) == 'R';
        if (isRevealed) {
            setText(isMine ? "*" : Integer.toString(adjacentMines));
            setEnabled(false);
        } else {
            setText("");
            setEnabled(true);
        }
    }
    public void toggleFlag() {
        isFlagged = !isFlagged;
        setText(isFlagged ? "F" : "");
        if (flagListener != null) {
            flagListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public void resetCell() {
        this.isFlagged = false;
        this.isMine = false;
        this.isRevealed = false;
        this.adjacentMines = 0;
        setText("");
        setEnabled(true);
    }

    public void setFlagListener(ActionListener listener) {
        this.flagListener = listener;
    }

    public boolean isFlagged() {
        return isFlagged;
    }
}
