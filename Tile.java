/*
File: Tile.java
Authors: N/A (for now)
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

        @pre value % 2 == 0 && value > 0 && color != null
        @post constructs a new Tile object with the given values
     */
    public Tile(int value, TileColor color) {
        this.value = value;
        this.color = color;
    }

    /*
        Copy constructor for the Tile class, which takes in a Tile object, and which constructs 
        a new Tile object with the same values for both of its fields. Note that, because both
        of the fields of the Tile class are immutable, there is no danger of escaping references
        
        @pre givenTile != null
        @post constructs a new Tile object that represents a copy of the given Tile object
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
        need to update the value of a Tile within the game.
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
     */
    boolean equals(Tile other) {
        return this.value == other.value;
    }
}
