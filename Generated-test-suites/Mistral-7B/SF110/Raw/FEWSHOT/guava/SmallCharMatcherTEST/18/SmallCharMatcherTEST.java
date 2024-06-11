package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.collect.BitSets;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesChar() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Chars matching pattern");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            boolean expected = (charMatcher.matches(c) && (c >= 0 && c <= 127));
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }
    }

    @Test
    public void testMatchesEmptyCharSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.emptySet(), "Empty char set");
        assertFalse("Expected empty char set to not match any character", charMatcher.matches((char) 0));
    }

    @Test
    public void testMatchesSingletonCharSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1), "Singleton char set");
        assertTrue("Expected singleton char set to match the character it contains", charMatcher.matches((char) 1));
        assertFalse("Expected singleton char set not to match other characters", charMatcher.matches((char) 2));
    }

    @Test
    public void testMatchesSubsetCharSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1, (byte) 3, (byte) 5), "Subset char set");
        assertTrue("Expected subset char set to match the characters it contains", charMatcher.matches((char) 1));
        assertTrue("Expected subset char set to match the characters it contains", charMatcher.matches((char) 3));
        assertTrue("Expected subset char set to match the characters it contains", charMatcher.matches((char) 5));
        assertFalse("Expected subset char set not to match other characters", charMatcher.matches((char) 2));
    }

    @Test
    public void testMatchesEmptyDescription() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "");
        assertNull("Expected empty description to return null", charMatcher.description());
    }

    @Test
    public void testMatchesNonEmptyDescription() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Description for char set");
        assertEquals("Expected non-empty description to be returned", "Description for char set", charMatcher.description());
    }

    @Test
    public void testChooseTableSize() {
        int size = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals("Expected table size to be 2 for single character set", 2, tableSize);

        size = 16;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        int expectedTableSize = (int) Math.pow(2, (Math.log(size) / Math.log(2)) + 1);
        assertEquals("Unexpected table size for given character set size", expectedTableSize, tableSize);
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Test CharMatcher setBits method");
        BitSet table = BitSets.newMutableSet();
        charMatcher.setBits(table);

        for (char c : charMatcher.table) {
            if (c != '\0') {
                assertTrue("Expected BitSet to contain matched character", table.get(c));
            }
        }

        if (charMatcher.containsZero) {
            assertTrue("Expected BitSet to contain zero", table.get(0));
        } else {
            assertFalse("Expected BitSet not to contain zero", table.get(0));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBitsUnmodifiableBitSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.fixedSize(2, true), "Test CharMatcher setBits method with unmodifiable BitSet");
        BitSet table = BitSets.fixedSize(2, false);
        charMatcher.setBits(table);
    }

    @Test(expected = NullPointerException.class)
    public void testSetBitsNullBitSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Test CharMatcher setBits method with null BitSet");
        charMatcher.setBits(null);
    }

    @Test
    public void testFromBitSetWithEmptyDescription() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.emptySet(), "");
        assertNull("Expected empty description to return null", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithNonEmptyDescription() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Description for char set");
        assertEquals("Expected non-empty description to be returned", "Description for char set", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithSingleCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1), "Single character set");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0);
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }
    }

    @Test
    public void testFromBitSetWithSubsetCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1, (byte) 3, (byte) 5), "Subset character set");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0 && Arrays.asList(1, 3, 5).contains((int) c));
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }
    }

    @Test
    public void testFromBitSetWithEmptyCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.emptySet(), "Empty character set");
        assertFalse("Expected empty character set not to match any character", charMatcher.matches((char) 1));
        assertFalse("Expected empty character set not to match any character", charMatcher.matches((char) 2));
        assertFalse("Expected empty character set not to match any character", charMatcher.matches((char) 3));
    }

    @Test
    public void testFromBitSetWithDescriptionAndEmptyCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.emptySet(), "Description for empty character set");
        assertNull("Expected empty character set to return null description", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndSingletonCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1), "Description for singleton character set");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0 && c == (char) 1);
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }

        assertEquals("Expected description to be returned", "Description for singleton character set", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndSubsetCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1, (byte) 3, (byte) 5), "Description for subset character set");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        List<Character> characters = Lists.newArrayList((char) 1, (char) 3, (char) 5);

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0 && characters.contains(c));
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }

        assertEquals("Expected description to be returned", "Description for subset character set", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndEmptyDescriptionCharSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.emptySet(), "Description for empty character set");
        assertNull("Expected empty character set to return null description", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndSingletonEmptyCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Description for empty character set");
        assertNull("Expected empty character set to return null description", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndSingletonCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1), "Description for singleton character set");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0 && c == (char) 1);
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }

        assertEquals("Expected description to be returned", "Description for singleton character set", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndSubsetCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1, (byte) 3, (byte) 5), "Description for subset character set");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        List<Character> characters = Lists.newArrayList((char) 1, (char) 3, (char) 5);

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0 && characters.contains(c));
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }

        assertEquals("Expected description to be returned", "Description for subset character set", charMatcher.description());
    }

    @Test
    public void testFromBitSetWithDescriptionAndSubsetCharacterSetAndEmptyDescription() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create((byte) 1, (byte) 3, (byte) 5), "");
        byte[] chars = new byte[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (byte) i;
        }

        List<Character> characters = Lists.newArrayList((char) 1, (char) 3, (char) 5);

        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            byte charIndex = Arrays.binarySearch(chars, (byte) c);
            boolean expected = (charIndex >= 0 && characters.contains(c));
            boolean actual = charMatcher.matches(c);
            assertEquals("Unexpected match result for character: " + c, expected, actual);
        }

        assertNull("Expected empty description to return null", charMatcher.description());
    }
}