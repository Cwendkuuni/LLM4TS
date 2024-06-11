package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());
        assertEquals(EOF, reader.current());
        reader.consume();
        assertEquals(EOF, reader.current());
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmpty());
        reader.advance();
        assertEquals('T', reader.current());
        assertEquals(2, reader.pos());
        reader.mark();
        reader.consume();
        assertEquals('T', reader.current());
        reader.rewindToMark();
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
        reader.unconsume();
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('i', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('.', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('f', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('C', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('.', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('f', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('CharacterReader', reader.consumeLetterSequence().toLowerCase());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('.', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('f', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('i', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('f', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('C', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('.', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals(EOF, reader.current());
        assertEquals(input.length(), reader.pos());
        assertEquals(false, reader.isEmpty());
        reader.consume();
        assertEquals(EOF, reader.current());
        assertEquals(input.length() + 1, reader.pos());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals("This", reader.consumeTo(' ').trim());
        assertEquals("is a test for CharacterReader.", reader.toString());
        assertEquals("test for CharacterReader.", reader.consumeTo(' ').trim());
        assertEquals("for CharacterReader.", reader.toString());
        assertEquals("CharacterReader.", reader.consumeToEnd().trim());
        assertEquals("", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[1]))) {
            @Override
            void bufferUp() {
                throw new IOException();
            }
        };
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals("This", reader.consumeToAny(new char[]{' '}).trim());
        assertEquals("is a test for CharacterReader.", reader.toString());
        assertEquals("is a test for CharacterReader.", reader.consumeToAny(new char[]{' ','.'}).trim());
        assertEquals("test for CharacterReader.", reader.toString());
        assertEquals("CharacterReader.", reader.consumeToAnySorted(new char[]{'T','h','i','s','i','s','a'}).trim());
        assertEquals("CharacterReader.", reader.toString());
        assertEquals("", reader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals(input, reader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        String input = "<title>This is a test for CharacterReader.</title>";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals("title", reader.consumeTagName().trim());
    }

    @Test
    public void testCharacterReaderMatches() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertTrue(reader.matches(' '));
        assertTrue(reader.matches('T'));
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches('i'));
        assertTrue(reader.matches('s'));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('t'));
        assertTrue(reader.matches('.'));
        assertFalse(reader.matches((char) 0));
        assertFalse(reader.matches('z'));
        assertTrue(reader.matches("is a test for CharacterReader.".toCharArray(), 0, 9));
        assertFalse(reader.matches("is a test for CharacterReader.".toCharArray(), 10, 11));
        assertFalse(reader.matches("is a test for CharacterReader.".toCharArray(), 12, 13));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertTrue(reader.matchesIgnoreCase("is a test for CharacterReader.".toCharArray(), 0, 9));
        assertTrue(reader.matchesIgnoreCase("is a test for CharacterReader.".toCharArray(), 10, 13));
        assertFalse(reader.matchesIgnoreCase("is a test for CharacterReader.".toCharArray(), 14, 15));
        assertFalse(reader.matchesIgnoreCase("is a test for JavaScript.".toCharArray(), 0, 9));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertTrue(reader.matchesAny(' '));
        assertTrue(reader.matchesAny('T'));
        assertTrue(reader.matchesAny('h'));
        assertTrue(reader.matchesAny('i'));
        assertTrue(reader.matchesAny('s'));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('t'));
        assertTrue(reader.matchesAny('.'));
        assertFalse(reader.matchesAny('z'));
        assertTrue(reader.matchesAnySorted(new char[]{'T', 'h', 'i', 's', 'a', 't', '.'}));
        assertTrue(reader.matchesAnySorted(new char[]{'T', 'h', 'i', 's', 'a'}));
        assertTrue(reader.matchesAnySorted(new char[]{'i', 's', 'a', 't'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter('T'));
        assertTrue(reader.matchesLetter('h'));
        assertTrue(reader.matchesLetter('i'));
        assertTrue(reader.matchesLetter('s'));
        assertTrue(reader.matchesLetter('a'));
        assertTrue(reader.matchesLetter('t'));
        assertFalse(reader.matchesLetter('.'));
        assertFalse(reader.matchesLetter('z'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit('T'));
        assertFalse(reader.matchesDigit('h'));
        assertFalse(reader.matchesDigit('i'));
        assertFalse(reader.matchesDigit('s'));
        assertFalse(reader.matchesDigit('a'));
        assertFalse(reader.matchesDigit('t'));
        assertFalse(reader.matchesDigit('.'));
        assertTrue(reader.matchesDigit('0'));
        assertTrue(reader.matchesDigit('1'));
        assertTrue(reader.matchesDigit('2'));
        assertTrue(reader.matchesDigit('3'));
        assertTrue(reader.matchesDigit('4'));
        assertTrue(reader.matchesDigit('5'));
        assertTrue(reader.matchesDigit('6'));
        assertTrue(reader.matchesDigit('7'));
        assertTrue(reader.matchesDigit('8'));
        assertTrue(reader.matchesDigit('9'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        String input = "<html><head><title>This is a test for CharacterReader.</title></head><body></body></html>";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/TITLE>"));
        assertTrue(reader.containsIgnoreCase("/Title>"));
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/TITLE>"));
        assertTrue(reader.containsIgnoreCase("/Title>"));
        assertFalse(reader.containsIgnoreCase("/h1>"));
        assertFalse(reader.containsIgnoreCase("/H1>"));
        assertFalse(reader.containsIgnoreCase("/HEAD>"));
        assertFalse(reader.containsIgnoreCase("/html>"));
    }
}