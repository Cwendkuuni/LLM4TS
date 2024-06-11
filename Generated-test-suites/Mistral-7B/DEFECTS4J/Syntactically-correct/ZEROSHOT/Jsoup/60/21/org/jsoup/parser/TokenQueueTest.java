import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueue() {
        // Test constructor
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abcdefg", queue.queue);
        assertEquals(0, queue.pos);

        // Test isEmpty
        assertTrue(queue.isEmpty());
        queue.addFirst("1");
        assertFalse(queue.isEmpty());

        // Test peek
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());

        // Test addFirst
        queue.addFirst("xyz");
        assertEquals("xyzabcdefg", queue.queue);
        assertEquals(0, queue.pos);

        // Test matches
        String seq = "ab";
        assertTrue(queue.matches(seq));
        queue.consume(seq);
        assertFalse(queue.matches(seq));

        // Test matchesCS
        seq = "Ab";
        assertTrue(queue.matchesCS(seq));
        queue.advance();
        assertFalse(queue.matchesCS(seq));

        // Test matchesAny
        String[] seqs = {"ab", "CD"};
        assertTrue(queue.matchesAny(seqs));
        queue.consume("ab");
        assertFalse(queue.matchesAny(seqs));

        // Test matchesAny with one character
        char[] seqChars = {'x'};
        assertTrue(queue.matchesAny(seqChars));
        queue.consume("a");
        assertFalse(queue.matchesAny(seqChars));

        // Test matchesStartTag
        assertTrue(queue.matchesStartTag());
        queue.consume("b");
        assertFalse(queue.matchesStartTag());

        // Test matchChomp
        seq = "ab";
        assertTrue(queue.matchChomp(seq));
        assertEquals("cdefg", queue.queue);

        // Test matchesWhitespace
        assertTrue(queue.matchesWhitespace());
        queue.consume("a");
        assertFalse(queue.matchesWhitespace());

        // Test matchesWord
        assertTrue(queue.matchesWord());
        queue.consume("a");
        assertFalse(queue.matchesWord());

        // Test advance
        queue.advance();
        assertEquals('b', queue.peek());

        // Test consume
        queue.consume('b');
        assertEquals('c', queue.peek());

        // Test consume
        queue.consume("cd");
        assertEquals('e', queue.peek());

        // Test consume
        queue.consumeTo("ef");
        assertEquals("g", queue.peek());

        // Test consumeToIgnoreCase
        queue.addFirst("ABCDEF");
        assertEquals("ABC", queue.consumeToIgnoreCase("AbC"));
        assertEquals("DEFg", queue.queue);

        // Test consumeToAny
        queue.addFirst("abcdef");
        assertEquals("abc", queue.consumeToAny("ab", "cd"));
        assertEquals("defg", queue.queue);

        // Test chompTo
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.queue);

        // Test chompBalanced
        assertEquals("(one (two) three)", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.queue);

        // Test unescape
        assertEquals("ab\\xc3\\xa4cd", TokenQueue.unescape("ab\\u00E4cd"));

        // Test consumeWhitespace
        queue.addFirst("   abc  ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc", queue.queue);

        // Test consumeWord
        assertEquals("abc", queue.consumeWord());
        assertEquals("", queue.queue);

        // Test consumeTagName
        assertEquals("div", queue.consumeTagName());
        assertEquals("", queue.queue);

        // Test consumeElementSelector
        assertEquals("div.class", queue.consumeElementSelector());
        assertEquals("", queue.queue);

        // Test consumeCssIdentifier
        assertEquals("class", queue.consumeCssIdentifier());
        assertEquals("", queue.queue);

        // Test consumeAttributeKey
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("", queue.queue);

        // Test remainder
        assertEquals("", queue.remainder());
        queue.addFirst("123");
        assertEquals("123", queue.remainder());

        // Test toString
        assertEquals("", queue.toString());
        queue.addFirst("abc");
        assertEquals("abc", queue.toString());

        // Test equals
        TokenQueue queue1 = new TokenQueue("abcdefg");
        TokenQueue queue2 = new TokenQueue("abcdefg");
        assertTrue(queue1.equals(queue2));

        TokenQueue queue3 = new TokenQueue("xyz");
        assertFalse(queue1.equals(queue3));
    }
}