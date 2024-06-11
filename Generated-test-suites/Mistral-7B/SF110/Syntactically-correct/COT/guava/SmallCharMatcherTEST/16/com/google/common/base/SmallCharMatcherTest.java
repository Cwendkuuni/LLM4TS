package com.example.junit.tests.smallcharmatcher;

import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

    @Test
    public void testSmallCharMatcher_givenEmptyBitSet_whenCreateMatcher_thenReturnMatcherContainingNoChars() {
        CharMatcher charMatcher = SmallCharMatcher.from(Collections.emptyBitSet(), "Empty CharMatcher");
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
        BitSet expectedTable = Collections.emptyBitSet();
        char[] table = charMatcher.toPrecompiledPattern().asCharArray();
        assertEquals(expectedTable, BitSet.valueOf(table));
    }

    @Test
    public void testSmallCharMatcher_givenBitSetWithOneChar_whenCreateMatcher_thenReturnMatcherContainingThatChar() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a'}), "CharMatcher for 'a'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('b'));
        BitSet expectedTable = new BitSet(new long[]{1L << 'a'});
        char[] table = charMatcher.toPrecompiledPattern().asCharArray();
        assertEquals(expectedTable, BitSet.valueOf(table));
    }

    @Test
    public void testSmallCharMatcher_givenBitSetWithMultipleChars_whenCreateMatcher_thenReturnMatcherContainingAllChars() {
        Set<Character> chars = new HashSet<Character>() {{
            add('a');
            add('b');
            add('c');
        }};
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(chars.stream().mapToLong(c -> 1L << c.charValue()).toArray()), "CharMatcher for {'a', 'b', 'c'}");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('d'));
        BitSet expectedTable = new BitSet(chars.stream().mapToLong(c -> 1L << c.charValue()).toArray());
        char[] table = charMatcher.toPrecompiledPattern().asCharArray();
        assertEquals(expectedTable, BitSet.valueOf(table));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSmallCharMatcher_givenBitSetWithTooManyChars_whenCreateMatcher_thenThrowException() {
        Set<Character> chars = new HashSet<Character>() {{
            addAll(Collections.nCopies(SmallCharMatcher.MAX_SIZE + 1, 'a').toArray());
        }};
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[SmallCharMatcher.chooseTableSize(chars.size())], 0L, false, "CharMatcher for {'a'}");
        charMatcher.matches((char)SmallCharMatcher.MAX_SIZE);
    }

    @Test
    public void testSmallCharMatcher_matches_givenCharInTable_whenCheckMatcher_thenReturnTrue() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a'}), "CharMatcher for 'a'");
        assertTrue(charMatcher.matches('a'));
    }

    @Test
    public void testSmallCharMatcher_matches_givenCharNotInTable_whenCheckMatcher_thenReturnFalse() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a'}), "CharMatcher for 'a'");
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testSmallCharMatcher_matches_givenCharAtStartOfTable_whenCheckMatcher_thenReturnTrue() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a'}), "CharMatcher for 'a'");
        assertTrue(charMatcher.matches(charMatcher.toPrecompiledPattern().charAt(0)));
    }

    @Test
    public void testSmallCharMatcher_matches_givenCharAtEndOfTable_whenCheckMatcher_thenReturnTrue() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a'}), "CharMatcher for 'a'");
        int lastIndex = charMatcher.toPrecompiledPattern().length - 1;
        assertTrue(charMatcher.matches(charMatcher.toPrecompiledPattern().charAt(lastIndex)));
    }

    @Test
    public void testSmallCharMatcher_matches_givenCharInMiddleOfTable_whenCheckMatcher_thenReturnFalse() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a', 1L << 'b'}), "CharMatcher for {'a', 'b'}");
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testSmallCharMatcher_matches_givenCharAlreadyProcessed_whenCheckMatcher_thenReturnFalse() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(new long[]{1L << 'a'}), "CharMatcher for 'a'");
        int startingIndex = charMatcher.toPrecompiledPattern().length - 1;
        int processedIndex = startingIndex - 1;
        assertFalse(charMatcher.matches(charMatcher.toPrecompiledPattern().charAt(processedIndex)));
    }

    @Test(expected = IllegalStateException.class)
    public void testSmallCharMatcher_givenNullDescription_whenCreateMatcher_thenThrowException() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSmallCharMatcher_givenNullTable_whenCreateMatcher_thenThrowException() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(null, 0L, false, "CharMatcher for null");
    }
}