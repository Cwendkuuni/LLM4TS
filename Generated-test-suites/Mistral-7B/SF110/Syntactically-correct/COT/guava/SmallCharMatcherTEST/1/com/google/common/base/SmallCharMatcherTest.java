package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSets;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesCharacterInTable() {
        char[] chars = new char[]{'a', 'b', 'c'};
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(chars), "Matcher for chars a, b, c");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
    }

    @Test
    public void testMatchesCharacterNotInTable() {
        char[] chars = new char[]{'a', 'b'};
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(chars), "Matcher for chars a, b");
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testMatchesCharacterZero() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(), "Matcher for zero character");
        assertTrue(charMatcher.matches('\0'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromEmptyBitSetThrowsException() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(), "Empty BitSet should throw IllegalArgumentException");
    }

    @Test
    public void testMatchesCharacterInFilter() {
        char[] chars = new char[]{'a', 'b', 'c'};
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(chars), "Matcher for chars a, b, c");
        long filter = charMatcher.filter();
        assertTrue(Character.isDefined((char)Character.forDigit('a' + 1, 16)) ^ (filter & (1L << Character.forDigit('a' + 1, 16))));
        assertTrue(Character.isDefined((char)Character.forDigit('b' + 1, 16)) ^ (filter & (1L << Character.forDigit('b' + 1, 16))));
        assertTrue(Character.isDefined((char)Character.forDigit('c' + 1, 16)) ^ (filter & (1L << Character.forDigit('c' + 1, 16))));
    }

    @Test
    public void testMatchesCharacterNotInFilter() {
        char[] chars = new char[]{'a', 'b'};
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(chars), "Matcher for chars a, b");
        long filter = charMatcher.filter();
        assertFalse(Character.isDefined((char)Character.forDigit('c' + 1, 16)) ^ (filter & (1L << Character.forDigit('c' + 1, 16))));
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet('a', 'b', 'c'), "Matcher for chars a, b, c");
        BitSet expectedTable = BitSets.newBitSet('a', 'b', 'c');
        char[] table = new char[SmallCharMatcher.chooseTableSize(expectedTable.size())];
        charMatcher.setBits(expectedTable);
        for (int i = 0; i < table.length; i++) {
            assertEquals(expectedTable.get((char) (i + '\u0000')), table[i] != '\u0000');
        }
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(16, SmallCharMatcher.chooseTableSize(16));
        assertEquals(512, SmallCharMatcher.chooseTableSize(512));
        assertEquals(1024, SmallCharMatcher.chooseTableSize(1023));
        assertEquals(2048, SmallCharMatcher.chooseTableSize(2047));
        assertEquals(4096, SmallCharMatcher.chooseTableSize(4095));
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        int expectedSmearedHashCode = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        assertEquals(expectedSmearedHashCode, smearedHashCode);
    }
}