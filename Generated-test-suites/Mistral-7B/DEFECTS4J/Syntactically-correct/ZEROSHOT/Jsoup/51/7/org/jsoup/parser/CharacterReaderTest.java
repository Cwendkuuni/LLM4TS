package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader reader;
    private final String input = "abc123def&<>&1234ABC";

    @Before
    public void setUp() {
        reader = new CharacterReader(input);
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

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals(input.charAt(0), reader.current());

        reader.consume();
        assertEquals(input.charAt(1), reader.current());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(input.charAt(0), reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals(input.charAt(1), reader.current());

        reader.consume();
        assertEquals(input.charAt(2), reader.current());

        reader.consume();
        assertEquals(input.charAt(3), reader.current());

        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals(input.charAt(0), reader.current());

        reader.consume();
        reader.unconsume();
        reader.consume();
        assertEquals(input.charAt(2), reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(input.charAt(1), reader.current());

        reader.advance();
        assertEquals(input.charAt(2), reader.current());

        reader.advance();
        assertEquals(input.charAt(3), reader.current());

        reader.advance();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals(input.charAt(0), reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("a", reader.consumeAsString());
        assertEquals("bc", reader.consumeAsString());
        assertEquals("def&<", reader.consumeAsString());

        reader.consume();
        assertEquals("&", reader.consumeAsString());
        reader.consume();
        assertEquals(">", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf('&'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(5, reader.nextIndexOf("&<"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("a", reader.consumeTo('b').trim());
        assertEquals("bcdef&<", reader.consumeTo("&<").trim());
        assertEquals("abc123", reader.consumeTo('1').trim());
        assertEquals("ABC", reader.consumeTo("ABC").trim());
        assertEquals("", reader.consumeTo('z').trim());
        assertEquals("", reader.consumeTo("xyz").trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("def&<>&1234ABC", reader.consumeToEnd().trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("&", reader.consumeToAny(new char[]{'&'}).trim());
        assertEquals("><", reader.consumeToAny(new char[]{'&', '<'}).trim());
        assertEquals("", reader.consumeToAny(new char[]{'z'}).trim());
        assertEquals("", reader.consumeToAny(new char[]{'x', 'y', 'z'}).trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("&<", reader.consumeToAnySorted(new char[]{'<', '&'}).trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}).trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'x', 'y', 'z'}).trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeData() {
        assertEquals("&", reader.consumeData().trim());
        assertEquals("><", reader.consumeData().trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("title", reader.consumeTagName().trim());
        assertEquals("style", reader.consumeTagName().trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("style"));
        assertFalse(reader.containsIgnoreCase("xyz"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('z'));
        assertTrue(reader.matches("-"));
        assertTrue(reader.matches('\u0001'));
        assertFalse(reader.matches('\u0002'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertTrue(reader.matchesIgnoreCase("abc"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(new char[]{'a'}));
        assertTrue(reader.matchesAny(new char[]{'b'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
        assertTrue(reader.matchesAny(new char[]{'&'}));
        assertTrue(reader.matchesAny(new char[]{'<'}));
        assertTrue(reader.matchesAny(new char[]{'>'}));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'&', '<'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
        assertTrue(reader.matchesAnySorted(new char[]{'x', 'y', 'z'}));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsume() {
        assertEquals("bcdef&<", reader.toString());
        reader.matchConsume("&<");
        assertEquals("abc123", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertEquals("def&<>&1234ABC", reader.toString());
        reader.matchConsumeIgnoreCase("title");
        assertEquals("abc123", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testToString() {
        assertEquals("", reader.toString());
        reader.consume();
        assertEquals("a", reader.toString());
        reader.consumeToEnd();
        assertEquals("def&<>&1234ABC", reader.toString());
        assertTrue(reader.isEmpty());
    }
}