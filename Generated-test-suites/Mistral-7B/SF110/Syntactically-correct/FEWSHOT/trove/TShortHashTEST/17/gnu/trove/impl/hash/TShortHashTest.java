package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.procedure.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testConstructors() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);

        TShortHash hash1 = new TShortHash(10);
        assertNotNull(hash1);
        assertEquals(10, hash1.size());

        TShortHash hash2 = new TShortHash(10, 0.75f);
        assertNotNull(hash2);
        assertEquals(10, hash2.capacity());
        assertEquals(0.75f, hash2.loadFactor(), 0.01f);

        TShortHash hash3 = new TShortHash(10, 0.75f, (short) 1);
        assertNotNull(hash3);
        assertEquals(10, hash3.capacity());
        assertEquals(0.75f, hash3.loadFactor(), 0.01f);
        assertEquals((short) 1, hash3.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(10);
        int capacity = hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(10);
        assertTrue(hash.contains((short) 0));
        assertFalse(hash.contains((short) 1));

        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals(count++, value);
                return true;
            }
        });
        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        hash.removeAt(2);
        assertFalse(hash.contains((short) 2));
        assertEquals(3, hash.size());
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        assertEquals(3, hash.index((short) 3));
        assertEquals(-1, hash.index((short) 5));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);

        assertEquals(-1, hash.insertKey((short) 1));
        assertEquals(5, hash.size());

        assertEquals(5, hash.insertKey((short) 5));
        assertEquals(6, hash.size());
    }
}