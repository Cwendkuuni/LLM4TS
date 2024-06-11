package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        assertEquals(2, block.getX1());
        assertEquals(3, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(7, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(2, 3);
        CharPosition p2 = new CharPosition(5, 7);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.getX(), block.getX1());
        assertEquals(p1.getY(), block.getY1());
        assertEquals(p2.getX(), block.getX2());
        assertEquals(p2.getY(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructionWithSchemeOnly() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, -1, 3, 5, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionX2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 2, 3, 10, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY1() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 2, -1, 5, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 2, 3, 5, 10);
    }

    @Test
    public void testBlockSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        Block subblock = parentBlock.subblock(1, 2, 3, 4);
        assertEquals(3, subblock.getX1());
        assertEquals(4, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
        assertEquals(scheme, subblock.getScheme());
        assertEquals(parentBlock, subblock.getParentBlock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinition() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        parentBlock.subblock(-1, 2, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionX1() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        parentBlock.subblock(1, -1, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionX2() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        parentBlock.subblock(1, 2, 5, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionY1() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        parentBlock.subblock(1, 2, 3, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionY2() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        parentBlock.subblock(1, 2, 3, 6);
    }

    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        assertEquals('X', block.charAt(3, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockCharAtOutOfBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        block.charAt(6, 6);
    }

    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        assertEquals("hello", block.textAt(3, 5, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockTextAtInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        block.textAt(6, 6, 3);
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockDoesNotContainOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        CharSequence text = "hello world";
        for (int y = 3; y <= 6; ++y) {
            for (int x = 2; x <= 5; ++x) {
                block.setCharAt(x, y, text.charAt(x - 2));
            }
        }
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        assertEquals(block.toString(), block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 2, 3, 5, 7);
        CharSequence lineText = "A B C D E F G H I J";
        for (int x = 2; x <= 5; ++x) {
            for (int y = 3; y <= 3; ++y) {
                block.setCharAt(x, y, lineText.charAt(x - 2));
            }
        }
        assertEquals(lineText.subSequence(0, 10), block.getUpperLine().toCharArray());
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals(3, subblocks.get(0).getX1());
        assertEquals(3, subblocks.get(0).getY1());
        assertEquals(4, subblocks.get(0).getX2());
        assertEquals(4, subblocks.get(0).getY2());
        assertEquals(4, subblocks.get(1).getX1());
        assertEquals(4, subblocks.get(1).getY1());
        assertEquals(5, subblocks.get(1).getX2());
        assertEquals(5, subblocks.get(1).getY2());
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 2, 3, 5, 7);
        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(3, subblocks.size());
        assertEquals(3, subblocks.get(0).getX1());
        assertEquals(3, subblocks.get(0).getY1());
        assertEquals(4, subblocks.get(0).getX2());
        assertEquals(4, subblocks.get(0).getY2());
        assertEquals(4, subblocks.get(1).getX1());
        assertEquals(4, subblocks.get(1).getY1());
        assertEquals(5, subblocks.get(1).getX2());
        assertEquals(5, subblocks.get(1).getY2());
        assertEquals(5, subblocks.get(2).getX1());
        assertEquals(5, subblocks.get(2).getY1());
        assertEquals(6, subblocks.get(2).getX2());
        assertEquals(6, subblocks.get(2).getY2());
    }
}