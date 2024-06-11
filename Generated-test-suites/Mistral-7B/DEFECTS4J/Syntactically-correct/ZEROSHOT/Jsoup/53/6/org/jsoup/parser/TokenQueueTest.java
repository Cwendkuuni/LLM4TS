import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("x");
        assertFalse(queue.isEmpty());
        assertEquals('x', queue.peek());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
        queue.addFirst("efg");
        assertEquals("efg", queue.toString());
        assertEquals('e', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("ef"));
        queue.advance();
        assertTrue(queue.matches("cd"));
        queue.advance();
        assertTrue(queue.matches(""));

        queue.addFirst("123");
        assertTrue(queue.matches("12"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSeq() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.matches(null);
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABcd");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("ef"));
        queue.advance();
        assertTrue(queue.matchesCS("cd"));
        queue.advance();
        assertTrue(queue.matchesCS(""));

        queue.addFirst("123");
        assertTrue(queue.matchesCS("12"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("ef"));
        queue.advance();
        assertTrue(queue.matchesAny("cd", ""));
        queue.advance();
        assertTrue(queue.matchesAny("", ""));

        queue.addFirst("123");
        assertTrue(queue.matchesAny("12", "3"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny(new Character[]{'a', 'b'}));
        assertFalse(queue.matchesAny(new Character[]{'e', 'f'}));
        queue.advance();
        assertTrue(queue.matchesAny(new Character[]{'c', 'd'}));
        queue.advance();
        assertTrue(queue.matchesAny(new Character[]{}));

        queue.addFirst("123");
        assertTrue(queue.matchesAny(new Character[]{'1', '2'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abcd>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("ab<abc>de");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("abcde", queue.toString());
        assertFalse(queue.matchChomp("ef"));
        assertEquals("abcde", queue.toString());

        assertTrue(queue.matchChomp("<abc>"));
        assertEquals("de", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t abcd ");
        assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd123ABCdE45");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());

        queue.consumeWord();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
        queue.advance();
        assertEquals('d', queue.peek());
        queue.advance();
        assertEquals('', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('', queue.peek());

        queue.addFirst("efg");
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertEquals('g', queue.consume());
        assertEquals('', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNullSeq() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.consume(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSeqNotFound() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.consume("ef");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSeqNotLongEnough() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.consume("ab");
        queue.consume("ef");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("ab<abc>def<xyz>");
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("defxyz", queue.toString());

        assertEquals("defxyz", queue.consumeToIgnoreCase("DEFxyz"));
        assertEquals("", queue.toString());

        assertEquals("def", queue.consumeToAny("x", "y"));
        assertEquals("", queue.toString());

        assertEquals("abc", queue.chompTo("abc"));
        assertEquals("defxyz", queue.toString());

        assertEquals("defxyz", queue.chompToIgnoreCase("DEFxyz"));
        assertEquals("", queue.toString());

        assertEquals("def", queue.chompToAny(new String[]{"x", "y"}));
        assertEquals("", queue.toString());

        assertEquals("def", queue.chompBalanced('(', ')'));
        assertEquals("xyz", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t abcd \t ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());

        assertEquals("abcd", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd123ABCdE45");
        assertEquals("abcd", queue.consumeWord());
        assertEquals("123ABCdE45", queue.toString());

        assertEquals("123", queue.consumeWord());
        assertEquals("ABCdE45", queue.toString());

        assertEquals("ABCd", queue.consumeWord());
        assertEquals("E45", queue.toString());

        assertEquals("E", queue.consumeWord());
        assertEquals("45", queue.toString());

        assertEquals("4", queue.consumeWord());
        assertEquals("5", queue.toString());

        assertEquals("", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tagName attribute=\"value\" />");
        assertEquals("tagName", queue.consumeTagName());
        assertEquals("", queue.toString());

        assertEquals("attribute", queue.consumeElementSelector());
        assertEquals("value", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<tagName attribute=\"value\" />");
        assertEquals("tagName", queue.consumeElementSelector());
        assertEquals("", queue.toString());

        assertEquals("tagName", queue.consumeTagName());
        assertEquals("", queue.toString());

        assertEquals("attribute", queue.consumeAttributeKey());
        assertEquals("value", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("tagName { color: red; } .className { color: blue; }");
        assertEquals("tagName", queue.consumeTagName());
        assertEquals("", queue.toString());

        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals("red", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());

        assertEquals(".", queue.consumeCssIdentifier());
        assertEquals("className", queue.consumeCssIdentifier());
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals("blue", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("<tagName attribute=\"value\" />");
        assertEquals("attribute", queue.consumeAttributeKey());
        assertEquals("value", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals("cd", queue.remainder());

        queue.addFirst("efg");
        assertEquals("efg", queue.remainder());

        queue.consume("abc");
        assertEquals("d", queue.peek());
        assertEquals("d", queue.remainder().charAt(0));
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals("cd", queue.remainder());
        assertEquals("cd", queue.toString());

        queue.addFirst("efg");
        assertEquals("efgcd", queue.toString());
    }
}