package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Testing CharacterReader class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('H', reader.current());
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.consume();
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consumeTo('l');
        Assert.assertEquals("Hello", reader.consumeToEnd());
        reader.rewindToMark();
        reader.consume('l');
        Assert.assertEquals('o', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(0, reader.nextIndexOf('H'));
        Assert.assertEquals(1, reader.nextIndexOf('e'));
        Assert.assertEquals(6, reader.nextIndexOf('W'));
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
        reader.rewindToMark();
        reader.consume('l');
        Assert.assertEquals(7, reader.nextIndexOf('l'));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            void bufferUp() {
                throw new IOException();
            }
        };
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.consumeToEnd());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", reader.consumeTo('W'));
        Assert.assertEquals("llo", reader.consumeTo('l'));
        Assert.assertEquals("ll", reader.consumeTo('l'));
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", reader.consumeToAny(new char[]{'W'}));
        Assert.assertEquals("llo", reader.consumeToAny(new char[]{'l'}));
        Assert.assertEquals("ll", reader.consumeToAny(new char[]{'l'}));
        Assert.assertEquals("", reader.consumeToAny(new char[]{'x'}));
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", reader.consumeToAnySorted(new char[]{'W'}));
        Assert.assertEquals("llo", reader.consumeToAnySorted(new char[]{'l'}));
        Assert.assertEquals("ll", reader.consumeToAnySorted(new char[]{'l'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'x'}));
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&Hello World<"));
        Assert.assertEquals("&H&e&l&l&o&nbsp;World<", reader.consumeData());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertEquals("<html><head>", reader.consumeTagName());
        Assert.assertEquals("title", reader.consumeTagName());
        Assert.assertEquals("</title>", reader.consumeTagName());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String hello = reader.consumeTo("Hello");
        String world = reader.consumeTo("World");
        String result = reader.cacheString(reader.charBuf, reader.stringCache, 0, reader.bufPos);
        Assert.assertEquals(hello, result);
        Assert.assertEquals(world, reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeToAny(new char[]{'x'}));
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }
}