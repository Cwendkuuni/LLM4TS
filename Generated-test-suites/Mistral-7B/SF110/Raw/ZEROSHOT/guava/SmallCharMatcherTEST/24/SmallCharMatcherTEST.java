package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.FastMatcher;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.BitSets;
import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class SmallCharMatcherTest {

    @org.junit.Test
    public void testSmallCharMatcher_empty() {
        CharMatcher charMatcher = CharMatcher.matcher('\0');
        BitSet expected = BitSet.create();
        CharMatcher expectedMatcher = CharMatcher.from(expected, "empty");
        assertEquals(expectedMatcher, charMatcher);
    }

    @org.junit.Test
    public void testSmallCharMatcher_singleCharacter() {
        CharMatcher charMatcher = CharMatcher.from(BitSets.create(1), "singleCharacter");
        assertEquals('a', charMatcher.matches('a').charAt(0));
        assertEquals(false, charMatcher.matches('b'));
    }

    @org.junit.Test
    public void testSmallCharMatcher_multipleCharacters() {
        CharMatcher charMatcher = CharMatcher.from(BitSets.create(1, 3), "multipleCharacters");
        assertEquals('a', charMatcher.matches('a').charAt(0));
        assertEquals('c', charMatcher.matches('c').charAt(0));
        assertEquals(false, charMatcher.matches('b'));
    }

    @org.junit.Test
    public void testSmallCharMatcher_smear() {
        int hashCode = 'a'.hashCode();
        int expected = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        int actual = SmallCharMatcher.smear(hashCode);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testSmallCharMatcher_checkFilter() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        long filter = 0b1111_0111_1111_1101L; // 0x0f_0xf0
        boolean containsZero = false;
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "checkFilter");
        assertEquals(true, charMatcher.checkFilter('f'));
        assertEquals(true, charMatcher.checkFilter('e'));
        assertEquals(false, charMatcher.checkFilter('g'));
        assertEquals(false, charMatcher.checkFilter('0'));
    }

    @org.junit.Test
    public void testSmallCharMatcher_chooseTableSize() {
        int setSize = 15;
        int expected = 32;
        int actual = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testSmallCharMatcher_from() {
        BitSet chars = BitSets.create(1, 3, 5, 7, 11);
        CharMatcher expectedMatcher = CharMatcher.from(chars, "expectedMatcher");
        SmallCharMatcher actualMatcher = SmallCharMatcher.from(chars, "actualMatcher");
        assertEquals(expectedMatcher, actualMatcher);
    }

    @org.junit.Test
        public void testSmallCharMatcher_matches() {
            CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(1, 3), "matches");
            byte[] input = "abcdefg".getBytes(Charsets.UTF_8);
            byte[] expectedOutput = "acg".getBytes(Charsets.UTF_8);
            byte[] actualOutput = Lists.newByteArrayList().addAll(Arrays.asList(charMatcher.apply(input))).toArray(new byte[0]);
            assertArrayEquals(expectedOutput, actualOutput);
        }

    @org.junit.Test
    public void testSmallCharMatcher_setBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(1, 3), "setBits");
        BitSet expectedTable = BitSets.create(1, 3);
        CharMatcher expectedCharMatcher = CharMatcher.from(expectedTable, "expectedCharMatcher");
        CharMatcher actualCharMatcher = new SmallCharMatcher(new char[0], 0, false, "actualCharMatcher");
        actualCharMatcher.setBits(expectedTable);
        assertEquals(expectedCharMatcher, actualCharMatcher);
    }
}