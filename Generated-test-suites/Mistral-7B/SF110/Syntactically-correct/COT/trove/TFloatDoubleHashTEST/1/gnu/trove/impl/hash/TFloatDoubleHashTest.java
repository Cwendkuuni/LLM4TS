package com.example.junit.test; // Replace with your actual test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.size());

        TFloatDoubleHash hashWithInitialCapacity = new TFloatDoubleHash(10);
        assertEquals(10, hashWithInitialCapacity.capacity());
        assertEquals(0, hashWithInitialCapacity.size());

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.5f);
        assertEquals(10, hashWithInitialCapacityAndLoadFactor.capacity());
        assertEquals(0, hashWithInitialCapacityAndLoadFactor.size());

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactorAndKeys = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(10, hashWithInitialCapacityAndLoadFactorAndKeys.capacity());
        assertEquals(0, hashWithInitialCapacityAndLoadFactorAndKeys.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.capacity());
        assertEquals(0, hash.size());
        assertArrayEquals(new float[5], hash._set);

        hash.setUp(10);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int count = 0;
        TFloatDoubleHash.TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(2, count);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtNegativeIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.removeAt(-1);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(2, hash.size());
        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexNegativeIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = -1;
        float key = hash.index(index);
        assertEquals(Float.NaN, key, 0.0f);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(1, hash.index(1.0f));
        assertFalse(hash.contains(3.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());

        hash.insertKey(1.0f);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullHashTable() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        for (float i = 0; i < hash.capacity(); i++) {
            hash.insertKey(i);
        }
        hash.insertKey(hash.capacity());
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);

        TFloatDoubleHash hashFromSerialized = (TFloatDoubleHash) in.readObject();

        assertEquals(2, hashFromSerialized.size());
        assertTrue(hashFromSerialized.contains(1.0f));
        assertTrue(hashFromSerialized.contains(2.0f));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.writeExternal(new ObjectOutputStream(new ByteArrayOutputStream()));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ObjectInput in = new ObjectInputStream(inputStream);

        TFloatDoubleHash hashFromDeserialized = (TFloatDoubleHash) in.readObject();

        assertEquals(0, hashFromDeserialized.size());
        assertFalse(hashFromDeserialized.contains(1.0f));
    }
}