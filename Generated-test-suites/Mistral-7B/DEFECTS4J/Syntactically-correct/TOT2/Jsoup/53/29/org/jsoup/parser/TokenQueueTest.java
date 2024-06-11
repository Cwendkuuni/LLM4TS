import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
        
        queue.advance();
        assertEquals('a', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("ac"));
        assertFalse(queue.matches("abd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abcABC");
        assertTrue(queue.matchesCS("a"));
        assertTrue(queue.matchesCS("b"));
        assertTrue(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("A"));
        assertFalse(queue.matchesCS("d"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny("a", "d"));
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("e", "f"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny('a', 'd'));
        assertTrue(queue.matchesAny('b', 'd'));
        assertTrue(queue.matchesAny('a', 'c'));
        assertFalse(queue.matchesAny('e', 'f'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume first character
    }

    @Test(expected = AssertionError.class)
    public void testMatchChompWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.matchChomp("abc");
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcabcabc");
        queue.matchChomp("abc");
        assertEquals("abcabcabc".substring(3), queue.toString());

        queue = new TokenQueue("abcabcabc");
        queue.matchChomp("xyz");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("    abc");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.advance());
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithMismatchedSequence() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consumeTo("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("ab"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCdef");
        assertEquals("ABC", queue.consumeToIgnoreCase("AbC"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals("ab", queue.consumeToAny("ab", "cd"));
        assertEquals("cd", queue.toString());
    }

    @Test
    public void testConsumeToAnyWithEmptySequence() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals("", queue.consumeToAny());
        assertEquals("abcd", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(abc)(def)(ghi)");
        assertEquals("abc(def)(ghi)", queue.toString());
        assertEquals("abc(def)", queue.chompBalanced('(', ')'));
        assertEquals("(ghi)", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("abc\\def", TokenQueue.unescape("abc\\def"));
        assertEquals("abc\\def\\ghi\\jkl", TokenQueue.unescape("abc\\def\\\\ghi\\\\jkl"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("body", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class1.class2", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 id2");
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2 id1 id2", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='value' class='class1 class2'");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class='class1 class2'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        String remainder = queue.remainder();
        assertEquals("def", queue.toString());
        assertEquals("def", remainder);
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.toString().substring(queue.pos));
    }
}