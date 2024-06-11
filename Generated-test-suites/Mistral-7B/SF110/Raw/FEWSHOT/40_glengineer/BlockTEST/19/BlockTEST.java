package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructionWithBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        assertEquals(5, block.x2 - block.x1);
        assertEquals(5, block.y2 - block.y1);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p2.x - p1.x, block.x2 - block.x1);
        assertEquals(p2.y - p1.y, block.y2 - block.y1);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithSinglePosition() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithInvalidBounds() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, -1, 1, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithNegativeBounds() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, -1, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithBoundsBeyondSchemeSize() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 11, 11, 15, 15);
    }

    @Test
    public void testSubblockConstruction() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5) {
            @Override
            public Block subblock(final int p0, final int p1, final int p2, final int p3) {
                return new Block(scheme, p0, p1, p2, p3) {
                    @Override
                    public List<Block> extractSequentialSubblocks() {
                        return Arrays.asList(new Block(scheme, 1, 1, 2, 2), new Block(scheme, 3, 1, 4, 2));
                    }

                    @Override
                    public List<Block> extractParallelSubblocks() {
                        return Arrays.asList(new Block(scheme, 1, 1, 5, 2), new Block(scheme, 1, 3, 5, 2));
                    }
                };
            }
        };
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals(new Block(scheme, 1, 1, 2, 2), subblocks.get(0));
        assertEquals(new Block(scheme, 3, 1, 4, 2), subblocks.get(1));

        subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals(new Block(scheme, 1, 1, 5, 2), subblocks.get(0));
        assertEquals(new Block(scheme, 1, 3, 5, 2), subblocks.get(1));
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 1) {
            @Override
            public WordPosition firstWord_Horizontally() {
                return new WordPosition(1, 1, "hello");
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(5, 1, "world");
            }
        };
        assertTrue(block.containsOnlyOneWord());

        block = new Block(scheme, 1, 1, 6, 2) {
            @Override
            public WordPosition firstWord_Horizontally() {
                return new WordPosition(1, 1, "hello");
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(5, 1, "world");
            }
        };
        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordOnTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme) {
            @Override
            public WordPosition firstWord_Horizontally() {
                return null;
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return null;
            }
        }.containsOnlyOneWord();
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 2) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(scheme, 1, 1, 3, 1), new Block(scheme, 4, 1, 3, 1), new Block(scheme, 7, 1, 3, 1));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(scheme, 1, 1, 3, 2), new Block(scheme, 1, 3, 3, 2));
            }
        };
        String expectedOutput = "Block(0,0,6,2):\n" +
                "# # h e l l o # #\n" +
                "# # e   w o r l d #\n" +
                "# # l #           #\n" +
                "# # o #           #\n" +
                "# # w #           #\n" +
                "# # r # #         #\n" +
                "# # d # #         #\n" +
                "-----------------\n";
        assertEquals(expectedOutput, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 2) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(scheme, 1, 1, 3, 1), new Block(scheme, 4, 1, 3, 1), new Block(scheme, 7, 1, 3, 1));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(scheme, 1, 1, 3, 2), new Block(scheme, 1, 3, 3, 2));
            }
        };
        assertEquals("h e l l o", block.getUpperLine());
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 2);
        assertEquals('h', block.charAt(1, 1));
        assertEquals('l', block.charAt(1, 3));
        assertEquals('d', block.charAt(5, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 2);
        block.charAt(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 2);
        block.charAt(12, 1);
    }
}