package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String testString = "This is a test string for CharacterReader";
        characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testString.getBytes())));
    }

    @After
    public void tearDown() {
        characterReader = null;
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(characterReader.isEmpty());
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() throws IOException {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('h', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        InputStreamReader invalidReader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        CharacterReader invalidCharacterReader = new CharacterReader(invalidReader, 1);
        invalidCharacterReader.consume();
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        assertEquals('h', characterReader.current());
        characterReader.consume();
        assertEquals('i', characterReader.current());
        characterReader.unconsume();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        characterReader.advance();
        assertEquals('s', characterReader.current());
    }

    @Test
    public void testMarkAndRewind() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(0, characterReader.nextIndexOf('T'));
        characterReader.consume();
        assertEquals(1, characterReader.nextIndexOf('T'));
        assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(11, characterReader.nextIndexOf("is a"));
        characterReader.consume();
        characterReader.consume();
        assertEquals(12, characterReader.nextIndexOf("is a"));
        assertEquals(-1, characterReader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeTo() {
        String result = characterReader.consumeTo('s');
        assertEquals("This is a ", result);
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeTo(' ');
        assertEquals("This is a test ", result);
    }

    @Test
    public void testConsumeToSequence() {
        String result = characterReader.consumeTo("is a");
        assertEquals("This is ", result);
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeTo("is a");
        assertEquals("This is a test", result);
    }

    @Test
    public void testConsumeToAny() {
        String result = characterReader.consumeToAny(',', ';');
        assertEquals("This is a test string for CharacterReader", result);
        characterReader.consume();
        result = characterReader.consumeToAny(',', ';');
        assertEquals("This is a test string for CharacterReader", result);
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeToAny(',', ';');
        assertEquals("This is a test string for CharacterReader", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = characterReader.consumeToAnySorted(';', ',');
        assertEquals("This is a test string for CharacterReader", result);
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeToAnySorted(';', ',');
        assertEquals("This is a test string for CharacterReader", result);
        characterReader.consume();
        result = characterReader.consumeToAnySorted(';', ',');
        assertEquals("This is a test string for CharacterReader", result);
    }

    @Test
    public void testConsumeData() {
        String result = characterReader.consumeData();
        assertEquals("This is a test string for CharacterReader", result);
    }

    @Test
    public void testConsumeTagName() {
        String result = characterReader.consumeTagName();
        assertEquals("This", result);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeTagName();
        assertEquals("is", result);
    }

    @Test
    public void testConsumeToEnd() {
        String result = characterReader.consumeToEnd();
        assertEquals("This is a test string for CharacterReader", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = characterReader.consumeLetterSequence();
        assertEquals("This", result);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeLetterSequence();
        assertEquals("is a test", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("This", result);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("is a test string for CharacterReader", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String result = characterReader.consumeHexSequence();
        assertEquals("This", result);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeHexSequence();
        assertEquals("is a test string for CharacterReader", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String result = characterReader.consumeDigitSequence();
        assertEquals("0", result);
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeDigitSequence();
        assertEquals("1", result);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        result = characterReader.consumeDigitSequence();
        assertEquals("15", result);
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        characterReader.consume();
        assertTrue(characterReader.matches('h'));
        assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(characterReader.matches("is a"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        assertTrue(characterReader.matches("is a test"));
        assertFalse(characterReader.matches("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('h', 't'));
        assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('t', 'h'));
        assertFalse(characterReader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSequence() {
        characterReader.matchConsume("is a");
        assertEquals(6, characterReader.pos());
        assertFalse(characterReader.matches("is a"));
    }

    @Test
    public void testMatchConsumeSequenceIgnoreCase() {
        characterReader.matchConsumeIgnoreCase("is a");
        assertEquals(6, characterReader.pos());
        assertTrue(characterReader.matchesIgnoreCase("is a"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/STYLE"));
        assertFalse(characterReader.containsIgnoreCase("/script"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string for CharacterReader", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        characterReader.cacheString(charBuf, stringCache, 0, 6);
        assertEquals("is a", characterReader.stringCache[0]);
        characterReader.cacheString(charBuf, stringCache, 6, 11);
        assertEquals("is a test", characterReader.stringCache[1]);
    }

}