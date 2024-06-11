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
        assertNull(hash.contains((short) 0));
    }

    @Test
    public void testTShortHashInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 100);
        assertEquals(10, hash.size());
        assertEquals((short) 100, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        final int initialCapacity = 5;
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(final int initialCapacity) {
                return super.setUp(initialCapacity + 2);
            }
        };
        assertEquals(initialCapacity + 2, hash.size());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 0));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        short count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(final short value) {
                count++;
                return true;
            }
        });
        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.removeAt(0);
        assertFalse(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        assertEquals(0, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 0));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        assertEquals(-1, hash.indexRehashed((short) 0, 0, 0, (byte) 0));
        assertEquals(2, hash.indexRehashed((short) 2, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        assertEquals(-2, hash.insertKey((short) 1));
        assertEquals(3, hash.size());
        assertEquals(1, hash.index((short) 1));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        assertEquals(-1, hash.insertKeyRehash((short) 0, 0, 0, (byte) 0));
        assertEquals(4, hash.size());
        assertEquals(3, hash.index((short) 3));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TShortHash hash = new TShortHash(1);
        for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
            hash.put(i);
        }
    }

    @Test
    public void testArrayCreation() {
        TShortHash hash = new TShortHash(10);
        short[] set = hash._set;
        assertEquals(10, set.length);
        Arrays.fill(set, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
    }

}