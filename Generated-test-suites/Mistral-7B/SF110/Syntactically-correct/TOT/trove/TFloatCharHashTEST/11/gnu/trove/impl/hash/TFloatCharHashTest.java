package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertNull(hash.getNoEntryKey());
        assertNull(hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
        assertFalse(hash.contains(Float.NaN));
    }

    @Test
    public void testBasicOperations() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.put(0.0f, 'B');
        hash.put(1.0f, 'C');

        assertFalse(hash.isEmpty());
        assertTrue(hash.contains(Float.NaN));
        assertTrue(hash.containsKey(Float.NaN));
        assertTrue(hash.containsValue('A'));
        assertEquals('A', hash.get(Float.NaN));
        assertEquals('B', hash.get(0.0f));
        assertEquals('C', hash.get(1.0f));

        assertNull(hash.get(Float.NaN));
        assertNull(hash.get(2.0f));

        hash.remove(Float.NaN);
        assertFalse(hash.containsKey(Float.NaN));
        assertNull(hash.get(Float.NaN));
        assertFalse(hash.containsValue('A'));

        hash.clear();
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(5);
        for (int i = 0; i < 6; i++) {
            hash.put(Float.intBitsToFloat(i * 2), (char) i);
        }

        assertFalse(hash.isEmpty());
        assertEquals(0.6f, hash.getLoadFactor(), 0.01f);

        TFloatCharHash hash2 = new TFloatCharHash(5, 0.75f);
        hash2.put(Float.NaN, 'A');
        hash2.put(0.0f, 'B');
        hash2.put(1.0f, 'C');
        hash2.put(2.0f, 'D');
        hash2.put(3.0f, 'E');
        hash2.put(4.0f, 'F');
        hash2.put(5.0f, 'G');
        hash2.put(6.0f, 'H');
        hash2.put(Float.intBitsToFloat(15), 'I');

        assertEquals(0.86f, hash2.getLoadFactor(), 0.01f);
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(Float.NaN, 'A');
        hash.put(0.0f, 'B');
        hash.put(1.0f, 'C');

        TFloatProcedure procedure = new TFloatProcedure() {
            private int count = 0;

            @Override
            public boolean execute(float key) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);
        assertEquals(3, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TFloatCharHash(5, -0.5f);
    }
}