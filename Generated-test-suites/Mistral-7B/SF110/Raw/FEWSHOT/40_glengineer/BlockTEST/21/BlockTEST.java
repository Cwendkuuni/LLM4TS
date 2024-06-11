package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithSchemeOnly() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionX2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 10, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 3, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionX1Y1() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, -1, -1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionX1X2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY1Y2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 3, 1);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        Block subblock = parentBlock.subblock(1, 2, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        block.subblock(1, 1, 1, 1);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
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
            block.charAt(4, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("ABC", block.textAt(0, 3, 0));
        assertEquals("DEF", block.textAt(0, 3, 2));
        try {
            block.textAt(-1, 0, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            block.textAt(4, 0, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'A');
        block.setCharAt(2, 1, 'B');
        block.setCharAt(2, 2, 'B');
        block.setCharAt(3, 2, 'C');

        assertTrue(block.containsOnlyOneWord());

        block.setCharAt(2, 1, 'B');
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(4, subblocks.size());
        for (Block subblock : subblocks) {
            assertEquals(2, subblock.y2 - subblock.y1);
        }
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(5, subblocks.size());
        for (Block subblock : subblocks) {
            assertEquals(2, subblock.x2 - subblock.x1);
        }
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String expectedOutput = "Block(0,0:)\n" +
                "# A B C #\n" +
                "# A B C #\n" +
                "# A B C #\n" +
                "# A B C #\n" +
                "# A B C #\n" +
                "#------------------------#\n";
        assertEquals(expectedOutput, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("ABC", block.getUpperLine());
    }
}