/*
File: Board.java
Authors: Micaila Marcelle (micailamarcelle)
Course: CSC 335
Purpose: Implements the actual board for the 2048 game, which will contain a
4 x 4 grid of Tile objects, and which will provide methods to respond to the 
actions of the user (right arrow, left arrow, up arrow, down arrow). The board
will also provide functionality for determining when the win condition has been
achieved. The board will also keep track of the user's current score, effectively
acting as the model within the system.
 */

// NOTE: STILL NEED TO FIGURE OUT HOW TO IMPLEMENT RANDOMLY ADDING 1-2 TILES PER
// MOVE WITHIN THE GAME!
// Keep in mind that this only needs to be done for moves that actually do something
// (i.e. moves in which something in the board actually goes somewhere- could keep
// track of the number of shifts, and only add when greater than 0?)

import java.util.Optional;

public class Board {
    // Declares the private instance variables for the Board class, which include
    // a nested array of Optional<Tile> objects (that will represent the board
    // itself) and an int representing the current score associated with the board.
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
        exist, are then combined, and all tiles are shifted as far to the right as possible.
        The score associated with the board is also then updated appropriately.
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
            secondTile.multiplyValByTwo();
            row[firstTileIndex] = Optional.empty();

            // We also add the value of the combined tile to the overall score associated with the
            // board
            this.score += secondTile.getValue();
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

    /*
        Method which can be used to update the Board in a manner appropriate for a left-arrow click
        within the 2048 game. This method looks at each row within boardGrid, combines all tiles that
        would be combined with a left shift, and moves all of the tiles as far to the left within the
        board as they can go once this is done. The score associated with the board is then updated
        appropriately according to the combinations made with these shifts.
     */
    public void shiftLeft() {
        // Iterates through all of the rows within boardGrid
        for (int i = 0; i < boardGrid.length; i++) {
            // Gets the current row
            Optional<Tile>[] curRow = boardGrid[i];

            // Combines everything horizontally that can be combined
            combineAllHorizontal(curRow);

            // Shifts everything in the row to the left once this is done
            shiftAllLeft(curRow);
        }
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within our 
        board, and which shifts all of the Tile objects within the board as far to the left as they
        can go
     */
    private void shiftAllLeft(Optional<Tile>[] row) {
        // Iterates until we run out of indices
        int i = 0;
        while (i < row.length) {
            // Finds the first Tile starting from the current index
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }

            // If there are no more Tiles left, then we simply return, since our job is done
            if (i >= row.length) {
                return;
            }

            // Otherwise, we've found a Tile, so we figure out the spot where it should go when shifted
            // to the left
            Tile curTile = row[i].get();
            int curIndex = i;
            while (i >= 0 && row[i].isPresent() == false) {
                i--;
            }
            i++;

            // We then add the current Tile into the appropriate spot, and fill its old spot with a null 
            // value, assuming that its new spot isn't just its old spot once again
            if (i != curIndex) {
                row[curIndex] = Optional.empty();
                row[i] = Optional.of(curTile);
            }

            // Increments i for the next iteration
            i++;
        }
    }

    /*
        Method which can be used to update the Board in a manner appropriate for an up-arrow click
        within the 2048 game. This method looks at each column within boardGrid, combines all tiles that
        would be combined with an up shift, and moves all of the tiles as far upwards within the
        board as they can go once this is done. The score associated with the board is then updated
        appropriately according to the combinations made with these shifts.
     */
    public void shiftUp() {
        // Iterates through all of the columns within boardGrid
        int curCol = 0;
        while (curCol < boardGrid.length) {
            // Makes all of the necessary combinations for an up shift, combining Tiles as necessary
            // within the current column
            combineAllVertical(curCol);

            // Shifts all Tiles as far up as they can go within the board after any potential combinations
            shiftAllUp(curCol);

            // Continues iterating on
            curCol++;
        }
    }

    /*
        Private helper method which can be used to shift all of the Tile objects in a particular
        column of the board as far up as they can go. This method takes in an int representing the
        particular column that we're interested in, and it does the shifting on the desired column
        within the boardGrid instance variable.
     */
    private void shiftAllUp(int col) {
        // Iterates through the current column from top to bottom
        int i = 0;
        while (i < boardGrid.length) {
            // Iterates until we find a Tile
            while (i < boardGrid.length && boardGrid[i][col].isPresent() == false) {
                i++;
            }

            // If we hit the end of the column without hitting any more Tiles, then we simply return,
            // since the job of the method is done
            if (i == boardGrid.length) {
                return;
            }

            // Otherwise, we get the Tile at the current spot in the board, then iterate backwards
            // through the column until we either hit another Tile object or we hit the top of the 
            // column
            int curIndex = i;
            Tile curTile = boardGrid[i][col].get();
            i--;
            while (i >= 0 && boardGrid[i][col].isPresent() == false) {
                i--;
            }
            i++;

            // We then add this Tile into its new spot, and replace its previous spot with an empty
            // Optional<Tile> object, assuming that its new spot is not the same as its current spot
            if (i != curIndex) {
                boardGrid[i][col] = Optional.of(curTile);
                boardGrid[curIndex][col] = Optional.empty();
            }

            // Finally, we increment i and continue iterating on
            i++;
        }
    }

