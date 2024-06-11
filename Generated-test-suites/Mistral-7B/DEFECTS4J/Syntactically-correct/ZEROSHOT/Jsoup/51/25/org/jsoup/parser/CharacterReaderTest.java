package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals("a", reader.toString());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('a', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('c', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('c', reader.current());
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('d', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeAsString());
        assertEquals("ab", reader.consumeAsString());
        assertEquals("cd", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(3, reader.nextIndexOf("cd"));
        assertEquals(3, reader.nextIndexOf("DC"));
        assertEquals(3, reader.nextIndexOf("DC", true));
        assertEquals(3, reader.nextIndexOf("dc", true));
        assertEquals(3, reader.nextIndexOf("dc", false));
        assertEquals(-1, reader.nextIndexOf("ab"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeTo('\0'));
        assertEquals("a", reader.consumeTo('a'));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("d", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('e'));
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeToEnd());
        assertEquals("abcd", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeToAny());
        assertEquals("a", reader.consumeToAny(Character.MIN_VALUE));
        assertEquals("ab", reader.consumeToAny(Arrays.asList('a', 'b')));
        assertEquals("bc", reader.consumeToAny(Arrays.asList('b', 'c')));
        assertEquals("d", reader.consumeToAny(Character.MAX_VALUE));
        assertEquals("d", reader.consumeToAny(Arrays.asList('d')));
        assertEquals("", reader.consumeToAny(Arrays.asList('e')));
        assertEquals("", reader.consumeToAny(Arrays.asList('z')));
        assertEquals("", reader.consumeToAny(Arrays.asList('z', 'z')));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeToAnySorted(Character.MIN_VALUE));
        assertEquals("a", reader.consumeToAnySorted(Arrays.asList('a')));
        assertEquals("ab", reader.consumeToAnySorted(Arrays.asList('a', 'b')));
        assertEquals("bc", reader.consumeToAnySorted(Arrays.asList('b', 'c')));
        assertEquals("d", reader.consumeToAnySorted(Character.MAX_VALUE));
        assertEquals("d", reader.consumeToAnySorted(Arrays.asList('d')));
        assertEquals("", reader.consumeToAnySorted(Arrays.asList('e')));
        assertEquals("", reader.consumeToAnySorted(Arrays.asList('z')));
        assertEquals("", reader.consumeToAnySorted(Arrays.asList('z', 'z')));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&<&");
        assertEquals("", reader.consumeData());
        assertEquals("&", reader.consumeData());
        assertEquals("&", reader.consumeData());
        assertEquals("<", reader.consumeData());
        assertEquals("", reader.consumeData());
        assertEquals("", reader.consumeData());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>");
        assertEquals("", reader.consumeTagName());
        assertEquals("title", reader.consumeTagName());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("abc", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertEquals("a", reader.consumeLetterThenDigitSequence());
        assertEquals("ab", reader.consumeLetterThenDigitSequence());
        assertEquals("abc", reader.consumeLetterThenDigitSequence());
        assertEquals("abc1", reader.consumeLetterThenDigitSequence());
        assertEquals("abc12", reader.consumeLetterThenDigitSequence());
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&#x1234;");
        assertEquals("", reader.consumeHexSequence());
        assertEquals("\u1234", reader.consumeHexSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("1", reader.consumeDigitSequence());
        assertEquals("12", reader.consumeDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("cd"));
        assertFalse(reader.matches("ef"));
        assertTrue(reader.matchesAny(Character.MIN_VALUE));
        assertTrue(reader.matchesAny(Character.MAX_VALUE));
        assertTrue(reader.matchesAny(Arrays.asList('a')));
        assertTrue(reader.matchesAny(Arrays.asList('b')));
        assertTrue(reader.matchesAny(Arrays.asList('c')));
        assertTrue(reader.matchesAny(Arrays.asList('d')));
        assertFalse(reader.matchesAny(Arrays.asList('e')));
        assertFalse(reader.matchesAny(Arrays.asList('z')));
        assertFalse(reader.matchesAny(Arrays.asList('z', 'z')));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("CD"));
        assertFalse(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("Cd"));
        assertFalse(reader.matchesIgnoreCase("Ef"));
        assertTrue(reader.matchesIgnoreCase(""));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.toString());
        assertTrue(reader.isEmpty());
    }
}