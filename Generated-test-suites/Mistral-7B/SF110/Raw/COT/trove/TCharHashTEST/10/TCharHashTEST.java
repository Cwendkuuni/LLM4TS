package com.example.junit4; // replace with your test package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash charHash;

    @Before
    public void setUp() {
        charHash = new TCharHash(5);
    }

    // Test constructor with default arguments
    @Test
    public void testDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    // Test constructor with initialCapacity
    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
    }

    // Test constructor with initialCapacity and loadFactor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(5, 0.75f);
        assertEquals(5, charHash.size());
        assertEquals(0.75f, charHash.getLoadFactor(), 0.001f);
    }

    // Test constructor with initialCapacity, loadFactor, and no_entry_value
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        char c = 'A';
        TCharHash charHash = new TCharHash(5, 0.75f, c);
        assertEquals(5, charHash.size());
        assertEquals(0.75f, charHash.getLoadFactor(), 0.001f);
        assertEquals(c, charHash.getNoEntryValue());
    }

    // Test getNoEntryValue method
    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());

        char customNoEntryValue = 'Z';
        charHash = new TCharHash(5, customNoEntryValue);
        assertEquals(customNoEntryValue, charHash.getNoEntryValue());
    }

    // Test setUp method
    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(5);
        int capacity = charHash.setUp(5);
        assertEquals(5, capacity);
    }

    // Test contains method
    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';
        charHash.insertKey(key);
        assertTrue(charHash.contains(key));
    }

    // Test forEach method
    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';
        charHash.insertKey(key);

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char charObject) {
                if (charObject == key) {
                    return false;
                }
                fail("Unexpected character: " + charObject);
                return true;
            }
        };

        assertTrue(charHash.forEach(procedure));
    }

    // Test removeAt method
    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';
        charHash.insertKey(key);
        charHash.removeAt(0);
        assertFalse(charHash.contains(key));
    }

    // Test index method
    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';
        charHash.insertKey(key);
        int index = charHash.index(key);
        assertEquals(0, index);
    }

    // Test indexRehashed method
    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';
        charHash.insertKey(key);

        int index = charHash.indexRehashed(key, 0, HashFunctions.hash(key) % Integer.MAX_VALUE, (byte) 1);
        assertEquals(-1, index);

        charHash.insertKey(key); // rehash
        index = charHash.indexRehashed(key, 0, HashFunctions.hash(key) % Integer.MAX_VALUE, (byte) 1);
        int expectedIndex = (HashFunctions.hash(key) % Integer.MAX_VALUE + 1) % Integer.MAX_VALUE;
        assertEquals(expectedIndex, index);
    }

    // Test insertKey method
    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';
        int index = charHash.insertKey(key);
        assertEquals(0, index);
    }

    // Test insertKeyRehash method
    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';

        try {
            charHash.insertKeyRehash(key, 0, HashFunctions.hash(key) % Integer.MAX_VALUE, (byte) 1);
            fail("Expected IllegalStateException when no free or removed slots available.");
        } catch (IllegalStateException e) {
            // Expected
        }

        charHash.insertKey(key);
        charHash.insertKey(key); // rehash

        int index = charHash.insertKeyRehash(key, 0, HashFunctions.hash(key) % Integer.MAX_VALUE, (byte) 1);
        int expectedIndex = (HashFunctions.hash(key) % Integer.MAX_VALUE + 1) % Integer.MAX_VALUE;
        assertEquals(expectedIndex, index);
    }

    // Test insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        TCharHash charHash = new TCharHash(5);
        char key = 'A';

        int index = charHash.insertKeyAt(5, key);
        assertEquals(5, index);
    }

    // Edge case: Test hash overflow
    @Test(expected = IllegalStateException.class)
    public void testHashOverflow() {
        TCharHash charHash = new TCharHash(2);
        char key = 'A';

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            charHash.insertKey(key);
        }
    }

    // Edge case: Test negative initialCapacity
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TCharHash(-1);
    }

    // Edge case: Test negative loadFactor
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TCharHash(5, -0.5f);
    }

    // Edge case: Test null no_entry_value
    @Test(expected = NullPointerException.class)
    public void testNullNoEntryValue() {
        new TCharHash(5, null);
    }
}