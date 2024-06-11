package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.*;

import java.util.Arrays;

public class TCharHashTest {

    @Test
    public void testConstructor() {
        TCharHash charHash = new TCharHash();
        assertNotNull(charHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        assertEquals(0, charHash.size());

        TCharHash charHash2 = new TCharHash(50);
        assertNotNull(charHash2);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash2.getNoEntryValue());
        assertEquals(0, charHash2.size());

        TCharHash charHash3 = new TCharHash(50, 0.75f);
        assertNotNull(charHash3);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash3.getNoEntryValue());
        assertEquals(0, charHash3.size());

        TCharHash charHash4 = new TCharHash(50, 0.75f, 'X');
        assertNotNull(charHash4);
        assertEquals('X', charHash4.getNoEntryValue());
        assertEquals(0, charHash4.size());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpNegativeInitialCapacity() {
        new TCharHash(-5);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);
        charHash.put('F', 6);

        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertTrue(charHash.contains('D'));
        assertTrue(charHash.contains('E'));
        assertTrue(charHash.contains('F'));

        assertFalse(charHash.contains('G'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);
        charHash.put('F', 6);

        int count = 0;
        charHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        });

        assertEquals(6, count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);
        charHash.put('F', 6);

        char removed = charHash.remove('B');
        assertEquals('B', removed);
        assertEquals(5, charHash.size());

        assertNull(charHash.remove('G'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);
        charHash.put('F', 6);

        assertEquals(5, charHash.index('E'));
        assertEquals(-1, charHash.index('G'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(10);

        char key = 'A';
        int index = charHash.insertKey(key);
        assertEquals(1, index);
        assertEquals(1, charHash.size());
        assertEquals(key, charHash.get('A'));

        key = 'B';
        index = charHash.insertKey(key);
        assertEquals(3, index);
        assertEquals(2, charHash.size());
        assertEquals(key, charHash.get('B'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(10);

        char key = 'A';
        charHash.insertKey(key);

        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');
        charHash.put('F');

        int index = charHash.insertKey(key);
        assertEquals(-5, index);

        charHash.resize(30);

        index = charHash.insertKey(key);
        assertEquals(16, index);
        assertEquals(key, charHash.get('A'));
    }

    @Test
    public void testPutIfAbsent() {
        TCharHash charHash = new TCharHash(10);

        char key1 = 'A';
        char key2 = 'B';

        char value1 = charHash.putIfAbsent(key1, 'X');
        assertEquals(0, value1);
        assertEquals(1, charHash.size());
        assertEquals(key1, charHash.get('A'));

        char value2 = charHash.putIfAbsent(key1, 'Y');
        assertEquals(0, value2);
        assertEquals(1, charHash.size());
        assertEquals(key1, charHash.get('A'));

        char value3 = charHash.putIfAbsent(key2, 'X');
        assertEquals(0, value3);
        assertEquals(2, charHash.size());
        assertEquals(key2, charHash.get('B'));

        char value4 = charHash.putIfAbsent(key1, 'Z');
        assertEquals(key1, charHash.get('A'));
        assertEquals(2, charHash.size());
        assertEquals(value4, value1);
    }
}