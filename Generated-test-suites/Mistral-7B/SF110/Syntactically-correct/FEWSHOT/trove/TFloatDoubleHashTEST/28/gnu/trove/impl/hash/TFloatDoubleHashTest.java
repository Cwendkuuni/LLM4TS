package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(10, hashMap.size());
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
        assertNull(hashMap.contains(1.0f));
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacity() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        assertEquals(5, hashMap.size());
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
        assertNull(hashMap.contains(1.0f));
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(11, 0.75f);
        assertEquals(11, hashMap.size());
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
        assertNull(hashMap.contains(1.0f));
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(15, 0.75f, 1.0f, 2.0);
        assertEquals(15, hashMap.size());
        assertEquals(1.0f, hashMap.getNoEntryKey());
        assertEquals(2.0, hashMap.getNoEntryValue());
        assertNull(hashMap.contains(1.0f));
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(10, hashMap.size());
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
        assertArrayEquals(new float[10], hashMap._set);
        assertNotNull(hashMap._states);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        assertTrue(hashMap.contains(1.0f));
        assertFalse(hashMap.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        int count = 0;
        boolean result;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                result = value == 2.0f;
                count++;
                return true;
            }
        };

        hashMap.forEach(procedure);

        assertEquals(3, hashMap.size());
        assertEquals(true, result);
        assertEquals(1, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        hashMap.removeAt(1);

        assertEquals(2, hashMap.size());
        assertFalse(hashMap.contains(2.0f));
        assertTrue(hashMap.contains(1.0f));
        assertFalse(hashMap.contains(3.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtWhenHashMapIsEmpty() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);

        int index = hashMap.index(1.0f);
        assertEquals(-1, index);

        hashMap.put(1.0f, 1.0);

        index = hashMap.index(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);

        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        int index = hashMap.indexRehashed(2.0f, 0, HashFunctions.hash(2.0f) & Integer.MAX_VALUE, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);

        int index = hashMap.insertKey(1.0f);
        assertEquals(-1, index);

        hashMap.put(1.0f, 1.0);

        index = hashMap.insertKey(1.0f);
        assertEquals(-2, index);

        index = hashMap.insertKey(2.0f);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);

        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        int index = hashMap.insertKeyRehash(4.0f, 0, HashFunctions.hash(4.0f) & Integer.MAX_VALUE, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hashMap.writeExternal(out);

        TFloatDoubleHash hashMapFromByteArray = new TFloatDoubleHash();
        hashMapFromByteArray.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));

        assertEquals(3, hashMapFromByteArray.size());
        assertEquals(1.0f, hashMapFromByteArray.getNoEntryKey());
        assertEquals(0.0, hashMapFromByteArray.getNoEntryValue());
        assertTrue(hashMapFromByteArray.contains(1.0f));
        assertTrue(hashMapFromByteArray.contains(2.0f));
        assertTrue(hashMapFromByteArray.contains(3.0f));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.writeExternal(out);

        TFloatDoubleHash hashMapFromByteArray = new TFloatDoubleHash();
        hashMapFromByteArray.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));

        assertEquals(0, hashMapFromByteArray.size());
        assertEquals(0.0f, hashMapFromByteArray.getNoEntryKey());
        assertEquals(0.0, hashMapFromByteArray.getNoEntryValue());
        assertFalse(hashMapFromByteArray.contains(1.0f));
    }
}