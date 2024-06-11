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
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.charAt(0));

        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("abc");
        assertEquals("abc\u0000a", queue.toString());

        queue = new TokenQueue("abc");
        queue.addFirst("def");
        assertEquals("defabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("123<tag>456");

        assertTrue(queue.matches("12"));
        assertTrue(queue.matches("tag"));
        assertTrue(queue.matches("456"));
        assertFalse(queue.matches("x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesNegative() {
        TokenQueue queue = new TokenQueue("123<tag>456");
        queue.matches(null);
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Tag1 Tag2 Tag3");

        assertTrue(queue.matchesCS("Tag"));
        assertFalse(queue.matchesCS("tag"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("123<tag>456");

        assertTrue(queue.matchesAny("1", "2"));
        assertTrue(queue.matchesAny("1", "3", "4"));
        assertFalse(queue.matchesAny("x"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("123<tag>456");

        assertTrue(queue.matchesAny(new char[]{'1', '2'}));
        assertTrue(queue.matchesAny(new char[]{'1', '3', '4'}));
        assertFalse(queue.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("123<tag>456");

        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume <
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<tag attr='value'>Text</tag>");

        assertTrue(queue.matchChomp("<tag"));
        assertEquals(" attr='value'>Text</tag>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n\f ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());

        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");

        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.advance(); // consume 'c'
        assertEquals('d', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");

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
        assertEquals("", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNegative() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc<123>def<tag>ghi");

        assertEquals("123", queue.consumeTo("123"));
        assertEquals("def<tag>", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABC<123>Def<tag>GHI");

        assertEquals("123", queue.consumeToIgnoreCase("123"));
        assertEquals("Def<tag>GHI", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("1<tag>2<tag>3<tag>4");

        assertEquals("1<tag>", queue.consumeToAny("1", "2", "3"));
        assertEquals("2<tag>3<tag>4", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())");
        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.toString());

        queue = new TokenQueue("((()))");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());

        queue = new TokenQueue("((())");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\", TokenQueue.unescape("\\"));
        assertEquals("ab\\cd", TokenQueue.unescape("ab\\cd"));
        assertEquals("ab\\cdef\\g\\hi\\j\\k\\l", TokenQueue.unescape("ab\\cdef\\g\\hi\\j\\k\\l"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n\f ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());

        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("tag1 tag2 tag3");
        assertEquals("tag1", queue.consumeTagName());
        assertEquals("tag2 tag3", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag1|tag2 tag3|tag4 tag5.class tag6.class#ID");
        assertEquals("tag1|tag2", queue.consumeElementSelector());
        assertEquals("tag3|tag4 tag5.class tag6.class#ID", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 id1 class2 id2 class3 id3");
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("id1 class2 id2 class3 id3", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='value' class='class' style='style'");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class='class' style='style'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());

        queue = new TokenQueue("");
        assertEquals("", queue.toString());
    }
}