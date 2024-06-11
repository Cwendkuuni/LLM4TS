package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testTFloatCharHashConstruction() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);

        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        assertNotNull(hashWithInitialCapacity);

        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);

        TFloatCharHash hashWithInitialCapacityAndLoadFactorAndKeys = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertNotNull(hashWithInitialCapacityAndLoadFactorAndKeys);
    }

    @org.junit.Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
    }

    @org.junit.Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.setUp(5));
    }

    @org.junit.Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                return ++count > 2;
            }
        };
        assertFalse(hash.forEach(procedure));

        procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                assertEquals(Float.valueOf(floatValue), Float.valueOf(1.0f));
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testRemoveAtNegativeIndex() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.removeAt(-1);
    }

    @org.junit.Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.index(3.0f);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInsertNegativeKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(-1.0f);
    }

    @org.junit.Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
        TFloatCharHash readHash = new TFloatCharHash();
        readHash.readExternal(objectInput);

        assertFalse(hash.equals(readHash));
        assertTrue(hash.containsAll(readHash));

        byteArrayOutputStream.close();
        byteArrayInputStream.close();
        objectInputStream.close();
        objectInput.close();
    }

    private int count = 0;

}