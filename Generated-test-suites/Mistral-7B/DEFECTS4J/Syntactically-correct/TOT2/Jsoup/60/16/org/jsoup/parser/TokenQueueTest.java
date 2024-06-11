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
        TokenQueue queue = new TokenQueue("abcdef");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekWhenEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("123");
        assertEquals("123a", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("12"));
        assertFalse(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abc123DEF");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertTrue(queue.matchesAny("ab", "12"));
        assertFalse(queue.matchesAny("ef"));
        assertFalse(queue.matchesAny("z"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title>");
        assertTrue(queue.matchChomp("<title>"));
        assertEquals("Test", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.matchesWhitespace());
        assertEquals("abc", queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd123efg");
        assertTrue(queue.matchesWord());
        assertEquals("abcd", queue.consumeWord());
        assertFalse(queue.matchesWord());
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
    public void testConsumeWhenEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("abc123def");
        String seq = "abc";
        queue.consume(seq);
        assertEquals("def", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceWhenNotMatch() {
        TokenQueue queue = new TokenQueue("abc123def");
        String seq = "efg";
        queue.consume(seq);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceWhenNotLongEnough() {
        TokenQueue queue = new TokenQueue("abc123def");
        String seq = "abc";
        queue.consume(seq);
        queue.consume(seq);
        queue.consume(seq);
        queue.consume(seq); // not long enough
        queue.consume(seq);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());
        assertEquals("(one (two) three)", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\abc\\def\\\\";
        String out = TokenQueue.unescape(in);
        assertEquals("abcdef\\", out);
    }
}