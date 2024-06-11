package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.impl.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

    @Test
    public void testTShortHashEmptyConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTShortHashInitialCapacityConstructor() {
        TShortHash hash = new TShortHash(5);
        assertEquals(5, hash.size());
        assertNull(hash.getNoEntryValue());

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);
        hash.put((short) 4, (short) 4);
        hash.put((short) 5, (short) 5);

        assertEquals(5, hash.size());
        assertEquals((short) 1, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));
        assertEquals((short) 4, hash.get((short) 4));
        assertEquals((short) 5, hash.get((short) 5));
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorConstructor() {
        TShortHash hash = new TShortHash(5, 0.75f);
        assertEquals(5, hash.capacity());
        assertEquals(3, hash.size());
        assertNull(hash.getNoEntryValue());

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());
        assertEquals((short) 1, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorNoEntryValueConstructor() {
        byte noEntryValue = 0;
        TShortHash hash = new TShortHash(5, 0.75f, noEntryValue);
        assertEquals(5, hash.capacity());
        assertEquals(3, hash.size());
        assertEquals(noEntryValue, hash.getNoEntryValue());

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());
        assertEquals((short) 1, hash.get((short) 1));
        assertEquals((short) 2, hash.get((short) 2));
        assertEquals((short) 3, hash.get((short) 3));
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

        byte customNoEntryValue = 42;
        hash = new TShortHash() {{
            this.no_entry_value = customNoEntryValue;
        }};
        assertEquals(customNoEntryValue, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();

        assertFalse(hash.contains((short) 1));
        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testForEachProcedure() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short key) {
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

        hash.put((short) 4, (short) 4);
        assertEquals(4, hash.size());
        assertTrue(hash.contains((short) 4));
    }
}