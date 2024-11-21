/*
File: BoardTest.java
Authors: Elise Bushra (ebushra)
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


    // Test when board is initilized whether two tiles are present on the board with values of 
    // either 2 or 4
    @Test
    public void testStartBoard() {
        Board boardObject = new Board(BoardSize.FOUR);
        Optional<Tile>[][] gameBoard = boardObject.getBoard();

        int tileCount = 0;
        Tile[] presentTiles = new Tile[2];
        int presentTilesIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j].isPresent()) {
                    tileCount ++;
                    presentTiles[presentTilesIndex] = gameBoard[i][j].get();
                    presentTilesIndex ++;
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
                    assertEquals(gameBoardEight[i][j].get().getValue(), 4);
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

    /*@Test 
    public void testIsGameOver2048() {
        Board boardFour = new Board(BoardSize.FOUR);

        // Gets 64 in top left corner
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftLeft();
        boardFour.shiftUp();
        boardFour.shiftLeft();


        // Gets 64 in top left corner, with 32 to the right of it, 16 to the right of that
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftUp();
        boardFour.shiftLeft();

        // Gets 128 in the top left corner
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftUp();
        boardFour.shiftLeft();
        boardFour.shiftLeft();
        boardFour.shiftLeft();

        // Gets 128 in the top left corner, with 64 next to it
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftLeft();
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftUp();
        boardFour.shiftLeft();

        for (int i = 0; i < 4; i++) {
            for (int j = 3; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftUp();
        boardFour.shiftLeft();
        boardFour.shiftLeft();

        // Gets 128 in top left corner, with 64 to the right of it, and 64 below it
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftLeft();
        boardFour.shiftLeft();
        boardFour.shiftUp();


        for (int i = 3; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftLeft();
        boardFour.shiftLeft();
        boardFour.shiftUp();
        boardFour.shiftUp();

        // Gets 256 in top left corner
        boardFour.shiftRight();
        boardFour.shiftUp();
        boardFour.shiftLeft();
        //assertEquals(boardFour.isGameOver(), HasWon.LOST);

        // Gets 256 in the top left corner, with 128 to the right of it
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }

        boardFour.shiftUp();
        boardFour.shiftUp();
        boardFour.shiftLeft();
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                boardFour.setTile(i, j, 4);
            }
        }
        boardFour.shiftUp();
        boardFour.shiftLeft();

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
 
}*/

public void testIsGameOver2048Size4() {
    Board boardFour = new Board(BoardSize.FOUR);
    
    while (boardFour.findHighestValue() < 2048) {
        fillWithFours(boardFour); // Refill with 4s where needed
        boardFour.shiftUp();      // Perform merges
        boardFour.shiftUp();
        boardFour.shiftLeft();    // Align tiles
        boardFour.shiftLeft();
    }

    assertEquals(boardFour.isGameOver(), HasWon.WON);
}

public void testIsGameOver2048Size6() {
    Board boardSix = new Board(BoardSize.SIX);
    
    while (boardSix.findHighestValue() < 2048) {
        fillWithFours(boardSix); // Refill with 4s where needed
        boardSix.shiftUp();      // Perform merges
        boardSix.shiftUp();
        boardSix.shiftUp();
        boardSix.shiftLeft();    // Align tiles
        boardSix.shiftLeft();
        boardSix.shiftLeft();
    }

    assertEquals(boardSix.isGameOver(), HasWon.WON);
}

public void testIsGameOver2048Size8() {
    Board boardEight = new Board(BoardSize.EIGHT);
    
    while (boardEight.findHighestValue() < 2048) {
        fillWithFours(boardEight); // Refill with 4s where needed
        boardEight.shiftUp();      // Perform merges
        boardEight.shiftUp();
        boardEight.shiftUp();
        boardEight.shiftUp();
        boardEight.shiftLeft();    // Align tiles
        boardEight.shiftLeft();
        boardEight.shiftLeft();
        boardEight.shiftLeft();
    }

    assertEquals(boardEight.isGameOver(), HasWon.WON);
}

private void fillWithFours(Board b) {
    for (int i = 0; i < b.length; i++) {
        for (int j = 0; j < b.length; j++) {
            if (!b[i][j].isPresent()) {
                b.setTile(i, j, 4);
            }
        }
    }
}
