package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyTFloatDoubleHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testTFloatDoubleHashWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testTFloatDoubleHashWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hash.size(), 10); // Allow for some extra capacity
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testTFloatDoubleHashWithInitialCapacityLoadFactorAndNoEntryKeyValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertEquals(10, hash.size(), 10); // Allow for some extra capacity
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            private int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKey(1.0f);
        assertEquals(1.0f, hash._set[index]);
        assertEquals(1, hash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.consumeFreeSlot = true;
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f); // Should rehash

        int index = hash.index(6.0f);
        assertEquals(6, index);
        assertEquals(6.0f, hash._set[index]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);

        TFloatDoubleHash hashFromByteArray = new TFloatDoubleHash();
        hashFromByteArray.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));

        assertEquals(2, hashFromByteArray.size());
        assertEquals(1.0f, hashFromByteArray._set[0], 0.0);
        assertEquals(2.0f, hashFromByteArray._set[1], 0.0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatDoubleHash hashFromByteArray = (TFloatDoubleHash) in.readObject();

        assertEquals(0, hashFromByteArray.size());

        out.writeByte(1); // Write the initial capacity
        out.writeInt(5); // Write the initial capacity
        out.writeFloat(1.0f); // Write no_entry_key
        out.writeDouble(2.0); // Write no_entry_value
        hashFromByteArray.readExternal(out);

        assertEquals(0, hashFromByteArray.size());
        assertEquals(1.0f, hashFromByteArray.getNoEntryKey());
        assertEquals(2.0, hashFromByteArray.getNoEntryValue(), 0.0);
        assertEquals(5, hashFromByteArray.size());
    }
}