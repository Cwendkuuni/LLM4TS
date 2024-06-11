package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.BitSets;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmallCharMatcherTest {

    private SmallCharMatcher charMatcher;
    private BitSet expectedTable;

    @Before
    public void setUp() {
        char[] chars = {'a', 'b', 'c'};
        expectedTable = BitSets.newBitSet(Arrays.asList(chars));
        charMatcher = SmallCharMatcher.from(expectedTable, "Test CharMatcher");
    }

    @Test
    public void testMatches() {
        for (char c : new char[]{'a', 'b', 'c', '\u0000'}) {
            boolean expectedResult = expectedTable.get(Character.valueOf(c).intValue());
            boolean actualResult = charMatcher.matches(c);
            assertTrue("Expected matches for character: " + c + " but does not.", expectedResult == actualResult);
        }

        assertFalse("Expected does not match for character: null.", charMatcher.matches((char) 0));
    }

    @Test
    @VisibleForTesting
    public void testSetBits() {
        BitSet table = BitSets.newBitSet();
        charMatcher.setBits(table);
        assertTrue("Expected table to have the same bits as the original.", expectedTable.equals(table));
    }

    @Test
    @VisibleForTesting
    public void testChooseTableSize() {
        int size = 15;
        int expectedTableSize = 16;
        int actualTableSize = SmallCharMatcher.chooseTableSize(size);
        assertTrue("Expected table size to be: " + expectedTableSize + " but was: " + actualTableSize, expectedTableSize == actualTableSize);
    }

    @Test
    @VisibleForTesting
    public void testSmear() {
        int hashCode = 32;
        int expectedResult = 803802283;
        int actualResult = SmallCharMatcher.smear(hashCode);
        assertTrue("Expected smear result to be: " + expectedResult + " but was: " + actualResult, expectedResult == actualResult);
    }

    @Test
    @VisibleForTesting
    public void testCheckFilter() {
        char c = 'a';
        boolean expectedResult = true;
        boolean actualResult = charMatcher.checkFilter(c);
        assertTrue("Expected filter to pass for character: " + c + " but does not.", expectedResult == actualResult);
    }
}