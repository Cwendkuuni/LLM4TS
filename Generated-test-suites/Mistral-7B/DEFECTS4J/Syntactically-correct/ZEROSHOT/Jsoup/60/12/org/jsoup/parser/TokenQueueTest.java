package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeekAndConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals('h', queue.peek());
        assertEquals('h', queue.consume());
        assertEquals('e', queue.peek());
        assertEquals('e', queue.consume());
        assertEquals('l', queue.peek());
        assertEquals('l', queue.consume());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('x');
        assertEquals('x', queue.consume());
        assertEquals('h', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matches("he"));
        assertTrue(queue.matches("ll"));
        assertFalse(queue.matches("foobar"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesCS("he"));
        assertTrue(queue.matchesCS("Hello"));
        assertFalse(queue.matchesCS("foobar"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesAny("he", "ll"));
        assertFalse(queue.matchesAny("foobar"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesAny('h', 'e'));
        assertTrue(queue.matchesAny('l', 'l'));
        assertFalse(queue.matchesAny('f', 'o'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<div class=\"foo\">");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\">");
        assertTrue(queue.matchChomp("class"));
        assertEquals(3, queue.remainingLength());
        assertFalse(queue.matchChomp("class"));
        assertEquals("=\"foo\" id=\"bar\">", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesWord());
        assertEquals('h', queue.consume());
        assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNotFound() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consume("foobar");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNotFoundEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("foobar");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\">");
        assertEquals("class=\"foo\"", queue.consumeTo("class"));
        assertEquals(" id=\"bar\">", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("class=\"FOO\" id=\"bar\">");
        assertEquals("class=\"FOO\"", queue.consumeToIgnoreCase("class"));
        assertEquals(" id=\"bar\">", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\">");
        assertEquals("class=\"foo\"", queue.consumeToAny("class", "foo"));
        assertEquals(" id=\"bar\">", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        assertEquals("abc\\def", TokenQueue.unescape("abc\\def"));
        assertEquals("abcdef", TokenQueue.unescape("abc\\def\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.consumeWord());
        assertEquals(" world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<div class=\"foo\">");
        assertEquals("div", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.foo.bar:hover");
        assertEquals("div.foo.bar", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: green;");
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals(" background-color", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("style=\"color: red;\" class=\"foo bar\"");
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals(" \"class=\"foo bar\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\">");
        assertEquals(" id=\"bar\">", queue.remainder());
        queue.consumeTo("id");
        assertEquals(">", queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\">");
        assertEquals(" class=\"foo\" id=\"bar\">", queue.toString());
        queue.consumeTo("id");
        assertEquals(">", queue.toString());
    }
}