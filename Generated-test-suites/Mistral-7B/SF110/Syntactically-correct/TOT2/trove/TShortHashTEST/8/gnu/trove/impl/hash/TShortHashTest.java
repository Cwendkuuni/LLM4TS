package gnu.trove.impl.hash.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.procedure.TShortProcedureAdaptor;
import java.util.Arrays;
import static java.util.Arrays.asList;

public class TShortHashTest {
    @Test
    public void testTShortHashDefaultConstructor() {
        TShortHash hash = new TShortHash();
        // Check if the hash table is empty
        assertTrue(hash.isEmpty());
        // Check if the no_entry_value is set correctly
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        // Check if the hash table has the correct initial capacity
        assertEquals(10, hash.size());
        // Check if the no_entry_value is set correctly
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        // Check if the hash table has the correct initial capacity and load factor
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
        // Check if the no_entry_value is set correctly
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        short noEntryValue = 127;
        TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
        // Check if the hash table has the correct initial capacity, load factor, and no_entry_value
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithNegativeInitialCapacity() {
        new TShortHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithNegativeLoadFactor() {
        new TShortHash(10, -0.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithEmptyInitialCapacity() {
        new TShortHash(0);
    }

    @Test
    public void testTShortHashWithLargeLoadFactor() {
        TShortHash hash = new TShortHash(10, 1.2f);
        hash.put(1);
        hash.put(2);
        hash.put(3);
        // Hash table should have expanded
        assertEquals(15, hash.size());
    }

    @Test
    public void testTShortHashWithDuplicatedKeys() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 1);
        // Hash table should not contain duplicated keys
        assertEquals(1, hash.size());
    }

    @Test
    public void testTShortHashWithOverflowingCapacity() {
        TShortHash hash = new TShortHash(5);
        // Fill the hash table with keys
        for (int i = 0; i < 6; i++) {
            hash.put((short) i);
        }
        // Hash table should not accept any more keys
        try {
            hash.put((short) 6);
            fail();
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testTShortHashWithNullProcedure() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        assertFalse(hash.forEach(null));
    }

    @Test
    public void testTShortHashWithRemoval() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        short keyToRemove = 2;
        hash.remove(keyToRemove);

        // Hash table should not contain the removed key
        assertFalse(hash.contains(keyToRemove));
    }

    @Test
    public void testTShortHashWithIndexing() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        assertEquals((short) 2, hash.get((short) 1));
        assertEquals((short) 3, hash.get((short) 2));
    }

    @Test
    public void testTShortHashWithEmptyHashTable() {
        TShortHash hash = new TShortHash();
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTShortHashWithPartialFill() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);

        // Hash table should have 4 keys
        assertEquals(4, hash.size());
    }

    @Test
    public void testTShortHashWithFullHashTable() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        // Hash table should have expanded
        assertEquals(10, hash.size());
    }

    @Test
    public void testTShortHashWithRandomKeys() {
        TShortHash hash = new TShortHash(10);
        List<Short> keys = asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
        for (Short key : keys) {
            hash.put(key);
        }

        // Hash table should contain all keys
        for (Short key : keys) {
            assertTrue(hash.contains(key));
        }
    }
}