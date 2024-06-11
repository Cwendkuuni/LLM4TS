import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {
    

    @Test
    public void testCase1_constructor1() {
        // Test Block construction with valid arguments
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        // Test block properties
        assertTrue(block.x1 == 1);
        assertTrue(block.y1 == 1);
        assertTrue(block.x2 == 3);
        assertTrue(block.y2 == 3);
    }

    @Test
    public void testCase1_constructor2() {
        // Test Block construction with valid CharPositions
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new Block(scheme, p1, p2);
        // Test block properties
        assertTrue(block.x1 == p1.x);
        assertTrue(block.y1 == p1.y);
        assertTrue(block.x2 == p2.x);
        assertTrue(block.y2 == p2.y);
    }

    @Test
    public void testCase1_constructor3() {
        // Test Block construction with no arguments
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        // Test block properties
        assertTrue(block.x1 == 0);
        assertTrue(block.y1 == 0);
        assertTrue(block.x2 == scheme.x2);
        assertTrue(block.y2 == scheme.y2);
    }

    @Test
    public void testCase1_subblock() {
        // Test subblock extraction
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        Block subblock = subblocks.get(0);
        // Test subblock properties
        assertTrue(subblock.x1 == 1);
        assertTrue(subblock.y1 == 1);
        assertTrue(subblock.x2 == 2);
        assertTrue(subblock.y2 == 1);
    }

    // ... (Continue with the remaining test methods)

    @Test
    public void testCase2_constructor1_invalidBlockDefinition() {
        // Test IllegalArgumentException when passing invalid arguments to constructor
        Scheme scheme = new Scheme(5, 5);
        try {
            new Block(scheme, -1, 1, 3, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_constructor2_invalidCharPosition() {
        // Test IllegalArgumentException when passing invalid CharPosition to constructor
        Scheme scheme = new Scheme(5, 5);
        CharPosition p = new CharPosition(-1, 1);
        try {
            new Block(scheme, p, new CharPosition(3, 4));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_constructor3_nullScheme() {
        // Test NullPointerException when passing null Scheme to constructor
        try {
            new Block(null, new CharPosition(1, 2), new CharPosition(3, 4));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_subblock_negativeIndex() {
        // Test IndexOutOfBoundsException when extracting subblocks with negative index
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        try {
            List<Block> subblocks = block.extractSequentialSubblocks();
            subblocks.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_subblock_outOfBounds() {
        // Test IndexOutOfBoundsException when extracting subblocks out of bounds
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        try {
            List<Block> subblocks = block.extractSequentialSubblocks();
            subblocks.get(subblocks.size() + 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_charAt_outOfBounds() {
        // Test IllegalArgumentException when passing invalid char position to charAt()
        Scheme scheme = new Scheme(5, 5);
        CharPosition p = new CharPosition(-1, 1);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        try {
            block.charAt(p.x, p.y);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_textAt_invalidSublineCoordinates() {
        // Test IllegalArgumentException when passing invalid subline coordinates to textAt()
        Scheme scheme = new Scheme(5, 5);
        CharPosition p = new CharPosition(-1, 1);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        try {
            block.textAt(p.x, p.x, p.y);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_containsOnlyOneWord_trivialBlock() {
        // Test IllegalArgumentException when checking unicity of words in a trivial block
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCase2_containsOnlyOneWord_multipleWords() {
        // Test false when checking unicity of words in a block containing multiple words
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 2);
        CharPosition p3 = new CharPosition(4, 3);
        CharPosition p4 = new CharPosition(5, 3);
        Block block = new Block(scheme, p1, p2);
        block.scheme.lines[block.y1].setCharAt(block.x1, 'A');
        block.scheme.lines[block.y1].setCharAt(block.x1 + 1, 'B');
        block.scheme.lines[block.y1].setCharAt(block.x1 + 2, ' ');
        block.scheme.lines[block.y1].setCharAt(block.x1 + 3, 'C');
        block.scheme.lines[block.y1].setCharAt(block.x1 + 4, 'D');
        assertTrue(block.containsOnlyOneWord() == false);
    }

    
}