
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import glengineer.blocks.Block;
import glengineer.blocks.BlockTest;
import glengineer.blocks.Scheme;
import glengineer.positions.*;
import org.junit.Before;

public class BlockTest {

    Scheme scheme;

    @Before
    public void setup() {
        scheme = new Scheme(10, 10);
    }

    @Test
    public void testConstructorWithFourArguments() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        assertNotNull(block);
    }

    @Test
    public void testConstructorWithTwoCharPositionArguments() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        assertNotNull(block);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithOutOfRangeX1Y1() {
        Block block = new Block(scheme, new CharPosition(-1, -1), new CharPosition(3, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithOutOfRangeX2Y2() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(11, 11));
    }

    @Test
    public void testConstructorWithOverlappingBlocks() {
        Block block1 = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        Block block2 = new Block(scheme, new CharPosition(2, 2), new CharPosition(4, 4));
        assertNotNull(block1);
        assertNotNull(block2);
        assertEquals(block1, block2); // Test for overlapping blocks
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidScheme() {
        Scheme invalidScheme = new Scheme(-1, -1);
        Block block = new Block(invalidScheme, new CharPosition(1, 2), new CharPosition(3, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullScheme() {
        Block block = new Block(null, new CharPosition(1, 2), new CharPosition(3, 4));
    }

    @Test
    public void testCharAtWithValidPosition() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        assertEquals('X', block.charAt(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithOutOfRangePosition() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        try {
            block.charAt(-1, 2);
        } catch (IllegalArgumentException e) {
            assertEquals("char position -1, 2 out of block.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidSublineCoordinates() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        try {
            block.textAt(-1, 2, 2);
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Block block = new Block(scheme);
        try {
            block.containsOnlyOneWord();
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWordWithSingleWordBlock() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        assertEquals(true, block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithMultipleWordsBlock() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 5));
        assertEquals(false, block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        List<Block> expectedSubblocks = Arrays.asList(
                new Block(scheme, new CharPosition(1, 2), new CharPosition(2, 2)),
                new Block(scheme, new CharPosition(2, 2), new CharPosition(3, 2)),
                new Block(scheme, new CharPosition(3, 2), new CharPosition(3, 4))
        );
        List<Block> actualSubblocks = block.extractSequentialSubblocks();
        assertEquals(expectedSubblocks, actualSubblocks);
    }

    @Test
    public void testToString() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        String expectedResult = "Block(1, 2, 3, 4):\n# # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n# X # X # X # X # X # X # X # X # X # X # X # X # X # X # X # X # X # X # \n# # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n";
        assertEquals(expectedResult, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 2));
        String expectedUpperLine = "# X #";
        assertEquals(expectedUpperLine, block.getUpperLine());
    }
}