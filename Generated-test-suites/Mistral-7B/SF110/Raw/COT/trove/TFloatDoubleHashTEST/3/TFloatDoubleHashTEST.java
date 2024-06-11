package com.example.junit; // replace with your testing package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryKeyAndNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.1f, 0.0);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float expected = 0.0f;
        float actual = hash.getNoEntryKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double expected = 0.0;
        double actual = hash.getNoEntryValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int expectedCapacity = 10;
        int actualCapacity = hash.setUp(10);
        assertEquals(expectedCapacity, actualCapacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 0.1f;
        boolean expected = false;
        boolean actual = hash.contains(key);
        assertEquals(expected, actual);

        hash.insertKey(key);
        expected = true;
        actual = hash.contains(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float[] expectedValues = new float[5];
        TFloatProcedure procedure = new TFloatProcedure() {
            private int index = 0;

            @Override
            public boolean execute(float value) {
                expectedValues[index] = value;
                index++;
                return true;
            }
        };

        for (int i = 0; i < 5; i++) {
            hash.insertKey((float) i);
        }

        hash.forEach(procedure);

        for (int i = 0; i < 5; i++) {
            float expectedValue = (float) i;
            float actualValue = expectedValues[i];
            assertEquals(expectedValue, actualValue, 0.0f);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtWhenNoFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1);
        hash.insertKey(0.1f);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        // key not present
        float key = 0.1f;
        int expectedIndex = -1;
        int actualIndex = hash.index(key);
        assertEquals(expectedIndex, actualIndex);

        // key present
        hash.insertKey(key);
        expectedIndex = 0;
        actualIndex = hash.index(key);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(2);
        hash.insertKey(0.1f);

        int expectedIndex = 1;
        int actualIndex = hash.indexRehashed(0.1f, 0, 0, (byte) 1);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenNoFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1);
        hash.insertKey(0.1f);
        hash.insertKey(0.2f);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.1f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutput);

        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInput.readObject();

        float key = 0.1f;
        boolean expectedPresent = hash.contains(key);
        boolean actualPresent = deserializedHash.contains(key);
        assertTrue(expectedPresent == actualPresent);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutput);

        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        hash.readExternal(objectInput);

        float key = 0.1f;
        boolean expectedPresent = hash.contains(key);
        boolean actualPresent = hash.contains(key);
        assertTrue(expectedPresent == actualPresent);
    }
}