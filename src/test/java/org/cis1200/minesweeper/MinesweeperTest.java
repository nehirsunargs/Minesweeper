package org.cis1200.minesweeper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MinesweeperTest {

    private Minesweeper game;

    @BeforeEach
    void setUp() {
        game = new Minesweeper();
    }

   //I created this test to ensure that when the Minesweeper game starts,
   // the board is correctly initialized with the right size, which is 10x10 in this case.
   // I also check that all cells are initially unrevealed, as this is a critical aspect of
   // the game's starting state.
    @Test
    void testBoardInitialization() {
        assertEquals(10, game.getBoardSize(), "Board should be of size 10x10");
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                assertFalse(game.isCellRevealed(i, j), "Cell should not be revealed initially");
            }
        }
    }

   //In this test, I count the number of mines placed on the board and verify that it
   // matches the expected number (NUM_MINES). This is crucial to ensure that the game
   // starts with the correct level of challenge.
    @Test
    void testMinePlacement() {
        int mineCount = 0;
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isCellMine(i, j)) {
                    mineCount++;
                }
            }
        }
        assertEquals(game.getNumMines(), mineCount, "Incorrect number of mines placed");
    }

    //Here, I reveal a cell and then try to reveal it again. The purpose is to confirm that
    // revealing an already-revealed cell does not alter its state. This test ensures the
    // stability of the game state during player interaction.
    @Test
    void testRevealAlreadyRevealedCell() {
        game.revealCell(0, 0);
        boolean initialRevealedState = game.isCellRevealed(0, 0);
        game.revealCell(0, 0);
        assertEquals(initialRevealedState, game.isCellRevealed(0, 0), "Revealing an " +
                "already revealed cell should not change its state");
    }

   //I specifically test the behavior of revealing an edge cell (like (0, 0)), which is important
   // for verifying the game's behavior at the boundaries of the board. Ensuring correct
   // functionality at the edges is critical for a robust game.
    @Test
    void testRevealEdgeCell() {
        game.revealCell(0, 0);
        assertTrue(game.isCellRevealed(0, 0), "Edge cell should be revealed after " +
                "clicking");
    }

    //In this test, I toggle a flag on a cell and then check if it's correctly flagged. This
    // functionality is a key aspect of Minesweeper gameplay, as players need to mark cells
    // they suspect contain mines.
    @Test
    void testFlaggingFunctionality() {
        Minesweeper game = new Minesweeper();
        game.toggleFlag(0, 0);
        assertTrue(game.isCellFlagged(0, 0), "Cell (0,0) should be flagged");
    }

    //I simulate a player clicking on a cell and flagging another, then reset the game.
    // This test is designed to ensure that the reset functionality works correctly, bringing
    // all game elements back to their initial state.
    @Test
    void testResetFunctionality() {
        Minesweeper game = new Minesweeper();
        game.simulateClick(0, 0);
        game.toggleFlag(1, 1);

        game.resetGame();

        assertFalse(game.isCellRevealed(0, 0), "Cell (0,0) should not be revealed " +
                "after reset");
        assertFalse(game.isCellFlagged(1, 1), "Cell (1,1) should not be flagged " +
                "after reset");
        assertEquals(0, game.getElapsedTime(), "Timer should be reset to 0");
    }

    //This test involves flagging and unflagging cells, then checking the number of flags used.
    // It's essential for ensuring that the game accurately tracks the number of flags, which
    // is a vital part of game strategy and player feedback.
    @Test
    void testCorrectFlagCount() {
        Minesweeper game = new Minesweeper();
        game.toggleFlag(1, 1);
        game.toggleFlag(2, 2);
        game.toggleFlag(1, 1);

        assertEquals(1, game.getFlagsUsed(), "Only one cell should remain flagged");
    }
}
