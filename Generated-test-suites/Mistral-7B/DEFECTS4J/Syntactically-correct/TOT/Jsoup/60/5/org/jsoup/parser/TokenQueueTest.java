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
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        Assert.assertEquals("xabc", queue.toString());
        Assert.assertEquals('x', queue.peek());
        Assert.assertEquals('x', queue.consume());
        Assert.assertEquals("abc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertFalse(queue.matches("ef"));
        Assert.assertTrue(queue.matches("abcd"));
        Assert.assertFalse(queue.matches("efg"));
        Assert.assertTrue(queue.matches("ef")); // matches at position
        Assert.assertTrue(queue.matches("a", 1)); // matches at position
        Assert.assertFalse(queue.matches("a", 5)); // doesn't match at position
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCdEf");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("ef"));
        Assert.assertTrue(queue.matchesCS("ABCD"));
        Assert.assertFalse(queue.matchesCS("efg"));
        Assert.assertTrue(queue.matchesCS("ef")); // matches at position
        Assert.assertTrue(queue.matchesCS("A", 1)); // matches at position
        Assert.assertFalse(queue.matchesCS("A", 5)); // doesn't match at position
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345");
        Assert.assertTrue(queue.matchesAny("1", "2", "3", "4", "5"));
        Assert.assertTrue(queue.matchesAny("1", "5"));
        Assert.assertFalse(queue.matchesAny("6"));
        Assert.assertFalse(queue.matchesAny("x", "y", "z"));

        TokenQueue queue2 = new TokenQueue("abcABC123");
        Assert.assertTrue(queue2.matchesAny("a", "b", "c", "A", "B"));
        Assert.assertTrue(queue2.matchesAny("a", "B"));
        Assert.assertFalse(queue2.matchesAny("d"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("12345");
        Assert.assertTrue(queue.matchesAny(new char[]{'1', '2', '3', '4', '5'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'1', '5'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'6'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'x', 'y', 'z'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body><p>Hello</p>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // already consumed the <
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertTrue(queue.matchChomp("cd"));
        Assert.assertFalse(queue.matchChomp("ef"));

        TokenQueue queue2 = new TokenQueue("ABCdEf");
        Assert.assertFalse(queue2.matchChomp("AB"));
        Assert.assertTrue(queue2.matchChomp("d"));
        Assert.assertFalse(queue2.matchChomp("EF"));
    }

    @Test
    public void testMatchWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace()); // already consumed a whitespace
    }

    @Test
    public void testMatchWord() {
        TokenQueue queue = new TokenQueue("123word456");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord()); // already consumed a word character
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.advance();
        queue.advance();
        Assert.assertEquals("bc123", queue.toString());
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals("bc123", queue.toString());
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consume("ab");
        Assert.assertEquals("c123", queue.toString());
        Assert.assertEquals('c', queue.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        Assert.assertEquals("(one (two) three)", queue.chompBalanced('(', ')'));
        Assert.assertEquals("four", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToUnbalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        queue.consumeTo(")");
    }

    @Test
    public void testUnescape() {
        String in = "\\\\\\n\\\\\\r\\\\\\b\\\\\\f\\\\\\t";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("\n\r\b\f\t", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def ");
        assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("abc def", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("123word456");
        String word = queue.consumeWord();
        Assert.assertEquals("word", word);
        Assert.assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body><p class=\"class\">Hello</p>");
        String tagName = queue.consumeTagName();
        Assert.assertEquals("p", tagName);
        Assert.assertEquals("<html><body><class>class</class>Hello", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#myId.class:hover input[type=text]");
        String selector = queue.consumeElementSelector();
        Assert.assertEquals("div", selector);
        Assert.assertEquals("#myId.class:hover input[type=text]", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: #ff0000; font-size: 12px");
        String identifier = queue.consumeCssIdentifier();
        Assert.assertEquals("color", identifier);
        Assert.assertEquals("background-color: #ff0000; font-size: 12px", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class\" id=\"myId\" data-foo=\"bar\"");
        String key = queue.consumeAttributeKey();
        Assert.assertEquals("class", key);
        Assert.assertEquals("id=\"myId\" data-foo=\"bar\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("123");
        String remainder = queue.remainder();
        Assert.assertEquals("", remainder);
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("abcdef");
        remainder = queue.remainder();
        Assert.assertEquals("def", remainder);
        Assert.assertEquals("abc", queue.toString());
    }
}