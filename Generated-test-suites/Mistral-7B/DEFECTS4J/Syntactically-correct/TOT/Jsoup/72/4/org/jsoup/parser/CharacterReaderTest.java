package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("test") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(2, reader.nextIndexOf('c'));
        Assert.assertEquals(3, reader.nextIndexOf('d'));
        Assert.assertEquals(4, reader.nextIndexOf('e'));
        Assert.assertEquals(5, reader.nextIndexOf('e'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        Assert.assertEquals("", reader.consumeTo(EOF));
        Assert.assertEquals("a", reader.consumeTo('b'));
        Assert.assertEquals("ab", reader.consumeTo('c'));
        Assert.assertEquals("abc", reader.consumeTo('d'));
        Assert.assertEquals("abcd", reader.consumeTo('e'));
        Assert.assertEquals("abcd", reader.consumeTo('e'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd123"));
        Assert.assertEquals("123", reader.consumeToAny(EOF, '1', '2', '3'));
        Assert.assertEquals("123", reader.consumeToAnySorted(new char[]{'3', '1', '2'}));
        Assert.assertEquals("1", reader.consumeToAny(EOF, '1'));
        Assert.assertEquals("", reader.consumeToAny(EOF));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abc&def"));
        Assert.assertEquals("abc", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Test</title>"));
        Assert.assertEquals("title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        Assert.assertEquals("abcde", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("def", reader.consumeLetterSequence());
        Assert.assertEquals("ghijk", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("a1b2c3d4e5"));
        Assert.assertEquals("a1", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("b2", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("c3", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("d4", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("e5", reader.consumeLetterThenDigitSequence());
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
        Assert.assertFalse(reader.matchesAny(EOF, 'e'));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }
}