package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertNotNull(characterReader);
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader("Hello World!");
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        // consume all characters
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        characterReader.mark();
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("H", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals(4, characterReader.nextIndexOf('l'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals(7, characterReader.nextIndexOf("llo"));
        assertEquals(-1, characterReader.nextIndexOf("zzz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("ello", characterReader.consumeTo('l'));
        characterReader.consume();
        assertEquals("elloW", characterReader.consumeTo('W'));
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("ello", characterReader.consumeTo("llo"));
        characterReader.consume();
        assertEquals("elloW", characterReader.consumeTo("W"));
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("12345HelloWorld!");
        assertEquals("123", characterReader.consumeToAny(new char[]{'1', '2'}));
        assertEquals("5", characterReader.consumeToAny(new char[]{'5'}));
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("World!", characterReader.consumeToEnd());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("Hello", characterReader.consumeLetterSequence());
        characterReader.consumeLetterSequence();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello12345World!");
        assertEquals("Hello12345", characterReader.consumeLetterThenDigitSequence());
        characterReader.consumeLetterThenDigitSequence();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World!&#x61;&#x6C;&#x65;");
        assertEquals("Hello World!aabc", characterReader.consumeHexSequence());
        characterReader.consumeHexSequence();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("12345HelloWorld!");
        assertEquals("12345", characterReader.consumeDigitSequence());
        characterReader.consumeDigitSequence();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertTrue(characterReader.matches('l'));
        assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertTrue(characterReader.matches("llo"));
        assertFalse(characterReader.matches("zzz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertTrue(characterReader.matchesIgnoreCase("llo"));
        assertFalse(characterReader.matchesIgnoreCase("ZZZ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("12345HelloWorld!");
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("llo", characterReader.consumeTo("llo"));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("llo", characterReader.consumeToIgnoreCase("llo"));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<html><head><title>Test</title></head><body></body></html>");
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertTrue(characterReader.containsIgnoreCase("HEAD"));
        assertFalse(characterReader.containsIgnoreCase("zzz"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        assertEquals("World!", characterReader.toString());
    }
}