package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.*;

/**
 * CharacterReaderTest class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(input.length(), reader.length);
        assertEquals(0, reader.pos);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_Constructor_NullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        assertEquals('H', reader.current());
        reader.consume();
        reader.consume();
        assertEquals('l', reader.current());
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("He", reader.consumeAsString());
        assertEquals("ld", reader.consumeAsString());
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("ll", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo('d'));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("ll", reader.consumeToAny('l'));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("123abcXYZ");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123456789ABCDEF");
        assertEquals("ABCDEF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("0", reader.consumeDigitSequence());
        assertEquals("9", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches('o'));
        assertTrue(reader.matches(' '));
        assertTrue(reader.matches('W'));
        assertTrue(reader.matches('r'));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('A'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertTrue(reader.matchesIgnoreCase("world"));
        assertFalse(reader.matchesIgnoreCase("Hello"));
        assertFalse(reader.matchesIgnoreCase("WORLD"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny('l'));
        assertTrue(reader.matchesAny('o'));
        assertTrue(reader.matchesAny('H'));
        assertTrue(reader.matchesAny('W'));
        assertFalse(reader.matchesAny('A'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsume("ll"));
        assertEquals("ld", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello <title>World</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("world"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.toString());
    }
}