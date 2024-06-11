package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {
    @Test
    public void testMatchesCharInTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('a'), Character.valueOf('e'), Character.valueOf('i')), "Vowels");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('e'));
        assertTrue(charMatcher.matches('i'));
    }

    @Test
    public void testMatchesCharNotInTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('a'), Character.valueOf('e'), Character.valueOf('i')), "Vowels");
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('x'));
        assertFalse(charMatcher.matches('\u00DC')); // UTF-8 'Ü'
    }

    @Test
    public void testMatchesZero() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Empty");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test
    public void testMatchesZeroWithContainsZeroFlag() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('\0')), "Zero");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test
    public void testMatchesZeroWithoutContainsZeroFlag() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Empty");
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test
    public void testMatchesFilteredChar() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('a'), Character.valueOf('b')), "AB");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('a'), Character.valueOf('b')), "AB");
        assertFalse(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('a'), Character.valueOf('b')), "AB");
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.valueOf('a'), Character.valueOf('b')), "AB^c");
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testTableSizeCalculation() {
        int setSize = 1000;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(Math.pow(2, 10), (double) tableSize);
    }

    @Test
    public void testTableSizeCalculationWithOneElement() {
        int setSize = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2, tableSize);
    }

    @Test
    public void testTableSizeCalculationWithSmallSet() {
        int setSize = 16;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(Math.pow(2, 4), (double) tableSize);
    }

    @Test
    public void testTableSizeCalculationWithHalfLoadFactor() {
        int setSize = 10000;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        double actualLoadFactor = (double) setSize / tableSize;
        assertEquals(SmallCharMatcher.DESIRED_LOAD_FACTOR, actualLoadFactor, 0.01);
    }
}