package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueNotEmpty() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertFalse(tokenQueue.isEmpty());
        Assert.assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testTokenQueuePeekAndConsume() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals('a', tokenQueue.peek());
        Assert.assertEquals('a', tokenQueue.consume());
        Assert.assertEquals('b', tokenQueue.peek());
        Assert.assertEquals('b', tokenQueue.consume());
        Assert.assertEquals('c', tokenQueue.peek());
        Assert.assertEquals('c', tokenQueue.consume());
        Assert.assertEquals('d', tokenQueue.peek());
        Assert.assertEquals('d', tokenQueue.consume());
        Assert.assertEquals('e', tokenQueue.peek());
        Assert.assertEquals('e', tokenQueue.consume());
        Assert.assertEquals('f', tokenQueue.peek());
        Assert.assertEquals('f', tokenQueue.consume());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueAddFirst() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('x');
        Assert.assertEquals('x', tokenQueue.peek());
        Assert.assertEquals('x', tokenQueue.consume());
        Assert.assertEquals('a', tokenQueue.peek());
        Assert.assertEquals('a', tokenQueue.consume());
        // ...
    }

    @Test
    public void testTokenQueueMatches() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matches("a"));
        Assert.assertTrue(tokenQueue.matches("ab"));
        Assert.assertTrue(tokenQueue.matches("abc"));
        Assert.assertFalse(tokenQueue.matches("abcd"));
        Assert.assertFalse(tokenQueue.matches("xyz"));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesAny("a", "b", "c"));
        Assert.assertFalse(tokenQueue.matchesAny("x", "y", "z"));
        Assert.assertTrue(tokenQueue.matchesAny("a", "e", "f"));
        Assert.assertFalse(tokenQueue.matchesAny("d", "e", "f"));
        Assert.assertTrue(tokenQueue.matchesAny("a", "b", "c", "d"));
    }

    @Test
    public void testTokenQueueMatchChomp() {
        String data = "abcabc";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchChomp("ab"));
        Assert.assertEquals('c', tokenQueue.peek());
        Assert.assertFalse(tokenQueue.matchChomp("ab"));
        Assert.assertEquals('c', tokenQueue.peek());
        Assert.assertTrue(tokenQueue.matchChomp("c"));
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueWhitespaceAndWord() {
        String data = "   abc  def";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertEquals("abc", tokenQueue.consumeWord());
        Assert.assertFalse(tokenQueue.matchesWord());
        Assert.assertEquals("def", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeFail() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("def");
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeToFail() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consumeTo("xyz");
    }

    @Test
    public void testTokenQueueConsumeTo() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTo("def");
        Assert.assertEquals("abc", result);
        Assert.assertEquals("ef", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueConsumeToIgnoreCase() {
        String data = "abcDef";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToIgnoreCase("Def");
        Assert.assertEquals("abc", result);
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase("xyz"));
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueConsumeToAny() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToAny("a", "b", "c", "d");
        Assert.assertEquals("abc", result);
        Assert.assertEquals("def", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueChompBalanced() {
        String data = "((())())";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("((())", result);
        Assert.assertEquals(")", tokenQueue.peek());
        Assert.assertEquals(")", tokenQueue.consume());
        Assert.assertTrue(tokenQueue.isEmpty());
    }
}