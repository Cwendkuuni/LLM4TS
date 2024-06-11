package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("", reader.consumeTo('B'));
        assertEquals("A", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeTo('b'));
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMarkPosition() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(4, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('x'));
        reader.consume();
        reader.consume();
        assertEquals(5, reader.nextIndexOf('c'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertEquals(5, reader.nextIndexOf("def"));
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        assertEquals(8, reader.nextIndexOf("def"));
    }

    @Test
    public void testCharacterReaderConsumeSequence() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        String sequence = reader.consumeLetterSequence();
        assertEquals("abc", sequence);
        sequence = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", sequence);
        sequence = reader.consumeHexSequence();
        assertEquals("3ef", sequence);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("cd"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('b', 'c'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertTrue(reader.containsIgnoreCase("def"));
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

}