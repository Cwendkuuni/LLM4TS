package com.google.common.base.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.FastMatcher;
import com.google.common.base.SmallCharMatcher;
import org.junit.Test;

import java.util.Arrays;

public class SmallCharMatcherTest {

    @Test
    public void testSmallCharMatcher_fromBitSet() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set('a');
        chars.set('z');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches any char in the given set");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('z'));
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testSmallCharMatcher_matchesCharInTable() {
        char[] table = new char[32];
        table[0] = 'a';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "matches 'a'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testSmallCharMatcher_matchesCharInFilter() {
        char[] table = new char[32];
        long filter = 1L << 'a' | 1L << 'z';
        CharMatcher charMatcher = new SmallCharMatcher(table, filter, false, "matches 'a' or 'z'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('z'));
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testSmallCharMatcher_matchesZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, true, "matches zero");
        assertTrue(charMatcher.matches((char) 0));
    }

    @Test
    public void testSmallCharMatcher_doesNotMatchZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "does not match zero");
        assertFalse(charMatcher.matches((char) 0));
    }

    @Test(expected = NullPointerException.class)
    public void testSmallCharMatcher_nullTable() {
        CharMatcher charMatcher = new SmallCharMatcher(null, 0L, false, "should throw NullPointerException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_negativeFilter() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[10], -1L, false, "should throw IllegalArgumentException");
    }

    @Test
    public void testSmallCharMatcher_containsZeroAndZeroIsInTable() {
        char[] table = new char[2];
        table[0] = '\0';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, true, "matches zero");
        assertTrue(charMatcher.matches((char) 0));
    }

    @Test
    public void testSmallCharMatcher_containsZeroButZeroIsNotInTable() {
        char[] table = new char[2];
        table[0] = 'a';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, true, "matches zero");
        assertFalse(charMatcher.matches((char) 0));
    }

    @Test
    public void testSmallCharMatcher_doesNotContainZeroButZeroIsInTable() {
        char[] table = new char[2];
        table[0] = '\0';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "does not match zero");
        assertFalse(charMatcher.matches((char) 0));
    }

    @Test
    public void testSmallCharMatcher_doesNotContainZeroButZeroIsNotInTable() {
        char[] table = new char[2];
        table[0] = 'a';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "does not match zero");
        assertFalse(charMatcher.matches((char) 0));
    }

    @Test
    public void testSmallCharMatcher_matchesMultipleChars() {
        char[] table = new char[3];
        table[0] = 'a';
        table[1] = 'b';
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "matches 'a' or 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testSmallCharMatcher_matchesLargeCharSet() {
        char[] table = new char[1024];
        BitSet chars = new BitSet(1024);
        for (int i = 0; i < table.length; i++) {
            chars.set(i, true);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches all chars in the given set");
        for (int i = 0; i < table.length; i++) {
            assertTrue(charMatcher.matches(table[i]));
        }
    }
}