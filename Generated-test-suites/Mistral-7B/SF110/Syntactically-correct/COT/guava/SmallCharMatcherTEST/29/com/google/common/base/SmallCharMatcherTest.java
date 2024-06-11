package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        char[] table = new char[]{'a', 'e', 'i'};
        long filter = 0b11101111_01101110_11011111_11110111L;
        boolean containsZero = true;
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "Vowels");
        assertTrue("Matches 'a'", charMatcher.matches('a'));
        assertTrue("Matches 'e'", charMatcher.matches('e'));
        assertTrue("Matches 'i'", charMatcher.matches('i'));
        assertFalse("Does not match 'b'", charMatcher.matches('b'));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "EmptySet");
        assertFalse("Does not match 'a' for empty set", charMatcher.checkFilter('a'));

        char[] table = new char[]{'a', 'e'};
        long filter = 0b11101111_01101110L;
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, false, "SetA");
        assertTrue("Matches 'a' for set A", charMatcher.checkFilter('a'));
        assertFalse("Does not match 'b' for set A", charMatcher.checkFilter('b'));

        SmallCharMatcher charMatcherWithZero = new SmallCharMatcher(new char[0], 0, true, "SetWithZero");
        assertTrue("Matches zero for set with zero", charMatcherWithZero.checkFilter('\0'));
    }

    @Test
    public void testChooseTableSize() {
        int size = 1;
        int expectedSize = 2;
        int actualSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals("Expected size 2 for size 1", expectedSize, actualSize);

        size = 9;
        expectedSize = 16;
        actualSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals("Expected size 16 for size 9", expectedSize, actualSize);

        size = 23;
        expectedSize = 32;
        actualSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals("Expected size 32 for size 23", expectedSize, actualSize);

        size = 1024;
        expectedSize = 1024;
        actualSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals("Expected size 1024 for size 1024", expectedSize, actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChooseTableSizeNegativeSize() {
        SmallCharMatcher.chooseTableSize(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChooseTableSizeZeroSize() {
        SmallCharMatcher.chooseTableSize(0);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('e');
        chars.set('i');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Vowels");
        char[] table = charMatcher.table;
        long filter = charMatcher.filter;
        boolean containsZero = charMatcher.containsZero;
        assertEquals("Expected length 3 for the table", 3, table.length);
        assertTrue("Expected 'a' in the table", table[0] == 'a');
        assertTrue("Expected 'e' in the table", table[1] == 'e');
        assertTrue("Expected 'i' in the table", table[2] == 'i');
        assertEquals("Expected filter 0b11101111_01101110_11011111_11110111", filter, 0b11101111_01101110_11011111_11110111L);
        assertTrue("Expected contains zero for the charMatcher", containsZero);
    }

    @Test
    public void testFromEmptySet() {
        BitSet emptySet = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(emptySet, "EmptySet");
        assertNull("Expected null for the table", charMatcher.table);
        assertEquals("Expected filter 0 for the charMatcher", 0, charMatcher.filter);
        assertFalse("Expected false for the containsZero", charMatcher.containsZero);
    }
}