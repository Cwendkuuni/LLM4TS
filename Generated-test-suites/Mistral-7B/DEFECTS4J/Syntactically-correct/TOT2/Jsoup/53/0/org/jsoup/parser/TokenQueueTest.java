package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TokenQueueTest.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.queue);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "example data";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data.charAt(0), tokenQueue.peek());
        tokenQueue.advance();
        assertEquals(data.charAt(1), tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.queue.charAt(0));
        assertEquals('A', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "example";
        assertTrue(tokenQueue.matches(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matches(seq));
    }

    @Test
    public void testMatchesCS() {
        String data = "Example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "Example";
        assertTrue(tokenQueue.matchesCS(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesCS(seq));
    }

    @Test
    public void testMatchesAny() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"a", "b", "c"};
        assertFalse(tokenQueue.matchesAny(seq));
        tokenQueue.addFirst('a');
        assertTrue(tokenQueue.matchesAny(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "sequence";
        tokenQueue.matchChomp(seq);
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithMismatchedSequence() {
        String data = "data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "seq";
        tokenQueue.matchChomp(seq);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\nexample data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals(data.charAt(1), tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data.charAt(0), tokenQueue.consume());
        assertEquals(data.charAt(1), tokenQueue.peek());
    }

    @Test
    public void testConsumeString() {
        String data = "example data";
        String seq = "example";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(seq);
        assertEquals("data", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(new String[]{"seq"});
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatchedSequence() {
        String data = "data";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(new String[]{"seq"});
    }

    @Test
    public void testConsumeTo() {
        String data = "example data";
        String seq = "example";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(seq, tokenQueue.consumeTo(seq).toString());
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "Example data";
        String seq = "example";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(seq, tokenQueue.consumeToIgnoreCase(seq).toString());
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"seq1", "seq2"};
        assertEquals("example", tokenQueue.consumeToAny(seq).toString());
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "(example (two) three) four";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompBalanced('(', ')').toString();
        assertEquals("example (two) three", result);
        assertEquals("four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\\\example\\\\\\\\\\\\\\\\";
        String result = TokenQueue.unescape(data);
        assertEquals("example\\\\\\\\\\\\\\\\", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " \t\n example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("example data", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("example", tokenQueue.consumeWord().toString());
        assertEquals(" data", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tagName attribute=\"value\" />";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName", tokenQueue.consumeTagName().toString());
        assertEquals(" attribute=\"value\" />", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div.class:hover";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("div", tokenQueue.consumeElementSelector().toString());
        assertEquals(".class:hover", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id:class#name";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("id", tokenQueue.consumeCssIdentifier().toString());
        assertEquals(":class#name", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attribute=\"value\"";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("attribute", tokenQueue.consumeAttributeKey().toString());
        assertEquals("=\"value\"", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("data", tokenQueue.remainder().toString());
        assertEquals("", tokenQueue.toString());
    }
}