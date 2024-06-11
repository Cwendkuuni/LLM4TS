package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher of 'a' and 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testMatchesCharacterInTable() {
        char[] table = new char[SmallCharMatcher.chooseTableSize(3)];
        table[0] = 'a';
        table[1] = 'b';
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0x1L, false, "CharMatcher of 'a' and 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testMatchesCharacterInFilter() {
        char[] table = new char[SmallCharMatcher.chooseTableSize(3)];
        table[0] = 'a';
        table[1] = 'b';
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0x1L | (1L << 'c'), false, "CharMatcher of 'a', 'b' and 'c'");
        assertFalse(charMatcher.matches('c')); // filter
        assertFalse(charMatcher.matches('d')); // not in table or filter
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
    }

    @Test
    public void testMatchesCharacterZero() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0x1L, true, "CharMatcher of zero");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromEmptyBitSet() {
        BitSet chars = new BitSet();
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher of empty set");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullDescription() {
        BitSet chars = new BitSet();
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckFilterNullCharacter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0x1L, false, "CharMatcher of null");
        charMatcher.checkFilter((char)0);
    }

}