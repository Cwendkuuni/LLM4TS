package com.example.junit4; // replace with your package name if necessary

import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testEmptyConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(5, 0.75f);
        assertEquals(5, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(5, 0.75f, noEntryValue);
        assertEquals(5, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TCharHash(-5);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        char defaultNoEntryValue = charHash.getNoEntryValue();
        assertNotNull(defaultNoEntryValue);
        char customNoEntryValue = 'A';
        charHash = new TCharHash();
        charHash.setNoEntryValue(customNoEntryValue);
        assertEquals(customNoEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash();
        int capacity = charHash.setUp(5);
        assertEquals(5, capacity);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        assertTrue(charHash.contains(key));
        assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEachProcedure() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        TCharProcedure procedure = new TCharProcedure() {
            private int count = 0;

            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
        assertEquals(1, charHash.size());
        assertEquals(1, count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        assertEquals(1, charHash.size());
        charHash.removeAt(0);
        assertEquals(0, charHash.size());
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        int index = charHash.index(key);
        assertEquals(0, index);
        assertFalse(charHash.contains('B'));
        index = charHash.index('B');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.insertKey(key);
        assertEquals(1, charHash.size());
        assertEquals(key, charHash.get(0));
    }

    @Test
    public void testInsertKeyWithExistingKey() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        int index = charHash.index(key);
        charHash.insertKey(key);
        assertEquals(1, charHash.size());
        assertEquals(key, charHash.get(index));
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHashTable() {
        TCharHash charHash = new TCharHash(3);
        for (char key = 'A'; key <= 'Z'; key++) {
            charHash.put(key);
        }
    }
}