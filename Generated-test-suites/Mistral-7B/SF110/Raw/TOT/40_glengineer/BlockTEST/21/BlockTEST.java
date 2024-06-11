import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstructionWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        assertEquals(5, block.x2 - block.x1);
        assertEquals(5, block.y2 - block.y1);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithValidCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(2, 3);
        CharPosition p2 = new CharPosition(5, 7);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p2.x - p1.x, block.x2 - block.x1);
        assertEquals(p2.y - p1.y, block.y2 - block.y1);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithDefaultCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockWithOneWord() {
        Scheme scheme = new Scheme(10, 10);
        char[] line1 = "Hello".toCharArray();
        char[] line2 = "World".toCharArray();
        scheme.lines = new char[][] { line1, line2 };
        Block block = new Block(scheme, 0, 0, 5, 5);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockWithMultipleWords() {
        Scheme scheme = new Scheme(10, 10);
        char[] line1 = "Hello".toCharArray();
        char[] line2 = "World".toCharArray();
        scheme.lines = new char[][] { line1, line2 };
        Block block = new Block(scheme, 0, 0, 11, 5);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        char[] line1 = "ABCDABCDABCDABCDABCD".toCharArray();
        char[] line2 = "12341234123412341234".toCharArray();
        scheme.lines = new char[][] { line1, line2 };
        Block parentBlock = new Block(scheme, 0, 0, 16, 6);
        List<Block> subBlocks = parentBlock.extractSequentialSubblocks();
        assertEquals(7, subBlocks.size());
        for (int i = 0; i < 7; i++) {
            Block subBlock = subBlocks.get(i);
            assertEquals(i + 1, subBlock.x1 - parentBlock.x1);
            assertEquals(i + 1, subBlock.x2 - subBlock.x1);
        }
    }

    @Test
    public void testTextAtValidPosition() {
        Scheme scheme = new Scheme(10, 10);
        char[] line = "HelloWorld".toCharArray();
        scheme.lines = new char[][] { line };
        Block block = new Block(scheme, 0, 0, 11, 1);
        assertEquals("Hello", block.textAt(0, 7, 0));
        assertEquals("World", block.textAt(4, 11, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidPosition() {
        Scheme scheme = new Scheme(10, 10);
        char[] line = "HelloWorld".toCharArray();
        scheme.lines = new char[][] { line };
        Block block = new Block(scheme, 0, 0, 11, 1);
        try {
            block.textAt(-1, 11, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        try {
            block.textAt(12, 11, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        try {
            block.textAt(0, 12, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        try {
            block.textAt(0, 11, 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
}