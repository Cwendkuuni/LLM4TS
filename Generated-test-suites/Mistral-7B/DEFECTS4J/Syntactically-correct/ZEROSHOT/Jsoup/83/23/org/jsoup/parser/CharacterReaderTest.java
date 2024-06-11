package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with valid input
        Reader reader = new StringReader("test");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("test", characterReader.toString());

        // Test constructor with maxBufferLen
        reader = new StringReader("test");
        characterReader = new CharacterReader(reader, 5);
        assertEquals("test", characterReader.toString());

        // Test constructor with String input
        characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.toString());

        // Test bufferUp
        characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        assertEquals(0, characterReader.pos());
        assertEquals(4, characterReader.bufLength);
        assertEquals('t', characterReader.current());
        assertEquals('t', characterReader.consume());
        assertEquals(3, characterReader.pos());

        // Test pos
        assertEquals(3, characterReader.pos());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(3, characterReader.pos());
        characterReader.advance();
        assertEquals(4, characterReader.pos());

        // Test isEmpty
        assertEquals(false, characterReader.isEmpty());
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        assertEquals(true, characterReader.isEmpty());

        // Test consume
        characterReader = new CharacterReader(new StringReader("test"));
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals(' ', characterReader.consume());
        assertEquals(EOF, characterReader.consume());

        // Test unconsume
        characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());

        // Test nextIndexOf
        characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(4, characterReader.nextIndexOf(' '));
        assertEquals(-1, characterReader.nextIndexOf('z'));
        characterReader.consume();
        assertEquals(3, characterReader.nextIndexOf(' '));

        // Test nextIndexOf with CharSequence
        characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(0, characterReader.nextIndexOf("te"));
        assertEquals(4, characterReader.nextIndexOf("st"));
        assertEquals(-1, characterReader.nextIndexOf("zz"));

        // Test consumeTo
        characterReader = new CharacterReader(new StringReader("12345"));
        assertEquals("123", characterReader.consumeTo('3'));
        assertEquals("45", characterReader.consumeTo('5'));
        assertEquals("", characterReader.consumeTo('6'));

        // Test consumeTo with CharSequence
        characterReader = new CharacterReader(new StringReader("12345"));
        assertEquals("123", characterReader.consumeTo("3"));
        assertEquals("45", characterReader.consumeTo("5"));
        assertEquals("", characterReader.consumeTo("6"));

        // Test consumeToAny
        characterReader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", characterReader.consumeToAny(new char[]{ '1', '2', '3', '4', '5'}));
        assertEquals("", characterReader.consumeToAny(new char[]{ '6'}));

        // Test consumeToAnySorted
        characterReader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", characterReader.consumeToAnySorted(new char[]{ '5', '4', '3', '2', '1'}));
        assertEquals("", characterReader.consumeToAnySorted(new char[]{ '6'}));

        // Test consumeData
        characterReader = new CharacterReader(new StringReader("&lt;abc&gt;"));
        assertEquals("&lt;abc&gt;", characterReader.consumeData());

        // Test consumeTagName
        characterReader = new CharacterReader(new StringReader("&lt;html&gt;"));
        assertEquals("<html>", characterReader.consumeTagName());

        // Test consumeToEnd
        characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.consumeToEnd());

        // Test consumeLetterSequence
        characterReader = new CharacterReader(new StringReader("abc123"));
        assertEquals("abc", characterReader.consumeLetterSequence());

        // Test consumeLetterThenDigitSequence
        characterReader = new CharacterReader(new StringReader("abc123"));
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());

        // Test consumeHexSequence
        characterReader = new CharacterReader(new StringReader("&x27;"));
        assertEquals("\u0027", characterReader.consumeHexSequence());

        // Test consumeDigitSequence
        characterReader = new CharacterReader(new StringReader("123"));
        assertEquals("123", characterReader.consumeDigitSequence());

        // Test matches
        characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matches('t'));
        assertTrue(characterReader.matches("es"));
        assertFalse(characterReader.matches('z'));

        // Test matchesIgnoreCase
        characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesIgnoreCase("es"));
        assertTrue(characterReader.matchesIgnoreCase("Test"));
        assertFalse(characterReader.matchesIgnoreCase("Z"));

        // Test matchesAny
        characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAny(new char[]{ 't', 'e'}));
        assertFalse(characterReader.matchesAny(new char[]{ 'z'}));

        // Test matchesAnySorted
        characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAnySorted(new char[]{ 't', 'e'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{ 'z'}));

        // Test matchesLetter
        characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());

        // Test matchesDigit
        characterReader = new CharacterReader(new StringReader("test"));
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
    }
}