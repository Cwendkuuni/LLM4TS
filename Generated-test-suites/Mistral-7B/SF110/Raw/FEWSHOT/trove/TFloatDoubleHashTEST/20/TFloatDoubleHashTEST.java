package org.apache.commons.cli; // Make sure to replace the package name with the actual one if it's different.

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size()); // Approximate size based on load factor
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryKeyAndNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, Float.NaN, Double.NaN);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size()); // Approximate size based on load factor
        assertEquals(Float.NaN, hash.getNoEntryKey());
        assertEquals(Double.NaN, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.capacity());
        hash.setUp(5);
        assertEquals(5, hash.capacity());
        assertNotNull(hash._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
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
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertEquals(2, hash.size());
        hash.removeAt(1);
        assertEquals(1, hash.size());
        assertNull(hash.get(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.capacity());
        assertEquals(0, hash.size());
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.size());
        assertEquals(1.0f, hash.get(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        assertEquals(3, hash.size());
        assertEquals(-1, hash.insertKey(4.0f));
        assertEquals(4, hash.size());
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TFloatDoubleHash hashFromSerial = (TFloatDoubleHash) objectInput.readObject();
        objectInput.close();

        assertEquals(2, hashFromSerial.size());
        assertEquals(1.0f, hashFromSerial.get(1.0f));
        assertEquals(2.0f, hashFromSerial.get(2.0f));
    }

    @Test
    public void testConsumeFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.capacity());
        assertEquals(0, hash.size());
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.size());
        assertEquals(true, hash.consumeFreeSlot);
        hash.consumeFreeSlot = false;

        assertEquals(-1, hash.insertKey(2.0f));
        assertEquals(2, hash.size());
        assertEquals(false, hash.consumeFreeSlot);
    }
}