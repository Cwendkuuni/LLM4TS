package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(-1, reader.nextIndexOf(EOF));
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(0, reader.nextIndexOf(EOF));
        Assert.assertEquals("A", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals(0, reader.nextIndexOf(EOF));
        Assert.assertEquals("Hello World", reader.consumeToEnd());

        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(10, reader.pos());
        Assert.assertEquals('H', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        throw new RuntimeException("Test IO exception");
    }

    @Test
    public void testCharacterReaderBufferSizes() {
        CharacterReader reader1 = new CharacterReader(new StringReader(""), 100);
        CharacterReader reader2 = new CharacterReader(new StringReader(""), 1000);
        CharacterReader reader3 = new CharacterReader(new StringReader(""));

        Assert.assertNotSame(reader1.charBuf, reader2.charBuf);
        Assert.assertNotSame(reader1.charBuf, reader3.charBuf);
        Assert.assertNotSame(reader2.charBuf, reader3.charBuf);
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        reader.bufferUp();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderBufferUpWithData() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGh"));
        reader.bufferUp();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());

        reader.consume();
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.bufferUp();

        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(1, reader.nextIndexOf('A'));
        Assert.assertEquals("A", reader.consumeTo('A'));
        Assert.assertEquals(0, reader.nextIndexOf(EOF));
        Assert.assertEquals("Ab", reader.consumeToEnd());

        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGh"));
        String expected = "AbC";
        String consumed = reader.consumeTo('C');
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals(3, reader.nextIndexOf('C'));
        Assert.assertEquals("dEfGh", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToSorted() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGh"));
        String expected = "AbCd";
        String consumed = reader.consumeToAnySorted('A', 'C', 'd');
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals("EfGh", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGh"));
        String expected = "AbCdE";
        String consumed = reader.consumeToAny(new char[]{'A', 'B', 'C', 'D', 'E'});
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals('E', reader.current());
        Assert.assertEquals(5, reader.nextIndexOf('E'));
        Assert.assertEquals("fGh", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Test</title>"));
        String expected = "title";
        String tagName = reader.consumeTagName();
        Assert.assertEquals(expected, tagName);
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals('/', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("<data>Test Data</data>"));
        String expected = "Test Data";
        String data = reader.consumeData();
        Assert.assertEquals(expected, data);
        Assert.assertEquals(13, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMatching() {
        CharacterReader reader = new CharacterReader(new StringReader("This is the test string."));
        Assert.assertTrue(reader.matches('T'));
        Assert.assertTrue(reader.matches("is"));
        Assert.assertTrue(reader.matchesIgnoreCase("Is"));
        Assert.assertTrue(reader.matchesAny('s', 't'));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'s', 't'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x', 'y'}));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesLetterThenDigitSequence());
        Assert.assertFalse(reader.matchesHexSequence());
        Assert.assertFalse(reader.matchesConsume("is"));
        Assert.assertFalse(reader.matchesConsumeIgnoreCase("IS"));
        Assert.assertFalse(reader.containsIgnoreCase("Test"));
    }
}