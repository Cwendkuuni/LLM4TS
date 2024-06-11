package gnu.trove.impl.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmpty() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new char[0], hash._set);
    }

    @Test
    public void testTCharHashInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new char[10], hash._set);
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryValue());
        assertArrayEquals(new char[13], hash._set); // 10 * 1.25
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash hash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, hash.size());
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertArrayEquals(new char[13], hash._set); // 10 * 1.25
        Arrays.fill(hash._set, noEntryValue);
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash(10);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));

        assertFalse(hash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        int count = 0;
        hash.forEach(character -> {
            assertEquals(character, hash.keyAt(count++));
        });

        assertEquals(3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        hash.removeAt(1);
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        assertEquals(1, hash.index('B'));
        assertEquals(-1, hash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        assertEquals(-1, hash.insertKey('B'));
        assertEquals(4, hash.size());

        hash.put('D', 'D');
        assertEquals(5, hash.size());
        assertEquals(-1, hash.insertKey('B'));

        hash.put('E', 'E');
        assertEquals(6, hash.size());
        assertEquals(3, hash.index('C')); // rehashed
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');

        hash.insertKeyAt(3, 'D');
        assertEquals(5, hash.size());
        assertEquals('D', hash.keyAt(3));
    }
}