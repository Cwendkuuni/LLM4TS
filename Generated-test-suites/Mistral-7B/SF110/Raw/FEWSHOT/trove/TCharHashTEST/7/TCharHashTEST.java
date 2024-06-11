package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.TCharProcedure;
import gnu.trove.impl.hash.TCharHash;

public class TCharHashTest {

    @Test
    public void testTCharHashEmpty() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'x';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertFalse(charHash.isEmpty());

        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');

        assertEquals(5, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));

        assertFalse(charHash.contains('f'));
    }

    @Test
    public void testTCharHashPutAndGet() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');

        assertEquals(5, charHash.size());

        assertEquals('a', charHash.get('a'));
        assertEquals('b', charHash.get('b'));
        assertEquals('c', charHash.get('c'));
        assertEquals('d', charHash.get('d'));
        assertEquals('e', charHash.get('e'));

        assertNull(charHash.get('f'));
    }

    @Test
    public void testTCharHashRemove() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');

        assertEquals(5, charHash.size());

        charHash.remove('b');

        assertEquals(4, charHash.size());

        assertFalse(charHash.contains('b'));
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
    }

    @Test
    public void testTCharHashContains() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');

        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));

        assertFalse(charHash.contains('f'));
    }

    @Test
    public void testTCharHashForEach() {
        TCharHash charHash = new TCharHash(5);

        charHash.put('a');
        charHash.put('b');
        charHash.put('c');
        charHash.put('d');
        charHash.put('e');

        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        };

        charHash.forEach(procedure);

        assertEquals(5, procedure.getCount());
    }
}