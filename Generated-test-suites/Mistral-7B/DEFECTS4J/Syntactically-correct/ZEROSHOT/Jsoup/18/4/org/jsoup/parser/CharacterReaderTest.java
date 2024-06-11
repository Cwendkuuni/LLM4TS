package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyString() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.isEmpty());
        assertFalse(characterReader.matches('a'));
        assertNull(characterReader.consume());
        assertNull(characterReader.consumeLetterSequence());
        assertNull(characterReader.consumeHexSequence());
        assertNull(characterReader.consumeDigitSequence());
        assertNull(characterReader.consumeTo('a'));
        assertNull(characterReader.consumeTo("ab"));
        assertNull(characterReader.consumeToAny('a'));
        assertNull(characterReader.consumeToEnd());

        assertEquals("", characterReader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        String input = "a";
        CharacterReader characterReader = new CharacterReader(input);

        assertFalse(characterReader.isEmpty());
        assertTrue(characterReader.matches('a'));
        assertEquals('a', characterReader.consume());
        assertEquals("", characterReader.consumeLetterSequence());
        assertEquals("a", characterReader.consumeHexSequence());
        assertEquals("a", characterReader.consumeDigitSequence());
        assertEquals("a", characterReader.consumeTo('a'));
        assertEquals("a", characterReader.consumeTo("a"));
        assertEquals("", characterReader.consumeToAny('b'));
        assertEquals("", characterReader.consumeToEnd());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testCharacterReaderMultiCharacters() {
        String input = "abcdef";
        CharacterReader characterReader = new CharacterReader(input);

        assertFalse(characterReader.isEmpty());
        assertFalse(characterReader.matches('b'));
        assertFalse(characterReader.matches("xyz"));
        assertEquals('a', characterReader.consume());
        assertEquals("bcdef", characterReader.consumeLetterSequence());
        assertEquals("a", characterReader.consumeHexSequence());
        assertEquals("012345", characterReader.consumeDigitSequence());
        assertEquals("bcdef", characterReader.consumeTo('f'));
        assertEquals("abcdef", characterReader.consumeTo("def"));
        assertEquals("a", characterReader.consumeToAny('a'));
        assertEquals("cdef", characterReader.consumeToEnd());
        assertEquals("ef", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConsumeAll() {
        String input = "abcdef";
        CharacterReader characterReader = new CharacterReader(input);

        assertFalse(characterReader.isEmpty());
        assertEquals("ef", characterReader.consumeToEnd());
        assertEquals("", characterReader.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderMark() {
        String input = "abcdef";
        CharacterReader characterReader = new CharacterReader(input);

        characterReader.mark();
        characterReader.advance();
        assertEquals('b', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeSequence() {
        String input = "abcdef";
        CharacterReader characterReader = new CharacterReader(input);

        String sequence = "bc";
        assertEquals(sequence, characterReader.consumeTo(sequence));
        assertEquals("def", characterReader.toString());

        sequence = "def";
        assertEquals(sequence, characterReader.consumeTo(sequence));
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConsumeAny() {
        String input = "abcdef";
        CharacterReader characterReader = new CharacterReader(input);

        char[] sequence = new char[]{'a', 'b'};
        assertEquals(characterReader.consumeToAny(sequence), "a");
        assertEquals("bcdef", characterReader.toString());

        sequence = new char[]{'z', 'b'};
        assertEquals(characterReader.consumeToAny(sequence), "");
        assertEquals("bcdef", characterReader.toString());
    }
}