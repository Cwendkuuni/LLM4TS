package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "TestCharMatcher");
        // Test empty CharMatcher
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));

        charMatcher = SmallCharMatcher.from(BitSet.create(), "TestCharMatcher2", "a");
        // Test CharMatcher with single character
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));

        charMatcher = SmallCharMatcher.from(BitSet.create(0, 1, 2), "TestCharMatcher3", "a", "b", "c");
        // Test CharMatcher with multiple characters
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(BitSet.create(0, 4, 6), "TestCharMatcher4", "b", "e", "f");
        // Test CharMatcher with multiple characters and check index
        assertFalse(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('e'));
        assertFalse(charMatcher.matches('f'));
        assertFalse(charMatcher.matches('g'));
    }

    @Test
    public void testSetBits() {
        BitSet testBitSet = BitSet.create();
        SmallCharMatcher charMatcher = SmallCharMatcher.from(testBitSet, "TestCharMatcher5");
        charMatcher.setBits(testBitSet);

        assertTrue(testBitSet.get(0));
        for (char c : charMatcher.table) {
            if (c != '\0') {
                assertTrue(testBitSet.get(c));
            }
        }
    }

    @Test
    public void testFrom() {
        BitSet testBitSet = BitSet.create(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        SmallCharMatcher charMatcher = SmallCharMatcher.from(testBitSet, "TestCharMatcher6");

        int size = charMatcher.table.length;
        for (int i = 0; i < size; i++) {
            int index = i % 10;
            assertEquals((char) (index + '0'), charMatcher.table[i]);
            assertTrue(testBitSet.get((char) (index + '0')));
        }

        int hashCode = 123;
        int tableSize = SmallCharMatcher.chooseTableSize(100);
        char[] table = new char[tableSize];
        for (int i = 0; i < size; i++) {
            int c = (char) (i % 10 + '0');
            int index = (SmallCharMatcher.smear(c) & (tableSize - 1));
            table[index] = c;
        }
        CharMatcher testCharMatcher = new SmallCharMatcher(table, 0x1L, false, "TestCharMatcher7");

        assertArrayEquals(charMatcher.table, testCharMatcher.table);
        assertEquals(charMatcher.filter, testCharMatcher.filter);
        assertEquals(charMatcher.containsZero, testCharMatcher.containsZero);
        assertEquals(charMatcher.description, testCharMatcher.description);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 42;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        int tableSize = SmallCharMatcher.chooseTableSize(1000);
        char[] table = new char[tableSize];
        int index = (smearedHashCode & (tableSize - 1));

        // Check index is not the same as the original hashCode
        assertNotSame(hashCode, index);
        // Check index is within the table size
        assertTrue(index >= 0);
        assertTrue(index < tableSize);
    }
}