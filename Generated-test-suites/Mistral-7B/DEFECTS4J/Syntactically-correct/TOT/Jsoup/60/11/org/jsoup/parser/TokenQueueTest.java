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
    public void testPeekAndConsumeFirstCharacter() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('d');
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("def");
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertTrue(queue.matches("ef"));
        Assert.assertFalse(queue.matches("x"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("xyz"));
        Assert.assertTrue(queue.matchesCS("EF"));
        Assert.assertFalse(queue.matchesCS("x"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny("ab", "ef"));
        Assert.assertFalse(queue.matchesAny("xyz"));
        Assert.assertFalse(queue.matchesAny("x"));
    }

    @Test
    public void testMatchesAnyWithSingleCharacter() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny('a', 'e', 'f'));
        Assert.assertTrue(queue.matchesAny('d'));
        Assert.assertFalse(queue.matchesAny('x'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abcdef>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // consume <
        Assert.assertFalse(queue.matchesStartTag()); // consume a
        Assert.assertTrue(queue.matchesStartTag()); // consume b
        Assert.assertTrue(queue.matchesStartTag()); // consume c
        Assert.assertTrue(queue.matchesStartTag()); // consume d
        Assert.assertTrue(queue.matchesStartTag()); // consume e
        Assert.assertTrue(queue.matchesStartTag()); // consume f
        Assert.assertFalse(queue.matchesStartTag()); // consume >
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("ab<cd>ef");
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertEquals("cd", queue.consumeTo(String.valueOf(ESC)).consumeTo(String.valueOf(ESC)));
        Assert.assertTrue(queue.matchChomp("ef"));
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("abc", queue.consumeWord());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd1234efg");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("ab", queue.consumeWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("cd", queue.consumeWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("1234", queue.consumeWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("ef", queue.consumeWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("g", queue.consumeWord());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        Assert.assertEquals('b', queue.peek());
        queue.advance();
        Assert.assertEquals('c', queue.peek());
        queue.advance();
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.consume());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithMismatchedSequence() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("def");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("ab<cd>ef");
        Assert.assertEquals("ab<cd>", queue.consumeTo("cd"));
        Assert.assertEquals("ef", queue.consumeTo("ef"));
        Assert.assertEquals("", queue.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        Assert.assertEquals("AbC", queue.consumeToIgnoreCase("cd"));
        Assert.assertEquals("Ef", queue.consumeToIgnoreCase("ef"));
        Assert.assertEquals("", queue.consumeToIgnoreCase("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("ab<cd>ef");
        Assert.assertEquals("ab", queue.consumeToAny("ab"));
        Assert.assertEquals("cd", queue.consumeToAny("cd"));
        Assert.assertEquals("ef", queue.consumeToAny("ef"));
        Assert.assertEquals("", queue.consumeToAny("xyz"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(ab(cd)ef)g");
        Assert.assertEquals("ab(cd)ef", queue.chompBalanced('(', ')'));
        Assert.assertEquals("g", queue.consumeWord());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testUnescape() {
        String input = "\\<abc\\>def\\\\";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals("<abc>def\\", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("abc", queue.consumeWord());
        Assert.assertFalse(queue.isEmpty());
    }
}