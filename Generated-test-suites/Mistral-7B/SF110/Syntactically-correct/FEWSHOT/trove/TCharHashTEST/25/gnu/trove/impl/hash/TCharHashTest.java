package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmptyConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertArrayEquals(new char[0], charHash._set);
    }

    @Test
    public void testTCharHashInitialCapacityConstructor() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertArrayEquals(new char[10], charHash._set);
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertArrayEquals(new char[14], charHash._set); // 10 * 1.5
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorNoEntryValueConstructor() {
        char noEntryValue = 'x';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(0, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertArrayEquals(new char[14], charHash._set); // 10 * 1.5
        for (int i = 0; i < 14; i++) {
            charHash._set[i] = noEntryValue;
        }
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        char defaultNoEntryValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultNoEntryValue);

        char customNoEntryValue = 'x';
        charHash = new TCharHash(10, 0.75f, customNoEntryValue);
        assertEquals(customNoEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        assertFalse(charHash.contains('a'));
        charHash.put('a', 'a');
        assertTrue(charHash.contains('a'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();

        Set<Character> charSet = new HashSet<>();
        charHash.forEach(value -> charSet.add(value));

        assertEquals(0, charHash.size());
        assertTrue(charSet.isEmpty());

        charHash.put('a', 'a');
        charHash.put('b', 'b');

        charSet.clear();
        charHash.forEach(value -> charSet.add(value));

        assertEquals(2, charHash.size());
        assertEquals(2, charSet.size());
        assertTrue(charSet.contains('a'));
        assertTrue(charSet.contains('b'));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.put('a', 'a');
        charHash.put('b', 'b');

        assertEquals(2, charHash.size());
        assertNotNull(charHash.get('a'));
        assertNotNull(charHash.get('b'));

        charHash.removeAt(0);

        assertEquals(1, charHash.size());
        assertNotNull(charHash.get('b'));
        assertNull(charHash.get('a'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();

        char key1 = 'a';
        char key2 = 'b';
        char key3 = 'c';

        charHash.put(key1, key1);
        charHash.put(key2, key2);
        charHash.put(key3, key3);

        int index1 = charHash.index(key1);
        int index2 = charHash.index(key2);
        int index3 = charHash.index(key3);
        int index4 = charHash.index('d');

        assertEquals(3, charHash.size());
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(-1, index4);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();

        char key1 = 'a';
        char key2 = 'b';

        int index1 = charHash.insertKey(key1);
        int index2 = charHash.insertKey(key2);

        assertEquals(2, charHash.size());
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(key1, charHash.get(0));
        assertEquals(key2, charHash.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TCharHash charHash = new TCharHash(1);
        char key1 = 'a';

        charHash.put(key1, key1);
        charHash.put(key1, key1);
    }
}