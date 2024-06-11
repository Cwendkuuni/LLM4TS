package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import gnu.trove.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.impl.hash.TShortHash.Constants;

public class TShortHashTest {

    @Test
    public void testEmptyTShortHash() {
        TShortHash hash = new TShortHash(5);
        assertTrue(hash.isEmpty());
        assertEquals(0, hash.size());
        assertFalse(hash.contains((short) 1));
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testDefaultTShortHash() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.isEmpty());
        assertEquals(Constants.DEFAULT_SHORT_INITIAL_CAPACITY, hash.size());
        assertFalse(hash.contains((short) 1));
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertFalse(hash.contains((short) 1));
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 1);
        assertEquals(10, hash.size());
        assertFalse(hash.contains((short) 1));
        assertEquals((short) 1, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashPutAndGet() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        assertEquals(5, hash.size());

        assertEquals((short) 1, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));
        assertEquals((short) 4, hash.get((short) 4));
        assertEquals((short) 5, hash.get((short) 5));

        assertNull(hash.get((short) 6));
    }

    @Test
    public void testTShortHashPutAndGetWithNoEntryValue() {
        TShortHash hash = new TShortHash(5, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        assertEquals(5, hash.size());

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));
        assertEquals((short) 4, hash.get((short) 4));
        assertEquals((short) 5, hash.get((short) 5));

        assertNull(hash.get((short) 6));
    }

    @Test
    public void testTShortHashContains() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
        assertTrue(hash.contains((short) 5));

        assertFalse(hash.contains((short) 6));
    }

    @Test
    public void testTShortHashForEach() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        int count = 0;
        hash.forEach((TShortProcedure) (Short short1) -> count++);

        assertEquals(5, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashNegativeInitialCapacity() {
        new TShortHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashNegativeLoadFactor() {
        new TShortHash(5, -0.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashNegativeNoEntryValue() {
        new TShortHash(5, (short) -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTShortHashIndexOutOfBounds() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.removeAt(10);
    }

    @Test(expected = NullPointerException.class)
    public void testTShortHashNullProcedure() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.forEach(null);
    }
}