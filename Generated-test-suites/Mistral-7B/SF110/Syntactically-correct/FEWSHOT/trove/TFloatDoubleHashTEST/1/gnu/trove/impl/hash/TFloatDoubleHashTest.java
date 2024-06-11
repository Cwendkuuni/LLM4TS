package org.apache.commons.cli;

import gnu.trove.procedure.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        assertTrue(hashMap.contains(1.0f));
        assertTrue(hashMap.contains(2.0f));
        assertFalse(hashMap.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        int count = 0;
        hashMap.forEach((key, value) -> count++);

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        hashMap.removeAt(0);

        assertFalse(hashMap.containsKey(1.0f));
        assertTrue(hashMap.containsKey(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        assertEquals(0, hashMap.index(1.0f));
        assertEquals(1, hashMap.index(2.0f));
        assertEquals(-1, hashMap.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);

        int index = hashMap.insertKey(1.0f);
        assertEquals(1.0f, hashMap.get(index));

        index = hashMap.insertKey(2.0f);
        assertEquals(2.0f, hashMap.get(index));

        index = hashMap.insertKey(1.0f); // Should rehash
        assertEquals(-1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenNoFreeSlots() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(1);

        hashMap.put(1.0f, 1.0);

        hashMap.insertKey(2.0f); // Should fail
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        hashMap.writeExternal(objectOutputStream);
        objectOutputStream.close();

        TFloatDoubleHash deserializedHashMap = new TFloatDoubleHash(10);
        deserializedHashMap.readExternal(new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray())));

        assertEquals(1.0f, deserializedHashMap.get(0));
        assertEquals(2.0f, deserializedHashMap.get(1));
    }
}