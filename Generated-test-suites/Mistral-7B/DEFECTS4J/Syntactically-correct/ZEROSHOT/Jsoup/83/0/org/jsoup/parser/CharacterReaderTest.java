package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderWithData() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('h', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.advance();
        // Consume to 'l'
        String consumed = reader.consumeTo('l');
        Assert.assertEquals("hell", consumed);
        Assert.assertEquals('w', reader.current());
        reader.consume(); // consume 'w'
        reader.consume(); // consume 'r'
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals('d', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("hello") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String data = reader.consumeToEnd();
        Assert.assertEquals("hello world", data);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("abc", sequence);
        Assert.assertEquals('d', reader.current());

        sequence = reader.consumeLetterSequence();
        Assert.assertEquals("defg", sequence);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        String sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", sequence);
        Assert.assertEquals('d', reader.current());

        sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("def456", sequence);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x1a2b3c"));
        String sequence = reader.consumeHexSequence();
        Assert.assertEquals("1a2b3c", sequence);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String sequence = reader.consumeDigitSequence();
        Assert.assertEquals("123456", sequence);
        Assert.assertEquals('7', reader.current());

        sequence = reader.consumeDigitSequence();
        Assert.assertEquals("890", sequence);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        reader.mark();
        reader.consumeTo('l');
        reader.rewindToMark();
        Assert.assertEquals('h', reader.current());
        Assert.assertEquals('h', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        int index = reader.nextIndexOf('e');
        Assert.assertEquals(3, index);

        index = reader.nextIndexOf('z');
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        int index = reader.nextIndexOf("llo");
        Assert.assertEquals(2, index);

        index = reader.nextIndexOf("zzz");
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String data = reader.consumeTo('l');
        Assert.assertEquals("hell", data);
        Assert.assertEquals('w', reader.current());

        data = reader.consumeTo("llo");
        Assert.assertEquals("hellow", data);
        Assert.assertEquals(' ', reader.current());

        data = reader.consumeTo('d');
        Assert.assertEquals("hello world", data);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        String data = reader.consumeToAny(',');
        Assert.assertEquals("hello", data);
        Assert.assertEquals('w', reader.current());

        data = reader.consumeToAny('o');
        Assert.assertEquals("hellow", data);
        Assert.assertEquals(' ', reader.current());

        data = reader.consumeToAnySorted(new char[]{'w', 'o'});
        Assert.assertEquals("hellow", data);
        Assert.assertEquals(' ', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<& & < &"));
        String data = reader.consumeData();
        Assert.assertEquals("&<& & ", data);
        Assert.assertEquals('&', reader.current());

        data = reader.consumeData();
        Assert.assertEquals("", data);
        Assert.assertEquals('<', reader.current());

        data = reader.consumeData();
        Assert.assertEquals("", data);
        Assert.assertEquals('&', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title></title>"));
        String tagName = reader.consumeTagName();
        Assert.assertEquals("title", tagName);
        Assert.assertEquals('/', reader.current());

        tagName = reader.consumeTagName();
        Assert.assertEquals("", tagName);
        Assert.assertEquals('>', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertTrue(reader.matches('h'));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase("hello"));
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertFalse(reader.matchesIgnoreCase("ZZZ"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertTrue(reader.matchesAny('h', 'w', 'l'));
        Assert.assertTrue(reader.matchesAny('h', 'e', 'l'));
        Assert.assertFalse(reader.matchesAny('z', 'w', 'l'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'h'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'h', 'l'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z', 'w'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("hello world"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter()); // consume all letters
        Assert.assertFalse(reader.matchesLetter()); // consume non-letter
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit()); // consume all digits
        Assert.assertFalse(reader.matchesDigit()); // consume non-digit
    }
}