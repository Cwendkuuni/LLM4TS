package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testNonEmptyQueue() {
        TokenQueue queue = new TokenQueue("hello");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('h', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("hello");
        queue.addFirst('w');
        Assert.assertEquals('w', queue.peek());
        queue.advance();
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("hello");
        queue.addFirst("world");
        Assert.assertEquals('w', queue.peek());
        queue.advance();
        Assert.assertEquals('o', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matches("wc"));
        Assert.assertTrue(queue.matches("ll"));
        Assert.assertFalse(queue.matches("zz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("HELLO"));
        Assert.assertFalse(queue.matchesCS("zzz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny("lw", "or"));
        Assert.assertFalse(queue.matchesAny("zz", "abc"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny(new char[]{'l', 'w'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'a', 'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html>");
        Assert.assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html class='foo'>");
        Assert.assertTrue(queue.matchChomp("cla"));
        Assert.assertEquals("foo", queue.consume().toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   \t hello world");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesWord());
        queue.consumeWord();
        Assert.assertEquals('w', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnmatchedSequence() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consume("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToShortSequence() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consumeTo("ll");
    }

    @Test
    public void testConsumeToMatchingSequence() {
        TokenQueue queue = new TokenQueue("hello world");
        String consumed = queue.consumeTo("ll");
        Assert.assertEquals("hell", consumed);
        Assert.assertEquals('o', queue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Hello World");
        String consumed = queue.consumeToIgnoreCase("HELLO");
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals('W', queue.peek());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("hello world");
        String consumed = queue.consumeToAny("ll", "or");
        Assert.assertEquals("hell", consumed);
        Assert.assertEquals('o', queue.peek());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(<tag> (<subtag> (<subsubtag>)))>");
        String expected = "<tag> (<subtag> (<subsubtag>))";
        String result = queue.chompBalanced('(', ')');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String input = "\\<tag\\> \\<subtag\\> \\&amp; \\&lt; \\&gt;";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals("<tag><subtag> & < >", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   \t hello world");
        boolean consumed = queue.consumeWhitespace();
        Assert.assertTrue(consumed);
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        String consumed = queue.consumeWord();
        Assert.assertEquals("hello", consumed);
        Assert.assertEquals('w', queue.peek());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name='value'>");
        String consumed = queue.consumeTagName();
        Assert.assertEquals("tag", consumed);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class:hover");
        String consumed = queue.consumeElementSelector();
        Assert.assertEquals("div", consumed);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red;");
        String consumed = queue.consumeCssIdentifier();
        Assert.assertEquals("color", consumed);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='id' class='class'");
        String consumed = queue.consumeAttributeKey();
        Assert.assertEquals("id", consumed);
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello world");
        String remainder = queue.remainder();
        Assert.assertEquals(" world", remainder);
    }
}