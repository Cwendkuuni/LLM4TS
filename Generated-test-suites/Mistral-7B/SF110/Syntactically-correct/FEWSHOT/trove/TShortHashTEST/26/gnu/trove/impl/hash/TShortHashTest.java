package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.procedure.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash hash = new TShortHash(initialCapacity);
        assertEquals(initialCapacity, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash.size());
        assertEquals(loadFactor, hash.getLoadFactor(), 0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = 1;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(initialCapacity, hash.size());
        assertEquals(loadFactor, hash.getLoadFactor(), 0);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithNegativeInitialCapacity() {
        TShortHash hash = new TShortHash(-1);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        int capacity = hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));

        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();

        TShortProcedure procedure = t -> assertTrue(t > 0);
        assertTrue(hash.forEach(procedure));

        hash.put((short) 0, (short) 0);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);

        procedure = t -> assertFalse(t < 0);
        assertTrue(hash.forEach(procedure));

        procedure = t -> assertFalse(t > 3);
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);

        int index = hash.index((short) 1);
        assertEquals(index, hash.removeAt(index));
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();

        assertFalse(hash.contains((short) 1));
        assertEquals(-1, hash.index((short) 1));

        hash.put((short) 1, (short) 1);
        int index = hash.index((short) 1);
        assertEquals(index, hash.removeAt(index));
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        assertEquals(1, hash.size());

        short key = (short) 2;
        int index = hash.insertKey(key);
        assertEquals(2, hash.size());
        assertEquals(key, hash.get(index));
        assertEquals(key, hash.get(index * TShortHash.HASH_SPREAD));
        assertEquals(1, hash.get(1));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(3);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        int index = hash.insertKey((short) 4);
        assertEquals(4, hash.size());
        assertEquals(4, hash.get(index));
        assertEquals(4, hash.get(index * TShortHash.HASH_SPREAD));
    }
}