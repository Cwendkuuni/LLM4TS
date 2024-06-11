package com.example.junit.test; // replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashZeroArgumentsConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorAndNoEntryValuesConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setNoEntryKey(1.0f);
        assertEquals(1.0f, hash.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setNoEntryValue(1.0);
        assertEquals(1.0, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        int capacity = hash.setUp(15);
        assertEquals(15, capacity);
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
            public boolean execute(float value) {
                assertEquals(1.0f, value, 0.01f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtWithoutConsumeFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.removeAt(0);
    }

    @Test
    public void testRemoveAtWithConsumeFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.setConsumeFreeSlot(true);
        hash.removeAt(0);
        assertEquals(0, hash.size());
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        assertEquals(2, hash.indexRehashed(3.0f, 0, (int)HashFunctions.hash(3.0f), (byte)0));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.setConsumeFreeSlot(true);
        hash.insertKey(3.0f);
        assertEquals(3, hash.size());
        assertEquals(3.0f, hash.get(3.0f), 0.01f);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.setConsumeFreeSlot(true);
        hash.insertKey(5.0f);
        assertEquals(5, hash.size());
        assertEquals(5.0f, hash.get(5.0f), 0.01f);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
        hash.setConsumeFreeSlot(true);
        hash.insertKey(6.0f);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatDoubleHash hashFromByteArray = (TFloatDoubleHash) in.readObject();
        assertEquals(2, hashFromByteArray.size());
        assertEquals(1.0f, hashFromByteArray.get(1.0f), 0.01f);
        assertEquals(2.0f, hashFromByteArray.get(2.0f), 0.01f);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatDoubleHash hashFromByteArray = (TFloatDoubleHash) in.readObject();
        assertEquals(0, hash.size());
        assertEquals(0, hashFromByteArray.size());
        hash.put(1.0f, 1.0);
        hashFromByteArray.put(2.0f, 2.0);
        assertEquals(2, hash.size());
        assertEquals(2, hashFromByteArray.size());
        assertEquals(1.0f, hash.get(1.0f), 0.01f);
        assertEquals(2.0f, hashFromByteArray.get(2.0f), 0.01f);
    }

}