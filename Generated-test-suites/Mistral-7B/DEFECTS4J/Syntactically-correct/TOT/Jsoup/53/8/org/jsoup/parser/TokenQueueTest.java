package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        // Test empty queue
        TokenQueue queue = new TokenQueue("");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("", queue.toString());

        // Test non-empty queue
        String data = "abcdef";
        queue = new TokenQueue(data);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(data.substring(0, queue.pos()), queue.toString());

        // Test adding characters to the start of the queue
        queue.addFirst('a');
        Assert.assertEquals("a" + data.substring(0, queue.pos()), queue.toString());

        queue.addFirst("ab");
        Assert.assertEquals("ab" + data.substring(0, queue.pos()), queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        String data = "abcdef";
        queue = new TokenQueue(data);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals(data.charAt(queue.pos()), queue.peek());
        Assert.assertEquals(data.charAt(queue.pos()), queue.consume());

        queue.addFirst('a');
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        queue.addFirst("ab");
        Assert.assertEquals("ab" + data.substring(0, queue.pos()), queue.toString());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals(data.charAt(queue.pos()), queue.peek());
    }

    @Test
    public void testAddFirstCharacter() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        queue.addFirst('a');
        Assert.assertEquals("a" + data.substring(0, queue.pos()), queue.toString());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals(data.charAt(queue.pos()), queue.peek());
    }

    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        // Test empty string
        Assert.assertTrue(queue.matches(""));

        // Test matching subsequence
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertEquals("ab", queue.consumeTo("ab"));
        Assert.assertEquals("def", queue.toString());

        // Test non-matching subsequence
        Assert.assertFalse(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        String data = "abcABdef";
        TokenQueue queue = new TokenQueue(data);

        // Test matching sequence
        Assert.assertTrue(queue.matchesCS("ab"));
        Assert.assertEquals("ab", queue.consumeTo("ab"));
        Assert.assertEquals("ABdef", queue.toString());

        // Test non-matching sequence
        Assert.assertFalse(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdefghijklm";
        TokenQueue queue = new TokenQueue(data);

        String[] sequences = {"ab", "ef", "m"};

        // Test matching subsequence
        Assert.assertTrue(queue.matchesAny(sequences));
        Assert.assertEquals("am", queue.consumeToAny(sequences));
        Assert.assertEquals("defghijkl", queue.toString());

        // Test non-matching subsequences
        Assert.assertFalse(queue.matchesAny(new String[]{"zx"}));
        Assert.assertEquals("", queue.consumeToAny(new String[]{"zx"}));
        Assert.assertEquals("defghijkl", queue.toString());
    }

    @Test
    public void testMatchesAnyCharacter() {
        String data = "abcdefghijklm";
        TokenQueue queue = new TokenQueue(data);

        char[] sequences = {'a', 'f', 'k'};

        // Test matching character
        Assert.assertTrue(queue.matchesAny(sequences));
        Assert.assertEquals("k", queue.consumeToAny(sequences));
        Assert.assertEquals("abcd eghijlm", queue.toString());

        // Test non-matching characters
        Assert.assertFalse(queue.matchesAny(new char[]{'z'}));
        Assert.assertEquals("", queue.consumeToAny(new char[]{'z'}));
        Assert.assertEquals("abcd eghijlm", queue.toString());
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<tag1><tag2><tag3>";
        TokenQueue queue = new TokenQueue(data);

        // Test empty queue
        Assert.assertFalse(queue.matchesStartTag());

        // Test unmatched start tag
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertEquals("", queue.consumeToAny(new String[]{"<tag4"}));
        Assert.assertEquals("", queue.toString());

        // Test matched start tag
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertEquals("<tag2", queue.consumeTo("<tag2"));
        Assert.assertEquals("<tag3>", queue.toString());
    }

    @Test
    public void testMatchChomp() {
        String data = "<tag1><tag2><tag3>";
        TokenQueue queue = new TokenQueue(data);

        // Test unmatched sequence
        Assert.assertFalse(queue.matchChomp("<tag4"));
        Assert.assertEquals("<tag3>", queue.toString());

        // Test matched sequence
        Assert.assertTrue(queue.matchChomp("<tag2"));
        Assert.assertEquals("<tag3>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abc def   ";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertEquals("", queue.consume());
        Assert.assertEquals("abc def   ", queue.toString());
    }

    @Test
    public void testMatchesWord() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("def", queue.consumeWord());
        Assert.assertEquals("ghi jkl", queue.toString());
    }

    @Test
    public void testAdvance() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        queue.advance();
        Assert.assertEquals("def ghi jkl", queue.toString());

        queue.advance();
        Assert.assertEquals("ghi jkl", queue.toString());

        queue.advance();
        Assert.assertEquals("jkl", queue.toString());
    }

    @Test
    public void testConsume() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals("bc def ghi jkl", queue.toString());

        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals("c def ghi jkl", queue.toString());

        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals("def ghi jkl", queue.toString());

        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals("ef ghi jkl", queue.toString());

        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals("f ghi jkl", queue.toString());

        Assert.assertEquals('f', queue.consume());
        Assert.assertEquals("ghi jkl", queue.toString());

        Assert.assertEquals('g', queue.consume());
        Assert.assertEquals("hi jkl", queue.toString());

        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals("i jkl", queue.toString());

        Assert.assertEquals('i', queue.consume());
        Assert.assertEquals("jkl", queue.toString());

        Assert.assertEquals('j', queue.consume());
        Assert.assertEquals("kl", queue.toString());

        Assert.assertEquals('k', queue.consume());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeString() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("abc", queue.consume(new String[]{"abc"}));
        Assert.assertEquals("def ghi jkl", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnmatchedString() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        queue.consume(new String[]{"xyz"});
    }

    @Test
    public void testConsumeTo() {
        String data = "abcd <tag1>efg <tag2>hij";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("abcd <tag1>", queue.consumeTo("<tag1>"));
        Assert.assertEquals("efg <tag2>hij", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "abcd <TAG1>efg <TAG2>hij";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("abcd <TAG1>", queue.consumeToIgnoreCase("<tag1>"));
        Assert.assertEquals("efg <TAG2>hij", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcd efg hij klm nop";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("efg hij", queue.consumeToAny(new String[]{"efg", "hij"}));
        Assert.assertEquals("klm nop", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "()()()(())";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("()()()", queue.chompBalanced('(', ')'));
        Assert.assertEquals("()()", queue.toString());

        Assert.assertEquals("(())", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\(abc\\)def\\\\";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("(abc)def\\", queue.toString());
        Assert.assertEquals("(abc)def", TokenQueue.unescape(queue.toString()));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc def   ";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("abc def   ", queue.toString());

        Assert.assertFalse(queue.consumeWhitespace());
        Assert.assertEquals("abc def", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("def", queue.consumeWord());
        Assert.assertEquals("ghi jkl", queue.toString());

        Assert.assertEquals("ghi", queue.consumeWord());
        Assert.assertEquals("jkl", queue.toString());

        Assert.assertEquals("jkl", queue.consumeWord());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag1><tag2><tag3>";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("tag2", queue.consumeTagName());
        Assert.assertEquals("<tag3>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<div tag=\"tag1\" class=\"class1 class2\">";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("tag1", queue.consumeElementSelector());
        Assert.assertEquals("class1 class2", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "color: red; background-color: #ff0000;";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("color", queue.consumeCssIdentifier());
        Assert.assertEquals("red; background-color: #ff0000", queue.toString());

        Assert.assertEquals("background-color", queue.consumeCssIdentifier());
        Assert.assertEquals("#ff0000", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "id=\"id1\" class=\"class1 class2\"";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("id1 class=\"class1 class2\"", queue.toString());

        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("class1 class2", queue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "abc def ghi jkl";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertEquals("jkl", queue.remainder());
        Assert.assertEquals("abc def", queue.toString());

        Assert.assertEquals("", queue.toString());
    }
}