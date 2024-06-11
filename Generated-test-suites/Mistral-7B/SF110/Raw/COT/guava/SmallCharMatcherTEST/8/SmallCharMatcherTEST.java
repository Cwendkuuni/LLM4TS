package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 0);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "testCharMatcher");
        for (char c : chars) {
            assertTrue("Matches expected character: " + c, charMatcher.matches(c));
        }
    }

    @Test
    public void testMatchesZero() {
        char[] chars = new char[0];
        BitSet bitSet = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "testCharMatcher");
        assertTrue("Matches expected zero", charMatcher.matches('\0'));
    }

    @Test
    public void testMatchesNonMatching() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            bitSet.set(i, chars[i] != 'd');
        }
        CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "testCharMatcher");
        assertFalse("Does not match unexpected character: d", charMatcher.matches('d'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTableSize() {
        char[] chars = new char[]{'a'};
        BitSet bitSet = new BitSet(1);
        SmallCharMatcher charMatcher = new SmallCharMatcher(null, 0L, false, "testCharMatcher");
        charMatcher.setBits(bitSet);
    }

    @Test
    public void testSetBits() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet expectedBitSet = new BitSet(chars.length);
        BitSet bitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            expectedBitSet.set(i, chars[i] != 0);
            bitSet.clear();
        }
        CharMatcher charMatcher = SmallCharMatcher.from(expectedBitSet, "testCharMatcher");
        charMatcher.setBits(bitSet);
        for (int i = 0; i < chars.length; i++) {
            assertTrue("Sets expected bit: " + i, bitSet.get(i));
            if (chars[i] != 0) {
                assertTrue("Sets non-zero character: " + chars[i], charMatcher.matches(chars[i]));
            }
        }
    }

    @Test
    public void testSetBitsZero() {
        char[] chars = new char[0];
        BitSet expectedBitSet = new BitSet();
        BitSet bitSet = new BitSet(chars.length);
        CharMatcher charMatcher = SmallCharMatcher.from(expectedBitSet, "testCharMatcher");
        charMatcher.setBits(bitSet);
        assertTrue("Sets expected zero", bitSet.get(0));
        assertFalse("Does not match zero", charMatcher.matches('\0'));
    }

    @Test
    public void testSetBitsNonMatching() {
        char[] chars = new char[]{'a', 'b', 'c'};
        BitSet expectedBitSet = new BitSet(chars.length);
        for (int i = 0; i < chars.length; i++) {
            expectedBitSet.clear(i);
        }
        BitSet bitSet = new BitSet(chars.length);
        CharMatcher charMatcher = SmallCharMatcher.from(expectedBitSet, "testCharMatcher");
        charMatcher.setBits(bitSet);
        for (char c : chars) {
            assertFalse("Does not set matching character: " + c, bitSet.get(c - 'a'));
            assertFalse("Does not match character: " + c, charMatcher.matches(c));
        }
    }

    @Test
    public void testChooseTableSize() {
        int[] sizes = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};
        for (int size : sizes) {
            int tableSize = SmallCharMatcher.chooseTableSize(size);
            assertTrue("Choose table size is power of 2: " + size, (tableSize & (tableSize - 1)) == 0);
            assertTrue("Table size is greater or equal to size", tableSize >= size);
        }
    }
}