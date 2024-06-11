package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals('B', reader.current());
        reader.consume();
        Assert.assertEquals('C', reader.current());
        reader.consume();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp(); // trigger IOException
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        reader.mark();
        reader.consume(); // consume 'A'
        Assert.assertEquals(1, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertEquals(6, reader.nextIndexOf('F'));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo('F');
        Assert.assertEquals("ABC", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("ABCDEFG", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAny(new char[]{'F', 'E'});
        Assert.assertEquals("ABCDE", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAnySorted(new char[]{'F', 'E', 'D'});
        Assert.assertEquals("ABCD", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;script src=\"script.js\"&gt;"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&lt;script src=\"script.js\"", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head></head><body></body></html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
    }
}