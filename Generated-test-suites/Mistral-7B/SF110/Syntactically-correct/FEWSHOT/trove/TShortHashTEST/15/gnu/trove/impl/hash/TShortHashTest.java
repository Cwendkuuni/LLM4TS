package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashEmptyConstruction() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        short[] set = hash._set;
        assertEquals(0, set.length);
    }

    @Test
    public void testTShortHashInitialCapacityConstruction() {
        TShortHash hash = new TShortHash(5);
        assertEquals(5, hash.size());
        assertNull(hash.getNoEntryValue());
        short[] set = hash._set;
        assertNotNull(set);
        assertEquals(5, set.length);
        Arrays.fill(set, (short) 0);
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorConstruction() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        short[] set = hash._set;
        assertNotNull(set);
        assertEquals(10, set.length);
        Arrays.fill(set, (short) 0);
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorNoEntryValueConstruction() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 1);
        assertEquals(10, hash.size());
        assertEquals((short) 1, hash.getNoEntryValue());
        short[] set = hash._set;
        assertNotNull(set);
        assertEquals(10, set.length);
        Arrays.fill(set, (short) 0);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash(5);
        hash.setNoEntryValue((short) 1);
        assertEquals((short) 1, hash.getNoEntryValue());
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
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                assertEquals(key, (short) 3);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        assertEquals(2, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testIndexRehashed() {
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
        int index = hash.insertKey((short) 1);
        assertEquals(1, index);
        assertEquals((short) 1, hash.get(1));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        int index = hash.insertKey((short) 5);
        assertEquals(3, index);
        assertEquals((short) 5, hash.get(3));
    }
}