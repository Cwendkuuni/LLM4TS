package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmallCharMatcher_empty() {
        CharMatcher charMatcher = CharMatcher.inRange('a', 'z');
        BitSet expected = new BitSet();
        charMatcher.setBits(expected);
        BitSet actual = new BitSet();
        charMatcher.matches('a');
        charMatcher.matches('b');
        // ... match all characters from 'a' to 'z'
        charMatcher.matches('z');
        charMatcher.matches(' '); // should not match space

        assertEquals(expected, actual);
    }

    @Test
    public void testSmallCharMatcher_singleCharacter() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a'), "matches 'a'");
        BitSet expected = new BitSet().set('a');
        charMatcher.setBits(expected);
        BitSet actual = new BitSet();
        charMatcher.matches('a');
        charMatcher.matches('b');
        // ... other characters should not match

        assertEquals(expected, actual);
    }

    @Test
    public void testSmallCharMatcher_multipleCharacters() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', 'b', 'c'), "matches 'a', 'b' and 'c'");
        BitSet expected = new BitSet().set('a', 'b', 'c');
        charMatcher.setBits(expected);
        BitSet actual = new BitSet();
        charMatcher.matches('a');
        charMatcher.matches('b');
        charMatcher.matches('c');

        assertEquals(expected, actual);
    }

    @Test
    public void testSmallCharMatcher_containsZero() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a'), "matches 'a' or the empty set");
        BitSet expected = new BitSet();
        expected.set(0);
        charMatcher.setBits(expected);
        BitSet actual = new BitSet();
        charMatcher.matches('\0');

        assertEquals(expected, actual);
    }

    @Test
    public void testSmallCharMatcher_largeSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'), "matches all lowercase letters");
        BitSet expected = new BitSet();
        for (char c = 'a'; c <= 'z'; c++) {
            expected.set(c);
        }
        charMatcher.setBits(expected);
        BitSet actual = new BitSet();
        for (char c = 'a'; c <= 'z'; c++) {
            actual.set(c);
            charMatcher.matches(c);
        }

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_nullDescription() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_emptyTable() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_emptyFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0L, false, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_negativeFilterBit() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, -1L, false, "description");
    }

    @Test(expected = IllegalStateException.class)
    public void testSmallCharMatcher_duplicateFilterBit() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "description");

        BitSet table = charMatcher.table;
        int index = table.length >> 1;
        table[index] = 'a';
        table[index] = 'b';
    }

    @Test(expected = IllegalStateException.class)
    public void testSmallCharMatcher_indexOutOfBounds() {
        BitSet chars = new BitSet();
        chars.set('a');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "description");

        int tableSize = charMatcher.table.length;
        int index = tableSize >> 1;
        charMatcher.matches((char)(index + 1));
    }
}