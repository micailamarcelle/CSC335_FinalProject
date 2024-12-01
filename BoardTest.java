
/*
File: BoardTest.java
Authors: Elise Bushra (ebushra), Cate Yip (cyip)
Course: CSC 335
Purpose: Provides JUnit tests for the functionalities of the Board class
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class BoardTest {
    // Test board size and score
    @Test
    public void testEmptyBoard() {
        Board gameBoard = new Board(BoardSize.FOUR);
        assertTrue(gameBoard.getScore() == 0);
        assertTrue(gameBoard.getBoard().length == 4);

        Board gameBoard2 = new Board(BoardSize.SIX);
        assertTrue(gameBoard2.getScore() == 0);
        assertTrue(gameBoard2.getBoard().length == 6);

        Board gameBoard3 = new Board(BoardSize.EIGHT);
        assertTrue(gameBoard3.getScore() == 0);
        assertTrue(gameBoard3.getBoard().length == 8);
    }

    // Test when board is initialized whether two tiles are present on the board
    // with
    // values of
    // either 2 or 4
    @Test
    public void testStartBoard() {
        Board boardObject = new Board(BoardSize.FOUR);
        boardObject.placeTilesStartGame();
        Optional<Tile>[][] gameBoard = boardObject.getBoard();

        int tileCount = 0;
        Tile[] presentTiles = new Tile[2];
        int presentTilesIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j].isPresent()) {
                    tileCount++;
                    presentTiles[presentTilesIndex] = gameBoard[i][j].get();
                    presentTilesIndex++;
                }
            }
        }
        assertEquals(tileCount, 2);
        assertTrue(presentTiles[0].getValue() == 2 || presentTiles[0].getValue() == 4);
        assertTrue(presentTiles[1].getValue() == 2 || presentTiles[1].getValue() == 4);
    }

    @Test
    public void testSetTile() {
        Board boardObjectSizeFour = new Board(BoardSize.FOUR);
        boardObjectSizeFour.setTile(0, 0, 2);

        Optional<Tile>[][] gameBoardFour = boardObjectSizeFour.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoardFour[i][j].isPresent()) {
                    assertEquals(i, 0);
                    assertEquals(j, 0);
                    assertEquals(gameBoardFour[i][j].get().getValue(), 2);
                } else {
                    assertEquals(gameBoardFour[i][j], Optional.empty());
                }
            }
        }

        Board boardObjectSizeSix = new Board(BoardSize.SIX);
        boardObjectSizeSix.setTile(4, 1, 4);

        Optional<Tile>[][] gameBoardSix = boardObjectSizeSix.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (gameBoardSix[i][j].isPresent()) {
                    assertEquals(i, 4);
                    assertEquals(j, 1);
                    assertEquals(gameBoardSix[i][j].get().getValue(), 4);
                } else {
                    assertEquals(gameBoardSix[i][j], Optional.empty());
                }
            }
        }

        Board boardObjectSizeEight = new Board(BoardSize.EIGHT);
        boardObjectSizeEight.setTile(7, 7, 2);

        Optional<Tile>[][] gameBoardEight = boardObjectSizeEight.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoardEight[i][j].isPresent()) {
                    assertEquals(i, 7);
                    assertEquals(j, 7);
                    assertEquals(gameBoardEight[i][j].get().getValue(), 2);
                } else {
                    assertEquals(gameBoardEight[i][j], Optional.empty());
                }
            }
        }
    }

    @Test
    public void testIsGameOverEmptyBoard() {
        Board boardFour = new Board(BoardSize.FOUR);
        assertEquals(boardFour.isGameOver(), HasWon.NOT_DONE);

        Board boardSix = new Board(BoardSize.SIX);
        assertEquals(boardSix.isGameOver(), HasWon.NOT_DONE);

        Board boardEight = new Board(BoardSize.EIGHT);
        assertEquals(boardEight.isGameOver(), HasWon.NOT_DONE);
    }

    @Test
    public void testIsGameOverNotFull() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(1, 3, 4);
        boardFour.setTile(0, 2, 2);
        boardFour.setTile(3, 2, 2);
        assertEquals(boardFour.isGameOver(), HasWon.NOT_DONE);

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 5, 4);
        boardSix.setTile(4, 2, 2);
        boardSix.setTile(5, 3, 2);
        assertEquals(boardSix.isGameOver(), HasWon.NOT_DONE);

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(6, 7, 4);
        boardEight.setTile(0, 5, 2);
        boardEight.setTile(7, 3, 2);
        assertEquals(boardEight.isGameOver(), HasWon.NOT_DONE);
    }

    @Test
    public void testIsGameOverFullShifts() {
        Board boardFour = new Board(BoardSize.FOUR);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                boardFour.setTile(i, j, 2);
            }
        }
        assertEquals(boardFour.isGameOver(), HasWon.NOT_DONE);

        Board boardSix = new Board(BoardSize.SIX);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boardSix.setTile(i, j, 2);
            }
        }
        assertEquals(boardSix.isGameOver(), HasWon.NOT_DONE);

        Board boardEight = new Board(BoardSize.EIGHT);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardEight.setTile(i, j, 2);
            }
        }
        assertEquals(boardEight.isGameOver(), HasWon.NOT_DONE);
    }

    @Test
    public void testIsGameOverNoShifts() {
        Board boardFour = new Board(BoardSize.FOUR);
        // 2 4 2 4
        // 4 2 4 2
        // 2 4 2 4
        // 4 2 4 2
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    boardFour.setTile(i, j, 2);
                } else if (i % 2 == 1 && j % 2 == 1) {
                    boardFour.setTile(i, j, 2);
                } else {
                    boardFour.setTile(i, j, 4);
                }
            }
        }
        assertEquals(boardFour.isGameOver(), HasWon.LOST);

        Board boardSix = new Board(BoardSize.SIX);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    boardSix.setTile(i, j, 2);
                } else if (i % 2 == 1 && j % 2 == 1) {
                    boardSix.setTile(i, j, 2);
                } else {
                    boardSix.setTile(i, j, 4);
                }
            }
        }
        assertEquals(boardSix.isGameOver(), HasWon.LOST);

        Board boardEight = new Board(BoardSize.EIGHT);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    boardEight.setTile(i, j, 2);
                } else if (i % 2 == 1 && j % 2 == 1) {
                    boardEight.setTile(i, j, 2);
                } else {
                    boardEight.setTile(i, j, 4);
                }
            }
        }
        assertEquals(boardEight.isGameOver(), HasWon.LOST);
    }

    @Test
    public void testIsGameOver2048Size4() {
        Board boardFour = new Board(BoardSize.FOUR);

        // get an 8 in top left corner
        boardFour.setTile(0, 0, 4);
        boardFour.setTile(0, 1, 4);
        boardFour.shiftLeft();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 8);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // get 16 in top left corner
        boardFour.setTile(1, 0, 8);
        boardFour.shiftUp();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 16);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // get 32 in top left corner
        boardFour.setTile(3, 3, 16);
        boardFour.shiftUp();
        boardFour.shiftLeft();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 32);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Gets 64 in the top left corner
        boardFour.setTile(3, 0, 32);
        boardFour.shiftUp();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 64);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Gets 128 in the top left corner
        boardFour.setTile(0, 1, 32);
        boardFour.setTile(1, 0, 32);
        boardFour.shiftRight();
        boardFour.shiftUp();
        boardFour.shiftLeft();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 128);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Gets 256 in the top left corner
        boardFour.setTile(0, 3, 64);
        boardFour.setTile(3, 0, 64);
        boardFour.shiftRight();
        boardFour.shiftUp();
        boardFour.shiftLeft();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 256);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Gets 512 in the top left corner
        boardFour.setTile(0, 3, 256);
        boardFour.shiftLeft();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 512);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Gets 1024 in the top left corner
        boardFour.setTile(3, 0, 512);
        boardFour.shiftUp();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 1024);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Gets 2048 in the top left corner
        boardFour.setTile(2, 2, 1024);
        boardFour.shiftUp();
        boardFour.shiftLeft();
        gameBoardFour = boardFour.getBoard();
        assertTrue(gameBoardFour[0][0].isPresent());
        assertEquals(gameBoardFour[0][0].get().getValue(), 2048);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        // Checks to make sure that the method indicates that the user actually won
        assertEquals(boardFour.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOver2048Size6() {
        Board boardSix = new Board(BoardSize.SIX);

        // fill boards with 64s
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boardSix.setTile(i, j, 64);
            }
        }

        boardSix.shiftUp();
        // 128 128 128 128 128 128
        // 128 128 128 128 128 128
        // 128 128 128 128 128 128
        boardSix.shiftLeft();
        // 256 256 256
        // 256 256 256
        // 256 256 256
        boardSix.shiftUp();
        // 512 512 512
        // 256 256 256
        boardSix.shiftLeft();
        // cur state
        // 1024 512 . . . .
        // 512 256 . . . .
        // . . . . . .

        boardSix.setTile(0, 2, 512);
        boardSix.shiftLeft();
        boardSix.shiftLeft();
        // 2048 . . . . .
        // 512 256 . . . .
        // . . . . . .

        // Ensures that there is a 2048 in the top left corner, where we expect there to
        // be a Tile of this value
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertTrue(gameBoardSix[0][0].isPresent());
        assertEquals(gameBoardSix[0][0].get().getValue(), 2048);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 1 && j == 0) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 512);
                } else if (i == 1 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 256);
                } else if (!(i == 0 && j == 0)) {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        // Checks to make sure that the game indicates that the user has actually won
        assertEquals(boardSix.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOver2048Size8() {
        Board boardEight = new Board(BoardSize.EIGHT);

        boardEight.setTile(0, 0, 2048);
        assertEquals(boardEight.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOverWinFullBoardNoMovesSize4() {
        // Creates the board and fills it with alternating 2's and 4's
        Board board = new Board(BoardSize.FOUR);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board.setTile(i, j, 2);
                } else if (i % 2 != 0 && j % 2 != 0) {
                    board.setTile(i, j, 2);
                } else {
                    board.setTile(i, j, 4);
                }
            }
        }

        // Adds a single 2048 to the board
        board.setTile(2, 3, 2048);
        assertEquals(board.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOverWinFullBoardMovesSize4() {
        // Creates the board and fills it with only 4's
        Board board = new Board(BoardSize.FOUR);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board.setTile(i, j, 4);
            }
        }

        // Adds a single 2048 to the board
        board.setTile(2, 3, 2048);
        assertEquals(board.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOverWinFullBoardNoMovesSize6() {
        // Creates the board and fills it with alternating 2's and 4's
        Board board = new Board(BoardSize.SIX);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board.setTile(i, j, 2);
                } else if (i % 2 != 0 && j % 2 != 0) {
                    board.setTile(i, j, 2);
                } else {
                    board.setTile(i, j, 4);
                }
            }
        }

        // Adds a single 2048 to the board
        board.setTile(5, 1, 2048);
        assertEquals(board.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOverWinFullBoardMovesSize6() {
        // Creates the board and fills it with only 2's
        Board board = new Board(BoardSize.SIX);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                board.setTile(i, j, 2);
            }
        }

        // Adds a single 2048 to the board
        board.setTile(1, 5, 2048);
        assertEquals(board.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOverWinFullBoardNoMovesSize8() {
        // Creates the board and fills it with alternating 2's and 4's
        Board board = new Board(BoardSize.EIGHT);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board.setTile(i, j, 2);
                } else if (i % 2 != 0 && j % 2 != 0) {
                    board.setTile(i, j, 2);
                } else {
                    board.setTile(i, j, 4);
                }
            }
        }

        // Adds a single 2048 to the board
        board.setTile(2, 6, 2048);
        assertEquals(board.isGameOver(), HasWon.WON);
    }

    @Test
    public void testIsGameOverWinFullBoardMovesSize8() {
        // Creates the board and fills it with only 2's
        Board board = new Board(BoardSize.EIGHT);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setTile(i, j, 2);
            }
        }

        // Adds a single 2048 to the board
        board.setTile(6, 7, 2048);
        assertEquals(board.isGameOver(), HasWon.WON);
    }

    @Test
    public void testShiftUpTwoTiles() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(1, 1, 2);
        boardFour.setTile(3, 1, 2);
        boardFour.shiftUp();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(5, 3, 2);
        boardSix.setTile(3, 3, 2);
        boardSix.shiftUp();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 4);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(6, 7, 2);
        boardEight.setTile(0, 7, 2);
        boardEight.shiftUp();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 4);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftLeftTwoTiles() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(3, 2, 2);
        boardFour.setTile(3, 0, 2);
        boardFour.shiftLeft();
        assertEquals(boardFour.getScore(), 4);
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 0) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(4, 5, 2);
        boardSix.setTile(4, 3, 2);
        boardSix.shiftLeft();
        assertEquals(boardSix.getScore(), 4);
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 4 && j == 0) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(0, 2, 2);
        boardEight.setTile(0, 7, 2);
        boardEight.shiftLeft();
        assertEquals(boardEight.getScore(), 4);
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0 && j == 0) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftRightTwoTiles() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(3, 2, 2);
        boardFour.setTile(3, 0, 2);
        boardFour.shiftRight();
        assertEquals(boardFour.getScore(), 4);
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(4, 5, 2);
        boardSix.setTile(4, 3, 2);
        boardSix.shiftRight();
        assertEquals(boardSix.getScore(), 4);
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 4 && j == 5) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(0, 2, 2);
        boardEight.setTile(0, 7, 2);
        boardEight.shiftRight();
        assertEquals(boardEight.getScore(), 4);
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftDownTwoTiles() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(1, 1, 2);
        boardFour.setTile(3, 1, 2);
        boardFour.shiftDown();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(5, 3, 2);
        boardSix.setTile(3, 3, 2);
        boardSix.shiftDown();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 4);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 5 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(6, 7, 2);
        boardEight.setTile(0, 7, 2);
        boardEight.shiftDown();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 4);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 7 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftDownThreeTiles() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(0, 1, 4);
        boardFour.setTile(1, 1, 2);
        boardFour.setTile(3, 1, 2);
        boardFour.shiftDown();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                } else if (i == 2 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 3, 8);
        boardSix.setTile(5, 3, 2);
        boardSix.setTile(3, 3, 2);
        boardSix.shiftDown();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 4);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 5 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 4);
                } else if (i == 4 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 8);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(6, 7, 2);
        boardEight.setTile(0, 7, 2);
        boardEight.setTile(7, 7, 4);
        boardEight.shiftDown();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 4);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 7 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else if (i == 6 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftUpNoCombos() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(1, 1, 2);
        boardFour.setTile(3, 1, 32);
        boardFour.setTile(2, 0, 8);
        boardFour.shiftUp();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 8);
                } else if (i == 0 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 2);
                } else if (i == 1 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 32);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 1, 64);
        boardSix.setTile(3, 1, 32);
        boardSix.setTile(1, 3, 64);
        boardSix.setTile(3, 3, 32);
        boardSix.shiftUp();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 1 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else if (i == 0 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 1 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(0, 7, 2);
        boardEight.setTile(7, 7, 4);
        boardEight.shiftUp();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 2);
                } else if (i == 1 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftDownNoCombos() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(1, 1, 2);
        boardFour.setTile(3, 1, 32);
        boardFour.setTile(2, 0, 8);
        boardFour.shiftDown();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 0) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 8);
                } else if (i == 3 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 32);
                } else if (i == 2 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 2);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 1, 64);
        boardSix.setTile(3, 1, 32);
        boardSix.setTile(1, 3, 64);
        boardSix.setTile(3, 3, 32);
        boardSix.shiftDown();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 4 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 5 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else if (i == 4 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 5 && j == 3) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(0, 7, 2);
        boardEight.setTile(7, 7, 4);
        boardEight.shiftDown();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 6 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 2);
                } else if (i == 7 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftRightNoCombos() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(0, 0, 2);
        boardFour.setTile(0, 1, 128);
        boardFour.shiftRight();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 3) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 128);
                } else if (i == 0 && j == 2) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 2);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 1, 32);
        boardSix.setTile(3, 1, 32);
        boardSix.setTile(1, 3, 64);
        boardSix.setTile(3, 3, 64);
        boardSix.shiftRight();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 1 && j == 5) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 1 && j == 4) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else if (i == 3 && j == 5) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 3 && j == 4) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(0, 7, 2);
        boardEight.setTile(0, 6, 4);
        boardEight.setTile(1, 7, 64);
        boardEight.setTile(1, 0, 128);
        boardEight.shiftRight();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 2);
                } else if (i == 0 && j == 6) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else if (i == 1 && j == 7) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 64);
                } else if (i == 1 && j == 6) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 128);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

    @Test
    public void testShiftLeftNoCombos() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(0, 0, 2);
        boardFour.setTile(0, 1, 128);
        boardFour.shiftLeft();
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        assertEquals(boardFour.getScore(), 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 2);
                } else if (i == 0 && j == 1) {
                    assertTrue(gameBoardFour[i][j].isPresent());
                    assertEquals(gameBoardFour[i][j].get().getValue(), 128);
                } else {
                    assertFalse(gameBoardFour[i][j].isPresent());
                }
            }
        }

        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 1, 32);
        boardSix.setTile(3, 1, 32);
        boardSix.setTile(1, 3, 64);
        boardSix.setTile(3, 3, 64);
        boardSix.shiftLeft();
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        assertEquals(boardSix.getScore(), 0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 1 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 1 && j == 0) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else if (i == 3 && j == 1) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 64);
                } else if (i == 3 && j == 0) {
                    assertTrue(gameBoardSix[i][j].isPresent());
                    assertEquals(gameBoardSix[i][j].get().getValue(), 32);
                } else {
                    assertFalse(gameBoardSix[i][j].isPresent());
                }
            }
        }

        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(0, 7, 2);
        boardEight.setTile(0, 6, 4);
        boardEight.setTile(1, 7, 64);
        boardEight.setTile(1, 0, 128);
        boardEight.shiftLeft();
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        assertEquals(boardEight.getScore(), 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j == 1) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 2);
                } else if (i == 0 && j == 0) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
                } else if (i == 1 && j == 1) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 64);
                } else if (i == 1 && j == 0) {
                    assertTrue(gameBoardEight[i][j].isPresent());
                    assertEquals(gameBoardEight[i][j].get().getValue(), 128);
                } else {
                    assertFalse(gameBoardEight[i][j].isPresent());
                }
            }
        }
    }

        @Test
        public void testDoubleCombos() {
            Board boardFour = new Board(BoardSize.FOUR);
            for (int i = 0; i < 4; i ++) {
                for (int j = 0; j < 4; j ++) {
                    boardFour.setTile(i, j, 4);
                }
            }
            boardFour.shiftUp();
            Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
            for (int i = 0; i < 2; i ++) {
                for (int j = 0; j < 4; j ++) {
                    assertEquals(gameBoardFour[i][j].get().getValue(), 8);
                }
            }
            boardFour.shiftDown();
            gameBoardFour = boardFour.getBoard();
            for (int i = 3; i < 4; i ++) {
                for (int j = 0; j < 4; j ++) {
                    assertEquals(gameBoardFour[i][j].get().getValue(), 16);
                }
            }
            boardFour.shiftLeft();
            gameBoardFour = boardFour.getBoard();
            assertEquals(gameBoardFour[3][1].get().getValue(), 32);
            assertEquals(gameBoardFour[3][0].get().getValue(), 32);

            boardFour.shiftRight();
            gameBoardFour = boardFour.getBoard();
            assertEquals(gameBoardFour[3][3].get().getValue(), 64);

            // size six
            Board boardSix = new Board(BoardSize.SIX);
            for (int i = 0; i < 6; i ++) {
                for (int j = 0; j < 6; j ++) {
                    boardSix.setTile(i, j, 4);
                }
            }
            boardSix.shiftUp();
            Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
            for (int i = 0; i < 3; i ++) {
                for (int j = 0; j < 6; j ++) {
                    assertEquals(gameBoardSix[i][j].get().getValue(), 8);
                }
            }
            boardSix.shiftLeft();
            gameBoardSix = boardSix.getBoard();
            for (int i = 0; i < 3; i ++) {
                for (int j = 0; j < 3; j ++) {
                    assertEquals(gameBoardSix[i][j].get().getValue(), 16);
                }
            }

            // size 8
            Board boardEight = new Board(BoardSize.EIGHT);
            for (int i = 0; i < 8; i ++) {
                for (int j = 0; j < 8; j ++) {
                    boardEight.setTile(i, j, 4);
                }
            }
            boardEight.shiftUp();
            Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
            for (int i = 0; i < 4; i ++) {
                for (int j = 0; j < 8; j ++) {
                    assertEquals(gameBoardEight[i][j].get().getValue(), 8);
                }
            }
            boardEight.shiftDown();
            gameBoardEight = boardEight.getBoard();
            for (int i = 6; i < 8; i ++) {
                for (int j = 0; j < 8; j ++) {
                    assertEquals(gameBoardEight[i][j].get().getValue(), 16);
                }
            }
            boardEight.shiftLeft();
            gameBoardEight = boardEight.getBoard();
            for (int i = 6; i < 8; i ++) {
                for (int j = 0; j < 4; j ++) {
                    assertEquals(gameBoardEight[i][j].get().getValue(), 32);
                }
            }

            boardEight.shiftRight();
            gameBoardEight = boardEight.getBoard();
            for (int i = 6; i < 8; i ++) {
                for (int j = 6; j < 8; j ++) {
                    assertEquals(gameBoardEight[i][j].get().getValue(), 64);
                }
            }

            boardEight.shiftUp();
            boardEight.shiftRight();
            gameBoardEight = boardEight.getBoard();
            assertEquals(gameBoardEight[0][7].get().getValue(), 256);
        }

        @Test
        public void testOrderOfCombos() {
            Board boardFour = new Board(BoardSize.FOUR);
            for (int i = 0; i < 3; i ++) {
                for (int j = 0; j < 3; j ++) {
                    boardFour.setTile(i, j, 4);
                }
            }
            boardFour.shiftUp();
            Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
            for (int i = 0; i < 1; i ++) {
                for (int j = 0; j < 3; j ++) {
                    assertEquals(gameBoardFour[i][j].get().getValue(), 8);
                }
            }
            for (int i = 1; i < 2; i ++) {
                for (int j = 0; j < 3; j ++) {
                    assertEquals(gameBoardFour[i][j].get().getValue(), 4);
                }
            }
            boardFour.shiftLeft();
            gameBoardFour = boardFour.getBoard();
            assertEquals(gameBoardFour[0][0].get().getValue(), 16);
            assertEquals(gameBoardFour[1][0].get().getValue(), 8);
            assertEquals(gameBoardFour[0][1].get().getValue(), 8);
            assertEquals(gameBoardFour[1][1].get().getValue(), 4);
        }

    @Test
    public void testPlaceRandomTileSizeFourBoard() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(1, 1, 2);
        boardFour.setTile(2, 0, 8);
        int moves = boardFour.shiftDown();
        assertEquals(moves, 2); // one move for the 8 shifting down and the second for the 2 shifting down
        boardFour.placeRandomTile(moves); // place one random tile
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        int numTiles = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoardFour[i][j].isPresent()) {
                    numTiles ++;
                }
            }
        }
        assertEquals(numTiles, 3);
    }

    @Test
    public void testDoesNotPlaceRandomTileSizeFourBoard() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(3, 0, 2);
        boardFour.setTile(3, 1, 8);
        int moves = boardFour.shiftLeft();
        assertEquals(moves, 0); // one move for the 8 shifting down and the second for the 2 shifting down
        boardFour.placeRandomTile(moves); // Try placing a tile but there are no moves so it should not place a tile
        Optional<Tile>[][] gameBoardFour = boardFour.getBoard();
        int numTiles = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoardFour[i][j].isPresent()) {
                    numTiles ++;
                }
            }
        }
        assertEquals(numTiles, 2);
    }

    @Test
    public void testPlaceRandomTilesSizeSixBoard() {
        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(1, 1, 2);
        boardSix.setTile(2, 0, 8);
        int moves = boardSix.shiftDown();
        assertEquals(moves, 2); // one move for the 8 shifting down and the second for the 2 shifting down
        boardSix.placeRandomTile(moves); // place two random tiles
        boardSix.placeRandomTile(moves);
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        int numTiles = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (gameBoardSix[i][j].isPresent()) {
                    numTiles ++;
                }
            }
        }
        assertEquals(numTiles, 4);
    }

    @Test
    public void testDoesNotPlaceRandomTilesSizeSixBoard() {
        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(0, 0, 2);
        boardSix.setTile(0, 1, 8);
        int moves = boardSix.shiftUp();
        assertEquals(moves, 0); // one move for the 8 shifting down and the second for the 2 shifting down
        boardSix.placeRandomTile(moves); // Try to place two random tiles
        boardSix.placeRandomTile(moves);
        Optional<Tile>[][] gameBoardSix = boardSix.getBoard();
        int numTiles = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (gameBoardSix[i][j].isPresent()) {
                    numTiles ++;
                }
            }
        }
        assertEquals(numTiles, 2);
    }

    @Test
    public void testPlaceRandomTileSizeEightBoard() {
        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(1, 1, 2);
        boardEight.setTile(2, 0, 8);
        int moves = boardEight.shiftDown();
        assertEquals(moves, 2); // one move for the 8 shifting down and the second for the 2 shifting down
        boardEight.placeRandomTile(moves); // place three random tiles
        boardEight.placeRandomTile(moves);
        boardEight.placeRandomTile(moves);
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        int numTiles = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoardEight[i][j].isPresent()) {
                    numTiles ++;
                }
            }
        }
        assertEquals(numTiles, 5);
    }

    @Test
    public void testDoesNotPlaceRandomTileSizeEightBoard() {
        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(7, 7, 2);
        boardEight.setTile(6, 7, 64);
        int moves = boardEight.shiftRight();
        assertEquals(moves, 0); // one move for the 8 shifting down and the second for the 2 shifting down
        boardEight.placeRandomTile(moves); // Try to place three random tiles
        boardEight.placeRandomTile(moves);
        boardEight.placeRandomTile(moves);
        Optional<Tile>[][] gameBoardEight = boardEight.getBoard();
        int numTiles = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoardEight[i][j].isPresent()) {
                    numTiles ++;
                }
            }
        }
        assertEquals(numTiles, 2);
    }

    @Test
    public void testCountCombosSizeFourBoard() {
        Board boardFour = new Board(BoardSize.FOUR);
        boardFour.setTile(0, 0, 2);
        boardFour.setTile(0, 1, 2);
        int moves = boardFour.shiftLeft();
        assertEquals(moves, 1);
    }

    @Test
    public void testCountCombosSizeSixBoard() {
        Board boardSix = new Board(BoardSize.SIX);
        boardSix.setTile(0, 0, 2);
        boardSix.setTile(0, 1, 2);
        boardSix.setTile(1, 1, 2);
        int moves = boardSix.shiftLeft();
        assertEquals(moves, 2);
    }

    @Test
    public void testCountCombosSizeEightBoard() {
        Board boardEight = new Board(BoardSize.EIGHT);
        boardEight.setTile(7, 0, 2);
        boardEight.setTile(6, 0, 2);
        boardEight.setTile(7, 7, 32);
        boardEight.setTile(6, 7, 32);
        int moves = boardEight.shiftDown();
        assertEquals(moves, 2);
    }

}