package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testEmptyTCharHash() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());

        charHash.put('a');
        assertTrue(charHash.contains('a'));
        assertFalse(charHash.contains('b'));
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(5, 0.75f);
        assertEquals(5, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());

        charHash.put('a');
        assertTrue(charHash.contains('a'));
        assertFalse(charHash.contains('b'));

        charHash.put('b');
        assertEquals(7, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'x';
        TCharHash charHash = new TCharHash(5, 0.75f, noEntryValue);
        assertEquals(5, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());

        charHash.put('a');
        assertTrue(charHash.contains('a'));
        assertFalse(charHash.contains('b'));

        charHash.put('b');
        assertEquals(7, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
    }

    @Test
    public void testPutAndGet() {
        TCharHash charHash = new TCharHash();

        charHash.put('a');
        assertTrue(charHash.contains('a'));
        assertEquals('a', charHash.get('a'));

        charHash.put('b');
        assertTrue(charHash.contains('b'));
        assertEquals('b', charHash.get('b'));

        charHash.put('c');
        assertTrue(charHash.contains('c'));
        assertEquals('c', charHash.get('c'));

        assertEquals(3, charHash.size());
    }

    @Test
    public void testPutAndGetWithLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);

        charHash.put('a');
        assertTrue(charHash.contains('a'));
        assertEquals('a', charHash.get('a'));

        charHash.put('b');
        assertTrue(charHash.contains('b'));
        assertEquals('b', charHash.get('b'));

        charHash.put('c');
        charHash.put('d');

        assertEquals(5, charHash.size()); // Load factor is 0.75 so 7 entries is max
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));

        charHash.put('e'); // Should rehash
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('e'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();

        charHash.put('a');
        charHash.put('b');
        charHash.put('c');

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                assertEquals('a', character);
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));

        procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                if (character == 'b') {
                    fail();
                }
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));

        procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                if (character == 'c') {
                    fail();
                }
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.put('a');
        charHash.put('b');
        charHash.removeAt(0);
    }

    @Test
    public void testRemoveAtWithNoEntryValue() {
        char noEntryValue = 'x';
        TCharHash charHash = new TCharHash();
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');

        charHash.putNoEntryValue(noEntryValue);
        charHash.removeAt(1);

        assertEquals(2, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('c'));
        assertEquals(noEntryValue, charHash.get('b'));
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();

        assertFalse(charHash.contains('a'));
        charHash.put('a');
        assertTrue(charHash.contains('a'));

        assertFalse(charHash.contains('b'));
        charHash.put('b');
        assertTrue(charHash.contains('b'));

        assertFalse(charHash.contains('c'));
        charHash.put('c');
        assertTrue(charHash.contains('c'));

        assertEquals(3, charHash.size());
    }
}