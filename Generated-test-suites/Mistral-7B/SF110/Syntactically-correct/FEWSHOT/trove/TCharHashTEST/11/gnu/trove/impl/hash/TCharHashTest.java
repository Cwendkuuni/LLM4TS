package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHash() {
        TCharHash hashMap = new TCharHash(10);
        assertEquals(10, hashMap.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTCharHashNegativeInitialCapacity() {
        new TCharHash(-1);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash hashMap = new TCharHash(10);
        char noEntryValue = hashMap.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testContains() {
        TCharHash hashMap = new TCharHash(10);
        hashMap.put('a', 'a');
        assertTrue(hashMap.contains('a'));
        assertFalse(hashMap.contains('b'));
    }

    @Test
    public void testForEach() {
        TCharHash hashMap = new TCharHash(10);
        hashMap.put('a', 'a');
        hashMap.put('b', 'b');
        HashSet<Character> set = new HashSet<>();
        hashMap.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                set.add(character);
                return true;
            }
        });
        assertEquals(2, set.size());
        assertTrue(set.contains('a'));
        assertTrue(set.contains('b'));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hashMap = new TCharHash(10);
        hashMap.put('a', 'a');
        hashMap.put('b', 'b');
        char removedValue = hashMap.remove('b');
        assertEquals('b', removedValue);
        assertFalse(hashMap.contains('b'));
    }

    @Test
    public void testIndex() {
        TCharHash hashMap = new TCharHash(10);
        hashMap.put('a', 'a');
        hashMap.put('b', 'b');
        int index = hashMap.index('a');
        assertEquals(0, index);
        index = hashMap.index('b');
        assertEquals(1, index);
        index = hashMap.index('c');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash hashMap = new TCharHash(10);
        hashMap.put('a', 'a');
        hashMap.put('b', 'b');
        int index = hashMap.insertKey('c');
        assertEquals(2, hashMap.size());
        assertEquals(1, index);
        assertTrue(hashMap.contains('c'));
    }

    @Test
    public void testInsertKeyRehash() {
        Random random = new Random();
        TCharHash hashMap = new TCharHash(5);
        for (int i = 0; i < 10; i++) {
            hashMap.put((char) (random.nextInt(255) + 'a'), (char) (random.nextInt(255) + 'a'));
        }
        char keyToInsert = (char) (random.nextInt(255) + 'a');
        int index = hashMap.insertKey(keyToInsert);
        assertEquals(11, hashMap.size());
        assertTrue(hashMap.contains(keyToInsert));
        assertEquals(5, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyNoFreeOrRemovedSlot() {
        TCharHash hashMap = new TCharHash(5);
        for (int i = 0; i < 10; i++) {
            hashMap.put((char) (i + 'a'), (char) (i + 'a'));
        }
        hashMap.insertKey('z');
    }
}