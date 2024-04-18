package org.cis1200.minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.io.*;

public class Minesweeper extends JFrame {

    private final int size = 10;
    private final int numMines = 10;
    private Cell[][] cells;
    private int revealedCells;
    private Timer timer;
    private int elapsedTime;
    private JLabel timerLabel;
    private JLabel gameStateLabel;
    private int flagsUsed;
    private boolean gameOver = false;

    public Minesweeper() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(size, size));
        initializeGameStateDisplay();
        initializeTimer();
        showInstructions();

        cells = new Cell[size][size];
        initializeBoard();
        setMines();
        initializeMenuBar();

        pack();
        setVisible(true);

    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveGame("minesweeper_save.txt"));

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> loadGame("minesweeper_save.txt"));

        menu.add(saveItem);
        menu.add(loadItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }


    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int row = i;
                final int col = j;

                cells[row][col] = new Cell();
                cells[row][col].addActionListener(new CellActionListener(row, col));

                cells[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(row, col);
                        }
                    }
                });

                cells[row][col].setFlagListener(e -> {
                    if (cells[row][col].isFlagged()) {
                        flagsUsed++;
                    } else {
                        flagsUsed--;
                    }
                    updateGameStateDisplay();
                });

                add(cells[row][col]);
            }
        }
    }



    private void updateGameStateDisplay() {
        gameStateLabel.setText("Flags used: " + flagsUsed);
    }


    private void setMines() {
        Random rand = new Random();
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (!cells[x][y].isMine()) {
                cells[x][y].setMine(true);
                incrementAdjacentMines(x, y);
                minesPlaced++;
            }
        }
    }

    private void incrementAdjacentMines(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < size && ny >= 0 && ny < size) {
                    cells[nx][ny].incrementAdjacentMines();
                }
            }
        }
    }
    
    void revealCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size || cells[x][y].isRevealed()) {
            return;
        }

        if (!timer.isRunning()) {
            timer.start();
        }

        cells[x][y].reveal();

        if (cells[x][y].isMine()) {
            gameOver(false);
            return;
        }

        revealedCells++;
        if (revealedCells == size * size - numMines) {
            gameOver(true);
        }

        if (cells[x][y].getAdjacentMines() == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {
                        revealCell(x + dx, y + dy);
                    }
                }
            }
        }
    }

    private void gameOver(boolean won) {
        this.gameOver = true;
        timer.stop();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.reveal();
            }
        }

        String message = won ? "Congratulations, you won!" : "Boom! Game Over.";
        JOptionPane.showMessageDialog(this, message);
        gameStateLabel.setText(message);
        System.exit(0);
    }

    public void saveGame(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    writer.write(cells[i][j].toFileString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < size) {
                for (int col = 0; col < size && col < line.length() / 2; col++) {
                    String cellState = line.substring(2 * col, 2 * col + 2);
                    cells[row][col].fromFileString(cellState);
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CellActionListener implements ActionListener {
        private final int x;
        private final int y;

        public CellActionListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            revealCell(x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Minesweeper::new);
    }

    public int getBoardSize() {
        return size;
    }

    public boolean isCellRevealed(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return cells[x][y].isRevealed();
        }
        return false;
    }

    public boolean isCellMine(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return cells[x][y].isMine();
        }
        return false;
    }

    public int getNumMines() {
        return numMines;
    }

    private void showInstructions() {
        JDialog instructionsDialog = new JDialog(this, "Game Instructions", true);
        instructionsDialog.setLayout(new BorderLayout());

        JTextArea instructionsText = new JTextArea();
        instructionsText.setText("Welcome to Minesweeper!\n\n"
                + "How to Play:\n"
                + "- Left-click on a cell to reveal it.\n"
                + "- Right-click on a cell to flag it as a mine.\n"
                + "- The goal is to reveal all cells without mines.\n\n"
                + "Features:\n"
                + "- Custom minefield generation.\n"
                + "- Timer to track how long you take to win.\n"
                + "Enjoy the game!");
        instructionsText.setEditable(false);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setLineWrap(true);

        instructionsDialog.add(instructionsText, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> instructionsDialog.dispose());
        instructionsDialog.add(closeButton, BorderLayout.SOUTH);

        instructionsDialog.setSize(400, 300);
        instructionsDialog.setLocationRelativeTo(this);
        instructionsDialog.setVisible(true);
    }

    private void initializeTimer() {
        elapsedTime = 0;
        timerLabel = new JLabel("Time: 0s", JLabel.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        timer = new Timer(1000, e -> {
            elapsedTime++;
            timerLabel.setText("Time: " + elapsedTime + "s");
        });
    }

    private void initializeGameStateDisplay() {
        gameStateLabel = new JLabel("Flags used: 0", JLabel.CENTER);
        add(gameStateLabel, BorderLayout.SOUTH);
    }

    public void simulateClick(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            revealCell(x, y);
        }
    }

    public void toggleFlag(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            cells[x][y].toggleFlag();
        }
    }

    public boolean isCellFlagged(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return cells[x][y].isFlagged();
        }
        return false;
    }

    public void resetGame() {
        timer.stop();
        elapsedTime = 0;
        timerLabel.setText("Time: 0s");

        revealedCells = 0;
        flagsUsed = 0;
        gameOver = false;
        gameStateLabel.setText("Flags used: 0");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].resetCell();
            }
        }

        setMines();
    }
    public int getElapsedTime() {
        return elapsedTime;
    }
    public int getFlagsUsed() {
        return flagsUsed;
    }
}


