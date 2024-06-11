package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());
        assertEquals(data, tokenQueue.toString());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("a");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testIsEmpty() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("a");
        assertFalse(tokenQueue.isEmpty());
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("x");
        tokenQueue.addFirst("y");
        assertEquals("yxabcdef", tokenQueue.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddFirst() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst(null);
    }

    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matches("abc"));
        assertFalse(tokenQueue.matches("def"));
    }

    @Test
    public void testMatchesCS() {
        String data = "abcABCdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchesCS("abc"));
        assertFalse(tokenQueue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        tokenQueue.addFirst("def");
        assertTrue(tokenQueue.matchesAny("abc", "def"));
        assertFalse(tokenQueue.matchesAny("ghi"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        tokenQueue.addFirst("def");
        assertTrue(tokenQueue.matchesAny(new Character[]{'a', 'b'}));
        assertFalse(tokenQueue.matchesAny(new Character[]{'g'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<abcd>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<abcd>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("<");
        tokenQueue.addFirst("ab");
        assertTrue(tokenQueue.matchChomp("<ab"));
        assertEquals("cd>", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abc   def";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst(" ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.addFirst("a");
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abc123def";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("a");
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.addFirst("1");
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("a");
        tokenQueue.addFirst("b");
        tokenQueue.addFirst("c");
        tokenQueue.advance();
        assertEquals("bcdef", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("a");
        assertEquals('a', tokenQueue.consume());
        assertEquals("bcdef", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnexpected() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
    }

    @Test
    public void testConsumeSequence() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        String seq = "abc";
        tokenQueue.consume(seq);
        assertEquals("def", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnexpectedSequence() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        String seq = "def";
        tokenQueue.consume(seq);
    }

    @Test
    public void testConsumeTo() {
        String data = "<abcd><efgh>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("<");
        String seq = "abcd>";
        assertEquals("efgh", tokenQueue.consumeTo(seq).toString());
        assertEquals(">", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "ABC<efgh>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("ABC");
        String seq = "efgh";
        assertEquals("", tokenQueue.consumeToIgnoreCase(seq).toString());
        assertEquals(">", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        String[] seq = {"def", "abc"};
        assertEquals("def", tokenQueue.consumeToAny(seq).toString());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "(ab)(cd)(ef)";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("(");
        String result = tokenQueue.chompBalanced('(', ')').toString();
        assertEquals("ab(cd)(ef)", tokenQueue.toString());
        assertEquals("ab", result);
    }

    @Test
    public void testUnescape() {
        String data = "\\\\\\\\";
        String result = TokenQueue.unescape(data);
        assertEquals("\\", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc   def";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abcdef", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "abc123def";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("abc", tokenQueue.consumeWord().toString());
        assertEquals("123def", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tagName attr=\"value\" otherattr=\"value2\" />";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName", tokenQueue.consumeTagName().toString());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<div class=\"class1 class2\" id=\"id1\">";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("div", tokenQueue.consumeElementSelector().toString());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".class1 .class2#id1";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("class1", tokenQueue.consumeCssIdentifier().toString());
        assertEquals(".class2#id1", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attr=\"value\" attr2=\"value2\"";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("attr", tokenQueue.consumeAttributeKey().toString());
        assertEquals("attr2=\"value2\"", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "abcddef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("def", tokenQueue.remainder().toString());
        assertEquals("", tokenQueue.toString());
    }
}