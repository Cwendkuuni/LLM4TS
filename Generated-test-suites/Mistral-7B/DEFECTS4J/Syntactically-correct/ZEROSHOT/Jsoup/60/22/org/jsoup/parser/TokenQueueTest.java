package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertNotNull(tokenQueue);
        assertEquals(data, tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("a");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.peek());
        tokenQueue.addFirst("a");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("a");
        assertEquals("a" + data.substring(0, 5), tokenQueue.queue);
        tokenQueue.addFirst("b");
        assertEquals("ba" + data.substring(6), tokenQueue.queue);
    }

    @Test
    public void testMatches() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matches("test"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesCS("Test"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesAny("test", "data"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchesAny("test", "data"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesAny('a', 'b'));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<testdata>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.addFirst("<");
        tokenQueue.addFirst("t");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "testdata<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchChomp("test"));
        tokenQueue.addFirst("test");
        tokenQueue.addFirst("data");
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals("data<tag>", tokenQueue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "test data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals(1, tokenQueue.pos);
        tokenQueue.addFirst("a");
        tokenQueue.advance();
        assertEquals(2, tokenQueue.pos);
    }

    @Test
    public void testConsume() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('t', tokenQueue.consume());
        assertEquals(6, tokenQueue.pos);
        assertEquals("estdata", tokenQueue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWrongSequence() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("wrongsequence");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNotLongEnough() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("test");
        tokenQueue.consume("test");
        tokenQueue.consume("test");
        tokenQueue.consume("test");
        tokenQueue.consume("test");
        tokenQueue.consume("test");
        tokenQueue.consume("test"); // This call should throw an exception
    }

    @Test
    public void testConsumeTo() {
        String data = "test<tag>data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTo("test");
        assertEquals("test<", result);
        assertEquals("tag>data", tokenQueue.queue);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "TEST<TAG>data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToIgnoreCase("test");
        assertEquals("TEST<", result);
        assertEquals("TAG>data", tokenQueue.queue);
    }

    @Test
    public void testConsumeToAny() {
        String data = "test:data<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToAny("test", "data");
        assertEquals("test:data<", result);
        assertEquals("tag>", tokenQueue.queue);
    }

    @Test
    public void testChompBalanced() {
        String data = "test((((()()))())()))data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompBalanced('(', ')');
        assertEquals("test", result);
        assertEquals("((((())()))()))data", tokenQueue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\test\\data";
        String result = TokenQueue.unescape(data);
        assertEquals("testdata", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " test data ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals(7, tokenQueue.pos);
        assertEquals("testdata", tokenQueue.queue);
    }

    @Test
    public void testConsumeWord() {
        String data = "testWordTestWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeWord();
        assertEquals("test", result);
        assertEquals("WordTestWord", tokenQueue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tagName attribute=\"value\" />";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTagName();
        assertEquals("tagName", result);
        assertEquals(" attribute=\"value\" />", tokenQueue.queue);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div#id.class p#id2.class";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeElementSelector();
        assertEquals("div", result);
        assertEquals("#id.class p#id2.class", tokenQueue.queue);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id:id1 class:class1 id:id2 class:class2";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeCssIdentifier();
        assertEquals("id", result);
        assertEquals("id1 class:class1 id:id2 class:class2", tokenQueue.queue);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attribute=\"value\" attribute2=\"value2\" attribute3=\"value3\"";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeAttributeKey();
        assertEquals("attribute", result);
        assertEquals("value attribute2=\"value2\" attribute3=\"value3\"", tokenQueue.queue);
    }

    @Test
    public void testRemainder() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.remainder();
        assertEquals("data", result);
        assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testToString() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("data", tokenQueue.toString());
        assertEquals("data", tokenQueue.queue.substring(tokenQueue.pos));
    }
}