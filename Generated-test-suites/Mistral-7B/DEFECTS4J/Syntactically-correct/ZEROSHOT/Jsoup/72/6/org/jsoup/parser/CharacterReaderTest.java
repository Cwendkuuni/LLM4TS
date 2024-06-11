package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_ReaderInput() throws IOException {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        assertEquals("Hello World!", reader.toString());
        assertEquals(11, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
        reader.mark();
        reader.rewindToMark();
        assertEquals('H', reader.current());
        assertEquals(10, reader.nextIndexOf('l'));
        reader.consumeTo('l');
        assertEquals("ll", reader.toString());
        assertEquals(6, reader.pos());

        reader.consumeToEnd();
        assertEquals("", reader.toString());
        assertEquals(11, reader.pos());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testCharacterReader_StringInput() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("Hello World!", reader.toString());
        reader = new CharacterReader("Hello World!");
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testCharacterReader_ReaderInput_Exception() throws IOException {
        StringReader reader = new StringReader("");
        try {
            new CharacterReader(reader, 1);
            fail("Expected UncheckedIOException");
        } catch (UncheckedIOException e) {
            assertEquals("", e.getCause().getMessage());
        }
    }

    @Test
    public void testStringCache() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefabcdefabcdef"));
        String str1 = reader.consumeTo("abcdef");
        String str2 = reader.consumeTo("abcdef");
        assertEquals(str1, str2);

        String str3 = reader.consumeTo("xyz");
        assertEquals(3, reader.pos());
        String str4 = reader.consumeTo("xyz");
        assertEquals(str3, str4);
    }

    @Test
    public void testCharacterReader_ReaderInput_ConsumeLetterSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String str = reader.consumeLetterSequence();
        assertEquals(26, str.length());
        assertEquals("z", reader.current());
    }

    @Test
    public void testCharacterReader_ReaderInput_ConsumeLetterThenDigitSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef1234567890"));
        String str = reader.consumeLetterThenDigitSequence();
        assertEquals(10, str.length());
        assertEquals('9', reader.current());
    }

    @Test
    public void testCharacterReader_ReaderInput_ConsumeHexSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789ABCDEFabcdef"));
        String str = reader.consumeHexSequence();
        assertEquals(6, str.length());
        assertEquals('F', reader.current());
    }

    @Test
    public void testCharacterReader_ReaderInput_ConsumeDigitSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String str = reader.consumeDigitSequence();
        assertEquals(10, str.length());
        assertEquals('0', reader.current());
    }

    @Test
    public void testCharacterReader_ReaderInput_Matches() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("He"));
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matches("hto"));
        assertFalse(reader.matches("World"));
        assertFalse(reader.matches("!"));
    }

    @Test
    public void testCharacterReader_ReaderInput_MatchConsume() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String str = reader.matchConsume("He");
        assertEquals("llo World!", reader.toString());
        assertEquals("He", str);
    }

    @Test
    public void testCharacterReader_ReaderInput_MatchConsumeIgnoreCase() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String str = reader.matchConsumeIgnoreCase("hello");
        assertEquals("llo World!", reader.toString());
        assertEquals("hello", str);
    }

    @Test
    public void testCharacterReader_ReaderInput_ContainsIgnoreCase() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HEAD"));
        assertTrue(reader.containsIgnoreCase("html"));
    }

    @Test
    public void testCharacterReader_ReaderInput_Empty() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.toString());
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());
    }
}