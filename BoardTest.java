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


    // Test when board is intilized whether two tiles are present on the board with values of 
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
