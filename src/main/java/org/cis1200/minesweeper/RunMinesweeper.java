package org.cis1200.minesweeper;

public class RunMinesweeper implements Runnable {

    @Override
    public void run() {
        new Minesweeper();
    }
}
