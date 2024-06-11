package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
        block = new Block(scheme, 1, 1, 3, 3);
    }

    @Test
    public void testBlockConstructorWithCoordinates() {
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructorWithPositions() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.getX1());
        assertEquals(p1.y, block.getY1());
        assertEquals(p2.x, block.getX2());
        assertEquals(p2.y, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructorWithoutCoordinates() {
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        new Block(scheme, -1, 1, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates2() {
        new Block(scheme, 5, 5, 0, 1);
    }

    @Test
    public void testBlockSubblockWithInvalidCoordinates() {
        try {
            block.subblock(0, 0, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0,0,0.", e.getMessage());
        }

        try {
            block.subblock(scheme.getX2() + 1, scheme.getY2() + 1, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: " + (scheme.getX2() + 1) + "," + (scheme.getY2() + 1) + ", 0,0.", e.getMessage());
        }
    }

    @Test
    public void testBlockSubblockWithValidCoordinates() {
        Block subblock = block.subblock(1, 2, 2, 2);
        assertEquals(1, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(2, subblock.getY2());
        assertEquals(scheme, subblock.getScheme());
    }

    @Test
    public void testBlockCharAt() {
        assertEquals('A', block.charAt(0, 0));
        assertEquals('B', block.charAt(1, 1));
        assertEquals('C', block.charAt(2, 2));
        assertEquals('D', block.charAt(3, 3));
        assertEquals('E', block.charAt(3, 2));
        assertNull(block.charAt(4, 4));
    }

    @Test
    public void testBlockTextAt() {
        assertEquals("ABC", block.textAt(0, 3, 0));
        assertEquals("BCD", block.textAt(0, 3, 1));
        assertEquals("CD", block.textAt(0, 2, 2));
        assertEquals("", block.textAt(4, 4, 0));
        assertNull(block.textAt(4, 4, 1));
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        assertTrue(block.containsOnlyOneWord());

        CharTable table = new CharTable('A', 5, 5);
        table.setCharAt(1, 1, 'A');
        table.setCharAt(2, 1, 'B');
        Block multWordBlock = new Block(scheme, table);
        assertFalse(multWordBlock.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockContainsOnlyOneWordWithTrivialBlock() {
        Block trivialBlock = new Block(scheme);
        trivialBlock.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockContainsOnlyOneWordWithEmptyBlock() {
        Block emptyBlock = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return null;
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.getX1());
        assertEquals(1, subblock1.getY1());
        assertEquals(2, subblock1.getX2());
        assertEquals(2, subblock1.getY2());

        Block subblock2 = subblocks.get(1);
        assertEquals(2, subblock2.getX1());
        assertEquals(2, subblock2.getY1());
        assertEquals(2, subblock2.getX2());
        assertEquals(3, subblock2.getY2());

        Block subblock3 = subblocks.get(2);
        assertEquals(3, subblock3.getX1());
        assertEquals(3, subblock3.getY1());
        assertEquals(3, subblock3.getX2());
        assertEquals(3, subblock3.getY2());
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.getX1());
        assertEquals(1, subblock1.getY1());
        assertEquals(3, subblock1.getX2());
        assertEquals(3, subblock1.getY2());

        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.getX1());
        assertEquals(3, subblock2.getY1());
        assertEquals(3, subblock2.getX2());
        assertEquals(3, subblock2.getY2());
    }

    @Test
    public void testBlockToString() {
        assertEquals("Block(0,0:5,5):\n" +
                "# AABBCC\n" +
                "# AA#BB\n" +
                "# ABBCC\n" +
                "# BBCC#\n" +
                "# C#CC#\n" +
                "# CC#CC\n" +
                "# ---#--\n", block.toString());

        assertEquals("Block(1,1:3,3):\n" +
                "# BC\n" +
                "# CD\n" +
                "# D#\n" +
                "# #\n" +
                "# #\n" +
                "# #\n" +
                "# ---#--\n", block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        assertEquals("AABB", block.getUpperLine());
    }
}