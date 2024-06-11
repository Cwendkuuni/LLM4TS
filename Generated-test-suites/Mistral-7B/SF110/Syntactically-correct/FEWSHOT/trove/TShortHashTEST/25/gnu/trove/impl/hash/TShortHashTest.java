package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.*;

public class TShortHashTest {

    @Test
    public void testEmptyTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testInitialCapacityTShortHash() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testLoadFactorTShortHash() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testLoadFactorTShortHashWithInitialCapacityGreaterThanZero() {
        TShortHash hash = new TShortHash(20, 0.75f);
        assertEquals(20, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityTShortHash() {
        new TShortHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactorTShortHash() {
        new TShortHash(5, -0.5f);
    }

    @Test
    public void testNoEntryValueTShortHash() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) -1);
        assertEquals(10, hash.size());
        assertEquals((short) -1, hash.getNoEntryValue());
    }

    @Test
    public void testSetUpTShortHash() {
        TShortHash hash = new TShortHash(10) {
            @Override
            protected int setUp(final int initialCapacity) {
                return super.setUp(initialCapacity) + 5;
            }
        };
        assertEquals(10, hash.size());
        assertEquals(15, hash._set.length);
    }

    @Test
    public void testContainsTShortHash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.containsKey((short) 1));
        assertFalse(hash.contains((short) 2));
        assertFalse(hash.containsKey((short) 2));
    }

    @Test
    public void testForEachProcedureTShortHash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        hash.put((short) 5, (short) 5);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(final short key) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAtTShortHash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        hash.put((short) 5, (short) 5);

        hash.removeAt(2);

        assertEquals(4, hash.size());
        assertFalse(hash.containsKey((short) 3));
        assertTrue(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 4));
        assertTrue(hash.containsKey((short) 5));
    }

    @Test
    public void testIndexTShortHash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        hash.put((short) 5, (short) 5);

        assertEquals(4, hash.index((short) 3));
        assertEquals(-1, hash.index((short) 6));
    }

    @Test
    public void testInsertKeyTShortHash() {
        TShortHash hash = new TShortHash(5);

        short key = (short) 1;
        short value = (short) 1;

        int index = hash.insertKey(key);
        assertTrue(hash.containsKey(key));
        assertEquals(index, hash.index(key));

        key = (short) 2;
        value = (short) 2;

        index = hash.insertKey(key);
        assertTrue(hash.containsKey(key));
        assertEquals(index, hash.index(key));

        key = (short) 3;
        value = (short) 3;

        index = hash.insertKey(key);
        assertTrue(hash.containsKey(key));
        assertEquals(index, hash.index(key));

        key = (short) 4;
        value = (short) 4;

        index = hash.insertKey(key);
        assertTrue(hash.containsKey(key));
        assertEquals(index, hash.index(key));

        key = (short) 5;
        value = (short) 5;

        index = hash.insertKey(key);
        assertTrue(hash.containsKey(key));
        assertEquals(index, hash.index(key));
    }

    @Test
    public void testInsertKeyRehashTShortHash() {
        TShortHash hash = new TShortHash(5);

        short key = (short) 1;
        short value = (short) 1;

        hash.insertKey(key);

        key = (short) 2;
        value = (short) 2;

        hash.insertKey(key);

        key = (short) 3;
        value = (short) 3;

        hash.insertKey(key);

        key = (short) 4;
        value = (short) 4;

        hash.insertKey(key);

        key = (short) 5;
        value = (short) 5;

        hash.insertKey(key);

        key = (short) 6;
        value = (short) 6;

        index = hash.insertKey(key);
        assertTrue(hash.containsKey(key));
        assertEquals(index, hash.index(key));

        int length = hash._set.length;
        assertEquals(11, length);
    }

    @Test
    public void testInsertKeyAtTShortHash() {
        TShortHash hash = new TShortHash(5);

        short key = (short) 1;
        short value = (short) 1;

        hash.insertKeyAt(0, key);
        hash.insertKeyAt(1, key);
        hash.insertKeyAt(2, key);
        hash.insertKeyAt(3, key);
        hash.insertKeyAt(4, key);

        assertEquals(5, hash.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(key, hash._set[i]);
        }
    }
}