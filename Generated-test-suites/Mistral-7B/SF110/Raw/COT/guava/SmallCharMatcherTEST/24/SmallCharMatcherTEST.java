package com.google.common.base.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_EmptyCharMatcher() {
        SmallCharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
        assertTrue(emptyMatcher.matches('\0'));
    }

    @Test
    public void testMatches_CharInTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(1, 3, 5), "CharMatcher with chars 1, 3, 5");
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
        assertTrue(charMatcher.matches('5'));
        assertFalse(charMatcher.matches('0'));
        assertFalse(charMatcher.matches('2'));
        assertFalse(charMatcher.matches('4'));
    }

    @Test
    public void testMatches_CharNotInTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(1, 3, 5), "CharMatcher with chars 1, 3, 5");
        assertFalse(charMatcher.matches('0'));
        assertFalse(charMatcher.matches('2'));
        assertFalse(charMatcher.matches('6'));
    }

    @Test(expected = IllegalStateException.class)
    public void testMatches_NullTable() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(null, 0, false, "Null Table CharMatcher");
        charMatcher.matches('0');
    }

    @Test
    public void testMatches_ContainsZero() {
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(1, 3, 5), "CharMatcher with chars 1, 3, 5");
        SmallCharMatcher containsZeroMatcher = SmallCharMatcher.from(new BitSet(), "ContainsZero CharMatcher");
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
        assertTrue(charMatcher.matches('5'));
        assertFalse(charMatcher.matches('0'));
        assertTrue(containsZeroMatcher.matches('0'));
        assertFalse(containsZeroMatcher.matches('1'));
        assertFalse(containsZeroMatcher.matches('3'));
        assertFalse(containsZeroMatcher.matches('5'));
    }

    @Test
    public void testMatches_Filter() {
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(1, 2, 4, 8), "CharMatcher with filter 0b1111");
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('2'));
        assertFalse(charMatcher.matches('3'));
        assertFalse(charMatcher.matches('4'));
        assertTrue(charMatcher.matches('8'));
        assertFalse(charMatcher.matches('5'));
    }

    @Test
    public void testMatches_CharsOutsideTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(1, 3, 5), "CharMatcher with chars 1, 3, 5");
        int maxSize = SmallCharMatcher.MAX_SIZE;
        for (int i = maxSize; i < Integer.MAX_VALUE; i++) {
            char c = (char)i;
            assertFalse(charMatcher.matches(c));
        }
    }

    @Test
    public void testChooseTableSize() {
        int tableSize = SmallCharMatcher.chooseTableSize(2);
        assertEquals(4, tableSize);

        tableSize = SmallCharMatcher.chooseTableSize(10);
        assertEquals(32, tableSize);

        tableSize = SmallCharMatcher.chooseTableSize(33);
        assertEquals(128, tableSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChooseTableSize_NegativeSize() {
        SmallCharMatcher.chooseTableSize(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChooseTableSize_ZeroSize() {
        SmallCharMatcher.chooseTableSize(0);
    }
}