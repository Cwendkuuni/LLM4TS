package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashDefaultConstructor() {
        TCharHash hash = new TCharHash();
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TCharHash hash = new TCharHash(10, 0.75f, 'N');
        assertEquals('N', hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
        assertFalse(hash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        char[] values = new char[3];
        int index = 0;
        hash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                values[index++] = character;
                return true;
            }
        });
        assertArrayEquals(new char[]{'A', 'B', 'C'}, values);
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        hash.removeAt(1);
        assertFalse(hash.contains('B'));
        assertEquals(2, hash.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithNegativeIndex() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.removeAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithIndexGreaterThanSize() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.removeAt(5);
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        assertEquals(0, hash.index('A'));
        assertEquals(1, hash.index('B'));
        assertEquals(2, hash.index('C'));
        assertEquals(-1, hash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        assertEquals(3, hash.insertKey('D'));
        assertTrue(hash.contains('D'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        hash.put('D', (Object) null);
        hash.put('E', (Object) null);
        hash.put('F', (Object) null);
        assertThrows(IllegalStateException.class, () -> hash.put('G', (Object) null));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(5);
        hash.put('A', (Object) null);
        hash.put('B', (Object) null);
        hash.put('C', (Object) null);
        hash.put('D', (Object) null);
        hash.put('E', (Object) null);
        hash.put('F', (Object) null);
        hash.put('G', (Object) null);
        assertEquals(8, hash.size());
    }
}