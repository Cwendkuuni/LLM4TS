package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

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
    public void testBasicTCharHashOperations() {
        TCharHash charHash = new TCharHash(11);
        assertEquals(11, charHash.size());
        assertNull(charHash.getNoEntryValue());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());

        charHash.put('a', 'a');
        assertEquals(1, charHash.size());
        assertTrue(charHash.contains('a'));
        assertFalse(charHash.isEmpty());

        charHash.put('b', 'b');
        assertEquals(2, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));

        charHash.put('a', 'x');
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.contains('b'));

        charHash.put('a', 'a');
        assertEquals(2, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));

        charHash.put('c', 'c');
        assertEquals(3, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
    }

    @Test
    public void testBasicTCharHashRemovals() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');

        assertEquals(3, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));

        charHash.remove('b');
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('b'));
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('c'));

        charHash.remove('a');
        assertEquals(1, charHash.size());
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.contains('c'));

        charHash.remove('c');
        assertEquals(0, charHash.size());
        assertFalse(charHash.contains('c'));
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testBasicTCharHashForEach() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');

        int index = 0;
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                assertEquals(character, (char) (index++ % 3 + 'a'));
                return true;
            }
        };

        charHash.forEach(procedure);
        assertEquals(3, index);
    }

}