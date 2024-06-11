package org.apache.commons.cli.test; // or the appropriate package for your test cases

import static org.junit.Assert.*;

import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatDoubleHashTest {
    @Test
    public void testTFloatDoubleHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertEquals(10, hash.size());

        float key = 0.1f;
        hash.put(key, 0.1);
        assertTrue(hash.containsKey(key));
        assertEquals(0.1, hash.get(key), 0.0);
        assertEquals(1, hash.size());

        key = 0.2f;
        hash.put(key, 0.2);
        assertTrue(hash.containsKey(key));
        assertEquals(0.2, hash.get(key), 0.0);
        assertEquals(2, hash.size());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, key, 0.0);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));

        hash.remove(key);
        assertFalse(hash.containsKey(key));
        assertEquals(1, hash.size());

        hash.put(key, 0.3);
        assertTrue(hash.containsKey(key));
        assertEquals(0.3, hash.get(key), 0.0);
        assertEquals(2, hash.size());

        TFloatDoubleHash hash2 = new TFloatDoubleHash(10, 0.75f);
        hash2.put(key, 0.4);
        assertFalse(hash.containsKey(key));
        assertEquals(2, hash.size());
        assertEquals(1, hash2.size());

        hash.clear();
        assertEquals(0, hash.size());
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
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(0.1f, 0.1);
        hash.put(0.2f, 0.2);
        hash.put(0.3f, 0.3);

        float key = 0.4f;
        int index = hash.XinsertKey(key);
        assertEquals(0.4f, hash.get(key), 0.0);
        assertEquals(4, hash.size());
        assertEquals(1, index);

        key = 0.5f;
        index = hash.XinsertKey(key);
        assertEquals(0.5f, hash.get(key), 0.0);
        assertEquals(5, hash.size());
        assertEquals(3, index);

        hash.clear();
        key = 0.1f;
        index = hash.XinsertKey(key);
        assertEquals(0.1f, hash.get(key), 0.0);
        assertEquals(1, hash.size());
        assertEquals(0, index);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(0.1f, 0.1);
        hash.put(0.2f, 0.2);
        hash.put(0.3f, 0.3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        TFloatDoubleHash hash2 = (TFloatDoubleHash) in.readObject();

        assertEquals(3, hash2.size());
        assertTrue(hash2.containsKey(0.1f));
        assertTrue(hash2.containsKey(0.2f));
        assertTrue(hash2.containsKey(0.3f));
    }
}