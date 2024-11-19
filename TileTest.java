/*
File: TileTest.java
Authors: Elise Bushra (ebushra)
Course: CSC 335
Purpose: Provides JUnit tests for the functionalities of the Tile class
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TileTest {
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

        Tile c = new Tile(2, TileColor.GRAY);
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        assertEquals(c, a);
    }
}
