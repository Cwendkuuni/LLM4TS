package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void testCreateNewInstance() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertNotNull(tokenQueue);
        assertEquals(data, tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullData() {
        new TokenQueue(null);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "testData";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(0, tokenQueue.peek());

        data = "testData<tagName>";
        tokenQueue = new TokenQueue(data);
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);

        tokenQueue.addFirst('c');
        assertEquals("c" + data.substring(0, tokenQueue.pos), tokenQueue.queue);

        tokenQueue.addFirst("testString");
        assertEquals("testString" + data.substring(0, tokenQueue.pos), tokenQueue.queue);
    }

    @Test
    public void testMatches() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);

        String seq = "tagName";
        assertTrue(tokenQueue.matches(seq));
        assertEquals(seq.length(), tokenQueue.remainingLength());

        seq = "TagName";
        assertFalse(tokenQueue.matches(seq));
    }

    @Test
    public void testMatchesCS() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);

        String seq = "tagName";
        assertTrue(tokenQueue.matchesCS(seq));

        seq = "TagName";
        assertFalse(tokenQueue.matchesCS(seq));
    }

    @Test
    public void testMatchesAny() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);

        String[] seq = {"TagName", "tagName"};
        assertTrue(tokenQueue.matchesAny(seq));

        seq = new String[]{"TagName", "TagName"};
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);

        char[] seq = {'T', 'a'};
        assertTrue(tokenQueue.matchesAny(seq));

        seq = new char[]{'T', 'A'};
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());

        data = "testData<tagName>";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<tagName>content</tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);

        String seq = "tagName";
        assertTrue(tokenQueue.matchChomp(seq));
        assertEquals("content</tagName>", tokenQueue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " testData ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());

        data = "testData";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testData word";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());

        data = "testData";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals("testData<name", tokenQueue.queue);

        data = "testData";
        tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertNull(tokenQueue.queue);
    }

    @Test
    public void testConsume() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('t', tokenQueue.consume());
        assertEquals("estData<name", tokenQueue.queue);

        data = "testData";
        tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName", tokenQueue.consume(2));
        assertEquals("estData", tokenQueue.queue);

        data = "testData<tagName>";
        tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.consume("tagName"));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnmatchedString() {
        String data = "testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("TestName");
    }

    @Test
    public void testConsumeTo() {
        String data = "<tagName>content</tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName", tokenQueue.consumeTo("tagName"));
        assertEquals("content</tagName>", tokenQueue.queue);

        data = "<tagName>content</tagName>";
        tokenQueue = new TokenQueue(data);
        assertEquals("content</tagName>", tokenQueue.consumeTo(""));
        assertNull(tokenQueue.queue);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "<TAGName>content</TAGName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("TAGName", tokenQueue.consumeToIgnoreCase("tagName"));
        assertEquals("content</TAGName>", tokenQueue.queue);

        data = "<TAGName>content</TAGName>";
        tokenQueue = new TokenQueue(data);
        assertEquals("content</TAGName>", tokenQueue.consumeToIgnoreCase("TAGName"));
        assertNull(tokenQueue.queue);
    }

    @Test
    public void testConsumeToAny() {
        String data = "testData<tagName>content testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName", tokenQueue.consumeToAny(new String[]{"TagName", "tagName"}));
        assertEquals("content testData", tokenQueue.queue);

        data = "testData<tagName>content testData";
        tokenQueue = new TokenQueue(data);
        assertEquals("content testData", tokenQueue.consumeToAny(new String[]{"TagName", "TagName"}));
        assertNull(tokenQueue.queue);
    }

    @Test
    public void testChompBalanced() {
        String data = "(testData)";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("testData", tokenQueue.chompBalanced('(', ')').toString());
        assertNull(tokenQueue.queue);

        data = "(testData (testData))";
        tokenQueue = new TokenQueue(data);
        assertEquals("testData (testData)", tokenQueue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\testData\\testData\\";
        String result = TokenQueue.unescape(data);
        assertEquals("testDatatestData", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " testData ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("estData", tokenQueue.queue);
    }

    @Test
    public void testConsumeWord() {
        String data = "testData word";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("word", tokenQueue.consumeWord());
        assertEquals("testData", tokenQueue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tagName>testData<tagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName", tokenQueue.consumeTagName());
        assertEquals("testData<tagName>", tokenQueue.queue);

        data = "<tagName>testData<tagName>";
        tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tagName:element>testData<tagName:element>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("tagName:element", tokenQueue.consumeElementSelector());
        assertEquals("testData<tagName:element>", tokenQueue.queue);

        data = "<tagName:element>testData<tagName:element>";
        tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id:testIdentifier class:classIdentifier testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("testIdentifier", tokenQueue.consumeCssIdentifier());
        assertEquals("class:classIdentifier testData", tokenQueue.queue);

        data = "id:testIdentifier class:classIdentifier testData";
        tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attribute:key testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("key", tokenQueue.consumeAttributeKey());
        assertEquals("testData", tokenQueue.queue);

        data = "attribute:key testData";
        tokenQueue = new TokenQueue(data);
        assertNull(tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("", tokenQueue.remainder());

        data = "testData<tagName>";
        tokenQueue = new TokenQueue(data);
        assertEquals("<tagName>", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("testData", tokenQueue.toString());

        data = "testData<tagName>";
        tokenQueue = new TokenQueue(data);
        assertEquals("<tagName>", tokenQueue.toString());
    }
}