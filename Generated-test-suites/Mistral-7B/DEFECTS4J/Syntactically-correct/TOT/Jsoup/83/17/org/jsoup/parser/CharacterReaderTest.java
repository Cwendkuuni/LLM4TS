package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.consume());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals('C', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new IOException("Test IOException"));
    }

    @Test
    public void testCharacterReaderWithReaderMarkSupported() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderWithReaderReset() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark(5);
        reader.consume(5);
        reader.reset();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        CharacterReader reader = new CharacterReader(new StringReader("AAAABC"));
        Assert.assertEquals(1, reader.nextIndexOf('A'));
        Assert.assertEquals(5, reader.nextIndexOf('C'));
        Assert.assertEquals(-1, reader.nextIndexOf('D'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("AAAABCABC"));
        Assert.assertEquals(1, reader.nextIndexOf("AA"));
        Assert.assertEquals(5, reader.nextIndexOf("BC"));
        Assert.assertEquals(7, reader.nextIndexOf("ABC"));
        Assert.assertEquals(-1, reader.nextIndexOf("DEF"));
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeTo('C');
        Assert.assertEquals("AB", result);
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeTo("BC");
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnyChar() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeToAny(',');
        Assert.assertEquals("", result);

        reader = new CharacterReader(new StringReader("ABC,DEF"));
        result = reader.consumeToAny(',');
        Assert.assertEquals("A,B,C", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySortedChar() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeToAnySorted(new char[]{'A', 'B', 'C'});
        Assert.assertEquals("", result);

        reader = new CharacterReader(new StringReader("ABC,DEF"));
        result = reader.consumeToAnySorted(new char[]{'A', 'B', 'C', 'D'});
        Assert.assertEquals("A,B,C", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;"));
        String result = reader.consumeData();
        Assert.assertEquals("&lt;", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("html", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("hello"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("hello", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("hello123"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("hello123", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&#x61;"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("a", result);

        reader = new CharacterReader(new StringReader("&#x41;&#x42;"));
        result = reader.consumeHexSequence();
        Assert.assertEquals("AB", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("123", result);
    }

    @Test
    public void testCharacterReaderMatchesChar() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches('A'));
        Assert.assertTrue(reader.matches('B'));
        Assert.assertFalse(reader.matches('D'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches("AB"));
        Assert.assertTrue(reader.matches("BC"));
        Assert.assertFalse(reader.matches("DEF"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("BC"));
        Assert.assertFalse(reader.matchesIgnoreCase("DEF"));
    }

    @Test
    public void testCharacterReaderMatchesAnyChar() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAny('A', 'B', 'C'));
        Assert.assertFalse(reader.matchesAny('D'));
    }

    @Test
    public void testCharacterReaderMatchesAnySortedChar() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'A', 'B', 'C'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'D'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchConsume("AB"));
        Assert.assertTrue(reader.matchConsume("BC"));
        Assert.assertFalse(reader.matchConsume("DEF"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("BC"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("DEF"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>My Title</title></head></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("<title>"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertFalse(reader.containsIgnoreCase("<body>"));
    }
}