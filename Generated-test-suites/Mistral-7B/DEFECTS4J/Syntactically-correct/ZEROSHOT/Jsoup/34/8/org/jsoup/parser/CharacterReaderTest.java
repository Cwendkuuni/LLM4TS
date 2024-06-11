package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest tests the methods in the CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("b", reader.toString());

        reader.rewindToMark();
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());

        reader.mark();
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("cd", reader.toString());

        reader.rewindToMark();
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
    }

    @Test
    public void testIndexOf() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals(3, reader.pos());
        assertEquals("cd", reader.toString());

        reader.rewindToMark();
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());

        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        assertEquals('d', reader.current());
        assertEquals(-1, reader.nextIndexOf('b'));
        assertEquals("cd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeSequence() {
        CharacterReader reader = new CharacterReader("abc123ABC");
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals(3, reader.pos());
        assertEquals("123ABC", reader.toString());

        reader.mark();
        assertEquals("123", reader.consumeToDigitSequence());
        assertEquals(7, reader.pos());
        assertEquals("ABC", reader.toString());

        reader.rewindToMark();
        assertEquals("123", reader.consumeToSequence("123"));
        assertEquals(7, reader.pos());
        assertEquals("ABC", reader.toString());

        reader.mark();
        reader.advance();
        assertEquals("BC", reader.consumeToEnd());
        assertEquals(8, reader.pos());
        assertEquals("", reader.toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("abcd"));

        reader.mark();
        reader.consume();
        reader.consume();
        assertFalse(reader.matches("ab"));

        reader.rewindToMark();
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('z'));

        reader.mark();
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());

        reader.mark();
        reader.consumeDigitSequence();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("Title"));
        assertFalse(reader.containsIgnoreCase("HEAD"));
    }
}