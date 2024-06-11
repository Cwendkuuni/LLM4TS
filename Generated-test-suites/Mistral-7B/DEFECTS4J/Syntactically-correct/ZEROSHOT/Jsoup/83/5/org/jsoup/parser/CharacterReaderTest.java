package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test file for CharacterReader class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test CharacterReader constructor with reader
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());

        // Test CharacterReader constructor with string
        CharacterReader stringReader = new CharacterReader("Hello World");
        assertEquals("Hello World", stringReader.toString());

        // Test initial state
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        // Test read character
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals('H', reader.consume());
        assertEquals(' ', reader.current());
        assertFalse(reader.isEmpty());
        assertEquals(2, reader.pos());

        // Test read multiple characters
        StringBuilder str = new StringBuilder();
        while (!reader.isEmpty()) {
            str.append(reader.consume());
        }
        assertEquals("llo Worl", str.toString());

        // Test mark and rewind
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());

        // Test skipping characters
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());

        // Test scanning for a character
        reader.mark();
        reader.consumeTo('l');
        reader.rewindToMark();
        assertEquals(3, reader.pos());

        // Test scanning for a sequence of characters
        reader.mark();
        reader.consumeTo("llo");
        reader.rewindToMark();
        assertEquals(7, reader.pos());

        // Test scanning for any character
        reader.mark();
        String result = reader.consumeToAny(new char[]{'l'});
        reader.rewindToMark();
        assertEquals("llo Worl", result);

        // Test scanning for any character sorted
        reader.mark();
        result = reader.consumeToAnySorted(new char[]{'l'});
        reader.rewindToMark();
        assertEquals("llo Worl", result);

        // Test consuming data
        reader.mark();
        result = reader.consumeData();
        reader.rewindToMark();
        assertEquals("llo Worl", result);

        // Test consuming tag name
        reader.mark();
        result = reader.consumeTagName();
        reader.rewindToMark();
        assertEquals("llo", result);

        // Test consuming to end
        reader.mark();
        result = reader.consumeToEnd();
        reader.rewindToMark();
        assertEquals("llo Worl", result);

        // Test consuming letter sequence
        reader.mark();
        result = reader.consumeLetterSequence();
        reader.rewindToMark();
        assertEquals("llo", result);

        // Test consuming letter then digit sequence
        reader.mark();
        result = reader.consumeLetterThenDigitSequence();
        reader.rewindToMark();
        assertEquals("llo", result);

        // Test consuming hex sequence
        reader.mark();
        result = reader.consumeHexSequence();
        reader.rewindToMark();
        assertEquals("llo", result);

        // Test consuming digit sequence
        reader.mark();
        result = reader.consumeDigitSequence();
        reader.rewindToMark();
        assertEquals("llo", result);

        // Test matching character
        reader.mark();
        assertTrue(reader.matches('l'));
        reader.rewindToMark();

        // Test matching sequence of characters
        reader.mark();
        assertTrue(reader.matches("llo"));
        reader.rewindToMark();

        // Test matching any character
        reader.mark();
        assertTrue(reader.matchesAny(new char[]{'l'}));
        reader.rewindToMark();

        // Test matching any character sorted
        reader.mark();
        assertTrue(reader.matchesAnySorted(new char[]{'l'}));
        reader.rewindToMark();

        // Test matching letter
        reader.mark();
        assertTrue(reader.matchesLetter());
        reader.rewindToMark();

        // Test matching digit
        reader.mark();
        assertTrue(reader.matchesDigit());
        reader.rewindToMark();

        // Test matching sequence and consuming it
        reader.mark();
        assertTrue(reader.matchConsume("llo"));
        reader.rewindToMark();

        // Test matching sequence and consuming it ignoring case
        reader.mark();
        assertTrue(reader.matchConsumeIgnoreCase("llo"));
        reader.rewindToMark();

        // Test containing sequence ignoring case
        reader.mark();
        assertTrue(reader.containsIgnoreCase("/title"));
        reader.rewindToMark();

        // Test empty reader
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        assertTrue(emptyReader.isEmpty());
        assertEquals("", emptyReader.toString());

        // Test exception handling
        try {
            new CharacterReader(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new CharacterReader(new StringReader(""));
            reader.consume();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            reader.consume(10);
            fail();
        } catch (UncheckedIOException e) {
            // expected
        }
    }
}