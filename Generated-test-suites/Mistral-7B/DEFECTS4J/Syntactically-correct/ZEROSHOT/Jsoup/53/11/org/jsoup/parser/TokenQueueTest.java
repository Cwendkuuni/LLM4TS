import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.TokenQueue.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("abcdefg");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('x');
        queue.addFirst("abc");
        assertEquals("abcx", queue.toString());

        queue = new TokenQueue("123");
        queue.addFirst('0');
        assertEquals("0123", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("ef"));

        queue = new TokenQueue("123");
        assertTrue(queue.matches("12"));
        assertFalse(queue.matches("34"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("EF"));

        queue = new TokenQueue("123");
        assertTrue(queue.matchesCS("12"));
        assertFalse(queue.matchesCS("34"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ef", "cd"));
        assertFalse(queue.matchesAny("ab", "gh"));

        queue = new TokenQueue("123");
        assertTrue(queue.matchesAny("1", "2"));
        assertFalse(queue.matchesAny("3", "4"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>def");
        assertTrue(queue.matchesStartTag());

        queue = new TokenQueue("x<y z");
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdefg", queue.toString());

        queue = new TokenQueue("123");
        assertFalse(queue.matchChomp("34"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("abc def");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc def");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("123");
        assertEquals('1', queue.consume());
        assertEquals('2', queue.consume());
        assertEquals('3', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        assertEquals('A', queue.consumeCS());
        assertEquals('B', queue.consumeCS());
        assertEquals('C', queue.consumeCS());
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("123");
        assertEquals('1', queue.consumeCS());
        assertEquals('2', queue.consumeCS());
        assertEquals('3', queue.consumeCS());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefg<xyz>");
        assertEquals("abcdefg", queue.consumeTo("abcdefg").toString());
        assertEquals("xyz", queue.toString());

        queue = new TokenQueue("123 <456>");
        assertEquals("123", queue.consumeTo("123").toString());
        assertEquals(" <456>", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEFG<xyz>");
        assertEquals("ABCDEFG", queue.consumeToIgnoreCase("ABCDEFG").toString());
        assertEquals("xyz", queue.toString());

        queue = new TokenQueue("123 <456>");
        assertEquals("123", queue.consumeToIgnoreCase("123").toString());
        assertEquals(" <456>", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefg<xyz>");
        assertEquals("abcdefg", queue.consumeToAny("abc", "def").toString());
        assertEquals("xyz", queue.toString());

        queue = new TokenQueue("123 <456>");
        assertEquals("123", queue.consumeToAny("1", "2").toString());
        assertEquals(" <456>", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())[]{}");
        assertEquals("(())[]{}", queue.toString());
        assertEquals("()", queue.chompBalanced('(', ')').toString());
        assertEquals("[]{}", queue.toString());
        assertEquals("[]", queue.chompBalanced('[', ']').toString());
        assertEquals("{}", queue.toString());
        assertEquals("{}", queue.chompBalanced('{', '}').toString());

        queue = new TokenQueue("(<tag>)(<tag/>)");
        assertEquals("(<tag>)(<tag/>)", queue.toString());
        assertEquals("<tag>", queue.chompBalanced('<', '>').toString());
        assertEquals("<tag/>", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithMismatchedSequence() {
        TokenQueue queue = new TokenQueue("(<tag>)<tag/>");
        queue.consumeTo("(<tag>");
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc def ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc def");
        assertEquals("abc", queue.consumeWord().toString());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tagname>");
        assertEquals("tagname", queue.consumeTagName());
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("<tagname:namespace>");
        assertEquals("tagname:namespace", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<tagname>");
        assertEquals("tagname", queue.consumeElementSelector());
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("<tagname|namespace>");
        assertEquals("tagname|namespace", queue.consumeElementSelector());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:identifier ID:identifier");
        assertEquals("identifier", queue.consumeCssIdentifier());
        assertEquals("class:identifier", queue.toString());

        queue = new TokenQueue("class:identifier ID:identifier1");
        assertEquals("identifier1", queue.consumeCssIdentifier());
        assertEquals("class:identifier", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("name=\"attribute\"");
        assertEquals("name", queue.consumeAttributeKey());
        assertEquals("attribute", queue.toString());

        queue = new TokenQueue("class=\"attribute class\"");
        assertEquals("attribute class", queue.consumeAttributeKey());
        assertEquals("class", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("g", queue.remainder().toString());
        assertEquals("abcdef", queue.toString());

        queue = new TokenQueue("123");
        assertEquals("", queue.remainder().toString());
        assertEquals("123", queue.toString());
    }
}