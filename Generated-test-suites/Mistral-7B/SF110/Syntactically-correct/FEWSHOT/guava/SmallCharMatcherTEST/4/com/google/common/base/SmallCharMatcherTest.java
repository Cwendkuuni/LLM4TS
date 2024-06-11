package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b'), "Matches 'a' and 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));

        charMatcher = SmallCharMatcher.from(new BitSet().add('\u0000').add('x'), "Matches null and 'x'");
        assertTrue(charMatcher.matches('\u0000'));
        assertTrue(charMatcher.matches('x'));
        assertFalse(charMatcher.matches('a'));

        charMatcher = SmallCharMatcher.from(new BitSet(), "Matches no character");
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('\u0000'));
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('b'), "Matches 'a' and 'b'");
        BitSet table = new BitSet();
        charMatcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('c'));

        charMatcher = SmallCharMatcher.from(new BitSet().add('\u0000').add('x'), "Matches null and 'x'");
        table.clear();
        charMatcher.setBits(table);
        assertTrue(table.get('\u0000'));
        assertTrue(table.get('x'));
        assertFalse(table.get('a'));

        charMatcher = SmallCharMatcher.from(new BitSet(), "Matches no character");
        table.clear();
        charMatcher.setBits(table);
        assertFalse(table.get('a'));
        assertFalse(table.get('\u0000'));
    }

    @Test
    public void testTableSize() {
        checkArgument(SmallCharMatcher.chooseTableSize(0) == 2, "Invalid table size for empty set");

        checkArgument(SmallCharMatcher.chooseTableSize(1) == 2, "Invalid table size for single character set");

        checkArgument(SmallCharMatcher.chooseTableSize(3) == 4, "Invalid table size for set with three elements");

        checkArgument(SmallCharMatcher.chooseTableSize(13) == 16, "Invalid table size for set with 13 elements");

        checkArgument(SmallCharMatcher.chooseTableSize(15) == 32, "Invalid table size for set with 15 elements");

        checkArgument(SmallCharMatcher.chooseTableSize(65535) == 128 * 1024, "Invalid table size for set with 65535 elements");
    }

    @Test
    public void testSmearedHashCode() {
        checkArgument(SmallCharMatcher.smear(Character.codePointAt('a', 0)) == 1298812305, "Invalid smeared hashcode for 'a'");
        checkArgument(SmallCharMatcher.smear(Character.codePointAt('z', 0)) == 1616208841, "Invalid smeared hashcode for 'z'");
        checkArgument(SmallCharMatcher.smear(Character.codePointAt('\u0304', 0)) == 3401097701, "Invalid smeared hashcode for 'combining acute accent'");
        checkArgument(SmallCharMatcher.smear(Character.codePointAt('\uD800', 0)) == -1413225955, "Invalid smeared hashcode for high surrogate");
    }
}