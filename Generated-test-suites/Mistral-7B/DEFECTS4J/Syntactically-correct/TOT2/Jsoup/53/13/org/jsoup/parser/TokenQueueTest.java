package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TokenQueueTest.
 */
public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        TokenQueue nonEmptyQueue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals((char) 0, emptyQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals('a', queue.peek());

        TokenQueue queueWithString = new TokenQueue("");
        queueWithString.addFirst("xyz");
        assertEquals('x', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("cd"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertFalse(emptyQueue.matches("ab"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCabc");
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("ab"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertFalse(emptyQueue.matchesCS("Ab"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchesAny("ab", "bc"));
        assertFalse(queue.matchesAny("cd"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertFalse(emptyQueue.matchesAny("ab", "bc"));

        TokenQueue sequenceQueue = new TokenQueue("abab");
        assertTrue(sequenceQueue.matchesAny("ab"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('d'));

        TokenQueue emptyQueue = new TokenQueue("");
        assertFalse(emptyQueue.matchesAny('a', 'b', 'c'));

        TokenQueue sequenceQueue = new TokenQueue("abab");
        assertTrue(sequenceQueue.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>");
        assertTrue(queue.matchesStartTag());

        TokenQueue nonStartTagQueue = new TokenQueue("abc");
        assertFalse(nonStartTagQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abab");
        assertTrue(queue.matchChomp("ab"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertFalse(emptyQueue.matchChomp("ab"));

        TokenQueue sequenceQueue = new TokenQueue("abab");
        assertTrue(sequenceQueue.matchChomp("ab"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc");
        assertTrue(queue.matchesWhitespace());

        TokenQueue nonWhitespaceQueue = new TokenQueue("123");
        assertFalse(nonWhitespaceQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());

        TokenQueue nonWordQueue = new TokenQueue("!@#$%^");
        assertFalse(nonWordQueue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNoMatch() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("123abcdefg");
        assertEquals("123", queue.consumeTo("abc"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeTo("abc"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("123ABCDEFg");
        assertEquals("123", queue.consumeToIgnoreCase("abc"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeToIgnoreCase("abc"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123abcdefg");
        assertEquals("123", queue.consumeToAny("abc", "def"));

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeToAny("abc", "def"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        assertEquals("(())", queue.chompBalanced('(', ')'));

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        String input = "\\abc\\def\\\\";
        String expectedOutput = "abcdef\\";
        String output = TokenQueue.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc def ");
        assertTrue(queue.consumeWhitespace());

        TokenQueue emptyQueue = new TokenQueue("");
        assertFalse(emptyQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue(" <tagName> ");
        assertEquals("tagName", queue.consumeTagName());

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue(" <tagName> :class:tagName2");
        assertEquals("tagName:tagName2", queue.consumeElementSelector());

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue(" id:identifier:class:id2");
        assertEquals("identifier", queue.consumeCssIdentifier());

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue(" name:attribute:value:attribute2");
        assertEquals("attribute", queue.consumeAttributeKey());

        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("123abcdefg");
        String remainder = queue.remainder();
        assertEquals("defg", remainder);

        TokenQueue emptyQueue = new TokenQueue("");
        String remainderEmpty = queue.remainder();
        assertEquals("", remainderEmpty);
    }
}