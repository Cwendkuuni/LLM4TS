package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 2, 3, 4);
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new Block(scheme, p1, p2);
        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithSchemeOnly() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockThrowsIllegalArgumentExceptionWhenImproperBlockDefinition() {
        Scheme scheme = new Scheme(10, 10);
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, -2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 2, 3, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 2, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 2, 3, 10));
    }

    @Test
    public void testBlockSubblockThrowsAbstractMethodError() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            @Override
            public Block subblock(final int p0, final int p1, final int p2, final int p3) {
                throw new AbstractMethodError();
            }
        };
        assertThrows(AbstractMethodError.class, () -> block.subblock(1, 2, 3, 4));
    }

    @Test
    public void testBlockCharAtThrowsIllegalArgumentExceptionWhenOutOfBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 2, 3, 4);
        assertThrows(IllegalArgumentException.class, () -> block.charAt(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(4, 2));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(3, -1));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(3, 5));
    }

    @Test
    public void testBlockTextAtThrowsIllegalArgumentExceptionWhenImproperSublineCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 2, 3, 4);
        assertThrows(IllegalArgumentException.class, () -> block.textAt(-1, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(5, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(3, -1, 4));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(3, 5, 4));
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        CharTable charTable = new CharTable(scheme) {
            @Override
            public char charAt(final int x, final int y) {
                return (char) (x + y);
            }
        };
        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(3, 4)) {
                    @Override
                    public List<Block> extractSequentialSubblocks() {
                        return Arrays.asList(new Block(this.scheme, new CharPosition(2, 3), new CharPosition(4, 4)));
                    }
                });
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(4, 4)));
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(new CharPosition(1, 2), new CharPosition(3, 2));
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(new CharPosition(3, 3), new CharPosition(4, 3));
            }
        };
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockDoesNotContainOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        CharTable charTable = new CharTable(scheme) {
            @Override
            public char charAt(final int x, final int y) {
                return (char) (x + y);
            }
        };
        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(3, 4)),
                        new Block(this.scheme, new CharPosition(5, 5), new CharPosition(7, 5)));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(5, 5)),
                        new Block(this.scheme, new CharPosition(6, 5), new CharPosition(7, 5)));
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(new CharPosition(1, 2), new CharPosition(3, 2));
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(new CharPosition(5, 5), new CharPosition(7, 5));
            }
        };
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testBlockExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        CharTable charTable = new CharTable(scheme) {
            @Override
            public char charAt(final int x, final int y) {
                return (char) (x + y);
            }
        };
        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(3, 4)),
                        new Block(this.scheme, new CharPosition(5, 5), new CharPosition(7, 5)));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(5, 5)),
                        new Block(this.scheme, new CharPosition(6, 5), new CharPosition(7, 5)));
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(new CharPosition(1, 2), new CharPosition(3, 2));
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(new CharPosition(5, 5), new CharPosition(7, 5));
            }
        };
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        assertEquals(1, firstSubblock.x1);
        assertEquals(2, firstSubblock.y1);
        assertEquals(3, firstSubblock.x2);
        assertEquals(4, firstSubblock.y2);
        Block secondSubblock = subblocks.get(1);
        assertEquals(5, secondSubblock.x1);
        assertEquals(5, secondSubblock.y1);
        assertEquals(7, secondSubblock.x2);
        assertEquals(5, secondSubblock.y2);
    }

    @Test
    public void testBlockExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        CharTable charTable = new CharTable(scheme) {
            @Override
            public char charAt(final int x, final int y) {
                return (char) (x + y);
            }
        };
        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(3, 4)),
                        new Block(this.scheme, new CharPosition(5, 5), new CharPosition(7, 5)));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(5, 5)),
                        new Block(this.scheme, new CharPosition(6, 5), new CharPosition(7, 5)));
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(new CharPosition(1, 2), new CharPosition(3, 2));
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(new CharPosition(5, 5), new CharPosition(7, 5));
            }
        };
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        Block firstSubblock = subblocks.get(0);
        assertEquals(1, firstSubblock.x1);
        assertEquals(2, firstSubblock.y1);
        assertEquals(5, firstSubblock.x2);
        assertEquals(5, firstSubblock.y2);
        Block secondSubblock = subblocks.get(1);
        assertEquals(6, secondSubblock.x1);
        assertEquals(5, secondSubblock.y1);
        assertEquals(7, secondSubblock.x2);
        assertEquals(5, secondSubblock.y2);
    }

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(10, 10);
        CharTable charTable = new CharTable(scheme) {
            @Override
            public char charAt(final int x, final int y) {
                return (char) (x + y);
            }
        };
        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(3, 4)),
                        new Block(this.scheme, new CharPosition(5, 5), new CharPosition(7, 5)));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(5, 5)),
                        new Block(this.scheme, new CharPosition(6, 5), new CharPosition(7, 5)));
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(new CharPosition(1, 2), new CharPosition(3, 2));
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(new CharPosition(5, 5), new CharPosition(7, 5));
            }
        };
        String expectedOutput = "Block(0,0:, 1,2):\n" +
                "# 12 # # 34 # # 56 #\n" +
                "# # 35 # # 45 # # 65 #\n" +
                "# # # 46 # # 56 # # 76 #\n" +
                "# # # # 57 # # 67 # # 77 #\n" +
                "# # # # # 78 # # 88 # #\n" +
                " # # # # # # # # # # #\n" +
                "# # # # # # # # # # #\n" +
                "# # # # # # # # # # #\n" +
                "# # # # # # # # # # #\n" +
                "# # # # # # # # # # #\n" +
                "Block(1,2:1234, 5,5):\n" +
                "# 57 # # 67 # # 77 #\n" +
                "# # # 68 # # 78 # # 88 #\n" +
                "Block(6,5:67, 7,5):\n" +
                "# 67 # # 77 # #\n" +
                "# # # 78 # #";
        assertEquals(expectedOutput, block.toString());
    }

    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        CharTable charTable = new CharTable(scheme) {
            @Override
            public char charAt(final int x, final int y) {
                return (char) (x + y);
            }
        };
        Block block = new Block(scheme) {
            @Override
            public List<Block> extractSequentialSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(3, 4)),
                        new Block(this.scheme, new CharPosition(5, 5), new CharPosition(7, 5)));
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return Arrays.asList(new Block(this.scheme, new CharPosition(1, 2), new CharPosition(5, 5)),
                        new Block(this.scheme, new CharPosition(6, 5), new CharPosition(7, 5)));
            }

            @Override
            public HWordPosition firstWord_Horizontally() {
                return new HWordPosition(new CharPosition(1, 2), new CharPosition(3, 2));
            }

            @Override
            public WordPosition lastWord_Horizontally() {
                return new WordPosition(new CharPosition(5, 5), new CharPosition(7, 5));
            }
        };
        assertEquals("1234", block.getUpperLine());
    }
}