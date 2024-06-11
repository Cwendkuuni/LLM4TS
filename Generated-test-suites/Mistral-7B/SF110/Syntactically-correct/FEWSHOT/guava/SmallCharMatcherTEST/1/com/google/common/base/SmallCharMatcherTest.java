package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.platform.commons.util.Collections.*;

import java.util.*;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet charSet = createBitSet();
        charSet.add('a');
        charSet.add('b');
        charSet.add('c');
        CharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches characters a, b, c");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        BitSet table = new BitSet();
        charMatcher.setBits(table);
        assertEquals(3, table.cardinality());
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
    }

    @Test
    public void testChooseTableSize() {
        int size = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(2, tableSize);

        size = 16;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        int expectedTableSize = 32;
        assertEquals(expectedTableSize, tableSize);

        size = 1024;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        expectedTableSize = 2048;
        assertEquals(expectedTableSize, tableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);

        int expectedSmearedHashCode = 403434254;
        assertEquals(expectedSmearedHashCode, smearedHashCode);

        hashCode = -123;
        smearedHashCode = SmallCharMatcher.smear(hashCode);

        expectedSmearedHashCode = -513158802;
        assertEquals(expectedSmearedHashCode, smearedHashCode);
    }

    @Test
    public void testCheckFilter() {
        long filter = 1L;
        boolean containsZero = false;

        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], filter, containsZero, "empty filter");
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));
        assertTrue(charMatcher.checkFilter('c'));
        assertFalse(charMatcher.checkFilter('d'));

        filter |= 1L << 32;
        charMatcher = new SmallCharMatcher(new char[0], filter, containsZero, "filter with highest bit set");
        assertFalse(charMatcher.checkFilter(' '));
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));
        assertTrue(charMatcher.checkFilter('c'));
        assertFalse(charMatcher.checkFilter('d'));

        containsZero = true;
        charMatcher = new SmallCharMatcher(new char[0], filter, containsZero, "filter with zero set");
        assertTrue(charMatcher.checkFilter('\0'));
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));
        assertTrue(charMatcher.checkFilter('c'));
        assertFalse(charMatcher.checkFilter('d'));
    }

    @Test
    public void testMatches() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        long filter = 0x1L << 1 | 0x1L << 3;
        boolean containsZero = false;

        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "matches characters 1 and 3");
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
        assertFalse(charMatcher.matches('0'));
        assertFalse(charMatcher.matches('2'));

        char tableChar = '1';
        int index = (smear(tableChar) & (SmallCharMatcher.MAX_SIZE - 1));
        table[index] = tableChar;

        charMatcher = new SmallCharMatcher(table, filter, containsZero, "matches character 1");
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
        assertFalse(charMatcher.matches('0'));
        assertTrue(charMatcher.matches('1'));
        assertFalse(charMatcher.matches('2'));
    }

    @Test
    public void testSetBits() {
        BitSet table = createBitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(Bits.create(true, false, true), "matches characters a, b, c");
        charMatcher.setBits(table);
        assertEquals(3, table.cardinality());
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));

        table = new BitSet();
        charMatcher = new SmallCharMatcher(new char[0], 0, false, "empty char matcher");
        charMatcher.setBits(table);
        assertTrue(table.isEmpty());
    }

    private static BitSet createBitSet() {
        return new BitSet(new BitArray(new long[1]));
    }
}