package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        reader.consume();
        assertFalse(reader.matches('a'));
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeAsString());
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo('d');
        assertEquals("abc", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeToEnd();
        assertEquals("abcdef", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo("def");
        assertEquals("abc", consumed);
        assertEquals("def", reader.consumeAsString());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeToAny('b', 'd');
        assertEquals("a", consumed);
        assertEquals('b', reader.current());

        consumed = reader.consumeToAny('x');
        assertEquals("", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdabef");
        String consumed = reader.consumeToAnySorted('a', 'c');
        assertEquals("ab", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&amp;<tag>null");
        String consumed = reader.consumeData();
        assertEquals("&amp;<tag>null", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<tag attr=\"value\">content</tag>");
        String consumed = reader.consumeTagName();
        assertEquals("tag", consumed);
        assertEquals(' ', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals(6, reader.nextIndexOf('f'));
        assertEquals(-1, reader.nextIndexOf('g'));
        reader.advance();
        assertEquals(7, reader.nextIndexOf('f'));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSeq() {
        CharacterReader reader = new CharacterReader("abcabcdef");
        String seq = "abc";
        assertEquals(0, reader.nextIndexOf(seq));
        assertEquals(6, reader.nextIndexOf(seq, 6));
        assertEquals(-1, reader.nextIndexOf(seq, 8));
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&heta;&#x3B4;&#x3B5;");
        String consumed = reader.consumeHexSequence();
        assertEquals("&#x3B4;&#x3B5;", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123456", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("b"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAnySorted('e'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        String consumed = reader.consumeTo('d');
        assertEquals("abc", consumed);
        assertEquals("def", reader.toString());
        reader.rewindToMark();
        assertEquals("abc", reader.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderConsumePastEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testCharacterReaderCacheString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo("def");
        assertEquals("abc", consumed);
        String cached = reader.cacheString(0, consumed.length());
        assertEquals("abc", cached);
        reader.consumeToEnd();
        String end = reader.cacheString(0, reader.length());
        assertEquals("abcdef", end);
    }

    @Test
    public void testCharacterReaderRangeEquals() {
        CharacterReader reader = new CharacterReader("abcdef");
        String cached = reader.cacheString(0, 3);
        assertTrue(reader.rangeEquals(0, 3, cached));
    }
}