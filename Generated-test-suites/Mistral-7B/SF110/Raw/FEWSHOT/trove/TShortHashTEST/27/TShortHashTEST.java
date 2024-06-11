package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.impl.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        hash.put((short) 1);
        hash.put((short) 2);
        assertEquals(2, hash.size());

        TShortHash hash2 = new TShortHash(3);
        hash2.put((short) 1);
        hash2.put((short) 2);
        hash2.put((short) 3);
        assertEquals(3, hash2.size());

        TShortHash hash3 = new TShortHash(3, 0.5f);
        hash3.put((short) 1);
        hash3.put((short) 2);
        hash3.put((short) 3);
        assertEquals(3, hash3.size());

        TShortHash hash4 = new TShortHash(3, 0.5f, (short) 1);
        hash4.put((short) 1);
        hash4.put((short) 2);
        hash4.put((short) 3);
        assertEquals(3, hash4.size());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals((short) 0, hash.getNoEntryValue());
        TShortHash hash2 = new TShortHash((short) 1);
        assertEquals((short) 1, hash2.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        assertEquals(4, hash.setUp(5));

        TShortHash hash2 = new TShortHash(3);
        assertEquals(3, hash2.setUp(5));
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));
        hash.put((short) 1);
        assertTrue(hash.contains((short) 1));

        TShortHash hash2 = new TShortHash(3);
        hash2.put((short) 1);
        hash2.put((short) 2);
        assertTrue(hash2.contains((short) 1));
        assertTrue(hash2.contains((short) 2));
        assertFalse(hash2.contains((short) 3));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
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
        hash.put((short) 3);

        hash.removeAt(1);

        assertEquals(2, hash.size());
        assertFalse(hash.contains((short) 2));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        assertEquals(1, hash.index((short) 1));
        assertEquals(2, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        assertEquals(-1, hash.insertKey((short) 1));
        hash.put((short) 1);
        assertEquals(0, hash.insertKey((short) 1));

        TShortHash hash2 = new TShortHash(3);
        assertEquals(-1, hash2.insertKey((short) 1));
        assertEquals(-1, hash2.insertKey((short) 2));
        assertEquals(-1, hash2.insertKey((short) 3));
        hash2.put((short) 1);
        hash2.put((short) 2);
        hash2.put((short) 3);
        assertEquals(0, hash2.insertKey((short) 1));
        assertEquals(1, hash2.insertKey((short) 2));
        assertEquals(2, hash2.insertKey((short) 3));

        TShortHash hash3 = new TShortHash(3, 0.5f);
        assertEquals(-1, hash3.insertKey((short) 1));
        assertEquals(-1, hash3.insertKey((short) 2));
        assertEquals(-1, hash3.insertKey((short) 3));
        hash3.put((short) 1);
        hash3.put((short) 2);
        hash3.put((short) 3);
        assertEquals(0, hash3.insertKey((short) 1));
        assertEquals(1, hash3.insertKey((short) 2));
        assertEquals(2, hash3.insertKey((short) 3));

        TShortHash hash4 = new TShortHash(3, 0.5f, (short) 0);
        assertEquals(-1, hash4.insertKey((short) 1));
        assertEquals(-1, hash4.insertKey((short) 2));
        assertEquals(-1, hash4.insertKey((short) 3));
        hash4.put((short) 1);
        hash4.put((short) 2);
        hash4.put((short) 3);
        assertEquals(0, hash4.insertKey((short) 1));
        assertEquals(1, hash4.insertKey((short) 2));
        assertEquals(2, hash4.insertKey((short) 3));
    }

}