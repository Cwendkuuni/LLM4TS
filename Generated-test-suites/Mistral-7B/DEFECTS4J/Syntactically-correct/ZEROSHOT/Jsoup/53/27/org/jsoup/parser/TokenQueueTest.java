package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests for TokenQueue class
 *
 * @author [Your Name]
 */
public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueNotEmpty() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
        assertEquals('a', queue.peek());
    }

    @Test
    public void testTokenQueueAddFirst() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        queue.addFirst("xyz");
        assertEquals('x', queue.peek());
        assertEquals("xyzabcdef", queue.toString());
    }

    @Test
    public void testTokenQueueMatches() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matchesIgnoreCase("Ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("ef"));
        assertFalse(queue.matches("gg"));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        String data = "abcabcab caBcaBcaB";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("ab", "AB"));
        assertTrue(queue.matchesAny("bc", "CA"));
        assertTrue(queue.matchesAny("ca", "B"));
        assertTrue(queue.matchesAny("b", "b"));
        assertFalse(queue.matchesAny("dd"));
    }

    @Test
    public void testTokenQueueMatchChomp() {
        String data = "abcabcab caBcaBcaB";
        TokenQueue queue = new TokenQueue(data);
        queue.matchChomp("ab");
        assertEquals("cabcabcab caBcaBcaB", queue.toString());
        queue.matchChomp("AB");
        assertEquals("cabcabcab caBcaB", queue.toString());
        queue.matchChomp("ca");
        assertEquals("cabcabcab caB", queue.toString());
        queue.matchChomp("B");
        assertEquals("cabcabcab cA", queue.toString());
    }

    @Test
    public void testTokenQueueMatchesWhitespace() {
        String data = "   abc   def  ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testTokenQueueMatchesWord() {
        String data = "abcd123efgh";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("ab"));
        assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeWithMismatchedSequence() {
        String data = "abcd123efgh";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("123");
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeWithInsufficientSequence() {
        String data = "abcd123efgh";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("12");
    }

    @Test
    public void testTokenQueueConsumeTo() {
        String data = "abcd123efgh";
        TokenQueue queue = new TokenQueue(data);
        String result = queue.consumeTo("123");
        assertEquals("123", result);
        assertEquals("efgh", queue.toString());
    }

    @Test
    public void testTokenQueueChompBalanced() {
        String data = "((())())";
        TokenQueue queue = new TokenQueue(data);
        String result = queue.chompBalanced('(', ')');
        assertEquals("()()", result);
        assertEquals("()()", queue.toString());
    }

    @Test
    public void testTokenQueueUnescape() {
        String data = "\\(abc\\def\\)";
        String result = TokenQueue.unescape(data);
        assertEquals("(abcdef)", result);
    }
}