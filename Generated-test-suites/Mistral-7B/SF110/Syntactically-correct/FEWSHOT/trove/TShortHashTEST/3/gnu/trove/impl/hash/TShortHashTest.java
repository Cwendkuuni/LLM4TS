package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testEmptyTShortHash() {
        TShortHash shortHash = new TShortHash();
        assertEquals(0, shortHash.size());
        assertNull(shortHash.getNoEntryValue());
        assertTrue(shortHash.isEmpty());
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash shortHash = new TShortHash(11);
        assertEquals(11, shortHash.size());
        assertNull(shortHash.getNoEntryValue());
        assertFalse(shortHash.isEmpty());
        short[] set = shortHash._set;
        assertEquals(0, set[0]);
        for (int i = 1; i < shortHash.size(); i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, set[i]);
        }
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash shortHash = new TShortHash(11, 0.75f);
        assertEquals(11, shortHash.size());
        assertNull(shortHash.getNoEntryValue());
        assertFalse(shortHash.isEmpty());
        short[] set = shortHash._set;
        assertEquals(0, set[0]);
        for (int i = 1; i < shortHash.size(); i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, set[i]);
        }
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash shortHash = new TShortHash(11, 0.75f, 1);
        assertEquals(11, shortHash.size());
        assertEquals(1, shortHash.getNoEntryValue());
        assertFalse(shortHash.isEmpty());
        short[] set = shortHash._set;
        assertEquals(1, set[0]);
        for (int i = 1; i < shortHash.size(); i++) {
            assertEquals(1, set[i]);
        }
    }

    @Test
    public void testSetUp() {
        TShortHash shortHash = new TShortHash(11);
        int capacity = shortHash.setUp(13);
        assertEquals(13, capacity);
    }

    @Test
    public void testContains() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        shortHash.insertKey(hashVal);
        assertTrue(shortHash.contains(hashVal));
    }

    @Test
    public void testForEach() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        shortHash.insertKey(hashVal);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals(hashVal, value);
                return true;
            }
        };
        assertTrue(shortHash.forEach(procedure));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullProcedure() {
        TShortHash shortHash = new TShortHash(5);
        TShortProcedure procedure = null;
        assertFalse(shortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        shortHash.insertKey(hashVal);
        int index = shortHash.index(hashVal);
        shortHash.removeAt(index);
        assertFalse(shortHash.contains(hashVal));
    }

    @Test
    public void testIndex() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        shortHash.insertKey(hashVal);
        int index = shortHash.index(hashVal);
        assertEquals(index, shortHash.size() - 1);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        shortHash.insertKey(hashVal);
        int index = shortHash.indexRehashed(hashVal, -1, 0, (byte) 0);
        assertFalse(index > -1);
    }

    @Test
    public void testInsertKey() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        int index = shortHash.insertKey(hashVal);
        assertTrue(shortHash.contains(hashVal));
        assertEquals(index, shortHash.size() - 1);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        int index = shortHash.insertKeyRehash(hashVal, -1, 0, (byte) 0);
        assertFalse(index > -1);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash shortHash = new TShortHash(5);
        short hashVal = 5;
        int index = shortHash.insertKey(hashVal);
        shortHash.insertKeyAt(index, 10);
        assertFalse(shortHash.contains(hashVal));
        assertTrue(shortHash.contains(10));
    }
}