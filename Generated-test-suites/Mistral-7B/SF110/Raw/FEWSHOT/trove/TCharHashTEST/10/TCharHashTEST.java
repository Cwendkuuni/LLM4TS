package gnu.trove.impl.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testEmptyTCharHash() {
        TCharHash hash = new TCharHash();
        assertTrue(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertFalse(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());

        hash.put('a', 'a');
        hash.put('b', 'b');
        hash.put('c', 'c');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertEquals(3, hash.size());

        assertFalse(hash.contains('d'));
        assertEquals(-1, hash.index('d'));

        hash.put('d', 'd');
        assertTrue(hash.contains('d'));
        assertEquals(4, hash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertFalse(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());

        hash.put('a', 'a');
        hash.put('b', 'b');
        hash.put('c', 'c');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertEquals(3, hash.size());

        assertFalse(hash.contains('d'));
        assertEquals(-1, hash.index('d'));

        hash.put('d', 'd');
        assertTrue(hash.contains('d'));
        assertEquals(4, hash.size());

        assertEquals(14, hash._set.length); // check that hash table was resized
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TCharHash hash = new TCharHash(10, 0.75f, 'x');
        assertFalse(hash.isEmpty());
        assertEquals(0, hash.size());
        assertEquals('x', hash.getNoEntryValue());

        hash.put('a', 'a');
        hash.put('b', 'b');
        hash.put('c', 'c');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertEquals(3, hash.size());

        assertFalse(hash.contains('d'));
        assertEquals(-1, hash.index('d'));

        hash.put('d', 'd');
        assertTrue(hash.contains('d'));
        assertEquals(4, hash.size());

        assertEquals('x', hash._set[0]);
        assertEquals('x', hash._set[1]);
        assertEquals('x', hash._set[2]);
        // ...
        // other elements should be the keys put in the hash
    }

    @Test
    public void testTCharHashForEach() {
        TCharHash hash = new TCharHash(5);
        hash.put('a', 'a');
        hash.put('b', 'b');
        hash.put('c', 'c');

        int count = 0;
        hash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char key) {
                assertTrue(hash.contains(key));
                assertEquals(key, hash.get(key));
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTCharHashRemoveAt() {
        TCharHash hash = new TCharHash(5);
        hash.put('a', 'a');
        hash.removeAt(0);
    }

    @Test
    public void testTCharHashRemoveAtWithIndexOutOfBoundsException() {
        TCharHash hash = new TCharHash(5);
        hash.put('a', 'a');

        try {
            hash.removeAt(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            hash.removeAt(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testTCharHashRemoveAtWithValidIndex() {
        TCharHash hash = new TCharHash(5);
        hash.put('a', 'a');
        hash.put('b', 'b');
        hash.put('c', 'c');

        assertEquals('c', hash.get('c'));
        hash.removeAt(2);
        assertEquals('a', hash.get('c')); // 'c' was removed, 'a' was at previous position
    }
}