package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmptyConstruction() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10);
        charHash.put('A', '1');
        charHash.put('B', '2');
        charHash.put('C', '3');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertFalse(charHash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', '1');
        charHash.put('B', '2');
        charHash.put('C', '3');

        int count = 0;
        charHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                assertEquals(Character.toString(character), "A" + Integer.toString(count++ % 3 + 1));
                return true;
            }
        });
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', '1');
        charHash.put('B', '2');
        charHash.put('C', '3');
        charHash.removeAt(1);
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('B'));
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('C'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', '1');
        charHash.put('B', '2');
        charHash.put('C', '3');
        assertEquals(1, charHash.index('B'));
        assertEquals(-1, charHash.index('D'));
    }
}