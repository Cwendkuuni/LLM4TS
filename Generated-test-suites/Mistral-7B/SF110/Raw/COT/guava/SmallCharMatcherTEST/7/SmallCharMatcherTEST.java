package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = BitSet.create(); // empty set
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Empty CharMatcher");
        assertNull(charMatcher);

        // test with single character set
        chars.set(0, 'a');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "SingleCharMatcher");
        CharMatcher expected = CharMatcher.forRegex("[a]");
        assertEquals(expected.toString(), charMatcher.toString());

        // test with multiple character set
        chars.set(1, 'b');
        charMatcher = SmallCharMatcher.from(chars, "MultipleCharsMatcher");
        expected = CharMatcher.or(CharMatcher.forRegex("[a]"), CharMatcher.forRegex("[b]"));
        assertEquals(expected.toString(), charMatcher.toString());

        // test with character set containing zero
        chars.set(0, '0');
        charMatcher = SmallCharMatcher.from(chars, "CharMatcherWithZero");
        expected = CharMatcher.forRegex("[0]");
        assertTrue(charMatcher.matches('0'));
        assertTrue(charMatcher.matches('a')); // should not affect other characters
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromWithNegativeSizeSet() {
        BitSet chars = BitSet.create();
        chars.set(-1); // negative size
        SmallCharMatcher.from(chars, "NegativeSizeSetMatcher");
    }

    @Test
    public void testMatches() {
        char[] table = new char[2];
        table[0] = 'a';
        table[1] = '\0';

        long filter = 0b1111_0000_0001_1111_0000_0010_1111_1111L; // binary representation of filter
        boolean containsZero = true;

        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "SmallCharMatcher");

        // test with character that is present in the charMatcher
        assertTrue(charMatcher.matches('a'));

        // test with character that is not present in the charMatcher
        assertFalse(charMatcher.matches('b'));

        // test with character that is equal to zero and charMatcher contains zero
        if (charMatcher.containsZero) {
            assertTrue(charMatcher.matches((char) 0));
        } else {
            assertFalse(charMatcher.matches((char) 0));
        }

        // test with character that is equal to zero and charMatcher does not contain zero
        assertFalse(charMatcher.matches((char) 0));

        // test edge cases
        char testChar = Character.MIN_VALUE;
        while (testChar <= Character.MAX_VALUE) {
            if (charMatcher.matches(testChar)) {
                fail("Character " + testChar + " should not match CharMatcher: " + charMatcher.toString());
            }
            testChar++;
        }
    }

    @Test
    public void testSetBits() {
        BitSet table = BitSet.create();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "EmptyCharMatcher");
        charMatcher.setBits(table);
        assertTrue(table.isEmpty());

        char[] charTable = new char[2];
        charTable[0] = 'a';
        charMatcher = new SmallCharMatcher(charTable, 0, false, "CharTableMatcher");
        charMatcher.setBits(table);
        table.set(0, true);
        assertEquals(charTable[0], table.get(0).toChar());

        charTable[1] = 'b';
        charMatcher = new SmallCharMatcher(charTable, 1L, false, "CharTableWithFilterMatcher");
        charMatcher.setBits(table);
        table.set(1, true);
        assertEquals(charTable[1], table.get(1).toChar());
    }
}