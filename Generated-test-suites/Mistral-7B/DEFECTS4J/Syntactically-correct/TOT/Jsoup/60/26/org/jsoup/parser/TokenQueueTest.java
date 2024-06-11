import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue.addFirst("hello");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("hello");
        Assert.assertEquals("hello", queue.toString());

        queue.addFirst("world");
        Assert.assertEquals("worldhello", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("12345abcxyz12345");
        Assert.assertTrue(queue.matches("123"));
        Assert.assertTrue(queue.matches("abc"));
        Assert.assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCxyz123ABC");
        Assert.assertTrue(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345abcxyz12345");
        Assert.assertTrue(queue.matchesAny("123", "abc"));
        Assert.assertFalse(queue.matchesAny("xyz"));
        Assert.assertTrue(queue.matchesAny("1", "2"));
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('', queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testNoMatchFound() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consume("def");
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("123abcxyz123");
        Assert.assertTrue(queue.matchChomp("123"));
        Assert.assertEquals("abcxyz", queue.toString());

        Assert.assertTrue(queue.matchChomp("abc"));
        Assert.assertEquals("xyz", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc  def   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world hello");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertFalse(queue.matchesWord());

        queue.addFirst("world");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals("world", queue.consumeWord());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcxyz123abcxyz");
        Assert.assertEquals("abc", queue.consumeTo("abc"));
        Assert.assertEquals("xyz123abc", queue.toString());

        Assert.assertEquals("xyz", queue.consumeTo("xyz"));
        Assert.assertEquals("123abc", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("({hello} world)({one (two) three})");
        Assert.assertEquals("{hello} world", queue.chompBalanced('(', ')').toString());
        Assert.assertEquals("({one (two) three})", queue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("\\\\", TokenQueue.unescape("\\\\"));
    }
}