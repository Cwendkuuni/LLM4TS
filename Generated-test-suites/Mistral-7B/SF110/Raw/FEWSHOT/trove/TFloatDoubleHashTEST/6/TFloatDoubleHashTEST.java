package org.apache.commons.cli;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.1f));
        assertEquals(0, hash.size());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);

        assertEquals(3, hash.size());
        assertTrue(hash.containsKey(0.1f));
        assertTrue(hash.containsKey(0.2f));
        assertTrue(hash.containsKey(0.3f));

        assertEquals(1.0, hash.get(0.1f));
        assertEquals(2.0, hash.get(0.2f));
        assertEquals(3.0, hash.get(0.3f));

        assertNull(hash.get(0.4f));

        hash.remove(0.1f);
        assertEquals(2, hash.size());
        assertFalse(hash.containsKey(0.1f));
        assertEquals(2.0, hash.get(0.2f));
        assertEquals(3.0, hash.get(0.3f));
    }

    @Test
    public void testLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.5f);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);

        assertEquals(5, hash.size());
        assertTrue(hash.containsKey(0.1f));
        assertTrue(hash.containsKey(0.2f));
        assertTrue(hash.containsKey(0.3f));
        assertTrue(hash.containsKey(0.4f));

        assertEquals(1.0, hash.get(0.1f));
        assertEquals(2.0, hash.get(0.2f));
        assertEquals(3.0, hash.get(0.3f));
        assertEquals(4.0, hash.get(0.4f));

        hash.put(0.5f, 5.0);
        assertEquals(6, hash.size());
        assertTrue(hash.containsKey(0.5f));
        assertEquals(1.0, hash.get(0.1f));
        assertEquals(2.0, hash.get(0.2f));
        assertEquals(3.0, hash.get(0.3f));
        assertEquals(4.0, hash.get(0.4f));
        assertEquals(5.0, hash.get(0.5f));

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key == 0.1f || key == 0.2f || key == 0.3f;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.5f);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);
        hash.put(0.6f, 6.0);
    }
}