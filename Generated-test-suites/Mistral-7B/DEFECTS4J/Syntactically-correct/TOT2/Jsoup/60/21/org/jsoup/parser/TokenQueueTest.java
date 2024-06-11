import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeekAndAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        Assert.assertEquals('a', tokenQueue.peek());
        tokenQueue.addFirst('x');
        Assert.assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertTrue(tokenQueue.matches("ab"));
        Assert.assertFalse(tokenQueue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEF");
        Assert.assertTrue(tokenQueue.matchesCS("AB"));
        Assert.assertFalse(tokenQueue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertTrue(tokenQueue.matchesAny("ab", "ef"));
        Assert.assertFalse(tokenQueue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyWithEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertFalse(tokenQueue.matchesAny("ab", "ef"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstNull() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstNegative() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumePastEnd() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume(); // throw exception
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        Assert.assertEquals('a', tokenQueue.consume());
        Assert.assertEquals('b', tokenQueue.consume());
        Assert.assertEquals('c', tokenQueue.consume());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatch() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeShortSequence() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume("ab");
        tokenQueue.consume(); // throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeLongSequence() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume("abcdef");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToMismatch() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consumeTo("xyz");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToShort() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        tokenQueue.consumeTo("ab");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToLong() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        tokenQueue.consumeTo("abcd");
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        Assert.assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        Assert.assertEquals(" four", tokenQueue.remainder());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("abc\\def\\\\", TokenQueue.unescape("abc\\def\\\\\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  abc  def ");
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("abc", tokenQueue.consumeWord());
        Assert.assertEquals("def", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abc def");
        Assert.assertEquals("abc", tokenQueue.consumeWord());
        Assert.assertEquals("def", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<div class=\"container\">");
        Assert.assertEquals("div", tokenQueue.consumeTagName());
        Assert.assertEquals("container", tokenQueue.consumeTagName());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("table.example td.highlight");
        Assert.assertEquals("table", tokenQueue.consumeElementSelector());
        Assert.assertEquals(".example", tokenQueue.consumeElementSelector());
        Assert.assertEquals("td", tokenQueue.consumeElementSelector());
        Assert.assertEquals(".highlight", tokenQueue.consumeElementSelector());
        Assert.assertTrue(tokenQueue.isEmpty());
    }
}