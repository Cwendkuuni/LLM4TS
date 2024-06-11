package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;

/**
 * CharacterReaderTest class
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader("Hello World"));
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
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(6, reader.pos());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(6, reader.nextIndexOf('W'));
        assertEquals(7, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfString() {
        assertEquals(6, reader.nextIndexOf("llo"));
        assertEquals(11, reader.nextIndexOf("World"));
        assertEquals(-1, reader.nextIndexOf("XYZ"));
    }

    @Test
    public void testConsumeTo() {
        String result = reader.consumeTo(' ');
        assertEquals("llo", result);
    }

    @Test
    public void testConsumeToString() {
        String result = reader.consumeTo("ld");
        assertEquals("ld", result);
    }

    @Test
    public void testConsumeToAny() {
        String result = reader.consumeToAny('l', 'd');
        assertEquals("llo", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = reader.consumeToAnySorted('d', 'l');
        assertEquals("llo", result);
    }

    @Test
    public void testConsumeData() {
        String result = reader.consumeData();
        assertEquals("lloWorld", result);
    }

    @Test
    public void testConsumeTagName() {
        String result = reader.consumeTagName();
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToEnd() {
        String result = reader.consumeToEnd();
        assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = reader.consumeLetterSequence();
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("Hello123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String result = reader.consumeHexSequence();
        assertEquals("HelloWorld", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String result = reader.consumeDigitSequence();
        assertEquals("123456", result);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches('o'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("HEllO"));
        assertFalse(reader.matchesIgnoreCase("XXX"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('l', 'o'));
        assertTrue(reader.matchesAny('h', 'l'));
        assertFalse(reader.matchesAny('x', 'z'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        assertTrue(reader.matchesAnySorted(new char[]{'h', 'l'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x', 'z'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('1'));
    }

    @Test
    public void testMatchConsume() {
        reader.matchConsume("llo");
        assertEquals(8, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader.matchConsumeIgnoreCase("Hello");
        assertEquals(11, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("STYLE"));
        assertFalse(reader.containsIgnoreCase("XYZ"));
    }
}