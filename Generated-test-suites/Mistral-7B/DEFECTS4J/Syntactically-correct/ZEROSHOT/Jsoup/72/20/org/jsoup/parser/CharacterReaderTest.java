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
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
        Assert.assertEquals(-1, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals(5, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderReader() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        reader.bufferUp();
        Assert.assertEquals(13, reader.bufLength);
        Assert.assertEquals(3, reader.bufPos);
        Assert.assertEquals(10, reader.readerPos);
        Assert.assertEquals(3, reader.bufSplitPoint);
        Assert.assertEquals(13, reader.bufMark);
        Assert.assertEquals('d', reader.charBuf[3]);
        Assert.assertEquals('e', reader.charBuf[4]);
        Assert.assertEquals('f', reader.charBuf[5]);
        Assert.assertEquals('g', reader.charBuf[6]);
        Assert.assertEquals('h', reader.charBuf[7]);
        Assert.assertEquals('i', reader.charBuf[8]);
        Assert.assertEquals('j', reader.charBuf[9]);
        Assert.assertEquals('k', reader.charBuf[10]);
        Assert.assertEquals('l', reader.charBuf[11]);
        Assert.assertEquals('m', reader.charBuf[12]);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String consumed = reader.consumeTo('e');
        Assert.assertEquals(5, reader.pos());
        Assert.assertEquals("abcde", consumed);

        consumed = reader.consumeTo('s');
        Assert.assertEquals(11, reader.pos());
        Assert.assertEquals("abcdefghijklmns", consumed);

        consumed = reader.consumeTo('z');
        Assert.assertEquals(26, reader.pos());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", consumed);

        consumed = reader.consumeTo('z');
        Assert.assertEquals(26, reader.pos());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", consumed);

        consumed = reader.consumeToAny(new char[]{'a', 'b'});
        Assert.assertEquals(13, reader.pos());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'z', 'y', 'x'});
        Assert.assertEquals(26, reader.pos());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals(26, reader.pos());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body></body></html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals("html", consumed);

        consumed = reader.consumeTagName();
        Assert.assertEquals(10, reader.pos());
        Assert.assertEquals("head", consumed);

        consumed = reader.consumeTagName();
        Assert.assertEquals(14, reader.pos());
        Assert.assertEquals("title", consumed);

        consumed = reader.consumeTo('>');
        Assert.assertEquals(20, reader.pos());
        Assert.assertEquals("<title>Test</title>", consumed);
    }
}