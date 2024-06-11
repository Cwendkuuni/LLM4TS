package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(-1, characterReader.nextIndexOf('a'));
        Assert.assertEquals(-1, characterReader.nextIndexOf("ab"));
        Assert.assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.nextIndexOf('a'));
        Assert.assertEquals("a", characterReader.consumeTo('a').trim());
        Assert.assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ab"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.nextIndexOf('a'));
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        Assert.assertEquals("ab", characterReader.consumeToEnd());
        Assert.assertEquals("a", characterReader.consumeTo('a').trim());
        Assert.assertEquals("b", characterReader.consumeTo('b').trim());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader characterReader = new CharacterReader(new IOException("Test IOException").getReader(), maxBufferLen);
        characterReader.bufferUp();
    }

    @Test
    public void testCharacterReaderStringInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.nextIndexOf('a'));
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        Assert.assertEquals(2, characterReader.nextIndexOf('c'));
        Assert.assertEquals("abcdefg", characterReader.consumeToEnd());
        Assert.assertEquals("abc", characterReader.consumeTo("abc").trim());
    }

    @Test
    public void testCharacterReaderConsumeToDelimiter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.nextIndexOf('a'));
        Assert.assertEquals(3, characterReader.nextIndexOf('c'));
        Assert.assertEquals("abcd", characterReader.consumeTo('c').trim());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.nextIndexOf('a'));
        Assert.assertEquals(10, characterReader.nextIndexOf(characterReader.consumeToEnd()));
        Assert.assertEquals("abcdabcdabcd", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals("bcdabcdabcd", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("bd", characterReader.consumeToAny(new char[]{'a', 'b'}));
        Assert.assertEquals("cdabcdabcd", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("bc", characterReader.consumeToAnySorted(new char[]{'b', 'c'}));
        Assert.assertEquals("abcdabcdabcd", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;hello&amp;world"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("<helloworld", characterReader.consumeData().trim());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("title", characterReader.consumeTagName().trim());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("abcdefg", characterReader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x61;&#x62;&#x63;"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("abc", characterReader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("1234567890", characterReader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(characterReader.matches('a'));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(characterReader.matches("abc"));
        Assert.assertFalse(characterReader.matches("xyz"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("abc"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'b', 'c'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('z'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(characterReader.matchConsume("abc"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("abc"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/BODY"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/head"));
    }
}