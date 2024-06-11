package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for TokenQueue class.
 */
public class TokenQueueTest {
    private TokenQueue queue;

    @Before
    public void setUp() {
        queue = new TokenQueue("hello world <tag>this is a test<tag>");
    }

    /**
     * Test isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.addFirst("hello");
        assertFalse(queue.isEmpty());
    }

    /**
     * Test peek method.
     */
    @Test
    public void testPeek() {
        assertEquals('h', queue.peek());
        queue.consume();
        assertEquals('e', queue.peek());
    }

    /**
     * Test addFirst method.
     */
    @Test
    public void testAddFirst() {
        queue.addFirst("test");
        assertEquals("testhello", queue.toString());
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        assertTrue(queue.matches("hello"));
        assertFalse(queue.matches("world"));
        queue.advance();
        assertTrue(queue.matches("this"));
    }

    /**
     * Test matchesCS method.
     */
    @Test
    public void testMatchesCS() {
        assertTrue(queue.matchesCS("Hello"));
        assertFalse(queue.matchesCS("world"));
        queue.advance();
        assertTrue(queue.matchesCS("This"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        assertTrue(queue.matchesAny("he", "this"));
        assertFalse(queue.matchesAny("world"));
        queue.consume();
        assertTrue(queue.matchesAny("is", "a"));
        queue.consume();
        assertFalse(queue.matchesAny("test"));
    }

    /**
     * Test matchesAny method with char array.
     */
    @Test
    public void testMatchesAnyChars() {
        assertTrue(queue.matchesAny(new char[]{'h', 'e'}));
        assertFalse(queue.matchesAny(new char[]{'w'}));
        queue.advance();
        assertTrue(queue.matchesAny(new char[]{'t', 'h', 'i', 's'}));
        queue.consume();
        assertFalse(queue.matchesAny(new char[]{'t'}));
    }

    /**
     * Test matchesStartTag method.
     */
    @Test
    public void testMatchesStartTag() {
        assertTrue(queue.matchesStartTag());
        queue.consume();
        assertFalse(queue.matchesStartTag());
    }

    /**
     * Test matchChomp method.
     */
    @Test
    public void testMatchChomp() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        queue.consume("hello");
        queue.consume("world");
        assertTrue(queue.matchChomp("<tag>"));
        assertEquals("this is a test<tag>", queue.toString());
    }

    /**
     * Test consume method.
     */
    @Test
    public void testConsume() {
        assertEquals('h', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('t', queue.consume());
        assertEquals(' ', queue.consume()); // whitespace character
        assertEquals('i', queue.consume());
        assertEquals('s', queue.consume());
        assertEquals(' ', queue.consume()); // whitespace character
        assertEquals('a', queue.consume());
        assertEquals(' ', queue.consume()); // whitespace character
        assertEquals('t', queue.consume());
        assertEquals(' ', queue.consume()); // whitespace character
        assertEquals('e', queue.consume());
        assertEquals('s', queue.consume());
        assertEquals('t', queue.consume());
        assertEquals(' ', queue.consume()); // whitespace character
        assertEquals('>', queue.consume());
        assertTrue(queue.isEmpty());
    }

    /**
     * Test consume method with specified sequence.
     */
    @Test
    public void testConsumeWithSequence() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        String consumed = queue.consume("hello world");
        assertEquals("this is a test<tag>", consumed);
        assertEquals(">", queue.toString());
    }

    /**
     * Test consumeTo method.
     */
    @Test
    public void testConsumeTo() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        String consumed = queue.consumeTo("this is a ");
        assertEquals("this is a ", consumed);
        assertEquals("<tag>", queue.toString());
    }

    /**
     * Test consumeToIgnoreCase method.
     */
    @Test
    public void testConsumeToIgnoreCase() {
        queue.addFirst("hello world <TAG>this is a test<TAG>");
        String consumed = queue.consumeToIgnoreCase("this is a ");
        assertEquals("this is a ", consumed);
        assertEquals(">", queue.toString());
    }

