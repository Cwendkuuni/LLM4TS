package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("TestString12345_ABCdef123ABC");
    }

    @Test
    public void testConstructor() {
        assertNotNull(reader);
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String consumed = reader.consumeAsString();
        assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo('3');
        assertEquals("123", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        String consumed = reader.consumeToEnd();
        assertEquals("ABCdef123ABC", consumed);
    }

    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny('a', 'b');
        assertEquals("ABCdef123", consumed);
    }

    @Test
    public void testConsumeToEndIfNoMatch() {
        String consumed = reader.consumeToAny('x');
        assertNull(consumed);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("est"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("est"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('e', 'd'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('A'));
    }

    @Test
    public void testMatchConsume() {
        String consumed = reader.matchConsume("est");
        assertEquals("est", consumed);
        assertEquals('f', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String consumed = reader.matchConsumeIgnoreCase("Est");
        assertEquals("est", consumed);
        assertEquals('f', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("def"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test (expected = AssertionError.class)
    public void testConsumePastEnd() {
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();
        assertEquals("ABC", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        String consumed = reader.consumeHexSequence();
        assertEquals("123", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
    }

}