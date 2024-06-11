package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.*;

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
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithSchemeOnly() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithImproperCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithImproperSchemeDimensions() {
        Scheme scheme = new Scheme(-5, 5);
        new Block(scheme, 1, 1, 3, 3);
    }

    @Test
    public void testBlockSubblockWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block parentBlock = new Block(scheme, 1, 1, 4, 4);
        Block subblock = parentBlock.subblock(1, 1, 1, 1);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String expected = "Block(1,1,3,3):\n#########\n # foo # #\n # bar # #\n # baz # #\n ########";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String expected = "foo";
        assertEquals(expected, block.getUpperLine());
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'f');
        block.setCharAt(2, 1, 'o');
        block.setCharAt(2, 2, 'o');
        block.setCharAt(3, 2, '\n');
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockDoesNotContainOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.setCharAt(1, 1, 'f');
        block.setCharAt(2, 1, 'o');
        block.setCharAt(2, 2, 'o');
        block.setCharAt(3, 2, ' ');
        block.setCharAt(3, 3, 'b');
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        parentBlock.setCharAt(1, 1, 'A');
        parentBlock.setCharAt(1, 2, 'B');
        parentBlock.setCharAt(1, 3, 'C');
        parentBlock.setCharAt(1, 4, 'D');
        parentBlock.setCharAt(1, 5, 'E');

        parentBlock.setCharAt(2, 1, 'F');
        parentBlock.setCharAt(2, 2, 'G');
        parentBlock.setCharAt(2, 3, 'H');
        parentBlock.setCharAt(2, 4, 'I');
        parentBlock.setCharAt(2, 5, 'J');

        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.x1);
        assertEquals(1, subblock1.y1);
        assertEquals(2, subblock1.x2);
        assertEquals(2, subblock1.y2);
        Block subblock2 = subblocks.get(1);
        assertEquals(3, subblock2.x1);
        assertEquals(3, subblock2.y1);
        assertEquals(5, subblock2.x2);
        assertEquals(5, subblock2.y2);
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        parentBlock.setCharAt(1, 1, 'A');
        parentBlock.setCharAt(1, 2, 'B');
        parentBlock.setCharAt(1, 3, 'C');
        parentBlock.setCharAt(1, 4, 'D');
        parentBlock.setCharAt(1, 5, 'E');

        parentBlock.setCharAt(2, 1, 'F');
        parentBlock.setCharAt(2, 2, 'G');
        parentBlock.setCharAt(2, 3, 'H');
        parentBlock.setCharAt(2, 4, 'I');
        parentBlock.setCharAt(2, 5, 'J');

        parentBlock.setCharAt(3, 1, 'K');
        parentBlock.setCharAt(3, 2, 'L');
        parentBlock.setCharAt(3, 3, 'M');
        parentBlock.setCharAt(3, 4, 'N');
        parentBlock.setCharAt(3, 5, 'O');

        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(3, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.x1);
        assertEquals(1, subblock1.y1);
        assertEquals(5, subblock1.x2);
        assertEquals(1, subblock1.y2);
        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.x1);
        assertEquals(3, subblock2.y1);
        assertEquals(5, subblock2.x2);
        assertEquals(3, subblock2.y2);
        Block subblock3 = subblocks.get(2);
        assertEquals(1, subblock3.x1);
        assertEquals(5, subblock3.y1);
        assertEquals(5, subblock3.x2);
        assertEquals(5, subblock3.y2);
    }
}