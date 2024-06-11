package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * CharacterReaderTest.
 */
public class CharacterReaderTest {

    private CharacterReader characterReader;
    private static final String TEST_STRING = "This is a test string for CharacterReader.";

    @Before
    public void setUp() {
        characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(TEST_STRING.getBytes())));
    }

    @After
    public void tearDown() {
        if (characterReader != null) {
            try {
                characterReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCharacterReaderConstructorWithReader() {
        characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(TEST_STRING.getBytes())));
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        characterReader = new CharacterReader(TEST_STRING);
        assertNotNull(characterReader);
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(0, characterReader.nextIndexOf('T'));
        assertEquals(4, characterReader.nextIndexOf('g'));
        assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        String subString = "is a test";
        assertEquals(7, characterReader.nextIndexOf(subString));
    }

    @Test
    public void testConsumeTo() {
        String expected = "is a test string for CharacterReader.";
        assertEquals(expected, characterReader.consumeTo(' '));
        assertEquals("Reader.", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Reader.", characterReader.consumeToAny('.'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Reader.", characterReader.consumeToAnySorted('.'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("&, <, null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals(TEST_STRING, characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("is a test string for CharacterReader.", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("is a test string for CharacterReader.", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("for CharacterReader.", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("Reader.", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("is a"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        assertTrue(characterReader.matchesIgnoreCase("is a"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('s'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('.'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        assertEquals("is a test string for CharacterReader.", characterReader.consumeLetterSequence());
        characterReader.matchConsume("is a test");
        assertEquals("string for CharacterReader.", characterReader.consumeLetterSequence());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        characterReader.consumeLetterSequence();
        characterReader.matchConsumeIgnoreCase("is a test");
        assertEquals("string for CharacterReader.", characterReader.consumeLetterSequence());
    }

    @Test
    public void testContainsIgnoreCaseString() {
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/STYLE"));
    }

    @Test
    public void testToString() {
        assertEquals(TEST_STRING, characterReader.toString());
    }
}