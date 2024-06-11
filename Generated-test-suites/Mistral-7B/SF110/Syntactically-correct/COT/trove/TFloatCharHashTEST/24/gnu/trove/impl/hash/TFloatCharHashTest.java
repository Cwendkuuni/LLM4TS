package com.example.junit.test; // replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputOutput;
import java.io.IOException;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testTFloatCharHashConstruction() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);

        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(5);
        assertNotNull(hashWithInitialCapacity);

        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(5, 0.5f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);

        TFloatCharHash hashWithInitialCapacityAndLoadFactorAndNoEntryValues = new TFloatCharHash(5, 0.5f, 1.0f, 'A');;
        assertNotNull(hashWithInitialCapacityAndLoadFactorAndNoEntryValues);
    }

    @org.junit.Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.001f);
    }

    @org.junit.Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char noEntryValue = hash.getNoEntryValue();
        assertEquals('\u0000', noEntryValue);
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
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
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                int count = 0;
                if (value == 1.0f) {
                    count++;
                }
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(1, count);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testRemoveAtWithEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.removeAt(-1);
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testIndexWithNonExistentKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        int index = hash.index(2.0f);
        assertEquals(-1, index);
    }

    @org.junit.Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int index = hash.index(1.0f);
        assertEquals(0, index);
    }

    @org.junit.Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
    }

    @org.junit.Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f);
        int index = hash.insertKey(7.0f);
        assertEquals(-1, index);
        assertTrue(hash.size() == 7);
    }

    @org.junit.Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputOutput inputOutput = new ByteArrayInputOutput(outputStream.toByteArray());
        hash.writeExternal(inputOutput);
        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(new ObjectInput(new ByteArrayInputStream(outputStream.toByteArray())));
        assertTrue(deserializedHash.size() == 2);
        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
    }

}