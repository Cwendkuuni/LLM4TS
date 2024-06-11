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
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals('C', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        });
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        reader.bufferUp();
        Assert.assertEquals(13, reader.bufLength);
        Assert.assertEquals(13, reader.bufSplitPoint);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
        reader.consume();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
        reader.consume();
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('B', reader.current());
        reader.advance();
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('C', reader.current());
        reader.advance();
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('B', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));
        Assert.assertEquals(1, reader.nextIndexOf('B'));
        Assert.assertEquals(2, reader.nextIndexOf('C'));
        Assert.assertEquals(3, reader.nextIndexOf('C'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABABABC"));
        Assert.assertEquals(-1, reader.nextIndexOf("XYZ"));
        Assert.assertEquals(0, reader.nextIndexOf("AB"));
        Assert.assertEquals(3, reader.nextIndexOf("AB"));
    }

    @Test
    public void testCharacterReaderConsumeToSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals("A", reader.consumeTo('B'));
        Assert.assertEquals("BC", reader.consumeTo('C'));
        Assert.assertEquals("", reader.consumeTo('Z'));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABABC"));
        Assert.assertEquals("AB", reader.consumeTo("AB"));
        Assert.assertEquals("ABC", reader.consumeTo(""));
    }

    @Test
    public void testCharacterReaderConsumeToAnySingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals("ABC", reader.consumeToAny('A'));
        Assert.assertEquals("C", reader.consumeToAny('C'));
        Assert.assertEquals("", reader.consumeToAny('Z'));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals("ABC", reader.consumeToAnySorted('C', 'A'));
        Assert.assertEquals("C", reader.consumeToAnySorted('C'));
        Assert.assertEquals("", reader.consumeToAnySorted('Z'));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        Assert.assertEquals("&", reader.consumeData());
        Assert.assertEquals("&<", reader.consumeData());
        Assert.assertEquals("", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>"));
        Assert.assertEquals("<title>", reader.consumeTagName());
        Assert.assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals("ABC", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AB123CD456EF789"));
        Assert.assertEquals("AB123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("CD456EF789", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderMatchesSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches('A'));
        Assert.assertFalse(reader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches("AB"));
        Assert.assertFalse(reader.matches("XYZ"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testCharacterReaderMatchesAnySingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAny('A'));
        Assert.assertTrue(reader.matchesAny('C'));
        Assert.assertFalse(reader.matchesAny('Z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAnySorted('A', 'C'));
        Assert.assertTrue(reader.matchesAnySorted('C'));
        Assert.assertFalse(reader.matchesAnySorted('Z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit('A'));
    }

    @Test
    public void testCharacterReaderMatchConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals("AB", reader.matchConsume("AB"));
        Assert.assertEquals("C", reader.matchConsume("C"));
        Assert.assertEquals("", reader.matchConsume("XYZ"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals("ab", reader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals("C", reader.matchConsumeIgnoreCase("C"));
        Assert.assertEquals("", reader.matchConsumeIgnoreCase("XYZ"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("XYZ"));
    }
}