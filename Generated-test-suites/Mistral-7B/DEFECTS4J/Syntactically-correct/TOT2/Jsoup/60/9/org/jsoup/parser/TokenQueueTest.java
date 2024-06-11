import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abcABC");
        assertTrue(queue.matchesCS("ab"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xyz", "ab"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchesAny('a', 'b'));
        assertFalse(queue.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchChomp("ab"));
        assertFalse(queue.matchChomp("xyz"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertEquals("abc", queue.consume("abc"));
        assertEquals("abc", queue.consume("abc"));
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatch() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNotLongEnough() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("ab");
        queue.consume("c");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("", queue.consumeTo("xyz"));
        assertEquals("", queue.consumeTo(""));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCabc");
        assertEquals("ABC", queue.consumeToIgnoreCase("ABC"));
        assertEquals("abc", queue.consumeToIgnoreCase("abc"));
        assertEquals("", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeToAny("ab", "cd"));
        assertEquals("def", queue.consumeToAny("xyz", "ef"));
        assertEquals("", queue.consumeToAny("ab", "xyz"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())()");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', '('));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        assertEquals("aaa\\bbb", TokenQueue.unescape("aaa\\bbb"));
        assertEquals("aaabbb", TokenQueue.unescape("aaa\\bbb\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id.class:hover");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("id", queue.consumeElementSelector());
        assertEquals("class", queue.consumeElementSelector());
        assertEquals(":hover", queue.consumeElementSelector());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:myId.class:myClass");
        assertEquals("myId", queue.consumeCssIdentifier());
        assertEquals("myClass", queue.consumeCssIdentifier());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("style=\"color: red\" class=\"highlight\" id=\"myId\"");
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("", queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("c", queue.toString());
        assertEquals("", queue.toString());
    }
}