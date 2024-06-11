package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashEmpty() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.size());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.5f);
        assertEquals(5, hash.size());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorAndNoEntryValues() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.5f, 1.0f, 2.0);
        assertEquals(5, hash.size());
        assertFalse(hash.isEmpty());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals(2.0, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
        assertEquals(5, hash._states.length);
        assertEquals(5, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        assertEquals(5, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));

        assertFalse(hash.contains(6.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                assertEquals(value, count++, 0.0f);
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        hash.removeAt(2);

        assertEquals(5, hash.size());
        assertFalse(hash.contains(2.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));

        assertEquals(4.0f, hash._set[2]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        assertEquals(5, hash.size());
        assertEquals(1, hash.index(1.0f));
        assertEquals(2, hash.index(2.0f));
        assertEquals(3, hash.index(3.0f));
        assertEquals(4, hash.index(4.0f));
        assertEquals(5, hash.index(5.0f));
        assertEquals(-1, hash.index(6.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);

        int index = hash.insertKey(1.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);

        index = hash.insertKey(2.0f);
        assertEquals(2, index);
        assertEquals(2.0f, hash._set[1]);
        assertEquals(1, hash._states[1]);

        index = hash.insertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(2, hash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);

        int index = hash.insertKey(1.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);

        index = hash.insertKey(2.0f);
        assertEquals(-1, index);
        assertEquals(1, hash.size());

        hash.put(2.0f, 2.0);

        index = hash.insertKey(3.0f);
        assertEquals(2, index);
        assertEquals(3.0f, hash._set[2]);
        assertEquals(1, hash._states[2]);

        assertEquals(3, hash.size());
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);

        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);

        index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2.0f, hash._set[1]);
        assertEquals(1, hash._states[1]);

        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(2, hash.size());
    }

    @Test
    public void testXinsertKeyWithCollision() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);

        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);

        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(1, hash.size());

        hash.put(1.0f, 1.0);

        index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hash._set[1]);
        assertEquals(1, hash._states[1]);
        assertEquals(2.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);

        assertEquals(2, hash.size());
    }

    @Test
    public void testXinsertKeyWithCollisionAndRemove() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);

        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);

        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(1, hash.size());

        hash.put(1.0f, 1.0);

        index = hash.XinsertKey(2.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash._states[0]);
        assertEquals(2.0f, hash._set[1]);
        assertEquals(1, hash._states[1]);

        hash.remove(1.0f);

        index = hash.XinsertKey(3.0f);
        assertEquals(1, index);
        assertEquals(3.0f, hash._set[1]);
        assertEquals(1, hash._states[1]);

        assertEquals(2, hash.size());
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);

        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray())));

        assertEquals(5, hash2.size());
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertTrue(hash2.contains(3.0f));
        assertTrue(hash2.contains(4.0f));
        assertTrue(hash2.contains(5.0f));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.writeExternal(out);

        ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(input);

        assertEquals(0, hash2.size());
        assertNull(hash2.getNoEntryKey());
        assertNull(hash2.getNoEntryValue());

        hash2.put(1.0f, 1.0);
        hash2.put(2.0f, 2.0);
        hash2.put(3.0f, 3.0);
        hash2.put(4.0f, 4.0);
        hash2.put(5.0f, 5.0);

        assertEquals(5, hash2.size());
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertTrue(hash2.contains(3.0f));
        assertTrue(hash2.contains(4.0f));
        assertTrue(hash2.contains(5.0f));
    }

}