package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testIllegalBlockDefinition() {
        try {
            new Block(null, 0, 0, 1, 1);
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0,1,1.", e.getMessage());
        }

        try {
            new Block(null, new CharPosition(-1, 0), new CharPosition(1, 1));
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0,1,1.", e.getMessage());
        }

        try {
            new Block(null, new CharPosition(1, 1), new CharPosition(1, 2));
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1,1,2.", e.getMessage());
        }

        try {
            new Block(null, new CharPosition(1, 1), new CharPosition(2, 2));
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1,2,2.", e.getMessage());
        }

        try {
            new Block(null, new CharPosition(2, 2), new CharPosition(1, 1));
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 2,2,1,1.", e.getMessage());
        }

        try {
            new Block(null, new CharPosition(0, 2), new CharPosition(3, 3));
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,2,3,3.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructorWithSchemeAndCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, 1, 1, 3, 3);
        assertEquals(scheme, block1.scheme);
        assertEquals(1, block1.x1);
        assertEquals(1, block1.y1);
        assertEquals(3, block1.x2);
        assertEquals(3, block1.y2);
    }

    @Test
    public void testBlockConstructorWithSchemeAndPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block1 = new Block(scheme, p1, p2);
        assertEquals(scheme, block1.scheme);
        assertEquals(p1.x, block1.x1);
        assertEquals(p1.y, block1.y1);
        assertEquals(p2.x, block1.x2);
        assertEquals(p2.y, block1.y2);
    }

    @Test
    public void testBlockConstructorWithSchemeOnly() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme);
        assertEquals(scheme, block1.scheme);
        assertEquals(0, block1.x1);
        assertEquals(0, block1.y1);
        assertEquals(scheme.x2, block1.x2);
        assertEquals(scheme.y2, block1.y2);
    }

    @Test
    public void testSubblockWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        Block subblock1 = block1.subblock(1, 1, 1, 1);
        Block subblock2 = block1.subblock(2, 2, 2, 2);
        assertEquals(scheme, subblock1.scheme);
        assertEquals(1, subblock1.x1);
        assertEquals(1, subblock1.y1);
        assertEquals(1, subblock1.x2);
        assertEquals(1, subblock1.y2);
        assertEquals(scheme, subblock2.scheme);
        assertEquals(2, subblock2.x1);
        assertEquals(2, subblock2.y1);
        assertEquals(2, subblock2.x2);
        assertEquals(2, subblock2.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        block1.subblock(-1, -1, 1, 1);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        block1.setSchemeline(new String("word1 word2").toCharArray(), 0, 10);
        assertTrue(block1.containsOnlyOneWord());
        assertEquals("word1", block1.textAt(0, block1.x2, 0));
    }

    @Test
    public void testContainsNoWords() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        assertTrue(block1.containsOnlyOneWord());
        block1.setSchemeline(new char[0], 0, 10);
        assertTrue(block1.containsOnlyOneWord());
        assertEquals("", block1.textAt(0, block1.x2, 0));
    }

    @Test
    public void testIsTrivial() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        assertFalse(block1.isTrivial());
        block1.setSchemeline(new char[0], 0, 10);
        assertTrue(block1.isTrivial());
    }

    @Test
    public void testFirstWordHorizontally() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        block1.setSchemeline(new String("word1 word2").toCharArray(), 0, 10);
        HWordPosition firstWord = block1.firstWord_Horizontally();
        assertEquals(0, firstWord.x);
        assertEquals(0, firstWord.y);
    }

    @Test
    public void testLastWordHorizontally() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        block1.setSchemeline(new String("word1 word2").toCharArray(), 0, 10);
        WordPosition lastWord = block1.lastWord_Horizontally();
        assertEquals(3, lastWord.x);
        assertEquals(0, lastWord.y);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        block1.setSchemeline(new String("word1 word2").toCharArray(), 0, 10);
        String expected = "Block(1,1,3,3):\n" +
                "# word1 word2 #\n" +
                "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n";
        assertEquals(expected, block1.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        block1.setSchemeline(new String("word1 word2").toCharArray(), 0, 10);
        assertEquals("word1", block1.getUpperLine());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        block1.setSchemeline(new String("ab cdef ghij klmno pqrst uvwxyz").toCharArray(), 0, 50);
        List<Block> subblocks = block1.extractSequentialSubblocks();
        List<Block> expected = new ArrayList<>();
        expected.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 4)));
        expected.add(new Block(scheme, new CharPosition(2, 1), new CharPosition(2, 4)));
        expected.add(new Block(scheme, new CharPosition(3, 1), new CharPosition(3, 4)));
        expected.add(new Block(scheme, new CharPosition(4, 1), new CharPosition(4, 4)));
        expected.add(new Block(scheme, new CharPosition(5, 1), new CharPosition(5, 5)));
        assertEquals(expected, subblocks);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        block1.setSchemeline(new String("abcd efgh ijkl mnop qrst uvwxyz").toCharArray(), 0, 50);
        List<Block> subblocks = block1.extractParallelSubblocks();
        List<Block> expected = new ArrayList<>();
        expected.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 1)));
        expected.add(new Block(scheme, new CharPosition(1, 2), new CharPosition(5, 2)));
        expected.add(new Block(scheme, new CharPosition(1, 3), new CharPosition(5, 3)));
        expected.add(new Block(scheme, new CharPosition(1, 4), new CharPosition(5, 4)));
        expected.add(new Block(scheme, new CharPosition(1, 5), new CharPosition(5, 5)));
        assertEquals(expected, subblocks);
    }
}