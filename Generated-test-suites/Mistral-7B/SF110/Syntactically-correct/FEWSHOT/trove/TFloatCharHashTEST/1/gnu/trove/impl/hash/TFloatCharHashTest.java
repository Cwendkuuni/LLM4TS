package org.apache.commons.cli; // This package is just for reference, update it accordingly

import static org.junit.Assert.*;
import gnu.trove.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertTrue(hash.isEmpty());
        assertEquals(0, hash.size());
    }

    @Test
    public void testInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.5f, hash.loadFactor(), 0.01f);
    }

    @Test
    public void testNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.01f);
    }

    @Test
    public void testNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char noEntryValue = hash.getNoEntryValue();
        assertEquals('\0', noEntryValue);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(1.0f, value, 0.01f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        char removedValue = hash.removeAt(1);
        assertEquals('b', removedValue);
        assertEquals(2, hash.size());
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        int index = hash.index(1.0f);
        assertEquals(1, index);
        index = hash.index(4.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        assertEquals(3, hash.size());
        int index = hash.insertKey(4.0f);
        assertEquals(4, hash.size());
        assertEquals(1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f); // should trigger rehash
        int index = hash.insertKey(5.0f);
        assertEquals(5, hash.size());
        assertEquals(-2, index); // the position of the removed entry
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f); // should trigger rehash
        hash.put(5.0f); // should trigger an exception
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.writeExternal(out);
        out.close();
        byte[] bytes = baos.toByteArray();
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        TFloatCharHash hash2 = (TFloatCharHash) in.readObject();
        in.close();
        assertEquals(hash.size(), hash2.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.getKey(i), hash2.getKey(i), 0.01f);
            assertEquals(hash.getValue(i), hash2.getValue(i));
        }
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.writeExternal(out);
        out.close();
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        TFloatCharHash hash2 = (TFloatCharHash) in.readObject();
        in.close();
        assertEquals(hash.size(), hash2.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.getKey(i), hash2.getKey(i), 0.01f);
            assertEquals(hash.getValue(i), hash2.getValue(i));
        }
    }
}