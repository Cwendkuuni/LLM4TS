package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("test"));
    }

    @Test
    public void testBasicOperations() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        queue.addFirst('x');
        Assert.assertEquals('x', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("123abcDEF456");
        Assert.assertTrue(queue.matches("1"));
        Assert.assertTrue(queue.matches("2"));
        Assert.assertTrue(queue.matches("3"));
        Assert.assertTrue(queue.matches("a"));
        Assert.assertTrue(queue.matches("b"));
        Assert.assertTrue(queue.matches("c"));
        Assert.assertTrue(queue.matches("d"));
        Assert.assertTrue(queue.matches("e"));
        Assert.assertTrue(queue.matches("F"));
        Assert.assertTrue(queue.matches("F")); // case insensitive
        Assert.assertFalse(queue.matches("G"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("123abcDEF456");
        Assert.assertTrue(queue.matchesCS("1"));
        Assert.assertTrue(queue.matchesCS("2"));
        Assert.assertTrue(queue.matchesCS("3"));
        Assert.assertFalse(queue.matchesCS("a")); // case sensitive
        Assert.assertTrue(queue.matchesCS("d"));
        Assert.assertFalse(queue.matchesCS("e")); // case sensitive
        Assert.assertFalse(queue.matchesCS("f"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("123abcDEF456");
        Assert.assertTrue(queue.matchesAny("1", "2"));
        Assert.assertTrue(queue.matchesAny("1", "a"));
        Assert.assertTrue(queue.matchesAny("2", "c"));
        Assert.assertTrue(queue.matchesAny("d", "e"));
        Assert.assertFalse(queue.matchesAny("G"));
        Assert.assertTrue(queue.matchesAny("1", "2", "3"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("123abcDEF456");
        Assert.assertTrue(queue.matchesAny(new char[]{'1', '2'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'1', 'a'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'2', 'c'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'d', 'e'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'G'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'1', '2', '3'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // already consumed <html>
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("123abcDEF456");
        Assert.assertFalse(queue.matchChomp("1"));
        Assert.assertFalse(queue.matchChomp("2"));
        Assert.assertFalse(queue.matchChomp("3"));
        Assert.assertTrue(queue.matchChomp("a"));
        Assert.assertTrue(queue.matchChomp("b"));
        Assert.assertTrue(queue.matchChomp("c"));
        Assert.assertTrue(queue.matchChomp("d"));
        Assert.assertTrue(queue.matchChomp("e"));
        Assert.assertTrue(queue.matchChomp("F"));
        Assert.assertFalse(queue.matchChomp("G"));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.consumeWhitespace()); // consume whitespace
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123def");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('a', queue.consumeWord());
        Assert.assertEquals("bc", queue.consumeWord());
        Assert.assertEquals("123", queue.consumeWord());
        Assert.assertEquals("def", queue.consumeWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("123<abc>def<xyz>");
        Assert.assertEquals("123", queue.consumeTo("123"));
        Assert.assertEquals("abc", queue.consumeTo("abc"));
        Assert.assertEquals("def", queue.consumeTo("def"));
        Assert.assertEquals("xyz", queue.consumeTo("xyz"));
        Assert.assertEquals("", queue.consumeTo("xyz"));
        Assert.assertEquals("", queue.consumeTo("xyzx"));
        Assert.assertEquals("", queue.consumeTo("x"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("123<ABC>Def<XYZ>");
        Assert.assertEquals("123", queue.consumeToIgnoreCase("123"));
        Assert.assertEquals("ABC", queue.consumeToIgnoreCase("ABC"));
        Assert.assertEquals("Def", queue.consumeToIgnoreCase("def"));
        Assert.assertEquals("XYZ", queue.consumeToIgnoreCase("XYZ"));
        Assert.assertEquals("", queue.consumeToIgnoreCase("XYZ"));
        Assert.assertEquals("", queue.consumeToIgnoreCase("XYZx"));
        Assert.assertEquals("", queue.consumeToIgnoreCase("x"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123<abc>DEF<xyz>");
        Assert.assertEquals("123", queue.consumeToAny("123"));
        Assert.assertEquals("abc", queue.consumeToAny("abc"));
        Assert.assertEquals("DEF", queue.consumeToAny("DEF"));
        Assert.assertEquals("xyz", queue.consumeToAny("xyz"));
        Assert.assertEquals("", queue.consumeToAny("xyz"));
        Assert.assertEquals("", queue.consumeToAny("xyzx"));
        Assert.assertEquals("", queue.consumeToAny("x"));
        Assert.assertEquals("", queue.consumeToAny("123")); // already consumed
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        Assert.assertEquals("((())", queue.chompBalanced('(', ')'));
        Assert.assertEquals("()()", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.chompBalanced('(', '('));
        Assert.assertEquals("", queue.chompBalanced('(', 'z'));
        Assert.assertEquals("", queue.chompBalanced(')
', '('));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        TokenQueue queue = new TokenQueue("123<abc>DEF<xyz>");
        queue.consume("xyz"); // not matched
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToInvalidSequence() {
        TokenQueue queue = new TokenQueue("123<abc>DEF<xyz>");
        queue.consumeTo("xyz"); // not matched
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToInvalidSequenceIgnoreCase() {
        TokenQueue queue = new TokenQueue("123<ABC>DEF<XYZ>");
        queue.consumeToIgnoreCase("XYZ"); // not matched
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToAnyInvalidSequence() {
        TokenQueue queue = new TokenQueue("123<abc>DEF<xyz>");
        queue.consumeToAny("XYZ"); // not matched
    }
}