package org.jsoup.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

/**
 * JUnit 4 test file for the TokenQueue class.
 */
public class TokenQueueTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abcdef", queue.queue);
        assertEquals(0, queue.pos);
    }

    /**
     * Test isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.isEmpty());

        data = "abcdef";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    /**
     * Test peek method.
     */
    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
        assertEquals('a', queue.queue.charAt(0));

        queue.advance();
        assertEquals('b', queue.peek());
    }

    /**
     * Test peek when queue is empty.
     */
    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.peek();
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("b"));
        assertTrue(queue.matches("ef"));
        assertFalse(queue.matches("g"));
    }

    /**
     * Test matchesCS method.
     */
    @Test
    public void testMatchesCS() {
        String data = "abcABCdef";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesCS("a"));
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("ef"));
        assertFalse(queue.matchesCS("g"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("a", "ef"));
        assertFalse(queue.matchesAny("g", "h"));
    }

    /**
     * Test matchesAny with multiple arguments.
     */
    @Test
    public void testMatchesAnyMultiple() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesAny("a", "b", "g"));
        assertFalse(queue.matchesAny("h", "i", "j"));
    }

    /**
     * Test matchesStartTag method.
     */
    @Test
    public void testMatchesStartTag() {
        String data = "<abcd><efgh>";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesStartTag());
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    /**
     * Test matchChomp method.
     */
    @Test
    public void testMatchChomp() {
        String data = "abc<efgh>";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchChomp("ab"));
        assertEquals("c<efgh>", queue.queue);
        assertEquals(3, queue.pos);

        assertTrue(queue.matchChomp("ef"));
        assertEquals("gh>", queue.queue);
        assertEquals(5, queue.pos);

        assertFalse(queue.matchChomp("g"));
    }

    /**
     * Test matchesWhitespace method.
     */
    @Test
    public void testMatchesWhitespace() {
        String data = "   abc   def  ";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    /**
     * Test matchesWord method.
     */
    @Test
    public void testMatchesWord() {
        String data = "abcdEfgh";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    /**
     * Test advance method.
     */
    @Test
    public void testAdvance() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        queue.advance();
        assertEquals('b', queue.queue.charAt(0));
        assertEquals(1, queue.pos);

        queue.advance();
        assertEquals('c', queue.queue.charAt(0));
        assertEquals(2, queue.pos);
    }

    /**
     * Test consume method.
     */
    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        String consumed = queue.consume();
        assertEquals("abcdef", queue.queue);
        assertEquals(5, queue.pos);
        assertEquals("a", consumed);

        consumed = queue.consume();
        assertEquals("ef", queue.queue);
        assertEquals(7, queue.pos);
        assertEquals("e", consumed);

        consumed = queue.consume();
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
        assertNull(consumed);
    }

    /**
     * Test consume method with specified sequence.
     */
    @Test
    public void testConsumeSpecifiedSequence() {
        String data = "abcd<efgh>";
        TokenQueue queue = new TokenQueue(data);

        String consumed = queue.consume("ab");
        assertEquals("cd<efgh>", queue.queue);
        assertEquals(3, queue.pos);
        assertEquals("ab", consumed);

        consumed = queue.consume("ef");
        assertEquals("gh>", queue.queue);
        assertEquals(6, queue.pos);
        assertEquals("ef", consumed);

        consumed = queue.consume("gh");
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
        assertNull(consumed);
    }

    /**
     * Test consumeTo method.
     */
    @Test
    public void testConsumeTo() {
        String data = "abcd<efgh>";
        TokenQueue queue = new TokenQueue(data);

        String consumed = queue.consumeTo("ef");
        assertEquals("abcd<", queue.queue);
        assertEquals(4, queue.pos);
        assertEquals("abcd", consumed);

        consumed = queue.consumeTo("gh");
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
        assertNull(consumed);
    }

    /**
     * Test consumeToIgnoreCase method.
     */
    @Test
    public void testConsumeToIgnoreCase() {
        String data = "ABCefGH";
        TokenQueue queue = new TokenQueue(data);

        String consumed = queue.consumeToIgnoreCase("ef");
        assertEquals("ABCefGH", queue.queue);
        assertEquals(5, queue.pos);
        assertEquals("ef", consumed);

        consumed = queue.consumeToIgnoreCase("GH");
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
        assertNull(consumed);
    }

    /**
     * Test consumeToAny method.
     */
    @Test
    public void testConsumeToAny() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        String consumed = queue.consumeToAny("a", "b");
        assertEquals("bcdef", queue.queue);
        assertEquals(3, queue.pos);
        assertEquals("ab", consumed);

        consumed = queue.consumeToAny("c", "d");
        assertEquals("ef", queue.queue);
        assertEquals(5, queue.pos);
        assertNull(consumed);
    }

    /**
     * Test chompBalanced method.
     */
    @Test
    public void testChompBalanced() {
        String data = "((())())";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("()()()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);
        assertEquals(6, queue.pos);
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        String data = "\\abc\\def\\\\";
        String expected = "abcdef\\";
        String result = TokenQueue.unescape(data);
        assertEquals(expected, result);
    }

    /**
     * Test consumeWhitespace method.
     */
    @Test
    public void testConsumeWhitespace() {
        String data = "   abc   def  ";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    /**
     * Test consumeWord method.
     */
    @Test
    public void testConsumeWord() {
        String data = "abcdEfgh";
        TokenQueue queue = new TokenQueue(data);

        String word = queue.consumeWord();
        assertEquals("abcd", queue.queue);
        assertEquals(5, queue.pos);
        assertEquals("abcd", word);

        word = queue.consumeWord();
        assertEquals("Efgh", queue.queue);
        assertEquals(8, queue.pos);
        assertEquals("Efgh", word);

        word = queue.consumeWord();
        assertEquals("", queue.queue);
        assertEquals(10, queue.pos);
        assertNull(word);
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        String data = "<abcd:efgh>";
        TokenQueue queue = new TokenQueue(data);

        String tagName = queue.consumeTagName();
        assertEquals("abcd:", queue.queue);
        assertEquals(6, queue.pos);
        assertEquals("abcd", tagName);

        tagName = queue.consumeTagName();
        assertEquals("efgh", queue.queue);
        assertEquals(10, queue.pos);
        assertEquals("efgh", tagName);

        tagName = queue.consumeTagName();
        assertEquals("", queue.queue);
        assertEquals(12, queue.pos);
        assertNull(tagName);
    }

    /**
     * Test consumeElementSelector method.
     */
    @Test
    public void testConsumeElementSelector() {
        String data = "abcd|efgh";
        TokenQueue queue = new TokenQueue(data);

        String selector = queue.consumeElementSelector();
        assertEquals("abcd|", queue.queue);
        assertEquals(5, queue.pos);
        assertEquals("abcd", selector);

        selector = queue.consumeElementSelector();
        assertEquals("efgh", queue.queue);
        assertEquals(9, queue.pos);
        assertEquals("efgh", selector);

        selector = queue.consumeElementSelector();
        assertEquals("", queue.queue);
        assertEquals(11, queue.pos);
        assertNull(selector);
    }

    /**
     * Test consumeCssIdentifier method.
     */
    @Test
    public void testConsumeCssIdentifier() {
        String data = "id1 id2 class1 class2";
        TokenQueue queue = new TokenQueue(data);

        String identifier = queue.consumeCssIdentifier();
        assertEquals("id1", queue.queue);
        assertEquals(4, queue.pos);
        assertEquals("id1", identifier);

        identifier = queue.consumeCssIdentifier();
        assertEquals("id2", queue.queue);
        assertEquals(7, queue.pos);
        assertEquals("id2", identifier);

        identifier = queue.consumeCssIdentifier();
        assertEquals("class1", queue.queue);
        assertEquals(11, queue.pos);
        assertEquals("class1", identifier);

        identifier = queue.consumeCssIdentifier();
        assertEquals("class2", queue.queue);
        assertEquals(15, queue.pos);
        assertEquals("class2", identifier);

        identifier = queue.consumeCssIdentifier();
        assertEquals("", queue.queue);
        assertEquals(18, queue.pos);
        assertNull(identifier);
    }

    /**
     * Test consumeAttributeKey method.
     */
    @Test
    public void testConsumeAttributeKey() {
        String data = "name1 name2 class1 class2";
        TokenQueue queue = new TokenQueue(data);

        String key = queue.consumeAttributeKey();
        assertEquals("name1", queue.queue);
        assertEquals(6, queue.pos);
        assertEquals("name1", key);

        key = queue.consumeAttributeKey();
        assertEquals("name2", queue.queue);
        assertEquals(10, queue.pos);
        assertEquals("name2", key);

        key = queue.consumeAttributeKey();
        assertEquals("class1", queue.queue);
        assertEquals(14, queue.pos);
        assertEquals("class1", key);

        key = queue.consumeAttributeKey();
        assertEquals("class2", queue.queue);
        assertEquals(18, queue.pos);
        assertEquals("class2", key);

        key = queue.consumeAttributeKey();
        assertEquals("", queue.queue);
        assertEquals(21, queue.pos);
        assertNull(key);
    }

    /**
     * Test remainder method.
     */
    @Test
    public void testRemainder() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        String remainder = queue.remainder();
        assertEquals("", queue.queue);
        assertEquals(6, queue.pos);
        assertEquals("ef", remainder);

        remainder = queue.remainder();
        assertEquals("", queue.queue);
        assertEquals(7, queue.pos);
        assertNull(remainder);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("ef", queue.toString());
    }
}