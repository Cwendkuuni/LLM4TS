package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testEmptyTCharHash() {
        TCharHash charHash = new TCharHash(10);
        assertFalse(charHash.contains('\u0001'));
        assertEquals(0, charHash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('\u0001');
        assertTrue(charHash.contains('\u0001'));
        assertEquals(1, charHash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        charHash.put('\u0001');
        assertTrue(charHash.contains('\u0001'));
        assertEquals(1, charHash.size());
        charHash.put('\u0002');
        assertTrue(charHash.contains('\u0002'));
        assertEquals(2, charHash.size());
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = '\u00FF';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        charHash.put('\u0001');
        assertTrue(charHash.contains('\u0001'));
        assertEquals(1, charHash.size());
        charHash.put('\u0002');
        assertTrue(charHash.contains('\u0002'));
        assertEquals(2, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashContains() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('\u0001');
        assertTrue(charHash.contains('\u0001'));
        assertFalse(charHash.contains('\u0002'));
        charHash.put('\u0002');
        assertTrue(charHash.contains('\u0001'));
        assertTrue(charHash.contains('\u0002'));
    }

    @Test
    public void testTCharHashForEach() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');

        int count = 0;
        TCharHash.TCharProcedure procedure = new TCharHash.TCharProcedure() {
            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        };
        charHash.forEach(procedure);
        assertEquals(3, count);
    }

    @Test
    public void testTCharHashRemoveAt() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');

        charHash.removeAt(1);
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('b'));
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('c'));
    }

    @Test(expected = IllegalStateException.class)
    public void testTCharHashInsertKeyFull() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');

        charHash.put('d');
    }
}