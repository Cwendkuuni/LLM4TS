import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testCharAt() {
        Block block = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(10, 10));
        char expectedChar = 'A';
        char actualChar = block.charAt(5, 5);
        assertEquals(expectedChar, actualChar);
    }

    @Test
    public void testCharAtEdgeCases() {
        Block block = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(10, 10));

        // Negative x and y values
        assertThrows(IllegalArgumentException.class, () -> block.charAt(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(10, -1));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(-1, 10));

        // x and y values out of the block's boundary
        assertThrows(IllegalArgumentException.class, () -> block.charAt(11, 11));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(0, 11));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(11, 0));

        // Invalid Scheme object
        Scheme invalidScheme = null;
        assertThrows(IllegalArgumentException.class, () -> new Block(invalidScheme, 0, 0, 10, 10));
    }

    @Test
    public void testContainsOnlyOneWord() {
        Block block = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(10, 10));
        String expectedWord = "BlockTest";
        block.setTextAt(5, 5, expectedWord);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordEdgeCases() {
        Block emptyBlock = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(0, 0));
        assertFalse(emptyBlock.containsOnlyOneWord());

        Block blockWithMultipleWords = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(10, 10));
        blockWithMultipleWords.setTextAt(5, 5, "BlockTest");
        blockWithMultipleWords.setTextAt(5, 11, "TestBlock");
        assertFalse(blockWithMultipleWords.containsOnlyOneWord());

        Block blockWithIllegalDefinition = new Block(new Scheme(), new CharPosition(-1, -1), new CharPosition(10, 10));
        assertThrows(IllegalArgumentException.class, blockWithIllegalDefinition::containsOnlyOneWord);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(10, 10));
        List<Block> expectedSubblocks = Arrays.asList(new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(5, 5)),
                new Block(new Scheme(), new CharPosition(6, 5), new CharPosition(10, 10)));
        List<Block> actualSubblocks = block.extractSequentialSubblocks();
        assertEquals(expectedSubblocks, actualSubblocks);
    }

    @Test
    public void testExtractSequentialSubblocksEdgeCases() {
        Block emptyBlock = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(0, 0));
        List<Block> expectedEmptySubblocks = Collections.emptyList();
        List<Block> actualEmptySubblocks = emptyBlock.extractSequentialSubblocks();
        assertEquals(expectedEmptySubblocks, actualEmptySubblocks);

        Block blockWithOneWord = new Block(new Scheme(), new CharPosition(0, 0), new CharPosition(10, 10));
        List<Block> expectedOneWordSubblocks = Collections.singletonList(block);
        List<Block> actualOneWordSubblocks = block.extractSequentialSubblocks();
        assertEquals(expectedOneWordSubblocks, actualOneWordSubblocks);

        Block blockWithImproperDimensions = new Block(new Scheme(), new CharPosition(-1, -1), new CharPosition(10, 10));
        assertThrows(IllegalArgumentException.class, blockWithImproperDimensions::extractSequentialSubblocks);
    }

    @Test
    public void testExtractParallelSubblocks() {
        // Test extractParallelSubblocks method here
    }

    @Test
    public void testExtractParallelSubblocksEdgeCases() {
        // Test extractParallelSubblocks edge cases here
    }
}