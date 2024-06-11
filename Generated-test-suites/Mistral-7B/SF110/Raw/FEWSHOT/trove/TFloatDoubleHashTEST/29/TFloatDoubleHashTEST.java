package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hashMap.size());
    }

    @Test
    public void testConstructors() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        TFloatDoubleHash hashMap1 = new TFloatDoubleHash(5);
        TFloatDoubleHash hashMap2 = new TFloatDoubleHash(5, 0.75f);
        TFloatDoubleHash hashMap3 = new TFloatDoubleHash(5, 0.75f, 0.0f, 0.0);

        assertEquals(5, hashMap1.size());
        assertEquals(5, hashMap2.size());
        assertEquals(5, hashMap3.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        float noEntryKey = hashMap.getNoEntryKey();
        assertEquals(0.0f, noEntryKey);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        double noEntryValue = hashMap.getNoEntryValue();
        assertEquals(0.0, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(10, hashMap.size());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.5f, 1.0);
        assertTrue(hashMap.contains(1.5f));
        assertFalse(hashMap.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.5f, 1.0);
        hashMap.put(2.5f, 2.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key == 1.5f || key == 2.5f;
            }
        };
        assertTrue(hashMap.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.5f, 1.0);
        hashMap.put(2.5f, 2.0);
        hashMap.removeAt(0);
        assertFalse(hashMap.containsKey(1.5f));
        assertTrue(hashMap.containsKey(2.5f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.5f, 1.0);
        hashMap.put(2.5f, 2.0);
        int index = hashMap.index(1.5f);
        assertEquals(0, index);
        index = hashMap.index(2.5f);
        assertEquals(1, index);
        index = hashMap.index(3.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        int index = hashMap.insertKey(1.5f);
        assertEquals(0, index);
        assertEquals(1.0, hashMap.get(1.5f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.5f, 1.0);
        hashMap.put(2.5f, 2.0);
        int index = hashMap.insertKey(3.0f);
        assertEquals(-2, index);
        hashMap.put(3.0f, 3.0);
        assertEquals(3.0, hashMap.get(3.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.5f, 1.0);
        hashMap.put(2.5f, 2.0);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        hashMap.writeExternal(objectStream);

        ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatDoubleHash hashMapRead = (TFloatDoubleHash) objectInput.readObject();

        assertEquals(2, hashMapRead.size());
        assertEquals(1.5f, hashMapRead.keyAt(0), 0.01f);
        assertEquals(1.0, hashMapRead.valueAt(0), 0.01f);
        assertEquals(2.5f, hashMapRead.keyAt(1), 0.01f);
        assertEquals(2.0, hashMapRead.valueAt(1), 0.01f);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        hashMap.writeExternal(objectStream);
        ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        hashMap.readExternal(objectInput);

        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
        assertEquals(5, hashMap.size());
    }
}