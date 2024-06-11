package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
    }

    @org.junit.Test
    public void testConstructorsWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @org.junit.Test
    public void testConstructorsWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
    }

    @org.junit.Test
    public void testConstructorsWithInitialCapacityLoadFactorAndDefaultNoEntryKeyAndValue() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 0.0f, '\0');
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @org.junit.Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @org.junit.Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.capacity());
        hash.setUp(5);
        assertEquals(5, hash.capacity());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatCharHash(-1);
    }

    @org.junit.Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;

            @Override
            public boolean execute(float value) {
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
        hash.insert(1.0f);
        hash.insert(2.0f);
        assertEquals(2, hash.size());
        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.removeAt(-1);
    }

    @org.junit.Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        assertEquals(-1, hash.index(-2.0f));
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.1f));
        assertEquals(1, hash.index(2.0f));
    }

    @org.junit.Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        hash.insert(3.0f);
        assertEquals(-1, hash.indexRehashed(-2.0f, 0, 0, (byte) 0));
        assertEquals(1, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed(3.1f, 1, 0, (byte) 1));
        assertEquals(2, hash.indexRehashed(2.0f, 1, 0, (byte) 1));
    }

    @org.junit.Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        assertEquals(2, hash.size());
        hash.insert(1.0f); // should not insert a duplicate
        assertEquals(2, hash.size());
        assertEquals(-2, hash.insertKey(2.1f));
        assertEquals(3, hash.size());
        assertEquals(1, hash.index(1.0f));
        assertEquals(2, hash.index(2.0f));
        assertEquals(3, hash.index(2.1f));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        new TFloatCharHash().insert(-1.0f);
    }

    @org.junit.Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        hash.insert(3.0f);
        hash.insert(4.0f);
        hash.insert(5.0f);
        hash.insert(6.0f);
        assertEquals(6, hash.size());
        assertEquals(-1, hash.insertKeyRehash(-1.0f, 0, 0, (byte) 1));
        assertEquals(7, hash.size());
        assertEquals(5, hash.index(5.0f));
        assertEquals(-1, hash.index(-1.0f));
    }

    @org.junit.Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        hash.writeExternal(out);
        ObjectInput in = new ObjectInput(new ByteArrayInputStream(output.toByteArray()));
        TFloatCharHash hashFromStream = new TFloatCharHash();
        hashFromStream.readExternal(in);
        assertEquals(2, hashFromStream.size());
        assertEquals(1.0f, hashFromStream.get(0), 0.0f);
        assertEquals(2.0f, hashFromStream.get(1), 0.0f);
    }
}