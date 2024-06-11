package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertTrue(hash._set == null);

        TShortHash hash2 = new TShortHash(10);
        assertEquals(10, hash2.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash2.getNoEntryValue());
        assertNotNull(hash2._set);
        assertArrayEquals(new short[10], hash2._set);

        TShortHash hash3 = new TShortHash(10, 0.75f);
        assertEquals(10, hash3.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash3.getNoEntryValue());
        assertNotNull(hash3._set);
        assertArrayEquals(new short[14], hash3._set); //14 is the capacity after resizing

        TShortHash hash4 = new TShortHash(10, 0.75f, (short) 123);
        assertEquals(10, hash4.size());
        assertEquals((short) 123, hash4.getNoEntryValue());
        assertNotNull(hash4._set);
        assertArrayEquals(new short[14], hash4._set);
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

        short count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });

        assertEquals(0, count);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        hash.removeAt(1);

        assertEquals(2, hash.size());
        assertFalse(hash.contains((short) 2));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        assertEquals(1, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        assertEquals(-2, hash.insertKey((short) 0));
        assertEquals(-1, hash.insertKey((short) 4));
        assertEquals(3, hash.size());

        assertEquals(0, hash.index((short) 0));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        assertEquals(-2, hash.insertKey((short) 0));
        assertEquals(-1, hash.insertKey((short) 4));
        assertEquals(3, hash.size());

        hash.resize();

        assertEquals(7, hash.size());
        assertEquals(0, hash.index((short) 0));
        assertEquals(-1, hash.index((short) 4));
    }
}