package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmptyConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
        assertArrayEquals(new char[10], charHash._set);
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
        assertEquals(7, charHash._set.length);
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(0, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
        assertEquals(7, charHash._set.length);
        Arrays.fill(charHash._set, noEntryValue);
    }

    @Test
    public void testGetNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTCharHashWithNegativeInitialCapacity() {
        new TCharHash(-5);
    }

    @Test
    public void testContains() {
        char key = 'A';
        TCharHash charHash = new TCharHash(5);
        charHash.insert(key);
        assertTrue(charHash.contains(key));
        assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEach() {
        char key1 = 'A';
        char key2 = 'B';
        TCharHash charHash = new TCharHash(5);
        charHash.insert(key1);
        charHash.insert(key2);
        HashSet<Character> resultSet = new HashSet<>();
        charHash.forEach(entry -> resultSet.add(entry));
        assertEquals(2, resultSet.size());
    }

    @Test
    public void testRemoveAt() {
        char key = 'A';
        TCharHash charHash = new TCharHash(5);
        charHash.insert(key);
        int index = charHash.indexOf(key);
        charHash.removeAt(index);
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testIndex() {
        char key1 = 'A';
        char key2 = 'B';
        TCharHash charHash = new TCharHash(5);
        charHash.insert(key1);
        charHash.insert(key2);
        int index1 = charHash.indexOf(key1);
        int index2 = charHash.indexOf(key2);
        assertEquals(index1, 0);
        assertEquals(index2, 1);
    }

    @Test
    public void testInsertKey() {
        char key = 'A';
        TCharHash charHash = new TCharHash(5);
        int index = charHash.insertKey(key);
        assertEquals(index, 0);
        assertTrue(charHash.contains(key));
    }

    @Test
    public void testRehash() {
        char key = 'A';
        TCharHash charHash = new TCharHash(5);
        charHash.insertKey(key);
        charHash.insertKey(key);
        charHash.insertKey(key);
        charHash.insertKey(key);
        charHash.insertKey(key);
        charHash.insertKey(key);
        charHash.insertKey(key);
        charHash.insertKey(key);
        int index = charHash.insertKey(key);
        assertEquals(index, -1);
        assertEquals(12, charHash._set.length);
        assertTrue(charHash.contains(key));
    }

    @Test
    public void testRandomlyInsertAndRetrieveKeys() {
        int size = 1000;
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(size, 0.75f, noEntryValue);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char key = (char) random.nextInt(128);
            charHash.insert(key);
        }
        for (int i = 0; i < size; i++) {
            char key = (char) random.nextInt(128);
            assertTrue(charHash.contains(key));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        char key = 'A';
        TCharHash charHash = new TCharHash(5);
        for (int i = 0; i < 6; i++) {
            charHash.insertKey(key);
        }
        charHash.insertKey(key);
    }
}