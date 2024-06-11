package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);

        TFloatDoubleHash hashWithInitialCapacity = new TFloatDoubleHash(10);
        assertNotNull(hashWithInitialCapacity);

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactorAndNoEntry = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
        assertNotNull(hashWithInitialCapacityAndLoadFactorAndNoEntry);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.setUp(10));
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        hash.forEach(procedure);

        assertEquals(2, count);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(2, hash.indexRehashed(2.0f, 1, 1, 1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 1, 1));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));

        hash = new TFloatDoubleHash(10);
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2, hash.insertKey(2.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);

        TFloatDoubleHash deserializedHash = new TFloatDoubleHash();
        deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));

        assertEquals(2, deserializedHash.size());
        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
    }

    @Test
    public void testReadExternal() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        TFloatDoubleHash hash = new TFloatDoubleHash();

        hash.writeExternal(out);
        byteStream.reset();
        ObjectInput in = new ObjectInputStream(byteStream);

        hash.readExternal(in);

        assertEquals(0, hash.size());
    }
}