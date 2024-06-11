package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        reader.consume();
        assertFalse(reader.matches('A'));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeAsString();
        assertEquals("abc", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abccde");
        int index = reader.nextIndexOf('c');
        assertEquals(2, index);
        index = reader.nextIndexOf("cc");
        assertEquals(3, index);
        index = reader.nextIndexOf("zz");
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo('e');
        assertEquals("abcde", reader.toString());
        assertEquals("def", consumed);
        consumed = reader.consumeTo('f');
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String consumed = reader.consumeToAny('a', 'b');
        assertEquals("ab", consumed);
        consumed = reader.consumeToAny('1', '2');
        assertEquals("12", consumed);
        consumed = reader.consumeToAny('x', 'y');
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcdefg", consumed);
        consumed = reader.consumeLetterSequence();
        assertEquals("hijklm", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", consumed);
        consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("def45", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("def"));
        assertFalse(reader.matches("xyz"));
        assertFalse(reader.matches("123"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("def"));
        assertTrue(reader.matchesIgnoreCase("AbCd"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny('d', 'e', 'f'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(" <html><head></head><body><title>Test</title></body></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("head"));
        assertFalse(reader.containsIgnoreCase("foo"));
    }

}