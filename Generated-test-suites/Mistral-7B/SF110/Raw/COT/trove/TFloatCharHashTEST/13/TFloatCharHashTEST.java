package com.example.junit4; // replace with your package name

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructors() {
        TFloatCharHash hash = new TFloatCharHash();
        TFloatCharHash hashWithCapacity = new TFloatCharHash(10);
        TFloatCharHash hashWithCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        TFloatCharHash hashWithInitialCapacityLoadFactorAndKeys = new TFloatCharHash(10, 0.75f, 1.0f, 'A');

        assertNotNull(hash);
        assertEquals(0, hash.size());
        assertEquals(10, hashWithCapacity.capacity());
        assertEquals(0.75f, hashWithCapacityAndLoadFactor.loadFactor(), 0.01f);
        assertEquals(1.0f, hashWithInitialCapacityLoadFactorAndKeys.no_entry_key);
        assertEquals('A', hashWithInitialCapacityLoadFactorAndKeys.no_entry_value);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.capacity());
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertTrue(hash.contains(0.0f));
        hash.insert(0.0f, 'A');
        assertFalse(hash.contains(0.0f));
        assertTrue(hash.containsKey(0.0f));
        hash.remove(0.0f);
        assertFalse(hash.containsKey(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');

        int count = 0;
        TFloatProcedure procedure = procedure -> count++;
        hash.forEach(procedure);

        assertEquals(2, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');

        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');
        hash.insert(3.0f, 'C');

        assertEquals(1, hash.indexRehashed(1.0f, 1, 1, (byte) 1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 1, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insert(1.0f);
        assertEquals(1, hash.size());
        assertEquals(1.0f, hash.get(0));

        hash.insert(2.0f);
        assertEquals(2, hash.size());
        assertEquals(1.0f, hash.get(0));
        assertEquals(2.0f, hash.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWhenNoFreeOrRemovedSlotsAvailable() {
        TFloatCharHash hash = new TFloatCharHash(2);
        hash.insert(1.0f);
        hash.insert(2.0f);
        hash.insert(3.0f);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);

        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));

        assertEquals(2, deserializedHash.size());
        assertEquals(1.0f, deserializedHash.get(0));
        assertEquals(2.0f, deserializedHash.get(1));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));

        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(input);

        assertEquals(0, deserializedHash.size());

        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');
        deserializedHash.insert(1.0f, 'A');
        deserializedHash.insert(2.0f, 'B');

        assertEquals(2, deserializedHash.size());
        assertEquals(1.0f, deserializedHash.get(0));
        assertEquals(2.0f, deserializedHash.get(1));
    }
}