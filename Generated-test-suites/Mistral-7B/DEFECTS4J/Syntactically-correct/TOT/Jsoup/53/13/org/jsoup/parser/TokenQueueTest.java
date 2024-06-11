package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeekAndAddFirst() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.peek());
        queue.addFirst('x');
        Assert.assertEquals('x', queue.peek());
        queue.addFirst('y');
        Assert.assertEquals('y', queue.peek());
        queue.consume(); // consume 'y'
        Assert.assertEquals('x', queue.peek());
        queue.consume(); // consume 'x'
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testMatchesAndAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertEquals('c', queue.consume()); // consume 'c'
        Assert.assertFalse(queue.matches("ab")); // should not match after consuming 'c'
        Assert.assertFalse(queue.matches("cd")); // should not match
        queue.advance(); // move pos to consume 'd'
        Assert.assertFalse(queue.matches("cd")); // should not match
        Assert.assertEquals('e', queue.consume()); // consume 'e'
        Assert.assertEquals('f', queue.consume()); // consume 'f'
        Assert.assertFalse(queue.isEmpty()); // queue should be empty, but it's not, since we haven't checked it
        Assert.assertEquals("", queue.toString()); // queue should be empty after consuming all characters
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.matchChomp("ab")); // should not match
        Assert.assertTrue(queue.matchChomp("cd")); // should match
        Assert.assertEquals('f', queue.consume()); // consume 'f'
        Assert.assertFalse(queue.isEmpty()); // queue should be empty, but it's not, since we haven't checked it
        Assert.assertEquals("", queue.toString()); // queue should be empty after consuming all characters
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeFail() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz"); // should throw IllegalStateException
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        String consumed = queue.consumeTo("cd");
        Assert.assertEquals("abc", consumed); // "cd" was not found, but the longest matching substring was consumed
        Assert.assertEquals('f', queue.consume()); // consume 'f'
        Assert.assertEquals("", queue.toString()); // queue should be empty after consuming all characters
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        Assert.assertEquals("((()))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString()); // queue should be empty after consuming all characters
        queue = new TokenQueue("(one (two) three) four");
        Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        Assert.assertEquals(" four", queue.toString()); // queue should not be empty after consuming all characters
    }

    @Test
    public void testUnescape() {
        String in = "\\(\\)";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("()", out);
    }
}