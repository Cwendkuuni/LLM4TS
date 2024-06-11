package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("TestString");
    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.rewindToMark();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals(reader.current(), '\u0000');
        reader.consume();
        assertEquals(reader.current(), reader.input[0]);
        reader.rewindToMark();
        assertEquals(reader.current(), '\u0000');
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals(reader.current(), reader.input[0]);
        reader.consume();
        assertEquals(reader.current(), reader.input[1]);
        reader.rewindToMark();
        assertEquals(reader.current(), reader.input[0]);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals(reader.current(), reader.input[0]);
        reader.consume();
        reader.unconsume();
        assertEquals(reader.current(), reader.input[0]);
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(reader.current(), reader.input[1]);
        reader.advance();
        assertEquals(reader.current(), reader.input[2]);
        reader.rewindToMark();
        assertEquals(reader.current(), reader.input[1]);
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals(reader.current(), reader.input[0]);
    }

    @Test
    public void testRewindToMark() {
        reader.consume();
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        reader.consume();
        assertEquals(reader.current(), reader.input[1]);
    }

    @Test
    public void testConsumeAsString() {
        String str = reader.consumeAsString();
        assertEquals(str, new String(new char[]{reader.input[0]}));
        reader.consume();
        str = reader.consumeAsString();
        assertEquals(str, new String(new char[]{reader.input[1]}));
        reader.rewindToMark();
        str = reader.consumeAsString();
        assertEquals(str, new String(new char[]{reader.input[0]}));
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, reader.nextIndexOf('x'));
        reader.consume();
        assertEquals(0, reader.nextIndexOf('T'));
        reader.consume();
        assertEquals(1, reader.nextIndexOf('e'));
        reader.consume();
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testConsumeTo() {
        String str = reader.consumeTo('t');
        assertEquals(str, new String(new char[]{'T'}));
        reader.consume();
        str = reader.consumeTo('s');
        assertEquals(str, new String(new char[]{'s'}));
        reader.consume();
        str = reader.consumeTo('t');
        assertEquals(str, new String(new char[]{'t'}));
        reader.rewindToMark();
        str = reader.consumeTo('t');
        assertEquals(str, new String(new char[]{'t'}));
    }

    @Test
    public void testConsumeToEnd() {
        String str = reader.consumeToEnd();
        assertEquals(str, new String(reader.input, 0, reader.length));
    }

    @Test
    public void testConsumeLetterSequence() {
        String str = reader.consumeLetterSequence();
        assertEquals(str, new String(new char[]{reader.input[0]}));
        reader.consume();
        str = reader.consumeLetterSequence();
        assertEquals(str, new String(new char[]{reader.input[1]}));
        reader.consume();
        str = reader.consumeLetterSequence();
        assertEquals(str, new String(new char[]{reader.input[2]}));
        reader.rewindToMark();
        str = reader.consumeLetterSequence();
        assertEquals(str, new String(new char[]{reader.input[0]}));
    }

    @Test
    public void testMatches() {
        assertFalse(reader.matches('x'));
        reader.consume();
        assertTrue(reader.matches('T'));
        reader.consume();
        assertTrue(reader.matches('e'));
        reader.consume();
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("x"));
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("T"));
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("e"));
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("x"));
    }
}