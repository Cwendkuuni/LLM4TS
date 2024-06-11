package org.apache.commons.cli;

import gnu.trove.impl.hash.*; // Import the TFloatDoubleHash package
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, (float) (count++ + 1.0));
                return true;
            }
        });

        assertEquals(2, count);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int index = hash.insertKey(1.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());

        index = hash.insertKey(2.0f);
        assertEquals(3, hash.size());
        assertEquals(2.0f, hash.get(1));
        assertEquals(2.0f, hash.get(2));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int index = hash.insertKey(1.0f);
        assertEquals(1, index);
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());

        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f);
        hash.insertKey(7.0f);
        hash.insertKey(8.0f);
        hash.insertKey(9.0f);

        index = hash.insertKey(10.0f);
        assertEquals(-5, index);

        index = hash.insertKey(5.5f);
        assertEquals(-3, index);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertFalse(hash.contains(1.0f));
        assertEquals(2.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());
        assertEquals(2.0f, hash.get(0));
    }

    @Test
    public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        byte[] bytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(inputStream);
        TFloatDoubleHash hashFromStream = (TFloatDoubleHash) in.readObject();

        assertEquals(2, hashFromStream.size());
        assertEquals(1.0f, hashFromStream.get(0));
        assertEquals(2.0f, hashFromStream.get(1));
    }
}