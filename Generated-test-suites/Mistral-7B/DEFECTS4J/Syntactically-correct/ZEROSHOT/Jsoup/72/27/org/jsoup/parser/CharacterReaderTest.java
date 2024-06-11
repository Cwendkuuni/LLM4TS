package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(1, reader.pos());
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
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSubSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String expected = "123";
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(expected.charAt(0), reader.current());
        reader.consume();
        Assert.assertEquals(expected.charAt(1), reader.current());
        reader.consume();
        String actual = reader.consumeTo(expected.charAt(2));
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        CharacterReader reader = new CharacterReader(new InputStreamReader(inputStream));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // This should throw an IOException
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        String expected = reader.consumeTo('B');
        Assert.assertEquals(1, reader.pos() - reader.bufPos);
        reader.rewindToMark();
        String actual = reader.consumeTo('B');
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, reader.pos() - reader.bufPos);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals(6, reader.nextIndexOf('f'));
        Assert.assertEquals(6, reader.pos() - reader.bufPos);
    }

    @Test
    public void testCharacterReaderNextIndexOfEmptySequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(-1, reader.nextIndexOf("def"));
    }

    @Test
    public void testCharacterReaderNextIndexOfCaseInsensitive() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfG"));
        Assert.assertEquals(3, reader.nextIndexOfIgnoreCase("cd"));
    }

    @Test
    public void testCharacterReaderNextIndexOfAny() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertEquals(1, reader.nextIndexOfAny(new char[]{'1', '3'}));
        Assert.assertEquals(1, reader.pos() - reader.bufPos);
    }

    @Test
    public void testCharacterReaderNextIndexOfAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertEquals(4, reader.nextIndexOfAnySorted(new char[]{'3', '5'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String expected = reader.consumeData();
        Assert.assertEquals("&<null", expected);
        Assert.assertEquals(3, reader.pos() - reader.bufPos);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        String expected = "tag";
        Assert.assertEquals(expected, reader.consumeTagName());
        Assert.assertEquals(5, reader.pos() - reader.bufPos);
    }
}