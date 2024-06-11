package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testEmptyTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new short[0], hash._set);
        assertTrue(hash.isEmpty());
        assertTrue(hash.contains(Short.MIN_VALUE));
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new short[10], hash._set);
        assertFalse(hash.isEmpty());
        assertTrue(hash.contains(Short.MIN_VALUE));
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new short[13], hash._set); // 13 is the next power of 2 > 10
        assertFalse(hash.isEmpty());
        assertTrue(hash.contains(Short.MIN_VALUE));
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 1);
        assertEquals(10, hash.size());
        assertEquals((short) 1, hash.getNoEntryValue());
        assertArrayEquals(new short[13], hash._set);
        assertFalse(hash.isEmpty());
        assertTrue(hash.contains((short) 0));
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(10);
        assertFalse(hash.contains((short) 1));
        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
        hash.remove((short) 1);
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        hash.removeAt(0);
        assertEquals(4, hash.size());
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        int index = hash.insertKey((short) 1);
        assertEquals(-1, index);
        assertEquals(5, hash.size());

        index = hash.insertKey((short) 5);
        assertEquals(5, index);
        assertEquals(6, hash.size());
        assertTrue(hash.contains((short) 5));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithNoFreeOrRemovedSlotsAvailable() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.insertKey((short) 4);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        int index = hash.index((short) 1);
        assertEquals(0, index);

        index = hash.index((short) 5);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        int index = hash.indexRehashed((short) 1, 0, HashFunctions.hash((short) 1), (byte) 1);
        assertEquals(0, index);

        index = hash.indexRehashed((short) 5, 0, HashFunctions.hash((short) 5), (byte) 1);
        assertEquals(-1, index);
    }
}