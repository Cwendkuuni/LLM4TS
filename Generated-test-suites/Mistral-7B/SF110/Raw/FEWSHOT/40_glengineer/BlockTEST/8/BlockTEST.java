package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstruction() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, 1, 1, 3, 2);
        Block block2 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 2));
        Block block3 = new Block(scheme);
        assertEquals(1, block1.x1);
        assertEquals(1, block1.y1);
        assertEquals(3, block1.x2);
        assertEquals(2, block1.y2);
        assertEquals(1, block2.x1);
        assertEquals(1, block2.y1);
        assertEquals(3, block2.x2);
        assertEquals(2, block2.y2);
        assertEquals(0, block3.x1);
        assertEquals(0, block3.y1);
        assertEquals(10, block3.x2);
        assertEquals(10, block3.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionInvalid() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, -1, 1, 3, 2);
    }

    @Test
    public void testBlockSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 3, 2);
        Block childBlock = parentBlock.subblock(1, 1, 1, 1);
        assertEquals(1, childBlock.x1);
        assertEquals(1, childBlock.y1);
        assertEquals(1, childBlock.x2);
        assertEquals(1, childBlock.y2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractBlockSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new EmptyBlock(scheme);
        parentBlock.subblock(1, 1, 1, 1);
    }

    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 2);
        assertEquals(' ', block.charAt(0, 0));
        assertEquals('A', block.charAt(1, 1));
        assertEquals('Z', block.charAt(2, 2));
        try {
            block.charAt(-1, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            block.charAt(3, 3);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 2);
        String text = "The quick brown fox jumps over the lazy dog";
        scheme.lines = new String[10][];
        scheme.lines[0] = text.substring(0, 20).toCharArray();
        assertEquals("The quick ", block.textAt(0, 10, 0));
        assertEquals("brown fox", block.textAt(11, 20, 0));
        try {
            block.textAt(0, 10, 3);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            block.textAt(15, 20, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, 1, 1, 3, 2);
        Block block2 = new WordBlock(scheme, "The quick brown fox", new CharPosition(1, 1), new CharPosition(3, 2));
        Block block3 = new EmptyBlock(scheme);
        assertTrue(block1.containsOnlyOneWord());
        assertFalse(block2.containsOnlyOneWord());
        assertFalse(block3.containsOnlyOneWord());
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 8, 8);
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        List<Block> expected = new ArrayList<>();
        expected.add(new WordBlock(scheme, "The quick brown fox", new CharPosition(1, 1), new CharPosition(3, 2)));
        expected.add(new WordBlock(scheme, "jumps over", new CharPosition(4, 1), new CharPosition(6, 2)));
        expected.add(new WordBlock(scheme, "the lazy dog", new CharPosition(7, 1), new CharPosition(8, 2)));
        assertEquals(expected, subblocks);
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 8, 8);
        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        List<Block> expected = new ArrayList<>();
        expected.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 8)));
        expected.add(new Block(scheme, new CharPosition(1, 9), new CharPosition(1, 8)));
        expected.add(new Block(scheme, new CharPosition(2, 1), new CharPosition(2, 8)));
        expected.add(new Block(scheme, new CharPosition(2, 9), new CharPosition(2, 8)));
        expected.add(new Block(scheme, new CharPosition(3, 1), new CharPosition(3, 8)));
        expected.add(new Block(scheme, new CharPosition(3, 9), new CharPosition(3, 8)));
        expected.add(new Block(scheme, new CharPosition(4, 1), new CharPosition(4, 8)));
        expected.add(new Block(scheme, new CharPosition(4, 9), new CharPosition(4, 8)));
        expected.add(new Block(scheme, new CharPosition(5, 1), new CharPosition(5, 8)));
        expected.add(new Block(scheme, new CharPosition(5, 9), new CharPosition(5, 8)));
        expected.add(new Block(scheme, new CharPosition(6, 1), new CharPosition(6, 8)));
        expected.add(new Block(scheme, new CharPosition(6, 9), new CharPosition(6, 8)));
        expected.add(new Block(scheme, new CharPosition(7, 1), new CharPosition(7, 8)));
        expected.add(new Block(scheme, new CharPosition(7, 9), new CharPosition(7, 8)));
        expected.add(new Block(scheme, new CharPosition(8, 1), new CharPosition(8, 8)));
        expected.add(new Block(scheme, new CharPosition(8, 9), new CharPosition(8, 8)));
        assertEquals(expected, subblocks);
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 2);
        assertEquals("Block(1,1,3,2):\n------------------\n # The quick brown fox #\n------------------\n", block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 2);
        assertEquals("The quick brown fox", block.getUpperLine());
    }

    @Test
    public void testEmptyBlockToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new EmptyBlock(scheme);
        assertEquals("Block(0,0,10,10):\n------------------\n------------------\n------------------\n", block.toString());
    }

    @Test
    public void testEmptyBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new EmptyBlock(scheme);
        assertEquals("", block.getUpperLine());
    }
}