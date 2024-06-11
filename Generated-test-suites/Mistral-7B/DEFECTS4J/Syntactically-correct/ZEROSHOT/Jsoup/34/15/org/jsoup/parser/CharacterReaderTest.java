package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
        assertEquals("", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals('b', reader.current());
        assertEquals(1, reader.pos());
        reader.consume();
        assertEquals('c', reader.current());
        assertEquals(2, reader.pos());
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals('c', reader.current());
        assertEquals(3, reader.pos());
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCharacterReaderMarkUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleChar() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        int index = reader.nextIndexOf('e');
        assertEquals(5, index);
        index = reader.nextIndexOf('a');
        assertEquals(0, index);
        index = reader.nextIndexOf('z');
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        int index = reader.nextIndexOf("def");
        assertEquals(6, index);
        index = reader.nextIndexOf("xyz");
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderConsumeToSingleChar() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals("cdef", reader.consumeTo('e'));
        assertEquals('f', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals("def", reader.consumeTo("def"));
        assertEquals('f', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("123", reader.consumeToAny('1', '2', '3'));
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals("abcdefabcdef", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderMatchesSingleChar() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'
));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matches("def"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchesIgnoreCase("Def"));
        assertTrue(reader.matchesIgnoreCase("def"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertTrue(reader.matchesAny('1', '2', '3'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchConsume("def"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchConsumeIgnoreCase("DEF"));
        assertTrue(reader.matchConsumeIgnoreCase("def"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.containsIgnoreCase("def"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }
}