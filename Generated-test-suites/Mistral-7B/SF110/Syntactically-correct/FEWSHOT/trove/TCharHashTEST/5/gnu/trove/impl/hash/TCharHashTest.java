package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        char[] set = charHash._set;
        assertTrue(set.length > 0);
        Arrays.fill(set, Constants.DEFAULT_CHAR_NO_ENTRY_VALUE);
    }

    @Test
    public void testTCharHashInitialCapacityConstructor() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
        assertNull(charHash.getNoEntryValue());
        char[] set = charHash._set;
        Arrays.fill(set, '\0');
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(5, 0.75f);
        assertEquals(5, charHash.capacity());
        assertEquals(7, charHash.size()); // initial size based on load factor
        assertNull(charHash.getNoEntryValue());
        char[] set = charHash._set;
        Arrays.fill(set, '\0');
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorAndNoEntryValueConstructor() {
        TCharHash charHash = new TCharHash(5, 0.75f, 'X');
        assertEquals(5, charHash.capacity());
        assertEquals(7, charHash.size()); // initial size based on load factor
        assertEquals('X', charHash.getNoEntryValue());
        char[] set = charHash._set;
        Arrays.fill(set, 'X');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        TCharHash charHash = new TCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        TCharHash charHash = new TCharHash(5, -0.5f);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertFalse(charHash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char charVal) {
                assertEquals('A', charVal);
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.removeAt(1);
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('B'));
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('C'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        int index = charHash.index('A');
        assertEquals(0, index);
        index = charHash.index('B');
        assertEquals(1, index);
        index = charHash.index('C');
        assertEquals(2, index);
        index = charHash.index('D');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(10);
        char key = 'A';
        char value = 'A';
        charHash.put(key, value);
        assertEquals(key, charHash.get(key));
        assertEquals(1, charHash.size());
        charHash.put(key, 'B');
        assertEquals('B', charHash.get(key));
        assertEquals(1, charHash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(3);
        char key = 'A';
        charHash.put(key, 'A');
        int index = charHash.index(key);
        assertEquals(0, index);
        charHash.put('B', 'B');
        index = charHash.index(key);
        assertEquals(1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.put('D', 'D');
    }
}