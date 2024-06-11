package com.example.junit.test; // replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStore;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatDoubleHashTest {

    @org.junit.Test
    public void testTFloatDoubleHashConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
    }

    @org.junit.Test
    public void testTFloatDoubleHashWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @org.junit.Test
    public void testTFloatDoubleHashWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(10, hash.size());
    }

    @org.junit.Test
    public void testTFloatDoubleHashWithInitialCapacityLoadFactorAndNoEntryValues() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.1f, 0.0);;
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.1f, hash.getNoEntryKey(), 0.001f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @org.junit.Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                if (value == 1.0f) {
                    return false;
                }
                assertEquals(2.0f, value, 0.001);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtNegativeIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.removeAt(-1);
    }

    @org.junit.Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @org.junit.Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(-1, hash.indexRehashed(4.0f, 0, 0, (byte) 0));
        assertEquals(1, hash.indexRehashed(1.5f, 0, 0, (byte) 0));
    }

    @org.junit.Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKey(1.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(1.0f));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatDoubleHash hash = new TFloatDoubleHash(2);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
    }

    @org.junit.Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
    }

    @org.junit.Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutput);

        ObjectInput objectInput = new ObjectInput(new ByteArrayInputStore(byteArrayOutputStream.toByteArray()));
        TFloatDoubleHash deserializedHash = new TFloatDoubleHash();
        deserializedHash.readExternal(objectInput);

        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
    }
}