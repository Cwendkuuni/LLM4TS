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

        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches characters 'a', 'b' and 'c'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testContainsZero() {
        BitSet chars = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches no character");
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.containsZero());

        chars.set(0);
        charMatcher = SmallCharMatcher.from(chars, "matches null character");
        assertTrue(charMatcher.matches('\0'));
        assertTrue(charMatcher.containsZero());
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2, tableSize);

        setSize = 16;
        tableSize = SmallCharMatcher.chooseTableSize(setSize);
        int expectedTableSize = (int)Math.pow(2, 5);
        assertEquals(expectedTableSize, tableSize);

        setSize = 256;
        tableSize = SmallCharMatcher.chooseTableSize(setSize);
        expectedTableSize = (int)Math.pow(2, 9);
        assertEquals(expectedTableSize, tableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 7;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        int expectedSmearedHashCode = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        assertEquals(expectedSmearedHashCode, smearedHashCode);
    }

    @Test
    public void testMatches() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "matches no character");
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));

        BitSet chars = new BitSet();
        chars.set('a');
        charMatcher = SmallCharMatcher.from(chars, "matches character 'a'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));

        chars.set('b');
        charMatcher = SmallCharMatcher.from(chars, "matches characters 'a' and 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));

        chars.set(0);
        charMatcher = SmallCharMatcher.from(chars, "matches null character");
        assertTrue(charMatcher.matches('\0'));

        charMatcher = SmallCharMatcher.from(new BitSet(), "matches all characters");
        assertTrue(charMatcher.matches('\0'));
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('z'));
        assertTrue(charMatcher.matches('\u00FF'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "matches no character");
        charMatcher.setBits(table);
        assertFalse(table.get(0));
        assertFalse(table.get('a'));
        assertFalse(table.get('b'));

        table.clear();
        charMatcher = SmallCharMatcher.from(new BitSet(), "matches no character");
        charMatcher.setBits(table);
        assertTrue(table.get(0));
        assertFalse(table.get('a'));
        assertFalse(table.get('b'));

        table.clear();
        BitSet chars = new BitSet();
        chars.set('a');
        charMatcher = SmallCharMatcher.from(chars, "matches character 'a'");
        charMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertFalse(table.get('b'));

        table.clear();
        chars.set('a');
        chars.set('b');
        charMatcher = SmallCharMatcher.from(chars, "matches characters 'a' and 'b'");
        charMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }
}