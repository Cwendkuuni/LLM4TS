package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.impl.*;
import gnu.trove.procedure.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);

        TShortHash hash2 = new TShortHash(10);
        assertNotNull(hash2);
        assertEquals(10, hash2.size());

        TShortHash hash3 = new TShortHash(10, 0.75f);
        assertNotNull(hash3);
        assertEquals(10, hash3.capacity());
        assertEquals(7, (int) (hash3.capacity() * 0.75f));

        TShortHash hash4 = new TShortHash(10, 0.75f, (short) 1);
        assertNotNull(hash4);
        assertEquals(10, hash4.capacity());
        assertEquals(7, (int) (hash4.capacity() * 0.75f));
        assertEquals((short) 1, hash4.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(11);
        assertEquals(11, hash.capacity());
        assertArrayEquals(new short[11], hash._set);
        assertNotNull(hash._states);
        assertEquals(11, hash._states.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));

        assertFalse(hash.contains((short) 4));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals((short) (count++), value);
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TShortHash(-1);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();

        short key = (short) 1;
        int index = hash.insertKey(key);
        assertEquals(0, index);
        assertEquals(key, hash.get(key));

        key = (short) 2;
        index = hash.insertKey(key);
        assertEquals(1, index);
        assertEquals(key, hash.get(key));

        key = (short) 1;
        index = hash.insertKey(key);
        assertEquals(-1, index);
        assertNull(hash.get(key));

        key = (short) 3;
        index = hash.insertKey(key);
        assertEquals(2, index);
        assertEquals(key, hash.get(key));

        key = (short) 1;
        index = hash.insertKey(key);
        assertEquals(1, index);
        assertEquals(key, hash.get(key));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();

        short key = (short) 1;
        hash.insertKeyAt(0, key);
        assertEquals(key, hash.get(key));

        key = (short) 2;
        hash.insertKeyAt(1, key);
        assertEquals(key, hash.get(key));

        key = (short) 3;
        hash.insertKeyAt(2, key);
        assertEquals(key, hash.get(key));

        key = (short) 0;
        hash.insertKeyAt(3, key);
        assertEquals(key, hash.get(key));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();

        short key1 = (short) 1;
        short key2 = (short) 2;
        short key3 = (short) 3;

        hash.put(key1);
        hash.put(key2);
        hash.put(key3);

        assertEquals(key1, hash.get(key1));
        assertEquals(key2, hash.get(key2));
        assertEquals(key3, hash.get(key3));

        hash.removeAt(0);
        assertEquals(key2, hash.get(key1));
        assertEquals(key3, hash.get(key2));
        assertEquals(null, hash.get(key1));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();

        short key1 = (short) 1;
        short key2 = (short) 2;
        short key3 = (short) 3;

        hash.put(key1);
        hash.put(key2);
        hash.put(key3);

        assertEquals(key1, hash.get(key1));
        assertEquals(key2, hash.get(key2));
        assertEquals(key3, hash.get(key3));

        assertEquals(-1, hash.index((short) 0));
        assertEquals(0, hash.index(key1));
        assertEquals(1, hash.index(key2));
        assertEquals(2, hash.index(key3));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();

        short key1 = (short) 1;
        short key2 = (short) 2;
        short key3 = (short) 3;

        hash.put(key1);
        hash.put(key2);
        hash.put(key3);

        assertEquals(-1, hash.indexRehashed((short) 0, 0, 0, (byte) 0));
        assertEquals(0, hash.indexRehashed(key1, 0, 0, (byte) 0));
        assertEquals(1, hash.indexRehashed(key2, 0, 0, (byte) 0));
        assertEquals(2, hash.indexRehashed(key3, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();

        short key1 = (short) 1;
        short key2 = (short) 2;
        short key3 = (short) 3;

        hash.put(key1);
        hash.put(key2);
        hash.put(key3);

        hash.put((short) 4);

        assertEquals(-1, hash.index((short) 0));
        assertEquals(0, hash.index(key1));
        assertEquals(1, hash.index(key2));
        assertEquals(2, hash.index(key3));
        assertEquals(3, hash.index((short) 4));

        assertEquals(0, hash.get(key1));
        assertEquals(1, hash.get(key2));
        assertEquals(2, hash.get(key3));
        assertEquals(4, hash.get((short) 4));
    }
}