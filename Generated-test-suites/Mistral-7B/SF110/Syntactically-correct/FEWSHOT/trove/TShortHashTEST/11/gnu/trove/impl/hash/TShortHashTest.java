package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 123);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
        assertEquals((short) 123, hash.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        hash.no_entry_value = (short) 456;
        assertEquals((short) 456, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
        byte[] states = hash._states;
        short[] set = hash._set;
        assertNull(states);
        assertArrayEquals(new short[10], set);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.put((short) 123);
        hash.put((short) 456);
        assertTrue(hash.contains((short) 123));
        assertTrue(hash.contains((short) 456));
        assertFalse(hash.contains((short) 789));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.put((short) 123);
        hash.put((short) 456);
        TShortProcedure procedure = new TAdder();
        hash.forEach(procedure);
        assertEquals(65535, procedure.getResult());
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 123);
        hash.put((short) 456);
        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertEquals((short) 456, hash.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAtIndexOutOfBounds() {
        TShortHash hash = new TShortHash();
        hash.insertKeyAt(-1, (short) 456);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenTableIsFull() {
        TShortHash hash = new TShortHash();
        for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
            hash.put(i);
        }
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.put((short) 123);
        hash.put((short) 456);
        hash.put((short) 789);
        assertEquals(3, hash.size());
        assertEquals((short) 789, hash.get(2));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
            hash.put(i);
        }
        short key = (short) 1337;
        int index = hash.insertKey(key);
        assertEquals(1337, hash.get(index));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 123);
        hash.put((short) 456);
        hash.put((short) 789);
        assertEquals(1, hash.index((short) 123));
        assertEquals(2, hash.index((short) 456));
        assertEquals(-1, hash.index((short) 1111));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
            hash.put(i);
        }
        short key = (short) 1337;
        int index = hash.indexRehashed(key, 0, 0, (byte) 0);
        assertEquals(index, hash.index(key));
    }

    static class TAdder implements TShortProcedure {

        private int result;

        public TAdder() {
            result = 0;
        }

        public boolean execute(short value) {
            result += value;
            return true;
        }

        public short getResult() {
            return (short) result;
        }
    }
}