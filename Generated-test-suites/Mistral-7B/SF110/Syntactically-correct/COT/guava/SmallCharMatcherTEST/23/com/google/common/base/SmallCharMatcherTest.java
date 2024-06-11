package com.example.junit.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

import java.util.Arrays;

public class SmallCharMatcherTest {

    @org.junit.Test
    public void testSmallCharMatcher_from() {
        // Given
        CharSet charSet = CharSets.range('a', 'z');
        CharMatcher expectedMatcher = CharMatcher.forCharRange('a', 'z');
        CharMatcher charMatcher = SmallCharMatcher.from(charSet.asBitSet(), "Uppercase alphabet");

        // When
        CharMatcher actualMatcher = charMatcher;

        // Then
        assertEquals(expectedMatcher, actualMatcher);
    }

    @org.junit.Test
    public void testSmallCharMatcher_matches_charInTable() {
        // Given
        char[] table = new char[]{'a', 'b'};
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "Matches 'a' or 'b'");

        // When
        boolean result = charMatcher.matches('a');

        // Then
        assertTrue(result);
    }

    @org.junit.Test
    public void testSmallCharMatcher_matches_charNotInTable() {
        // Given
        char[] table = new char[]{'a', 'b'};
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "Matches 'a' or 'b'");

        // When
        boolean result = charMatcher.matches('c');

        // Then
        assertFalse(result);
    }

    @org.junit.Test
    public void testSmallCharMatcher_matches_zero() {
        // Given
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "Matches zero");

        // When
        boolean result = charMatcher.matches((char) 0);

        // Then
        assertTrue(result);
    }

    @org.junit.Test
    public void testSmallCharMatcher_matches_empty() {
        // Given
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Empty matcher");

        // When
        boolean result = charMatcher.matches((char) 0);

        // Then
        assertFalse(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_from_emptyBitSet() {
        // Given
        BitSet bitSet = new BitSet();

        // When & Then
        SmallCharMatcher.from(bitSet, "Test");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_from_bitSetWithZeroAndOtherChars() {
        // Given
        BitSet bitSet = new BitSet().set(0).set('a');

        // When & Then
        SmallCharMatcher.from(bitSet, "Test");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_from_bitSetContainingZeroTwice() {
        // Given
        BitSet bitSet = new BitSet().set(0).set(0);

        // When & Then
        SmallCharMatcher.from(bitSet, "Test");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_from_bitSetWithSizeGreaterThanTableSize() {
        // Given
        BitSet bitSet = new BitSet();
        int setSize = SmallCharMatcher.MAX_SIZE + 1;
        for (int i = 0; i < setSize; i++) {
            bitSet.set(i);
        }

        // When & Then
        SmallCharMatcher.from(bitSet, "Test");
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSmallCharMatcher_from_nullDescription() {
        // Given
        BitSet bitSet = new BitSet();

        // When & Then
        SmallCharMatcher.from(bitSet, null);
    }
}