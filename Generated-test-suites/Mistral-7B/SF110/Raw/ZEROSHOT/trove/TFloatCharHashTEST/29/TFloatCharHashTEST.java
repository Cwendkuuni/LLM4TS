package com.example.junit; // replace with your package name if necessary

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertFalse(hash.contains(Float.NaN));
    }

    @Test
    public void testHashWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testHashWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(14, hash.size()); // initial capacity * load factor
    }

    @Test
    public void testHashWithInitialCapacityLoadFactorAndNoEntryValues() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 0.0f, '\u0000');
        assertEquals(14, hash.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\u0000', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.size());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        assertTrue(hash.contains(Float.NaN));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(Float.NaN, key);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.removeAt(-1);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.removeAt(0);
        assertEquals(4, hash.size());
        assertFalse(hash.contains(Float.NaN));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        assertEquals(0, hash.index(Float.NaN));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.put(0.5f, 'B');
        hash.put(1.0f, 'C');
        assertEquals(-1, hash.index(1.5f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        int index = hash.insertKey(0.5f);
        assertEquals(6, hash.size());
        assertEquals(0, index);
        assertEquals(0.5f, hash.keyAt(0), 0.001f);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.put(0.5f, 'B');
        hash.put(1.0f, 'C');
        hash.put(1.5f, 'D');
        int index = hash.insertKey(2.0f);
        assertEquals(8, hash.size());
        assertEquals(5, index);
        assertEquals(2.0f, hash.keyAt(5), 0.001f);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.put(0.5f, 'B');
        hash.put(1.0f, 'C');
        hash.put(1.5f, 'D');
        int index = hash.XinsertKey(2.0f);
        assertEquals(8, hash.size());
        assertEquals(5, index);
        assertEquals(2.0f, hash.keyAt(5), 0.001f);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.put(0.5f, 'B');
        hash.put(1.0f, 'C');
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        TFloatCharHash hashFromStream = new TFloatCharHash();
        hashFromStream.readExternal(new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray())));
        assertEquals(hash.size(), hashFromStream.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.keyAt(i), hashFromStream.keyAt(i), 0.001f);
        }
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        TFloatCharHash hashFromStream = new TFloatCharHash();
        hashFromStream.readExternal(in);
        assertEquals(hash.size(), hashFromStream.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.keyAt(i), hashFromStream.keyAt(i), 0.001f);
        }
    }
}