package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesChar() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher");
        for (char c : chars) {
            assertTrue(charMatcher.matches(c));
        }
    }

    @Test
    public void testMatchesZeroFlag() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, true, "Test CharMatcher with zero flag");
        assertTrue(charMatcher.matches((char) 0));
    }

    @Test
    public void testMatchesNonMatchingChar() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher");
        char nonMatchingChar = 'd';
        assertFalse(charMatcher.matches(nonMatchingChar));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTableSize() {
        char[] chars = new char[0];
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = null;
        try {
            charMatcher = SmallCharMatcher.from(bitSet, "Test CharMatcher with invalid table size");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Set contains no elements", e.getMessage());
        }
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "Test CharMatcher");
        BitSet table = new BitSet(256);
        charMatcher.setBits(table);
        for (char c = '\u0000'; c <= '\u007F'; c++) {
            assertTrue(table.get(c));
        }
        for (char c = '\u0080'; c <= '\uffff'; c++) {
            assertFalse(table.get(c));
        }
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 256;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(tableSize, (int) Math.pow(2, Math.ceil(Math.log(setSize) / Math.log(2))));
    }
}