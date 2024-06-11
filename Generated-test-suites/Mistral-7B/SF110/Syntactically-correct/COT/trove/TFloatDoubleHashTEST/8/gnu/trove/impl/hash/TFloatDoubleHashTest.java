package com.example.junit.test; // replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatDoubleHashTest {

    @org.junit.Test
    public void testTFloatDoubleHashConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);

        TFloatDoubleHash hashWithInitialCapacity = new TFloatDoubleHash(10);
        assertNotNull(hashWithInitialCapacity);

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);

        TFloatDoubleHash hashWithInitialCapacityLoadFactorAndKeys = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertNotNull(hashWithInitialCapacityLoadFactorAndKeys);
    }

    @org.junit.Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
    }

    @org.junit.Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.01);
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.setUp(10));
    }

    @org.junit.Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key != 2.0f;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @org.junit.Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @org.junit.Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInput(inputStream);
        TFloatDoubleHash hashFromSerialization = new TFloatDoubleHash();
        hashFromSerialization.readExternal(in);

        assertTrue(hashFromSerialization.contains(1.0f));
        assertTrue(hashFromSerialization.contains(2.0f));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenNoFreeOrRemovedSlotsAvailable() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f);
        hash.insertKey(7.0f);
        hash.insertKey(8.0f);
        hash.insertKey(9.0f);
        hash.insertKey(10.0f);
        hash.insertKey(11.0f);
    }
}