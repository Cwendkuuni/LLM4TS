import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertTrue(tokenQueue.isEmpty());

        tokenQueue.addFirst("test");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals('t', tokenQueue.peek());

        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst("test");
        Assert.assertEquals("test", tokenQueue.consumeToIgnoreCase());

        tokenQueue.addFirst("anotherTest");
        Assert.assertEquals("anotherTesttest", tokenQueue.consumeToIgnoreCase());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("testtest");
        Assert.assertTrue(tokenQueue.matches("test"));
        Assert.assertFalse(tokenQueue.matches("hello"));

        tokenQueue.advance();
        Assert.assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("TestTeSt");
        Assert.assertTrue(tokenQueue.matchesCS("Test"));
        Assert.assertFalse(tokenQueue.matchesCS("hello"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesthello");
        Assert.assertTrue(tokenQueue.matchesAny("test", "hello"));
        Assert.assertTrue(tokenQueue.matchesAny("test", "t"));
        Assert.assertFalse(tokenQueue.matchesAny("test", "world"));

        tokenQueue.consume("test");
        Assert.assertTrue(tokenQueue.matchesAny("hello", "world"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("testtesttest");
        Assert.assertTrue(tokenQueue.matchesAny(new char[]{'t', 'e'}));
        Assert.assertFalse(tokenQueue.matchesAny(new char[]{'w', 'o'}));

        tokenQueue.advance();
        Assert.assertTrue(tokenQueue.matchesAny(new char[]{'e', 's'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<test>");
        Assert.assertTrue(tokenQueue.matchesStartTag());
        Assert.assertFalse(tokenQueue.matchesStartTag()); // after consuming <

        tokenQueue.consume("<");
        Assert.assertTrue(tokenQueue.matchesStartTag()); // after consuming >
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesthello");
        Assert.assertTrue(tokenQueue.matchChomp("test"));
        Assert.assertEquals("testtesthello", tokenQueue.consumeToIgnoreCase());

        Assert.assertTrue(tokenQueue.matchChomp("test")); // consumes "test" and returns true
        Assert.assertEquals("testtesthello", tokenQueue.consumeToIgnoreCase());

        Assert.assertFalse(tokenQueue.matchChomp("hello"));
        Assert.assertEquals("testtesthello", tokenQueue.consumeToIgnoreCase());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" test test ");
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertFalse(tokenQueue.matchesWhitespace());

        tokenQueue.consumeWhitespace();
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("testtesttest");
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertFalse(tokenQueue.matchesWord());

        tokenQueue.consumeWord();
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume("test");
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("testtesttest");
        Assert.assertEquals("test", tokenQueue.consume());
        Assert.assertEquals("esttesttest", tokenQueue.consumeToIgnoreCase());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesthello");
        Assert.assertEquals("test", tokenQueue.consumeTo("test").toString());
        Assert.assertEquals("testtesthello", tokenQueue.consumeToIgnoreCase());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("testtesthellohellotest");
        Assert.assertEquals("test", tokenQueue.consumeToAny("test").toString());
        Assert.assertEquals("hellohellotest", tokenQueue.consumeToIgnoreCase());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(test)(test)hello");
        String data = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("test(test)hello", tokenQueue.consumeToIgnoreCase());
        Assert.assertEquals("testtest", data);

        tokenQueue.consume("(");
        data = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("testtesthello", tokenQueue.consumeToIgnoreCase());
        Assert.assertEquals("test", data);
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("\\test\\", TokenQueue.unescape("\\test\\"));
        Assert.assertEquals("test", TokenQueue.unescape("test"));
    }
}