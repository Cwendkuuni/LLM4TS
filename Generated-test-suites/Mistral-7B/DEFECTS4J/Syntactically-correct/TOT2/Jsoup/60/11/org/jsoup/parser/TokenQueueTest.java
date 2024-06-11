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
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());

        queue = new TokenQueue("");
        assertEquals('\u0000', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
        assertEquals('x', queue.consume());
        assertEquals('a', queue.peek());

        queue = new TokenQueue("");
        queue.addFirst("xyz");
        assertEquals('x', queue.peek());
        assertEquals('x', queue.consume());
        assertEquals('y', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("ef"));
        assertFalse(queue.matches("xyz"));

        queue = new TokenQueue("xyz");
        assertTrue(queue.matches("xyz"));
        assertFalse(queue.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abcABCdef");
        assertTrue(queue.matchesCS("abC"));
        assertFalse(queue.matchesCS("ef"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ef", "ab"));
        assertFalse(queue.matchesAny("xyz", "ab"));

        queue = new TokenQueue("xyzxyz");
        assertTrue(queue.matchesAny("xy", "yz"));
        assertFalse(queue.matchesAny("ab", "xy"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny(new Character[]{'a', 'b', 'c'}));
        assertFalse(queue.matchesAny(new Character[]{'e', 'f'}));
        assertFalse(queue.matchesAny(new Character[]{'x', 'y'}));

        queue = new TokenQueue("xyzxyz");
        assertTrue(queue.matchesAny(new Character[]{'x', 'y'}));
        assertFalse(queue.matchesAny(new Character[]{'a', 'b'}));
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
        assertEquals('c', queue.peek());

        assertTrue(queue.matchChomp("efg"));
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("xyzxyz");
        assertFalse(queue.matchChomp("ab"));
        assertEquals('x', queue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\n abc ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());

        queue = new TokenQueue("123");
        assertFalse(queue.matchesWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        assertEquals('a', queue.consume());

        queue = new TokenQueue("123");
        assertFalse(queue.matchesWord());
        assertFalse(queue.consumeWord().isEmpty());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue = new TokenQueue("xyzxyz");
        queue.advance();
        assertEquals('y', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequencesMismatch() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.consume("ef");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequencesNotLongEnough() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.consume("ab");
        queue.consume("ef");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcd<efg>xyz");
        assertEquals("abcd<efg>", queue.consumeTo("abcd<efg>"));
        assertEquals("xyz", queue.toString());

        queue = new TokenQueue("xyzxyz");
        assertEquals("xyz", queue.consumeTo("xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCd<efg>XYz");
        assertEquals("AbCd<efg>", queue.consumeToIgnoreCase("AbCd<efg>"));
        assertEquals("", queue.toString());

        queue = new TokenQueue("xyzXYz");
        assertEquals("xyz", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcd<efg>xyz");
        assertEquals("abcd", queue.consumeToAny("ab", "cd"));
        assertEquals("efgxyz", queue.toString());

        queue = new TokenQueue("xyzxyz");
        assertEquals("xyz", queue.consumeToAny("xy", "yz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());

        queue = new TokenQueue("(xyz)('abc')(\\xyz\\)");
        assertEquals("xyz(abc)", queue.chompBalanced('(', ')'));
        assertEquals("\\xyz", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\abc\\def\\\\";
        String out = TokenQueue.unescape(in);
        assertEquals("abcdef\\", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abcd   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());

        queue = new TokenQueue("123");
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals("ab", queue.consumeWord());
        assertEquals('c', queue.peek());

        queue = new TokenQueue("123");
        assertEquals("1", queue.consumeWord());
        assertEquals("23", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<divid='myId' class='myClass'>");
        assertEquals("div", queue.consumeTagName());
        assertEquals("id='myId' class='myClass>", queue.toString());

        queue = new TokenQueue("<div class='myClass'>");
        assertEquals("div", queue.consumeTagName());
        assertEquals("class='myClass>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue(".myClass .myClass:hover");
        assertEquals(".myClass", queue.consumeElementSelector());
        assertEquals(".myClass:hover", queue.toString());

        queue = new TokenQueue("div.myClass:hover");
        assertEquals("div.myClass", queue.consumeElementSelector());
        assertEquals(":hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: #myColor; background-color: #myOtherColor;");
        assertEquals("myColor", queue.consumeCssIdentifier());
        assertEquals("background-color: #myOtherColor;", queue.toString());

        queue = new TokenQueue("background-color: #myOtherColor; color: #myColor;");
        assertEquals("myOtherColor", queue.consumeCssIdentifier());
        assertEquals("color: #myColor;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='myClass' id='myId'");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id='myId'", queue.toString());

        queue = new TokenQueue("id='myId' class='myClass'");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class='myClass'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcd<efg>xyz");
        assertEquals("xyz", queue.remainder());
        assertEquals("abcd<efg>", queue.toString());

        queue = new TokenQueue("xyzxyz");
        assertEquals("", queue.remainder());
        assertEquals("xyzxyz", queue.toString());
    }
}