package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.*;

import com.google.common.base.BitSet;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = BitSet.create();
        chars.add('a').add('b').add('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b', 'c'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testChooseTableSize() {
        int size = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(2, tableSize);

        size = 10;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(20, tableSize);

        size = 21;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(42, tableSize);

        size = 43;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(84, tableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        int expectedSmearedHashCode = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        assertEquals(expectedSmearedHashCode, smearedHashCode);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[SmallCharMatcher.MAX_SIZE], 0L, false, null);
        assertFalse(charMatcher.checkFilter('a'));

        charMatcher = new SmallCharMatcher(new char[SmallCharMatcher.MAX_SIZE], 1L << 'a', false, null);
        assertTrue(charMatcher.checkFilter('a'));

        charMatcher = new SmallCharMatcher(new char[SmallCharMatcher.MAX_SIZE], 1L << 'a' | 1L << 'b', false, null);
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));

        charMatcher = new SmallCharMatcher(new char[SmallCharMatcher.MAX_SIZE], 1L << 'a' | 1L << 'z', false, null);
        assertFalse(charMatcher.checkFilter('z'));
    }

    @Test
    public void testMatchesCharAtIndex() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, null);

        table[1] = 'a';
        table[2] = 'b';
        assertEquals('a', charMatcher.matches((char)1));
        assertEquals('b', charMatcher.matches((char)2));
        assertFalse(charMatcher.matches((char)0));
        assertFalse(charMatcher.matches((char)3));
    }

    @Test
    public void testMatchesChar() {
        BitSet chars = BitSet.create();
        chars.add('a').add('b').add('c');
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b', 'c'");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        BitSet table = BitSet.create();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[SmallCharMatcher.MAX_SIZE], 0L, false, null);

        charMatcher.setBits(table);
        assertFalse(table.get(0));

        table.set(1, true);
        charMatcher.setBits(table);
        assertTrue(charMatcher.containsZero);
        assertTrue(charMatcher.matches(' '));

        table.set(2, true);
        charMatcher.setBits(table);
        assertFalse(charMatcher.containsZero);
        assertTrue(charMatcher.matches(' '));
        assertTrue(charMatcher.matches('a'));
    }
}