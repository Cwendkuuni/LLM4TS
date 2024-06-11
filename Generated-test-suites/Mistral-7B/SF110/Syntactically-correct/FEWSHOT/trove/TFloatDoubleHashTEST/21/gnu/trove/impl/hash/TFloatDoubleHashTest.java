package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashEmpty() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0, hashMap.size());
        assertNull(hashMap.getNoEntryValue());
    }

    @Test
    public void testTFloatDoubleHashBasic() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(0, hashMap.size());
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        assertEquals(3, hashMap.size());
        assertTrue(hashMap.containsKey(1.0f));
        assertTrue(hashMap.containsKey(2.0f));
        assertTrue(hashMap.containsKey(3.0f));
        assertFalse(hashMap.containsKey(4.0f));

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 2.0f;
            }
        };
        assertFalse(hashMap.forEach(procedure));

        hashMap.remove(2.0f);
        assertEquals(2, hashMap.size());
        assertFalse(hashMap.containsKey(2.0f));
        assertTrue(hashMap.containsKey(1.0f));
        assertTrue(hashMap.containsKey(3.0f));
    }

    @Test
    public void testTFloatDoubleHashPutAndGet() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(0, hashMap.size());

        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        assertEquals(3, hashMap.size());

        assertEquals(1.0, hashMap.get(1.0f), 0.01);
        assertEquals(2.0, hashMap.get(2.0f), 0.01);
        assertEquals(3.0, hashMap.get(3.0f), 0.01);

        assertNull(hashMap.get(4.0f));
    }

    @Test
    public void testTFloatDoubleHashPutAndGetCollision() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(0, hashMap.size());

        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        assertEquals(5, hashMap.size());

        assertEquals(1.0, hashMap.get(1.0f), 0.01);
        assertEquals(2.0, hashMap.get(2.0f), 0.01);
        assertEquals(3.0, hashMap.get(3.0f), 0.01);
        assertEquals(4.0, hashMap.get(4.0f), 0.01);
        assertEquals(5.0, hashMap.get(5.0f), 0.01);
    }

    @Test
    public void testTFloatDoubleHashLoadFactor() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(0, hashMap.size());

        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        assertEquals(5, hashMap.size());

        TFloatDoubleHash newHashMap = new TFloatDoubleHash(5, 0.5f);
        hashMap.putAll(newHashMap);
        assertEquals(8, hashMap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testTFloatDoubleHashNoFreeOrRemovedSlotsAvailable() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        hashMap.put(6.0f, 6.0);
        hashMap.put(7.0f, 7.0);
        hashMap.put(8.0f, 8.0);
        hashMap.put(9.0f, 9.0);

        hashMap.put(10.0f, 10.0); // Should throw an IllegalStateException
    }
}