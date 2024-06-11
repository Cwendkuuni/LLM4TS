package com.google.common.base.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.FastMatcher;
import com.google.common.base.SmallCharMatcher;
import java.util.Arrays;
import org.junit.Test;

public class SmallCharMatcherTest {

    static final char TEST_CHAR = 'A';

    @Test
    public void testMatches_CharIsInTable_ReturnsTrue() {
        SmallCharMatcher smallCharMatcher = SmallCharMatcher.from(new BitSet(), "Test CharMatcher");
        smallCharMatcher.setBits(new BitSet());
        smallCharMatcher.table[smear(TEST_CHAR) & (SmallCharMatcher.MAX_SIZE - 1)] = TEST_CHAR;
        assertTrue(smallCharMatcher.matches(TEST_CHAR));
    }

    // The remaining test cases for the matches method

    // Test cases for the setBits method

    // Test cases for the chooseTableSize method

    // Test case for the from method

    @Test
    public void testFrom_EmptyBitSet_ReturnsEmptyCharMatcher() {
        CharMatcher emptyCharMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
        assertNotNull(emptyCharMatcher);
        assertTrue(emptyCharMatcher.matches('\0'));
        assertTrue(emptyCharMatcher.matches('\u0000'));
        assertFalse(emptyCharMatcher.matches('A'));
        assertTrue(emptyCharMatcher.description().equals("Empty CharMatcher"));
    }

    @Test
    public void testFrom_BitSetWithSingleChar_ReturnsCharMatcherWithSingleChar() {
        CharMatcher singleCharMatcher = SmallCharMatcher.from(new BitSet().set(1), "Single CharMatcher");
        assertNotNull(singleCharMatcher);
        assertTrue(singleCharMatcher.matches('A'));
        assertFalse(singleCharMatcher.matches('B'));
        assertTrue(singleCharMatcher.description().equals("Single CharMatcher"));
    }

    @Test
    public void testFrom_BitSetWithMultipleChars_ReturnsCharMatcherWithMultipleChars() {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(2);
        CharMatcher multiCharMatcher = SmallCharMatcher.from(bitSet, "Multi CharMatcher");
        assertNotNull(multiCharMatcher);
        assertTrue(multiCharMatcher.matches('A'));
        assertFalse(multiCharMatcher.matches('C'));
        assertTrue(multiCharMatcher.description().equals("Multi CharMatcher"));
        CharMatcher expectedMultiCharMatcher = CharMatcher.or(CharMatcher.is('A'), CharMatcher.is('B'));
        assertEquals(expectedMultiCharMatcher, multiCharMatcher);
    }

    @Test
    public void testFrom_BitSetWithSameCharMultipleTimes_ReturnsCharMatcherWithSingleChar() {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(1);
        CharMatcher singleCharMatcher = SmallCharMatcher.from(bitSet, "Single CharMatcher");
        assertNotNull(singleCharMatcher);
        assertTrue(singleCharMatcher.matches('A'));
        assertFalse(singleCharMatcher.matches('B'));
        assertTrue(singleCharMatcher.description().equals("Single CharMatcher"));
    }

    @Test
    public void testFrom_BitSetWithAdjacentChars_ReturnsCharMatcherWithAdjacentChars() {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(2);
        CharMatcher adjacentCharsMatcher = SmallCharMatcher.from(bitSet, "Adjacent Chars Matcher");
        assertNotNull(adjacentCharsMatcher);
        assertTrue(adjacentCharsMatcher.matches('A'));
        assertTrue(adjacentCharsMatcher.matches('B'));
        assertFalse(adjacentCharsMatcher.matches('C'));
        assertTrue(adjacentCharsMatcher.description().equals("Adjacent Chars Matcher"));
    }

    @Test
    public void testFrom_BitSetWithOverlappingChars_ReturnsCharMatcherWithOverlappingChars() {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(3);
        CharMatcher overlappingCharsMatcher = SmallCharMatcher.from(bitSet, "Overlapping Chars Matcher");
        assertNotNull(overlappingCharsMatcher);
        assertTrue(overlappingCharsMatcher.matches('A'));
        assertTrue(overlappingCharsMatcher.matches('C'));
        assertFalse(overlappingCharsMatcher.matches('B'));
        assertTrue(overlappingCharsMatcher.description().equals("Overlapping Chars Matcher"));
    }

    @Test
    public void testFrom_BitSetWithCharOutOfTableSize_ThrowsIllegalArgumentException() {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < SmallCharMatcher.MAX_SIZE + 1; i++) {
            bitSet.set(i);
        }
        try {
            SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "CharMatcher with too many chars");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFrom_BitSetWithNegativeChars_ThrowsIllegalArgumentException() {
        BitSet bitSet = new BitSet();
        for (int i = -1; i < 0; i++) {
            bitSet.set(i);
        }
        try {
            SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "CharMatcher with negative chars");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFrom_BitSetWithSameFilter_ReturnsSameInstance() {
        BitSet bitSet1 = new BitSet();
        bitSet1.set(1);
        CharMatcher charMatcher1 = SmallCharMatcher.from(bitSet1, "CharMatcher1");
        BitSet bitSet2 = new BitSet();
        bitSet2.set(1);
        CharMatcher charMatcher2 = SmallCharMatcher.from(bitSet2, "CharMatcher2");
        assertNotSame(charMatcher1, charMatcher2);
        CharMatcher charMatcherSame = SmallCharMatcher.from(bitSet1, "CharMatcherSame");
        assertEquals(charMatcher1, charMatcherSame);
    }

    @Test
    public void testFrom_BitSetWithEmptyTable_ReturnsEmptyCharMatcher() {
        BitSet bitSet = new BitSet();
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, "CharMatcher with empty table");
        assertNotNull(charMatcher);
        assertTrue(charMatcher.matches('\u0000'));
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('A'));
        assertTrue(charMatcher.description().equals("CharMatcher with empty table"));
    }

    // Test case for Smear method

    // Test case for checkFilter method

    // Test case for static initializers

    
}