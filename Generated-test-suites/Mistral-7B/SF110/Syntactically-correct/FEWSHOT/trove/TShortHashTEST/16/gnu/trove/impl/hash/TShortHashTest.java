package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHashConstruction() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.getNoEntryValue());
        assertEquals(Constants.DEFAULT_INITIAL_CAPACITY, hash.size());
        assertEquals(Constants.DEFAULT_LOAD_FACTOR, hash.loadFactor(), 0.01f);

        TShortHash hash2 = new TShortHash(10);
        assertEquals(0, hash2.getNoEntryValue());
        assertEquals(10, hash2.size());
        assertEquals(0.5f, hash2.loadFactor(), 0.01f);

        TShortHash hash3 = new TShortHash(12, 0.75f);
        assertEquals(0, hash3.getNoEntryValue());
        assertEquals(12, hash3.size());
        assertEquals(0.75f, hash3.loadFactor(), 0.01f);

        TShortHash hash4 = new TShortHash(15, 0.75f, (short) 3);
        assertEquals(3, hash4.getNoEntryValue());
        assertEquals(15, hash4.size());
        assertEquals(0.75f, hash4.loadFactor(), 0.01f);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TShortHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TShortHash(10, -0.5f);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.insertKey((short) 4);

        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));

        assertFalse(hash.contains((short) 0));
        assertFalse(hash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.insertKey((short) 4);

        int count = 0;
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short arg) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);
        assertEquals(4, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.insertKey((short) 4);

        short removed = hash.removeAt(2);
        assertEquals((short) 3, removed);
        assertEquals(3, hash.size());

        removed = hash.removeAt(4);
        assertEquals((short) 0, removed);
        assertEquals(2, hash.size());
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.insertKey((short) 4);

        int index1 = hash.index((short) 1);
        int index2 = hash.index((short) 2);
        int index3 = hash.index((short) 3);
        int index4 = hash.index((short) 4);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(3, index4);

        assertNegative(hash.index((short) 0));
        assertNegative(hash.index((short) 5));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);

        short value1 = (short) 1;
        short value2 = (short) 2;
        short value3 = (short) 3;
        short value4 = (short) 4;

        int index1 = hash.insertKey(value1);
        int index2 = hash.insertKey(value2);
        int index3 = hash.insertKey(value3);
        int index4 = hash.insertKey(value4);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(3, index4);

        assertEquals(value1, hash.get((short) 1));
        assertEquals(value2, hash.get((short) 2));
        assertEquals(value3, hash.get((short) 3));
        assertEquals(value4, hash.get((short) 4));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(5);
        hash.consumeFreeSlot = false;

        short value1 = (short) 1;
        short value2 = (short) 2;
        short value3 = (short) 3;
        short value4 = (short) 4;
        short value5 = (short) 5;

        int index1 = hash.insertKey(value1);
        int index2 = hash.insertKey(value2);
        int index3 = hash.insertKey(value3);
        int index4 = hash.insertKey(value4);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(3, index4);

        assertEquals(value1, hash.get((short) 1));
        assertEquals(value2, hash.get((short) 2));
        assertEquals(value3, hash.get((short) 3));
        assertEquals(value4, hash.get((short) 4));

        hash.insertKey(value5);

        assertEquals(5, hash.size());

        int index5 = hash.index(value5);
        assertTrue(index5 >= 0 && index5 < hash._set.length);

        assertEquals(value5, hash.get((short) index5));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash(5);

        short value1 = (short) 1;
        short value2 = (short) 2;
        short value3 = (short) 3;
        short value4 = (short) 4;

        hash.insertKeyAt(0, value1);
        hash.insertKeyAt(1, value2);
        hash.insertKeyAt(2, value3);
        hash.insertKeyAt(3, value4);

        assertEquals(value1, hash.get((short) 1));
        assertEquals(value2, hash.get((short) 2));
        assertEquals(value3, hash.get((short) 3));
        assertEquals(value4, hash.get((short) 4));

        assertEquals(4, hash.size());
    }
}