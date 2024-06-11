package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.BitSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SmallCharMatcherTest {
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches 'a', 'b' and 'c'");
        Assert.assertTrue(charMatcher.matches('a'));
        Assert.assertTrue(charMatcher.matches('b'));
        Assert.assertTrue(charMatcher.matches('c'));
        Assert.assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testChooseTableSize() {
        int size = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(size);
        Assert.assertEquals(2, tableSize);

        size = 10;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        Assert.assertEquals(20, tableSize);

        size = 25;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        Assert.assertEquals(50, tableSize);

        size = 127;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        Assert.assertEquals(256, tableSize);

        size = 128;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        Assert.assertEquals(512, tableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        Assert.assertNotEqual(hashCode, smearedHashCode);

        int expectedTableIndex = smearedHashCode & (SmallCharMatcher.MAX_SIZE - 1);
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        Arrays.fill(table, (char)0);
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, null);
        int actualTableIndex = Arrays.binarySearch(table, (char)expectedTableIndex);
        Assert.assertEquals(-(actualTableIndex + 1), expectedTableIndex);
    }

    @Test
    @VisibleForTesting
    public void testMatches() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        Arrays.fill(table, (char)0);
        CharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, null);

        Assert.assertFalse(charMatcher.matches((char)0));
        Assert.assertFalse(charMatcher.matches((char)1));

        table[0] = 'a';
        charMatcher.setBits(new BitSet());
        Assert.assertTrue(charMatcher.matches('a'));

        table[511] = 'z';
        charMatcher.setBits(new BitSet());
        Assert.assertTrue(charMatcher.matches('z'));

        charMatcher = new SmallCharMatcher(table, 0b11111111111111111111111111111111L, false, null);
        for (char c = '0'; c <= '~'; c++) {
            Assert.assertTrue(charMatcher.matches(c));
        }
        Assert.assertFalse(charMatcher.matches(' '));
        Assert.assertFalse(charMatcher.matches('\u007F'));
    }

    @Test
    public void testContainsZero() {
        CharMatcher charMatcher1 = new SmallCharMatcher(new char[0], 0L, true, "matches nothing");
        Assert.assertFalse(charMatcher1.matches((char)0));
        Assert.assertFalse(charMatcher1.matches((char)1));

        CharMatcher charMatcher2 = new SmallCharMatcher(new char[0], 0L, false, "matches all characters");
        Assert.assertTrue(charMatcher2.matches((char)0));
        Assert.assertTrue(charMatcher2.matches((char)1));
    }
}