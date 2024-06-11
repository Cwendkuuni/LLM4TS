package com.example.junit4.test; // Replace with the actual package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
    }

    @Test
    public void testTFloatCharHashWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(8, hash.size()); // Based on capacity and load factor
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityLoadFactorAndNoEntryValues() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 0.5f, 'N'); // Replace with the actual no_entry_value character
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(8, hash.size()); // Based on capacity and load factor
        assertEquals(0.5f, hash.getNoEntryKey());
        assertEquals('N', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.capacity());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        assertTrue(hash.contains(2.5f));
        assertFalse(hash.contains(3.5f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(1.5f);
        hash.insert(2.5f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatVal) {
                assertEquals(1.5f, floatVal);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, hash.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtWithEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.removeAt(-1);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        hash.removeAt(0);
        assertFalse(hash.contains(2.5f));
        assertEquals(4, hash.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexWithNoEntry() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        hash.insert(3.5f);
        int index = hash.index(-1.5f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        hash.insert(3.5f);
        int index = hash.index(2.5f);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        hash.insert(3.5f);
        hash.insert(4.5f);
        int index = hash.indexRehashed(3.5f, 0, 3, (byte) 1);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        hash.insert(3.5f);
        hash.insert(4.5f);
        hash.insert(5.5f);
        assertEquals(5, hash.size());
        assertTrue(hash.contains(2.5f));
        assertTrue(hash.contains(3.5f));
        assertTrue(hash.contains(4.5f));
        assertTrue(hash.contains(5.5f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        hash.insert(3.5f);
        hash.insert(4.5f);
        hash.insert(5.5f);
        hash.insert(6.5f); // The hash is full, should throw an IllegalStateException
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(2.5f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        hash.writeExternal(objectOutput);
        byte[] bytes = out.toByteArray();
        TFloatCharHash hashFromSerial = new TFloatCharHash(5);
        hashFromSerial.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertTrue(hashFromSerial.contains(2.5f));
        assertEquals(hash, hashFromSerial); // Compare the hashes after deserialization
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        hash.writeExternal(objectOutput);
        byte[] bytes = out.toByteArray();
        TFloatCharHash hashFromSerial = new TFloatCharHash(5);
        hashFromSerial.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertTrue(hashFromSerial.contains(2.5f));
        assertEquals(hash, hashFromSerial); // Compare the hashes after deserialization
    }

}