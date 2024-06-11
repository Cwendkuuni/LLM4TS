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
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertFalse(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNotNull(hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertFalse(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNotNull(hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.loadFactor(), 0.01f);
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 42);
        assertFalse(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNotNull(hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.loadFactor(), 0.01f);
        assertEquals((short) 42, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));
        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                count++;
                return true;
            }
        });
        assertTrue(hash.isEmpty());
        assertEquals(0, count);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                count++;
                return true;
            }
        });
        assertEquals(2, hash.size());
        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);

        assertEquals(2, hash.size());
        assertTrue(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));

        hash.removeAt(0);

        assertEquals(1, hash.size());
        assertFalse(hash.containsKey((short) 1));
        assertTrue(hash.containsKey((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);

        assertEquals(1, hash.index((short) 1));
        assertEquals(1, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 3));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);

        assertEquals(-1, hash.indexRehashed((short) 3, 0, 0, (byte) 0));
        assertEquals(1, hash.indexRehashed((short) 1, 0, 0, (byte) 0));
        assertEquals(1, hash.indexRehashed((short) 1, 1, 0x789734ab, (byte) 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.insertKey((short) 1); // should throw an IllegalStateException
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.insertKeyRehash((short) 4, 0, 0, (byte) 0); // should throw an IllegalStateException
    }

}