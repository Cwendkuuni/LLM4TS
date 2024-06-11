package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.helper.StringUtil.isWhitespace;
import static org.jsoup.helper.Validate.notNull;

public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        notNull(tokenQueue);
        assertEquals(data, tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "abc";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('a', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.peek();
    }

    @Test
    public void testAddFirst() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('x');
        assertEquals('x', tokenQueue.queue.charAt(0));
        assertEquals('a', tokenQueue.queue.charAt(1));
        assertEquals(1, tokenQueue.pos);

        tokenQueue.addFirst("def");
        assertEquals("def", tokenQueue.queue.substring(0, 4));
        assertEquals(4, tokenQueue.pos);
    }

    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matches("ab"));
        assertTrue(tokenQueue.matches("def"));
        assertFalse(tokenQueue.matches("xyz"));
        assertFalse(tokenQueue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        String data = "ABCdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesCS("AB"));
        assertFalse(tokenQueue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesAny("ab", "xy"));
        assertFalse(tokenQueue.matchesAny("xyz"));

        data = "abc\\";
        tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesAny("ab", "\\"));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<abc>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());

        data = "abc";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.matchChomp("ab");
        assertEquals("cdef", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals('b', tokenQueue.queue.charAt(tokenQueue.pos));

        tokenQueue.advance();
        assertEquals('c', tokenQueue.queue.charAt(tokenQueue.pos));

        tokenQueue.advance();
        assertEquals(-1, tokenQueue.queue.length());
    }

    @Test
    public void testConsume() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertEquals(-1, tokenQueue.queue.length());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume();
    }

    @Test
    public void testConsumeTo() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("abc", tokenQueue.consumeTo("ab"));
        assertEquals("def", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);

        assertEquals("def", tokenQueue.consumeToIgnoreCase("ef"));
        assertEquals("def", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);

        assertEquals("ab", tokenQueue.consumeToAny("ab", "xy"));
        assertEquals("cdef", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
    }

    @Test
    public void testChompBalanced() {
        String data = "((())())";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("(())()", tokenQueue.chompBalanced('(', ')'));
        assertEquals("()", tokenQueue.queue);
        assertEquals(4, tokenQueue.pos);

        data = "((())())";
        tokenQueue = new TokenQueue(data);
        assertEquals("(())()", tokenQueue.chompBalanced('(', ')'));
        assertEquals("()", tokenQueue.queue);
        assertEquals(4, tokenQueue.pos);
    }

    @Test
    public void testUnescape() {
        String data = "\\\\abc\\";
        String expected = "abc\\";
        assertEquals(expected, TokenQueue.unescape(data));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals('a', tokenQueue.queue.charAt(tokenQueue.pos));
    }

    @Test
    public void testConsumeWord() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("abc", tokenQueue.consumeWord());
        assertEquals("def", tokenQueue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag name attribute='value'>content</tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tag", tokenQueue.consumeTagName());
        assertEquals("name", tokenQueue.consumeTagName());
        assertEquals("-", tokenQueue.queue.charAt(tokenQueue.pos));
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tag name attribute='value' class='class'>content</tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tag", tokenQueue.consumeElementSelector());
        assertEquals("name", tokenQueue.consumeElementSelector());
        assertEquals(".", tokenQueue.queue.charAt(tokenQueue.pos));
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".class:hover:focus";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("class", tokenQueue.consumeCssIdentifier());
        assertEquals(":", tokenQueue.queue.charAt(tokenQueue.pos));
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "tag [attribute='value']";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("attribute", tokenQueue.consumeAttributeKey());
        assertEquals("'", tokenQueue.queue.charAt(tokenQueue.pos));
    }

    @Test
    public void testRemainder() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("def", tokenQueue.remainder());
    }
}