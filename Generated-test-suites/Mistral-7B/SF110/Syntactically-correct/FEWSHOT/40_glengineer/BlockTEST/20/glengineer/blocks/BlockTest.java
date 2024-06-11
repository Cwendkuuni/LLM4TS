package glengineer.blocks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
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
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionX2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 10, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY2() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 3, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionXY() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 11, 3, 3);
    }

    @Test
    public void testSubblockConstruction() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        Block subBlock = parentBlock.subblock(1, 2, 2, 2);
        assertEquals(1, subBlock.getX1());
        assertEquals(2, subBlock.getY1());
        assertEquals(3, subBlock.getX2());
        assertEquals(3, subBlock.getY2());
        assertEquals(scheme, subBlock.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinition() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(-1, -1, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionX() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(1, 1, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionY() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(1, 1, 3, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSubblockDefinitionXY() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.subblock(4, 4, 1, 1);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals('A', block.charAt(1, 1));
        assertEquals('C', block.charAt(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.charAt(4, 4);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String text = "The quick brown fox";
        scheme.setLines(Arrays.asList(text.split("\\s+")));
        assertEquals("The quick", block.textAt(1, 2, 1));
        assertEquals("quick brown", block.textAt(1, 4, 1));
        assertEquals("quick brown fox", block.textAt(1, 5, 1));
        assertEquals("fox", block.textAt(3, 4, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSublineCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.textAt(-1, 3, 1);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        String text = "The quick brown fox";
        scheme.setLines(Arrays.asList(text.split("\\s+")));
        assertTrue(block.containsOnlyOneWord());
        block.setX1(1);
        block.setY1(2);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        assertEquals(1, firstSubblock.getX1());
        assertEquals(1, firstSubblock.getY1());
        assertEquals(3, firstSubblock.getX2());
        assertEquals(2, firstSubblock.getY2());

        Block secondSubblock = subblocks.get(1);
        assertEquals(3, secondSubblock.getX1());
        assertEquals(1, secondSubblock.getY1());
        assertEquals(4, secondSubblock.getX2());
        assertEquals(2, secondSubblock.getY2());

        Block thirdSubblock = subblocks.get(2);
        assertEquals(4, thirdSubblock.getX1());
        assertEquals(1, thirdSubblock.getY1());
        assertEquals(5, thirdSubblock.getX2());
        assertEquals(5, thirdSubblock.getY2());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());

        Block firstSubblock = subblocks.get(0);
        assertEquals(1, firstSubblock.getX1());
        assertEquals(1, firstSubblock.getY1());
        assertEquals(5, firstSubblock.getX2());
        assertEquals(1, firstSubblock.getY2());

        Block secondSubblock = subblocks.get(1);
        assertEquals(1, secondSubblock.getX1());
        assertEquals(2, secondSubblock.getY1());
        assertEquals(5, secondSubblock.getX2());
        assertEquals(5, secondSubblock.getY2());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        assertEquals("Block(0,0:)\n# The quick brown fox #\n# h e l l o #\n# w o r l d #\n# j u m p #\n# f o x #\nBlock(5,5:\n", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        assertEquals("The quick brown fox", block.getUpperLine());
    }
}