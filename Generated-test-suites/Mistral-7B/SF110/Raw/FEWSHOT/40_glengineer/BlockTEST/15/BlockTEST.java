package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstruction() {
        Scheme scheme = new Scheme(10, 5);
        Block block1 = new Block(scheme, 1, 1, 3, 3);
        Block block2 = new Block(scheme, new CharPosition(2, 2), new CharPosition(5, 4));
        Block block3 = new Block(scheme);

        assertEquals(1, block1.x1);
        assertEquals(1, block1.y1);
        assertEquals(3, block1.x2);
        assertEquals(3, block1.y2);

        assertEquals(2, block2.x1);
        assertEquals(2, block2.y1);
        assertEquals(5, block2.x2);
        assertEquals(4, block2.y2);

        assertEquals(0, block3.x1);
        assertEquals(0, block3.y1);
        assertEquals(10, block3.x2);
        assertEquals(5, block3.y2);
    }

    @Test
    public void testBlockConstructionWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 5);
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 1, 3, 3));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, -1, 3, 3));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 3, 6));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 3, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 12, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 3, 11));
    }

    @Test
    public void testSubblockConstruction() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(1, 2, 1, 2);

        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "abc";

        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));

        assertEquals('a', block.charAt(0, 1));
        assertEquals('b', block.charAt(1, 1));
        assertEquals('c', block.charAt(2, 1));

        assertEquals('a', block.charAt(0, 2));
        assertEquals('b', block.charAt(1, 2));
        assertEquals('c', block.charAt(2, 2));

        assertThrows(IllegalArgumentException.class, () -> block.charAt(3, 3));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "abc";

        assertEquals("ab", block.textAt(1, 2, 0));
        assertEquals("bc", block.textAt(1, 2, 1));
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 5);
        Block block1 = new Block(scheme, 1, 1, 3, 3);
        block1.scheme.lines[0] = "hello world";
        Block block2 = new Block(scheme, 1, 1, 3, 3);
        block2.scheme.lines[0] = "hello";

        assertTrue(block1.containsOnlyOneWord());
        assertFalse(block2.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        block.scheme.lines[0] = "abcdef";
        block.scheme.lines[1] = "ghijkl";
        block.scheme.lines[2] = "mno";

        List<Block> subblocks = block.extractSequentialSubblocks();

        List<Block> expectedSubblocks = new ArrayList<>();
        expectedSubblocks.add(new Block(scheme, 1, 1, 2, 2));
        expectedSubblocks.add(new Block(scheme, 1, 2, 3, 3));
        expectedSubblocks.add(new Block(scheme, 1, 3, 4, 4));
        expectedSubblocks.add(new Block(scheme, 1, 4, 5, 5));

        assertEquals(expectedSubblocks, subblocks);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        block.scheme.lines[0] = "abcdef";
        block.scheme.lines[1] = "ghijkl";
        block.scheme.lines[2] = "mno";

        List<Block> subblocks = block.extractParallelSubblocks();

        List<Block> expectedSubblocks = new ArrayList<>();
        expectedSubblocks.add(new Block(scheme, 1, 1, 1, 3));
        expectedSubblocks.add(new Block(scheme, 1, 2, 1, 3));
        expectedSubblocks.add(new Block(scheme, 1, 3, 1, 3));
        expectedSubblocks.add(new Block(scheme, 1, 4, 1, 3));
        expectedSubblocks.add(new Block(scheme, 1, 1, 5, 1));
        expectedSubblocks.add(new Block(scheme, 1, 2, 5, 1));
        expectedSubblocks.add(new Block(scheme, 1, 3, 5, 1));
        expectedSubblocks.add(new Block(scheme, 1, 4, 5, 1));
        expectedSubblocks.add(new Block(scheme, 1, 1, 5, 2));
        expectedSubblocks.add(new Block(scheme, 1, 1, 5, 4));

        assertEquals(expectedSubblocks, subblocks);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "abc";
        block.scheme.lines[1] = "def";

        String expectedResult = "Block(1,1,3,3):\n" +
                "# # a # #\n" +
                "# # b # #\n" +
                "# # c # #\n" +
                "# # # # #\n" +
                "# # d # #\n" +
                "# # e # #\n" +
                "# # f # #\n" +
                "# # # # #\n" +
                "# # # # #\n" +
                "# # # # #\n";

        assertEquals(expectedResult, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "abc";

        assertEquals("abc", block.getUpperLine());
    }
}