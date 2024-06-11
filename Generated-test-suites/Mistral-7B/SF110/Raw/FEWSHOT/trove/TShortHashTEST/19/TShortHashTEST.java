package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.procedure.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashDefaultConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityConstructor() {
        final int initialCapacity = 10;
        TShortHash hash = new TShortHash(initialCapacity);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorConstructor() {
        final int initialCapacity = 10;
        final float loadFactor = 0.75f;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(loadFactor, hash.loadFactor(), 0.001f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        final int initialCapacity = 10;
        final float loadFactor = 0.75f;
        final short noEntryValue = -1;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(loadFactor, hash.loadFactor(), 0.001f);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(10);
        assertTrue(hash.contains((short) 0));
        hash.put((short) 0, (short) 1);
        assertFalse(hash.contains((short) 0));
        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(10);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals(value, (short) 1);
                return true;
            }
        };
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.forEach(procedure);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(10);
        hash.put((short) 0, (short) 1);
        hash.put((short) 1, (short) 2);
        hash.put((short) 2, (short) 3);
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains((short) 2));
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(10);
        hash.put((short) 0, (short) 1);
        hash.put((short) 1, (short) 2);
        hash.put((short) 2, (short) 3);
        assertEquals(1, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenFull() {
        TShortHash hash = new TShortHash(2);
        hash.put((short) 0, (short) 1);
        hash.put((short) 1, (short) 2);
        hash.put((short) 2, (short) 3);
        hash.put((short) 3, (short) 4);
    }
}