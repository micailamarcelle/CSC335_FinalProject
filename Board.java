/*
File: Board.java
Authors: N/A (for now)
Course: CSC 335
Purpose: Implements the actual board for the 2048 game, which will contain a
4 x 4 grid of Tile objects, and which will provide methods to respond to the 
actions of the user (right arrow, left arrow, up arrow, down arrow). The board
will also provide functionality for determining when the win condition has been
achieved. The board will also keep track of the user's current score, effectively
acting as the model within the system.

May want to decompose this further? Unsure of exactly how we want to do this
 */

import java.util.Optional;

public class Board {
    private Tile[][] boardGrid;
    private int score;
    
    public Board(BoardSize size) {
        // 4 + 2*ordinal
        int sizeAsInt = 4 + 2*(size.ordinal());
        this.boardGrid = Tile[sizeAsInt];
        for (int i = 0; i < sizeAsInt; i ++) {
            this.boardGrid[sizeAsInt] = new Tile[sizeAsInt];
            for (int j = 0; j < sizeAsInt; j++) {
                Optional<Tile> curTile = new Optional.empty();
                boardGrid[i][j] = curTile;
            }
        }
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public getBoard() {
        int size = boardGrid.length;
        Tile[][] boardGridCopy = new Tile[size][size];
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j++) {
                //boardGridCopy[i][j] = boardGrid[i][j];
            }
        }
        return boardGridCopy;
    }

    public void shiftLeft() {

    }

    public void shiftRight() {

    }

    public void shiftUp() {

    }

    public void shiftDown() {
        
    }

    private void updateScore() {
        
    }

    public boolean isGameOver() {

    }
}
