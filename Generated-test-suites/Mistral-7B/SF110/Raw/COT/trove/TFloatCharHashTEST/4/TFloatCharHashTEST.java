package com.example.junit4; // replace with your package name if necessary

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);

        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        assertNotNull(hashWithInitialCapacity);

        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);

        TFloatCharHash hashWithInitialCapacityLoadFactorAndKeys = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertNotNull(hashWithInitialCapacityLoadFactorAndKeys);
    }

    @org.junit.Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0f);
    }

    @org.junit.Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char noEntryValue = hash.getNoEntryValue();
        assertEquals('\0', noEntryValue);
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.setUp(5));
    }

    @org.junit.Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f, 0.0f);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.indexOfKey(1.0f);
        hash.removeAt(index);

        assertFalse(hash.containsKey(1.0f));
    }

    @org.junit.Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.indexOfKey(1.0f);
        assertEquals(index, 0);

        index = hash.indexOfKey(4.0f);
        assertEquals(index, -1);
    }

    @org.junit.Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        int index = hash.indexRehashed(1.5f, 0, HashFunctions.hash(1.5f), (byte) 1);
        assertEquals(index, -1);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
    }

    @org.junit.Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInput(byteInput);
        TFloatCharHash deserializedHash = new TFloatCharHash(3);
        deserializedHash.readExternal(in);

        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
        assertTrue(deserializedHash.contains(3.0f));
    }

}