package com.example.junit.test; // replace with your test package if needed

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
        assertEquals(0.0d, noEntryValue, 0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatVal) {
                assertEquals(1.0f, floatVal, 0);
                return true;
            }
        };
        hash.forEach(procedure);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsExceptionWhenRemovingAtInvalidIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(1, index);

        index = hash.index(3.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        indexRehashedTestHelper(hash, 1.0f, 0, HashFunctions.hash(1.0f), (byte) 1);
        indexRehashedTestHelper(hash, 2.0f, 1, HashFunctions.hash(2.0f), (byte) 1);
        indexRehashedTestHelper(hash, 3.0f, 2, HashFunctions.hash(3.0f), (byte) 1);
        indexRehashedTestHelper(hash, 4.0f, -1, HashFunctions.hash(4.0f), (byte) 0);
    }

    private void indexRehashedTestHelper(TFloatDoubleHash hash, float key, int expectedIndex, int hash, byte state) {
        int index = hash.indexRehashed(key, 0, hash, state);
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash.get(0), 0);

        index = hash.insertKey(1.0f); // should not insert again
        assertEquals(-1, index);

        index = hash.insertKey(2.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hash.get(0), 0);
        assertEquals(2.0f, hash.get(1), 0);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        byte[] bytes = outputStream.toByteArray();

        TFloatDoubleHash clonedHash = new TFloatDoubleHash();
        clonedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(1, clonedHash.size());
        assertEquals(1.0f, clonedHash.get(0), 0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        byte[] bytes = outputStream.toByteArray();
        ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(bytes));
        TFloatDoubleHash clonedHash = (TFloatDoubleHash) input.readObject();
        assertEquals(0, hash.size());
        assertEquals(0, clonedHash.size());

        hash.put(1.0f, 1.0);
        hash.writeExternal(outputStream);
        bytes = outputStream.toByteArray();
        clonedHash = (TFloatDoubleHash) input.readObject();
        assertEquals(1, hash.size());
        assertEquals(1, clonedHash.size());
        assertEquals(1.0f, hash.get(0), 0);
        assertEquals(1.0f, clonedHash.get(0), 0);
    }
}