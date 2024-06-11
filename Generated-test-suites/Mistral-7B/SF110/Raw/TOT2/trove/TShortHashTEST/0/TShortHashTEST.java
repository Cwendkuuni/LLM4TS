import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHashDefaultConstructor() {
        // ... Expert 1's first test case ...
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        // ... Expert 1's second test case ...
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        // ... Expert 1's third test case ...
    }

    @Test
    public void testGetNoEntryValue() {
        // ... Expert 1's fourth test case ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithNegativeCapacity() {
        // Testing negative capacity
        new TShortHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithNegativeLoadFactor() {
        // Testing negative load factor
        new TShortHash(1, -0.1f);
    }

    @Test
    public void testContainsKey() {
        // Testing contains key
        TShortHash hash = new TShortHash();
        short key = Short.MIN_VALUE;
        hash.put(key);
        assertTrue(hash.containsKey(key));
        short nonExistentKey = Short.MAX_VALUE;
        assertFalse(hash.containsKey(nonExistentKey));
    }

    @Test
    public void testRemoveKey() {
        // Testing removing a key
        TShortHash hash = new TShortHash();
        short key = Short.MIN_VALUE;
        hash.put(key);
        hash.remove(key);
        assertFalse(hash.containsKey(key));
    }

    @Test
    public void testPutAndGetMultipleKeys() {
        // Testing putting and getting multiple keys
        TShortHash hash = new TShortHash();
        short key1 = Short.MIN_VALUE;
        hash.put(key1);
        short key2 = Short.MIN_VALUE + 1;
        hash.put(key2);
        assertEquals(key1, hash.get(key1));
        assertEquals(key2, hash.get(key2));
    }

    @Test
    public void testPutAndGetSameKey() {
        // Testing putting and getting the same key
        TShortHash hash = new TShortHash();
        short key = Short.MIN_VALUE;
        hash.put(key);
        hash.put(key);
        assertEquals(key, hash.get(key));
    }

    @Test
    public void testResize() {
        // Testing if the hash table is resized during insertion
        TShortHash hash = new TShortHash();
        for (int i = 0; i < 11; i++) {
            hash.put((short) i);
        }
        short[] expected = new short[16];
        assertArrayEquals(expected, hash._set);
    }

    @Test
    public void testTShortProcedureForEach() {
        // Testing the forEach method with a TShortProcedure
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        int count = 0;
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short short1) {
                count++;
                return true;
            }
        };
        hash.forEach(procedure);
        assertEquals(3, count);
    }
}