package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "charMatcherTest");

        assertEquals("charMatcherTest", charMatcher.description());

        // Test empty character set
        BitSet emptySet = new BitSet();
        CharMatcher emptyMatcher = SmallCharMatcher.from(emptySet, "emptyMatcherTest");
        assertFalse(emptyMatcher.matches('a'));
        assertFalse(emptyMatcher.matches('\0'));

        // Test character set with single character
        chars.set(0, 'a');
        CharMatcher singleCharacterMatcher = SmallCharMatcher.from(chars, "singleCharacterMatcherTest");
        assertTrue(singleCharacterMatcher.matches('a'));
        assertFalse(singleCharacterMatcher.matches('b'));
        assertFalse(singleCharacterMatcher.matches('\0'));

        // Test character set with multiple characters
        chars.set(1, 'b');
        CharMatcher multipleCharactersMatcher = SmallCharMatcher.from(chars, "multipleCharactersMatcherTest");
        assertTrue(multipleCharactersMatcher.matches('a'));
        assertTrue(multipleCharactersMatcher.matches('b'));
        assertFalse(multipleCharactersMatcher.matches('c'));
        assertFalse(multipleCharactersMatcher.matches('\0'));

        // Test character set with zero
        chars.set(0, '\0');
        CharMatcher zeroMatcher = SmallCharMatcher.from(chars, "zeroMatcherTest");
        assertTrue(zeroMatcher.matches('\0'));
        assertFalse(zeroMatcher.matches('a'));
        assertFalse(zeroMatcher.matches('b'));
    }

    @Test
    public void testMatches() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b'), "charMatcherTest");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('\0'));
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b'), "charMatcherTest");
        BitSet table = new BitSet();

        charMatcher.setBits(table);

        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('c'));
        assertFalse(table.get('\0'));
    }
}