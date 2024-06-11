package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SmallCharMatcherTest {

    @org.junit.Test
    public void testSmallCharMatcher_from() {
        BitSet chars = BitSet.create();
        chars.set('a');
        chars.set('b');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "test CharMatcher");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(Collections.emptySet(), "empty CharMatcher");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
    }

    @org.junit.Test
    public void testSmallCharMatcher_matches() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'b'), "test CharMatcher");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(Collections.singleton('a'), "singleton CharMatcher");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(Collections.emptySet(), "empty CharMatcher");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_from_nullDescription() {
        BitSet chars = BitSet.create('a');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_matches_nullCharacter() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a'), "test CharMatcher");
        char[] chars = new char[1];
        chars[0] = null;
        assertFalse(charMatcher.matches(chars[0]));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_from_emptySet() {
        CharMatcher charMatcher = SmallCharMatcher.from(Collections.emptySet(), "test CharMatcher");
    }

    @org.junit.Test
    public void testSmallCharMatcher_setBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'b'), "test CharMatcher");
        BitSet bitSet = BitSet.create();
        charMatcher.setBits(bitSet);

        List<Character> expected = Lists.newArrayList('a', 'b');
        List<Character> actual = Lists.newArrayList(Iterables.getAll(bitSet));
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSmallCharMatcher_setBits_unmodifiableBitSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'b'), "test CharMatcher");
        BitSet unmodifiableBitSet = Collections.unmodifiableBitSet(BitSet.create('a', 'b'));
        charMatcher.setBits(unmodifiableBitSet);
    }
}