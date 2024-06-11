package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashDefaultConstruction() {
        TCharHash charHash = new TCharHash();
        char[] set = charHash._set;
        assertEquals(0, set.length);
    }

    @Test
    public void testTCharHashInitialCapacityConstruction() {
        TCharHash charHash = new TCharHash(10);
        char[] set = charHash._set;
        assertEquals(10, set.length);
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorConstruction() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        char[] set = charHash._set;
        assertEquals(14, set.length);
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorAndNoEntryValueConstruction() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        char[] set = charHash._set;
        assertEquals(14, set.length);
        for (int i = 0; i < set.length; i++) {
            assertEquals(noEntryValue, set[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTCharHashNegativeInitialCapacity() {
        new TCharHash(-1);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.insertKey(key);
        assertTrue(charHash.contains(key));
        assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.insertKey(key);
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                assertEquals(value, character);
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.insertKey(key);
        int index = charHash.index(key);
        charHash.removeAt(index);
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testHashFunction() {
        TCharHash charHash = new TCharHash();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char key = (char) random.nextInt(256);
            int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
            int index = hash % charHash._states.length;
            int probe = 1 + hash % (charHash._set.length - 2);
            int loopIndex = index;
            do {
                index -= probe;
                if (index < 0) {
                    index += charHash._set.length;
                }
            } while (index != loopIndex);
            assertEquals(index, charHash.index(key));
        }
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        int index = charHash.insertKey(key);
        assertTrue(charHash.contains(key));
        assertEquals(index, charHash.index(key));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullKeySet() {
        TCharHash charHash = new TCharHash(2);
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            char key = (char) random.nextInt(256);
            charHash.insertKey(key);
        }
    }
}