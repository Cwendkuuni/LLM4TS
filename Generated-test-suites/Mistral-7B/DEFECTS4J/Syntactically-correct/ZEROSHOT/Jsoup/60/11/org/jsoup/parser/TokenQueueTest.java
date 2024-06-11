package org.jsoup.parser.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

/**
 * Test class for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        String input = "test";
        TokenQueue queue = new TokenQueue(input);
        assertNotNull(queue);
        assertTrue(queue.isEmpty());
        assertEquals(input, queue.queue);
    }

    @Test
    public void testIsEmpty() {
        String input = "test";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.isEmpty());
        queue.addFirst('a');
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String input = "test\\a\\b";
        TokenQueue queue = new TokenQueue(input);
        assertEquals(0, queue.peek());
        queue.advance();
        assertEquals('t', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekWhenEmpty() {
        String input = "";
        TokenQueue queue = new TokenQueue(input);
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        String input = "test";
        TokenQueue queue = new TokenQueue(input);
        queue.addFirst('a');
        assertEquals("a" + input.substring(1), queue.queue);
        assertEquals('a', queue.peek());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        String input = "test\\tmatch\\test";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matches("test"));
        assertTrue(queue.matches("match"));
        assertFalse(queue.matches("mismatch"));
    }

    @Test
    public void testMatchesCS() {
        String input = "TestTokenQueue";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchesCS("Test"));
        assertTrue(queue.matchesCS("TokenQueue"));
        assertFalse(queue.matchesCS("TokenQueueTest"));
    }

    @Test
    public void testMatchesAny() {
        String input = "test\\tmatch\\test";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchesAny("match", "test"));
        assertFalse(queue.matchesAny("mismatch", "test"));
        assertTrue(queue.matchesAny("a", "test"));
    }

    @Test
    public void testMatchesAnyChar() {
        String input = "test\\tmatch\\test";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchesAny(new char[]{'t', 'm'}));
        assertFalse(queue.matchesAny(new char[]{'m', 't'}));
        assertTrue(queue.matchesAny(new char[]{'t'}));
    }

    @Test
    public void testMatchesStartTag() {
        String input = "<test<tag>test>";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String input = "<test<tag>test>";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchChomp("<tag>"));
        assertFalse(queue.isEmpty());
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchesWhitespace() {
        String input = " test ";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String input = "testWordtest";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String input = "test\\tmatch\\test";
        TokenQueue queue = new TokenQueue(input);
        queue.advance();
        assertEquals('t', queue.peek());
    }

    @Test
    public void testConsume() {
        String input = "test\\tmatch\\test";
        TokenQueue queue = new TokenQueue(input);
        assertEquals('t', queue.consume());
        assertEquals('e', queue.consume());
        assertFalse(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWhenEmpty() {
        String input = "";
        TokenQueue queue = new TokenQueue(input);
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        String input = "test<tag>matchtest";
        TokenQueue queue = new TokenQueue(input);
        queue.consume("test<tag>");
        assertEquals("matchtest", queue.queue);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        String input = "Test<TAG>matchTest";
        TokenQueue queue = new TokenQueue(input);
        queue.consumeToIgnoreCase("testTAG");
        assertEquals("matchTest", queue.queue);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeAny() {
        String input = "test\\tmatch\\test";
        TokenQueue queue = new TokenQueue(input);
        String consumed = queue.consumeToAny("m", "t");
        assertEquals("test" + consumed, queue.queue);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        String input = "(one (two) three) four";
        TokenQueue queue = new TokenQueue(input);
        String result = queue.chompBalanced('(', ')');
        assertEquals("one (two) three", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testUnescape() {
        String input = "\\test\\\\\\";
        String result = TokenQueue.unescape(input);
        assertEquals("test\\", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String input = " test ";
        TokenQueue queue = new TokenQueue(input);
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        String input = "testWordtest";
        TokenQueue queue = new TokenQueue(input);
        String result = queue.consumeWord();
        assertEquals("test", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        String input = "<testTagName>test";
        TokenQueue queue = new TokenQueue(input);
        String result = queue.consumeTagName();
        assertEquals("testTagName", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        String input = "test|tagName|test";
        TokenQueue queue = new TokenQueue(input);
        String result = queue.consumeElementSelector();
        assertEquals("test|tagName", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String input = "test#identifier test";
        TokenQueue queue = new TokenQueue(input);
        String result = queue.consumeCssIdentifier();
        assertEquals("identifier", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        String input = "test[attributeKey] test";
        TokenQueue queue = new TokenQueue(input);
        String result = queue.consumeAttributeKey();
        assertEquals("attributeKey", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        String input = "test test";
        TokenQueue queue = new TokenQueue(input);
        queue.advance();
        queue.advance();
        String result = queue.remainder();
        assertEquals("", result);
        assertFalse(queue.isEmpty());

        input = "test test";
        queue = new TokenQueue(input);
        result = queue.remainder();
        assertEquals(" test", result);
        assertFalse(queue.isEmpty());
    }
}