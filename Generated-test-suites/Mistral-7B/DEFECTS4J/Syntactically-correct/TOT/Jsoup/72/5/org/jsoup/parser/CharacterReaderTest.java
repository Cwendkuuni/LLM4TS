package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertEquals("hello world", reader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        reader.bufferUp();
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertEquals('h', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals('o', reader.consume());
        Assert.assertEquals(' ', reader.consume());
        Assert.assertEquals('w', reader.consume());
        Assert.assertEquals('r', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        reader.advance();
        Assert.assertEquals('h', reader.current());
        reader.advance();
        reader.advance();
        Assert.assertEquals('l', reader.current());
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertEquals('o', reader.current());
        reader.advance();
        Assert.assertEquals(' ', reader.current());
        reader.advance();
        Assert.assertEquals('w', reader.current());
        reader.advance();
        reader.advance();
        Assert.assertEquals('r', reader.current());
        reader.advance();
        Assert.assertEquals('d', reader.current());
        reader.advance();
        Assert.assertEquals(CharacterReader.EOF, reader.current());
        reader.advance();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        reader.mark();
        Assert.assertEquals('h', reader.current());
        reader.advance();
        reader.advance();
        Assert.assertEquals('l', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('h', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"), 2);
        reader.bufferUp();
        reader.reader.close();
        reader.bufferUp();
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String expected = "world";
        String result = reader.consumeTo(expected.charAt(0));
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String expected = "llo";
        String result = reader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String expected = "lorl";
        String result = reader.consumeToAnySorted(new char[]{'l', 'o', 'r'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;&lt;&gt;null"));
        String expected = "&amp;&lt;&gt;null";
        String result = reader.consumeData();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("   <title>Something</title>\n"));
        String expected = "title";
        String result = reader.consumeTagName();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String expected = "hello world";
        String result = reader.consumeToEnd();
        Assert.assertEquals(expected, result
        );
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdABCd123"));
        String expected = "abcd";
        String result = reader.consumeLetterSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab1cd2ef123"));
        String expected = "ab1cd2";
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd\u00E5ef123"));
        String expected = "abcd\u00E5";
        String result = reader.consumeHexSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String expected = "1234567890";
        String result = reader.consumeDigitSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertFalse(reader.matchesAny('e', 'f', 'g', 'h'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'d', 'c', 'b', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234"));
        Assert.assertFalse(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String expected = "cd";
        reader.matchConsume("cd");
        Assert.assertEquals(expected, reader.toString());
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String expected = "cd";
        reader.matchConsumeIgnoreCase("CD");
        Assert.assertEquals(expected, reader.toString());
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Something</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
        Assert.assertTrue(reader.containsIgnoreCase("Title"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String expected = "abcd";
        Assert.assertEquals(expected, reader.toString());
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }
}