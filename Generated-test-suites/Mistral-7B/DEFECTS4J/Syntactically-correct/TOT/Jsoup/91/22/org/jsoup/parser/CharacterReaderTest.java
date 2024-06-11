package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testReaderWithSingleCharacter() {
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
    public void testReaderWithMultipleCharacters() {
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

    @Test
    public void testReaderWithSpecialCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("\n\r\f\t/&< >"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('\n', reader.current());
        Assert.assertEquals('\n', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('\r', reader.current());
        Assert.assertEquals('\r', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('\f', reader.current());
        Assert.assertEquals('\f', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('\t', reader.current());
        Assert.assertEquals('\t', reader.consume());
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('/', reader.current());
        Assert.assertEquals('/', reader.consume());
        Assert.assertEquals(5, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('&', reader.current());
        Assert.assertEquals('&', reader.consume());
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('<', reader.current());
        Assert.assertEquals('<', reader.consume());
        Assert.assertEquals(7, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(' ', reader.current());
        Assert.assertEquals(' ', reader.consume());
        Assert.assertEquals(8, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('>', reader.current());
        Assert.assertEquals('>', reader.consume());
        Assert.assertEquals(9, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.bufferUp();
        Assert.assertEquals(3, reader.bufLength);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(0, reader.bufSplitPoint);
        Assert.assertArrayEquals(new char[]{'A', 'B', 'C'}, reader.charBuf);
    }

    @Test
    public void testReaderWithMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testReaderWithConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        String consumed = reader.consumeTo('D');
        Assert.assertEquals("ABC", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('E', reader.current());
    }

    @Test
    public void testReaderWithConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJ"));
        String consumed = reader.consumeToAny(new char[]{'D', 'E'});
        Assert.assertEquals("ABCDEF", consumed);
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals('G', reader.current());
    }

    @Test
    public void testReaderWithConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJ"));
        String consumed = reader.consumeToAnySorted(new char[]{'E', 'F'});
        Assert.assertEquals("ABCDEF", consumed);
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals('G', reader.current());
    }

    @Test
    public void testReaderWithConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("& < null"));
        String consumed = reader.consumeData();
        Assert.assertEquals("& <", consumed);
    }

    @Test
    public void testReaderWithConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag />"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("tag", consumed);
    }

    @Test
    public void testReaderWithConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJ"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("ABCDEFGHIJ", consumed);
    }

    @Test(expected = UncheckedIOException.class)
    public void testReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.bufferUp();
    }

    @Test(expected = UncheckedIOException.class)
    public void testReaderWithNegativeBufferSize() {
        CharacterReader reader = new CharacterReader(new StringReader(""), -1);
    }

    @Test(expected = UncheckedIOException.class)
    public void testReaderWithNullInput() {
        CharacterReader reader = new CharacterReader(null, maxBufferLen);
    }

    @Test(expected = UncheckedIOException.class)
    public void testReaderWithNonMarkSupportedInput() {
        CharacterReader reader = new CharacterReader(new StringBuffer(), maxBufferLen);
    }
}