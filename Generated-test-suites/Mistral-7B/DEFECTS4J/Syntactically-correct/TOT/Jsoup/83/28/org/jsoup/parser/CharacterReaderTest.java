package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(new StringReader("abcdefg"));
    }

    @Test
    public void testNewInstanceWithReader() {
        CharacterReader reader1 = new CharacterReader(new StringReader("123456"));
        CharacterReader reader2 = new CharacterReader(reader1);
        assertEquals(reader1, reader2);
    }

    @Test
    public void testNewInstanceWithString() {
        CharacterReader reader1 = new CharacterReader("abcdefg");
        CharacterReader reader2 = new CharacterReader("123456");
        assertNotSame(reader1, reader2);
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "abcdefg";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeTo() {
        String expected = "ab";
        String actual = reader.consumeTo('c');
        assertEquals(expected, actual);
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeToSingleChar() {
        String expected = "a";
        String actual = reader.consumeTo('a');
        assertEquals(expected, actual);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToNoMatch() {
        String expected = "";
        String actual = reader.consumeTo('z');
        assertEquals(expected, actual);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToAny() {
        String expected = "abcdefg";
        String actual = reader.consumeToAny(new char[]{'a', 'c', 'e'});
        assertEquals(expected, actual);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "defg";
        String actual = reader.consumeToAnySorted(new char[]{'a', 'c', 'e', 'f'});
        assertEquals(expected, actual);
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "abc";
        String actual = reader.consumeTagName();
        assertEquals(expected, actual);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeData() {
        String expected = "abcdefg";
        String actual = reader.consumeData();
        assertEquals(expected, actual);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }
}