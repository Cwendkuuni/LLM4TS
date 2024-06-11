package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import java.io.*;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashCreation() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);

        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        assertNotNull(hashWithInitialCapacity);

        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);

        TFloatCharHash hashWithInitialCapacityAndLoadFactorAndValues = new TFloatCharHash(10, 0.75f, Float.NaN, (char) 0);
        assertNotNull(hashWithInitialCapacityAndLoadFactorAndValues);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char noEntryValue = hash.getNoEntryValue();
        assertEquals('\0', noEntryValue);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();

        assertFalse(hash.contains(Float.NaN));
        assertFalse(hash.contains(1.0f));

        hash.insertKey(Float.NaN);
        assertTrue(hash.contains(Float.NaN));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, Float.NaN);
                return true;
            }
        };

        assertFalse(hash.forEach(procedure));

        hash.insertKey(Float.NaN);
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();

        hash.insertKey(Float.NaN);

        int indexBeforeRemoval = hash.index(Float.NaN);
        assertEquals(0, indexBeforeRemoval);

        hash.removeAt(0);

        int indexAfterRemoval = hash.index(Float.NaN);
        assertEquals(-1, indexAfterRemoval);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();

        hash.insertKey(Float.NaN);
        assertTrue(hash.contains(Float.NaN));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));

        hash.insertKey(-1.0f);
        assertTrue(hash.contains(-1.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();

        hash.insertKey(Float.NaN);
        assertEquals(0, hash.index(Float.NaN));

        hash.insertKey(1.0f);
        assertEquals(1, hash.index(1.0f));

        hash.insertKey(-1.0f);
        assertEquals(2, hash.index(-1.0f));

        hash.insertKey(0.5f);
        assertEquals(3, hash.index(0.5f));
    }

    @Test
    public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(Float.NaN);
        hash.insertKey(1.0f);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInput = new ObjectInputStream(inputStream);

        TFloatCharHash deserializedHash = (TFloatCharHash) objectInput.readObject();
        objectInput.close();

        assertEquals(2, deserializedHash.size());
        assertTrue(deserializedHash.contains(Float.NaN));
        assertTrue(deserializedHash.contains(1.0f));
    }
}