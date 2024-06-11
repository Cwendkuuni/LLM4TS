import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setup() {
        String data = "abc<def>123<xyz>";
        tokenQueue = new TokenQueue(data);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("a");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        tokenQueue.addFirst("a");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("a");
        tokenQueue.addFirst("b");
        assertEquals("ba", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        tokenQueue.addFirst("abcdef");
        assertTrue(tokenQueue.matches("ab"));
        assertTrue(tokenQueue.matches("ef"));
        assertFalse(tokenQueue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        tokenQueue.addFirst("abcDEF");
        assertTrue(tokenQueue.matchesCS("AB"));
        assertFalse(tokenQueue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        tokenQueue.addFirst("abc123xyz");
        assertTrue(tokenQueue.matchesAny("ab", "1", "xy"));
        assertFalse(tokenQueue.matchesAny("ef"));
    }

    @Test
    public void testMatchesAnyChar() {
        tokenQueue.addFirst("abc123xyz");
        assertTrue(tokenQueue.matchesAny(new char[]{'a', '1'}));
        assertFalse(tokenQueue.matchesAny(new char[]{'e'}));
    }

    @Test
    public void testMatchesStartTag() {
        tokenQueue.addFirst("abc<def>123<xyz>");
        assertTrue(tokenQueue.matchesStartTag());
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        tokenQueue.addFirst("abc<def>123<xyz>");
        assertTrue(tokenQueue.matchChomp("<def>"));
        assertEquals("123<xyz>", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        tokenQueue.addFirst("   abcd   ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        tokenQueue.addFirst("abc123xyz");
        assertTrue(tokenQueue.matchesWord());
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.addFirst("abc");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('c', tokenQueue.peek());
        tokenQueue.advance();
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsume() {
        tokenQueue.addFirst("abc");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeSequence() {
        tokenQueue.addFirst("abc<def>123<xyz>");
        assertEquals("def", tokenQueue.consume("def"));
        assertEquals("123<xyz>", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        tokenQueue.addFirst("abc<def>123<xyz>");
        assertEquals("def", tokenQueue.consumeTo("def"));
        assertEquals("123<xyz>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        tokenQueue.addFirst("abc<DEF>123<xyz>");
        assertEquals("DEF", tokenQueue.consumeToIgnoreCase("def"));
        assertEquals("123<xyz>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        tokenQueue.addFirst("abc123xyz");
        assertEquals("xyz", tokenQueue.consumeToAny("xy", "z"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        tokenQueue.addFirst("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        tokenQueue.addFirst(" abc def ");
        assertTrue(tokenQueue.consumeWhitespace());
        tokenQueue.consumeWhitespace();
        tokenQueue.consumeWhitespace();
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        tokenQueue.addFirst("abc123xyz");
        assertEquals("abc", tokenQueue.consumeWord());
        assertEquals("123xyz", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        tokenQueue.addFirst("abc<def>123<xyz>");
        assertEquals("def", tokenQueue.consumeTagName());
        assertEquals("123<xyz>", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        tokenQueue.addFirst("abc:def|xyz");
        assertEquals("def|xyz", tokenQueue.consumeElementSelector());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        tokenQueue.addFirst("class:myId");
        assertEquals("myId", tokenQueue.consumeCssIdentifier());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        tokenQueue.addFirst("class:myId attr:value");
        assertEquals("myId", tokenQueue.consumeAttributeKey());
        assertEquals("attr:value", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        tokenQueue.addFirst("abcdef");
        assertEquals("def", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        tokenQueue.addFirst("abc");
        assertEquals("abc", tokenQueue.toString());
        tokenQueue.consume();
        assertEquals("bc", tokenQueue.toString());
    }
}