package glengineer.blocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setup() {
        scheme = mock(Scheme.class);
        block = new Block(scheme, 1, 1, 3, 3) {
            @Override
            public Block subblock(final int p0, final int p1, final int p2, final int p3) {
                fail("Should not be called.");
                return null;
            }

            @Override
            public List<Block> extractSequentialSubblocks() {
                fail("Should not be called.");
                return Collections.emptyList();
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                fail("Should not be called.");
                return Collections.emptyList();
            }
        };
    }

    @Test
    public void testBlockConstruction() {
        assertEquals(scheme, block.scheme);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionIllegalArgs() {
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test
    public void testContainsOnlyOneWord() {
        String text = "This is a single word.";
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y1)))
                .thenReturn(text);
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y2)))
                .thenReturn(text);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y2)))
                .thenReturn("");
        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordMultipleWords() {
        String text1 = "First word.";
        String text2 = "Second word.";
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y1)))
                .thenReturn(text1);
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y2)))
                .thenReturn(text2);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testToString() {
        String expected = "Block(1, 1, 3, 3):\n-----------------\n # This is a word #\n # another word #\n -----------------\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        String expected = "This is a word";
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y1)))
                .thenReturn(expected);
        assertEquals(expected, block.getUpperLine());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUpperLineOutOfBounds() {
        when(scheme.textAt(eq(block.x1), eq(block.x2), eq(block.y1 - 1)))
                .thenThrow(new RuntimeException());
        throw new RuntimeException();
    }
}