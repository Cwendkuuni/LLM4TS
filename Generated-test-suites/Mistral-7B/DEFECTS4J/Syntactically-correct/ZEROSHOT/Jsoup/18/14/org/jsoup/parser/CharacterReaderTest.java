package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        String input = "abc123ABC DEF<tag>12345</tag>XYZ";
        reader = new CharacterReader(input);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeFromEmptyInput() {
        CharacterReader emptyReader = new CharacterReader("");
        emptyReader.consume();
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        char consumed = reader.consume();
        assertEquals('a', consumed);
        assertEquals('b', reader.current());

        char eof = reader.consume();
        assertEquals(-1, eof);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String consumed = reader.consumeAsString();
        assertEquals("bc", consumed);

        consumed = reader.consumeAsString(3);
        assertEquals("DEF", consumed);
    }

    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo('3');
        assertEquals("123", consumed);

        consumed = reader.consumeTo('T');
        assertEquals("<tag>", consumed);

        consumed = reader.consumeToEnd();
        assertEquals("XYZ", consumed);
    }

    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny('1', '2');
        assertEquals("123", consumed);

        consumed = reader.consumeToAny('a', 'z');
        assertEquals("DEF", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);

        consumed = reader.consumeLetterSequence(3);
        assertEquals("DEF", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        String consumed = reader.consumeHexSequence();
        assertEquals("12345", consumed);

        consumed = reader.consumeHexSequence(5);
        assertEquals("12345", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);

        consumed = reader.consumeDigitSequence(3);
        assertEquals("123", consumed);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("xyZ"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('z', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        String consumed = reader.matchConsume("XY");
        assertEquals("XY", consumed);
        assertEquals('Z', reader.current());

        consumed = reader.matchConsume("xy");
        assertEquals("xy", consumed);
        assertEquals('Z', reader.current());

        consumed = reader.matchConsume("TAG");
        assertEquals("TAG", consumed);
        assertEquals('1', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String consumed = reader.matchConsumeIgnoreCase("xy");
        assertEquals("xy", consumed);
        assertEquals('Z', reader.current());

        consumed = reader.matchConsumeIgnoreCase("TAG");
        assertEquals("TAG", consumed);
        assertEquals('1', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("TAG"));
        assertTrue(reader.containsIgnoreCase("tag"));
    }

    @Test
    public void testToString() {
        assertEquals("DEF<tag>12345</tag>XYZ", reader.toString());
    }
}