===================
=: Core Concepts :=
===================

  1. (2D Arrays) The Minesweeper game board is represented using a 2D array of Cell
  objects.

  2. (Recursion) The revealCell method demonstrates recursion when revealing empty cells
  adjacent to a cell with zero adjacent mines. 

  3. (File I/O) The game includes functionality to save (saveGame) and load (loadGame)
  the game state to and from a file. This feature enhances user experience by allowing
  games to be paused and resumed later.

  4. (JUnit Testable Components) The Minesweeper game utilizes JUnit tests to validate
  the functionality of various components such as cell behavior, game state management,
  and file I/O operations. The design separates the game logic (model) from the user
  interface (view). This separation allows for testing game mechanics independently of
  the GUI components. The core game logic in the Minesweeper and Cell classes does not
  depend on the GUI, enabling unit tests to be written for game logic without the need
  for a user interface. The explanations of the tests are provided in the test documentation.


=========================
=: Implementation :=
=========================

  Minesweeper: The main class that extends JFrame, containing the game logic,
  UI elements, and methods for game functionality like revealCell, saveGame,
  and loadGame.

  Cell: A subclass of JButton representing individual cells on the Minesweeper
  grid. It keeps track of the cell state, such as whether it's a mine, flagged,
  or revealed.