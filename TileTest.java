/*
File: TileTest.java
Authors: Elise Bushra (ebushra), Micaila Marcelle (micailamarcelle)
Course: CSC 335
Purpose: Provides JUnit tests for the functionalities of the Tile class
 */

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TileTest {
    @Test
    public void testMultiplyByValSmall() {
        Tile a = new Tile(2);
        assertTrue(a.getTileColor() == TileColor.YELLOW);
        assertTrue(a.getValue() == 2);

        a.multiplyValByTwo();
        assertTrue(a.getValue() == 4);
        assertTrue(a.getTileColor() == TileColor.CYAN);

        a.multiplyValByTwo();
        assertTrue(a.getValue() == 8);
        assertTrue(a.getTileColor() == TileColor.GREEN);

        a.multiplyValByTwo();
        assertTrue(a.getValue() == 16);
        assertTrue(a.getTileColor() == TileColor.BLUE);

        a.multiplyValByTwo();
        assertTrue(a.getValue() == 32);
        assertTrue(a.getTileColor() == TileColor.YELLOW);
    }

    @Test
    public void testMultiplyByValLarge() {
        Tile a = new Tile(32);
        assertEquals(a.getValue(), 32);
        assertEquals(a.getTileColor(), TileColor.YELLOW);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 64);
        assertEquals(a.getTileColor(), TileColor.CYAN);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 128);
        assertEquals(a.getTileColor(), TileColor.GREEN);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 256);
        assertEquals(a.getTileColor(), TileColor.BLUE);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 512);
        assertEquals(a.getTileColor(), TileColor.YELLOW);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 1024);
        assertEquals(a.getTileColor(), TileColor.CYAN);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 2048);
        assertEquals(a.getTileColor(), TileColor.GREEN);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 4096);
        assertEquals(a.getTileColor(), TileColor.BLUE);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 8192);
        assertEquals(a.getTileColor(), TileColor.YELLOW);

        a.multiplyValByTwo();
        assertEquals(a.getValue(), 16384);
        assertEquals(a.getTileColor(), TileColor.CYAN);
    }

    @Test
    public void testEqualsTile() {
        Tile a = new Tile(4);
        a.multiplyValByTwo();
        a.multiplyValByTwo();
        a.multiplyValByTwo();
        Tile b = new Tile(32);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));

        Tile c = new Tile(2);
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        assertTrue(c.equals(a));
        assertTrue(a.equals(c));
        assertTrue(c.equals(b));
        assertTrue(b.equals(c));
    }

    @Test
    public void testEqualsTileNotEqual() {
        Tile a = new Tile(32);
        a.multiplyValByTwo();

        Tile b = new Tile(32);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));

        Tile c = new Tile(32);
        c.multiplyValByTwo();
        c.multiplyValByTwo();
        assertFalse(a.equals(c));
        assertFalse(c.equals(a));
        assertFalse(b.equals(c));
        assertFalse(c.equals(b));
    }

    @Test
    public void testTileColorSmall() {
        Tile a = new Tile(2);
        Tile b = new Tile(4);
        Tile c = new Tile(8);
        Tile d = new Tile(16);
        assertEquals(a.getTileColor(), TileColor.YELLOW);
        assertEquals(b.getTileColor(), TileColor.CYAN);
        assertEquals(c.getTileColor(), TileColor.GREEN);
        assertEquals(d.getTileColor(), TileColor.BLUE);
    }

    @Test
    public void testTileColorUpTo2048() {
        Tile a = new Tile(32);
        assertEquals(a.getTileColor(), TileColor.YELLOW);

        Tile b = new Tile(64);
        assertEquals(b.getTileColor(), TileColor.CYAN);

        Tile c = new Tile(128);
        assertEquals(c.getTileColor(), TileColor.GREEN);

        Tile d = new Tile(256);
        assertEquals(d.getTileColor(), TileColor.BLUE);

        Tile e = new Tile(512);
        assertEquals(e.getTileColor(), TileColor.YELLOW);

        Tile f = new Tile(1028);
        assertEquals(f.getTileColor(), TileColor.CYAN);

        Tile g = new Tile(2048);
        assertEquals(g.getTileColor(), TileColor.GREEN);
    }

    @Test
    public void testTileColorPast2048() {
        Tile a = new Tile(4096);
        assertEquals(a.getTileColor(), TileColor.BLUE);

        Tile b = new Tile(8192);
        assertEquals(b.getTileColor(), TileColor.YELLOW);

        Tile c = new Tile(16384);
        assertEquals(c.getTileColor(), TileColor.CYAN);

        Tile d = new Tile(32768);
        assertEquals(d.getTileColor(), TileColor.GREEN);

        Tile e = new Tile(65536);
        assertEquals(e.getTileColor(), TileColor.BLUE);
    }

    @Test
    public void testCopyConstructor() {
        Tile a = new Tile(4);
        Tile copyA = new Tile(a);
        assertEquals(a.getValue(), copyA.getValue());
        assertEquals(a.getTileColor(), copyA.getTileColor());
        assertFalse(a == copyA);

        Tile b = new Tile(16);
        Tile copyB = new Tile(b);
        assertEquals(b.getValue(), copyB.getValue());
        assertEquals(b.getTileColor(), copyB.getTileColor());
        assertFalse(b == copyB);

        b.multiplyValByTwo();
        assertNotEquals(b.getValue(), copyB.getValue());
        assertNotEquals(b.getTileColor(), copyB.getTileColor());

        Tile c = new Tile(2048);
        Tile copyC = new Tile(c);
        assertEquals(c.getValue(), copyC.getValue());
        assertEquals(c.getTileColor(), copyC.getTileColor());
        assertFalse(c == copyC);
    }
}