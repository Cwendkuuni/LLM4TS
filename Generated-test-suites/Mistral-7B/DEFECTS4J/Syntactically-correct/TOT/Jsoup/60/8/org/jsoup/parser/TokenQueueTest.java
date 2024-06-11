package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testNonEmptyQueue() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('h', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        new TokenQueue(null);
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst("hello");
        Assert.assertEquals("hellohello world", queue.toString());
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('h');
        Assert.assertEquals("hhello world", queue.toString());
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matches("ll"));
        Assert.assertFalse(queue.matches("ww"));
        Assert.assertFalse(queue.matches("oo"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("Hello"));
        Assert.assertTrue(queue.matchesCS("World"));
        Assert.assertFalse(queue.matchesCS("hello"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("012345");
        Assert.assertTrue(queue.matchesAny("02", "23", "45"));
        Assert.assertFalse(queue.matchesAny("00"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(queue.matchesStartTag());
        queue.advance();
        Assert.assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("1<tag attribute='value'>2<tag attribute='value'>3");
        Assert.assertTrue(queue.matchChomp("1<tag"));
        Assert.assertEquals(" attribute='value'>2<tag", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world  ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertTrue(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals(' ', queue.consume());
        Assert.assertEquals('w', queue.consume());
        Assert.assertEquals('r', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('\0', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consume(5));
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("1<tag attribute='value'>2<tag attribute='value'>3");
        Assert.assertEquals("1<tag attribute='value'>", queue.consumeTo("1<tag attribute='"));
        Assert.assertEquals("attribute='value'>2<tag attribute='value'", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("1<TAG attribute='VALUE'>2<TAG attribute='VALUE'>3");
        Assert.assertEquals("1<TAG", queue.consumeToIgnoreCase("1<TAG"));
        Assert.assertEquals("attribute='VALUE'>2<TAG attribute='VALUE'", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("1<tag attribute='value1'>2<tag attribute='value2'>3");
        Assert.assertEquals("1<tag attribute='value", queue.consumeToAny("value1", "value2"));
        Assert.assertEquals("3", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("\\abc\\\\xyz\\\\\\\\", TokenQueue.unescape("\\abc\\xyz\\\\\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("helloworld", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertEquals("world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("<head><title>Test</title></head><body>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class:first-child");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals(".class:first-child", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color:red; background:#ff0000;");
        Assert.assertEquals("color", queue.consumeCssIdentifier());
        Assert.assertEquals(":red;\nbackground:#ff0000;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='red' id='test'");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("id='test'", queue.toString());
    }
}