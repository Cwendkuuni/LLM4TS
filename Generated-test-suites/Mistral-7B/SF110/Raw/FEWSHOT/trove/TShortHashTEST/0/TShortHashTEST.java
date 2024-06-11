package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hashMap = new TShortHash(5);
        assertEquals(5, hashMap.capacity());
        assertNull(hashMap.getNoEntryValue());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash hashMap = new TShortHash(10, 0.75f);
        assertEquals(10, hashMap.capacity());
        assertEquals(0.75f, hashMap.loadFactor(), 0.01f);
        assertNull(hashMap.getNoEntryValue());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        TShortHash hashMap = new TShortHash(10, 0.75f, (short) 100);
        assertEquals(10, hashMap.capacity());
        assertEquals(0.75f, hashMap.loadFactor(), 0.01f);
        assertEquals((short) 100, hashMap.getNoEntryValue());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testSetUp() {
        TShortHash hashMap = new TShortHash(5);
        int capacity = hashMap.setUp(5);
        assertEquals(5, capacity);
        assertArrayEquals(new short[5], hashMap._set);
    }

    @Test
    public void testContains() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.insertKey((short) 1);
        hashMap.insertKey((short) 2);
        hashMap.insertKey((short) 3);
        hashMap.insertKey((short) 4);
        hashMap.insertKey((short) 5);

        assertTrue(hashMap.contains((short) 1));
        assertTrue(hashMap.contains((short) 2));
        assertTrue(hashMap.contains((short) 3));
        assertTrue(hashMap.contains((short) 4));
        assertTrue(hashMap.contains((short) 5));

        assertFalse(hashMap.contains((short) 0));
        assertFalse(hashMap.contains((short) 6));
    }

    @Test
    public void testForEach() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.insertKey((short) 1);
        hashMap.insertKey((short) 2);
        hashMap.insertKey((short) 3);
        hashMap.insertKey((short) 4);
        hashMap.insertKey((short) 5);

        int count = 0;
        hashMap.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.insertKey((short) 1);
        hashMap.insertKey((short) 2);
        hashMap.insertKey((short) 3);
        hashMap.insertKey((short) 4);
        hashMap.insertKey((short) 5);

        hashMap.removeAt(2);

        short[] expected = {1, 4, 5};
        assertArrayEquals(expected, hashMap._set);
    }

    @Test
    public void testIndex() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.insertKey((short) 1);
        hashMap.insertKey((short) 2);
        hashMap.insertKey((short) 3);
        hashMap.insertKey((short) 4);
        hashMap.insertKey((short) 5);

        assertEquals(3, hashMap.index((short) 3));
        assertEquals(-1, hashMap.index((short) 6));
    }

    @Test
    public void testInsertKey() {
        TShortHash hashMap = new TShortHash(5);

        hashMap.insertKey((short) 1);
        hashMap.insertKey((short) 2);
        hashMap.insertKey((short) 3);
        hashMap.insertKey((short) 4);
        hashMap.insertKey((short) 5);

        assertEquals(5, hashMap.size());

        short[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, hashMap._set);

        hashMap.consumeFreeSlot = true;
        hashMap.insertKey((short) 6);

        expected = Arrays.copyOf(expected, 6);
        expected[5] = 6;
        assertArrayEquals(expected, hashMap._set);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashmap() {
        TShortHash hashMap = new TShortHash(5);

        hashMap.insertKey((short) 1);
        hashMap.insertKey((short) 2);
        hashMap.insertKey((short) 3);
        hashMap.insertKey((short) 4);
        hashMap.insertKey((short) 5);
        hashMap.insertKey((short) 6);
    }
}