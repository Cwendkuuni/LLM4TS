package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.TokenQueue.ESC;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueuePeek() {
        String testData = "abcdef";
        TokenQueue queue = new TokenQueue(testData);
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueuePeekAfterEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testTokenQueueAddFirst() {
        String testData = "abcdef";
        TokenQueue queue = new TokenQueue(testData);
        queue.addFirst('X');
        assertEquals('X', queue.peek());

        queue.addFirst("XYZ");
        assertEquals("XYZ", queue.peek(3));
    }

    @Test
    public void testTokenQueueMatches() {
        String testData = "abcdef";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("efg"));
    }

    @Test
    public void testTokenQueueMatchesCS() {
        String testData = "abcdef";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchesCS("a"));
        assertTrue(queue.matchesCS("bc"));
        assertFalse(queue.matchesCS("efg"));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        String testData = "abcdef";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("bc", "ef"));
        assertFalse(queue.matchesAny("xyz", "abc"));
    }

    @Test
    public void testTokenQueueMatchesAnyChars() {
        String testData = "abcdef";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchesAny(new Character[]{'a', 'b'}));
        assertTrue(queue.matchesAny(new Character[]{'b', 'e'}));
        assertFalse(queue.matchesAny(new Character[]{'x', 'y'}));
    }

    @Test
    public void testTokenQueueMatchesStartTag() {
        String testData = "<abc def>";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume first <

        queue.advance();
        assertTrue(queue.matchesStartTag()); // consume <a
        assertFalse(queue.matchesStartTag()); // consume a, not <
    }

    @Test
    public void testTokenQueueMatchChomp() {
        String testData = "<abc def>";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchChomp("<"));
        assertEquals("abc def", queue.toString());
    }

    @Test
    public void testTokenQueueMatchesWhitespace() {
        String testData = "   abc def   ";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testTokenQueueMatchesWord() {
        String testData = "abc def";
        TokenQueue queue = new TokenQueue(testData);
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord()); // consume a
    }

    @Test
    public void testTokenQueueAdvance() {
        String testData = "abc def";
        TokenQueue queue = new TokenQueue(testData);
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
    }

    @Test
    public void testTokenQueueConsume() {
        String testData = "abc def";
        TokenQueue queue = new TokenQueue(testData);
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testTokenQueueConsumeString() {
        String testData = "abc def";
        String expected = "def";
        TokenQueue queue = new TokenQueue(testData);
        queue.consume("abc");
        assertEquals(expected, queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeUnmatched() {
        String testData = "abc def";
        TokenQueue queue = new TokenQueue(testData);
        queue.consume("xyz");
    }

    @Test
    public void testTokenQueueConsumeTo() {
        String testData = "abc def <xyz>";
        String expected = "abc def";
        TokenQueue queue = new TokenQueue(testData);
        queue.consumeTo("def <xyz>");
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testTokenQueueConsumeToIgnoreCase() {
        String testData = "ABC DEF <xyz>";
        String expected = "ABC";
        TokenQueue queue = new TokenQueue(testData);
        queue.consumeToIgnoreCase("def <xyz>");
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testTokenQueueConsumeToAny() {
        String testData = "abc def <xyz>";
        String expected = "xyz";
        TokenQueue queue = new TokenQueue(testData);
        queue.consumeToAny("xy", "z");
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testTokenQueueChompBalanced() {
        String testData = "((((()))))";
        String expected = "((((()";
        TokenQueue queue = new TokenQueue(testData);
        String result = queue.chompBalanced('(', ')');
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueUnescape() {
        String testData = "\\abc\\123\\def\\\\";
        String expected = "abc123def\\";
        String result = TokenQueue.unescape(testData);
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueConsumeWhitespace() {
        String testData = "   abc def   ";
        boolean consumed = false;
        TokenQueue queue = new TokenQueue(testData);
        queue.consumeWhitespace();
        queue.consumeWhitespace();
        queue.consumeWhitespace();
        consumed = queue.consumeWhitespace();
        assertFalse(consumed);
    }

    @Test
    public void testTokenQueueConsumeWord() {
        String testData = "abc def";
        String expected = "def";
        TokenQueue queue = new TokenQueue(testData);
        String result = queue.consumeWord();
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueConsumeTagName() {
        String testData = "<abc:def>";
        String expected = "def";
        TokenQueue queue = new TokenQueue(testData);
        String result = queue.consumeTagName();
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueConsumeElementSelector() {
        String testData = "<abc:def | ghi:jkl>";
        String expected = "def | ghi:jkl";
        TokenQueue queue = new TokenQueue(testData);
        String result = queue.consumeElementSelector();
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueConsumeCssIdentifier() {
        String testData = ".abc .def";
        String expected = ".def";
        TokenQueue queue = new TokenQueue(testData);
        String result = queue.consumeCssIdentifier();
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueConsumeAttributeKey() {
        String testData = "name='value'";
        String expected = "value";
        TokenQueue queue = new TokenQueue(testData);
        String result = queue.consumeAttributeKey();
        assertEquals(expected, result);
    }

    @Test
    public void testTokenQueueToString() {
        String testData = "abc def";
        TokenQueue queue = new TokenQueue(testData);
        assertEquals("def", queue.toString());
    }
}