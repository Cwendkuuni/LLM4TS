package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.BitSet;
import com.google.common.base.FastMatcher;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet charSet = new BitSet();
        char[] chars = new char[]{'a', 'b', 'c'};
        SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches characters: a, b, c");

        assertEquals(3, charMatcher.table.length);
        assertEquals('a', charMatcher.table[0]);
        assertEquals('b', charMatcher.table[1]);
        assertEquals('c', charMatcher.table[2]);
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charSet = new BitSet();
        charMatcher = SmallCharMatcher.from(charSet, "matches no characters");
        assertEquals(0, charMatcher.table.length);
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }

    @Test
    public void testMatches() {
        BitSet charSet = new BitSet();
        char[] chars = new char[]{'a', 'b', 'c'};
        SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches characters: a, b, c");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charSet = new BitSet();
        charMatcher = SmallCharMatcher.from(charSet, "matches no characters");
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        BitSet bitSet = new BitSet();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "matches no characters");
        charMatcher.setBits(bitSet);

        assertEquals(0, bitSet.cardinality());
        assertFalse(bitSet.get(0));
        assertFalse(bitSet.get('a'));
        assertFalse(bitSet.get('b'));
        assertFalse(bitSet.get('c'));

        charMatcher = SmallCharMatcher.from(new BitSet(new long[] { Long.parseUnsignedLong("0000000000000001") }), "matches character: a");
        charMatcher.setBits(bitSet);

        assertEquals(1, bitSet.cardinality());
        assertTrue(bitSet.get(0));
        assertFalse(bitSet.get('a'));
        assertFalse(bitSet.get('b'));
        assertFalse(bitSet.get('c'));

        charMatcher = SmallCharMatcher.from(new BitSet(new long[] { Long.parseUnsignedLong("00000000000000ff") }), "matches characters: a-z");
        charMatcher.setBits(bitSet);

        assertEquals(26, bitSet.cardinality());
        for (char c = 'a'; c <= 'z'; c++) {
            assertTrue(bitSet.get(c));
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            assertFalse(bitSet.get(c));
        }
        for (char c = '\u0001'; c <= '\u007F'; c++) {
            assertFalse(bitSet.get(c));
        }
        for (char c = '\u0080'; c <= '\uffff'; c++) {
            assertFalse(bitSet.get(c));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(null, 0, false, "matches no characters");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFilterBit() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0x80000001L, false, "matches no characters");
    }

    @Test
    public void testStripLeadingZeroes() {
        BitSet charSet = new BitSet();
        char[] chars = new char[]{'0', '1', '2', '3', 'a'};
        SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches characters: 0, 1, 2, 3, a");

        assertEquals(5, charMatcher.table.length);
        assertEquals('1', charMatcher.table[0]);
        assertEquals('2', charMatcher.table[1]);
        assertEquals('3', charMatcher.table[2]);
        assertEquals('a', charMatcher.table[3]);
        assertEquals('\0', charMatcher.table[4]);
        assertFalse(charMatcher.containsZero);
    }

    @Test
    public void testStripLeadingZeroesWithZero() {
        BitSet charSet = new BitSet();
        char[] chars = new char[]{'0', '1', '2', '3', 'a', '\0'};
        SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches characters: 0, 1, 2, 3, a, 0");

        assertEquals(6, charMatcher.table.length);
        assertEquals('1', charMatcher.table[0]);
        assertEquals('2', charMatcher.table[1]);
        assertEquals('3', charMatcher.table[2]);
        assertEquals('a', charMatcher.table[3]);
        assertEquals('\0', charMatcher.table[4]);
        assertEquals('\0', charMatcher.table[5]);
        assertTrue(charMatcher.containsZero);
    }

    @VisibleForTesting
    static SmallCharMatcher createFromCharArray(char[] chars) {
        BitSet charSet = new BitSet();
        for (char c : chars) {
            charSet.set(c);
        }
        return SmallCharMatcher.from(charSet, "matches characters: " + new String(chars));
    }

    @Test
    public void testTableSize() {
        char[] chars = new char[1];
        SmallCharMatcher charMatcher = createFromCharArray(chars);
        assertEquals(2, charMatcher.table.length);

        chars = new char[1024];
        charMatcher = createFromCharArray(chars);
        assertEquals(1024 * 2, charMatcher.table.length);

        chars = new char[1025];
        charMatcher = createFromCharArray(chars);
        assertEquals(1026 * 2, charMatcher.table.length);

        chars = new char[Integer.MAX_VALUE - 1];
        charMatcher = createFromCharArray(chars);
        int tableSize = Integer.highestOneBit(chars.length - 1) << 1;
        assertEquals(tableSize, charMatcher.table.length);

        charMatcher = createFromCharArray(new char[Integer.MAX_VALUE]);
        tableSize = Integer.highestOneBit((Integer.MAX_VALUE - 1) << 1) << 1;
        assertEquals(tableSize, charMatcher.table.length);
    }

    @Test
    public void testCharsOutOfBounds() {
        char[] chars = new char[1];
        SmallCharMatcher charMatcher = createFromCharArray(chars);

        try {
            charMatcher.matches((char) (chars.length + 1));
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        try {
            charMatcher.matches((char) (chars.length - 1));
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSmear() {
        for (int hashCode = 0; hashCode <= Integer.MAX_VALUE; hashCode++) {
            int smearedHashCode = SmallCharMatcher.smear(hashCode);
            assertEquals(Integer.toHexString(smear(hashCode)), Integer.toHexString(smearedHashCode));
        }
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "matches no characters");
        assertFalse(charMatcher.checkFilter('\u0001'));
        assertFalse(charMatcher.checkFilter('\u007f'));
        assertFalse(charMatcher.checkFilter('\u0080'));
        assertFalse(charMatcher.checkFilter('\uffff'));
        assertFalse(charMatcher.checkFilter('\u0000'));

        charMatcher = new SmallCharMatcher(new char[]{'a'}, 1L, false, "matches character: a");
        assertTrue(charMatcher.checkFilter('a'));
        assertFalse(charMatcher.checkFilter('b'));

        charMatcher = new SmallCharMatcher(new char[]{'a'}, 1L << 5, false, "matches character: a");
        assertTrue(charMatcher.checkFilter('a'));
        assertFalse(charMatcher.checkFilter('b'));

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3L, false, "matches characters: a, b");
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));
        assertFalse(charMatcher.checkFilter('c'));

        charMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 3L | 1L << 5, false, "matches characters: a, b");
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));
        assertTrue(charMatcher.checkFilter('c'));
    }

    @Test
    public void testCharsInTable() {
        char[] chars = new char[1];
        SmallCharMatcher charMatcher = createFromCharArray(chars);

        assertEquals(chars[0], charMatcher.table[0]);

        chars = new char[2];
        charMatcher = createFromCharArray(chars);

        assertEquals(chars[0], charMatcher.table[0]);
        assertEquals(chars[1], charMatcher.table[1]);

        chars = new char[128];
        charMatcher = createFromCharArray(chars);

        for (int i = 0; i < charMatcher.table.length; i++) {
            assertEquals(chars[i], charMatcher.table[i]);
        }

        chars = new char[129];
        charMatcher = createFromCharArray(chars);

        for (int i = 0; i < charMatcher.table.length; i++) {
            assertEquals(chars[i % charMatcher.table.length], charMatcher.table[i]);
        }
    }
}