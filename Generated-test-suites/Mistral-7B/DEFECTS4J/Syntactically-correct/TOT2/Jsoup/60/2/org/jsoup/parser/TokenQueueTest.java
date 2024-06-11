import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("abcdef");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeekAndConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.peek());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.peek());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.peek());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.peek());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals('a', queue.peek());
        queue.addFirst('b');
        assertEquals('b', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("abcd"));
        assertFalse(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertTrue(queue.matchesCS("ab"));
        assertTrue(queue.matchesCS("AbCdEf"));
        assertFalse(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("123abcdef");
        assertTrue(queue.matchesAny("1", "2", "a", "b", "c"));
        assertTrue(queue.matchesAny("123"));
        assertFalse(queue.matchesAny("ef"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'c'}));
        assertFalse(queue.matchesAny(new char[]{'e', 'f'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abcd>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcd<efg>");
        assertTrue(queue.matchChomp("ab"));
        assertTrue(queue.matchChomp("efg"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abcdef ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
        assertEquals('a', queue.consume());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
        assertEquals('a', queue.consume());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
        queue.advance();
        assertEquals('d', queue.peek());
        queue.advance();
        assertEquals('e', queue.peek());
        queue.advance();
        assertEquals('f', queue.peek());
        queue.advance();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("ab");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithWrongSequence() {
        TokenQueue queue = new TokenQueue("abcd<efg>");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc<def>ghij");
        assertEquals("abc<def>", queue.consumeTo("abc<def>"));
        assertEquals("ghij", queue.remainder());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEfGhIj");
        assertEquals("AbCdEf", queue.consumeToIgnoreCase("AbCdEf"));
        assertEquals("GhIj", queue.remainder());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("12345<efg>hijk");
        assertEquals("12345", queue.consumeToAny("1", "2", "3", "4", "5"));
        assertEquals("hijk", queue.remainder());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(ab(cd)ef)ghij");
        assertEquals("ab(cd)", queue.chompBalanced('(', ')'));
        assertEquals("ef)ghij", queue.remainder());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\/\\\\";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("/\\", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abcdef ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.remainder());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<abcd>");
        assertEquals("abcd", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<div class='myclass' id='myid'>");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class='myclass' id='myid'", queue.remainder());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id='myclass' class='myid'");
        assertEquals("myclass", queue.consumeCssIdentifier());
        assertEquals("class='myid'", queue.remainder());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='myclass' class='myid'");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class='myid'", queue.remainder());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.toString());
        assertTrue(queue.isEmpty());
    }
}