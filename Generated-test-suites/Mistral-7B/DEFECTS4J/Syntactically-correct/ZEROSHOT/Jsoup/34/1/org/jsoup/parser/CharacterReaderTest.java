package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

/**
 * CharacterReaderTest class to test all methods in CharacterReader class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeAsString());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.current());
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("def", reader.consumeToEnd());
        assertEquals("ab", reader.consumeToAny('a', 'b'));
    }

    @Test
    public void testCharacterReaderComplex() {
        CharacterReader reader = new CharacterReader("abc<def>123<xyz>456");
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeAsString());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.unconsume();
        assertEquals('b', reader.current());
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.current());
        assertEquals(3, reader.nextIndexOf("<def>"));
        assertEquals(-1, reader.nextIndexOf("xyz1"));
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("def123xyz456", reader.consumeToEnd());
        assertEquals("abc<", reader.consumeToAny('a', '<'));
        assertEquals("23", reader.consumeDigitSequence());
        assertEquals("def<xyz", reader.consumeLetterThenDigitSequence());
        assertEquals("xyz", reader.consumeLetterSequence());
        assertEquals("def", reader.consumeHexSequence());
        assertEquals("def", reader.consumeLetterIgnoreCase("Def"));

        // test matches methods
        reader = new CharacterReader("123");
        assertTrue(reader.matches('1'));
        assertFalse(reader.matches('2'));
        assertFalse(reader.matches('3'));
        assertFalse(reader.matches('4'));
        reader.mark();
        reader.consume();
        assertFalse(reader.matches('1'));
        assertTrue(reader.matches('2'));
        reader.rewindToMark();

        reader = new CharacterReader("abcd123ABCD");
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("cd"));
        assertTrue(reader.matches("12"));
        assertTrue(reader.matches("3"));
        assertTrue(reader.matches("AB"));
        assertTrue(reader.matches("CD"));
        assertFalse(reader.matchesIgnoreCase("XY"));
        assertFalse(reader.matches("XYZ"));
        assertFalse(reader.matchesAny('b', 'c'));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchConsume("xy"));
        assertFalse(reader.matchConsumeIgnoreCase("XY"));
        assertTrue(reader.containsIgnoreCase("ab"));
    }
}