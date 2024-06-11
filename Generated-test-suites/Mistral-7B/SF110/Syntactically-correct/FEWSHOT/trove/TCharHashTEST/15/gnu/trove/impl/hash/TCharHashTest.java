package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmptyConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacityConstructor() {
        TCharHash<Character> hash = new TCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorConstructor() {
        TCharHash<Character> hash = new TCharHash(10, 0.75f);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size()); // default initial capacity for load factor 0.75
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        char noEntryValue = 'N';
        TCharHash<Character> hash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size()); // default initial capacity for load factor 0.75
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertTrue(hash.contains(noEntryValue));
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash<Character> hash = new TCharHash();
        hash.setNoEntryValue('N');
        assertEquals('N', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash<Character> hash = new TCharHash(5);
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
    }

    @Test
    public void testContains() {
        TCharHash<Character> hash = new TCharHash();
        hash.put('A', '1');
        hash.put('B', '2');
        hash.put('C', '3');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
        assertFalse(hash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash<Character> hash = new TCharHash();
        hash.put('A', '1');
        hash.put('B', '2');
        hash.put('C', '3');
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char charVal) {
                assertEquals('A', charVal);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash<Character> hash = new TCharHash();
        hash.put('A', '1');
        hash.put('B', '2');
        hash.put('C', '3');
        hash.removeAt(1);
        assertFalse(hash.containsKey('B'));
        assertFalse(hash.isEmpty());
        assertEquals('1', hash.get('A'));
        assertEquals('3', hash.get('C'));
    }

    @Test
    public void testIndex() {
        TCharHash<Character> hash = new TCharHash();
        hash.put('A', '1');
        hash.put('B', '2');
        hash.put('C', '3');
        assertEquals(1, hash.index('B'));
        assertEquals(-1, hash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash<Character> hash = new TCharHash();
        hash.put('A', '1');
        hash.put('B', '2');
        hash.put('C', '3');
        assertEquals(-1, hash.insertKey('A'));
        assertEquals(-1, hash.insertKey('B'));
        assertEquals(-1, hash.insertKey('C'));
        assertEquals(4, hash.size());
        assertEquals('1', hash.get('A'));
        assertEquals('2', hash.get('B'));
        assertEquals('3', hash.get('C'));
        assertEquals('4', hash.get('D'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullHashTable() {
        TCharHash<Character> hash = new TCharHash(3);
        hash.put('A', '1');
        hash.put('B', '2');
        hash.put('C', '3');
        hash.put('D', '4');
        // this will throw an IllegalStateException since the hash table is full
        hash.put('E', '5');
    }
}