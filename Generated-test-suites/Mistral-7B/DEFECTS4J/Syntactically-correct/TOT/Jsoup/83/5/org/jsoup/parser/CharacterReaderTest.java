package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });

        reader.consume(); // consumes the throw exception
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        reader.bufferUp();
        Assert.assertEquals(4, reader.bufLength);
        Assert.assertEquals(3, reader.bufSplitPoint);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.advance();
        Assert.assertEquals('c', reader.current());
        reader.advance();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('e'));
        Assert.assertEquals(8, reader.nextIndexOf('d', 6));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertEquals(6, reader.nextIndexOf("ab"));
        Assert.assertEquals(12, reader.nextIndexOf("cd"));
        Assert.assertEquals(-1, reader.nextIndexOf("ef"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String result = reader.consumeTo('d');
        Assert.assertEquals("abc", result);
        String result2 = reader.consumeTo('e');
        Assert.assertEquals("d", result2);
        String result3 = reader.consumeTo('f');
        Assert.assertEquals("", result3);
    }

    @Test
    public void testCharacterReaderConsumeToSorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String result = reader.consumeToAnySorted('a', 'b', 'c', 'd');
        Assert.assertEquals("abcd", result);
        String result2 = reader.consumeToAnySorted('d', 'c', 'b', 'a');
        Assert.assertEquals("d", result2);
        String result3 = reader.consumeToAnySorted('e', 'f');
        Assert.assertEquals("", result3);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String result = reader.consumeData();
        Assert.assertEquals("&<", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t\n<title>\r\fHello\tWorld"));
        String result = reader.consumeTagName();
        Assert.assertEquals("<title>", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld123"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("HelloWorld", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&#x2713;"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("\u2713", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("123456", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertFalse(reader.matches('d'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcDEFg"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(reader.matchesAny('b', 'a', 'c'));
        Assert.assertTrue(reader.matchesAny('c', 'a', 'b'));
        Assert.assertTrue(reader.matchesAny('a', 'c', 'b'));
        Assert.assertFalse(reader.matchesAny('d', 'e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matchesAnySorted('a', 'b', 'c'));
        Assert.assertTrue(reader.matchesAnySorted('b', 'a', 'c'));
        Assert.assertTrue(reader.matchesAnySorted('c', 'a', 'b'));
        Assert.assertTrue(reader.matchesAnySorted('a', 'c', 'b'));
        Assert.assertFalse(reader.matchesAnySorted('d', 'e'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
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
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
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
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(1, reader.bufPos);
        Assert.assertEquals(1, reader.readerPos);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals(2, reader.bufPos);
        Assert.assertEquals(2, reader.readerPos);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(4, reader.pos());
        Assert.assertEquals(4, reader.bufPos);
        Assert.assertEquals(4, reader.readerPos);
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCD"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("a"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(1, reader.bufPos);
        Assert.assertEquals(1, reader.readerPos);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals(2, reader.bufPos);
        Assert.assertEquals(2, reader.readerPos);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(4, reader.pos());
        Assert.assertEquals(4, reader.bufPos);
        Assert.assertEquals(4, reader.readerPos);
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/head"));
        Assert.assertTrue(reader.containsIgnoreCase("body"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }
}