import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.CharTable;
import glengineer.blocks.CharPosition;
import glengineer.blocks.HWordPosition;
import glengineer.blocks.Scheme;
import glengineer.blocks.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4);
        assertEquals(1, block.getX1());
        assertEquals(2, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(4, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        assertEquals(1, block.getX1());
        assertEquals(2, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(4, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructorWithScheme() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testSubblockThrowsExceptionWithInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        try {
            block.subblock(1, 2, 3, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCharAtThrowsExceptionWithInvalidPosition() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        try {
            block.charAt(0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testTextAtThrowsExceptionWithInvalidSublineCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        try {
            block.textAt(1, 2, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testContainsOnlyOneWordReturnsFalseWithMultipleWords() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "This is a test.";
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordReturnsTrueWithOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "This is a test.";
        block.scheme.lines[1] = " ";
        List<Block> subblocks = block.extractSequentialSubblocks();
        Block singleWordSubblock = subblocks.get(0);
        assertTrue(singleWordSubblock.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocksReturnsEmptyListWithNoSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(0, subblocks.size());
    }

    @Test
    public void testExtractSequentialSubblocksReturnsListWithSingleSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "This is a test.";
        block.scheme.lines[1] = " ";
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
        Block singleWordSubblock = subblocks.get(0);
        List<Block> singleWordSubblocksChildren = singleWordSubblock.extractSequentialSubblocks();
        assertEquals(0, singleWordSubblocksChildren.size());
    }

    @Test
    public void testExtractSequentialSubblocksReturnsListWithMultipleSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        block.scheme.lines[0] = "This is a test.";
        block.scheme.lines[1] = "line1 ";
        block.scheme.lines[2] = "word1 ";
        block.scheme.lines[3] = "line2 ";
        block.scheme.lines[4] = "word2 ";
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        List<Block> firstSubblockChildren = firstSubblock.extractSequentialSubblocks();
        assertEquals(1, firstSubblockChildren.size());
        Block firstWordSubblock = firstSubblockChildren.get(0);
        List<Block> firstWordSubblockChildren = firstWordSubblock.extractSequentialSubblocks();
        assertEquals(0, firstWordSubblockChildren.size());
        Block secondSubblock = subblocks.get(1);
        List<Block> secondSubblockChildren = secondSubblock.extractSequentialSubblocks();
        assertEquals(1, secondSubblockChildren.size());
        Block secondWordSubblock = secondSubblockChildren.get(0);
        List<Block> secondWordSubblockChildren = secondWordSubblock.extractSequentialSubblocks();
        assertEquals(0, secondWordSubblockChildren.size());
    }

    @Test
    public void testExtractParallelSubblocksReturnsEmptyListWithNoSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(0, subblocks.size());
    }

    @Test
    public void testExtractParallelSubblocksReturnsListWithSingleSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "This is a test.";
        Block subblock = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 1));
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
        Block extractedSubblock = subblocks.get(0);
        assertEquals(subblock, extractedSubblock);
    }

    @Test
    public void testExtractParallelSubblocksReturnsListWithMultipleSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        block.scheme.lines[0] = "This is a test.";
        block.scheme.lines[1] = "line1 word1 line2 word2";
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        List<Block> firstSubblockChildren = firstSubblock.extractSequentialSubblocks();
        assertEquals(1, firstSubblockChildren.size());
        Block firstWordSubblock = firstSubblockChildren.get(0);
        assertEquals("line1 ", firstWordSubblock.toString());
        List<Block> secondSubblockChildren = secondSubblock.extractSequentialSubblocks();
        assertEquals(1, secondSubblockChildren.size());
        Block secondWordSubblock = secondSubblockChildren.get(0);
        assertEquals("line2 ", secondWordSubblock.toString());
    }

    @Test
    public void testToStringReturnsBlockRepresentation() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "This is a test.";
        String expected = "Block(1,1,3,3):\n-------------------------------------\n # This is a test. #\n -------------------------------------\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLineReturnsUpperLineText() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.scheme.lines[0] = "This is a test.";
        assertEquals("This is a test.", block.getUpperLine());
    }
}