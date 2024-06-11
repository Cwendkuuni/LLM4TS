package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.BitSet;
import java.util.Arrays;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFromEmptySet() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "empty set");
        assertTrue(charMatcher.matches('\0'));
        assertTrue(charMatcher.matches('\u0001'));
        assertFalse(charMatcher.matches('x'));
        BitSet expectedTable = new BitSet();
        charMatcher.setBits(expectedTable);
        BitSet actualTable = new BitSet();
        charMatcher.setBits(actualTable);
        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void testFromSingleCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSet.of('\u0001'), "single character set");
        assertTrue(charMatcher.matches('\u0001'));
        assertFalse(charMatcher.matches('x'));
        BitSet expectedTable = BitSet.of('\u0001');
        charMatcher.setBits(expectedTable);
        BitSet actualTable = new BitSet();
        charMatcher.setBits(actualTable);
        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void testFromMultipleCharacterSet() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSet.of('\u0001', '\u0002', '\u0003'), "multiple character set");
        assertTrue(charMatcher.matches('\u0001'));
        assertTrue(charMatcher.matches('\u0002'));
        assertTrue(charMatcher.matches('\u0003'));
        assertFalse(charMatcher.matches('x'));
        BitSet expectedTable = BitSet.union(BitSet.of('\u0001'), BitSet.of('\u0002'), BitSet.of('\u0003'));
        charMatcher.setBits(expectedTable);
        BitSet actualTable = new BitSet();
        charMatcher.setBits(actualTable);
        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void testSmear() {
        for (int i = 0; i < 256; i++) {
            int hashCode = i;
            int expected = smear(hashCode);
            int actual = SmallCharMatcher.smear(hashCode);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testChooseTableSize() {
        for (int i = 0; i <= 1024; i++) {
            int expected = SmallCharMatcher.chooseTableSize(i);
            int actual = chooseTableSize(i);
            assertEquals(expected, actual);
        }
    }

    private int chooseTableSize(int setSize) {
        int tableSize;
        for (tableSize = 1 << (Integer.highestOneBit(setSize - 1) << 1); tableSize * 0.5 < setSize; tableSize <<= 1) {}
        return tableSize;
    }

    @Test
    public void testCheckFilter() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "empty filter");
        assertTrue(charMatcher.checkFilter('\u0001'));
        CharMatcher charMatcher1 = new SmallCharMatcher(new char[0], 1L, false, "single bit set filter");
        assertTrue(charMatcher1.checkFilter('\u0001'));
        assertFalse(charMatcher1.checkFilter('x'));
        CharMatcher charMatcher2 = new SmallCharMatcher(new char[0], 0b1111_0000_0000_1111_0000_0000_1111_1111L, false, "multiple bits set filter");
        assertTrue(charMatcher2.checkFilter('\u0001'));
        assertTrue(charMatcher2.checkFilter('\u0002'));
        assertFalse(charMatcher2.checkFilter('x'));
    }

    @Test
    public void testMatches() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "empty set and filter");
        assertTrue(charMatcher.matches('\0'));
        assertTrue(charMatcher.matches('\u0001'));
        assertFalse(charMatcher.matches('x'));
        CharMatcher charMatcher1 = new SmallCharMatcher(new char[]{'\u0001'}, 0L, false, "single character set and empty filter");
        assertTrue(charMatcher1.matches('\u0001'));
        assertFalse(charMatcher1.matches('x'));
        CharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'\u0001'}, 1L, false, "single character set and single bit set filter");
        assertTrue(charMatcher2.matches('\u0001'));
        assertFalse(charMatcher2.matches('x'));
        CharMatcher charMatcher3 = new SmallCharMatcher(new char[]{'a', 'b'}, 3L, false, "two characters set and three bits set filter");
        assertTrue(charMatcher3.matches('a'));
        assertTrue(charMatcher3.matches('b'));
        assertFalse(charMatcher3.matches('x'));
        CharMatcher charMatcher4 = new SmallCharMatcher(new char[]{'a', 'b'}, 7L, false, "two characters set and seven bits set filter");
        assertTrue(charMatcher4.matches('a'));
        assertTrue(charMatcher4.matches('b'));
        assertFalse(charMatcher4.matches('x'));
        CharMatcher charMatcher5 = new SmallCharMatcher(new char[]{'a', 'b'}, 15L, false, "two characters set and fifteen bits set filter");
        assertTrue(charMatcher5.matches('a'));
        assertTrue(charMatcher5.matches('b'));
        assertFalse(charMatcher5.matches('x'));
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "empty set and filter");
        BitSet expectedTable1 = new BitSet();
        BitSet actualTable1 = new BitSet();
        charMatcher.setBits(expectedTable1);
        charMatcher.setBits(actualTable1);
        assertEquals(expectedTable1, actualTable1);
        CharMatcher charMatcher1 = new SmallCharMatcher(new char[]{'a'}, 0L, false, "single character set and empty filter");
        BitSet expectedTable2 = BitSet.of('a');
        BitSet actualTable2 = new BitSet();
        charMatcher1.setBits(expectedTable2);
        charMatcher1.setBits(actualTable2);
        assertEquals(expectedTable2, actualTable2);
        CharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a', 'b'}, 3L, false, "two characters set and three bits set filter");
        BitSet expectedTable3 = BitSet.union(BitSet.of('a'), BitSet.of('b'));
        BitSet actualTable3 = new BitSet();
        charMatcher2.setBits(expectedTable3);
        charMatcher2.setBits(actualTable3);
        assertEquals(expectedTable3, actualTable3);
    }
}