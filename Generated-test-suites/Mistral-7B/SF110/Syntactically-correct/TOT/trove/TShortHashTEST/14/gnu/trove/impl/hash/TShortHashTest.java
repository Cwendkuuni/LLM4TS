package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link TShortHash}.
 */
public class TShortHashTest {

    @Test
    public void testEmptyHash() {
        TShortHash hash = new TShortHash(5);
        assertEquals(0, hash.size());
        assertFalse(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
        assertFalse(hash.contains((short) 3));
        assertFalse(hash.contains((short) 4));
        assertFalse(hash.contains((short) 5));
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new short[5], hash._set);
    }

    @Test
    public void testInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new short[10], hash._set);
        // filling the hash
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);
        hash.put((short) 6);
        hash.put((short) 7);
        hash.put((short) 8);
        hash.put((short) 9);
        // checking the contents
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
        assertTrue(hash.contains((short) 5));
        assertTrue(hash.contains((short) 6));
        assertTrue(hash.contains((short) 7));
        assertTrue(hash.contains((short) 8));
        assertTrue(hash.contains((short) 9));
        assertFalse(hash.contains((short) 10));
        assertArrayEquals(new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, hash._set);
    }

    @Test
    public void testLoadFactor() {
        TShortHash hash = new TShortHash(3, 0.75f);
        assertEquals(4, hash.size());
        // filling the hash
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        // checking the contents
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertFalse(hash.contains((short) 4));
        assertFalse(hash.contains((short) 5));
        assertFalse(hash.contains((short) 6));
        assertArrayEquals(new short[] { 0, 1, 2, 3 }, hash._set);
        // resizing the hash
        hash.put((short) 4);
        hash.put((short) 5);
        hash.put((short) 6);
        // checking the contents
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
        assertTrue(hash.contains((short) 5));
        assertTrue(hash.contains((short) 6));
        assertFalse(hash.contains((short) 7));
        assertFalse(hash.contains((short) 8));
        assertFalse(hash.contains((short) 9));
        assertArrayEquals(new short[] { 0, 1, 2, 3, 4, 5, 6 }, hash._set);
    }

    @Test
    public void testNoEntryValue() {
        TShortHash hash = new TShortHash(5, (short) 10);
        assertEquals((short) 10, hash.getNoEntryValue());
        Arrays.fill(hash._set, (short) 10);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TShortHash hash = new TShortHash(5);
        // filling the hash
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        // trying to insert a new key
        hash.put((short) 5);
    }

    @Test
    public void testRemoveKey() {
        TShortHash hash = new TShortHash(5);
        // filling the hash
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        // removing a key
        hash.remove((short) 1);
        // checking the contents
        assertFalse(hash.contains((short) 1));
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
        assertArrayEquals(new short[] { 0, 2, 3, 4 }, hash._set);
    }

    @Test
    public void testForEachProcedure() {
        TShortHash hash = new TShortHash(5);
        // filling the hash
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        // checking the procedure execution
        short count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short shortValue) {
                count++;
                return true;
            }
        });
        assertEquals(5, count);
    }

    @Test
    public void testContainsAll() {
        TShortHash hash = new TShortHash(5);
        // filling the hash
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        // checking the containsAll method
        short[] values = new short[] { 0, 1, 2, 3, 4 };
        assertTrue(hash.containsAll(Arrays.asList(values)));
        // adding an extra value
        hash.put((short) 5);
        assertFalse(hash.containsAll(Arrays.asList(values)));
    }

}