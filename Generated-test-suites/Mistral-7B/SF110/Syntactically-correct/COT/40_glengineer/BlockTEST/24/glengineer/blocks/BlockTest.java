import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructionWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        try {
            new Block(scheme, -1, 1, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,1,5,5.", e.getMessage());
        }

        try {
            new Block(scheme, 1, -1, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,-1,5,5.", e.getMessage());
        }

        try {
            new Block(scheme, 1, 1, 10, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1,10,10.", e.getMessage());
        }

        try {
            new Block(scheme, 1, 1, 0, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1,0,10.", e.getMessage());
        }

        try {
            new Block(scheme, 1, 1, 5, 11);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1,5,11.", e.getMessage());
        }

        try {
            new Block(scheme, 1, 1, 11, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1,11,10.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructionWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        assertEquals(5, block.x2 - block.x1);
        assertEquals(5, block.y2 - block.y1);
    }

    @Test
    public void testBlockConstructionWithSinglePosition() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1));
        assertEquals(10, block.x2 - block.x1);
        assertEquals(10, block.y2 - block.y1);
    }

    @Test
    public void testBlockConstructionWithSingleScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(10, block.x2 - block.x1);
        assertEquals(10, block.y2 - block.y1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidArguments() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        block.subblock(-1, -1, -1, -1);
    }

    @Test
    public void testCharAtWithValidCoordinates() {
        Scheme scheme = new Scheme("TestString", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        assertEquals('T', block.charAt(1, 1));
        assertEquals('e', block.charAt(2, 1));
        assertEquals('s', block.charAt(3, 1));
        assertEquals('t', block.charAt(4, 1));
        assertEquals('S', block.charAt(1, 2));
        assertEquals(' ', block.charAt(1, 3));
        assertEquals(' ', block.charAt(1, 4));
        assertEquals(' ', block.charAt(1, 5));
        assertEquals(' ', block.charAt(5, 1));
        assertEquals(' ', block.charAt(5, 2));
        assertEquals(' ', block.charAt(5, 3));
        assertEquals('S', block.charAt(5, 4));
        assertEquals(' ', block.charAt(5, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme("TestString", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        block.charAt(-1, -1);
        block.charAt(11, 1);
        block.charAt(1, 11);
        block.charAt(1, 11);
    }

    @Test
    public void testTextAtWithValidCoordinates() {
        Scheme scheme = new Scheme("TestStringTestString", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        assertEquals("Test", block.textAt(1, 4, 1));
        assertEquals("String", block.textAt(1, 6, 1));
        assertEquals("Test", block.textAt(1, 4, 2));
        assertEquals("String", block.textAt(1, 6, 2));
        assertEquals("Test", block.textAt(2, 4, 1));
        assertEquals("String", block.textAt(2, 6, 1));
        assertEquals("Test", block.textAt(2, 4, 2));
        assertEquals("String", block.textAt(2, 6, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme("TestStringTestString", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        block.textAt(-1, 1, 1);
        block.textAt(1, -1, 1);
        block.textAt(1, 1, 6);
        block.textAt(1, 1, 11);
        block.textAt(1, 1, 11);
    }

    @Test
    public void testContainsOnlyOneWordWithOneWordBlock() {
        Scheme scheme = new Scheme("Test", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithMultipleWordsBlock() {
        Scheme scheme = new Scheme("TestTest Test Test", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme("TestTest Test Test Test Test Test Test Test Test", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(7, 7));
        List<Block> result = block.extractSequentialSubblocks();
        List<Block> expected = Arrays.asList(
                new Block(new Scheme("Test", 7, 7), new CharPosition(1, 1), new CharPosition(1, 6)),
                new Block(new Scheme("Test", 7, 7), new CharPosition(2, 1), new CharPosition(2, 6)),
                new Block(new Scheme("Test", 7, 7), new CharPosition(3, 1), new CharPosition(3, 6)),
                new Block(new Scheme("Test", 7, 7), new CharPosition(4, 1), new CharPosition(4, 6)),
                new Block(new Scheme("Test", 7, 7), new CharPosition(5, 1), new CharPosition(5, 6)),
                new Block(new Scheme("Test", 7, 7), new CharPosition(6, 1), new CharPosition(6, 6))
        );
        assertEquals(expected, result);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme("TestTest Test Test Test Test Test Test Test Test", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(7, 7));
        List<Block> result = block.extractParallelSubblocks();
        List<Block> expected = Arrays.asList(
                new Block(new Scheme("Test Test", 7, 7), new CharPosition(1, 1), new CharPosition(3, 7)),
                new Block(new Scheme("Test Test", 7, 7), new CharPosition(4, 1), new CharPosition(6, 7))
        );
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme("TestTest Test Test Test Test Test Test Test Test", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(7, 7));
        String expected = "Block(1,1,7,7):\n" +
                "# Test Test Test Test Test Test Test Test Test #\n" +
                "# T   T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# T   T   T   T   T   T   T   T   T #\n" +
                "# #                               #\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme("TestTest Test Test Test Test Test Test Test Test Test", 10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(7, 7));
        assertEquals("TestTest", block.getUpperLine());
    }
}