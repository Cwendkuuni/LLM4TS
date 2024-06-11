package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmpty() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
        Arrays.fill(charHash._set, 0, charHash.size(), noEntryValue);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            assertFalse(charHash.contains(key));
            charHash.put(key);
        }
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            assertTrue(charHash.contains(key));
        }
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(10);
        char key;
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char charObject) {
                count++;
                return true;
            }
        };
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        charHash.forEach(procedure);
        assertEquals(11, procedure.getCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        charHash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            int index = charHash.index(key);
            assertEquals(i, index);
        }
        assertNull(charHash.index('Z'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 12; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        int index = charHash.index('B');
        assertNotNull(index);
        assertEquals(-2, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 11; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        int index = charHash.insertKey('Z');
        assertEquals(-1, index);
        key = 'Z';
        index = charHash.insertKey(key);
        assertEquals(12, charHash.size());
        assertEquals(11, Arrays.binarySearch(charHash._set, 0, charHash.size(), key));
        assertEquals(1, charHash._states[index]);
    }
}