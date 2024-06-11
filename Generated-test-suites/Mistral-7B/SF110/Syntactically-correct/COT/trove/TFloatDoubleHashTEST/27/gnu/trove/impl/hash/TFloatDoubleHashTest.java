package com.example.junit.test; // replace with your test package name

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
    public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.001f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.001);
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

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value > 1.0f;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int index1 = hash.index(1.0f);
        int index2 = hash.index(2.0f);
        int index3 = hash.index(3.0f);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, index3);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.insertKey(4.0f);
        assertEquals(-1, index);

        hash.insertKey(5.0f);
        int index2 = hash.index(5.0f);
        assertEquals(4, index2);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(new byte[0]);
        ObjectInput in = new ObjectInputStream(byteStream);
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.readExternal(in);

        assertEquals(5, hash.size());
        assertFalse(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatDoubleHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TFloatDoubleHash(5, -0.75f);
    }

    @Test(expected = NullPointerException.class)
    public void testNullProcedure() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        TFloatProcedure procedure = null;
        assertFalse(hash.forEach(procedure));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(-1.0f);
    }
}