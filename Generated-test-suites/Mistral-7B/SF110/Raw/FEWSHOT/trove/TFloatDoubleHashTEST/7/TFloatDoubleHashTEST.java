package org.apache.commons.cli;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        hashMap.put((float) 1.0, 1.0);
        hashMap.put((float) 2.0, 2.0);
        hashMap.put((float) 3.0, 3.0);

        assertTrue(hashMap.contains((float) 1.0));
        assertTrue(hashMap.contains((float) 2.0));
        assertTrue(hashMap.contains((float) 3.0));

        assertFalse(hashMap.contains((float) 4.0));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        hashMap.put((float) 1.0, 1.0);
        hashMap.put((float) 2.0, 2.0);
        hashMap.put((float) 3.0, 3.0);

        int count = 0;
        hashMap.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals((float) (count + 1), key);
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        hashMap.put((float) 1.0, 1.0);
        hashMap.put((float) 2.0, 2.0);
        hashMap.put((float) 3.0, 3.0);

        assertEquals((float) 1.0, hashMap.get((float) 1.0));
        hashMap.removeAt(0);

        assertNull(hashMap.get((float) 1.0));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        hashMap.put((float) 1.0, 1.0);
        hashMap.put((float) 2.0, 2.0);
        hashMap.put((float) 3.0, 3.0);

        assertEquals(0, hashMap.index((float) 1.0));
        assertEquals(1, hashMap.index((float) 2.0));
        assertEquals(2, hashMap.index((float) 3.0));
        assertEquals(-1, hashMap.index((float) 4.0));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);

        hashMap.put((float) 1.0, 1.0);
        hashMap.put((float) 2.0, 2.0);
        hashMap.put((float) 3.0, 3.0);

        assertEquals((float) 4.0, hashMap.put((float) 4.0, 4.0));

        assertEquals((float) 1.0, hashMap.get((float) 1.0));
        assertEquals((float) 2.0, hashMap.get((float) 2.0));
        assertEquals((float) 3.0, hashMap.get((float) 3.0));
        assertEquals((float) 4.0, hashMap.get((float) 4.0));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        hashMap.put((float) 1.0, 1.0);
        hashMap.put((float) 2.0, 2.0);
        hashMap.put((float) 3.0, 3.0);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hashMap.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TFloatDoubleHash deserializedHashMap = (TFloatDoubleHash) objectInput.readObject();
        objectInput.close();

        assertEquals(hashMap.size(), deserializedHashMap.size());
        for (int i = 0; i < hashMap.size(); i++) {
            assertEquals(hashMap.get(i), deserializedHashMap.get(i));
        }
    }
}