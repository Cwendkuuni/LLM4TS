package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.getX1());
        assertEquals(p1.y, block.getY1());
        assertEquals(p2.x, block.getX2());
        assertEquals(p2.y, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testConstructorWithSchemeOnly() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.x2, block.getX2());
        assertEquals(scheme.y2, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBlockDefinition() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, -1, -1, 3, 3);
    }

    @Test
    public void testIsTrivial() {
        Scheme scheme = new Scheme(1, 1);
        Block block = new Block(scheme, 1, 1, 1, 1);
        assertTrue(block.isTrivial());

        Block block1 = new Block(scheme, 1, 1, 2, 2);
        assertFalse(block1.isTrivial());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        Scheme scheme = new Scheme(1, 1);
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordImproperBlock() {
        Scheme scheme = new Scheme(1, 1);
        Block block = new Block(scheme, 1, 1, 2, 2);
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        String word = "hello world";
        CharSequence text = "hello world hello world";
        CharSequence line1 = "hello world";
        CharSequence line2 = "hello world";

        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(
                        new Block(scheme, 1, 1, 5, 1) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return line1;
                            }
                        },
                        new Block(scheme, 1, 1, 5, 1) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return line2;
                            }
                        }
                );
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(
                        new Block(scheme, 1, 1, 5, 5) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return text.subSequence(x1 - 1, x2).toString();
                            }
                        }
                );
            }
        };

        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        String word1 = "hello";
        String word2 = "world";

        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(
                        new Block(scheme, 1, 1, 5, 1) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return word1;
                            }
                        },
                        new Block(scheme, 1, 1, 5, 1) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return word2;
                            }
                        }
                );
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(
                        new Block(scheme, 1, 1, 5, 5) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return "hello world";
                            }
                        }
                );
            }
        };

        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals(word1, subblocks.get(0).getTextAt(0, word1.length(), 0));
        assertEquals(word2, subblocks.get(1).getTextAt(0, word2.length(), 0));
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        String word = "hello world";

        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(
                        new Block(scheme, 1, 1, 5, 5) {
                            @Override
                            public String textAt(int x1, int x2, int y) {
                                return word;
                            }
                        }
                );
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(
                        new Block(scheme, 1, 1, 1, 5),
                        new Block(scheme, 1, 1, 5, 1),
                        new Block(scheme, 1, 6, 5, 1),
                        new Block(scheme, 6, 1, 5, 1),
                        new Block(scheme, 6, 1, 1, 5)
                );
            }
        };

        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(5, subblocks.size());
        assertEquals("hello", subblocks.get(0).getTextAt(0, 5, 0));
        assertEquals("world", subblocks.get(4).getTextAt(0, 5, 0));
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(3, 3);
        Block block = new Block(scheme, 1, 1, 3, 3) {
            @Override
            public String getUpperLine() {
                return "---";
            }
        };

        String expected = "Block(1,1,3,3):\n--------------\n # hello #\n # world #\n # hello #\n--------------";
        assertEquals(expected, block.toString());
    }
}