package com.example.junit.test; // replace with your test package if necessary

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0d);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsWithNegativeValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.contains(-1.0f);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(1.0f, value, 0);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertFalse(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(2.0f, value, 0);
                return false;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int index = hash.index(1.0f);
        assertEquals(0, index);
        index = hash.index(2.0f);
        assertEquals(1, index);
        index = hash.index(-1.0f);
        assertEquals(-1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexWithNegativeValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.index(-1.0f);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        for (int i = 0; i < 3; i++) {
            hash.insertKey(i);
        }
        hash.insertKey(3);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKey(6.0f);
        assertEquals(-3, index);
        assertTrue(hash.contains(6.0f));
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        TFloatDoubleHash hashLoaded = new TFloatDoubleHash();
        hashLoaded.readExternal(new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())));
        assertTrue(hashLoaded.contains(1.0f));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TFloatDoubleHash hashLoaded = (TFloatDoubleHash) in.readObject();
        assertEquals(hash.getNoEntryKey(), hashLoaded.getNoEntryKey(), 0);
        assertEquals(hash.getNoEntryValue(), hashLoaded.getNoEntryValue(), 0);
    }
}