package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testEmptyTCharHash() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.contains('\0'));
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testTCharHashInitialization() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
        assertEquals('\0', charHash.getNoEntryValue());
        Arrays.fill(charHash._set, '\u0001');
        assertArrayEquals(new char[]{'\\u0001', '\u0001'}, charHash.toCharArray());
        charHash.clear();
        assertEquals(0, charHash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        assertEquals('\0', charHash.getNoEntryValue());
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        assertFalse(charHash.put('n')); // should fail because the hash is full
        assertEquals(13, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertTrue(charHash.contains('g'));
        assertTrue(charHash.contains('h'));
        assertTrue(charHash.contains('i'));
        assertTrue(charHash.contains('j'));
        assertTrue(charHash.contains('k'));
        assertTrue(charHash.contains('l'));
        assertTrue(charHash.contains('m'));
        assertFalse(charHash.contains('n'));
        assertFalse(charHash.isEmpty());
        assertEquals('m', charHash.get(hashCode("m") % charHash.size()));
    }

    @Test
    public void testTCharHashWithCustomNoEntryValue() {
        TCharHash charHash = new TCharHash(10, '\u0002');
        assertEquals('\u0002', charHash.getNoEntryValue());
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        assertFalse(charHash.put('n')); // should fail because the hash is full
        assertEquals(13, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertTrue(charHash.contains('g'));
        assertTrue(charHash.contains('h'));
        assertTrue(charHash.contains('i'));
        assertTrue(charHash.contains('j'));
        assertTrue(charHash.contains('k'));
        assertTrue(charHash.contains('l'));
        assertTrue(charHash.contains('m'));
        assertFalse(charHash.contains('n'));
        assertFalse(charHash.isEmpty());
        assertEquals('m', charHash.get(hashCode("m") % charHash.size()));
        assertEquals('\u0002', charHash.get(hashCode('\u0000') % charHash.size()));
    }

    @Test
    public void testTCharHashWithCustomLoadFactor() {
        TCharHash charHash = new TCharHash(10, '\u0002', 0.5f);
        assertEquals('\u0002', charHash.getNoEntryValue());
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        charHash.put('n');
        assertFalse(charHash.put('o')); // should fail because the hash is full
        assertEquals(14, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertTrue(charHash.contains('g'));
        assertTrue(charHash.contains('h'));
        assertTrue(charHash.contains('i'));
        assertTrue(charHash.contains('j'));
        assertTrue(charHash.contains('k'));
        assertTrue(charHash.contains('l'));
        assertTrue(charHash.contains('m'));
        assertTrue(charHash.contains('n'));
        assertFalse(charHash.contains('o'));
        assertFalse(charHash.isEmpty());
        assertEquals('n', charHash.get(hashCode("n") % charHash.size()));
    }

    @Test
    public void testTCharHashForEach() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        charHash.put('n');
        charHash.put('o');
        byte[] result = new byte[charHash.size()];
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                result[TCharHashTest.indexOf(charHash, value)] = 1;
                return true;
            }
        });
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, result);
    }

    @Test
    public void testTCharHashContains() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        charHash.put('n');
        charHash.put('o');
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertTrue(charHash.contains('g'));
        assertTrue(charHash.contains('h'));
        assertTrue(charHash.contains('i'));
        assertTrue(charHash.contains('j'));
        assertTrue(charHash.contains('k'));
        assertTrue(charHash.contains('l'));
        assertTrue(charHash.contains('m'));
        assertTrue(charHash.contains('n'));
        assertTrue(charHash.contains('o'));
        assertFalse(charHash.contains('p'));
        assertFalse(charHash.contains('\u0000'));
    }

    @Test
    public void testTCharHashRemoveAt() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        charHash.put('n');
        charHash.put('o');
        charHash.removeAt(hashCode('a') % charHash.size());
        assertEquals(12, charHash.size());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertTrue(charHash.contains('g'));
        assertTrue(charHash.contains('h'));
        assertTrue(charHash.contains('i'));
        assertTrue(charHash.contains('j'));
        assertTrue(charHash.contains('k'));
        assertTrue(charHash.contains('l'));
        assertTrue(charHash.contains('m'));
        assertTrue(charHash.contains('n'));
        assertTrue(charHash.contains('o'));
    }

    @Test
    public void testTCharHashPutAndGet() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        charHash.put('n');
        charHash.put('o');
        for (char key : charHash.keySet()) {
            assertEquals(key, charHash.get(hashCode(key) % charHash.size()));
        }
    }

    @Test
    public void testTCharHashRehash() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');
        charHash.put('f');
        charHash.put('g');
        charHash.put('h');
        charHash.put('i');
        charHash.put('j');
        charHash.put('k');
        charHash.put('l');
        charHash.put('m');
        charHash.put('n');
        charHash.put('o');
        charHash.put('p');
        charHash.put('q');
        charHash.put('r');
        charHash.put('s');
        charHash.put('t');
        charHash.put('u');
        charHash.put('v');
        charHash.put('w');
        charHash.put('x');
        charHash.put('y');
        charHash.put('z');
        assertEquals(26, charHash.size());
        char key = 'a';
        int index = charHash.index(key);
        assertEquals(-1, index);
        index = charHash.indexRehashed(key, index, hashCode(key), charHash._states[index]);
        assertEquals(0, index);
        assertEquals('a', charHash.get(hashCode(key) % charHash.size()));
    }

    private static int indexOf(TCharHash charHash, char key) {
        return (charHash._states[hashCode(key) % charHash.size()] == 1 && charHash._set[hashCode(key) % charHash.size()] == key)
                ? hashCode(key) % charHash.size()
                : -1;
    }

}