    /**
     * Test consumeToAny method.
     */
    @Test
    public void testConsumeToAny() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        String consumed = queue.consumeToAny("test", "tag");
        assertEquals("test<tag>", consumed);
        assertEquals(">", queue.toString());
    }

    /**
     * Test consumeToAny method with char array.
     */
    @Test
    public void testConsumeToAnyChars() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        String consumed = queue.consumeToAny(new char[]{'t', 'a', 'g'});
        assertEquals("tag", consumed);
        assertEquals(">", queue.toString());
    }

    /**
     * Test chompBalanced method.
     */
    @Test
    public void testChompBalanced() {
        queue.addFirst("(hello (world (hello world) (hello world))) (world)");
        String consumed = queue.chompBalanced('(', ')');
        assertEquals("hello (world (hello world) (hello world))", consumed);
        assertEquals("(world)", queue.toString());
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        String in = "\\hello\\world\\<tag>this is a test<tag>\\";
        String out = TokenQueue.unescape(in);
        assertEquals("helloworld<tag>this is a test<tag>", out);
    }

    /**
     * Test consumeWhitespace method.
     */
    @Test
    public void testConsumeWhitespace() {
        queue.addFirst("hello world   <tag>this is   a test<tag>   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("hello world<tag>this is a test<tag>", queue.toString());
    }

    /**
     * Test consumeWord method.
     */
    @Test
    public void testConsumeWord() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        String consumed = queue.consumeWord();
        assertEquals("hello", consumed);
        assertEquals("world<tag>this is a test<tag>", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("world", consumed);
        assertEquals("<tag>this is a test<tag>", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("tag", consumed);
        assertEquals("this is a test<tag>", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("this", consumed);
        assertEquals("is a test<tag>", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("is", consumed);
        assertEquals("a test<tag>", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("a", consumed);
        assertEquals("test<tag>", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("test", consumed);
        assertEquals(">", queue.toString());
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        queue.addFirst("hello world <tag:name>this is a test<tag:name>");
        String consumed = queue.consumeTagName();
        assertEquals("tag:name", consumed);
        assertEquals("this is a test<tag:name>", queue.toString());
    }

    /**
     * Test consumeElementSelector method.
     */
    @Test
    public void testConsumeElementSelector() {
        queue.addFirst("hello world <tag:name|namespace>this is a test<tag:name|namespace>");
        String consumed = queue.consumeElementSelector();
        assertEquals("tag:name|namespace", consumed);
        assertEquals("this is a test<tag:name|namespace>", queue.toString());
    }

    /**
     * Test consumeCssIdentifier method.
     */
    @Test
    public void testConsumeCssIdentifier() {
        queue.addFirst("hello world <tag:id:identifier>this is a test<tag:id:identifier>");
        String consumed = queue.consumeCssIdentifier();
        assertEquals("identifier", consumed);
        assertEquals("this is a test<tag:id:identifier>", queue.toString());
    }

    /**
     * Test consumeAttributeKey method.
     */
    @Test
    public void testConsumeAttributeKey() {
        queue.addFirst("hello world <tag attr:key=\"attributeKey\">this is a test<tag attr:key=\"attributeKey\">");
        String consumed = queue.consumeAttributeKey();
        assertEquals("attributeKey", consumed);
        assertEquals("this is a test<tag attr:key=\"attributeKey\">", queue.toString());
    }

    /**
     * Test remainder method.
     */
    @Test
    public void testRemainder() {
        queue.addFirst("hello world <tag>this is a test<tag>");
        String remainder = queue.remainder();
        assertEquals("", remainder);
        assertTrue(queue.isEmpty());

        queue.addFirst("hello world <tag>this is a test<tag>");
        remainder = queue.remainder();
        assertEquals("this is a test<tag>", remainder);
        assertFalse(queue.isEmpty());
    }
}