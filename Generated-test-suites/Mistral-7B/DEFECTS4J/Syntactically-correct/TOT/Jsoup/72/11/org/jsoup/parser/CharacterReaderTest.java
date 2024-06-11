package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class contains test cases for the CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(-1, characterReader.nextIndexOf('a'));
        Assert.assertEquals(-1, characterReader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReaderSimpleInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals('h', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.unconsume();
        Assert.assertEquals('h', characterReader.current());
        Assert.assertEquals(1, characterReader.nextIndexOf('l'));
        Assert.assertEquals(1, characterReader.nextIndexOf("ll"));
        Assert.assertEquals("hello", characterReader.consumeTo(' '));
        Assert.assertEquals("world", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderScanToSpecificChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123abcd123abcd"));
        Assert.assertEquals("123", characterReader.consumeTo('a'));
        Assert.assertEquals("abcd", characterReader.consumeToAny(new char[]{'a', 'b'}));
        Assert.assertEquals("123abcd123", characterReader.consumeToEnd());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        characterReader.bufferUp();
        throw characterReader.reader.toString(); // force IOException
    }

    @Test
    public void testCharacterReaderScanToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals("abcdef", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderScanToLetterThenDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def"));
        Assert.assertEquals("abc", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderScanToLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals("abcdefghijk", characterReader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderScanToHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0123456789ABCDEF"));
        Assert.assertEquals("012345", characterReader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderScanToDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890123456"));
        Assert.assertEquals("123456", characterReader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderScanToDelimiter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcde<fghij>k"));
        Assert.assertEquals("abcde", characterReader.consumeTo('<'));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcabc"));
        Assert.assertTrue(characterReader.matches('a'));
        Assert.assertTrue(characterReader.matches("ab"));
        Assert.assertTrue(characterReader.matches("ab", 1, 2));
        Assert.assertFalse(characterReader.matches('z'));
        Assert.assertFalse(characterReader.matches("cd"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABCabc"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ab"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertTrue(characterReader.matchesAny('1', '3', '5', '7', '9'));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'1', '3', '5', '7', '9'}));
        Assert.assertFalse(characterReader.matchesAny('0'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghij"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter('a'));
        Assert.assertTrue(characterReader.matchesLetter('z'));
        Assert.assertFalse(characterReader.matchesLetter('1'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit('1'));
        Assert.assertTrue(characterReader.matchesDigit('9'));
        Assert.assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ababab"));
        Assert.assertTrue(characterReader.matchConsume("ab"));
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abABab"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/HEAD"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/BODY"));
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertEquals("html", characterReader.consumeTagName());
        Assert.assertEquals("head", characterReader.consumeTagName());
        Assert.assertEquals("title", characterReader.consumeTagName());
        Assert.assertEquals("body", characterReader.consumeTagName());
        Assert.assertTrue(characterReader.isEmpty());
    }
}