import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashConstruction() {
        TCharHash hash = new TCharHash();
        assertNotNull(hash);
        TCharHash hashWithInitialCapacity = new TCharHash(10);
        assertNotNull(hashWithInitialCapacity);
        TCharHash hashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);
        TCharHash hashWithInitialCapacityAndLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'X');
        assertNotNull(hashWithInitialCapacityAndLoadFactorAndNoEntryValue);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash hash = new TCharHash();
        assertEquals(hash.getNoEntryValue(), Constants.DEFAULT_CHAR_NO_ENTRY_VALUE);
        TCharHash hashWithCustomNoEntryValue = new TCharHash('Z');
        assertEquals(hashWithCustomNoEntryValue.getNoEntryValue(), 'Z');
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash hash = new TCharHash(initialCapacity);
        assertEquals(hash._set.length, initialCapacity);
        TCharHash emptyHash = new TCharHash();
        assertEquals(emptyHash._set.length, emptyHash.capacity());
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('A'));
        hash.put('A', 'A');
        assertTrue(hash.contains('A'));
        hash.put('B', 'B');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        char[] values = {'A', 'B', 'C'};
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char charValue) {
                count++;
                return true;
            }
        };
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        hash.forEach(procedure);
        assertEquals(values.length, count);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TCharHash hash = new TCharHash();
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        int indexA = hash.index('A');
        int indexB = hash.index('B');
        int indexC = hash.index('C');
        int indexNotFound = hash.index('D');
        assertEquals(indexA, 0);
        assertEquals(indexB, 1);
        assertEquals(indexC, 2);
        assertEquals(indexNotFound, -1);
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        hash.put('D', 'D');
        assertEquals(hash.size(), 4);
        hash.put('A', 'E');
        assertEquals(hash.get('A'), 'E');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyWithDuplicateKey() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('A', 'B');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInsertKeyWithFullHash() {
        TCharHash hash = new TCharHash(3);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        hash.put('D', 'D');
        hash.put('E', 'E');
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(4);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        hash.put('D', 'D');
        hash.put('E', 'E');
        hash.put('F', 'F');
        assertEquals(hash.size(), 6);
    }
}