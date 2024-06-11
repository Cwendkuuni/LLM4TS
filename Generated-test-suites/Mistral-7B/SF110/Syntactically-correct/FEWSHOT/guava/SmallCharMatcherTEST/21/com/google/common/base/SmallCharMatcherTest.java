package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesCharacterInTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b'), "Matches 'a' and 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
    }

    @Test
    public void testMatchesCharacterNotInTable() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b'), "Matches 'a' and 'b'");
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testMatchesZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "Matches zero");
        assertTrue(charMatcher.matches('\0'));
    }

    @Test
    public void testMatchesNonZeroAndContainsZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, true, "Matches 'a' and zero");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('\0'));
    }

    @Test
    public void testMatchesNonZeroAndDoesNotContainZero() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matches 'a'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('\0'));
    }

    @Test
    public void testMatchesMultipleCharacters() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b').add('c'), "Matches 'a', 'b', and 'c'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddingCharacterToEmptyMatcherFails() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Does not match any character");
        charMatcher.setBits(new BitSet());
        charMatcher.setBits(new BitSet().add('a'));
    }

    @Test
    public void testTableSizeCalculation() {
        int setSize = 10;
        int expectedTableSize = (1 << 3) << 1; // 16
        int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedTableSize, actualTableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 'a'.hashCode();
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        assertEquals(-1515401117, smearedHashCode);
    }
}