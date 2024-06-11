package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.1f));
        assertFalse(hash.contains(0.2f));
        assertEquals(0, hash.size());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testHashWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);

        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
        assertTrue(hash.contains(0.3f));
        assertTrue(hash.contains(0.4f));
        assertTrue(hash.contains(0.5f));
        assertEquals(5, hash.size());
        assertFalse(hash.isEmpty());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testHashWithLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);
        hash.put(0.6f, 6.0);

        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
        assertTrue(hash.contains(0.3f));
        assertTrue(hash.contains(0.4f));
        assertTrue(hash.contains(0.5f));
        assertTrue(hash.contains(0.6f));
        assertEquals(6, hash.size());
        assertFalse(hash.isEmpty());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testHashWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 5.0f, 0.1f, 0.0);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);

        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
        assertTrue(hash.contains(0.3f));
        assertTrue(hash.contains(0.4f));
        assertTrue(hash.contains(0.5f));
        assertEquals(5, hash.size());
        assertFalse(hash.isEmpty());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testHashWithInitialCapacityRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);
        hash.put(0.6f, 6.0);
        hash.put(0.7f, 7.0);

        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
        assertTrue(hash.contains(0.3f));
        assertTrue(hash.contains(0.4f));
        assertTrue(hash.contains(0.5f));
        assertTrue(hash.contains(0.6f));
        assertTrue(hash.contains(0.7f));
        assertEquals(7, hash.size());
        assertFalse(hash.isEmpty());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testHashWithLoadFactorRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);
        hash.put(0.6f, 6.0);
        hash.put(0.7f, 7.0);
        hash.put(0.8f, 8.0);
        hash.put(0.9f, 9.0);

        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
        assertTrue(hash.contains(0.3f));
        assertTrue(hash.contains(0.4f));
        assertTrue(hash.contains(0.5f));
        assertTrue(hash.contains(0.6f));
        assertTrue(hash.contains(0.7f));
        assertTrue(hash.contains(0.8f));
        assertTrue(hash.contains(0.9f));
        assertEquals(9, hash.size());
        assertFalse(hash.isEmpty());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testHashWithInitialCapacityAndLoadFactorRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 5.0f, 0.1f, 0.0);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        hash.put(0.4f, 4.0);
        hash.put(0.5f, 5.0);
        hash.put(0.6f, 6.0);
        hash.put(0.7f, 7.0);
        hash.put(0.8f, 8.0);
        hash.put(0.9f, 9.0);
        hash.put(1.0f, 10.0);

        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
        assertTrue(hash.contains(0.3f));
        assertTrue(hash.contains(0.4f));
        assertTrue(hash.contains(0.5f));
        assertTrue(hash.contains(0.6f));
        assertTrue(hash.contains(0.7f));
        assertTrue(hash.contains(0.8f));
        assertTrue(hash.contains(0.9f));
        assertTrue(hash.contains(1.0f));
        assertEquals(10, hash.size());
        assertFalse(hash.isEmpty());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }
}