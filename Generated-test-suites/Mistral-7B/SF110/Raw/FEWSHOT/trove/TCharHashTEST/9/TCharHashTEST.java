package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import java.util.Arrays;

public class TCharHashTest {

    @Test
    public void testConstructors() {
        TCharHash charHash = new TCharHash();
        TCharHash charHashWithCapacity = new TCharHash(10);
        TCharHash charHashWithLoadFactor = new TCharHash(10, 0.75f);
        TCharHash charHashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f, 'N');

        assertEquals("Default constructor with no arguments", 0, charHash.getCapacity());
        assertEquals("Constructor with initial capacity", 10, charHashWithCapacity.getCapacity());
        assertEquals("Constructor with initial capacity and load factor", 10, charHashWithInitialCapacityAndLoadFactor.getCapacity());
        assertNotNull("Default constructor initializes no_entry_value with default value", charHash.no_entry_value);
        assertEquals("Constructor with specified no_entry_value sets it", 'N', charHashWithInitialCapacityAndLoadFactor.no_entry_value);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals("Default constructor sets no_entry_value", Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        TCharHash charHashWithCustomNoEntryValue = new TCharHash('Z');
        assertEquals("Constructor with custom no_entry_value sets it", 'Z', charHashWithCustomNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash();
        assertEquals("Set up method initializes _set with proper length", charHash._set.length, charHash.capacity());
        TCharHash charHashWithInitialCapacity = new TCharHash(5);
        assertEquals("Set up method initializes _set with proper length for constructor with initial capacity", charHashWithInitialCapacity._set.length, charHashWithInitialCapacity.capacity());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        assertFalse("Empty hash does not contain any value", charHash.contains('A'));
        charHash.put('A', 'A');
        assertTrue("Hash contains value 'A'", charHash.contains('A'));
        charHash.put('B', 'B');
        assertTrue("Hash contains value 'B'", charHash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char[] result = new char[3];
        TCharProcedure procedure = new TCharProcedure() {
            int index = 0;

            @Override
            public boolean execute(char charValue) {
                result[index++] = charValue;
                return true;
            }
        };
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.forEach(procedure);
        assertArrayEquals("ForEach method processes all elements in the correct order", Arrays.asList('A', 'B', 'C').toArray(), result);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        char removedValue = charHash.removeAt(1);
        assertEquals("Removed value is 'B'", 'B', removedValue);
        assertFalse("Hash does not contain value 'B' after removal", charHash.contains('B'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        assertEquals("Index method returns correct index for existing value", 1, charHash.index('B'));
        assertEquals("Index method returns -1 for non-existent value", -1, charHash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        assertEquals("Hash is empty initially", 0, charHash.size());
        charHash.put('A', 'A');
        assertEquals("Hash contains one element after insertion", 1, charHash.size());
        charHash.put('B', 'B');
        assertEquals("Hash contains two elements after second insertion", 2, charHash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenHashIsFull() {
        TCharHash charHash = new TCharHash(1);
        charHash.put('A', 'A');
        // Hash is full, so this should throw an IllegalStateException
        charHash.put('B', 'B');
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(2);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        // Hash needs to be rehashed after inserting 'D'
        charHash.put('D', 'D');
        assertEquals("Hash size is 4 after rehashing", 4, charHash.size());
    }
}