package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.util.Random;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testInitialization() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertFalse(hash.isEmpty());
        assertEquals(0.75f, hash.loadFactor(), 0.001f);
    }

    @Test
    public void testSerialization() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        objOut.writeObject(hash);
        objOut.close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInputStream(in);
        TFloatDoubleHash cloned = (TFloatDoubleHash) objIn.readObject();
        objIn.close();
        assertEquals(10, cloned.size());
        assertEquals(1.0f, cloned.getNoEntryKey(), 0.0f);
        assertEquals(2.0, cloned.getNoEntryValue(), 0.0);
        assertEquals(0.75f, cloned.loadFactor(), 0.001f);
    }

    @Test
    public void testPutGetAndRemove() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        assertEquals(1, hash.size());
        assertEquals(key1, hash.get(key1), 0.0);
        assertEquals(value1, hash.get(key1), 0.0);
        hash.remove(key1);
        assertEquals(0, hash.size());
        assertNull(hash.get(key1));
    }

    @Test
    public void testPutMultipleEntries() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        float key2 = 2.0f;
        double value2 = 2.0;
        hash.put(key2, value2);
        assertEquals(2, hash.size());
        assertEquals(key1, hash.get(key1), 0.0);
        assertEquals(value1, hash.get(key1), 0.0);
        assertEquals(key2, hash.get(key2), 0.0);
        assertEquals(value2, hash.get(key2), 0.0);
    }

    @Test
    public void testPutLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(4, 0.5f);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            float key = random.nextFloat();
            hash.put(key, (double) random.nextInt());
        }
        assertEquals(8, hash.size());
        assertEquals(0.625f, hash.loadFactor(), 0.001f);
    }

    @Test
    public void testPutNullKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        try {
            hash.put(Float.NaN, 1.0);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPutNullValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        hash.put(key, null);
        assertEquals(1, hash.size());
        assertEquals(key, hash.get(key), 0.0);
        assertNull(hash.get(key));
    }

    @Test
    public void testPutSameKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value1 = 1.0;
        hash.put(key, value1);
        double value2 = 2.0;
        hash.put(key, value2);
        assertEquals(1, hash.size());
        assertEquals(key, hash.get(key), 0.0);
        assertEquals(value2, hash.get(key), 0.0);
        assertEquals(value1, hash.getOldValue(key), 0.0);
    }

    @Test
    public void testPutSameKeyAndValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value = 1.0;
        hash.put(key, value);
        hash.put(key, value);
        assertEquals(1, hash.size());
        assertEquals(key, hash.get(key), 0.0);
        assertEquals(value, hash.get(key), 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        hash.put(key1, 1.0);
        assertTrue(hash.contains(key1));
        assertTrue(hash.containsKey(key1));
        assertFalse(hash.contains(1.1f));
        assertFalse(hash.containsKey(1.1f));
    }

    @Test
    public void testContainsMultipleEntries() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        float key2 = 2.0f;
        double value2 = 2.0;
        hash.put(key2, value2);
        assertTrue(hash.contains(key1));
        assertTrue(hash.containsKey(key1));
        assertTrue(hash.contains(key2));
        assertTrue(hash.containsKey(key2));
    }

    @Test
    public void testRemove() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        assertEquals(1, hash.size());
        hash.remove(key1);
        assertEquals(0, hash.size());
        assertNull(hash.get(key1));
    }

    @Test
    public void testRemoveNonExistent() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNull(hash.get(1.0f));
        hash.remove(1.0f);
        assertEquals(0, hash.size());
    }

    @Test
    public void testRemoveMultiple() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        float key2 = 2.0f;
        double value2 = 2.0;
        hash.put(key2, value2);
        assertEquals(2, hash.size());
        hash.remove(key1);
        assertEquals(1, hash.size());
        assertEquals(key2, hash.get(key2), 0.0);
        assertEquals(value2, hash.get(key2), 0.0);
        hash.remove(key2);
        assertEquals(0, hash.size());
    }

    @Test
    public void testRemoveAll() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        float key2 = 2.0f;
        double value2 = 2.0;
        hash.put(key2, value2);
        assertEquals(2, hash.size());
        hash.clear();
        assertEquals(0, hash.size());
        assertNull(hash.get(key1));
        assertNull(hash.get(key2));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        float key2 = 2.0f;
        double value2 = 2.0;
        hash.put(key2, value2);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 1.5f;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(0, hash.size());

        procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                hash.remove(key);
                return true;
            }
        };
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testWriteRead() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 1.0;
        hash.put(key1, value1);
        float key2 = 2.0f;
        double value2 = 2.0;
        hash.put(key2, value2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        objOut.writeObject(hash);
        objOut.close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInputStream(in);
        TFloatDoubleHash cloned = (TFloatDoubleHash) objIn.readObject();
        objIn.close();

        assertEquals(2, cloned.size());
        assertEquals(key1, cloned.get(key1), 0.0);
        assertEquals(value1, cloned.get(key1), 0.0);
        assertEquals(key2, cloned.get(key2), 0.0);
        assertEquals(value2, cloned.get(key2), 0.0);
    }

    @Test
    public void testConsumeFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        byte[] states = hash._states;
        float[] set = hash._set;
        int index = hash.setUp(10);
        set[index] = Float.NaN;
        states[index] = 0;
        hash.consumeFreeSlot = true;
        float key = 1.0f;
        hash.put(key, 1.0);
        assertEquals(1, hash.size());
        assertEquals(key, hash.get(key), 0.0);
        assertEquals(1.0, hash.get(key), 0.0);
        assertSame(set[index], key);
        assertEquals(1, states[index]);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int initialCapacity = hash.capacity();
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertArrayEquals(new float[initialCapacity], hash._set);
    }

}