package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashEmpty() {
        TShortHash hash = new TShortHash();

        assertTrue(hash.isEmpty());
        assertEquals(0, hash.size());
    }

    @Test
    public void testTShortHashBasic() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());
        assertTrue(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));
        assertTrue(hash.containsKey((short) 3));

        assertEquals((short) 1, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));

        short value = hash.get((short) 4);
        assertNull(value);

        hash.remove((short) 1);
        assertFalse(hash.containsKey((short) 1));
    }

    @Test
    public void testTShortHashPutAll() {
        TShortHash hash = new TShortHash(5);

        ShortArrayList list = new ShortArrayList();
        list.add((short) 1);
        list.add((short) 2);
        list.add((short) 3);

        hash.putAll(list);

        assertEquals(3, hash.size());
        assertTrue(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));
        assertTrue(hash.containsKey((short) 3));

        assertEquals((short) 1, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));
    }

    @Test
    public void testTShortHashLoadFactor() {
        TShortHash hash = new TShortHash(5);

        for (int i = 0; i < 10; i++) {
            hash.put((short) i, (short) i);
        }

        assertEquals(10, hash.size());
        assertFalse(hash.isEmpty());

        assertEquals(0.4f, hash.loadFactor(), 0.01f);

        TShortHash newHash = new TShortHash(15);
        hash.putAll(newHash);

        assertEquals(10, hash.size());
    }

    @Test
    public void testTShortHashWithNoEntryValue() {
        TShortHash hash = new TShortHash(5, (short) -1);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(-1, hash.getNoEntryValue());
        assertEquals(3, hash.size());
        assertTrue(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));
        assertTrue(hash.containsKey((short) 3));

        short value = hash.get((short) 4);
        assertNull(value);

        hash.remove((short) 1);
        assertFalse(hash.containsKey((short) 1));
    }

    @Test
    public void testTShortHashCustomNoEntryValue() {
        TShortHash hash = new TShortHash(5, (short) 0);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(0, hash.getNoEntryValue());
        assertEquals(3, hash.size());
        assertTrue(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));
        assertTrue(hash.containsKey((short) 3));

        short value = hash.get((short) 4);
        assertNull(value);

        hash.remove((short) 1);
        assertFalse(hash.containsKey((short) 1));
    }

    @Test
    public void testTShortHashPutNullKey() {
        TShortHash hash = new TShortHash(5);

        try {
            hash.put((short) null, (short) 1);
            fail();
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void testTShortHashPutNullValue() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) null);
        assertEquals((short) 0, hash.get((short) 1));
        assertNull(hash.get((short) null));
    }

    @Test
    public void testTShortHashForEach() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        ShortProcedure procedure = new ShortProcedure() {
            int count = 0;

            @Override
            public boolean execute(short short1) {
                count++;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTShortHashForEachConcurrentModification() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        ShortProcedure procedure = new ShortProcedure() {
            int count = 0;
            int index = -1;

            @Override
            public boolean execute(short short1) {
                count++;
                if (index == -1) {
                    index = hash.indexOfKey(short1);
                }
                hash.removeAt(index);
                return true;
            }
        };

        hash.forEach(procedure);
    }

    @Test
    public void testTShortHashRemoveAt() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        hash.removeAt(0);
        assertEquals(2, hash.size());
        assertFalse(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));
        assertTrue(hash.containsKey((short) 3));

        hash.removeAt(2);
        assertEquals(1, hash.size());
        assertTrue(hash.containsKey((short) 2));
        assertFalse(hash.containsKey((short) 3));

        hash.removeAt(-1);
        assertEquals(1, hash.size());
        assertFalse(hash.containsKey((short) 2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTShortHashRemoveAtOutOfBounds() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.removeAt(5);
    }

    @Test
    public void testTShortHashClear() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());
        hash.clear();
        assertEquals(0, hash.size());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testTShortHashResize() {
        TShortHash hash = new TShortHash(5);

        for (int i = 0; i < 15; i++) {
            hash.put((short) i, (short) i);
        }

        assertEquals(15, hash.size());

        TShortHash newHash = new TShortHash(25);
        hash.putAll(newHash);

        assertEquals(15, hash.size());
    }

    @Test
    public void testTShortHashRehash() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        TShortHash newHash = new TShortHash(31);
        hash.putAll(newHash);

        assertEquals(3, hash.size());
    }
}