    /*
        Private helper method which can be used to combine all adjacent tiles with the same value within
        a particular column of boardGrid. Takes in an int representing the column of boardGrid that we're
        interested in, and makes all of the necessary combinations, updating score as necessary
     */
    private void combineAllVertical(int col) {
        // Iterates until there are no possible vertical combinations left 
        while (areThereCombinationsVertical(col) == true) {
            // Finds two adjacent Tiles with the same value, keeping track of the index of the first
            Tile firstTile;
            int firstTileIndex;
            Tile secondTile;

            // Iterates until we find the first non-null tile
            int i = 0;
            while (boardGrid[i][col].isPresent() == false) {
                i++;
            }

            // Lets this be our first tile, capturing its value and index
            firstTile = boardGrid[i][col].get();
            firstTileIndex = i;
            int firstTileVal = firstTile.getValue();

            // Iterates until we find two adjacent tiles with the same value
            secondTile = firstTile;
            while (i < boardGrid.length) {
                // Finds the next non-null tile
                i++;
                while (boardGrid[i][col].isPresent() == false) {
                    i++;
                }

                // Checks to see whether this has the same value as our current tile
                if (boardGrid[i][col].get().getValue() == firstTileVal) {
                    // If it does, then we set secondTile equal to this tile, and break out of the 
                    // loop
                    secondTile = boardGrid[i][col].get();
                    break;
                } 

                // Otherwise, we make this our first tile and continue iterating on
                firstTile = boardGrid[i][col].get();
                firstTileIndex = i;
                firstTileVal = firstTile.getValue();
            }

            // Once we find our two adjacent Tiles with the same value, we combine them into a single
            // tile, replace the first tile with null, and update the value and color of the second tile
            secondTile.multiplyValByTwo();
            boardGrid[firstTileIndex][col] = Optional.empty();

            // We then update the score with the new value of this second tile
            this.score += secondTile.getValue();
        }
    }

    /*
        Private helper method which can be used in order to determine whether there are any remaining
        combinations to be made within a particular column of the board. Takes in an int representing the
        index of the column we're interested in, and returns true if there are any combinations, and 
        false otherwise
     */
    private boolean areThereCombinationsVertical(int col) {
        // Initially, we find the first non-null Tile within this column
        int i = 0;
        while (i < boardGrid.length && boardGrid[i][col].isPresent() == false) {
            i++;
        }

        // If we find no such Tile, then we return false, since there cannot be any combinations
        if (i == boardGrid.length) {
            return false;
        }

        // Otherwise, we continue iterating on until we reach the end of our column
        while (i < boardGrid.length) {
            // Gets the current Tile and its value
            Tile curTile = boardGrid[i][col].get();
            int curVal = curTile.getValue();

            // Iterates until we find another Tile or we hit the end of our column
            i++;
            while (i < boardGrid.length && boardGrid[i][col].isPresent() == false) {
                i++;
            }

            // If we find no such next tile, then we return false, since there cannot be any combinations
            if (i == boardGrid.length) {
                return false;
            }

            // Otherwise, we check to see whether these two Tiles have the same value
            if (curVal == boardGrid[i][col].get().getValue()) {
                // If so, then we return true, since there are still combinations left
                return true;
            }

            // If not, then we simply continue on to the next iteration
        }

        // If we find no Tiles that can be combined, then we return false
        return false;
    }

    /*
        Method which can be used to update the Board in a manner appropriate for a down-arrow click
        within the 2048 game. This method looks at each column within boardGrid, combines all tiles that
        would be combined with a down shift, and moves all of the tiles as far down within the
        board as they can go once this is done. The score associated with the board is then updated
        appropriately according to the combinations made with these shifts.
     */
    public void shiftDown() {
        // Iterates through all of the columns in boardGrid
        int curCol = 0;
        while (curCol < boardGrid.length) {
            // Makes all of the possible combinations within this column, updating the score according
            // to any combinations made
            combineAllVertical(curCol);

            // Shifts everything in the current column as far down as possible, after any potential
            // combinations are made
            shiftAllDown(curCol);

            // Increments curCol to continue iterating
            curCol++;
        }
    }

