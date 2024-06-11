package glengineer.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstructionWithX1Y1X2Y2() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }

    @Test
    public void testBlockConstructionWithP1P2() {
        final Scheme scheme = new Scheme(5, 5);
        final CharPosition p1 = new CharPosition(1, 1);
        final CharPosition p2 = new CharPosition(3, 3);
        final Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.getX1());
        assertEquals(p1.y, block.getY1());
        assertEquals(p2.x, block.getX2());
        assertEquals(p2.y, block.getY2());
    }

    @Test
    public void testBlockConstructionWithSchemeOnly() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.x2, block.getX2());
        assertEquals(scheme.y2, block.getY2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithNegativeX1() {
        final Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithNegativeY1() {
        final Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, -1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithX1GreaterThanX2() {
        final Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 5, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithY1GreaterThanY2() {
        final Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 5, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithX2GreaterThanSchemeMaxX() {
        final Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 1, 6, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithY2GreaterThanSchemeMaxY() {
        final Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 1, 1, 6);
    }

    @Test
    public void testSubblockConstruction() {
        final Scheme scheme = new Scheme(5, 5);
        final Block parentBlock = new Block(scheme, 0, 0, 5, 5);
        final Block subblock = parentBlock.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(2, subblock.getY2());
        assertEquals(scheme, subblock.getScheme());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubblockConstructionWithInvalidParameters() {
        final Scheme scheme = new Scheme(5, 5);
        final Block parentBlock = new Block(scheme, 0, 0, 5, 5);
        parentBlock.subblock(6, 6, 6, 6);
    }

    @Test
    public void testCharAt() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(' ', block.charAt(0, 0));
        assertEquals('A', block.charAt(1, 1));
        assertEquals('Z', block.charAt(2, 2));
        assertEquals('Z', block.charAt(3, 2));
        assertEquals('Z', block.charAt(3, 2));
        assertEquals('Z', block.charAt(2, 3));
        assertEquals('Z', block.charAt(3, 3));
        assertEquals('Z', block.charAt(3, 3));
        try {
            block.charAt(-1, 0);
        } catch (IllegalArgumentException iae) {
            //expected
        }
        try {
            block.charAt(4, 4);
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void testTextAt() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("ABC", block.textAt(1, 3, 1));
        assertEquals("DEF", block.textAt(1, 3, 2));
        assertEquals("GHI", block.textAt(1, 3, 3));
        try {
            block.textAt(-1, 3, 1);
        } catch (IllegalArgumentException iae) {
            //expected
        }
        try {
            block.textAt(1, 3, 4);
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'A');
        block.setCharAt(2, 1, 'B');
        block.setCharAt(2, 2, 'B');
        block.setCharAt(3, 2, 'C');
        block.setCharAt(3, 3, 'C');
        assertFalse(block.containsOnlyOneWord());

        block.setCharAt(3, 2, 'B');
        assertTrue(block.containsOnlyOneWord());

        block.setCharAt(2, 1, 'A');
        block.setCharAt(3, 1, 'B');
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(25, subblocks.size());

        for (Block subblock : subblocks) {
            assertEquals(1, subblock.getY1());
            assertEquals(1, subblock.getX1());
            assertEquals(1, subblock.getY2());
            assertEquals(1, subblock.getX2());
        }
    }

    @Test
    public void testExtractParallelSubblocks() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(5, subblocks.size());

        for (Block subblock : subblocks) {
            assertEquals(5, subblock.getX1());
            assertEquals(1, subblock.getY1());
            assertEquals(5, subblock.getX2());
            assertEquals(1, subblock.getY2());
        }
    }

    @Test
    public void testToString() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("Block(1,1,3,3):\n---------------------------\n # ABA #\n # BBB #\n # CBC #\n---------------------------\n", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("ABA", block.getUpperLine());
    }

    @Test
    public void testEquals() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block1 = new Block(scheme, 1, 1, 3, 3);
        final Block block2 = new Block(scheme, 1, 1, 3, 3);
        final Block block3 = new Block(scheme, 2, 1, 3, 3);

        assertEquals(block1, block1);
        assertEquals(block1, block2);
        assertNotEquals(block1, block3);
    }

    @Test
    public void testHashCode() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block1 = new Block(scheme, 1, 1, 3, 3);
        final Block block2 = new Block(scheme, 1, 1, 3, 3);
        final Block block3 = new Block(scheme, 2, 1, 3, 3);

        assertEquals(block1.hashCode(), block1.hashCode());
        assertEquals(block1.hashCode(), block2.hashCode());
        assertNotEquals(block1.hashCode(), block3.hashCode());
    }

    @Test
    public void testClone() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        final Block clonedBlock = block.clone();
        assertEquals(block, clonedBlock);
    }

    @Test
    public void testIsTrivial() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block1 = new Block(scheme, 1, 1, 1, 1);
        final Block block2 = new Block(scheme, 1, 1, 3, 3);
        assertTrue(block1.isTrivial());
        assertFalse(block2.isTrivial());
    }

    @Test
    public void testFirstWord_Horizontally() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'A');
        block.setCharAt(1, 2, 'B');
        block.setCharAt(2, 1, 'C');
        block.setCharAt(2, 2, 'B');
        block.setCharAt(3, 1, 'B');
        block.setCharAt(3, 2, 'E');
        final HWordPosition firstWord = block.firstWord_Horizontally();
        assertEquals(1, firstWord.x);
        assertEquals(1, firstWord.y);
    }

    @Test
    public void testLastWord_Horizontally() {
        final Scheme scheme = new Scheme(5, 5);
        final Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'A');
        block.setCharAt(1, 2, 'B');
        block.setCharAt(2, 1, 'C');
        block.setCharAt(2, 2, 'B');
        block.setCharAt(3, 1, 'B');
        block.setCharAt(3, 2, 'E');
        final WordPosition lastWord = block.lastWord_Horizontally();
        assertEquals(3, lastWord.x);
        assertEquals(2, lastWord.y);
    }
}