package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        assertEquals(0, reader.pos());
        reader.consume();
        assertFalse(reader.matches('A'));
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.current());
        reader.consume();
        assertEquals('B', reader.current());
        reader.consume();
        assertEquals('C', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABC");
        String consumed = reader.consumeAsString();
        assertEquals("AB", consumed);
        reader.consume();
        consumed = reader.consumeAsString();
        assertEquals("C", consumed);
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("ABABA");
        int index = reader.nextIndexOf('A');
        assertEquals(1, index);
        index = reader.nextIndexOf('B');
        assertEquals(3, index);
        index = reader.nextIndexOf('Z');
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("ABABA");
        String seq = "AB";
        int index = reader.nextIndexOf(seq);
        assertEquals(1, index);
        seq = "BA";
        index = reader.nextIndexOf(seq);
        assertEquals(4, index);
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader reader = new CharacterReader("ABABA");
        String consumed = reader.consumeTo('A');
        assertEquals("A", consumed);
        consumed = reader.consumeTo('B');
        assertEquals("AB", consumed);
        consumed = reader.consumeTo('Z');
        assertEquals("ABABA", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToCharSequence() {
        CharacterReader reader = new CharacterReader("ABABA");
        String consumed = reader.consumeTo("AB");
        assertEquals("AB", consumed);
        consumed = reader.consumeTo("BA");
        assertEquals("ABA", consumed);
        consumed = reader.consumeTo("Z");
        assertEquals("ABABA", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("123ABC456");
        String consumed = reader.consumeToAny('1', '2');
        assertEquals("12", consumed);
        consumed = reader.consumeToAny('C');
        assertEquals("C", consumed);
        consumed = reader.consumeToAny('Z');
        assertEquals("123ABC456", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABC");
        String consumed = reader.consumeToEnd();
        assertEquals("ABC", consumed);
        reader.consumeLetterSequence();
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcd", reader.toString());
        assertEquals("abc", consumed);
        reader.consumeLetterSequence();
        consumed = reader.consumeLetterSequence();
        assertEquals("d", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", reader.toString());
        assertEquals("abc", consumed);
        consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("123", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABCDEFabcdef");
        String consumed = reader.consumeHexSequence();
        assertEquals("ABCDEFabcdef", reader.toString());
        assertEquals("ABCDEF", consumed);
        reader.consumeHexSequence();
        consumed = reader.consumeHexSequence();
        assertEquals("abcdef", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        String consumed = reader.consumeDigitSequence();
        assertEquals("12345", reader.toString());
        assertEquals("12345", consumed);
        reader.consumeDigitSequence();
        consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matches('A'));
        assertFalse(reader.matches('B'));
        reader.consume();
        assertFalse(reader.matches('A'));
        assertTrue(reader.matches('B'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("B"));
        reader.consumeLetterSequence();
        assertTrue(reader.matchesIgnoreCase("c"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesAny('A', 'B', 'C'));
        assertFalse(reader.matchesAny('D'));
        reader.consume();
        assertFalse(reader.matchesAny('A'));
        assertTrue(reader.matchesAny('B', 'C'));
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        reader.consume();
        assertEquals(0, reader.pos());
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }
}