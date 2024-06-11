package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashEmptyConstruction() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertTrue(hashMap.isEmpty());
        assertEquals(0, hashMap.size());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityConstruction() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertFalse(hashMap.isEmpty());
        assertEquals(0, hashMap.size());
        assertEquals(10, hashMap.capacity());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityLoadFactorConstruction() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10, 0.75f);
        assertFalse(hashMap.isEmpty());
        assertEquals(0, hashMap.size());
        assertEquals(10, hashMap.capacity());
        assertEquals(0.75f, hashMap.loadFactor(), 0.001f);
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityLoadFactorNoEntryKeyNoEntryValueConstruction() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0d);
        assertFalse(hashMap.isEmpty());
        assertEquals(0, hashMap.size());
        assertEquals(10, hashMap.capacity());
        assertEquals(0.75f, hashMap.loadFactor(), 0.001f);
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0d, hashMap.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(5, hashMap.capacity());
        assertNotNull(hashMap._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        float key = 1.0f;
        assertFalse(hashMap.contains(key));
        hashMap.insertKey(key);
        assertTrue(hashMap.contains(key));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        TFloatProcedure procedure = (key, index) -> {};
        assertTrue(hashMap.forEach(procedure));

        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        int count = 0;
        hashMap.forEach(key -> count++);
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        int index = hashMap.index(1.0f);
        hashMap.removeAt(index);

        assertFalse(hashMap.contains(1.0f));
        assertEquals(2, hashMap.size());
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        int index1 = hashMap.index(1.0f);
        int index2 = hashMap.index(2.0f);
        int index3 = hashMap.index(3.0f);
        int index4 = hashMap.index(4.0f);

        assertEquals(index1, hashMap.index(1.0f));
        assertEquals(index2, hashMap.index(2.0f));
        assertEquals(index3, hashMap.index(3.0f));
        assertEquals(-1, index4);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        assertEquals(3, hashMap.size());
        assertEquals(1.0f, hashMap._set[hashMap.index(1.0f)]);
        assertEquals(2.0f, hashMap._set[hashMap.index(2.0f)]);
        assertEquals(3.0f, hashMap._set[hashMap.index(3.0f)]);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(2);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        int index1 = hashMap.index(1.0f);
        int index2 = hashMap.index(2.0f);
        int index3 = hashMap.index(3.0f);

        assertEquals(-1, index1);
        assertEquals(-1, index2);
        assertEquals(0, hashMap.size());

        hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        index1 = hashMap.index(1.0f);
        index2 = hashMap.index(2.0f);
        index3 = hashMap.index(3.0f);

        assertEquals(3, hashMap.size());
        assertNotEquals(0, index1);
        assertNotEquals(0, index2);
        assertNotEquals(0, index3);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(3);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hashMap.writeExternal(objectOutput);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        TFloatDoubleHash hashMapDeserialized = new TFloatDoubleHash();
        hashMapDeserialized.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

        TFloatProcedure procedure = (key, index) -> {};
        hashMapDeserialized.forEach(procedure);

        assertEquals(hashMap.size(), hashMapDeserialized.size());
        for (int i = 0; i < hashMap.size(); i++) {
            assertEquals(hashMap._set[hashMap.index(i)], hashMapDeserialized._set[hashMapDeserialized.index(i)]);
        }
    }
}