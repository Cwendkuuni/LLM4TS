package org.apache.commons.cli;

import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityLoadFactorAndEntryValuesConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertEquals(10, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals('A', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroLoadFactor() {
        new TFloatCharHash(5, 0.0f);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(-1, hash.insertKey(1.0f));
        hash.insertKey(1.0f);
        assertEquals(0, hash.size());
        assertEquals(1.0f, hash.get(0));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        int index = hash.insertKey(1.0f);
        assertEquals(-1, index);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash.get(0));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash.get(0));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        byte[] bytes = byteStream.toByteArray();
        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(2, deserializedHash.size());
        assertEquals(1.0f, deserializedHash.get(0));
        assertEquals(2.0f, deserializedHash.get(1));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        TFloatCharHash hash = new TFloatCharHash();
        hash.writeExternal(out);
        byte[] bytes = byteStream.toByteArray();
        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(0, hash.size());
        assertEquals(0, deserializedHash.size());

        out = new ObjectOutputStream(byteStream);
        hash.no_entry_key = 1.0f;
        hash.no_entry_value = 'A';
        hash.writeExternal(out);
        bytes = byteStream.toByteArray();
        deserializedHash = new TFloatCharHash();
        deserializedHash.no_entry_key = 1.0f;
        deserializedHash.no_entry_value = 'A';
        assertEquals(0, hash.size());
        assertEquals(0, deserializedHash.size());

        out = new ObjectInputStream(new ByteArrayInputStream(bytes));
        hash = new TFloatCharHash();
        hash.readExternal(out);
        assertEquals(0, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals('A', hash.getNoEntryValue());
        assertEquals(0, deserializedHash.size());
        assertEquals(1.0f, deserializedHash.getNoEntryKey());
        assertEquals('A', deserializedHash.getNoEntryValue());
    }
}