    /*
        Private helper method which can be used to shift all of the Tile objects in a particular
        column of the board as far down as they can go. This method takes in an int representing the
        particular column that we're interested in, and it does the shifting on the desired column
        within the boardGrid instance variable.
     */
    private void shiftAllDown(int col) {
        // Iterates through the given column from the bottom up
        int i = boardGrid.length - 1;
        while (i >= 0) {
            // Iterates until we either find a Tile from the current index or until we hit the top of
            // the column
            while (i >= 0 && boardGrid[i][col].isPresent() == false) {
                i--;
            }

            // If we find no other Tiles, then we simply return, since the job of the method is done
            if (i < 0) {
                return;
            }

            // Otherwise, we get the Tile at the current spot in the board, and iterate backwards 
            // through the column until we find where to put this Tile
            int curIndex = i;
            Tile curTile = boardGrid[i][col].get();
            i--;
            while (i >= 0 && boardGrid[i][col].isPresent() == false) {
                i--;
            }
            i++;

            // If this new spot is not the same as the current spot, then we put our Tile in this new 
            // spot, and fill its previous spot with an empty Optional<Tile> object
            if (curIndex != i) {
                boardGrid[i][col] = Optional.of(curTile);
                boardGrid[curIndex][col] = Optional.empty();
            }

            // We then decrement i and continue iterating on
            i--;
        }
    }

    /*
        Public method which can be used to check whether the game is over. Returns a value of the HasWon
        enumerated type, which will be WON if the player has won, LOST if the player has lost, and NOT_DONE
        if the game is not yet over
     */
    public HasWon isGameOver() {
        // First, we check to see whether the player has obtained a tile with a value of 2048
        if (findHighestValue() >= 2048) {
            // If so, we return true, since the player has won
            return HasWon.WON;
        }

        // Next, we check to see whether there are any empty tiles
        if (countNumEmpty() != 0) {
            // If so, then the game is not yet over, since the player still has moves left
            return HasWon.NOT_DONE;
        }

        // In all other cases, we need to check to see whether there are any additional combinations that
        // can be made. First, we iterate through all of the rows in the board, checking to see whether any
        // of these contain potential moves
        for (int i = 0; i < boardGrid.length; i++) {
            // Gets the current row
            Optional<Tile>[] curRow = boardGrid[i];

            // Checks to see whether there are any possible combinations    
            if (areThereCombinations(curRow) == true) {
                // If so, we return false, since the player still has moves left
                return HasWon.NOT_DONE;
            }
        }

        // If there are no possible combinations within the rows, then we check the columns for possible
        // combinations
        for (int col = 0; col < boardGrid.length; col++) {
            // Checks to see whether the current column contains any possible combinations
            if (areThereCombinationsVertical(col) == true) {
                // If so, we return false, since the player still has moves left
                return HasWon.NOT_DONE;
            }
        }

        // Otherwise, if there are no possible combinations in either the rows or cols, then we return 
        // true, since the player has lost
        return HasWon.LOST;
    }

    /*
        Private helper method which can be used in order to find the highest score associated with a
        Tile in the board. If there are no Tiles in the board, then this method returns 0, and otherwise,
        the method returns an int representing the highest value associated with a Tile in the board
     */
    private int findHighestValue() {
        // Initializes the current highest value to 0
        int curHighest = 0;

        // Iterates through all of the tiles in the board
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid.length; j++) {
                // Checks to see whether this spot actually contains a Tile
                if (boardGrid[i][j].isPresent() == true) {
                    // If so, we get the value of this tile
                    int curVal = boardGrid[i][j].get().getValue();

                    // We check to see whether this is greater than curHighest
                    if (curVal > curHighest) {
                        // If so, we update curHighest
                        curHighest = curVal;
                    }
                }
            }
        }

        // Once we're done iterating, curHighest is then returned
        return curHighest;
    }

    /*
        Private helper method which can be utilized in order to count the number of non-empty tiles
        currently present within the board. Takes in no parameters, and returns an int representing
        this number.
     */
    private int countNumEmpty() {
        // Initializes the return count to 0
        int count = 0;

        // Iterates through every tile in the board
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid.length; j++) {
                // Checks to see whether the current tile is empty
                if (boardGrid[i][j].isPresent() == false) {
                    // If so, we increment count
                    count++;
                }
            }
        }

        // We then return the final count
        return count;
    }
}
