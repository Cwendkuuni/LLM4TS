package org.apache.commons.cli; // Assuming the test file is in the same package as TFloatCharHash

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0, hash.size());
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testBasicOperations() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        assertEquals('a', (char) hash.get(1.0f));
        assertEquals('b', (char) hash.get(2.0f));
        assertEquals('c', (char) hash.get(3.0f));

        assertNull(hash.get(4.0f));

        hash.remove(1.0f);
        assertEquals(2, hash.size());

        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testHashCollision() {
        TFloatCharHash hash = new TFloatCharHash(11);

        hash.put(1.0f, 'a');
        hash.put(1.0f, 'b'); // Collision

        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));

        assertEquals('b', (char) hash.get(1.0f));
    }

    @Test
    public void testRehash() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.put(1.0f, 'a');
        hash.put(1.0f, 'b'); // Collision

        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));

        // Rehashing should occur when the load factor exceeds 50%
        hash.put(2.0f, 'c');

        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        assertEquals('b', (char) hash.get(1.0f));
        assertEquals('c', (char) hash.get(2.0f));
        assertNull(hash.get(1.0f)); // Get should throw KeyNotFoundException if the key is not present
    }

    @Test
    public void testOperationOrder() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(2.0f, 'b');
        hash.put(1.0f, 'a');

        assertEquals(2, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));

        assertEquals('a', (char) hash.get(1.0f));
        assertEquals('b', (char) hash.get(2.0f));
    }

    @Test
    public void testPutWithExistingKey() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(1.0f, 'b'); // Overwrite existing key

        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));

        assertEquals('b', (char) hash.get(1.0f));
    }

    @Test
    public void testPutWithNullKey() {
        TFloatCharHash hash = new TFloatCharHash(5);

        try {
            hash.put(null, 'a'); // Should throw an IllegalArgumentException
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPutWithNullValue() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, null);

        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));

        assertNull(hash.get(1.0f));
    }

    @Test
    public void testRemove() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        hash.remove(2.0f);

        assertEquals(2, hash.size());
        assertFalse(hash.contains(2.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testRemoveWithNonExistentKey() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        hash.remove(4.0f);

        assertEquals(3, hash.size());
        assertFalse(hash.contains(4.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;

            @Override
            public boolean execute(float key) {
                count++;
                assertEquals(key, 1.0f, 0.0f);
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(3, hash.size());
        assertEquals(3, procedure.getCallCount());
    }
}