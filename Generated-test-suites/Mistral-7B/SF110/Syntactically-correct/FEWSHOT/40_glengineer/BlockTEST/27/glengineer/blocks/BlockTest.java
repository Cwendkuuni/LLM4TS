package glengineer.blocks;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructionWithInvalidArguments() {
        Scheme scheme = new Scheme(10, 10);
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, -1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 1, -1));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 12, 1, 1, 1)); // wider than scheme
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 12, 1, 1)); // taller than scheme
    }

    @Test
    public void testBlockConstructionWithValidArguments() {
        Scheme scheme = new Scheme(10, 10);
        Block block1 = new Block(scheme, 1, 1, 5, 5);
        assertEquals(5, block1.x2 - block1.x1);
        assertEquals(5, block1.y2 - block1.y1);
        assertEquals(scheme, block1.scheme);

        Block block2 = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        assertEquals(block1, block2);

        Block block3 = new Block(scheme);
        assertEquals(scheme.x2, block3.x2);
        assertEquals(scheme.y2, block3.y2);
    }

    @Test
    public void testSubblockConstructionWithInvalidArguments() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        assertThrows(IllegalArgumentException.class, () -> block.subblock(-1, 0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(0, -1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(0, 0, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(0, 0, 1, -1));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(5, 5, 1, 1)); // wider than block
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 5, 1, 1)); // taller than block
        assertThrows(IllegalArgumentException.class, () -> block.subblock(-1, -1, 1, 1)); // out of block
    }

    @Test
    public void testSubblockConstructionWithValidArguments() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(2, subblock.x2 - subblock.x1);
        assertEquals(2, subblock.y2 - subblock.y1);
        assertEquals(scheme, subblock.scheme);
        assertEquals(block, subblock.parentBlock);

        assertTrue(block.contains(subblock));
        assertTrue(subblock.isSubblockOf(block));
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        for (int x = 0; x <= 5; ++x) {
            for (int y = 0; y <= 5; ++y) {
                int position = x + y * (block.x2 - block.x1 + 1);
                char expected = (char) ('A' + position);
                assertEquals(expected, block.charAt(x, y));
            }
        }
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String text = "ABCDEF";
        for (int y = 0; y < block.y2 - block.y1; ++y) {
            String expected = text.substring(y * (block.x2 - block.x1)
                    , (y + 1) * (block.x2 - block.x1));
            assertEquals(expected, block.textAt(block.x1, block.x2, y));
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 3);
        assertTrue(block.containsOnlyOneWord());
        Block block2 = new Block(scheme, 1, 1, 6, 6);
        assertFalse(block2.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 6, 6);
        List<Block> expected = new ArrayList<>();
        expected.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(2, 1)));
        expected.add(new Block(scheme, new CharPosition(3, 1), new CharPosition(4, 1)));
        expected.add(new Block(scheme, new CharPosition(5, 1), new CharPosition(6, 1)));
        expected.add(new Block(scheme, new CharPosition(1, 2), new CharPosition(2, 2)));
        expected.add(new Block(scheme, new CharPosition(3, 2), new CharPosition(4, 2)));
        expected.add(new Block(scheme, new CharPosition(5, 2), new CharPosition(6, 2)));
        assertEquals(expected, block.extractSequentialSubblocks());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 3);
        List<Block> expected = new ArrayList<>();
        expected.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 1)));
        expected.add(new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 2)));
        expected.add(new Block(scheme, new CharPosition(1, 3), new CharPosition(3, 3)));
        List<Block> actual = block.extractParallelSubblocks();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String expected = "Block(0,0:,5,5):\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# A B C D E #\n" +
                "# # # # # # #\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String expected = "ABCDE";
        assertEquals(expected, block.getUpperLine());
    }
}