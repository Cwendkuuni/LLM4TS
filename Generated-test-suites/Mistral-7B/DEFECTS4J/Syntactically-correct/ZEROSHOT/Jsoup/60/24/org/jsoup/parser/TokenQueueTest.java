package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.helper.StringUtil.isWhitespace;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueIsNotEmpty() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeekReturnsFirstCharacter() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekThrowsExceptionWhenQueueIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
        assertEquals('a', queue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC123");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("cd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("ef"));
    }

    @Test
    public void testMatchesAnyWithSingleCharacterSequence() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesAny('a'));
        assertFalse(queue.matchesAny('e'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume '<'
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<tag>123");
        assertTrue(queue.matchChomp("abc"));
        assertEquals('<', queue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc");
        assertTrue(queue.matchesWhitespace());
        queue.advance(); // consume whitespace
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
        queue.advance(); // consume 'a'
        assertTrue(queue.matchesWord());
        queue.advance(); // consume 'b'
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWhenQueueIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("abc<tag>123<tag2>efg");
        String consumed = queue.consumeTo("tag");
        assertEquals("abc<tag>", consumed);
        assertEquals('1', queue.peek());
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABC<TAG>123<TAG2>efg");
        String consumed = queue.consumeToIgnoreCase("tag");
        assertEquals("ABC<TAG>", consumed);
        assertEquals('1', queue.peek());
    }

    @Test
    public void testConsumeSequenceAny() {
        TokenQueue queue = new TokenQueue("abc<tag>123<tag2>efg");
        String consumed = queue.consumeToAny("tag", "tag2");
        assertEquals("abc<tag>", consumed);
        assertEquals('1', queue.peek());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("one (two) three", balanced);
        assertEquals(' four', queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(one \\(two\\) three\\) four";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("(one (two) three) four", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abcd   ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        String word = queue.consumeWord();
        assertEquals("abc", word);
        assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Page");
        String tagName = queue.consumeTagName();
        assertEquals("html", tagName);
        assertEquals("<head><title>My Page", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.myClass.myId:hover");
        String selector = queue.consumeElementSelector();
        assertEquals("div", selector);
        assertEquals(".myClass.myId:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: #myColor;");
        String identifier = queue.consumeCssIdentifier();
        assertEquals("myColor", identifier);
        assertEquals("color: #", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"myClass\"");
        String key = queue.consumeAttributeKey();
        assertEquals("class", key);
        assertEquals("\"myClass\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abc123");
        String remainder = queue.remainder();
        assertEquals("123", remainder);
        assertEquals("", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("123", queue.toString());
    }
}