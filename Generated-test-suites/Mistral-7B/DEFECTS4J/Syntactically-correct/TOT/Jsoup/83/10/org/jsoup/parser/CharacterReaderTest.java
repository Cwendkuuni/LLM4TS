package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;

/**
 * CharacterReaderTest class for testing CharacterReader methods.
 */
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
    public void testCharacterReaderSimpleString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('E', reader.current());
    }

    @Test
    public void testCharacterReaderMaxBufferLen() {
        CharacterReader reader = new CharacterReader(new StringReader(new String(new char[CharacterReader.maxBufferLen * 2])), CharacterReader.maxBufferLen);
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current()); // arbitrary character at the beginning of the String
        Assert.assertEquals('H', reader.consume());
        // maxBufferLen bytes have been read, bufferUp() should read the next 'readAheadLimit' bytes
        Assert.assertEquals(CharacterReader.readAheadLimit, reader.bufLength);
        Assert.assertEquals(CharacterReader.readAheadLimit, reader.bufSplitPoint);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""), 1);
        reader.bufferUp();
        throw new RuntimeException("IOException simulation");
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String consumed = reader.consumeTo('f');
        Assert.assertEquals("abcde", consumed);
        Assert.assertEquals(reader.pos(), 6);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdefg", consumed);
        Assert.assertEquals(reader.pos(), CharacterReader.EOF);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(5, reader.nextIndexOf('e'));
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals(-1, reader.nextIndexOf("hij"));
        Assert.assertEquals(5, reader.nextIndexOf("e"));
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        reader.mark();
        String initialData = reader.consumeToEnd();
        reader.rewindToMark();
        Assert.assertEquals(initialData, reader.consumeToEnd());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderConsumeAfterMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String consumed = reader.consumeToAny(new char[]{'e', 'a', 'i', 'o'});
        Assert.assertEquals("abcdefeaiaiio", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String consumed = reader.consumeToAnySorted(new char[]{'e', 'a', 'i', 'o'});
        Assert.assertEquals("abcdeaio", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&<", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tab\n\tTitle>\r"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("tabTitle>", consumed);
    }
}