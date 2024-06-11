package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Matches characters a, b, and c");

        assertEquals("Matches characters a, b, and c", charMatcher.description());
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        BitSet table = new BitSet();
        charMatcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertFalse(table.get('d'));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 16;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(32, tableSize);

        setSize = 2;
        tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(4, tableSize);

        setSize = 1;
        tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2, tableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        int expectedSmearedHashCode = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        assertEquals(expectedSmearedHashCode, smearedHashCode);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Empty CharMatcher");
        assertFalse(charMatcher.checkFilter('a'));

        charMatcher = new SmallCharMatcher(new char[]{'a'}, 1L, false, "Matches character a");
        assertTrue(charMatcher.checkFilter('a'));
        assertFalse(charMatcher.checkFilter('b'));

        charMatcher = new SmallCharMatcher(new char[]{'a'}, 1L | (1L << 1), false, "Matches character a or character 1");
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('1'));
        assertFalse(charMatcher.checkFilter('b'));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Empty CharMatcher");
        assertTrue(charMatcher.matches('\0'));
        assertEquals("Empty CharMatcher", charMatcher.description());

        charMatcher = new SmallCharMatcher(new char[]{'a'}, 1L, false, "Matches character a");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertEquals("Matches character a", charMatcher.description());

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3L, false, "Matches characters a and b");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertEquals("Matches characters a and b", charMatcher.description());

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3L | (1L << 1), false, "Matches character a or character 1, and character b");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertEquals("Matches character a or character 1, and character b", charMatcher.description());

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3L | (1L << 1) | (1L << 3), false, "Matches character a or character 1, or character 3, and character b");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertEquals("Matches character a or character 1, or character 3, and character b", charMatcher.description());

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3L | (1L << 1) | (1L << 3) | (1L << 5), false, "Matches character a or character 1, or character 3, or character 5, and character b");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
        assertTrue(charMatcher.matches('5'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertEquals("Matches character a or character 1, or character 3, or character 5, and character b", charMatcher.description());
    }
}