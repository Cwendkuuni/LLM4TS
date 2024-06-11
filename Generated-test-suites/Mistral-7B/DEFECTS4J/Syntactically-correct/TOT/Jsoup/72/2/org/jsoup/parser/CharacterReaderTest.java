package org.jsoup.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
        assertEquals(0, reader.pos());
        assertEquals(0, reader.consume());
        assertEquals(0, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('b'));
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals(3, reader.bufLength);
        assertEquals(3, reader.bufSplitPoint);
        assertFalse(reader.isEmpty());
        assertEquals(3, reader.nextIndexOf("abc"));
        assertEquals(-1, reader.nextIndexOf("def"));
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String consumed = reader.consumeToEnd();
        assertEquals("abc", consumed);
        assertEquals(3, reader.bufLength);
        assertEquals(3, reader.bufPos);
        assertEquals(3, reader.bufMark);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(3, reader.readerPos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('c');
        assertEquals("ab", consumed);
        assertEquals(3, reader.pos());
        assertEquals(3, reader.bufPos);
        assertEquals(3, reader.bufMark);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(3, reader.readerPos);
        assertEquals('c', reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToInvalidChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertThrows(IllegalArgumentException.class, () -> reader.consumeTo('z'));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String consumed = reader.consumeTo("bc");
        assertEquals("ab", consumed);
        assertEquals(3, reader.pos());
        assertEquals(3, reader.bufPos);
        assertEquals(3, reader.bufMark);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(3, reader.readerPos);
        assertEquals('c', reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderEmptyConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        String consumed = reader.consumeTo("xyz");
        assertEquals("", consumed);
        assertEquals(

0, reader.pos());
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.bufMark);
        assertEquals(0, reader.bufSplitPoint);
        assertEquals(0, reader.readerPos);
        assertEquals(0, reader.readerLength());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToAny('a');
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos());
        assertEquals(2, reader.bufPos);
        assertEquals(2, reader.bufMark);
        assertEquals(2, reader.bufSplitPoint);
        assertEquals(2, reader.readerPos);
        assertEquals('c', reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToAnySorted(new char[]{'b', 'c'});
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos());
        assertEquals(2, reader.bufPos);
        assertEquals(2, reader.bufMark);
        assertEquals(2, reader.bufSplitPoint);
        assertEquals(2, reader.readerPos);
        assertEquals('c', reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String consumed = reader.consumeData();
        assertEquals("&<null", consumed);
        assertEquals(3, reader.pos());
        assertEquals(3, reader.bufPos);
        assertEquals(3, reader.bufMark);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(3, reader.readerPos);
        assertEquals(3, reader.readerLength());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>"));
        String consumed = reader.consumeTagName();
        assertEquals("<title>", consumed);
        assertEquals(6, reader.pos());
        assertEquals(6, reader.bufPos);
        assertEquals(6, reader.bufMark);
        assertEquals(6, reader.bufSplitPoint);
        assertEquals(6, reader.readerPos);
        assertEquals(6, reader.readerLength());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
        assertFalse(reader.matches("c"));
        assertFalse(reader.matches("c"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("C"));
        assertFalse(reader.matchesAny(new char[]{'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'b'}));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matches("&lt;"));
        assertFalse(reader.matches("&gt;"));
        assertFalse(reader.matches("&null;"));
        assertFalse(reader.matchesIgnoreCase("&lt;"));
        assertFalse(reader.matchesIgnoreCase("&gt;"));
        assertFalse(reader.matchesIgnoreCase("&null;"));
        assertFalse(reader.matchesAny(new char[]{'<', '>', '&'}));
        assertFalse(reader.matchesAnySorted(new char[]{'<', '>', '&'}));
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matches(""));
        assertFalse(reader.matches(""));
        assertFalse(reader.matchesIgnoreCase(""));
        assertFalse(reader.matchesIgnoreCase(""));
        assertFalse(reader.matchesAny(new char[]{}));
        assertFalse(reader.matchesAnySorted(new char[]{}));
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
    }

    @Test
    public void testCharacterReaderThrowsIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertThrows(UncheckedIOException.class, () -> reader.bufferUp());
    }
}