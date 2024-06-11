package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashEmptyConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorAndKeysValuesConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new float[5], hash._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(1.0f, value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int indexBeforeRemoval = hash.index(1.0f);
        hash.removeAt(indexBeforeRemoval);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int indexOfKey1 = hash.index(1.0f);
        int indexOfKey2 = hash.index(2.0f);
        assertEquals(indexOfKey1, 0);
        assertEquals(indexOfKey2, 1);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f);
        hash.insertKey(7.0f);
        hash.insertKey(8.0f);
        hash.insertKey(9.0f);
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bytes);
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        out.writeObject(hash);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        TFloatDoubleHash hashRead = (TFloatDoubleHash) in.readObject();
        assertEquals(2, hashRead.size());
        assertTrue(hashRead.contains(1.0f));
        assertTrue(hashRead.contains(2.0f));
    }
}