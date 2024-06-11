package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testTFloatCharHashConstruction() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        assertEquals(0, hashWithInitialCapacity.size());
        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        assertEquals(0, hashWithInitialCapacityAndLoadFactor.size());
        TFloatCharHash hashWithInitialCapacityLoadFactorAndKeys = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertEquals(0, hashWithInitialCapacityLoadFactorAndKeys.size());
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.setUp(5).intValue());
        TFloatCharHash hashWithLoadFactor = new TFloatCharHash(5, 0.75f);
        assertEquals(5, hashWithLoadFactor.setUp(5).intValue());
    }

    @org.junit.Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(1.0f));
        hash.put(1.0f, 'A');
        assertTrue(hash.contains(1.0f));
        hash.put(2.0f, 'B');
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                count++;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(2, count);
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        char initialValue = hash.get(1.0f);
        assertEquals('A', initialValue);

        hash.removeAt(0);
        assertNull(hash.get(1.0f));
    }

    @org.junit.Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        int index1 = hash.index(1.0f);
        assertEquals(0, index1);

        int index2 = hash.index(2.0f);
        assertEquals(1, index2);

        int index3 = hash.index(-1.0f);
        assertEquals(-1, index3);
    }

    @org.junit.Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        assertEquals(2, hash.size());

        hash.put(1.5f, 'C');
        assertEquals(3, hash.size());

        assertEquals('C', hash.get(1.5f));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(2);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');
    }

    @org.junit.Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        // Add test code for writing and reading the object
    }
}