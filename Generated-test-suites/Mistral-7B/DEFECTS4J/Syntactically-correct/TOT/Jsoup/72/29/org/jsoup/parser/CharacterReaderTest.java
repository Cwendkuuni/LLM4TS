package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader("Hello World!"));
    }

    @Test
    public void testEmptyReader() {
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        assertTrue(emptyReader.isEmpty());
        assertEquals(0, emptyReader.pos());
    }

    @Test
    public void testConsumeSingleCharacter() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader iOReader = new CharacterReader(new StringReader("Invalid Input")) {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("I/O error");
            }
        };
        iOReader.consume();
    }

    @Test
    public void testConsumeToEnd() {
        String data = reader.consumeToEnd();
        assertEquals("World!", data);
    }

    @Test
    public void testConsumeTo() {
        assertEquals("llo ", reader.consumeTo('l'));
        assertEquals("ld!", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("llo", reader.consumeToAny(new char[]{'l'}));
        assertEquals("World!", reader.consumeToAny(new char[0]));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("llo", reader.consumeToAnySorted(new char[]{'l'}));
        assertEquals("World!", reader.consumeToAnySorted(new char[]{'d'}));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Hello", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Hello", reader.consumeTagName());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(5, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(5, reader.nextIndexOf("ld"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testMarkAndRewind() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        assertTrue(emptyReader.isEmpty());

        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }
}