package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.1f));
        int initialCapacity = hash.capacity();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatDoubleHash hash = new TFloatDoubleHash(11);
        assertFalse(hash.contains(0.1f));
        hash.put(0.1f, 1.2);
        assertTrue(hash.contains(0.1f));
        assertEquals(1, hash.size());
        assertEquals(1.2, hash.get(0.1f));
        assertNull(hash.get(0.2f));
    }

    @Test
    public void testLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        for (float i = 0.0f; i < 6.0f; i += 0.1f) {
            hash.put(i, i);
        }
        assertEquals(6, hash.size());
        float key;
        for (int i = 0; i < 5; i++) {
            key = (float) (Math.random() * 6.0);
            hash.put(key, key);
        }
        assertEquals(11, hash.capacity());
        assertFalse(hash.contains(6.1f));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(0.1f, 0.1);
        hash.put(0.2f, 0.2);
        hash.put(0.3f, 0.3);
        hash.put(0.4f, 0.4);
    }

    @Test
    public void testRemove() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.2);
        hash.put(0.2f, 2.3);
        hash.put(0.3f, 3.4);
        hash.remove(0.1f);
        assertFalse(hash.contains(0.1f));
        assertEquals(2, hash.size());
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.2);
        hash.put(0.2f, 2.3);
        hash.put(0.3f, 3.4);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 0.2f, 0.001f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.2);
        hash.put(0.2f, 2.3);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutput);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInput.readObject();
        assertEquals(hash.size(), deserializedHash.size());
        for (float key : hash.keySet()) {
            assertEquals(hash.get(key), deserializedHash.get(key));
        }
    }

}