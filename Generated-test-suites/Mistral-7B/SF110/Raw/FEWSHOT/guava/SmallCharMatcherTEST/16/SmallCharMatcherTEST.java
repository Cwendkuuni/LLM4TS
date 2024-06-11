package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesChar() {
        BitSet expectedChars = new BitSet();
        expectedChars.set('a');
        expectedChars.set('e');
        expectedChars.set('i');

        CharMatcher charMatcher = SmallCharMatcher.from(expectedChars, "matches 'a', 'e', 'i'");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('e'));
        assertTrue(charMatcher.matches('i'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('\u0000'));
    }

    @Test
    public void testMatchesEmpty() {
        BitSet expectedChars = new BitSet();

        CharMatcher charMatcher = SmallCharMatcher.from(expectedChars, "matches empty set");

        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('\u0000'));
    }

    @Test
    public void testMatchesSingleCharacter() {
        BitSet expectedChars = new BitSet();
        expectedChars.set('x');

        CharMatcher charMatcher = SmallCharMatcher.from(expectedChars, "matches 'x'");

        assertTrue(charMatcher.matches('x'));
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('\u0000'));
    }

    @Test
    public void testMatchesMultipleCharacters() {
        BitSet expectedChars = new BitSet();
        expectedChars.set('a');
        expectedChars.set('b');
        expectedChars.set('c');

        CharMatcher charMatcher = SmallCharMatcher.from(expectedChars, "matches 'a', 'b', 'c'");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
        assertFalse(charMatcher.matches('\u0000'));
    }

    @Test
    public void testMatchesContainsZero() {
        BitSet expectedChars = new BitSet();
        expectedChars.set('\u0000');

        CharMatcher charMatcher = SmallCharMatcher.from(expectedChars, "matches '\u0000'");

        assertTrue(charMatcher.matches('\u0000'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChooseTableSizeNegativeSize() {
        int setSize = -1;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
    }

    @Test
    public void testCheckFilter() {
        long filter = 0b0000_0001_1111_1111_0000_0001_1111_1111L;
        char c = 'x';

        assertTrue(SmallCharMatcher.checkFilter((int)c, filter));
        assertTrue(SmallCharMatcher.checkFilter((int)'\u0000', filter));
        assertFalse(SmallCharMatcher.checkFilter((int)'y', filter));
    }

    @Test
    public void testSmearedHashCode() {
        int hashCode = 123;
        int smearedHashCode = SmallCharMatcher.smear(hashCode);

        assertEquals(461845907 * Integer.rotateLeft(hashCode * -862048943, 15), smearedHashCode);
    }
}