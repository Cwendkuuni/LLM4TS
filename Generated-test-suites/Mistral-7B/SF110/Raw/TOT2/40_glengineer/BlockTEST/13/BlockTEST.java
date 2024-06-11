import static org.junit.Assert.*;

import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstructorWithValidDimensions() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(3, block.x2 - block.x1);
        assertEquals(3, block.y2 - block.y1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidDimensions() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithOutOfBoundsDimensions() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 6, 6, 10, 10);
    }

    @Test
    public void testBlockConstructorWithCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(-1, 1);
        CharPosition p2 = new CharPosition(6, 6);
        new Block(scheme, p1, p2);
    }

    @Test
    public void testBlockConstructorWithDefaultDimensions() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(5, block.x2 - block.x1);
        assertEquals(5, block.y2 - block.y1);
    }

    @Test
    public void testSubblockMethod() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(1, 1, 1, 1);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockMethodWithInvalidPosition() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(-1, 1, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtMethodOutOfBounds() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        try {
            block.charAt(-1, 1);
        } catch (IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtMethodOutOfBounds() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        try {
            block.textAt(3, 4, 1);
        } catch (IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(1, 1);
        Block block = new Block(scheme);
        try {
            block.containsOnlyOneWord();
        } catch (IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
        }
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
    }

    @Test
    public void testToStringMethod() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String expected = "Block(1,1,3,3):\n" +
                "# # #\n" +
                "# A B C #\n" +
                "# D E F #\n" +
                "# # #\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLineMethod() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String expected = "A B C";
        assertEquals(expected, block.getUpperLine());
    }
}