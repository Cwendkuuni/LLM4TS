package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);

        assertEquals("Hello World!", characterReader.toString());
        assertEquals(11, characterReader.pos());
        assertEquals(11, characterReader.isEmpty() ? 0 : characterReader.consume());
        assertEquals(10, characterReader.pos());

        characterReader.rewindToMark();
        assertEquals(11, characterReader.pos());
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());

        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.mark();

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals(10, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");

        assertEquals("Hello World!", characterReader.toString());
        assertEquals(11, characterReader.pos());
        assertEquals('H', characterReader.current());
        assertEquals(10, characterReader.isEmpty() ? 0 : characterReader.consume());
        assertEquals(9, characterReader.pos());

        characterReader.rewindToMark();
        assertEquals(11, characterReader.pos());
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());

        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.mark();

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals(9, characterReader.pos());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));

        characterReader.bufferUp();
        assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.bufferUp();
        assertEquals(11, characterReader.pos());
        assertEquals('H', characterReader.current());
        assertEquals(10, characterReader.isEmpty() ? 0 : characterReader.consume());
        assertEquals(9, characterReader.pos());

        characterReader.bufferUp();
        assertEquals(0, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        characterReader.advance();
        assertEquals(1, characterReader.pos() - characterReader.readerPos);

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        assertEquals(0, characterReader.pos() - characterReader.readerPos);
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertEquals('H', characterReader.current());

        characterReader.consume();
        assertEquals('e', characterReader.current());

        characterReader.bufferUp();
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        characterReader.consume();
        assertEquals('H', characterReader.current());
        assertEquals(10, characterReader.pos());

        characterReader.consume();
        assertEquals('e', characterReader.current());
        assertEquals(9, characterReader.pos());

        characterReader.bufferUp();
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        characterReader.unconsume();
        assertEquals('W', characterReader.current());
        assertEquals(11, characterReader.pos());

        characterReader.unconsume();
        assertEquals('o', characterReader.current());
        assertEquals(10, characterReader.pos());

        characterReader.bufferUp();
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        characterReader.advance();
        assertEquals('e', characterReader.current());
        assertEquals(10, characterReader.pos());

        characterReader.consume();
        assertEquals('l', characterReader.current());
        assertEquals(9, characterReader.pos());

        characterReader.bufferUp();
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });

        characterReader.bufferUp();
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertEquals(7, characterReader.nextIndexOf('l'));
        assertEquals(6, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals(11, characterReader.nextIndexOf('!'));
        assertEquals(0, characterReader.pos());

        characterReader.bufferUp();
        assertEquals(-1, characterReader.nextIndexOf('z'));
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        CharacterSequence cs = new String("Hello");
        assertEquals(3, characterReader.nextIndexOf(cs));
        assertEquals(3, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        cs = new String("World!");
        assertEquals(10, characterReader.nextIndexOf(cs));
        assertEquals(0, characterReader.pos());

        characterReader.bufferUp();
        CharacterSequence csEmpty = new String("");
        assertEquals(-1, characterReader.nextIndexOf(csEmpty));
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertEquals("Hello", characterReader.consumeTo(' '));
        assertEquals(3, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals("World!", characterReader.consumeToAny(new char[]{'!'}));
        assertEquals(12, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertEquals("Hello World!", characterReader.consumeToEnd());
        assertEquals(11, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertEquals("Hello", characterReader.consumeLetterSequence());
        assertEquals(6, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals("World!", characterReader.consumeLetterThenDigitSequence());
        assertEquals(11, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123 World_456!"));

        characterReader.bufferUp();
        assertEquals("Hello1", characterReader.consumeLetterThenDigitSequence());
        assertEquals(7, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals("World_456!", characterReader.consumeLetterThenDigitSequence());
        assertEquals(15, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello %#6x World!"));

        characterReader.bufferUp();
        assertEquals("%#6x", characterReader.consumeHexSequence());
        assertEquals(7, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals("World!", characterReader.consumeToEnd());
        assertEquals(11, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello 123 World_456!"));

        characterReader.bufferUp();
        assertEquals("123", characterReader.consumeDigitSequence());
        assertEquals(6, characterReader.pos());

        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals("World_456!", characterReader.consumeToEnd());
        assertEquals(11, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('x'));
        assertTrue(characterReader.matches("He"));
        assertFalse(characterReader.matches("Hel"));
        assertTrue(characterReader.matchesAny(new char[]{'H', 'e'}));
        assertFalse(characterReader.matchesAny(new char[]{'x', 'e'}));
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());

        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertTrue(characterReader.matches('l'));
        assertFalse(characterReader.matches('x'));
        assertTrue(characterReader.matches("World!"));
        assertFalse(characterReader.matches("Hello"));
        assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAny(new char[]{'x', 'o'}));
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());

        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertTrue(characterReader.matches(EOF));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.bufferUp();
        assertTrue(characterReader.matchesIgnoreCase("He"));
        assertFalse(characterReader.matchesIgnoreCase("Hel"));
        assertTrue(characterReader.matchesIgnoreCase("HE"));
        assertFalse(characterReader.matchesIgnoreCase("Helo"));
        assertTrue(characterReader.matchesAnyIgnoreCase(new String[]{"He", "he"}));
        assertFalse(characterReader.matchesAnyIgnoreCase(new String[]{"Hel", "Helo"}));
        assertTrue(characterReader.matchesLetterIgnoreCase());
        assertTrue(characterReader.matchesDigitIgnoreCase());

        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertTrue(characterReader.matchesIgnoreCase("World!"));
        assertTrue(characterReader.matchesIgnoreCase("world!"));
        assertTrue(characterReader.matchesIgnoreCase("WORLD!"));
        assertFalse(characterReader.matchesIgnoreCase("Hello"));
        assertTrue(characterReader.matchesAnyIgnoreCase(new String[]{"lo", "LO", "lO"}));
        assertFalse(characterReader.matchesAnyIgnoreCase(new String[]{"He", "Hel"}));
        assertFalse(characterReader.matchesLetterIgnoreCase());
        assertTrue(characterReader.matchesDigitIgnoreCase());

        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertFalse(characterReader.matchesIgnoreCase(EOF));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));

        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertTrue(characterReader.containsIgnoreCase("TITLE"));
        assertTrue(characterReader.containsIgnoreCase("TEST"));
        assertFalse(characterReader.containsIgnoreCase("/body"));
        assertFalse(characterReader.containsIgnoreCase("body"));
        assertFalse(characterReader.containsIgnoreCase("BODY"));

        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }
}