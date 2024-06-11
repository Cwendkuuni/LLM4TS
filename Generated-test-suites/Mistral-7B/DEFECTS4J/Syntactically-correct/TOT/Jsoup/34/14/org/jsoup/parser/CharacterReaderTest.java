package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("", reader.consumeTo("ab"));
        assertEquals("", reader.consumeToEnd());
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeDigitSequence());
        assertEquals(0, reader.pos());
        assertEquals(0, reader.mark());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf("a"));
        assertEquals("a", reader.consumeAsString());
        assertEquals("a", reader.consumeTo('a').trim());
        assertEquals("a", reader.consumeTo("a").trim());
        assertEquals("a", reader.consumeToAny('a').trim());
        assertEquals("a", reader.consumeToEnd().trim());
        assertEquals("a", reader.consumeLetterSequence().trim());
        assertEquals("a", reader.consumeLetterThenDigitSequence().trim());
        assertEquals("a", reader.consumeHexSequence().trim());
        assertEquals("a", reader.consumeDigitSequence().trim());
        assertEquals(1, reader.pos());
        assertEquals(0, reader.mark());
    }

    @Test
    public void testMultipleCharactersReader() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(2, reader.nextIndexOf('b'));
        assertEquals(3, reader.nextIndexOf('c'));
        assertEquals("a", reader.consumeAsString());
        assertEquals("a", reader.consumeTo('a').trim());
        assertEquals("a", reader.consumeTo("a").trim());
        assertEquals("a", reader.consumeToAny('a').trim());
        assertEquals("ab", reader.consumeTo('b').trim());
        assertEquals("ab", reader.consumeTo("ab").trim());
        assertEquals("abc", reader.consumeToEnd().trim());
        assertEquals("a", reader.consumeLetterSequence().trim());
        assertEquals("ab", reader.consumeLetterThenDigitSequence().trim());
        assertEquals("ac", reader.consumeHexSequence().trim());
        assertEquals("bc", reader.consumeDigitSequence().trim());
        assertEquals(3, reader.pos());
        assertEquals(0, reader.mark());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
}