package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashDefaultConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityConstructor() {
        TShortHash hash = new TShortHash(5);
        assertEquals(5, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorConstructor() {
        TShortHash hash = new TShortHash(5, 0.75f);
        assertEquals(5, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorNoEntryValueConstructor() {
        TShortHash hash = new TShortHash(5, 0.75f, (short) 42);
        assertEquals(5, hash.size());
        assertEquals((short) 42, hash.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash(5);
        hash.setNoEntryValue((short) 42);
        assertEquals((short) 42, hash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TShortHash(-5);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short shortKey) {
                assertEquals((short) count++, shortKey);
                return true;
            }
        });
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        short removed = hash.removeAt(1);
        assertEquals((short) 2, removed);
        assertEquals(2, hash.size());
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        int index = hash.index((short) 1);
        assertEquals(0, index);
        index = hash.index((short) 2);
        assertEquals(1, index);
        index = hash.index((short) 3);
        assertEquals(2, index);
        index = hash.index((short) 4);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        int index = hash.insertKey((short) 4);
        assertEquals(4, index);
        assertEquals(4, hash.size());
        assertEquals((short) 4, hash.get(4));
    }
}