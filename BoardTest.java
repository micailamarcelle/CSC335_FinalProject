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
    @Test
    public void testTile() {
        Tile a = new Tile(2, TileColor.GRAY);
        assertTrue(a.getTileColor() == TileColor.GRAY);
        assertTrue(a.getValue() == 2);
        a.multiplyValByTwo();
        assertTrue(a.getValue() == 4);
        assertTrue(a.getTileColor() == TileColor.LIGHT_BLUE);
        a.multiplyValByTwo();
        assertTrue(a.getValue() == 8);
        assertTrue(a.getTileColor() == TileColor.DARK_BLUE);
        a.multiplyValByTwo();
        assertTrue(a.getValue() == 16);
        assertTrue(a.getTileColor() == TileColor.PURPLE);
        a.multiplyValByTwo();
        assertTrue(a.getValue() == 32);
        assertTrue(a.getTileColor() == TileColor.GRAY);

        Tile b = new Tile(a);
        assertEquals(a, b);
    }

    @Test
    public void testEmptyBoard() {
        Board gameBoard = new Board(BoardSize.FOUR);
        assertTrue(gameBoard.getScore() == 0);
        assertTrue(gameBoard.length == 4);

        Board gameBoard2 = new Board(BoardSize.SIX);
        assertTrue(gameBoard2.getScore() == 0);
        assertTrue(gameBoard2.length == 6);

        Board gameBoard3 = new Board(BoardSize.EIGHT);
        assertTrue(gameBoard3.getScore() == 0);
        assertTrue(gameBoard3.length == 8);
    }
}
