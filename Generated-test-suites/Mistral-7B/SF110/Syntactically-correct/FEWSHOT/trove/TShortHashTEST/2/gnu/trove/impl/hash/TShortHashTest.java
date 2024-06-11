package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashDefaultConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.loadFactor(), 0.01f);
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        short noEntryValue = 123;
        TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
        assertEquals(0, hash.size());
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.loadFactor(), 0.01f);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        short customNoEntryValue = 456;
        hash = new TShortHash();
        hash.setNoEntryValue(customNoEntryValue);
        assertEquals(customNoEntryValue, hash.getNoEntryValue());
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
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals(value, (short) 1);
                return true;
            }
        };
        hash.put((short) 1, (short) 1);
        hash.forEach(procedure);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TShortHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TShortHash(10, -0.5f);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        hash.removeAt(0);

        assertEquals(2, hash.size());
        assertFalse(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(1, hash.index((short) 1));
        assertEquals(2, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        hash.put((short) 5, (short) 5);

        assertEquals(1, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 6));

        int index = hash.index((short) 6);
        assertTrue(index < 0);
        assertEquals(2, index % hash._set.length);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        assertEquals(-2, hash.insertKey((short) 0));
        assertEquals(-2, hash.insertKey((short) 4));
        assertEquals(-1, hash.insertKey((short) 1)); // already exists
        assertEquals(0, hash.insertKey((short) 0));
        assertEquals(1, hash.size());

        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        hash.put((short) 5, (short) 5);

        assertEquals(5, hash.size());

        assertEquals(-1, hash.insertKey((short) 5)); // already exists
        assertEquals(1, hash.insertKey((short) 6));
        assertEquals(2, hash.size());

        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
        assertTrue(hash.contains((short) 5));
        assertTrue(hash.contains((short) 6));
    }
}