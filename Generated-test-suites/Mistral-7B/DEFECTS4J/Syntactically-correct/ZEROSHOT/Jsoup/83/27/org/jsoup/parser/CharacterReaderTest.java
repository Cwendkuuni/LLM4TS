package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());
        assertEquals(11, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('W', reader.current());
        assertEquals('W', reader.consume());
        assertEquals(10, reader.pos());
        assertEquals('H', reader.current());
        assertEquals(11, reader.pos());
        reader.rewindToMark();
        assertEquals('W', reader.current());
        assertEquals(11, reader.pos());
        assertEquals("llo Wor", reader.consumeTo("llo Wor"));
        assertEquals("ld", reader.consumeToEnd());
        assertEquals("ld", reader.consumeToAny(new char[]{'l', 'd'}));
        assertEquals("ld", reader.consumeToAnySorted(new char[]{'d', 'l'}));
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertFalse(reader.matches('z'));
        assertTrue(reader.matches("ll"));
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertTrue(reader.matchesAny(new char[]{'h', 'e'}));
        assertTrue(reader.matchesAnySorted(new char[]{'h', 'e'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchConsume("ll"));
        assertTrue(reader.matchConsumeIgnoreCase("HELLO"));
        assertFalse(reader.containsIgnoreCase("foo"));
        reader.bufferUp();
        reader.consumeToAny(new char[]{'&', '<', '\u0000'});
        assertEquals("&<\\u0000", reader.consumeToEnd());
        reader.bufferUp();
        assertEquals("title", reader.consumeTagName());
        reader.mark();
        reader.rewindToMark();
        assertEquals("title", reader.consumeTagName());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.reader.close();
        reader.consume();
    }
}