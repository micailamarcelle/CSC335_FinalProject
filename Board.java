/*
File: Board.java
Authors: Micaila Marcelle (micailamarcelle), Cate Yip (cyip)
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


// NOTE: The game starts with 2 random tiles filled with Tile objects each of
// value 2. Maybe constructing a method to determine where to place the initial
// tiles. We have not implemented placing the first two random tiles yes. 

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

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
        @param size -- BoardSize enumerated type representing the size of the game board
     */
    public Board(BoardSize size) {
        // Initializes the score to 0 when the Board is created
        this.score = 0;

        // Uses the given enum in order to represent the size of the board as an int
        int sizeAsInt = 4 + 2 * (size.ordinal());

        // Constructs the actual board, filling it with empty Optional<Tile> objects,
        // since we want the board to be effectively empty at the beginning of the game
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

        @return a nested array of Optional<Tile> objects representing the board for the game
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
        The score associated with the board is also then updated appropriately. Note that this
        method also returns an int representing the total number of shifts/combinations that
        occurred during the run of the method, since this is used in order to determine whether
        or not randomized tiles need to be added to the board after the shift.

        @return an int representing the total number of shifts/combinations that occurred during
                the run of this method
     */
    public int shiftRight() {
        // Initializes an int for keeping track of the total number of shifts/combinations
        int numMoves = 0;

        // Iterates through all of the rows within the board
        for (int i = 0; i < boardGrid.length; i++) {
            // Gets the current row
            Optional<Tile>[] curRow = boardGrid[i];

            // Combines everything horizontally wihtin the row
            numMoves += combineAllHorizontalRight(curRow);

            // Shifts everything to the right after combination
            numMoves += shiftAllRight(curRow);
        }

        // Returns the total number of shifts/combinations
        return numMoves;
    }

    /*
        Private helper method, which takes in an Optional<Tile> array and which moves all
        Tiles within the array as far to the right as they can go. Note that this method also
        returns the number of shifts that were actually made, since this is used to help determine
        when new, randomized Tiles actually need to be added to the board

        @pre row != null
        @post shifts all Tile objects in each row as far to the right as they can go
        @param row -- Optional<Tile>[] object representing a row of the game board
        @return an int representing the number of Tiles that were actually shifted
     */
    private int shiftAllRight(Optional<Tile>[] row) {
        // Initializes an int representing the number of Tiles that were actually shifted
        int numShifted = 0;

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

                // We also increment numShifted, since we actually shifted a Tile
                numShifted++;
            }

            // Decrements i so we can continue iterating
            i--;
        }

        // Returns the value representing the number of tiles that were shifted
        return numShifted;
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within
        our board, and which combines all tiles that are adjacent to one another with the same
        value. Note that this method also counts the number of combinations that were actually made,
        and returns this value, since this will allow us to more easily determine when we need to
        add new Tile objects to the board. Also, the method will combine Tiles from right to left,
        since it is assumed that this method will be called upon a right shift specifically, and since
        this matched gameplay within the actual 2048 game. No Tiles will be able to merge more than
        once.

        @pre row != null
        @post makes all possible combinations of adjacent tiles in the given row of the board, though
            with no Tiles being combined more than once.
        @param row -- Optional<Tile>[] object that represents a particular row of the game board
        @return an int representing the number of combinations that were actually made 
     */
    private int combineAllHorizontalRight(Optional<Tile>[] row) {
        // Initializes the int that counts the number of combinations that are actually made
        int numCombined = 0;

        // We then iterate until we hit the end of the row, moving backwards through the row, 
        // according to the direction of the shift
        int i = row.length - 1;
        while (i >= 0) {
            // First, we iterate until we find a Tile (or we hit the end of the row)
            while (i >= 0 && row[i].isPresent() == false) {
                i--;
            }

            // If we find no tiles, then we break, since there's nothing left to do
            if (i < 0) {
                break;
            }

            // Otherwise, we get the current Tile, then look for another Tile
            Tile curTile = row[i].get();
            i--;
            while (i >= 0 && row[i].isPresent() == false) {
                i--;
            }

            // Again, if we find no other Tile, then there's nothing to combine, so we break
            if (i < 0) {
                break;
            }

            // If we do find another Tile, then we check to see whether its value is the same as
            // our first Tile
            Tile compareTile = row[i].get();
            if (curTile.equals(compareTile)) {
                // If these Tiles do have the same value, then we combine them, merging them into 
                // a single Tile, and leaving the spot for compareTile empty
                row[i] = Optional.empty();
                curTile.multiplyValByTwo();

                // The combination count is then incremented
                numCombined++;

                // To continue iterating, we then decrement i by 1, since we know that the current
                // spot must necessarily be empty
                i--;
            } 

            // If these two Tiles cannot be merged, then we leave our index i where it is and continue
            // on to the next iteration of the loop, so that we can see whether the second Tile we found
            // can be merged with any other adjacent tiles
        }
        
        // The combination count is then returned
        return numCombined;
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within
        our board, and which check to see whether there are any remaining Tile objects which
        have the same value and are next to one another in the grid, meaning that they should/
        could be combined. Returns true if there are any combinations left, and false otherwise.
        This method will be used to check for win conditions within the game board.

        @pre row != null
        @post checks for any combinations in the given row, returning true if there are any, false
                otherwise
        @param row -- Optional<Tile>[] object representing a particular row in the board
        @return true if there are any possible combinations in the given row, false otherwise
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
        appropriately according to the combinations made with these shifts. Note that this method also
        returns an int representing the total number of shifts/combinations that occurred during the
        run of the method, since this is used to determine whether we need to add in any randomized tiles

        @return an int representing the total number of shifts and combinations that occurred
     */
    public int shiftLeft() {
        // Initializes the count for the total number of shifts/combinations
        int numMoves = 0;

        // Iterates through all of the rows within boardGrid
        for (int i = 0; i < boardGrid.length; i++) {
            // Gets the current row
            Optional<Tile>[] curRow = boardGrid[i];

            // Combines everything horizontally that can be combined
            numMoves += combineAllHorizontalLeft(curRow);

            // Shifts everything in the row to the left once this is done
            numMoves += shiftAllLeft(curRow);
        }

        // Returns the total number of shifts/combinations
        return numMoves;
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within our 
        board, and which combines all Tiles with the same values which are next to one another. Note 
        that since this combination method is particularly for a left shift, the combinations will be
        made from left to right. Also, according to the actual gameplay of 2048, upon a single shift, 
        no Tiles can undergo more than one merge. This method will also count the number of combinations
        that are actually made, since this functionality will be used to determine when we should add
        randomized Tiles into the board after a shift.

        @pre row != null
        @post combines all adjacent Tiles in the row with the same value, with no Tiles being merged
            more than once.
        @param row -- an Optional<Tile>[] array representing a particular row of the game board
        @return an int representing the number of combinations that were actually made
     */
    private int combineAllHorizontalLeft(Optional<Tile>[] row) {
        // Initializes the counter for the number of combinations
        int numCombined = 0;

        // Iterates through the full row, specifically from left to right
        int i = 0;
        while (i < row.length) {
            // Iterates until we either find a Tile or we hit the end of the row
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }

            // If we find no other Tiles, then we break, since there are no combinations to make
            if (i >= row.length) {
                break;
            }

            // Otherwise, we get the Tile at the current index, then iterate until we either reach
            // the end of the row or we find another Tile
            Tile curTile = row[i].get();
            i++;
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }

            // If we find no other Tile, then we break, since there are no combinations left to make
            if (i >= row.length) {
                break;
            }

            // Otherwise, we get the Tile at the current spot, and see if it has the same value as our
            // first Tile that we found
            Tile compareTile = row[i].get();
            if (curTile.equals(compareTile)) {
                // If they do have the same value, then we merge them into a single Tile, leaving the 
                // original space of compareTile empty
                row[i] = Optional.empty();
                curTile.multiplyValByTwo();

                // We increment the number of combinations actually made by 1
                numCombined++;

                // Then, we increment i before the next iteration, since we know that there's nothing at
                // the current position in the array
                i++;
            }

            // If these Tiles do not have the same value, then we simply do nothing, since our index i 
            // is already in an appropriate position for the next iteration of the while loop
        }

        // Finally, once we're done iterating, we return the number of combinations that were actually made
        return numCombined;
    }

    /*
        Private helper method, which takes in an Optional<Tile> array representing a row within our 
        board, and which shifts all of the Tile objects within the board as far to the left as they
        can go. Note that this method also returns an int representing the number of shifts that were
        actually made, since this will be used to determine when we actually need to place randomized
        Tiles in the board

        @pre row != null
        @post shifts all of the Tiles in the board as far to the left as they can go
        @param row -- Optional<Tile>[] object representing a particular row of the board
        @return an int representing the number of shifts that were actually made
     */
    private int shiftAllLeft(Optional<Tile>[] row) {
        // Initializes the count for the number of shifts that were made
        int numShifts = 0;

        // Iterates until we run out of indices
        int i = 0;
        while (i < row.length) {
            // Finds the first Tile starting from the current index
            while (i < row.length && row[i].isPresent() == false) {
                i++;
            }

            // If there are no more Tiles left, then we simply return, since our job is done
            if (i >= row.length) {
                return numShifts;
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

                // If we actually shift, then we increment numShifted
                numShifts++;
            }

            // Increments i for the next iteration
            i++;
        }

        // Returns the count for the number of shifted Tiles
        return numShifts;
    }

    /*
        Method which can be used to update the Board in a manner appropriate for an up-arrow click
        within the 2048 game. This method looks at each column within boardGrid, combines all tiles that
        would be combined with an up shift, and moves all of the tiles as far upwards within the
        board as they can go once this is done. The score associated with the board is then updated
        appropriately according to the combinations made with these shifts. Note that this method also
        returns an int representing the total number of shifts/combinations that were made, since this
        will be used to determine whether or not we need to add in randomized tiles after this move

        @return an int representing the total number of shifts and combinations that occurred
     */
    public int shiftUp() {
        // Initializes an int keeping track of the total number of shifts/combinations that occur
        int numMoves = 0;

        // Iterates through all of the columns within boardGrid
        int curCol = 0;
        while (curCol < boardGrid.length) {
            // Makes all of the necessary combinations for an up shift, combining Tiles as necessary
            // within the current column
            numMoves += combineAllVerticalUp(curCol);

            // Shifts all Tiles as far up as they can go within the board after any potential combinations
            numMoves += shiftAllUp(curCol);

            // Continues iterating on
            curCol++;
        }

        // Returns the total number of shifts/combinations
        return numMoves;
    }

    /*
        Private helper method which can be used to shift all of the Tile objects in a particular
        column of the board as far up as they can go. This method takes in an int representing the
        particular column that we're interested in, and it does the shifting on the desired column
        within the boardGrid instance variable. Furthermore, this method returns an int representing
        the number of shifts that were actually made, since this will be used to determine whether or 
        not we actually need to add in new randomized Tiles to the board

        @pre col >= 0 && col < boardGrid.length
        @post shifts all of the Tiles in the current column as far up as possible
        @param col -- int representing the index of the column that we want to do the shifting in
        @return an int representing the number of shifts that were actually made
     */
    private int shiftAllUp(int col) {
        // Initializes the int representing the number of actual shifts that were made
        int numShifts = 0;

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
                return numShifts;
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

                // Since we actually shifted something, we increment numShifts
                numShifts++;
            }

            // Finally, we increment i and continue iterating on
            i++;
        }

        // Returns the number of shifts that were actually made
        return numShifts;
    }

    /*
        Private helper method which can be used to combine all adjacent tiles wiht the same values in a 
        particular column of boardGrid. Takes in an int representing the column of boardGrid that we're 
        interested in, and makes all of the combinations that should be made, with the method going from
        the top of the column to the bottom, according to the behavior of an up-shift in 2048. Note that,
        according to the rules for 2048, no Tile can be merged more than once within a single up-shift. The
        method also returns an int representing the number of combinations that were actually made, since
        this will be used to determine when we actually need to add randomized Tiles into the Board.

        @pre col >= 0 && col < boardGrid.length
        @post makes all combinations of adjacent Tiles with the same values that should be made for an
            up-shift within the given column
        @param col -- an int representing the index of the column we're interested in
        @return an int representing the number of combinations that were actually made
     */
    private int combineAllVerticalUp(int col) {
        // Initializes an int representing the number of combinations that were actually made
        int numCombined = 0;

        // We then iterate through the entire column to merge the adjacent Tiles that should be merged,
        // going from top to bottom, considering the behavior for an upward shift
        int i = 0;
        while (i < boardGrid.length) {
            // First, we iterate until we either find a Tile or we hit the end of the column
            while (i < boardGrid.length && boardGrid[i][col].isPresent() == false) {
                i++;
            }

            // If there are no Tiles, then we break, since there are no combinations to be made
            if (i >= boardGrid.length) {
                break;
            }

            // If we do find a Tile, then we get this Tile, then iterate from the next position until
            // we either find another Tile or hit the end of the column
            Tile curTile = boardGrid[i][col].get();
            i++;
            while (i < boardGrid.length && boardGrid[i][col].isPresent() == false) {
                i++;
            }

            // If we cannot find a second Tile, then we again break, since there's nothing to merge
            if (i >= boardGrid.length) {
                break;
            }

            // Otherwise, we check to see whether the value of the current Tile is equal to the value 
            // of the first Tile
            Tile compareTile = boardGrid[i][col].get();
            if (curTile.equals(compareTile)) {
                // If they do have the same value, then we merge them, leaving the space associated with
                // the second Tile we found empty
                boardGrid[i][col] = Optional.empty();
                curTile.multiplyValByTwo();

                // The total number of combinations is incremented
                numCombined++;

                // We then increment i, since we know that the current position is empty
                i++;
            }

            // If the values of these two Tiles are not the same, then we do nothing, since the index
            // i is already in the appropriate position for the next iteration of the loop
        }

        // Finally, once we're done iterating, we return the total number of combinations made
        return numCombined;
    }

    /*
        Private helper method which can be used in order to determine whether there are any remaining
        combinations to be made within a particular column of the board. Takes in an int representing the
        index of the column we're interested in, and returns true if there are any combinations, and 
        false otherwise. This method will then be used to tell whether or not the game is over within the
        isGameOver method.

        @pre col >= 0 && col < boardGrid.length
        @post checks to see if there are any combinations in the current column
        @param col -- int representing the index of the column of interest in which we want to check for
                    combinations
        @return true if there are vertical combinations in the current column, false otherwise
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
        appropriately according to the combinations made with these shifts. Note that this method also
        returns an int representing the total number of shifts and combinations that occurred, since this
        information will be used to determine whether or not any new randomized tiles need to be added in

        @return an int representing the total number of shifts and combinations that occurred
     */
    public int shiftDown() {
        // Initializes an int that will count the total number of shifts/combinations that occur
        int numMoves = 0;

        // Iterates through all of the columns in boardGrid
        int curCol = 0;
        while (curCol < boardGrid.length) {
            // Makes all of the possible combinations within this column, updating the score according
            // to any combinations made
            numMoves += combineAllVerticalDown(curCol);

            // Shifts everything in the current column as far down as possible, after any potential
            // combinations are made
            numMoves += shiftAllDown(curCol);

            // Increments curCol to continue iterating
            curCol++;
        }

        // Returns the total number of moves/combinations
        return numMoves;
    }

    /*
        Private helper method which can be used to combine all adjacent tiles wiht the same values in a 
        particular column of boardGrid. Takes in an int representing the column of boardGrid that we're 
        interested in, and makes all of the combinations that should be made, with the method going from
        the bottom of the column to the top, according to the behavior of a down-shift in 2048. Note that,
        according to the rules for 2048, no Tile can be merged more than once within a single up-shift. The
        method also returns an int representing the number of combinations that were actually made, since
        this will be used to determine when we actually need to add randomized Tiles into the Board.

        @pre col >= 0 && col < boardGrid.length
        @post makes all combinations of adjacent Tiles with the same values that should be made for an
            down-shift within the given column
        @param col -- an int representing the index of the column we're interested in
        @return an int representing the number of combinations that were actually made
     */
    private int combineAllVerticalDown(int col) {
        // Initializes the integer that will be used to count the number of combinations made
        int numCombined = 0;

        // Iterates through the column from bottom to top
        int i = boardGrid.length - 1;
        while (i >= 0) {
            // First, we iterate until we find the next Tile object in this column or until we hit the end of
            // the column
            while (i >= 0 && boardGrid[i][col].isPresent() == false) {
                i--;
            }

            // If we find no Tile object, then we simply break, since there are no combinations left
            if (i < 0) {
                break;
            }

            // If we do find a Tile, then we get this Tile and continue iterating until we either find
            // another Tile or we hit the end of the column
            Tile curTile = boardGrid[i][col].get();
            i--;
            while (i >= 0 && boardGrid[i][col].isPresent() == false) {
                i--;
            }

            // If we find no second Tile, then we again break, since there are no combinations to make
            if (i < 0) {
                break;
            }

            // Otherwise, we check to see whether the current Tile has the same value as the first
            Tile compareTile = boardGrid[i][col].get();
            if (curTile.equals(compareTile)) {
                // If these Tiles do have the same value, then we merge them into a single Tile, and replace
                // the spot of compareTile with an empty Optional<Tile> object
                boardGrid[i][col] = Optional.empty();
                curTile.multiplyValByTwo();

                // The count for the number of combinations made is incremented
                numCombined++;

                // The index is also decremented, since we know that the current spot is empty
                i--;
            }

            // If the values of these Tiles are not equal, then we do nothing, since our index i is already in
            // an appropriate position for the next iteration of the loop
        }

        // Finally, we return the count for the number of combinations that were actually made
        return numCombined;
    }

    /*
        Private helper method which can be used to shift all of the Tile objects in a particular
        column of the board as far down as they can go. This method takes in an int representing the
        particular column that we're interested in, and it does the shifting on the desired column
        within the boardGrid instance variable. Note that this method also returns an int representing
        the number of shifts that were actually made, since this will be used to determine when we 
        actually need to add in randomized tiles to the board

        @pre col >= 0 && col < boardGrid.length
        @post shifts all of the Tiles in the given column as far down as possible
        @param col -- int representing the column in which we want to do our shifting
        @return an int representing the number of shifts that were actually made
     */
    private int shiftAllDown(int col) {
        // Initializes the counter for the number of shifts made
        int numShifts = 0;

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
                return numShifts;
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

                // We also increment the number of shifts, since a shift actually occurred
                numShifts++;
            }

            // We then decrement i and continue iterating on
            i--;
        }

        // Finally, the number of shifts is returned
        return numShifts;
    }

    /*
        Public method which can be used to check whether the game is over. Returns a value of the HasWon
        enumerated type, which will be WON if the player has won, LOST if the player has lost, and NOT_DONE
        if the game is not yet over

        @return a HasWon enumerated type to indicate the win/lose/not done status of the game
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

        @return 0 if there are no Tiles in the board; otherwise, returns an int representing the highest
            value associated with a Tile in the board
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
        Private helper method which can be utilized in order to count the number of empty tiles
        currently present within the board. Takes in no parameters, and returns an int representing
        this number.

        @return an int representing the number of empty tiles current present within the board
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

    /*
        Private helper method which can be used to place a Tile object with a specified value in
        a particular spot on the board. Note that, based on the constructor for the Tile class, it
        is a precondition that value is equal to either 2 or 4. 

        @pre row >= 0 && row < boardGrid.length && col >= 0 && col < boardGrid.length && (value == 2^x, where x is an 
            int with x>0)
        @post a new Tile object with the specified value is placed at the specified location within the
            board.
    */
   public void setTile(int row, int col, int value) {
	    Tile tileToPlace = new Tile(value);
	    Optional<Tile> optionalTileToPlace = Optional.of(tileToPlace);
        boardGrid[row][col] = optionalTileToPlace;
   }

    /*
        Private helper method to generate a random value for a tile. The returned value will either be 2
        or 4, with a 70% probability that the tile value will be 2, and a 30% probability that it will be
        4, based on the probabilities associated with tile generation in the 2048 game.

        @return an int which is either 2 or 4, randomly selected with the probabilities described above
    */
   private int getRandValue() {
        int tileVal;
	  
            // Determine what value the new tile will be using random
            Random rand = new Random();
            int randInt = rand.nextInt(10);
            // 70% probability that the tile value will be 2
            // 30% probability that the tile value with be 4
            if (randInt <= 7) {
                tileVal = 2;
            }
            else {
                tileVal = 4;
            }
            return tileVal;
   }

    /*
        Private method which can be used to generate a random, empty position on the board in which a
        Tile can be placed. Note that this method assumes that there is at least one empty spot currently
        present within the board, and that the method will generate randomized positions until it finds
        an empty one, which will then be returned.

        @pre this.countNumEmpty() != 0
        @post generates a random, empty position on the board in which a new Tile object can be placed
        @return an int[] array of size two containing the [row, col] of the generated randomized position
    */
   private int[] getRandLocation() {
        // Determine the position to place the Tile by randomly generating positions on the board until
        // finding one that is empty
        Random rand = new Random();
            
        // Generate intial random location
        int row = rand.nextInt(boardGrid.length);
        int col = rand.nextInt(boardGrid.length);
        // If the board position is not empty, generate a new random position
        while (boardGrid[row][col].isPresent() == true) {
            row = rand.nextInt(boardGrid.length);
            col = rand.nextInt(boardGrid.length);
        }

        int[] pos = {row, col};

        return pos;
   }

    /*
        Public method which can be used to, at the start of the game, place two Tile objects
        in random spots on the board. Note that it is assumed that this method will be used by
        the GUI directly after the initialization of the board, so it is assumed that all spots 
        in the board are empty when this method is called. This method is also public and separate
        from the constructor itself in order to better allow for functionalities within the game
        to be tested via JUnit.

        @pre the board contains only empty Tiles
        @post two new Tiles, with a value of either 2 or 4, are placed into random, different
            positions on the board
     */
    public void placeTilesStartGame() {
        // Find pos and val of first tile
        int [] pos1 = getRandLocation();
        int val1 = getRandValue();
        // Place first tile
        setTile(pos1[0], pos1[1], val1);

        // Find pos and val of second tile
        int [] pos2 = getRandLocation();
        int val2 = getRandValue();
        // Place second tile
        setTile(pos2[0], pos2[1], val2);
    }

    /*
        Public method which can be used after a shift is made, and which places a Tile, with
        a value of either 2 or 4, in a random (empty) position on the board. If the board contains
        no empty tiles when this method is called, then the method will not do anything. 
        The number of shifts/combinations that took place from the previous move is passed in as
        an argument.
     */
    public void placeRandomTile(int numShiftCombos) {
        if (countNumEmpty() > 0 && numShiftCombos > 0) { 
            int [] pos = getRandLocation();
            int val = getRandValue();
            setTile(pos[0], pos[1], val);
        }
    }

}
