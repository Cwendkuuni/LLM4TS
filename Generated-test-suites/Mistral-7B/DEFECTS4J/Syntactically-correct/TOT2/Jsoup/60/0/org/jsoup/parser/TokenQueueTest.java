import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertEquals('H', queue.peek());
        Assert.assertEquals('H', queue.consume());
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("12345");
        Assert.assertEquals('1', queue.peek());
        Assert.assertEquals('1', queue.consume());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("Hello World");
        queue.addFirst('X');
        Assert.assertEquals('X', queue.peek());
        Assert.assertEquals('X', queue.consume());
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("def"));
        Assert.assertFalse(queue.matches("xyz"));

        queue = new TokenQueue("Hello <World>!");
        Assert.assertTrue(queue.matches("Hello"));
        Assert.assertTrue(queue.matches("<World>"));
        Assert.assertFalse(queue.matches("x"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.matchesCS("hello"));
        Assert.assertTrue(queue.matchesCS("World"));
        Assert.assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcabc");
        Assert.assertTrue(queue.matchesAny("a", "c"));
        Assert.assertFalse(queue.matchesAny("x", "y"));
        Assert.assertTrue(queue.matchesAny("a", "b", "c"));

        queue = new TokenQueue("Hello <World>!");
        Assert.assertTrue(queue.matchesAny("H", "e", "l", "o", "W", "r", "d"));
        Assert.assertFalse(queue.matchesAny("x", "y"));
        Assert.assertTrue(queue.matchesAny("H", "e", "l", "o", "W", "r", "d", "<"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Title</title></head><body>Body</body></html>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // consume first '<'
        Assert.assertFalse(queue.matchesStartTag()); // consume 'h'
        Assert.assertTrue(queue.matchesStartTag()); // consume 't'
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertFalse(queue.matchChomp("xyz"));
        Assert.assertEquals("defg", queue.toString());

        queue = new TokenQueue("Hello <World>!");
        Assert.assertTrue(queue.matchChomp("Hello"));
        Assert.assertEquals("<World>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd1234efghijk");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNonMatchingSequence() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNonMatchingSequenceLength() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.consume("abcd");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertEquals("abcd", queue.consumeTo("abcd"));
        Assert.assertEquals("efg", queue.toString());

        queue = new TokenQueue("Hello <World>!");
        Assert.assertEquals("Hello", queue.consumeTo("Hello"));
        Assert.assertEquals("<World>", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("abcDEFg");
        String result = queue.consumeToIgnoreCase("AbC");
        Assert.assertEquals("abc", result);
        Assert.assertEquals("DEFg", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcabc");
        Assert.assertEquals("abc", queue.consumeToAny("a", "b", "c"));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("Hello <World>!");
        Assert.assertEquals("Hello", queue.consumeToAny("H", "e", "l", "o", "W", "r", "d", "<"));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        Assert.assertEquals("((()))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("'abc'def'ghi'");
        Assert.assertEquals("'abc'def", queue.chompBalanced('\'', '\''));
        Assert.assertEquals("'ghi'", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\abc\\def\\\\";
        String expected = "abcdef\\";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("abc", queue.consumeWord());
        Assert.assertEquals("def", queue.consumeWord());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd1234efghijk");
        Assert.assertEquals("abcd", queue.consumeWord());
        Assert.assertEquals("1234", queue.consumeWord());
        Assert.assertEquals("efghijk", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Title</title></head><body>Body</body></html>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("head", queue.consumeTagName());
        Assert.assertEquals("title", queue.consumeTagName());
        Assert.assertEquals("body", queue.consumeTagName());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class:hover");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals(":", queue.consume());
        Assert.assertEquals("hover", queue.consumeWord());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: blue;");
        Assert.assertEquals("color", queue.consumeCssIdentifier());
        Assert.assertEquals("red", queue.consumeWord());
        Assert.assertEquals("background-color", queue.consumeCssIdentifier());
        Assert.assertEquals("blue", queue.consumeWord());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1 class2\" id=\"id1\"");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("class1 class2", queue.consumeWord());
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("id1", queue.consumeWord());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertEquals("g", queue.remainder());
        Assert.assertEquals("abcdef", queue.toString());

        queue = new TokenQueue("Hello <World>!");
        Assert.assertEquals("!", queue.remainder());
        Assert.assertEquals("Hello <World>", queue.toString());
    }
}