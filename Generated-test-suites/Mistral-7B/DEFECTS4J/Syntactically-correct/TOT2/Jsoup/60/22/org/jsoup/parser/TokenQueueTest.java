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
        assertEquals('a', queue.consume());
        queue.addFirst('x');
        assertEquals('x', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstNull() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst(null);
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("def");
        assertEquals("defabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertFalse(queue.matches("z"));
        queue.advance();
        assertTrue(queue.matches("b"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("A"));
        assertFalse(queue.matchesCS("z"));
        queue.advance();
        assertTrue(queue.matchesCS("B"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b"));
        assertFalse(queue.matchesAny("z"));
        queue.advance();
        assertTrue(queue.matchesAny("a", "c"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('z'));
        queue.advance();
        assertTrue(queue.matchesAny('a', 'c'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // already consumed <html> and <head>
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>abc</title></head>");
        String seq = "<title>";
        assertTrue(queue.matchChomp(seq));
        assertEquals("abc</title>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abc");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertTrue(queue.matchesWhitespace());
        queue.advance();
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

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("<html><head><title>abc</title></head>");
        String seq = "<title>";
        queue.consume(seq);
        assertEquals("abc</title>", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceMismatch() {
        TokenQueue queue = new TokenQueue("<html><head><title>abc</title></head>");
        String seq = "<head>";
        queue.consume(seq);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceTooShort() {
        TokenQueue queue = new TokenQueue("<html><head><title>abc</title></head>");
        String seq = "title";
        queue.consume(seq);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("<html><head><title>abc</title></head>");
        String seq = "</title>";
        assertEquals("abc", queue.consumeTo(seq).toString());
        assertEquals("</title>", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("<html><head><title>ABC</title></head>");
        String seq = "</title>";
        assertEquals("ABC", queue.consumeToIgnoreCase(seq).toString());
        assertEquals("</title>", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("<html><head><title>abc</title></head>");
        String[] seq = {"title", "head"};
        assertEquals("abc", queue.consumeToAny(seq).toString());
        assertEquals("</title>", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())");
        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.toString());

        queue = new TokenQueue("(()){(})");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("{(})", queue.toString());

        queue = new TokenQueue("(()){(abc)}");
        assertEquals("(()){(abc)}", queue.toString());

        queue = new TokenQueue("(()){(abc)}<tag>");
        assertEquals("(()){(abc)}", queue.toString());
    }

    @Test
    public void testChompBalancedWithEscapes() {
        TokenQueue queue = new TokenQueue("(()){(abc)\\})");
        assertEquals("(()){(abc)}", queue.toString());

        queue = new TokenQueue("(()){(abc)\\}\\}");
        assertEquals("(()){(abc)}", queue.toString());

        queue = new TokenQueue("(()){(abc)\'abc\\})");
        assertEquals("(()){(abc)}", queue.toString());

        queue = new TokenQueue("(()){(abc)\"abc\\})");
        assertEquals("(()){(abc)}", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\abc\\def\\\\";
        String out = TokenQueue.unescape(in);
        assertEquals("abcdef\\", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("  \t\r\n abc");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc def");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name>");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("name", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag name|class:class");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("name|class:class", queue.toString());

        queue = new TokenQueue("tag name|class:class name2");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("name|class:class name2", queue.toString());

        queue = new TokenQueue("*|tag name|class:class");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("name|class:class", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:identifier");
        assertEquals("identifier", queue.consumeCssIdentifier());
        assertEquals("class:", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class:attribute key=\"value\"");
        assertEquals("attribute", queue.consumeAttributeKey());
        assertEquals("key=\"value\"", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("abc", queue.toString());

        queue = new TokenQueue("abcdef");
        assertEquals("abcdef", queue.toString());

        queue = new TokenQueue("");
        assertEquals("", queue.toString());
    }
}