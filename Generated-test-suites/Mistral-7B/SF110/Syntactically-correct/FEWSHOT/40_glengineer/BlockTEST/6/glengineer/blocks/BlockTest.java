package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructionWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new Block(scheme, p1, p2);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructionWithSchemeOnly() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, -1, 1, 11, 10);
        fail("Expected IllegalArgumentException but got: " + block);
    }

    @Test
    public void testBlockSubblockWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        Block subblock = parentBlock.subblock(1, 1, 3, 3);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(scheme, subblock.scheme);
        assertEquals(parentBlock, subblock.parentBlock());
    }

    @Test
    public void testBlockSubblockWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        try {
            parentBlock.subblock(-1, 1, 3, 3);
            fail("Expected IllegalArgumentException but got: " + parentBlock);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            parentBlock.subblock(1, -1, 3, 3);
            fail("Expected IllegalArgumentException but got: " + parentBlock);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            parentBlock.subblock(1, 1, 3, 6);
            fail("Expected IllegalArgumentException but got: " + parentBlock);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        for (int x = 0; x < block.x2 - block.x1 + 1; ++x) {
            for (int y = 0; y < block.y2 - block.y1 + 1; ++y) {
                char expectedChar = (char) (x + y % 2 + 'A');
                char actualChar = block.charAt(x, y);
                assertEquals(expectedChar, actualChar);
            }
        }
    }

    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String expectedText = "ABCDE";
        String actualText = block.textAt(1, 5, 0);
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Collections.singletonList(this);
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Collections.singletonList(this);
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(1, 0, 5, 0);
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(5, 0, 5, 5);
            }
        };
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockWithMultipleWords() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(
                    new Block(scheme, 1, 1, 3, 2) {
                        @Override
                        public HWordPosition firstWord_Horizontally() {
                            return new HWordPosition(1, 0, 3, 0);
                        }

                        @Override
                        public WordPosition lastWord_Horizontally() {
                            return new WordPosition(3, 0, 3, 2);
                        }
                    },
                    new Block(scheme, 3, 2, 5, 2) {
                        @Override
                        public HWordPosition firstWord_Horizontally() {
                            return new HWordPosition(3, 0, 5, 2);
                        }

                        @Override
                        public WordPosition lastWord_Horizontally() {
                            return new WordPosition(5, 2, 5, 2);
                        }
                    }
                );
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Collections.emptyList();
            }
        };
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String expectedString = "Block(1,1,5,5):\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "#--------------------#\n";
        String actualString = block.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String expectedUpperLine = "ABCDE";
        String actualUpperLine = block.getUpperLine();
        assertEquals(expectedUpperLine, actualUpperLine);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = parentBlock.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        for (int i = 0; i < subblocks.size(); ++i) {
            Block subblock = subblocks.get(i);
            assertEquals(i + 1, subblock.x1);
            assertEquals(i + 1, subblock.y1);
            assertEquals(i + 2, subblock.x2);
            assertEquals(i + 2, subblock.y2);
        }
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block parentBlock = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = parentBlock.extractParallelSubblocks();
        assertEquals(0, subblocks.size());
    }
}