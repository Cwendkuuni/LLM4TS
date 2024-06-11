package glengineer.blocks;
import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    @Test
    public void testBlockConstructionWithCharPosition() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
        assertEquals("Block(1,1,3,3):\n#################\n # .......... #\n # .......... #\n # .......... #\n # .......... #\n # .......... #\n ####################", block.toString());
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        String text = "Word1 Word2 Word1";
        scheme.setTextAt(1, 1, text);
        List<Block> subblocks = block.extractSequentialSubblocks();
        Block subblock = subblocks.get(0);
        assertTrue(subblock.containsOnlyOneWord());
        assertEquals("Word1 Word1", subblock.getTextAt(0, text.length()));
    }

    @Test
    public void testBlockConstructionWithScheme() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
        assertEquals("Block(0,0,5,5):\n#################\n # .......... #\n # .......... #\n # .......... #\n # .......... #\n # .......... #\n ####################", block.toString());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
        Block subblock = subblocks.get(0);
        assertEquals(4, subblock.getX2() - subblock.getX1());
        assertEquals(5, subblock.getY2() - subblock.getY1());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(5, subblocks.size());
        Block subblock = subblocks.get(0);
        assertEquals(1, subblock.getX2() - subblock.getX1());
        assertEquals(5, subblock.getY2() - subblock.getY1());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructorCalledWithInvalidParameters() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 6, 5);
    }

    @Test
    public void testBlockMethodsWhenBlockIsEmpty() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 1, 1);
        assertFalse(block.containsOnlyOneWord());
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    @Test
    public void testBlockMethodsWhenBlockIsTrivial() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 5, 1);
        assertFalse(block.containsOnlyOneWord());
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue(subblocks.isEmpty());
    }
}