package com.example.junit.test;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = Sets.newBitSet();
        chars.set(0);
        chars.set('a');
        chars.set('b');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher from BitSet test");
        assertEquals("CharMatcher from BitSet test", charMatcher.description(), "CharMatcher description should match the input");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(Sets.newHashSet('a', 'b', 'c'), "CharMatcher from HashSet test");
        assertEquals("CharMatcher from HashSet test", charMatcher.description(), "CharMatcher description should match the input");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(Arrays.asList('a', 'b', 'c').stream().mapToInt(i -> i).toArray(), "CharMatcher from IntArray test");
        assertEquals("CharMatcher from IntArray test", charMatcher.description(), "CharMatcher description should match the input");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
    }

    @Test
    public void testMatches() {
        char[] table = new char[SmallCharMatcher.chooseTableSize(10)];
        BitSet chars = Sets.newBitSet();
        chars.set(0);
        chars.set('a');
        chars.set('b');
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Empty CharMatcher test");
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(chars, "SingleCharMatcher test");
        charMatcher.matches('a');
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));

        chars = Sets.newBitSet();
        chars.set(0);
        chars.set('a');
        chars.set('b');
        charMatcher = SmallCharMatcher.from(chars, "SingleCharMatcherWithZero test");
        charMatcher.matches('a');
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('0'));

        charMatcher = SmallCharMatcher.from(Sets.newBitSet('a', 'b', 'c'), "MultiCharMatcher test");
        charMatcher.matches('a');
        assertTrue(charMatcher.matches('a'));
        charMatcher.matches('b');
        assertTrue(charMatcher.matches('b'));
        charMatcher.matches('c');
        assertTrue(charMatcher.matches('c'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromWithEmptyBitSet() {
        SmallCharMatcher.from(Sets.emptyBitSet(), "EmptyBitSet CharMatcher test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromWithNullDescription() {
        SmallCharMatcher.from(Sets.newBitSet(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromWithNullCharacterSet() {
        SmallCharMatcher.from(null, "NullCharacterSet CharMatcher test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromWithNegativeBit() {
        BitSet chars = Sets.newBitSet();
        chars.set(-1);
        try {
            SmallCharMatcher.from(chars, "NegativeBit CharMatcher test");
        } catch (IllegalArgumentException e) {
            assertEquals("Negative bit set", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromWithNullCharacter() {
        BitSet chars = Sets.newBitSet();
        try {
            SmallCharMatcher.from(chars, "NullCharacter CharMatcher test");
            SmallCharMatcher charMatcher = SmallCharMatcher.from(new char[] { 0 }, "EmptyCharArray CharMatcher test");
            charMatcher.matches((char)0);
        } catch (IllegalArgumentException e) {
            assertEquals("Null character", e.getMessage());
            throw e;
        }
    }
}