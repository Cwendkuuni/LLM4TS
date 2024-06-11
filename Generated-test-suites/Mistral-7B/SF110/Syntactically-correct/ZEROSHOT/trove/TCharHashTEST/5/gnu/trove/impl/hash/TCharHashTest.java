package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

import gnu.trove.TCharHash;
import gnu.trove.TCharHash.TCharProcedure;
import gnu.trove.impl.hash.Constants;
import gnu.trove.procedure.TCharProcedureAdapter;

public class TCharHashTest {

    @Test
    public void testEmptyTCharHash() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testInitialCapacityTCharHash() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
    }

    @Test
    public void testInitialCapacityAndLoadFactorTCharHash() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(13, charHash.size()); // 10 + 3(extra)
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueTCharHash() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(13, charHash.size()); // 10 + 3(extra)
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());

        charHash.put('A');
        charHash.put('B');
        charHash.put('C');

        assertEquals(3, charHash.size());
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertFalse(charHash.contains('D'));
    }

    @Test
    public void testContainsTCharHash() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertTrue(charHash.contains('D'));
        assertTrue(charHash.contains('E'));
        assertFalse(charHash.contains('F'));
    }

    @Test
    public void testForEachProcedureTCharHash() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        int count = 0;
        TCharHash.TCharProcedure procedure = new TCharProcedureAdapter<Character>() {
            @Override
            public boolean execute(Character charObj) {
                count++;
                return true;
            }
        };

        charHash.forEach(procedure);

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAtTCharHash() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        char removed = charHash.remove('D');
        assertEquals('D', removed);
        assertEquals(4, charHash.size());
        assertFalse(charHash.contains('D'));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNegativeIndexTCharHash() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        charHash.remove(-1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNullKeyTCharHash() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        charHash.remove((char) 0);
    }

    @Test
    public void testPutAndSizeTCharHash() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('A');
        assertEquals(1, charHash.size());
        charHash.put('B');
        assertEquals(2, charHash.size());
        charHash.put('C');
        assertEquals(3, charHash.size());
        charHash.put('D');
        assertEquals(4, charHash.size());
        charHash.put('E');
        assertEquals(5, charHash.size());
        charHash.put('F');
        assertEquals(6, charHash.size());
    }

    @Test
    public void testPutAndContainsTCharHash() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('A');
        assertTrue(charHash.contains('A'));
        charHash.put('B');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        charHash.put('C');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        charHash.put('D');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertTrue(charHash.contains('D'));

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char key = (char) random.nextInt();
            charHash.put(key);
            assertTrue(charHash.contains(key));
        }
    }

    @Test
    public void testSizeAfterRemoveTCharHash() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        char removed = charHash.remove('D');
        assertEquals(4, charHash.size());
        assertFalse(charHash.contains('D'));

        assertEquals('D', removed);
    }

    @Test
    public void testClearTCharHash() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');

        charHash.clear();
        assertEquals(0, charHash.size());
        assertTrue(charHash.isEmpty());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testResizeTCharHash() {
        TCharHash charHash = new TCharHash();

        for (int i = 0; i < 30; i++) {
            charHash.put((char) i);
        }

        assertEquals(32, charHash.size());
        assertEquals(32, charHash._set.length);

        charHash.put((char) 33);
        int hash = HashFunctions.hash((char) 33) & Integer.MAX_VALUE;
        int index = hash % charHash._states.length;
        byte state = charHash._states[index];

        assertTrue(state > 0);
        assertTrue(charHash._set[index] == (char) 33);
    }

    @Test
    public void testPutAndGetNoEntryValueTCharHash() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash();
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');

        assertNull(charHash.getNoEntryValue());
        charHash.put(noEntryValue);
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testConsumeFreeSlotTCharHash() {
        TCharHash charHash = new TCharHash();

        charHash.put('A');
        charHash.put('B');
        charHash.put('C');

        charHash.put('D');
        assertEquals(4, charHash.size());

        charHash.put('E');
        assertEquals(5, charHash.size());

        char removed = charHash.remove('B');
        assertEquals(4, charHash.size());
        assertFalse(charHash.contains('B'));

        charHash.put('F');
        assertEquals(5, charHash.size());

        charHash.put('G');
        assertEquals(6, charHash.size());

        charHash.put('H');
        assertEquals(7, charHash.size());
    }
}