/*
File: Tile.java
Authors: Micaila Marcelle (micailamarcelle), Elise Bushra (ebushra), Cate Yip (cyip);
Course: CSC 335
Purpose: Implements the actual tiles within the 2048 board. These will have an 
associated value, and methods for more easily combining tiles together.
 */

public class Tile {
    // Private instance variables for the Tile class, which include an int representing
    // the value associated with the tile, and a TileColor enumerated type representing 
    // the color of the tile
    private int value;
    private TileColor color;

    /*
        Constructor for the Tile class. This takes in an int representing the value for the
        tile, and a TileColor enumerated type representing the color of the tile, and it
        constructs a new Tile object with these values

        @pre value == 2 || value == 4
        @post constructs a new Tile object with the given values
        @param value -- an int representing the desired value of the new Tile
     */
    public Tile(int value) {
        this.value = value;
        this.color = tileColor(value);

    }

    /*
        Copy constructor for the Tile class, which takes in a Tile object, and which constructs 
        a new Tile object with the same values for both of its fields. Note that, because both
        of the fields of the Tile class are immutable, there is no danger of escaping references
        
        @pre givenTile != null
        @post constructs a new Tile object that represents a copy of the given Tile object
        @param givenTile -- a Tile object whose value and color will be copied into a new Tile
     */
    public Tile(Tile givenTile) {
        this.value = givenTile.value;
        this.color = givenTile.color;
    }

    /*
        Getter for the value associated with a Tile

        @return an int representing the value of a particular Tile
     */
    public int getValue() {
        return this.value;
    }

    /*
        Getter for the color of a Tile

        @return a TileColor enumerated type representing the color of a particular Tile
     */
    public TileColor getTileColor() {
        return this.color;
    }

    /*
        Method for updating the value of a Tile. This takes in no inputs, and produces no
        outputs. Considering the functionality of the 2048 game, this method simply multiplies
        the value associated with the Tile by two, since this is the only way in which we may
        need to update the value of a Tile within the game. We also automatically update the 
        color of the tile when we multiply its value by two.
     */
    public void multiplyValByTwo() {
        this.value *= 2;
        this.updateToNextColor();
    }

    /*
        Method for updating the color of a Tile. Within our game, as Tiles increase in value,
        they cycle through a set of four colors. So, when we update the color of a Tile, we
        simply set the color to be the next ordinal value in the TileColor enumerated type,
        wrapping around if necessary.
     */
    private void updateToNextColor() {
        if (this.color.equals(TileColor.PURPLE)) {
            this.color = TileColor.GRAY;
        } else {
            this.color = TileColor.values()[this.color.ordinal() + 1];
        }
    }

    /*
        Method for comparing one Tile object to another in the context of the 2048 game. Returns
        true if both Tile objects have the same associated value, and false otherwise

        @pre other != null
        @post determines whether the current Tile object has the same value as other
        @param other -- a Tile object representing the Tile we want to compare the current Tile to
        @return true if this Tile has the same value as other, false otherwise
     */
    boolean equals(Tile other) {
        return this.value == other.value;
    }

    /*
    * 	Private helper method to determine the tile color that should be assigned for a given
    * 	value
    *   NOTE: Based on the game, only tiles of value 2 or 4 will ever be placed. So we only need to 
    *         check if the value of the tile is a 2 or a 4.
    *
    *   @pre value = 2^x for some integer x > 0
    *   @post returns a TileColor enumerated type representing the color associated with the given value
    *   @param value -- an int representing the value of the Tile whose color we're determining
    *   @return a TileColor enumerated type representing the color associated with the given value
    */
    private TileColor tileColor(int value) {
    	// Initialize a default color to the variable
    	TileColor color;
        if (value % 16 == 0) {
            color = TileColor.PURPLE;
        } else if (value % 8 == 0){
            color = TileColor.DARK_BLUE;
        } else if (value % 4 == 0){
            color = TileColor.LIGHT_BLUE;
        } else {
            color = TileColor.GRAY;
        }
		return color;
	}
}
