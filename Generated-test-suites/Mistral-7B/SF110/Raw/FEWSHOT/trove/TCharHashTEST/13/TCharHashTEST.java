package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

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
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';

        charHash.put(key1);
        charHash.put(key2);

        assertTrue(charHash.contains(key1));
        assertTrue(charHash.contains(key2));

        charHash.remove(key1);
        assertFalse(charHash.contains(key1));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';

        charHash.put(key1);
        charHash.put(key2);

        int count = 0;
        charHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char charValue) {
                assertEquals(charValue, key1);
                ++count;
                return true;
            }
        });

        assertEquals(1, count);

        count = 0;
        charHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char charValue) {
                assertEquals(charValue, key2);
                ++count;
                return true;
            }
        });

        assertEquals(1, count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';

        charHash.put(key1);
        charHash.put(key2);

        charHash.removeAt(0);

        assertFalse(charHash.contains(key1));
        assertTrue(charHash.contains(key2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtOutOfBounds() {
        TCharHash charHash = new TCharHash();
        char key = 'A';

        charHash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';

        charHash.put(key1);
        charHash.put(key2);

        int index1 = charHash.index(key1);
        int index2 = charHash.index(key2);

        assertEquals(0, index1);
        assertEquals(1, index2);

        charHash.remove(key1);
        assertEquals(-1, charHash.index(key1));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';

        int index1 = charHash.insertKey(key1);
        int index2 = charHash.insertKey(key2);

        assertEquals(0, index1);
        assertEquals(1, index2);

        charHash.remove(key1);
        assertEquals(-1, charHash.index(key1));
        assertEquals(1, charHash.size());

        index1 = charHash.insertKey(key1);
        assertEquals(0, index1);
        assertEquals(2, charHash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        char key3 = 'C';

        charHash.put(key1);
        charHash.put(key2);
        charHash.put(key3);

        int index1 = charHash.insertKey(key1);
        int index2 = charHash.insertKey(key2);
        int index3 = charHash.insertKey(key3);

        assertEquals(-1, index1);
        assertEquals(-index3 - 1, index2);
        assertEquals(index2, index3);

        charHash.remove(key2);
        assertEquals(-1, charHash.index(key2));
        assertEquals(2, charHash.size());

        charHash.clear();
        assertEquals(0, charHash.size());
    }
}