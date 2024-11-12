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
 */

import java.util.Optional;

public class Board {
    // Declares the private instance variables for the Board class, which include
    // a nested array of Optional<Tile> objects (that will represent the board
    // itself) and an int representing the current score associated with the board
    private Optional<Tile>[][] boardGrid;
    private int score;
    
    /*
        Primary constructor for the Board class, which takes in a BoardSize enumerated
        type representing the size of the board, and which constructs and intitializes 
        a new Board object, filled with empty Optional<Tile> objects

        @pre size != null
        @post constructs a new, empty Board object
     */
    public Board(BoardSize size) {
        // Initializes the score to 0 when the Board is created
        this.score = 0;

        // Uses the given enum in order to represent the size of the board as an int
        int sizeAsInt = 4 + 2 * (size.ordinal());

        // Constructs the actual board, filling it with empty Optional<Tile> objects,
        // since we want the board to be effectively empty at the beginning of the game
        // CHECK TO SEE WHETHER THERE IS A BETTER WAY TO DO THIS!!
        this.boardGrid = (Optional<Tile>[][]) new Optional<?>[sizeAsInt][sizeAsInt];
        for (int i = 0; i < sizeAsInt; i++) {
            for (int j = 0; j < sizeAsInt; j++) {
                boardGrid[i][j] = Optional.empty();
            }
        }
    }

    /*
        Getter for the current score associated with the Board object

        @return an int representing the current score for the board
     */
    public int getScore() {
        return score;
    }

    /*
        Getter for the current state of the Board. To avoid any escaping references,
        considering that Tile objects are mutable, this method goes through the boardGrid
        associated with the current Board object, copying each Tile within the board into
        a new nested array of Optional<Tile> objects via the Tile copy constructor. The
        copy of the nested array is then returned.
     */
    public Optional<Tile>[][] getBoard() {
        // Gets the size of the board
        int size = boardGrid.length;

        // Constructs a new nested array of Optional<Tile> objects, representing the copy of
        // the current Board object
        Optional<Tile>[][] boardGridCopy = (Optional<Tile>[][]) new Optional<?>[size][size];

        // Iterates through all of the elements of this.boardGrid, copying all of them into
        // the new nested array, ensuring that all Tiles are copied in a deep manner to 
        // prevent any escaping references.
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j++) {
                if (boardGrid[i][j].isPresent() == false) {
                    boardGridCopy[i][j] = Optional.empty();
                } else {
                    boardGridCopy[i][j] = Optional.of(new Tile(boardGrid[i][j].get()));
                }
            }
        }

        // Returns the resulting copy of the board
        return boardGridCopy;
    }

    /*
        Method which updates the board in an appropriate manner in response to a right-arrow
        click within the 2048 game. This method looks at each row within boardGrid, and sees
        if there are any tiles that can be combined from left to right. These tiles, if they
        exist, are then combined, and all tiles are shifted as far to the right as possible
     */
    public void shiftRight() {
        // Iterates through all of the rows within the board
        for (int i = 0; i < boardGrid.length; i++) {
            // Gets the current row
            Optional<Tile>[] curRow = boardGrid[i];

            // Combines everything horizontally wihtin the row
            combineAllHorizontal(curRow);

            // Shifts everything to the right after combination
            shiftAllRight(curRow);
        }
    }

    /*
        Private helper method, which takes in an Optional<Tile> array and which moves all
        Tiles within the array as far to the right as they can go.
     */
    private void shiftAllRight(Optional<Tile>[] row) {
        // Iterates through the array, but backwards
        int i = row.length - 1;
        while (i >= 0) {
            // Finds the first Tile object
            while (i >= 0 && row[i].isPresent() == false) {
                i--;
            }

            // If we find no more Tile objects, then we break our loop
            if (i < 0) {
                break;
            }

            // Once we find this object, we shift it right until we encounter either the 
            // end of the array or another Tile object
            Tile curTile = row[i].get();
            int curIndex = i;
            i++;
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }
            i--;

            // Adds curTile into the appropriate slot, if this isn't simply its current slot,
            // replacing its current slot with an empty Optional<T> object if necessary
            if (i != curIndex) {
                row[curIndex] = Optional.empty();
                row[i] = Optional.of(curTile);
            }

            // Decrements i so we can continue iterating
            i--;
        }
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within
        our board, and which combines all tiles that are adjacent to one another with the same
        value. 
     */
    private void combineAllHorizontal(Optional<Tile>[] row) {
        // Iterates until there are no more possible horizontal combinations
        while (areThereCombinations(row) == true) {
            // Finds two Tile objects with the same value, keeping track of their indices
            Tile firstTile;
            int firstTileIndex;
            Tile secondTile;

            // Gets the first position in the row containing a non-null value
            int i = 0;
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }

            // Letting this first Tile be the firstTile, we then iterate until we find another
            // Tile object with the same value
            firstTile = row[i].get();
            firstTileIndex = i;
            int firstTileVal = firstTile.getValue();

            // Sets secondTile to firstTile for now, simply to prevent any compilation errors
            // POTENTIALLY A BETTER WAY TO DO THIS?
            secondTile = firstTile;
            while (i < row.length) {
                // Iterates until we find the next non-null tile
                i++;
                while (i < row.length && row[i].isPresent() == false) {
                    i++;
                }

                // Checks to see whether this Tile has the same value as our current firstTile
                if (row[i].get().getValue() == firstTileVal) {
                    // If so, then we've found our second Tile, and so we update secondTile 
                    // accordingly, and break out of our loop
                    secondTile = row[i].get();
                    break;
                } else {
                    // Otherwise, we set firstTile equal to this Tile, and continue iterating
                    firstTile = row[i].get();
                    firstTileVal = firstTile.getValue();
                    firstTileIndex = i;
                }
            }       

            // Once we find both our tiles with the same value, along with their associated
            // indices, we multiply the value in the second tile by two, update its color, and
            // remove the first tile from the board
            secondTile.updateToNextColor();
            secondTile.multiplyValByTwo();
            row[firstTileIndex] = Optional.empty();
        }
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within
        our board, and which check to see whether there are any remaining Tile objects which
        have the same value and are next to one another in the grid, meaning that they should/
        could be combined. Returns true if there are any combinations left, and false otherwise
     */
    private boolean areThereCombinations(Optional<Tile>[] row) {
        // Initially, we find the first non-null Tile within this row of the board
        int i = 0;
        while (i < row.length && row[i].isPresent() == false) {
            i++;
        }

        // If we found no non-null Tiles, then we return false, since there cannot be any combinations
        if (i == row.length) {
            return false;
        }

        // Otherwise, we then iterate through the rest of the row
        while (i < row.length) {
            // Gets the current Tile and its value
            Tile curTile = row[i].get();
            int curVal = curTile.getValue();

            // Iterates until we find another tile
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }

            // If we find no other Tiles, then we return false, since there are no combinations
            if (i == row.length) {
                return false;
            }

            // Otherwise, we check to see whether the current Tile has the value we're looking for
            if (row[i].get().getValue() == curVal) {
                // If they do have the same value, then we return true, since there is still a combination
                return true;
            }

            // If they don't have the same value, then we simply continue on to the next iteration
        }

        // If we never find two adjacent Tiles with the same value, then we return false, since there
        // cannot be any combinations
        return false;
    }

    public void shiftLeft() {

    }

    public void shiftUp() {

    }

    public void shiftDown() {
        
    }

    private void updateScore() {
        
    }

    public boolean isGameOver() {
        return false;
    }
}
