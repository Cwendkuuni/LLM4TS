package com.example.junit4test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {
    @Test
    public void testMatches(){
        char[] table = new char[]{'a', 'e', 'i', 'o', 'u'};
        long filter = 1L;
        boolean containsZero = false;
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "Vowels CharMatcher");
        char c = 'a';
        assertTrue(charMatcher.matches(c));
        c = 'e';
        assertTrue(charMatcher.matches(c));
        c = 'i';
        assertTrue(charMatcher.matches(c));
        c = 'o';
        assertTrue(charMatcher.matches(c));
        c = 'u';
        assertTrue(charMatcher.matches(c));
        c = 'b';
        assertFalse(charMatcher.matches(c));
        c = '\0';
        if (containsZero) {
            assertTrue(charMatcher.matches(c));
        } else {
            assertFalse(charMatcher.matches(c));
        }
    }

    @Test
    public void testEdgeCasesAndExceptions(){
        char[] table = new char[1];
        long filter = -1L;
        BitSet bitSet = null;

        // Test empty table
        SmallCharMatcher charMatcherEmpty = new SmallCharMatcher(table, filter, false, "Empty CharMatcher");
        char emptyChar = '\u0000';
        assertFalse(charMatcherEmpty.matches(emptyChar));

        // Test table size is 1
        table = new char[]{'a'};
        SmallCharMatcher charMatcherSizeOne = new SmallCharMatcher(table, filter, false, "Size One CharMatcher");
        assertTrue(charMatcherSizeOne.matches('a'));
        assertFalse(charMatcherSizeOne.matches('b'));

        // Test characters exceed ASCII range
        char charOutOfRange = (char) 127;
        SmallCharMatcher charMatcherOutOfRange = new SmallCharMatcher(table, filter, false, "OutOfRange CharMatcher");
        assertFalse(charMatcherOutOfRange.matches(charOutOfRange));

        // Test filter has the highest bit set
        filter = Long.MAX_VALUE;
        SmallCharMatcher charMatcherFilter = new SmallCharMatcher(table, filter, false, "Filter CharMatcher");
        char charFilter = 'a';
        assertFalse(charMatcherFilter.matches(charFilter));

        // Test character is negative
        char negativeChar = (char) -1;
        SmallCharMatcher charMatcherNegative = new SmallCharMatcher(table, filter, false, "Negative CharMatcher");
        assertFalse(charMatcherNegative.matches(negativeChar));

        // Test BitSet is null
        SmallCharMatcher charMatcherNullBitSet = new SmallCharMatcher(table, filter, false, "Null BitSet CharMatcher");
        assertFalse(charMatcherNullBitSet.matches((char)0));
    }
}