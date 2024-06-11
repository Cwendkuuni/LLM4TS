package com.example.junit.test; // Replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
    }

    @Test
    public void testTFloatCharHashWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityLoadFactorAndNoEntryKeyAndValue() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'A');;
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals('A', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        int capacity = hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testContainsWithEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEachProcedure() {
        TFloatCharHash hash = new TFloatCharHash(10);
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

    @Test(expected = IllegalArgumentException.class)
    public void testForEachProcedureWithNullProcedure() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.forEach(null);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.removeAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithIndexBeyondCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.setUp(10);
        hash.removeAt(11);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.5f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        assertEquals(3, hash.indexRehashed(3.0f, 1, 3, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testInsertKeyWithEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        assertEquals(1, hash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.setUp(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f);
        assertEquals(6, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.setUp(10);
        for (int i = 0; i < 11; i++) {
            hash.insertKey(i);
        }
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        hash.writeExternal(out);
        byte[] bytes = output.toByteArray();
        TFloatCharHash hashFromBytes = new TFloatCharHash(10);
        hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertTrue(hashFromBytes.contains(1.0f));
        assertTrue(hashFromBytes.contains(2.0f));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(10);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        hash.writeExternal(out);
        byte[] bytes = output.toByteArray();
        TFloatCharHash hashFromBytes = new TFloatCharHash(10);
        hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertTrue(hashFromBytes.contains(1.0f));
        assertTrue(hashFromBytes.contains(2.0f));
    }
}