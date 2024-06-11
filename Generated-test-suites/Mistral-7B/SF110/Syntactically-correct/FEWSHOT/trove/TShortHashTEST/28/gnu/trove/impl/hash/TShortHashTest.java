package gnu.trove.impl.hash;

import static gnu.trove.impl.hash.TShortHash.Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashConstruction() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(0, hash.size());

        TShortHash hash2 = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash2.getNoEntryValue());
        assertEquals(0, hash2.size());

        TShortHash hash3 = new TShortHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash3.getNoEntryValue());
        assertEquals(0, hash3.size());

        TShortHash hash4 = new TShortHash(10, 0.75f, (short) 123);
        assertEquals((short) 123, hash4.getNoEntryValue());
        assertEquals(0, hash4.size());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        hash.setUp(10);
        assertEquals(10, hash.size());
        short[] set = hash._set;
        Arrays.fill(set, DEFAULT_SHORT_NO_ENTRY_VALUE);
        assertArrayEquals(set, hash._set);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));

        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));

        hash.put((short) 2, (short) 2);
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        short count = 0;

        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                count++;
                return true;
            }
        });

        assertEquals(2, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);

        assertEquals(0, hash.index((short) 1));
        assertEquals(1, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 3));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);

        assertEquals(-2, hash.insertKey((short) 1));
        assertEquals(2, hash.size());

        assertEquals(0, hash.insertKey((short) 3));
        assertEquals(3, hash.size());

        assertEquals(-3, hash.insertKey((short) 1));
        assertEquals(3, hash.size());
    }

}