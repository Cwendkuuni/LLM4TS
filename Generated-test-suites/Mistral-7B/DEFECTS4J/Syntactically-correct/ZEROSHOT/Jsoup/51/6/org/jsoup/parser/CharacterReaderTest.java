package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(1, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(0, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals(3, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals(0, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("AB", reader.consumeAsString());
        assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABABC");
        assertEquals(1, reader.nextIndexOf('A'));
        assertEquals(4, reader.nextIndexOf('B'));
        assertEquals(-1, reader.nextIndexOf('D'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCABC");
        assertEquals("AB", reader.consumeTo('C').toString());
        assertEquals("BC", reader.consumeTo('C').toString());
        assertEquals("", reader.consumeTo('D').toString());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("C", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("123456ABCDEF");
        assertEquals("123", reader.consumeToAny('1', '2', '3').toString());
        assertEquals("ABC", reader.consumeToAny('A', 'B', 'C').toString());
        assertEquals("DEF", reader.consumeToAny('D', 'E', 'F').toString());
        assertEquals("", reader.consumeToAny('G', 'H').toString());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("123456ABCDEF");
        assertEquals("123", reader.consumeToAnySorted(new char[]{'1', '2', '3'}).toString());
        assertEquals("ABC", reader.consumeToAnySorted(new char[]{'A', 'B', 'C'}).toString());
        assertEquals("DEF", reader.consumeToAnySorted(new char[]{'D', 'E', 'F'}).toString());
        assertEquals("", reader.consumeToAnySorted(new char[]{'G', 'H'}).toString());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&<", reader.consumeData().toString());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(" <tag>");
        assertEquals("", reader.consumeTagName().toString());
        reader.advance();
        assertEquals("<tag>", reader.consumeTagName().toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertFalse(reader.matches('B'));
        assertFalse(reader.matches("B"));
        assertFalse(reader.matches('C'));
        assertFalse(reader.matches("C"));
        assertFalse(reader.matches(EOF));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase('B'));
        assertFalse(reader.matchesIgnoreCase("B"));
        assertFalse(reader.matchesIgnoreCase('C'));
        assertFalse(reader.matchesIgnoreCase("C"));
        assertFalse(reader.matchesIgnoreCase(EOF));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesAny('A'));
        assertTrue(reader.matchesAny('B'));
        assertTrue(reader.matchesAny('C'));
        assertFalse(reader.matchesAny('D'));
        assertFalse(reader.matchesAny(EOF));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesAnySorted('A'));
        assertTrue(reader.matchesAnySorted('B'));
        assertTrue(reader.matchesAnySorted('C'));
        assertFalse(reader.matchesAnySorted('D'));
        assertFalse(reader.matchesAnySorted(EOF));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsume("A"));
        assertTrue(reader.matchConsume("A"));
        assertFalse(reader.matchConsume("B"));
        assertFalse(reader.matchConsume("B"));
        assertFalse(reader.matchConsume("C"));
        assertFalse(reader.matchConsume("C"));
        assertFalse(reader.matchConsume(EOF));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsumeIgnoreCase("A"));
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertFalse(reader.matchConsumeIgnoreCase("B"));
        assertFalse(reader.matchConsumeIgnoreCase("B"));
        assertFalse(reader.matchConsumeIgnoreCase("C"));
        assertFalse(reader.matchConsumeIgnoreCase("C"));
        assertFalse(reader.matchConsumeIgnoreCase(EOF));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(" <title>Hello World</title> ");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("hello"));
        assertTrue(reader.containsIgnoreCase("world"));
        assertFalse(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}