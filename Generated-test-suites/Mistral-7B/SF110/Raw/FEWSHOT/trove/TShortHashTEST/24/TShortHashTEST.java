package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Random;

import gnu.trove.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testEmptyTShortHash() {
        TShortHash hashMap = new TShortHash();
        assertEquals(0, hashMap.size());
        assertNull(hashMap.getNoEntryValue());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash<Short> hashMap = new TShortHash<>(10);
        assertEquals(10, hashMap.size());
        assertEquals((short) 0, hashMap.getNoEntryValue());
        assertFalse(hashMap.isEmpty());

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        assertEquals(3, hashMap.size());
        assertTrue(hashMap.containsKey((short) 1));
        assertTrue(hashMap.containsKey((short) 2));
        assertTrue(hashMap.containsKey((short) 3));

        Short value = hashMap.get((short) 1);
        assertEquals((short) 1, value);
    }

    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash<Short> hashMap = new TShortHash<>(10, 0.75f);
        assertEquals(10, hashMap.size());
        assertEquals((short) 0, hashMap.getNoEntryValue());
        assertFalse(hashMap.isEmpty());

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        hashMap.put((short) 4, (short) 4);
        hashMap.put((short) 5, (short) 5);
        hashMap.put((short) 6, (short) 6);
        hashMap.put((short) 7, (short) 7);
        hashMap.put((short) 8, (short) 8);
        hashMap.put((short) 9, (short) 9);

        assertEquals(9, hashMap.size());
        assertTrue(hashMap.containsKey((short) 1));
        assertTrue(hashMap.containsKey((short) 2));
        assertTrue(hashMap.containsKey((short) 3));
        assertTrue(hashMap.containsKey((short) 4));
        assertTrue(hashMap.containsKey((short) 5));
        assertTrue(hashMap.containsKey((short) 6));
        assertTrue(hashMap.containsKey((short) 7));
        assertTrue(hashMap.containsKey((short) 8));
        assertTrue(hashMap.containsKey((short) 9));

        Short value = hashMap.get((short) 1);
        assertEquals((short) 1, value);
    }

    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash<Short> hashMap = new TShortHash<>(10, 0.75f, (short) 100);
        assertEquals(10, hashMap.size());
        assertEquals((short) 100, hashMap.getNoEntryValue());
        assertFalse(hashMap.isEmpty());

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        hashMap.put((short) 4, (short) 4);
        hashMap.put((short) 5, (short) 5);
        hashMap.put((short) 6, (short) 6);
        hashMap.put((short) 7, (short) 7);
        hashMap.put((short) 8, (short) 8);
        hashMap.put((short) 9, (short) 9);

        assertEquals(9, hashMap.size());
        assertTrue(hashMap.containsKey((short) 1));
        assertTrue(hashMap.containsKey((short) 2));
        assertTrue(hashMap.containsKey((short) 3));
        assertTrue(hashMap.containsKey((short) 4));
        assertTrue(hashMap.containsKey((short) 5));
        assertTrue(hashMap.containsKey((short) 6));
        assertTrue(hashMap.containsKey((short) 7));
        assertTrue(hashMap.containsKey((short) 8));
        assertTrue(hashMap.containsKey((short) 9));

        Short value = hashMap.get((short) 1);
        assertEquals((short) 1, value);
    }

    @Test
    public void testTShortHashPutGetRemove() {
        TShortHash<Short> hashMap = new TShortHash<>(10);
        assertEquals(0, hashMap.size());

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        assertEquals(3, hashMap.size());
        assertTrue(hashMap.containsKey((short) 1));
        assertTrue(hashMap.containsKey((short) 2));
        assertTrue(hashMap.containsKey((short) 3));

        Short value = hashMap.get((short) 1);
        assertEquals((short) 1, value);

        hashMap.remove((short) 1);
        hashMap.remove((short) 2);

        assertEquals(1, hashMap.size());
        assertFalse(hashMap.containsKey((short) 1));
        assertFalse(hashMap.containsKey((short) 2));
        assertFalse(hashMap.containsKey((short) 3));
    }

    @Test
    public void testTShortHashPutGetRemoveAll() {
        TShortHash<Short> hashMap = new TShortHash<>(10);
        assertEquals(0, hashMap.size());

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        assertEquals(3, hashMap.size());
        assertTrue(hashMap.containsKey((short) 1));
        assertTrue(hashMap.containsKey((short) 2));
        assertTrue(hashMap.containsKey((short) 3));

        hashMap.clear();
        assertEquals(0, hashMap.size());
        assertFalse(hashMap.containsKey((short) 1));
        assertFalse(hashMap.containsKey((short) 2));
        assertFalse(hashMap.containsKey((short) 3));
    }

    @Test
    public void testTShortHashForEach() {
        TShortHash<Short> hashMap = new TShortHash<>(10);

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        int count = 0;
        hashMap.forEach(new TShortProcedure<Short>() {
            @Override
            public boolean execute(Short value) {
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test
    public void testTShortHashContains() {
        TShortHash<Short> hashMap = new TShortHash<>(10);

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        assertTrue(hashMap.contains((short) 1));
        assertTrue(hashMap.contains((short) 2));
        assertTrue(hashMap.contains((short) 3));

        assertFalse(hashMap.contains((short) 10));
    }

    @Test
    public void testTShortHashRehash() {
        TShortHash<Short> hashMap = new TShortHash<>(10);

        for (int i = 0; i < 12; i++) {
            hashMap.put((short) i, (short) i);
        }

        assertEquals(12, hashMap.size());
        assertTrue(hashMap.containsKey((short) 0));
        assertTrue(hashMap.containsKey((short) 1));
        assertTrue(hashMap.containsKey((short) 2));
        assertTrue(hashMap.containsKey((short) 3));
        assertTrue(hashMap.containsKey((short) 4));
        assertTrue(hashMap.containsKey((short) 5));
        assertTrue(hashMap.containsKey((short) 6));
        assertTrue(hashMap.containsKey((short) 7));
        assertTrue(hashMap.containsKey((short) 8));
        assertTrue(hashMap.containsKey((short) 9));
        assertTrue(hashMap.containsKey((short) 10));
        assertTrue(hashMap.containsKey((short) 11));

        Short value = hashMap.get((short) 5);
        assertEquals((short) 5, value);

        hashMap.remove((short) 5);

        assertEquals(11, hashMap.size());
        assertFalse(hashMap.containsKey((short) 5));
    }
}