package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Unit tests for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        assertEquals("", queue.toString());
    }

    @Test
    public void testQueueWithData() {
        TokenQueue queue = new TokenQueue("abcd");
        assertFalse(queue.isEmpty());
        assertEquals("abcd", queue.toString());
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals("bcde", queue.toString());
        assertEquals('b', queue.peek());
        assertEquals('b', queue.consume());
        assertEquals("cde", queue.toString());
        assertEquals('c', queue.peek());
        assertEquals('c', queue.consume());
        assertEquals("de", queue.toString());
        assertEquals('d', queue.peek());
        assertEquals('d', queue.consume());
        assertEquals("e", queue.toString());
        assertEquals('e', queue.peek());
        assertEquals('e', queue.consume());
        assertTrue(queue.isEmpty());
        assertEquals("", queue.toString());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.addFirst('x');
        assertEquals("xabcd", queue.toString());
        assertEquals('x', queue.peek());
        assertEquals('x', queue.consume());
        assertEquals("abcd", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("abcd"));
        assertTrue(queue.matches("abc"));
        assertFalse(queue.matches("efg"));
        assertTrue(queue.matches("d"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCD");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("ABCD"));
        assertFalse(queue.matchesCS("efg"));
        assertTrue(queue.matchesCS("D"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("a", "b", "d"));
        assertTrue(queue.matchesAny("a", "d"));
        assertTrue(queue.matchesAny("d", "b"));
        assertTrue(queue.matchesAny("a", "d", "e"));
        assertFalse(queue.matchesAny("e", "f"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'b', 'd'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'd'}));
        assertTrue(queue.matchesAny(new char[]{'d', 'b'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'd', 'e'}));
        assertFalse(queue.matchesAny(new char[]{'e', 'f'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag attr=\"value\">content</tag>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.matchChomp("ab");
        assertEquals("cd", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abcd   ");
        assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        assertEquals("abcd", queue.toString());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals('a', queue.consume());
        assertEquals("bcde", queue.toString());
        assertEquals('b', queue.consume());
        assertEquals("cde", queue.toString());
        assertEquals('c', queue.consume());
        assertEquals("de", queue.toString());
        assertEquals('d', queue.consume());
        assertEquals("e", queue.toString());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeChomp() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("four", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("123<tag attr=\"value\">content</tag>456");
        assertEquals("123", queue.consumeTo("123").toString());
        assertEquals("<tag attr=\"value\">content</tag>", queue.toString());
        assertEquals("tag", queue.consumeTagName().toString());
        assertEquals("value", queue.consumeCssIdentifier().toString());
        assertEquals(">", queue.consume().toString());
        assertEquals("content", queue.consumeToIgnoreCase("content").toString());
        assertEquals(">", queue.consume().toString());
        assertEquals("456", queue.consumeToAny("4", "5", "6").toString());
        assertTrue(queue.isEmpty());
    }
}