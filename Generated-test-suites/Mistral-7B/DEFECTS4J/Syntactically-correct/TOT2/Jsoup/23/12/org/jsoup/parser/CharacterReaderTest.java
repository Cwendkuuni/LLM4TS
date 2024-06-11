package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());

        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(3, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");

        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume(); // EOF
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");

        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consume(); // EOF
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abc", reader.consumeAsString());
        assertEquals("def", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefabc");

        String expected = "abc";
        String consumed = reader.consumeTo('c');
        assertEquals(expected, consumed);

        assertEquals("defabc", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("123abcdef");

        assertEquals("abc", reader.consumeLetterSequence());

        assertEquals("def", reader.input.substring(reader.pos));
    }

    // Add edge cases and exceptions handling test cases for further methods here.
}