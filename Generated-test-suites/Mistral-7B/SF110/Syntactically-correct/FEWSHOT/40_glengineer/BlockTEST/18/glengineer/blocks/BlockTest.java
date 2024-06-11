package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 2, 2);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(2, block.getX2());
        assertEquals(2, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(2, 2);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.getX(), block.getX1());
        assertEquals(p1.getY(), block.getY1());
        assertEquals(p2.getX(), block.getX2());
        assertEquals(p2.getY(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructionWithSchemeOnly() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockThrowsExceptionWithImproperDefinition() {
        Scheme scheme = new Scheme(2, 2);
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, -1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, -1, 2));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 2, -1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 3, 3));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 2, 4));
    }

    @Test
    public void testBlockSubblockWithInvalidCoordinates() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> block.subblock(-1, -1, -1, -1));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(2, 2, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 2, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 1, 1, 2));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 1, 1, 1));
    }

    @Test
    public void testBlockSubblockWithValidCoordinates() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        Block subblock = block.subblock(0, 0, 1, 1);
        assertEquals(0, subblock.getX1());
        assertEquals(0, subblock.getY1());
        assertEquals(1, subblock.getX2());
        assertEquals(1, subblock.getY2());
        assertEquals(scheme, subblock.getScheme());
    }

    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        block.scheme.setTextAt(0, 0, "A");
        char expected = 'A';
        assertEquals(expected, block.charAt(0, 0));
    }

    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        block.scheme.setTextAt(0, 0, "Hello, World!");
        String expected = "Hello, World!";
        assertEquals(expected, block.textAt(0, 1, 0));
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        block.scheme.setTextAt(0, 0, "Hello World");
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockDoesNotContainOnlyOneWord() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        block.scheme.setTextAt(0, 0, "Hello World1");
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(3, 3);
        Block block = new Block(scheme);
        block.scheme.setTextAt(0, 0, "ABC");
        block.scheme.setTextAt(0, 1, "DEF");
        block.scheme.setTextAt(0, 2, "GHI");
        List<Block> expected = Arrays.asList(
                new Block(scheme, new CharPosition(0, 0), new CharPosition(1, 0)),
                new Block(scheme, new CharPosition(1, 0), new CharPosition(2, 0)),
                new Block(scheme, new CharPosition(0, 1), new CharPosition(2, 1)),
                new Block(scheme, new CharPosition(0, 2), new CharPosition(2, 2))
        );
        List<Block> actual = block.extractSequentialSubblocks();
        assertEquals(expected, actual);
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(3, 3);
        Block block = new Block(scheme);
        block.scheme.setTextAt(0, 0, "ABC");
        block.scheme.setTextAt(0, 1, "DEF");
        block.scheme.setTextAt(0, 2, "GHI");
        List<Block> expected = Arrays.asList(
                new Block(scheme, new CharPosition(0, 0), new CharPosition(3, 0)),
                new Block(scheme, new CharPosition(0, 1), new CharPosition(3, 1)),
                new Block(scheme, new CharPosition(0, 2), new CharPosition(3, 2))
        );
        List<Block> actual = block.extractParallelSubblocks();
        assertEquals(expected, actual);
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        String expected = "Block(0, 0):\n" +
                "# # #\n" +
                "# A # #\n" +
                "# # #\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(2, 2);
        Block block = new Block(scheme, 1, 1, 1, 1);
        block.scheme.setTextAt(0, 0, "Hello, World!");
        String expected = "Hello, World!";
        assertEquals(expected, block.getUpperLine());
    }
}