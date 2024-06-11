package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import java.util.Locale;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any test data here
    }

    @org.junit.Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.input);
        assertEquals(11, reader.length);
    }

    @org.junit.Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
    }

    @org.junit.Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
    }

    @org.junit.Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @org.junit.Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
    }

    @org.junit.Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @org.junit.Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @org.junit.Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @org.junit.Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("cd", reader.consumeAsString());
    }

    @org.junit.Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abababab");
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));
        reader.advance();
        assertEquals(2, reader.nextIndexOf('a'));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.nextIndexOf('a'));
    }

    @org.junit.Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals(4, reader.nextIndexOf("cd"));
        assertEquals(-1, reader.nextIndexOf("ef"));

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(4, reader.nextIndexOf("cd"));
    }

    @org.junit.Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("cdabcd", reader.toString());

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals("cd", reader.consumeTo('d'));
    }

    @org.junit.Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals("abcdabcd", reader.consumeToEnd());
    }

    @org.junit.Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234abcd1234");
        assertEquals("123", reader.consumeToAny('1', '2', '3'));
        assertEquals("1234abcd1234", reader.toString());
    }

    @org.junit.Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("cd"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("ef"));

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.matches('b'));
    }

    @org.junit.Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCdABCd");
        assertTrue(reader.matchesIgnoreCase("CD"));
        assertFalse(reader.matchesIgnoreCase("EF"));

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.matchesIgnoreCase("B"));
    }

    @org.junit.Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("1234abcd1234");
        assertTrue(reader.matchesAny('1', '2', '3'));
        assertFalse(reader.matchesAny('5'));

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.matchesAny('1', '2', '3'));
    }

    @org.junit.Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.matchesLetter());
    }

    @org.junit.Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234abcd1234");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.matchesDigit());
    }

    @org.junit.Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><title>Hello World</title></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("html"));
        assertFalse(reader.containsIgnoreCase("body"));
    }

    @org.junit.After
    public void tearDown() {
        // Release any allocated resources here
    }
}