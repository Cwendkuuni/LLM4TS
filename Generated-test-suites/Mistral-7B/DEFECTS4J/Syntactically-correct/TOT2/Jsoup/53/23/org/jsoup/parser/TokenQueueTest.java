import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("hello world", queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("hello");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('x');
        Assert.assertEquals('x', queue.consume());
        Assert.assertEquals('h', queue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matches("he"));
        Assert.assertTrue(queue.matches("hello"));
        Assert.assertFalse(queue.matches("world"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("Hello"));
        Assert.assertFalse(queue.matchesCS("world"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny("he", "lo"));
        Assert.assertFalse(queue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny('h', 'l'));
        Assert.assertFalse(queue.matchesAny('x'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchesStartTag());
        queue.advance();
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.matchChomp("he"));
        Assert.assertTrue(queue.matchChomp("hello"));
        Assert.assertFalse(queue.matchChomp("world"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world  ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.advance();
        Assert.assertEquals('e', queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testAdvanceEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.advance();
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals('e', queue.consume());
        // ...
        Assert.assertEquals('d', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeTo("hello"));
        Assert.assertEquals("world", queue.toString());
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertEquals("Hello", queue.consumeToIgnoreCase("hello"));
        Assert.assertEquals("World", queue.toString());
    }

    @Test
    public void testConsumeAny() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeToAny("he", "lo"));
        Assert.assertEquals("world", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        Assert.assertEquals("((()))", queue.chompBalanced('(', ')'));
        Assert.assertEquals(")", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\((\\)())";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals("((()))", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("hello world", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertEquals("world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("<head>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class p#id > span:first-child");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals(".class p#id > span:first-child", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: #f00;");
        Assert.assertEquals("color", queue.consumeCssIdentifier());
        Assert.assertEquals("background-color: #f00;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("style='color: red;' class='class' id='id'");
        Assert.assertEquals("style", queue.consumeAttributeKey());
        Assert.assertEquals("class='class' id='id'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello world");
        String remainder = queue.remainder();
        Assert.assertEquals(" world", remainder);
        Assert.assertTrue(queue.isEmpty());
    }
}