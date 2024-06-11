package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        assertThat(reader, isInstanceOf(CharacterReader.class));
        assertThat(reader.input, is(input.toCharArray()));
        assertThat(reader.length, is(input.length()));
    }

    @Test
    public void testIsEmpty_whenEmpty_shouldReturnTrue() {
        CharacterReader reader = new CharacterReader("");
        assertThat(reader.isEmpty(), is(true));
    }

    @Test
    public void testIsEmpty_whenNotEmpty_shouldReturnFalse() {
        CharacterReader reader = new CharacterReader("abc");
        assertThat(reader.isEmpty(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testCurrent_whenAtEnd_shouldThrowException() {
        CharacterReader reader = new CharacterReader("");
        reader.consume();
        reader.consume();
        reader.current();
    }

    @Test
    public void testConsume_whenNotAtEnd_shouldConsumeCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        char expected = 'a';
        char actual = reader.consume();
        assertThat(actual, is(expected));
    }

    @Test
    public void testConsume_whenAtEnd_shouldReturnEOF() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        char actual = reader.consume();
        assertThat(actual, is(CharacterReader.EOF));
    }

    @Test
    public void testUnconsume_whenPositionHasBeenAdvanced_shouldRevertPosition() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        char actual = reader.current();
        assertThat(actual, is('a'));
    }

    @Test(expected = IllegalStateException.class)
    public void testUnconsume_whenNotConsumedBefore_shouldThrowException() {
        CharacterReader reader = new CharacterReader("abc");
        reader.unconsume();
    }

    @Test
    public void testAdvance_whenNotAtEnd_shouldAdvancePosition() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        char actual = reader.current();
        assertThat(actual, is('b'));
    }

    @Test(expected = IllegalStateException.class)
    public void testAdvance_whenAtEnd_shouldThrowException() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
    }

    @Test
    public void testMark_whenNotAtMark_shouldMarkPosition() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        char actual = reader.current();
        int mark = reader.mark;
        reader.consume();
        int pos = reader.pos;
        reader.rewindToMark();
        char actualMarked = reader.current();
        assertThat(actualMarked, is(actual));
        assertThat(pos, is(mark));
    }

    @Test(expected = IllegalStateException.class)
    public void testMark_whenAlreadyMarked_shouldThrowException() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.mark();
    }

    @Test
    public void testRewindToMark_whenMarked_shouldRevertPosition() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        char actual = reader.current();
        assertThat(actual, is('a'));
    }

    @Test(expected = IllegalStateException.class)
    public void testRewindToMark_whenNotMarked_shouldThrowException() {
        CharacterReader reader = new CharacterReader("abc");
        reader.rewindToMark();
    }

    @Test
    public void testConsumeAsString_whenSingleCharacter_shouldReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abc");
        String expected = "a";
        String actual = reader.consumeAsString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testConsumeAsString_whenMultipleCharacters_shouldReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abc");
        String expected = "ab";
        reader.consume();
        String actual = reader.consumeAsString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testNextIndexOf_whenTargetIsSingleCharacter_shouldReturnCorrectIndex() {
        CharacterReader reader = new CharacterReader("aaabccc");
        int expected = 1;
        int actual = reader.nextIndexOf('a');
        assertThat(actual, is(expected));
    }

    @Test
    public void testNextIndexOf_whenTargetIsSequence_shouldReturnCorrectIndex() {
        CharacterReader reader = new CharacterReader("aaabccc");
        String expected = "bb";
        int actual = reader.nextIndexOf(expected);
        assertThat(actual, is(2));
    }

    @Test
    public void testConsumeTo_whenTargetIsSingleCharacter_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "a";
        String actual = reader.consumeTo('a');
        assertThat(actual, is(expected));
        char consumed = reader.current();
        assertThat(consumed, is('b'));
    }

    @Test
    public void testConsumeTo_whenTargetIsSequence_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "ab";
        String actual = reader.consumeTo("ab");
        assertThat(actual, is(expected));
        char consumed = reader.current();
        assertThat(consumed, is('c'));
    }

    @Test
    public void testConsumeToAny_whenTargetIsSingleCharacter_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd");
        char expected = 'b';
        String actual = reader.consumeToAny(expected);
        assertThat(actual, is(expected + ""));
        char consumed = reader.current();
        assertThat(consumed, is('c'));
    }

    @Test
    public void testConsumeToAny_whenTargetIsSequence_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd");
        char[] expected = {'b', 'c'};
        String actual = reader.consumeToAny(expected);
        assertThat(actual, is(new String(expected)));
        char consumed = reader.current();
        assertThat(consumed, is('d'));
    }

    @Test
    public void testConsumeToEnd_whenNotAtEnd_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "abcd";
        String actual = reader.consumeToEnd();
        assertThat(actual, is(expected));
        assertThat(reader.isEmpty(), is(true));
    }

    @Test
    public void testConsumeLetterSequence_whenStringContainsLetterSequence_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcdABCdef");
        String expected = "abcd";
        String actual = reader.consumeLetterSequence();
        assertThat(actual, is(expected));
    }

    @Test
    public void testConsumeLetterThenDigitSequence_whenStringContainsLetterThenDigitSequence_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd1234ABCDEF");
        String expected = "abcd1234";
        String actual = reader.consumeLetterThenDigitSequence();
        assertThat(actual, is(expected));
    }

    @Test
    public void testConsumeHexSequence_whenStringContainsHexSequence_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd%61%63%61%65");
        String expected = "abcdabcde";
        String actual = reader.consumeHexSequence();
        assertThat(actual, is(expected));
    }

    @Test
    public void testConsumeDigitSequence_whenStringContainsDigitSequence_shouldConsumeAndReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd1234");
        String expected = "1234";
        String actual = reader.consumeDigitSequence();
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches_whenTargetIsSingleCharacter_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        char target = 'a';
        boolean expected = true;
        boolean actual = reader.matches(target);
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches_whenTargetIsSequence_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        String target = "ab";
        boolean expected = true;
        boolean actual = reader.matches(target);
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesIgnoreCase_whenTargetIsSingleCharacter_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        char target = 'A';
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase(Character.toString(target));
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesIgnoreCase_whenTargetIsSequence_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        String target = "ab";
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase(target);
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesAny_whenTargetIsSingleCharacter_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        char target = 'b';
        boolean expected = true;
        boolean actual = reader.matchesAny(target);
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesAnySorted_whenTargetIsSortedArray_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        char[] target = {'b', 'd'};
        boolean expected = true;
        boolean actual = reader.matchesAnySorted(target);
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesLetter_whenCurrentCharacterIsLetter_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("abcd");
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesDigit_whenCurrentCharacterIsDigit_shouldReturnCorrectValue() {
        CharacterReader reader = new CharacterReader("1234");
        boolean expected = true;
        boolean actual = reader.matchesDigit();
        assertThat(actual, is(expected));
    }

    @Test
    public void testToString_whenNotAtEnd_shouldReturnCorrectString() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "abcd";
        String actual = reader.toString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testCacheString_whenStringLengthIsLessThanMaxCacheLen_shouldReturnCachedString() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "abcd";
        String actual = reader.cacheString(0, reader.length);
        assertThat(actual, is(expected));
    }

    @Test
    public void testCacheString_whenStringLengthIsGreaterThanMaxCacheLen_shouldReturnNewString() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        String expected = "abcdabcdabcd";
        String actual = reader.cacheString(0, reader.length);
        assertThat(actual, isInstanceOf(String.class));
        assertThat(actual, isNot(expected));
    }

    @Test
    public void testRangeEquals_whenStringsAreEqual_shouldReturnTrue() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "abcd";
        boolean actual = reader.rangeEquals(0, reader.length, expected);
        assertThat(actual, is(true));
    }

    @Test
    public void testRangeEquals_whenStringsAreNotEqual_shouldReturnFalse() {
        CharacterReader reader = new CharacterReader("abcd");
        String expected = "efgh";
        boolean actual = reader.rangeEquals(0, reader.length, expected);
        assertThat(actual, is(false));
    }
}