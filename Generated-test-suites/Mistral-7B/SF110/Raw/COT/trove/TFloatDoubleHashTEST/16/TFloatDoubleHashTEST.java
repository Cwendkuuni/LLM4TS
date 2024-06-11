package com.example.junit; // replace with your testing package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);

        TFloatDoubleHash hashWithCapacity = new TFloatDoubleHash(10);
        assertNotNull(hashWithCapacity);
        assertEquals(10, hashWithCapacity.size());

        TFloatDoubleHash hashWithLoadFactor = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hashWithLoadFactor);
        assertEquals(10, hashWithLoadFactor.capacity());
        assertEquals(0.75f, hashWithLoadFactor.loadFactor(), 0.01f);

        TFloatDoubleHash hashWithInitialCapacityLoadFactor = new TFloatDoubleHash(10, 0.75f, 0f, 0d);
        assertNotNull(hashWithInitialCapacityLoadFactor);
        assertEquals(10, hashWithInitialCapacityLoadFactor.capacity());
        assertEquals(0.75f, hashWithInitialCapacityLoadFactor.loadFactor(), 0.01f);
    }

    @Test
    public void testGetNoEntryKeyAndValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        assertFalse(hash.contains(4.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        hash.forEach(procedure);

        assertEquals(3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);

        assertEquals(-1, hash.insertKey(0.0f));
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2, hash.insertKey(3.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(byteArrayOutputStream);
        try {
            hash.writeExternal(output);
            output.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            TFloatDoubleHash hashRead = new TFloatDoubleHash();
            hashRead.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

            assertEquals(3, hashRead.size());
            assertTrue(hashRead.contains(1.0f));
            assertTrue(hashRead.contains(2.0f));
            assertTrue(hashRead.contains(3.0f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(byteArrayOutputStream);
        try {
            hash.writeExternal(output);
            output.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            TFloatDoubleHash hashRead = new TFloatDoubleHash();
            hashRead.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

            assertEquals("Serial version ID", hash.serialVersionUID, hashRead.serialVersionUID);
            assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
            assertEquals(0.0, hash.getNoEntryValue(), 0.0);
            assertEquals(0, hash.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}