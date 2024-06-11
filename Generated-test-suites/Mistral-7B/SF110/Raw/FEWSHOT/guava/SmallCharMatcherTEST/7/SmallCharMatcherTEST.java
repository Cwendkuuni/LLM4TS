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

        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b' and 'c'");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testContainsZero() {
        BitSet chars = new BitSet();

        CharMatcher charMatcher1 = SmallCharMatcher.from(chars, "matches no character");
        assertFalse(charMatcher1.matches('\0'));

        BitSet containsZeroChars = new BitSet();
        containsZeroChars.set('\0');
        CharMatcher charMatcher2 = SmallCharMatcher.from(containsZeroChars, "matches null character");
        assertTrue(charMatcher2.matches('\0'));
    }

    @Test
    public void testMatchesMultipleCharacters() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');

        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b' and 'c'");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
    }

    @Test
    public void testMatchesSingleCharacter() {
        BitSet chars = new BitSet();
        chars.set('a');

        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a'");

        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testMatchesNoCharacter() {
        BitSet chars = new BitSet();

        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches no character");

        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }
}