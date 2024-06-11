package org.apache.commons.cli; // Assuming you want to test the TFloatDoubleHash class in the gnu.trove.impl.hash package, you need to import the package first

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        final TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertEquals(true, hash.contains(1.0f));
        assertEquals(true, hash.contains(2.0f));
        assertEquals(false, hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        final TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(key, (float) (count + 1));
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test
    public void testInsertKey() {
        final TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        assertEquals(true, hash.contains(1.0f));
        assertEquals(true, hash.contains(2.0f));
        assertEquals(true, hash.contains(3.0f));

        assertEquals(-1, hash.insertKey(1.0f)); // Should not insert a key that already exists
        assertEquals(4, hash.size());

        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());

        final float newKey = 4.0f;
        final int index = hash.insertKey(newKey);
        assertEquals(newKey, hash.get(index));
        assertEquals(newKey, hash.keyAt(index));
        assertEquals(newKey, hash.getNoEntryKey() + hash.size(), 0);
        assertEquals(5, hash.size());
    }

    @Test
    public void testRemoveAt() {
        final TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        assertEquals(true, hash.contains(1.0f));
        assertEquals(true, hash.contains(2.0f));
        assertEquals(true, hash.contains(3.0f));

        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertEquals(false, hash.contains(1.0f));
        assertEquals(true, hash.contains(2.0f));
        assertEquals(true, hash.contains(3.0f));

        hash.removeAt(2);
        assertEquals(1, hash.size());
        assertEquals(false, hash.contains(1.0f));
        assertEquals(false, hash.contains(2.0f));
        assertEquals(true, hash.contains(3.0f));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        final TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        final TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(1, hash.size());
        assertEquals(1, deserializedHash.size());

        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.get(i), deserializedHash.get(i));
        }
    }
}