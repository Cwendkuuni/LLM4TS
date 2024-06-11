package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHash() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
    }

    @Test
    public void testTCharHash_InitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        assertEquals(7, charHash.initialCapacity());
        assertEquals(0.75f, charHash.loadFactor(), 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTCharHash_NegativeInitialCapacity() {
        new TCharHash(-1);
    }

    @Test
    public void testTCharHash_Empty() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHash_InitialCapacityAndNoEntryValue() {
        TCharHash charHash = new TCharHash(10, 'X');
        assertEquals('X', charHash.getNoEntryValue());
        assertEquals(10, charHash.size());
    }

    @Test
    public void testTCharHash_InitialCapacityLoadFactorAndNoEntryValue() {
        TCharHash charHash = new TCharHash(10, 0.75f, 'X');
        assertEquals('X', charHash.getNoEntryValue());
        assertEquals(10, charHash.size());
        assertEquals(7, charHash.initialCapacity());
        assertEquals(0.75f, charHash.loadFactor(), 0.01f);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash(10);
        char noEntryValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(10);
        final char[] set = charHash._set;
        Arrays.assertNotNull(set);
        assertEquals(10, set.length);
        assertArrayEquals(new char[10], set);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            assertTrue(charHash.contains(key));
            charHash.put(key);
        }
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            assertTrue(charHash.contains(key));
        }
        key = 'Z';
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(10);
        Set<Character> set = new HashSet<>();
        char key;
        charHash.forEach(value -> set.add(value));
        assertEquals(10, charHash.size());
        assertEquals(10, set.size());
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            assertTrue(charHash.contains(key));
            assertTrue(set.contains(key));
        }
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        for (int i = 0; i < 9; i++) {
            key = (char) ('A' + i);
            assertTrue(charHash.contains(key));
            charHash.removeAt(i);
            assertFalse(charHash.contains(key));
        }
        key = 'J';
        charHash.put('J');
        assertEquals(10, charHash.size());
        assertFalse(charHash.contains('I'));
        assertTrue(charHash.contains('J'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            int index = charHash.index(key);
            assertEquals(i, index);
        }
        key = 'Z';
        int index = charHash.index(key);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            int index = charHash.indexRehashed(key, 0, HashFunctions.hash(key), (byte) 1);
            assertEquals(-1, index);
        }
        key = 'Z';
        int index = charHash.indexRehashed(key, 0, HashFunctions.hash(key), (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            int index = charHash.insertKey(key);
            assertEquals(i, index);
            assertTrue(charHash.contains(key));
        }
        key = 'J';
        int index = charHash.insertKey(key);
        assertEquals(4, index);
        assertTrue(charHash.contains('J'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            charHash.put(key);
        }
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            int index = charHash.insertKeyRehash(key, 0, HashFunctions.hash(key), (byte) 1);
            assertEquals(-(i + 1), index);
            assertTrue(charHash.contains(key));
        }
        key = 'Z';
        int index = charHash.insertKeyRehash(key, 0, HashFunctions.hash(key), (byte) 1);
        assertEquals(-1, index);
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash charHash = new TCharHash(10);
        char key;
        for (int i = 0; i < 10; i++) {
            key = (char) ('A' + i);
            charHash.insertKeyAt(i, key);
            assertTrue(charHash.contains(key));
        }
        assertEquals(10, charHash.size());
    }
}