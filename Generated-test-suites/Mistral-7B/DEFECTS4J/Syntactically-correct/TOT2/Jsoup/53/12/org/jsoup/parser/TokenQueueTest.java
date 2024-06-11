package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("abcdef", queue.queue);
        Assert.assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.isEmpty());

        data = "abcdef";
        queue = new TokenQueue(data);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekOnEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        Assert.assertEquals('x', queue.queue.charAt(0));
        Assert.assertEquals('a', queue.queue.charAt(1));
        Assert.assertEquals(1, queue.pos);

        queue.addFirst("123");
        Assert.assertEquals("123a", queue.queue);
        Assert.assertEquals('1', queue.queue.charAt(0));
        Assert.assertEquals(3, queue.pos);
    }

    @Test
    public void testMatches() {
        String data = "abcabc";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matches("bc"));
        Assert.assertEquals(2, queue.pos);

        Assert.assertFalse(queue.matches("def"));
        Assert.assertEquals(5, queue.pos);
    }

    @Test
    public void testMatchesCS() {
        String data = "abcABC";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesCS("ABC"));
        Assert.assertEquals(3, queue.pos);

        Assert.assertFalse(queue.matchesCS("def"));
        Assert.assertEquals(5, queue.pos);
    }

    @Test
    public void testMatchesAny() {
        String data = "abc123xyz";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesAny("1x"));
        Assert.assertEquals(2, queue.pos);

        Assert.assertFalse(queue.matchesAny("xyzdef"));
        Assert.assertEquals(7, queue.pos);
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abc123xyz";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesAny(new char[]{'1', 'x'}));
        Assert.assertEquals(2, queue.pos);

        Assert.assertFalse(queue.matchesAny(new char[]{'a', 'z'}));
        Assert.assertEquals(7, queue.pos);
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<abcd><e></abcd>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertEquals(1, queue.pos);

        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertEquals(5, queue.pos);
    }

    @Test
    public void testMatchChomp() {
        String data = "<abcd><e></abcd>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchChomp("<abcd>"));
        Assert.assertEquals(5, queue.pos);

        Assert.assertFalse(queue.matchChomp("xyz"));
        Assert.assertEquals(8, queue.pos);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\r\n abc ";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertEquals(1, queue.pos);

        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertEquals(5, queue.pos);
    }

    @Test
    public void testMatchesWord() {
        String data = "abcd123efgh";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals(1, queue.pos);

        Assert.assertFalse(queue.matchesWord());
        Assert.assertEquals(6, queue.pos);
    }

    @Test
    public void testAdvance() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        Assert.assertEquals('b', queue.queue.charAt(0));
        Assert.assertEquals(1, queue.pos);

        queue.advance();
        Assert.assertEquals('c', queue.queue.charAt(0));
        Assert.assertEquals(2, queue.pos);

        queue.advance();
        Assert.assertEquals('d', queue.queue.charAt(0));
        Assert.assertEquals(3, queue.pos);

        queue.advance();
        Assert.assertEquals('e', queue.queue.charAt(0));
        Assert.assertEquals(4, queue.pos);

        queue.advance();
        Assert.assertEquals('f', queue.queue.charAt(0));
        Assert.assertEquals(5, queue.pos);
    }

    @Test(expected = IllegalStateException.class)
    public void testAdvanceOnEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
    }

    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.queue.charAt(0));
        Assert.assertEquals(1, queue.pos);

        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.queue.charAt(0));
        Assert.assertEquals(2, queue.pos);

        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('d', queue.queue.charAt(0));
        Assert.assertEquals(3, queue.pos);

        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.queue.charAt(0));
        Assert.assertEquals(4, queue.pos);

        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.queue.charAt(0));
        Assert.assertEquals(5, queue.pos);

        Assert.assertEquals('f', queue.consume());
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(6, queue.pos);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeOnEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume();
    }

    @Test
    public void testConsumeString() {
        String data = "abcabc";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("bc", queue.consume(2));
        Assert.assertEquals(3, queue.pos);

        Assert.assertEquals("abc", queue.consume(3));
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(6, queue.pos);
    }

    @Test
    public void testConsumeTo() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("def", queue.consumeTo("def"));
        Assert.assertEquals("g", queue.queue.charAt(0));
        Assert.assertEquals(5, queue.pos);

        Assert.assertEquals("efg", queue.consumeTo("efg"));
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(8, queue.pos);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "ABCDEFabcdef";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("AbCDeF", queue.consumeToIgnoreCase("AbCDeF"));
        Assert.assertEquals("abcdef", queue.queue.substring(0, 11));
        Assert.assertEquals(12, queue.pos);

        Assert.assertEquals("ef", queue.consumeToIgnoreCase("ef"));
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(14, queue.pos);
    }

    @Test
    public void testConsumeToAny() {
        String data = "<abcd><e></abcd>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("abcd", queue.consumeToAny("<abcd>"));
        Assert.assertEquals("e", queue.queue.charAt(0));
        Assert.assertEquals(6, queue.pos);

        Assert.assertEquals("e", queue.consumeToAny("e"));
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(7, queue.pos);
    }

    @Test
    public void testChompBalanced() {
        String data = "((((((((((((((((((";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("(((((((((((((((((", queue.chompBalanced('(', ')'));
        Assert.assertEquals(13, queue.pos);

        Assert.assertEquals(")))))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(21, queue.pos);
    }

    @Test
    public void testUnescape() {
        String data = "abc\\def\\\\";
        Assert.assertEquals("abcdef\\", TokenQueue.unescape(data));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " \t\r\n abc ";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals(5, queue.pos);

        Assert.assertFalse(queue.consumeWhitespace());
        Assert.assertEquals("abc", queue.queue);
        Assert.assertEquals(8, queue.pos);
    }

    @Test
    public void testConsumeWord() {
        String data = "abcd123efgh";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("abcd", queue.consumeWord());
        Assert.assertEquals("123efgh", queue.queue);
        Assert.assertEquals(5, queue.pos);

        Assert.assertEquals("efgh", queue.consumeWord());
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(10, queue.pos);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag1 name='value1' tag2='value2' />";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("tag1", queue.consumeTagName());
        Assert.assertEquals("name='value1' tag2='value2' ", queue.queue);
        Assert.assertEquals(10, queue.pos);

        Assert.assertEquals("tag2", queue.consumeTagName());
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(13, queue.pos);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tag1 class='class1 tag2' id='id1'>Content</tag1>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("tag1", queue.consumeElementSelector());
        Assert.assertEquals("class='class1 tag2' id='id1'>Content</tag1>", queue.queue);
        Assert.assertEquals(20, queue.pos);

        Assert.assertEquals("tag2", queue.consumeElementSelector());
        Assert.assertEquals("id='id1'>Content</tag1>", queue.queue);
        Assert.assertEquals(24, queue.pos);

        Assert.assertEquals("id1", queue.consumeElementSelector());
        Assert.assertEquals("Content</tag1>", queue.queue);
        Assert.assertEquals(28, queue.pos);

        Assert.assertEquals("Content", queue.consumeElementSelector());
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(30, queue.pos);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".class1.tag2#id1 { color: red }";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals(".class1", queue.consumeCssIdentifier());
        Assert.assertEquals(".tag2#id1 { color: red }", queue.queue);
        Assert.assertEquals(17, queue.pos);

        Assert.assertEquals(".tag2", queue.consumeCssIdentifier());
        Assert.assertEquals("#id1 { color: red }", queue.queue);
        Assert.assertEquals(21, queue.pos);

        Assert.assertEquals("#id1", queue.consumeCssIdentifier());
        Assert.assertEquals(" { color: red }", queue.queue);
        Assert.assertEquals(26, queue.pos);

        Assert.assertEquals(" { color: red }", queue.queue);
        Assert.assertEquals(28, queue.pos);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "<div class='class1' id='id1' style='color: red'>Content</div>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("<div id='id1' style='color: red'>Content</div>", queue.queue);
        Assert.assertEquals(13, queue.pos);

        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("<div style='color: red'>Content</div>", queue.queue);
        Assert.assertEquals(17, queue.pos);

        Assert.assertEquals("style", queue.consumeAttributeKey());
        Assert.assertEquals("Content</div>", queue.queue);
        Assert.assertEquals(24, queue.pos);

        Assert.assertEquals("Content", queue.consumeAttributeKey());
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(28, queue.pos);
    }

    @Test
    public void testRemainder() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("def", queue.remainder());
        Assert.assertEquals("", queue.queue);
        Assert.assertEquals(5, queue.pos);
    }
}