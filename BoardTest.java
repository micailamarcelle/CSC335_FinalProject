/*
File: BoardTest.java
Authors: N/A (for now)
Course: CSC 335
Purpose: Provides JUnit tests for the functionalities of the Board class
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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


    // Test when board is intilized whether two tiles are present on the board with values of 
    // either 2 or 4
    @Test
    public void testStartBoard() {
        Board gameBoard = new Board(BoardSize.FOUR);

        int tileCount = 0;
        Optional<Tile>[] presentTiles = new Optional<?>[2];
        int presentTilesIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j].isPresent()) {
                    tileCount ++;
                    presentTiles[presentTilesIndex] = Optional.of(new Tile(gameBoard[i][j].get()));
                    presentTilesIndex ++;
                }
            }
        }
        assertEquals(tileCount, 2);
        assertTrue(presentTiles[0].getValue() == 2 || presentTiles[0].getValue() == 4);
        assertTrue(presentTiles[1].getValue() == 2 || presentTiles[1].getValue() == 4);
    }

    @Test
    public void testIsGameOver() {

    }

    @Test
    public void testShiftDown() {

    }

    @Test
    public void testShiftUp() {

    }

    @Test
    public void testShiftRight() {

    }

    @Test
    public void testShiftLeft() {
        
    }
}
