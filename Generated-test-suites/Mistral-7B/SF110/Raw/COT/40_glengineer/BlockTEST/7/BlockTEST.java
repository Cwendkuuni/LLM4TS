import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(2, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(4, block.getY2());
    }

    @Test
    public void testBlockConstructionWithScheme() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme);
        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 5);
        new Block(scheme, -1, 2, 4, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithInvalidSize() {
        Scheme scheme = new Scheme(10, 5);
        new Block(scheme, new CharPosition(1, 2), new CharPosition(10, 6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithOutOfBoundsSize() {
        Scheme scheme = new Scheme(10, 5);
        new Block(scheme, new CharPosition(11, 6), new CharPosition(12, 7));
    }

    @Test
    public void testBlockSubblockConstruction() {
        Scheme scheme = new Scheme(10, 5);
        Block parentBlock = new Block(scheme);
        Block subblock = parentBlock.subblock(0, 1, 2, 3);
        assertFalse(subblock.equals(parentBlock));
        assertEquals(scheme, subblock.getScheme());
        assertEquals(0, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(3, subblock.getY2());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodCall() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                throw new UnsupportedOperationException();
            }
        };
        block.subblock(0, 1, 2, 3);
    }

    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4)) {
            @Override
            public char charAt(int x, int y) {
                return 'A';
            }
        };
        assertEquals('A', block.charAt(1, 2));
        try {
            block.charAt(-1, 2);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("char position -1,2 out of block.", e.getMessage());
        }
        try {
            block.charAt(5, 5);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("char position 5,5 out of block.", e.getMessage());
        }
    }

    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4)) {
            @Override
            public String textAt(int x1, int x2, int y) {
                return "test";
            }
        };
        assertEquals("test", block.textAt(1, 3, 2));
        try {
            block.textAt(-1, 2, 2);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
        try {
            block.textAt(5, 5, 2);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme) {
            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(1, 2, 2);
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(2, 3, 2);
            }
        };
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockContainsOnlyOneWord_False() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme) {
            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(1, 2, 2);
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(2, 3, 3);
            }
        };
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 5);
        Block parentBlock = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(10, subblocks.size());
        for (int i = 0; i < 10; ++i) {
            Block subblock = subblocks.get(i);
            assertEquals(i + 1, subblock.getX1());
            assertEquals(i, subblock.getY1());
            assertEquals(i + 1, subblock.getX2());
            assertEquals(i + 1, subblock.getY2());
        }
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 5);
        Block parentBlock = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(5, subblocks.size());
        for (int i = 0; i < 5; ++i) {
            Block subblock = subblocks.get(i);
            assertEquals(0, subblock.getX1());
            assertEquals(i, subblock.getY1());
            assertEquals(5, subblock.getX2());
            assertEquals(i + 1, subblock.getY2());
        }
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4)) {
            @Override
            public String toString() {
                return "MyCustomBlock";
            }
        };
        String expected = "Block(1,2,3,4):\n" + "# MyCustomBlock #\n" +
                          "# A #\n" + "# B #\n" + "# C #\n" + "# D #\n" + "# E #\n" +
                          "# # #\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 5);
        Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4)) {
            @Override
            public String textAt(int x1, int x2, int y) {
                return "test";
            }
        };
        assertEquals("test", block.getUpperLine());
    }
}