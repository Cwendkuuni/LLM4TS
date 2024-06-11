package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderBasic() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('f', reader.current());
        Assert.assertEquals('f', reader.consume());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(12, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        Assert.assertEquals(12, reader.bufLength);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(12, reader.bufSplitPoint);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.bufferUp();
        Assert.assertEquals(12, reader.bufLength);
        Assert.assertEquals(4, reader.bufPos);
        Assert.assertEquals(4, reader.bufMark);
        Assert.assertEquals(7, reader.readerPos);
        Assert.assertEquals(12, reader.bufSplitPoint);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
        reader.unconsume();
        reader.rewindToMark();
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
        Assert.assertEquals(6, reader.nextIndexOf('f'));
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertEquals(5, reader.nextIndexOf('e'));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] buf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNegativeInput() {
        new CharacterReader(null);
        new CharacterReader(new StringReader(""), -1);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String result = reader.consumeTo('f');
        Assert.assertEquals("abce", result);
        String result2 = reader.consumeTo('g');
        Assert.assertEquals("", result2);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String result = reader.consumeToAny(new char[]{'a', 'e', 'i', 'o'});
        Assert.assertEquals("aeiio", result);
        String result2 = reader.consumeToAnySorted(new char[]{'z', 'y', 'x'});
        Assert.assertEquals("xyz", result2);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String result = reader.consumeData();
        Assert.assertEquals("&<", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t\n<title>\n<style>\n"));
        String result = reader.consumeTagName();
        Assert.assertEquals("title", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab%cdefghij"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("ab%cdef", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("123456", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertTrue(reader.matches("de"));
        Assert.assertFalse(reader.matches('g'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("G"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAny('a', 'e', 'f'));
        Assert.assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'e', 'f'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'g'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchConsume("de"));
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ef"));
        Assert.assertEquals(5, reader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title><meta name=\"description\"></title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/meta"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }
}