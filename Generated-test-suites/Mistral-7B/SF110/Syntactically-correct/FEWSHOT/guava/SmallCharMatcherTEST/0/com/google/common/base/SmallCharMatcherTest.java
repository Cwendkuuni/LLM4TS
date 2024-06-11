package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmallCharMatcher_matches_charInTable() {
        BitSet chars = new BitSet();
        chars.set('a');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches character 'a'");
        assertTrue(charMatcher.matches('a'));
    }

    @Test
    public void testSmallCharMatcher_matches_charNotInTable() {
        BitSet chars = new BitSet();
        chars.set('a');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches character 'a'");
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testSmallCharMatcher_containsZero() {
        BitSet chars = new BitSet();
        char[] table = new char[2];
        table[0] = '\0';
        table[1] = 'a';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0, true, "contains zero");;
        assertTrue(charMatcher.matches('\0'));
    }

    @Test
    public void testSmallCharMatcher_noCharInTable() {
        BitSet chars = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "no character in table");;
        assertFalse(charMatcher.matches('\0'));
    }

    @Test
    public void testSmallCharMatcher_largeTableSize() {
        BitSet chars = new BitSet();
        for (int i = 0; i < SmallCharMatcher.MAX_SIZE; i++) {
            chars.set((char) i);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "large table size");;
        assertTrue(charMatcher.matches('0'));
        assertTrue(charMatcher.matches('999'));
        assertFalse(charMatcher.matches('1024'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_emptyTable() {
        BitSet chars = new BitSet();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "empty table");;
    }

    @Test
    public void testSmallCharMatcher_matchesAll() {
        BitSet chars = new BitSet();
        for (char c = '\u0000'; c <= '\u00FF'; c++) {
            chars.set(c);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches all ASCII characters");;
        for (char c = '\u0000'; c <= '\u00FF'; c++) {
            assertTrue(charMatcher.matches(c));
        }
    }

    @Test
    public void testSmallCharMatcher_matchesNone() {
        BitSet chars = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches no characters");;
        for (char c = '\u0000'; c <= '\u00FF'; c++) {
            assertFalse(charMatcher.matches(c));
        }
    }

    @Test
    public void testSmallCharMatcher_filter() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "filter 'ab'");
        long filter = charMatcher.filter();
        assertEquals(1L << 'a'.codePointAt(0) | 1L << 'b'.codePointAt(0), filter);
    }

    @Test
    public void testSmallCharMatcher_filter_emptySet() {
        BitSet chars = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "empty filter");;
        long filter = charMatcher.filter();
        assertEquals(0, filter);
    }
}