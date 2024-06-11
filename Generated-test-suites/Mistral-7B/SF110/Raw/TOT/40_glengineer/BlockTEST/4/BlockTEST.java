
package glengineer.blocks;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCharPositions() {
        // Test implementation from Expert 1
    }

    @Test
    public void testContainsOnlyOneWord() {
        // Test implementation from Expert 2
    }

    @Test
    public void testExtractSequentialSubblocks() {
        // Test implementation from Expert 3
    }

    @Test
    public void testBlockConstructionWithScheme() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);

        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);

        CharTable charTable = block.getCharTable();
        assertEquals(scheme.x2 - scheme.x1 + 1, charTable.size());
    }

    @Test
    public void testIllegalArgumentExceptionWithImproperBlockDefinition() {
        Scheme scheme = new Scheme(5, 5);
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, -1, 5, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 5, -1, -1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 5, 5, 5 + 1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 5, 5 + 1, 5));
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme("Block4", 6, 3);
        Block block = new Block(scheme);
        block.setCharAt(0, 0, 'A');
        block.setCharAt(1, 0, 'B');
        block.setCharAt(2, 0, 'C');
        block.setCharAt(3, 0, 'D');
        block.setCharAt(4, 0, 'E');
        block.setCharAt(5, 0, 'F');

        List<Block> expectedBlocks = Arrays.asList(
                new Block(scheme, new CharPosition(0, 0), new CharPosition(3, 0)),
                new Block(scheme, new CharPosition(3, 0), new CharPosition(5, 0))
        );

        List<Block> actualBlocks = block.extractParallelSubblocks();

        assertEquals(expectedBlocks, actualBlocks);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme("Block5", 3, 3);
        Block block = new Block(scheme);
        block.setCharAt(0, 0, 'A');

        assertEquals('A', block.charAt(0, 0));

        // Test IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> block.charAt(4, 0));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(0, 4));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme("Block6", 3, 3);
        Block block = new Block(scheme);
        block.setCharAt(0, 0, 'A');
        block.setCharAt(0, 1, 'B');
        block.setCharAt(1, 0, 'C');
        block.setCharAt(1, 1, 'D');
        block.setCharAt(1, 2, 'E');

        assertEquals("AB", block.textAt(0, 2, 0));

        // Test IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> block.textAt(4, 2, 0));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(0, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(0, 2, -1));
    }
}
