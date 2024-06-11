package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertNull(hash.contains((short) 1));
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertNull(hash.contains((short) 1));
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.5f);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertNull(hash.contains((short) 1));
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(10, 0.5f, (short) 100);
        assertEquals(10, hash.capacity());
        assertEquals(100, hash.getNoEntryValue());
        assertEquals(0, hash.size());
        assertNull(hash.contains((short) 1));
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(10);
        byte[] states = hash._states;
        short[] set = hash._set;
        int capacity = hash.setUp(10);
        assertEquals(capacity, 10);
        assertEquals(Arrays.toString(set), "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");
        assertEquals(Arrays.toString(states), "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");
    }

    @Test
    public void testHasKey() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertFalse(hash.contains((short) 3));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals(value, (short) 1);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TShortHash hash = new TShortHash(5);
        for (int i = 0; i < hash.capacity(); i++) {
            hash.insertKey((short) i);
        }
        hash.insertKey((short) 10);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash(5);
        hash.insertKeyAt(0, (short) 1);
        short[] set = hash._set;
        byte[] states = hash._states;
        assertEquals(Arrays.toString(set), "[1, 0, 0, 0, 0]");
        assertEquals(Arrays.toString(states), "[1, 0, 0, 0, 0]");
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.removeAt(1);
        short[] set = hash._set;
        byte[] states = hash._states;
        assertEquals(Arrays.toString(set), "[1, 3, 0]");
        assertEquals(Arrays.toString(states), "[1, 0, 0]");
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        assertEquals(hash.index((short) 1), 0);
        assertEquals(hash.index((short) 2), 1);
        assertEquals(hash.index((short) 3), 2);
        assertEquals(hash.index((short) 4), -1);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.insertKey((short) 4);
        assertEquals(hash.indexRehashed((short) 1, 0, 1, (byte) 0), 0);
        assertEquals(hash.indexRehashed((short) 2, 0, 1, (byte) 0), 1);
        assertEquals(hash.indexRehashed((short) 3, 0, 1, (byte) 0), 2);
        assertEquals(hash.indexRehashed((short) 4, 0, 1, (byte) 0), 3);
        assertEquals(hash.indexRehashed((short) 5, 0, 1, (byte) 0), -1);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        assertEquals(hash.insertKey((short) 1), 0);
        assertEquals(hash.size(), 1);
        assertEquals(hash.getNoEntryValue(), (short) 0);
        assertEquals(hash.contains((short) 1), true);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(5);
        assertEquals(hash.insertKey((short) 1), 0);
        assertEquals(hash.insertKey((short) 2), 1);
        assertEquals(hash.size(), 2);
        assertEquals(hash.getNoEntryValue(), (short) 0);
        assertEquals(hash.contains((short) 1), true);
        assertEquals(hash.contains((short) 2), true);
    }
}