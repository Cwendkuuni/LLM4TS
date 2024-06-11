package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader("ABCDE");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeString() {
        CharacterReader reader = new CharacterReader("ABCDE");
        String consumed = reader.consumeAsString();
        assertEquals("AB", consumed);
        assertEquals(3, reader.pos());
        consumed = reader.consumeTo('D');
        assertEquals("CD", consumed);
        assertEquals(5, reader.pos());
        consumed = reader.consumeToEnd();
        assertEquals("E", consumed);
        assertEquals(6, reader.pos());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcABC123DEFG");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
        assertEquals("ABC123DEFG", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1A 0x2B 0x3C 0x4D 0x5E 0x6F 0x7G");
        String consumed = reader.consumeHexSequence();
        assertEquals("1A2B", consumed);
        assertEquals("0x3C 0x4D 0x5E 0x6F 0x7G", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345 123456 1234567 1234567890");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals("45 123456 7 1234567890", reader.toString());
    }

    @Test
    public void testCharacterReaderMatchSingleCharacter() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matches('A'));
        assertFalse(reader.matches('B'));
        assertFalse(reader.matches('C'));
        assertFalse(reader.matches('D'));
    }

    @Test
    public void testCharacterReaderMatchMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matches("AB"));
        assertFalse(reader.matches("AC"));
        assertFalse(reader.matches("BC"));
    }

    @Test
    public void testCharacterReaderMatchIgnoreCase() {
        CharacterReader reader = new CharacterReader("aBc");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("bc"));
        assertFalse(reader.matchesIgnoreCase("cd"));
    }

    @Test
    public void testCharacterReaderMatchAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d', 'e', 'f'));
        assertFalse(reader.matchesAny('g', 'h', 'i'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsume("AB"));
        assertEquals(2, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbC");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertEquals(2, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFGH");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("Ab"));
        assertTrue(reader.containsIgnoreCase("efg"));
        assertTrue(reader.containsIgnoreCase("EfG"));
        assertFalse(reader.containsIgnoreCase("HI"));
    }
}