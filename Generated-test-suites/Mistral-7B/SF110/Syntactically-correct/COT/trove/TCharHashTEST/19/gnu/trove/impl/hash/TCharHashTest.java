import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashConstruction() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.size());

        TCharHash hashWithInitialCapacity = new TCharHash(10);
        assertEquals(0, hashWithInitialCapacity.size());
        assertEquals(10, hashWithInitialCapacity.capacity());

        TCharHash hashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        assertEquals(0, hashWithInitialCapacityAndLoadFactor.size());
        assertEquals(10, hashWithInitialCapacityAndLoadFactor.capacity());
        assertEquals(0.75f, hashWithInitialCapacityAndLoadFactor.loadFactor(), 0.01);

        TCharHash hashWithInitialCapacityAndLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'X');
        assertEquals('X', hashWithInitialCapacityAndLoadFactorAndNoEntryValue.getNoEntryValue());
        assertEquals(0, hashWithInitialCapacityAndLoadFactorAndNoEntryValue.size());
        assertEquals('X', hashWithInitialCapacityAndLoadFactorAndNoEntryValue._set[0]);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());

        TCharHash hashWithCustomNoEntryValue = new TCharHash('Z');
        assertEquals('Z', hashWithCustomNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        assertEquals(hash.capacity(), hash.setUp(10).capacity());
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('A'));

        hash.put('A', 'A');
        assertTrue(hash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        TCharProcedure procedure = new TCharProcedure() {
            private int count = 0;

            @Override
            public boolean execute(final char character) {
                count++;
                return true;
            }
        };

        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        hash.forEach(procedure);
        assertEquals(3, procedure.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();

        // Index of an absent key
        assertEquals(-1, hash.index('A'));

        // Index of a present key
        hash.put('A', 'A');
        assertEquals(0, hash.index('A'));

        // Index of a present key after rehashing
        hash.put('B', 'B');
        hash.put('C', 'C');
        assertEquals(1, hash.index('B'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();

        // Insertion of an absent key
        hash.put('A', 'A');
        assertEquals(0, hash.indexOf('A'));

        // Insertion of a present key
        hash.put('B', 'B');
        hash.put('B', 'B'); // Should not insert a duplicate key
        assertEquals(1, hash.indexOf('B'));

        // Insertion of a key that hashes to an index with an occupied slot
        hash.put('C', 'C');
        hash.put('D', 'D');
        assertEquals(1, hash.indexOf('C'));
    }
}