package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testEmptyHash() {
        TCharHash charHash = new TCharHash(5);
        assertTrue(charHash.isEmpty());
        assertEquals(0, charHash.size());
        assertFalse(charHash.contains('a'));
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testHashWithInitialCapacityAndLoadFactor() {
        char expectedChar = 'e';
        TCharHash charHash = new TCharHash(5, 0.75f);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', expectedChar);
        charHash.put('f', 'f');
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertEquals(expectedChar, charHash.get('e'));
        assertFalse(charHash.contains('\u0000'));
        assertNull(charHash.getNoEntryValue());

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                if (character == 'e') {
                    fail("Expected 'e' to not be present.");
                }
                return true;
            }
        };
        assertFalse(charHash.forEach(procedure));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TCharHash(5, -0.5f);
    }

    @Test
    public void testHashWithCustomNoEntryValue() {
        char customNoEntryValue = 'x';
        TCharHash charHash = new TCharHash(5, 0.75f, customNoEntryValue);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', 'e');
        charHash.put('f', 'f');
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertEquals('e', charHash.get('e'));
        assertFalse(charHash.contains('\u0000'));
        assertEquals(customNoEntryValue, charHash.getNoEntryValue());

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                if (character == 'e') {
                    fail("Expected 'e' to not be present.");
                }
                return true;
            }
        };
        assertFalse(charHash.forEach(procedure));
    }

    @Test
    public void testPutAndGet() {
        char expectedChar = 'e';
        TCharHash charHash = new TCharHash(5);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', expectedChar);
        charHash.put('f', 'f');
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertEquals(expectedChar, charHash.get('e'));
        assertFalse(charHash.contains('\u0000'));
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testPutAndGetWithCollision() {
        char expectedChar = 'e';
        TCharHash charHash = new TCharHash(5);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', expectedChar);
        charHash.put('f', 'f');
        charHash.put('e', expectedChar);
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertEquals(expectedChar, charHash.get('e'));
        assertFalse(charHash.contains('\u0000'));
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testRemove() {
        char expectedChar = 'e';
        TCharHash charHash = new TCharHash(5);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', expectedChar);
        charHash.put('f', 'f');
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        charHash.remove('e');
        assertEquals(5, charHash.size());
        assertFalse(charHash.contains('e'));
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('f'));
        assertFalse(charHash.contains('\u0000'));
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testRemoveWithCollision() {
        char expectedChar = 'e';
        TCharHash charHash = new TCharHash(5);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', expectedChar);
        charHash.put('e', expectedChar);
        charHash.put('f', 'f');
        assertEquals(6, charHash.size());
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        charHash.remove('e');
        assertEquals(5, charHash.size());
        assertFalse(charHash.contains('e'));
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('f'));
        assertFalse(charHash.contains('\u0000'));
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', 'e');
        charHash.put('f', 'f');
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertTrue(charHash.contains('c'));
        assertTrue(charHash.contains('d'));
        assertTrue(charHash.contains('e'));
        assertTrue(charHash.contains('f'));
        assertFalse(charHash.contains('\u0000'));
    }

    @Test
    public void testForEach() {
        char expectedChar = 'e';
        TCharHash charHash = new TCharHash(5);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');
        charHash.put('e', expectedChar);
        charHash.put('f', 'f');
        charHash.put('g', 'g');
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char character) {
                assertEquals(character, (char) (count % 6 + 'a'));
                count++;
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
    }
}