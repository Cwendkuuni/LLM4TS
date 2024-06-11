package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashDefaultConstruction() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTFloatCharHashInitialCapacityConstruction() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTFloatCharHashInitialCapacityAndLoadFactorConstruction() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTFloatCharHashInitialCapacityAndLoadFactorAndNoEntryValuesConstruction() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 0.1f, 'X');
        assertEquals(10, hash.size());
        assertEquals(0.1f, hash.getNoEntryKey());
        assertEquals('X', hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.size());
        assertNotNull(hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(1.0f, key);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertEquals(1, hash.size());
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TFloatCharHash hash = new TFloatCharHash(5);
        for (float i = 0.0f; i < 6.0f; i++) {
            hash.insertKey(i);
        }
    }
}