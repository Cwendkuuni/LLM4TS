package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashEmpty() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertFalse(charHash.contains('\u0001'));
    }

    @Test
    public void testTCharHashInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
        char[] set = charHash._set;
        assertTrue(Arrays.isEmpty(set));
    }

    @Test
    public void testTCharHashInitialCapacityWithLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        char[] set = charHash._set;
        assertTrue(Arrays.isEmpty(set));
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorAndNoEntryValue() {
        TCharHash charHash = new TCharHash(10, 0.75f, '\u0002');
        assertEquals(10, charHash.size());
        char[] set = charHash._set;
        assertTrue(Arrays.fill(set, '\u0002').length == 10);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(5);
        int capacity = charHash.setUp(5);
        assertEquals(5, capacity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNull() {
        TCharHash charHash = new TCharHash();
        charHash.contains(null);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('\u0001');
        assertTrue(charHash.contains('\u0001'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('\u0001');
        charHash.insertKey('\u0002');
        TCharProcedure procedure = new TCharProcedure() {
            private int count = 0;

            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        };
        charHash.forEach(procedure);
        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('\u0001');
        charHash.removeAt(0);
        assertFalse(charHash.contains('\u0001'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('\u0001');
        assertEquals(0, charHash.index('\u0001'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('\u0001');
        charHash.insertKey('\u0002');
        charHash.insertKey('\u0003');
        charHash.insertKey('\u0004');
        charHash.insertKey('\u0005');
        charHash.insertKey('\u0001');
        int index = charHash.index('\u0001');
        assertEquals(4, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        int index = charHash.insertKey('\u0001');
        assertEquals(0, index);
        assertTrue(charHash.contains('\u0001'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('\u0001');
        charHash.insertKey('\u0002');
        charHash.insertKey('\u0003');
        charHash.insertKey('\u0004');
        charHash.insertKey('\u0005');
        charHash.insertKey('\u0001');
        int index = charHash.insertKey('\u0006');
        assertEquals(-1, index);
        charHash.insertKey('\u0007');
        int index2 = charHash.index('\u0006');
        assertEquals(7, index2);
    }
}