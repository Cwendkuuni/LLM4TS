package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.BitSet;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "TestCharMatcher");

        Assert.assertTrue(charMatcher.matches('a'));
        Assert.assertTrue(charMatcher.matches('b'));
        Assert.assertTrue(charMatcher.matches('c'));
        Assert.assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testMatches() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "EmptyCharMatcher");
        Assert.assertFalse(charMatcher.matches('\0'));
        Assert.assertFalse(charMatcher.matches('a'));

        BitSet chars = new BitSet();
        chars.set('a');
        charMatcher = SmallCharMatcher.from(chars, "SingleCharMatcher");
        Assert.assertTrue(charMatcher.matches('a'));
        Assert.assertFalse(charMatcher.matches('b'));

        chars.set('a');
        chars.set('b');
        charMatcher = SmallCharMatcher.from(chars, "MultipleCharsMatcher");
        Assert.assertTrue(charMatcher.matches('a'));
        Assert.assertTrue(charMatcher.matches('b'));

        chars.set('a');
        chars.set('b');
        chars.set('c');
        charMatcher = SmallCharMatcher.from(chars, "MultipleCharsWithZeroMatcher");
        charMatcher.setBits(chars);
        Assert.assertTrue(charMatcher.matches('a'));
        Assert.assertTrue(charMatcher.matches('b'));
        Assert.assertTrue(charMatcher.matches('c'));
        Assert.assertTrue(charMatcher.matches('\0'));

        chars.clear();
        charMatcher = SmallCharMatcher.from(chars, "EmptyCharMatcherWithZero");
        charMatcher.setBits(chars);
        Assert.assertFalse(charMatcher.matches('\0'));
        Assert.assertFalse(charMatcher.matches('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullDescription() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher.from(chars, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromEmptyBitSet() {
        SmallCharMatcher.from(new BitSet(), "");
    }

    @Test
    @VisibleForTesting
    public void testChooseTableSize() {
        int size = 1;
        int tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(2, tableSize);

        size = 16;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(32, tableSize);

        size = 32;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(64, tableSize);

        size = 1024;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(1024, tableSize);

        size = 1025;
        tableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(2048, tableSize);
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);
        int expectedSmearedHashCode = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        assertEquals(expectedSmearedHashCode, smearedHashCode);
    }
}