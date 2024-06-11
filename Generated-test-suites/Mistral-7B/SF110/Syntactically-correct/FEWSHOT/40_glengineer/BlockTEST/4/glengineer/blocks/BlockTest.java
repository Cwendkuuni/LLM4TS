package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.*;

public class BlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithInvalidParameters() {
        Scheme scheme = new Scheme(10, 10);
        Block block = null;
        try {
            block = new Block(scheme, -1, 1, 1, 1);
        } catch (IllegalArgumentException iae) {
        }
        assertNotNull(block);

        try {
            block = new Block(scheme, 1, -1, 1, 1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, -1, 1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, 1, -1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, 1, 1, null);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, 11, 1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, 1, 11);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 12, 1, 1, 1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 12, 1, 1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, 12, 1);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, 1, 1, 1, 12);
        } catch (IllegalArgumentException iae) {
        }

        try {
            block = new Block(scheme, null, 1, 1, 1);
        } catch (IllegalArgumentException iae) {
        }
    }

    @Test
    public void testBlockConstructionWithValidParameters() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 4);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(4, block.getY2());
    }

    @Test
    public void testBlockConstructionWithOrigin() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.getX1());
        assertEquals(p1.y, block.getY1());
        assertEquals(p2.x, block.getX2());
        assertEquals(p2.y, block.getY2());
    }

    @Test
    public void testBlockConstructionWithOriginAndSize() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(10, block.getX2());
        assertEquals(10, block.getY2());
    }

    @Test
    public void testSubBlockWithValidParameters() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 4);
        Block subBlock = parentBlock.subblock(1, 1, 1, 1);

        assertNull(subBlock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBlockWithInvalidParameters() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 4);
        parentBlock.subblock(-1, -1, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBlockWithNegativeCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 4);
        parentBlock.subblock(0, -1, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBlockWithOutOfBoundsCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 4);
        parentBlock.subblock(4, 4, 1, 1);
    }

    @Test
    public void testSubBlockWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 4);
        Block subBlock = parentBlock.subblock(1, 1, 1, 1);
        assertNotNull(subBlock);
    }

    @Test
    public void testSubBlockWithValidParametersAndSize() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 4);
        Block subBlock = parentBlock.subblock(1, 1, 2, 2);
        assertNotNull(subBlock);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 4, 5);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWord_MultipleWords() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 4, 6);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 6, 6);
        List<Block> subBlocks = parentBlock.extractSequentialSubblocks();
        assertEquals(5, subBlocks.size());
        Block firstSubBlock = subBlocks.get(0);
        assertEquals(3, firstSubBlock.getX1());
        assertEquals(1, firstSubBlock.getY1());
        assertEquals(4, firstSubBlock.getX2());
        assertEquals(1, firstSubBlock.getY2());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 6, 6);
        List<Block> subBlocks = parentBlock.extractParallelSubblocks();
        assertEquals(6, subBlocks.size());
        Block firstSubBlock = subBlocks.get(0);
        assertEquals(1, firstSubBlock.getX1());
        assertEquals(1, firstSubBlock.getY1());
        assertEquals(1, firstSubBlock.getX2());
        assertEquals(3, firstSubBlock.getY2());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 4);
        assertEquals("Block(0,0):\n----------------------------------\n # foo # bar # baz # qux # quux # quuz # quuz\n----------------------------------\n", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 4);
        assertEquals("foo", block.getUpperLine());
    }
}