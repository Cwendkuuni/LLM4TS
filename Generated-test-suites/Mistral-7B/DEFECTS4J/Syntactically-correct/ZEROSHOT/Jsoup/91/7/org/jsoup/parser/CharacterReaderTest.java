package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals(4, reader.nextIndexOf('E'));
        assertEquals(-1, reader.nextIndexOf('X'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals(4, reader.nextIndexOf("DE"));
        assertEquals(-1, reader.nextIndexOf("AB"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals("DE", reader.consumeTo('E'));
        assertEquals("FG", reader.consumeTo('G'));
        assertEquals("", reader.consumeTo('X'));
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals("DE", reader.consumeTo("DE"));
        assertEquals("FG", reader.consumeTo("FG"));
        assertEquals("", reader.consumeTo("XY"));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals("DE", reader.consumeToAny(new char[]{'D', 'E'}));
        assertEquals("FG", reader.consumeToAny(new char[]{'F', 'G'}));
        assertEquals("", reader.consumeToAny(new char[]{'X', 'Y'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals("DE", reader.consumeToAnySorted(new char[]{'D', 'E'}));
        assertEquals("FG", reader.consumeToAnySorted(new char[]{'F', 'G'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'X', 'Y'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        assertEquals("<tag", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals("ABCDEFG", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC123DEFG"));
        assertEquals("ABC", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC123DEFG"));
        assertEquals("ABC123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC#x12#DEFG"));
        assertEquals("12", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC123DEFG"));
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches('B'));
        assertFalse(reader.matches('C'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("c"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesAny('A'));
        assertTrue(reader.matchesAny('B'));
        assertTrue(reader.matchesAny('C'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesAnySorted('A'));
        assertTrue(reader.matchesAnySorted('B'));
        assertTrue(reader.matchesAnySorted('C'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC123"));
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertEquals("BC", reader.matchConsume("BC"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertEquals("BC", reader.matchConsumeIgnoreCase("BC"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Test</title>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("/style"));
    }
}