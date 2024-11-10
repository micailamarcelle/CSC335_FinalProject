/*
File: Tile.java
Authors: N/A (for now)
Course: CSC 335
Purpose: Implements the actual tiles within the 2048 board. These will have an 
associated value, and methods for more easily combining tiles together.
 */

public class Tile {
    // Private instance variables for the Tile class
    private int value;
    private TileColor color;

    // Constructor for the Tile class
    public Tile(int value, TileColor color) {
        this.value = value;
        this.color = color;
    }

    // Copy constructor for the Tile class

    // Getter for the value of the tile
    public int getValue() {
        return this.value;
    }

    // Getter for the color of the tile
    public TileColor getTileColor() {
        return this.color;
    }

    // Method for updating the value of a Tile. Considering the functionality of the 2048
    // game, this method simply multiplies the value of the tile by two, since this is the
    // context in which we might need to update the value of the tile
    public void multiplyValByTwo() {
        this.value *= 2;
    }

    // Updates the color of the tile. Considering the functionality of the tiles, this
    // simply gets the color of the next ordinal value in the enum, wrapping around if
    // necessary
    public void updateToNextColor() {
        if (this.color.equals(TileColor.PURPLE)) {
            this.color = TileColor.GRAY;
        } else {
            this.color = TileColor.values()[this.color.ordinal() + 1];
        }
    }

    // Method for comparing one tile to another, returning true if this Tile has the same
    // value as the given tile, and returing false otherwise
    boolean equals(Tile other) {
        return this.value == other.value;
    }
}
