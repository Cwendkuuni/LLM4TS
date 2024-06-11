package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.0f));
        int capacity = hash.capacity();
        assertEquals(0, capacity);
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testBasicFunctionality() {
        TFloatDoubleHash hash = new TFloatDoubleHash(11);
        float key = 1.0f;
        hash.insert(key, 1.0);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(key));
        assertEquals(1.0, hash.get(key), 0.0);
        assertEquals(1.0, hash.getNoEntryValue(), 0.0);

        key = 2.0f;
        hash.insert(key, 2.0);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(key));
        assertEquals(2.0, hash.get(key), 0.0);

        key = 1.0f;
        assertTrue(hash.contains(key));
        assertEquals(1.0, hash.get(key), 0.0);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                if (value == 1.0) {
                    fail("Expected value 2.0");
                }
                return true;
            }
        };
        assertFalse(hash.forEach(procedure));

        hash.remove(key);
        assertEquals(1, hash.size());
        assertFalse(hash.contains(key));
    }

    @Test
    public void testRehashing() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        float key = 0.5f;
        hash.insert(key, 0.5);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(key));
        assertEquals(0.5, hash.get(key), 0.0);

        key = 1.5f;
        hash.insert(key, 1.5);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(key));
        assertEquals(1.5, hash.get(key), 0.0);
        assertTrue(hash.contains(0.5));
        assertEquals(0.5, hash.get(key), 0.0);

        hash = new TFloatDoubleHash(7);
        hash.putAll(hash);
        assertEquals(2, hash.size());

        key = 2.0f;
        hash.insert(key, 2.0);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(key));
        assertEquals(2.0, hash.get(key), 0.0);
        assertTrue(hash.contains(0.5));
        assertEquals(0.5, hash.get(key), 0.0);
        assertTrue(hash.contains(1.5));
        assertEquals(1.5, hash.get(key), 0.0);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(1.5f, 1.5);
        hash.put(2.5f, 2.5);
        hash.put(3.5f, 3.5);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(hash);
        objStream.close();

        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream objInput = new ObjectInputStream(byteInput);

        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objInput.readObject();
        objInput.close();

        assertEquals(3, deserializedHash.size());
        assertTrue(deserializedHash.contains(1.5f));
        assertEquals(1.5, deserializedHash.get(1.5), 0.0);
        assertTrue(deserializedHash.contains(2.5f));
        assertEquals(2.5, deserializedHash.get(2.5), 0.0);
        assertTrue(deserializedHash.contains(3.5f));
        assertEquals(3.5, deserializedHash.get(3.5), 0.0);
    }
}