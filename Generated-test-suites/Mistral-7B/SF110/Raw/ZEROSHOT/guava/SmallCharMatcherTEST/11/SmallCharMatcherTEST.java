package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testFromBitSet() {
        BitSet chars = new BitSet();
        chars.set('a').set('b').set('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b' and 'c'");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testMatchesWithZero() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "matches any character including zero");

        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test
    public void testMatchesWithNonZero() {
        BitSet chars = new BitSet();
        chars.set('a').set('b').set('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b' and 'c'");

        assertFalse(charMatcher.matches('\0'));
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "matches any character including zero");
        BitSet table = new BitSet();

        charMatcher.setBits(table);

        assertTrue(table.get(0));
        for (char c = '\u0001'; c <= '\u007F'; c++) {
            assertTrue(table.get(c));
        }
    }

    @Test
    public void testMatchesWithLargeCharacter() {
        BitSet chars = new BitSet();
        chars.set('\uD800').set('\uD801');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches high surrogate and low surrogate");

        char highSurrogate = '\uD800';
        char lowSurrogate = '\uDC00';
        char compositeCharacter = (char) (highSurrogate << 10 | lowSurrogate & 0x3FF);

        assertTrue(charMatcher.matches(highSurrogate));
        assertTrue(charMatcher.matches(lowSurrogate));
        assertTrue(charMatcher.matches(compositeCharacter));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromEmptyBitSet() {
        SmallCharMatcher.from(new BitSet(), "empty charMatcher");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromNegativeBitSet() {
        BitSet chars = new BitSet();
        chars.set(-1);
        SmallCharMatcher.from(chars, "negative charMatcher");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromBitSetWithNegativeCharacter() {
        BitSet chars = new BitSet();
        chars.set(-1);
        SmallCharMatcher.from(chars, "negative character charMatcher");
    }
}