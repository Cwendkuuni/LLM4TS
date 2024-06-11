package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmptyConstruction() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'A';
        TCharHash hash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, hash.size());
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertFalse(hash.contains('a'));

        hash.put('a');
        assertTrue(hash.contains('a'));
    }

    @Test
    public void testTCharHashContains() {
        TCharHash hash = new TCharHash();
        hash.put('a');
        hash.put('b');
        hash.put('c');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));

        assertFalse(hash.contains('d'));
    }

    @Test
    public void testTCharHashForEach() {
        TCharHash hash = new TCharHash();
        hash.put('a');
        hash.put('b');
        hash.put('c');

        int count = 0;
        hash.forEach(charVal -> count++);

        assertEquals(3, count);
    }

    @Test
    public void testTCharHashRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.put('a');
        hash.put('b');
        hash.put('c');

        assertEquals(3, hash.size());

        hash.removeAt(1);

        assertEquals(2, hash.size());
        assertFalse(hash.contains('b'));
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('c'));
    }

    @Test
    public void testTCharHashIndex() {
        TCharHash hash = new TCharHash();
        hash.put('a');
        hash.put('b');
        hash.put('c');

        assertEquals(2, hash.index('b'));
        assertEquals(-1, hash.index('d'));
    }

    @Test(expected = IllegalStateException.class)
    public void testTCharHashInsertKeyFull() {
        TCharHash hash = new TCharHash(3);
        hash.put('a');
        hash.put('b');
        hash.put('c');

        hash.put('d'); // This should throw an IllegalStateException
    }
}