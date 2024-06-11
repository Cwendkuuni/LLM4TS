package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void testBlockConstructionWithInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithCoordinatesOutsideScheme() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 6, 1, 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithInvalidX2Y2Coordinates() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 1, 3, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithX2GreaterThanSchemeX2() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 1, 6, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithY2GreaterThanSchemeY2() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 1, 3, 6);
    }

    @Test
    public void testSubblockConstructionWithInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertNull(block.subblock(-1, -1, 1, 1));
        assertNull(block.subblock(4, 4, 1, 1));
        assertNull(block.subblock(1, 1, 4, 4));
        assertNull(block.subblock(1, 1, 1, 4));
        assertNull(block.subblock(1, 4, 1, 1));
        assertNull(block.subblock(1, 1, 1, 4));
    }

    @Test
    public void testSubblockConstructionWithValidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block parentBlock = new Block(scheme, 1, 1, 3, 3);
        Block subblock = parentBlock.subblock(1, 1, 1, 1);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        String line = "abcdef";
        scheme.lines = new String[5];
        scheme.lines[0] = line;
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('c', block.charAt(1, 1));
        assertEquals('e', block.charAt(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBlock() {
        Scheme scheme = new Scheme(5, 5);
        String line = "abcdef";
        scheme.lines = new String[5];
        scheme.lines[0] = line;
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.charAt(4, 4);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "abc";
        String line2 = "defghi";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        scheme.lines[1] = line2;
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("def", block.textAt(0, 3, 1));
        assertEquals("ghi", block.textAt(0, 3, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtImproperSublineCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "abc";
        String line2 = "defghi";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        scheme.lines[1] = line2;
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.textAt(-1, 3, 0);
        block.textAt(1, 3, 5);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "word";
        String line2 = "word word";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        scheme.lines[1] = line2;
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertTrue(block.containsOnlyOneWord());
        Block block2 = new Block(scheme, 1, 1, 3, 4);
        assertFalse(block2.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordOnTrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "a b c";
        String line2 = "d e f g";
        String line3 = "h i j k l";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        scheme.lines[1] = line2;
        scheme.lines[2] = line3;
        Block parentBlock = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.x1);
        assertEquals(1, subblock1.y1);
        assertEquals(1, subblock1.x2);
        assertEquals(1, subblock1.y2);
        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.x1);
        assertEquals(2, subblock2.y1);
        assertEquals(2, subblock2.x2);
        assertEquals(2, subblock2.y2);
        Block subblock3 = subblocks.get(2);
        assertEquals(1, subblock3.x1);
        assertEquals(3, subblock3.y1);
        assertEquals(3, subblock3.x2);
        assertEquals(3, subblock3.y2);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "a b c";
        String line2 = "d e f g";
        String line3 = "h i j k l";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        scheme.lines[1] = line2;
        scheme.lines[2] = line3;
        Block parentBlock = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(3, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.x1);
        assertEquals(1, subblock1.y1);
        assertEquals(3, subblock1.x2);
        assertEquals(1, subblock1.y2);
        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.x1);
        assertEquals(2, subblock2.y1);
        assertEquals(3, subblock2.x2);
        assertEquals(2, subblock2.y2);
        Block subblock3 = subblocks.get(2);
        assertEquals(1, subblock3.x1);
        assertEquals(3, subblock3.y1);
        assertEquals(3, subblock3.x2);
        assertEquals(3, subblock3.y2);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "##abc##";
        String line2 = "##def##";
        String line3 = "##ghi##";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        scheme.lines[1] = line2;
        scheme.lines[2] = line3;
        Block parentBlock = new Block(scheme, 1, 1, 3, 3);
        assertEquals(
                "Block(1, 1, 3, 3):\n" +
                        "# # #\n" +
                        "# a b c #\n" +
                        "# # # \n" +
                        "# d e f #\n" +
                        "# # # \n" +
                        "# g h i #\n" +
                        "# # # \n",
                parentBlock.toString()
        );
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        String line1 = "##abc##";
        scheme.lines = new String[5];
        scheme.lines[0] = line1;
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals("##abc##", block.getUpperLine());
    }
}