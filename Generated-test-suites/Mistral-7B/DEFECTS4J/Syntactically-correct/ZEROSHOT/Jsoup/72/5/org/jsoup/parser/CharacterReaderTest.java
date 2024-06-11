package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals('B', reader.current());
        reader.consume();
        Assert.assertEquals('C', reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderReadToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("ABC", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderReadToDelimiter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String consumed = reader.consumeTo('C');
        Assert.assertEquals("AB", consumed);
        Assert.assertEquals('C', reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderReadToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo("DEF");
        Assert.assertEquals("ABC", consumed);
        Assert.assertEquals('D', reader.current());
        Assert.assertEquals('E', reader.current());
        Assert.assertEquals('F', reader.current());
        reader.consume();
        Assert.assertEquals('G', reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() throws IOException {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("ABC".getBytes())));
        reader.mark();
        String consumed = reader.consumeTo('B');
        Assert.assertEquals("A", consumed);
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals("BC", reader.consumeToEnd());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[1][0])));
        reader.consume();
    }
}