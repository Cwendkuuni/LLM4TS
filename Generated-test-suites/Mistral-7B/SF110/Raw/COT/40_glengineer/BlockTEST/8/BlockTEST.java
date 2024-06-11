import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithoutCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, -1, -1, 6, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 5, 5, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition3() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition4() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, scheme.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition5() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, scheme.x2, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition6() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, scheme.x2, scheme.y2);
    }

    @Test
    public void testSubblockMethodWithValidArgs() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(1, 1, 1, 1);
        assertNotNull(subblock);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockMethodWithImproperargs() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(-1, -1, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockMethodWithNegativeWidth() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(1, 1, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockMethodWithNegativeHeight() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(1, 1, 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockMethodWithOutOfBoundsArgs() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(1, 1, 4, 4);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractSequentialSubblocks();
        // Assuming extractSequentialSubblocks returns two subblocks
        assertEquals(2, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        Block secondSubblock = subblocks.get(1);
        // Assuming first subblock is located at (1,1) and (1,2)
        assertEquals(1, firstSubblock.x1);
        assertEquals(1, firstSubblock.y1);
        assertEquals(1, firstSubblock.x2);
        assertEquals(2, firstSubblock.y2);
        // Assuming second subblock is located at (2,1) and (3,2)
        assertEquals(2, secondSubblock.x1);
        assertEquals(1, secondSubblock.y1);
        assertEquals(3, secondSubblock.x2);
        assertEquals(2, secondSubblock.y2);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractParallelSubblocks();
        // Assuming extractParallelSubblocks returns two subblocks
        assertEquals(2, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        Block secondSubblock = subblocks.get(1);
        // Assuming first subblock is located at (1,1) and (3,1)
        assertEquals(1, firstSubblock.x1);
        assertEquals(1, firstSubblock.y1);
        assertEquals(3, firstSubblock.x2);
        assertEquals(1, firstSubblock.y2);
        // Assuming second subblock is located at (1,2) and (3,2)
        assertEquals(1, secondSubblock.x1);
        assertEquals(2, secondSubblock.y1);
        assertEquals(3, secondSubblock.x2);
        assertEquals(2, secondSubblock.y2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractParallelSubblocksForSingleCharacterBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, new CharPosition(1, 1));
        List<Block> subblocks = block.extractParallelSubblocks();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractSequentialSubblocksForSingleCharacterBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, new CharPosition(1, 1));
        List<Block> subblocks = block.extractSequentialSubblocks();
    }
}