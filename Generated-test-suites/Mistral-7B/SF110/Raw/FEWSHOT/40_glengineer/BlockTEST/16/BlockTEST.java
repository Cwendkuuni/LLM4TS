package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4);
        assertEquals(1, block.getX1());
        assertEquals(2, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(4, block.getY2());
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.getX1());
        assertEquals(p1.y, block.getY1());
        assertEquals(p2.x, block.getX2());
        assertEquals(p2.y, block.getY2());
    }

    @Test
    public void testBlockConstructionWithSchemeOnly() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.x2, block.getX2());
        assertEquals(scheme.y2, block.getY2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinition() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 2, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionX2() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 5, 2, 6, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY1() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, -1, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperBlockDefinitionY2() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, 1, 2, 3, 5);
    }

    @Test
    public void testSubblockConstruction() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(2, subblock.getY2());
        assertEquals(scheme, subblock.getScheme());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractSubblockConstruction() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new EmptyBlock(scheme);
        block.subblock(1, 1, 2, 2);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4);
        String text = "abcdef";
        scheme.textAt(0, scheme.x2, 2) = text.charAt(0);
        scheme.textAt(0, scheme.x2, 3) = text.charAt(1);
        scheme.textAt(0, scheme.x2, 4) = text.charAt(2);

        assertEquals("abc", block.textAt(1, 3, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImproperSublineCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4);
        block.textAt(1, 1, 2);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme) {{
            scheme.lines[0] = "hello world".toCharArray();
        }};
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testDoesNotContainOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme) {{
            scheme.lines[0] = "hello".toCharArray();
            scheme.lines[1] = "world".toCharArray();
        }};
        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotSearchInATrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new EmptyBlock(scheme);
        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 4, 3) {{
            scheme.lines[0] = "abcd".toCharArray();
            scheme.lines[1] = "efgh".toCharArray();
            scheme.lines[2] = "ijk".toCharArray();
        }};

        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());

        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.getX1());
        assertEquals(1, subblock1.getY1());
        assertEquals(2, subblock1.getX2());
        assertEquals(1, subblock1.getY2());

        Block subblock2 = subblocks.get(1);
        assertEquals(2, subblock2.getX1());
        assertEquals(1, subblock2.getY1());
        assertEquals(4, subblock2.getX2());
        assertEquals(1, subblock2.getY2());

        Block subblock3 = subblocks.get(2);
        assertEquals(1, subblock3.getX1());
        assertEquals(2, subblock3.getY1());
        assertEquals(4, subblock3.getX2());
        assertEquals(3, subblock3.getY2());

        assertEquals(scheme, subblock1.getScheme());
        assertEquals(scheme, subblock2.getScheme());
        assertEquals(scheme, subblock3.getScheme());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 4, 3) {{
            scheme.lines[0] = "abcd".toCharArray();
            scheme.lines[1] = "efgh".toCharArray();
            scheme.lines[2] = "ijk".toCharArray();
        }};

        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(3, subblocks.size());

        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.getX1());
        assertEquals(1, subblock1.getY1());
        assertEquals(4, subblock1.getX2());
        assertEquals(3, subblock1.getY2());

        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.getX1());
        assertEquals(1, subblock2.getY1());
        assertEquals(4, subblock2.getX2());
        assertEquals(2, subblock2.getY2());

        Block subblock3 = subblocks.get(2);
        assertEquals(1, subblock3.getX1());
        assertEquals(2, subblock3.getY1());
        assertEquals(4, subblock3.getX2());
        assertEquals(3, subblock3.getY2());

        assertEquals(scheme, subblock1.getScheme());
        assertEquals(scheme, subblock2.getScheme());
        assertEquals(scheme, subblock3.getScheme());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4) {{
            scheme.lines[0] = "abc".toCharArray();
            scheme.lines[1] = "def".toCharArray();
            scheme.lines[2] = "ghi".toCharArray();
        }};

        String expected = "Block(0, 0, 4, 3):\n" +
                "# # a # b # c # # # # # # # # # # # # # # # # \n" +
                "# # d # e # f # # # # # # # # # # # # # # # # \n" +
                "# # g # h # i # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # \n" +
                "Block(1, 2, 3, 4):\n";

        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 2, 3, 4) {{
            scheme.lines[0] = "abcdef".toCharArray();
        }};
        assertEquals("abcdef", block.getUpperLine());
    }
}