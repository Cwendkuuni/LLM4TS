package gnu.trove.impl.hash.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.*;
import gnu.trove.procedure.*;
import gnu.trove.impl.hash.*;

public class TCharHashTest {
    @Test
    public void testTCharHashConstructors() {
        // Test default constructor
        TCharHash charHash1 = new TCharHash();
        // Test initial capacity constructor
        TCharHash charHash2 = new TCharHash(10);
        // Test initial capacity and load factor constructor
        TCharHash charHash3 = new TCharHash(10, 0.75f);
        // Test initial capacity, load factor, and no_entry_value constructor
        TCharHash charHash4 = new TCharHash(10, 0.75f, 'E');

        // Perform additional checks on the constructed objects if required
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        char noEntryValue = charHash.getNoEntryValue();
        assertNotNull(noEntryValue);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);

        TCharHash charHash2 = new TCharHash('A');
        char noEntryValue2 = charHash2.getNoEntryValue();
        assertNotNull(noEntryValue2);
        assertEquals('A', noEntryValue2);
    }

    @Test
    public void testSetUpContainsForEachRemoveAtIndex() {
        TCharHash charHash = new TCharHash(5);

        // Test setUp()
        int capacity = charHash.setUp(5);
        assertEquals(5, capacity);

        // Test contains()
        char key1 = 'A';
        charHash.insertKey(key1);
        assertTrue(charHash.contains(key1));

        // Test forEach()
        TCharProcedure procedure = (char c) -> {
            assertEquals(key1, c);
            return true;
        };
        assertTrue(charHash.forEach(procedure));

        // Test removeAt()
        int index = charHash.index(key1);
        charHash.removeAt(index);
        assertFalse(charHash.contains(key1));

        // Test index()
        char key2 = 'B';
        charHash.insertKey(key2);
        index = charHash.index(key2);
        assertEquals(index, charHash.index(key2));
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        TCharHash charHash = new TCharHash(5);

        // Test contains() with empty hash
        assertFalse(charHash.contains('A'));

        // Test forEach() with empty hash
        TCharProcedure procedure = (char c) -> {
            fail();
            return true;
        };
        assertTrue(charHash.forEach(procedure));

        // Test removeAt() with empty hash
        int index = charHash.index('A');
        charHash.removeAt(index);
        assertEquals(-1, index);

        // Test index() with empty hash
        index = charHash.index('A');
        assertEquals(-1, index);

        // Test index() with null key
        assertThrows(NullPointerException.class, () -> charHash.index(null));

        // Test insertKey() with existing key
        charHash.insertKey('A');
        assertThrows(IllegalArgumentException.class, () -> charHash.insertKey('A'));

        // Test insertKeyRehash() with null key
        assertThrows(NullPointerException.class, () -> charHash.insertKeyRehash(null, 0, 0, (byte) 0));

        // Test insertKeyRehash() with key already present
        charHash.insertKey('B');
        assertThrows(IllegalArgumentException.class, () -> charHash.insertKey('B'));

        // Test indexRehashed() with key not present
        assertThrows(IllegalStateException.class, () -> charHash.indexRehashed('C', 0, 0, (byte) 1));
    }
}