package com.google.common.base.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesCharacter() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        for (char c : chars) {
            assertTrue(charMatcher.matches(c));
        }
    }

    @Test
    public void testMatchesZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "SmallCharMatcherTest");
        assertTrue(charMatcher.matches((char) 0));
    }

    @Test
    public void testDoesNotMatchZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "SmallCharMatcherTest");
        assertFalse(charMatcher.matches((char) 0));
    }

    @Test
    public void testMatchesNonZeroCharacter() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        for (char c : chars) {
            if (c != 0) {
                assertTrue(charMatcher.matches(c));
            }
        }
    }

    @Test
    public void testDoesNotMatchNonZeroCharacter() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        for (char c : chars) {
            if (c != 0) {
                assertFalse(charMatcher.matches(c));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsIllegalArgumentExceptionWithEmptyCharacterArray() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "SmallCharMatcherTest");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsIllegalArgumentExceptionWithNegativeSizeBitSet() {
        BitSet bitSet = new BitSet(1);
        bitSet.set(0, false);
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        charMatcher.matches((char) 0);
        bitSet.clear(0);
        bitSet.set(-1, true);
        try {
            CharMatcher charMatcher2 = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest2");
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "SmallCharMatcherTest");
        BitSet bitSet = new BitSet(10);
        charMatcher.setBits(bitSet);
        for (int i = 0; i < 10; i++) {
            assertTrue(bitSet.get(i));
        }
    }

    @Test
    public void testSetBitsWithZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "SmallCharMatcherTest");
        BitSet bitSet = new BitSet(2);
        charMatcher.setBits(bitSet);
        assertTrue(bitSet.get(0));
        assertFalse(bitSet.get(1));
    }

    @Test
    public void testSetBitsWithNonZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0, false, "SmallCharMatcherTest");
        BitSet bitSet = new BitSet(3);
        charMatcher.setBits(bitSet);
        assertTrue(bitSet.get(0));
        assertTrue(bitSet.get(1));
        assertFalse(bitSet.get(2));
    }

    @Test
    public void testMatchesCharacterWithEmptyTable() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "SmallCharMatcherTest");
        assertFalse(charMatcher.matches((char) 0));
    }

    @Test
    public void testMatchesCharacterWithFullTable() {
        char[] chars = new char[SmallCharMatcher.MAX_SIZE];
        BitSet bitSet = new BitSet(SmallCharMatcher.MAX_SIZE);
        for (int i = 0; i < SmallCharMatcher.MAX_SIZE; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        for (int i = 0; i < SmallCharMatcher.MAX_SIZE; i++) {
            char c = (char) i;
            assertTrue(charMatcher.matches(c));
        }
    }

    @Test
    public void testMatchesCharacterWithFilter() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        char[] filterChars = new char[]{'b'};
        CharMatcher filterCharMatcher = SmallCharMatcher.from(BitSet.create(filterChars), "FilterCharMatcherTest");
        for (char c : chars) {
            if (Arrays.binarySearch(filterChars, c) > -1) {
                assertTrue(charMatcher.matches(c) && filterCharMatcher.matches(c));
            } else {
                assertTrue(charMatcher.matches(c) || !filterCharMatcher.matches(c));
            }
        }
    }

    @Test
    public void testMatchesCharacterWithEmptyFilter() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
        CharMatcher emptyFilterCharMatcher = SmallCharMatcher.from(new BitSet(), "EmptyFilterCharMatcherTest");
        for (char c : chars) {
            assertTrue(charMatcher.matches(c) || emptyFilterCharMatcher.matches(c));
        }
    }
}