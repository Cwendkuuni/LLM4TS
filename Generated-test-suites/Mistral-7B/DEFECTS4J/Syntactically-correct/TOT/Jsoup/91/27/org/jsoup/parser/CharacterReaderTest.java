package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp(); // throw IOException
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertEquals(2, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeTo('c'));
        Assert.assertEquals("d", reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeToAny(new char[]{'b', 'c'}));
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeToAnySorted(new char[]{'c', 'b'}));
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        Assert.assertEquals("&<null", reader.consumeData());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>"));
        Assert.assertEquals("<title>", reader.consumeTagName());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("abcd", reader.consumeToEnd());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals("bcdefghijklmnopqrstuvwxyz", reader.consumeLetterSequence());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        Assert.assertEquals("bc123def456", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;#x0a;"));
        Assert.assertEquals("&#x0a;", reader.consumeHexSequence());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertEquals("234567890", reader.consumeDigitSequence());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("aBc"));
        Assert.assertTrue(reader.matchesIgnoreCase("AbC"));
        Assert.assertTrue(reader.matchesIgnoreCase("AbCd"));
        Assert.assertFalse(reader.matchesIgnoreCase("ABCD"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'b', 'c', 'a'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x', 'y', 'z'}));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'b', 'c', 'a'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z', 'y', 'x'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abCdEf"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("aBc"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("AbC"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("abcd"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<title><meta charset=\"UTF-8\"></title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/meta"));
        Assert.assertTrue(reader.containsIgnoreCase("charset"));
        Assert.assertFalse(reader.containsIgnoreCase("/head"));
    }
}