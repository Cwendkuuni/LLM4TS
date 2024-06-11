package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithBounds() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(scheme, block.scheme);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
    }

    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(scheme, block.scheme);
        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
    }

    @Test
    public void testBlockConstructorWithSchemeOnly() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(scheme, block.scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithWrongBounds() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 1, 6, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithWrongPositions() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, new CharPosition(5, 5), new CharPosition(0, 0));
    }

    @Test
    public void testSubblockWithValidSubblockBounds() {
        Scheme scheme = new Scheme(5, 5);
        Block parentBlock = new Block(scheme, 1, 1, 3, 3);
        Block subblock = parentBlock.subblock(1, 1, 1, 1);
        assertEquals(scheme, subblock.scheme);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithWrongSubblockBounds() {
        Scheme scheme = new Scheme(5, 5);
        Block parentBlock = new Block(scheme, 1, 1, 3, 3);
        parentBlock.subblock(4, 4, 1, 1);
    }

    @Test
    public void testGetCharAt() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(' ', block.charAt(0, 0));
        assertEquals('X', block.charAt(1, 1));
        assertEquals('Y', block.charAt(2, 1));
        assertEquals('Z', block.charAt(1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharAtOutOfBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.charAt(-1, -1);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("XYZ", block.textAt(1, 3, 1));
        assertEquals("XY", block.textAt(1, 2, 1));
        assertEquals("X", block.textAt(1, 1, 1));
        assertEquals("", block.textAt(1, 0, 1));
        assertEquals("", block.textAt(0, 0, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtOutOfBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.textAt(0, 0, 1);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'W');
        block.setCharAt(1, 2, 'o');
        block.setCharAt(1, 3, 'r');
        block.setCharAt(2, 1, 'd');
        block.setCharAt(2, 2, ' ');
        block.setCharAt(2, 3, '.');
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithMultiLineWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 4);
        block.setCharAt(1, 1, 'A');
        block.setCharAt(1, 2, ' ');
        block.setCharAt(1, 3, 'B');
        block.setCharAt(2, 1, 'C');
        block.setCharAt(2, 2, ' ');
        block.setCharAt(2, 3, 'D');
        block.setCharAt(3, 1, 'E');
        block.setCharAt(3, 2, ' ');
        block.setCharAt(3, 3, 'F');
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 1, 1);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithEmptyString() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 1, 1);
        block.setCharAt(0, 0, '');
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(25, subblocks.size());
        for (int i = 0; i < 25; ++i) {
            Block subblock = subblocks.get(i);
            assertTrue(subblock.x1 <= subblock.x2);
            assertTrue(subblock.y1 <= subblock.y2);
            assertTrue(subblock.x1 % 2 != 0);
            assertTrue(subblock.y1 % 2 != 0);
        }
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(25, subblocks.size());
        for (int i = 0; i < 25; ++i) {
            Block subblock = subblocks.get(i);
            assertTrue(subblock.x1 % 2 == 0);
            assertTrue(subblock.y1 <= subblock.y2);
        }
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("Block(0,0):\n-----------------\n # XYZ #\n # XYZ #\n # XYZ #\n-----------------\n", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("XYZ", block.getUpperLine());
    }
}