package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesCharInTable() {
        char[] table = new char[16];
        table[0] = 'a';
        table[4] = 'e';
        BitSet bitSet = new BitSet(16);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != '\0') {
                bitSet.set(table[i]);
            }
        }
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher");
        for (char c : table) {
            assertTrue(charMatcher.matches(c));
        }
    }

    @Test
    public void testMatchesCharNotInTable() {
        char[] table = new char[16];
        table[0] = 'a';
        table[4] = 'e';
        BitSet bitSet = new BitSet(16);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != '\0') {
                bitSet.set(table[i]);
            }
        }
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher");
        char unknownChar = 'i';
        assertFalse(charMatcher.matches(unknownChar));
    }

    @Test
    public void testMatchesZero() {
        char[] table = new char[16];
        BitSet bitSet = new BitSet(16);
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "Test CharMatcher with zero");
        assertTrue(charMatcher.matches('\0'));
    }

    @Test
    public void testMatchesZeroWithNonZeroTable() {
        char[] table = new char[16];
        table[0] = 'a';
        table[4] = 'e';
        BitSet bitSet = new BitSet(16);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != '\0') {
                bitSet.set(table[i]);
            }
        }
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher");
        SmallCharMatcher charMatcherWithZero = new SmallCharMatcher(new char[0], 0, true, "Test CharMatcher with zero and non-zero table");
        assertTrue(charMatcherWithZero.matches('\0'));
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('e'));
        assertFalse(charMatcher.matches('i'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTableSize() {
        BitSet bitSet = new BitSet(0);
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher");
    }
}