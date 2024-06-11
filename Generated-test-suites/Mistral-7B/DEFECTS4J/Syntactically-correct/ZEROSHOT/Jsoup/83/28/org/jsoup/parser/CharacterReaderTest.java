package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        // Test empty constructor
        CharacterReader characterReader = new CharacterReader();
        assertNotNull(characterReader);

        // Test reader constructor
        StringReader stringReader = new StringReader("test string");
        CharacterReader characterReaderFromReader = new CharacterReader(stringReader);
        assertNotNull(characterReaderFromReader);
        assertEquals("test string", characterReaderFromReader.toString());

        // Test string constructor
        CharacterReader characterReaderFromString = new CharacterReader("test string");
        assertNotNull(characterReaderFromString);
        assertEquals("test string", characterReaderFromString.toString());

        // Test reader and size constructor
        CharacterReader characterReaderFromReaderAndSize = new CharacterReader(stringReader, 10);
        assertNotNull(characterReaderFromReaderAndSize);
        assertEquals(10, characterReaderFromReaderAndSize.charBuf.length);
        assertEquals("test string", characterReaderFromReaderAndSize.toString());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader();
        assertTrue(characterReader.isEmpty());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertFalse(characterReaderWithData.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals(0, characterReader.pos());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals(0, characterReaderWithData.pos());

        characterReaderWithData.advance();
        assertEquals(1, characterReaderWithData.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals(CharacterReader.EOF, characterReader.consume());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals('t', characterReaderWithData.consume());
        assertEquals('e', characterReaderWithData.consume());
        assertEquals(CharacterReader.EOF, characterReaderWithData.consume());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals(CharacterReader.EOF, characterReader.current());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals('t', characterReaderWithData.current());
        characterReaderWithData.advance();
        assertEquals('e', characterReaderWithData.current());
        characterReaderWithData.advance();
        assertEquals(CharacterReader.EOF, characterReaderWithData.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(CharacterReader.EOF, characterReader.current());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        characterReaderWithData.consume();
        characterReaderWithData.consume();
        characterReaderWithData.unconsume();
        assertEquals('t', characterReaderWithData.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader();
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.current());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        characterReaderWithData.advance();
        assertEquals('e', characterReaderWithData.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader characterReader = new CharacterReader();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(CharacterReader.EOF, characterReader.current());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        characterReaderWithData.mark();
        characterReaderWithData.advance();
        characterReaderWithData.advance();
        characterReaderWithData.rewindToMark();
        assertEquals('t', characterReaderWithData.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals(-1, characterReader.nextIndexOf('a'));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals(3, characterReaderWithData.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals(-1, characterReader.nextIndexOf("test"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals(4, characterReaderWithData.nextIndexOf("data"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeTo('a'));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("est ", characterReaderWithData.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeTo("test"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("test data", characterReaderWithData.consumeTo("data"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeToAny('a'));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("est", characterReaderWithData.consumeToAny(' '));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeToAnySorted('a'));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("est", characterReaderWithData.consumeToAnySorted(' '));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeData());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("&amp;", characterReaderWithData.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeTagName());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("<html><head></head></html>"));
        assertEquals("<html>", characterReaderWithData.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeToEnd());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("test data", characterReaderWithData.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeLetterSequence());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertEquals("test", characterReaderWithData.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader();
        assertEquals("", characterReader.consumeLetterThenDigitSequence());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test123 data"));
        assertEquals("test123", characterReaderWithData.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matches('a'));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matches('t'));
    }

    @Test
    public void testMatchesWithSequence() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matches("test"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matches("data"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchesIgnoreCase("Test"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchesIgnoreCase("data"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchesAny('a'));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchesAny('t'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchesAnySorted(new char[]{'a'}));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchesAnySorted(new char[]{'t'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchesLetter());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchesDigit());

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchConsume("Test"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchConsume("data"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.matchConsumeIgnoreCase("Test"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("test data"));
        assertTrue(characterReaderWithData.matchConsumeIgnoreCase("data"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader();
        assertFalse(characterReader.containsIgnoreCase("/html"));

        CharacterReader characterReaderWithData = new CharacterReader(new StringReader("<html><head></head></html>"));
        assertTrue(characterReaderWithData.containsIgnoreCase("/html"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test data") {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
        characterReader.consume();
    }
}