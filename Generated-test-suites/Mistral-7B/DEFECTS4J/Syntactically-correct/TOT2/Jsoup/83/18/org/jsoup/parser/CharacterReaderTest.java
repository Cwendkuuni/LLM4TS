package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderconstruction() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals('o', reader.consume());
        Assert.assertEquals(' ', reader.consume());
        Assert.assertEquals('W', reader.consume());
        Assert.assertEquals('o', reader.consume());
        Assert.assertEquals('r', reader.consume());
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello")){
            @Override
            public void bufferUp() throws IOException {
                throw new IOException("Simulated error");
            }
        };
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, reader.nextIndexOf(' '));
        Assert.assertEquals(13, reader.nextIndexOf('r'));
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String str = reader.consumeTo(' ');
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(' ', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String str = reader.consumeToEnd();
        Assert.assertEquals("Hello World", str);
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld123abcxyz"));
        String str = reader.consumeLetterSequence();
        Assert.assertEquals("HelloWorld", str);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld123abcxyz"));
        String str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("HelloWorld123", str);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x1A 0x2B 0x3C"));
        String str = reader.consumeHexSequence();
        Assert.assertEquals("1A2B3C", str);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String str = reader.consumeDigitSequence();
        Assert.assertEquals("1234567890", str);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("l"));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("L"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertFalse(reader.matchesAny('x', 'o'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        char[] chars = {'l', 'o', 'x'};
        Assert.assertTrue(reader.matchesAnySorted(chars));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'o', 'l'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('5'));
    }
}