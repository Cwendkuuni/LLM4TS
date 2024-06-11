package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHashCreation() {
        TShortHash hash = new TShortHash();
        TShortHash hashWithInitialCapacity = new TShortHash(10);
        TShortHash hashWithInitialCapacityAndLoadFactor = new TShortHash(10, 0.75f);
        TShortHash hashWithInitialCapacityLoadFactorAndNoEntryValue = new TShortHash(10, 0.75f, (short) 100);

        assertEquals(10, hashWithInitialCapacity.capacity());
        assertEquals(0.75f, hashWithInitialCapacityAndLoadFactor.loadFactor(), 0.01);
        assertEquals((short) 100, hashWithInitialCapacityLoadFactorAndNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        final int initialCapacity = 10;
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(final int initialCapacity) {
                return super.setUp(initialCapacity) + 5;
            }
        };

        assertEquals(initialCapacity + 5, hash.capacity());
    }

    @Test
    public void testContains() {
        final TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);

        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertFalse(hash.contains((short) 3));
    }

    @Test
    public void testForEach() {
        final TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);

        int count = 0;
        final TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(final short value) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        final TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        assertEquals((short) 3, hash.remove((short) 2));
        assertFalse(hash.contains((short) 2));
    }

    @Test
    public void testIndex() {
        final TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        assertEquals(1, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKey() {
        final TShortHash hash = new TShortHash();

        assertEquals(-1, hash.insertKey((short) 1));
        assertEquals(0, hash.insertKey((short) 1));

        assertEquals((short) 1, hash.get((short) 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        final TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);

        hash.put((short) 4);
    }
}