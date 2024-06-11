package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(10, hashMap.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0.0f, hashMap.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0.0, hashMap.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        final int initialCapacity = 5;
        TFloatDoubleHash hashMap = new TFloatDoubleHash(initialCapacity);
        assertEquals(initialCapacity, hashMap.size());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        assertTrue(hashMap.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        int count = 0;
        hashMap.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                count++;
                return true;
            }
        });

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        hashMap.removeAt(0);
        assertEquals(1, hashMap.size());
        assertFalse(hashMap.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        assertEquals(1, hashMap.index(1.0f));
        assertEquals(-1, hashMap.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(-1, hashMap.insertKey(1.0f));
        hashMap.put(1.0f, 1.0);
        assertEquals(0, hashMap.insertKey(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyException() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.insertKey(1.0f);
    }
}