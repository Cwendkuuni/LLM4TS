package com.google.common.base;

import com.google.common.annotations.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

@GwtIncompatible("no precomputation is done in GWT")
public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a').set('b').set('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b' and 'c'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testChooseTableSize() {
        int tableSize = SmallCharMatcher.chooseTableSize(1);
        assertEquals(2, tableSize);

        tableSize = SmallCharMatcher.chooseTableSize(3);
        assertEquals(4, tableSize);

        tableSize = SmallCharMatcher.chooseTableSize(11);
        assertEquals(16, tableSize);
    }

    @Test
    public void testMatches() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "matches 'a'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3, false, "matches 'a' or 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));

        charMatcher = new SmallCharMatcher(new char[]{'a'}, 1, true, "matches any character including null");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches((char) 0));

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 1 | (1L << 5), false, "matches 'a' or 'e'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('e'));
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0, false, "matches 'a' and 'b'");
        charMatcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('c'));
    }
}