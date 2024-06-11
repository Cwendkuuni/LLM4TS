package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertEquals(1, hash.capacity());
        short[] set = hash._set;
        assertTrue(set.length > 0);
        assertArrayEquals(new short[]{Constants.DEFAULT_SHORT_NO_ENTRY_VALUE}, set);

        TShortHash hash2 = new TShortHash(10);
        assertEquals(0, hash2.size());
        assertNull(hash2.getNoEntryValue());
        assertEquals(10, hash2.capacity());
        set = hash2._set;
        assertTrue(set.length > 0);
        assertArrayEquals(new short[10], set);

        TShortHash hash3 = new TShortHash(10, 0.75f);
        assertEquals(0, hash3.size());
        assertNull(hash3.getNoEntryValue());
        assertEquals(14, hash3.capacity()); // capacity = initialCapacity * (1 + loadFactor)
        set = hash3._set;
        assertTrue(set.length > 0);
        assertArrayEquals(new short[14], set);

        TShortHash hash4 = new TShortHash(10, 0.75f, (short) 42);
        assertEquals(0, hash4.size());
        assertEquals(42, hash4.getNoEntryValue());
        assertEquals(14, hash4.capacity());
        set = hash4._set;
        assertTrue(set.length > 0);
        assertArrayEquals(new short[14], set);
        Arrays.fill(set, 42);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        short[] set = hash._set;
        assertTrue(set.length > 0);
        assertArrayEquals(new short[10], set);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));

        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));

        hash.remove((short) 1);
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();

        TShortProcedure procedure = new TShortProcedure() {
            private int count = 0;

            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        };

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.forEach(procedure);

        assertEquals(3, hash.size());
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.removeAt(1);

        assertEquals(3, hash.size());
        assertArrayEquals(new short[]{1, 3}, hash._set);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(1, hash.index((short) 1));
        assertEquals(2, hash.index((short) 2));
        assertEquals(3, hash.index((short) 3));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();

        short value = (short) 1;
        int index = hash.insertKey(value);
        assertEquals(1, hash.size());
        assertEquals(value, hash.get(index));
        assertArrayEquals(new short[]{value}, hash._set);

        value = (short) 2;
        index = hash.insertKey(value);
        assertEquals(2, hash.size());
        assertEquals(value, hash.get(index));
        assertArrayEquals(new short[]{value, value}, hash._set);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();

        short value = (short) 1;
        int index = hash.insertKey(value);
        assertEquals(1, hash.size());
        assertEquals(value, hash.get(index));
        assertArrayEquals(new short[]{value}, hash._set);

        value = (short) 2;
        index = hash.insertKey(value);
        assertEquals(2, hash.size());
        assertEquals(value, hash.get(index));
        assertArrayEquals(new short[]{value, value}, hash._set);

        value = (short) 3;
        index = hash.insertKey(value); // table is full, need to rehash
        assertEquals(3, hash.size());
        assertEquals(value, hash.get(index));
        assertArrayEquals(new short[]{value, value, value}, hash._set);
    }
}