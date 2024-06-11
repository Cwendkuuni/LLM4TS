package org.apache.commons.cli.gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;
import java.io.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(10, hashMap.size());
        assertNull(hashMap.getNoEntryKey());
        assertNull(hashMap.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(0.0f, hashMap.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(0.0, hashMap.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        assertTrue(hashMap.contains(1.0f));
        assertFalse(hashMap.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        FloatProcedure procedure = new FloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f, 0.0f);
                return true;
            }
        };
        hashMap.insertKey(1.0f);
        hashMap.forEach(procedure);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashSet() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(1);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        int index = hashMap.insertKey(1.0f);
        assertEquals(1.0f, hashMap.get(index));
        assertEquals(1.0f, hashMap.get(index));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(1);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        int index = hashMap.insertKey(3.0f);
        assertEquals(3.0f, hashMap.get(index));
        assertEquals(3.0f, hashMap.get(index));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        hashMap.removeAt(0);
        assertNull(hashMap.get(0));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);

        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        hashMap.writeExternal(out);

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatDoubleHash hashMapCopy = (TFloatDoubleHash) in.readObject();

        assertEquals(1.0f, hashMapCopy.get(0));
    }
}