package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesAny("abc", "def"));
        Assert.assertFalse(queue.matchesAny(new char[]{'a', 'b'}));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("abc"));
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.consume('a') == 'a');
        Assert.assertFalse(queue.consume("ab") == "ab");
        Assert.assertEquals("", queue.consumeTo(""));
        Assert.assertEquals("", queue.consumeToIgnoreCase(""));
        Assert.assertEquals("", queue.consumeToAny());
        Assert.assertEquals("", queue.chompTo(""));
        Assert.assertEquals("", queue.chompToIgnoreCase(""));
        Assert.assertEquals("", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.unescape(""));
        Assert.assertFalse(queue.consumeWhitespace());
        Assert.assertEquals("", queue.consumeWord());
        Assert.assertEquals("", queue.consumeTagName());
        Assert.assertEquals("", queue.consumeElementSelector());
        Assert.assertEquals("", queue.consumeCssIdentifier());
        Assert.assertEquals("", queue.consumeAttributeKey());
        Assert.assertEquals("", queue.remainder());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testSimpleQueue() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('a', queue.peek());
        Assert.assertTrue(queue.matches("abc"));
        Assert.assertTrue(queue.matchesCS("ABC"));
        Assert.assertTrue(queue.matchesAny("abc", "def"));
        Assert.assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertTrue(queue.matchChomp("abc"));
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals("bcdef", queue.toString());
        Assert.assertEquals("bcdef", queue.remainder());
        Assert.assertEquals("bcdef", queue.consumeTo(""));
        Assert.assertEquals("bcdef", queue.consumeToIgnoreCase(""));
        Assert.assertEquals("bcdef", queue.consumeToAny());
        Assert.assertEquals("bcdef", queue.chompTo(""));
        Assert.assertEquals("bcdef", queue.chompToIgnoreCase(""));
        Assert.assertEquals("bcdef", queue.chompBalanced('(', ')'));
        Assert.assertEquals("bcdef", queue.unescape("abcdef"));
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("b", queue.consumeWord());
        Assert.assertEquals("b", queue.consumeTagName());
        Assert.assertEquals("b", queue.consumeElementSelector());
        Assert.assertEquals("b", queue.consumeCssIdentifier());
        Assert.assertEquals("b", queue.consumeAttributeKey());
        Assert.assertEquals("cdef", queue.remainder());
        Assert.assertEquals("cdef", queue.toString());
    }

    @Test
    public void testComplexQueue() {
        TokenQueue queue = new TokenQueue("(<p class=\"hello world\">abc <b>def</b> ghi)</p>");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('(', queue.peek());
        Assert.assertTrue(queue.matches("(<p class"));
        Assert.assertTrue(queue.matchesCS("<P CLASS"));
        Assert.assertTrue(queue.matchesAny("(<p class", "></p>"));
        Assert.assertTrue(queue.matchesAny(new char[]{'(', '<'}));
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertTrue(queue.matchChomp("(<p class"));
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('(', queue.consume());
        Assert.assertEquals("<p class", queue.toString());
        Assert.assertEquals("<p class", queue.remainder());
        Assert.assertEquals("<p class", queue.consumeTo(""));
        Assert.assertEquals("<p class", queue.consumeToIgnoreCase(""));
        Assert.assertEquals("<p class", queue.consumeToAny());
        Assert.assertEquals("<p class", queue.chompTo(""));
        Assert.assertEquals("<p class", queue.chompToIgnoreCase(""));
        Assert.assertEquals("<p class", queue.chompBalanced('(', ')'));
        Assert.assertEquals("<p class", queue.unescape("(<p class"));
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("class", queue.consumeWord());
        Assert.assertEquals("class", queue.consumeTagName());
        Assert.assertEquals("class", queue.consumeElementSelector());
        Assert.assertEquals("class", queue.consumeCssIdentifier());
        Assert.assertEquals("hello world>", queue.consumeAttributeKey());
        Assert.assertEquals("abc ", queue.remainder());
        Assert.assertEquals("abc ", queue.toString());
        Assert.assertEquals("abc ", queue.consumeTo(""));
        Assert.assertEquals("abc ", queue.consumeToIgnoreCase(""));
        Assert.assertEquals("abc ", queue.consumeToAny());
        Assert.assertEquals("abc ", queue.chompTo(""));
        Assert.assertEquals("abc ", queue.chompToIgnoreCase(""));
        Assert.assertEquals("abc ", queue.chompBalanced('>', '>'));
        Assert.assertEquals("abc ", queue.unescape("<p classhello world>abc "));
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("b", queue.consumeWord());
        Assert.assertEquals("b", queue.consumeTagName());
        Assert.assertEquals("b", queue.consumeElementSelector());
        Assert.assertEquals("b", queue.consumeCssIdentifier());
        Assert.assertEquals("def", queue.consumeAttributeKey());
        Assert.assertEquals("ghi)", queue.remainder());
        Assert.assertEquals("ghi)", queue.toString());
        Assert.assertEquals("ghi)", queue.consumeTo(""));
        Assert.assertEquals("ghi)", queue.consumeToIgnoreCase(""));
        Assert.assertEquals("ghi)", queue.consumeToAny());
        Assert.assertEquals("ghi)", queue.chompTo(""));
        Assert.assertEquals("ghi)", queue.chompToIgnoreCase(""));
        Assert.assertEquals("ghi)", queue.chompBalanced('>', ')'));
        Assert.assertEquals("ghi)", queue.unescape("</p>ghi"));
        Assert.assertEquals(0, queue.remainingLength());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidQueue() {
        TokenQueue queue = new TokenQueue("(<p class=\"hello world\">abc <b>def</b> ghi></p>");
        queue.consume("abc");
        queue.consume("def");
        queue.consume("ghi");
    }
}