import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue.addFirst('a');
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst('b');
        assertEquals("ba", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("efg"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345abcdef");
        assertTrue(queue.matchesAny("1", "4", "a"));
        assertFalse(queue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg<xyz>");
        assertTrue(queue.matchChomp("abc"));
        assertEquals("defg<xyz>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesWord());
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
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("defg", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABC defGHIjKlMnOPQrST");
        assertEquals("ABC defGHI", queue.consumeToIgnoreCase("ABC defGHI"));
        assertEquals("jKlMnOPQrST", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123abcd45efg");
        assertEquals("123", queue.consumeToAny("1", "2", "3"));
        assertEquals("abcd45efg", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());
    }
}