package org.apache.commons.cli;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashInitialization() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        TFloatDoubleHash hashMapWithInitialCapacity = new TFloatDoubleHash(10);
        TFloatDoubleHash hashMapWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
        TFloatDoubleHash hashMapWithInitialCapacityLoadFactorAndKeyValue = new TFloatDoubleHash(10, 0.75f, 0.5f, 1.0);

        assertEquals(0, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue(), 0.0);
        assertEquals(0, hashMap.size());

        assertEquals(10, hashMapWithInitialCapacity.getNoEntryKey());
        assertEquals(0.0, hashMapWithInitialCapacity.getNoEntryValue(), 0.0);
        assertEquals(0, hashMapWithInitialCapacity.size());

        assertEquals(10, hashMapWithInitialCapacityAndLoadFactor.getNoEntryKey());
        assertEquals(0.0, hashMapWithInitialCapacityAndLoadFactor.getNoEntryValue(), 0.0);
        assertEquals(0, hashMapWithInitialCapacityAndLoadFactor.size());

        assertEquals(10, hashMapWithInitialCapacityLoadFactorAndKeyValue.getNoEntryKey());
        assertEquals(1.0, hashMapWithInitialCapacityLoadFactorAndKeyValue.getNoEntryValue());
        assertEquals(0, hashMapWithInitialCapacityLoadFactorAndKeyValue.size());
    }

    @Test
    public void testTFloatDoubleHashContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        assertTrue(hashMap.contains(1.0f));
        assertTrue(hashMap.contains(2.0f));
        assertFalse(hashMap.contains(3.0f));
    }

    @Test
    public void testTFloatDoubleHashForEachProcedure() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        int count = 0;
        hashMap.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test
    public void testTFloatDoubleHashRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        assertEquals(3, hashMap.size());

        hashMap.removeAt(1);

        assertEquals(2, hashMap.size());
        assertFalse(hashMap.containsKey(1.0f));
        assertTrue(hashMap.containsKey(2.0f));
        assertTrue(hashMap.containsKey(3.0f));
    }

    @Test
    public void testTFloatDoubleHashIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);

        assertEquals(-1, hashMap.index((float) 0.5));
        assertEquals(1, hashMap.index(1.0f));
        assertEquals(2, hashMap.index(2.0f));
        assertEquals(3, hashMap.index(3.0f));
    }

    @Test
    public void testTFloatDoubleHashInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();

        int index = hashMap.insertKey(1.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hashMap.get(index));

        index = hashMap.insertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(1, hashMap.size());

        index = hashMap.insertKey(2.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hashMap.get(0));
        assertEquals(2.0f, hashMap.get(1));
        assertEquals(2, hashMap.size());
    }

    @Test
    public void testTFloatDoubleHashWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hashMap.writeExternal(objectOutputStream);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TFloatDoubleHash hashMapFromSerialized = (TFloatDoubleHash) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(2, hashMapFromSerialized.size());
        assertTrue(hashMapFromSerialized.containsKey(1.0f));
        assertTrue(hashMapFromSerialized.containsKey(2.0f));
    }
}