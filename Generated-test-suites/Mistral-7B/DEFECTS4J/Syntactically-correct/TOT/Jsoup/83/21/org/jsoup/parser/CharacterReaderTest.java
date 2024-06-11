package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.unconsume();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.bufferUp(); // simulate IOException
        Assert.assertEquals(EOF, reader.current());
        reader.consume(); // should throw UncheckedIOException
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String result = reader.consumeTo('c');
        Assert.assertEquals("ab", result);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abcd", result);
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String result = reader.consumeToAny(new char[]{'a', 'e', 'i'});
        Assert.assertEquals("aei", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String result = reader.consumeToAnySorted(new char[]{'z', 'y', 'x'});
        Assert.assertEquals("xyz", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;abcdef&amp;ghijklmnopqrstuvwxyz"));
        String result = reader.consumeData();
        Assert.assertEquals("&lt;abcdef", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("html", result);
        result = reader.consumeTagName();
        Assert.assertEquals("head", result);
        result = reader.consumeTagName();
        Assert.assertEquals("title", result);
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(-1, reader.nextIndexOf('/'));
        Assert.assertEquals(3, reader.nextIndexOf('d'));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcd"));
        Assert.assertEquals(0, reader.nextIndexOf("abcd"));
        Assert.assertEquals(6, reader.nextIndexOf("abcd", 4));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("cd"));
        Assert.assertFalse(reader.matches("ef"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCD"));
        Assert.assertTrue(reader.matchesIgnoreCase("CD"));
        Assert.assertFalse(reader.matchesIgnoreCase("EF"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd', 'e', 'f'));
        Assert.assertFalse(reader.matchesAny('g', 'h'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'e', 'f'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'g', 'h'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('a'));
        Assert.assertTrue(reader.matchesLetter('z'));
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('0'));
        Assert.assertTrue(reader.matchesDigit('9'));
        Assert.assertFalse(reader.matchesDigit('a'));
    }
}