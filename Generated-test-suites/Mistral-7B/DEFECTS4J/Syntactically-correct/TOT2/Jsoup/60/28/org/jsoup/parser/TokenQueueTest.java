import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals('a', queue.peek());
        queue.consume();
        Assert.assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("def");
        Assert.assertEquals("defabc", queue.toString());
    }

    @Test
    public void testAddFirstChar() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        Assert.assertEquals("xabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertFalse(queue.matches("ac"));
        queue.consume();
        Assert.assertTrue(queue.matches("c"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertTrue(queue.matchesCS("ab"));
        Assert.assertFalse(queue.matchesCS("ac"));
        queue.consume();
        Assert.assertFalse(queue.matchesCS("c"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertTrue(queue.matchesAny("ab", "bc"));
        Assert.assertFalse(queue.matchesAny("ac"));
        queue.consume();
        Assert.assertTrue(queue.matchesAny("c"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertTrue(queue.matchesAny('a', 'b'));
        Assert.assertFalse(queue.matchesAny('c'));
        queue.consume();
        Assert.assertTrue(queue.matchesAny('c'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html>");
        Assert.assertTrue(queue.matchesStartTag());
        queue.consume();
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<def>");
        Assert.assertTrue(queue.matchChomp("abc"));
        Assert.assertEquals("def", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertTrue(queue.matchesWord());
        queue.consume();
        Assert.assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume();
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals("c", queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        String result = queue.consumeTo("def");
        Assert.assertEquals("abc", result);
        Assert.assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        String result = queue.consumeToIgnoreCase("Def");
        Assert.assertEquals("AbC", result);
        Assert.assertEquals("dEf", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        String result = queue.consumeToAny("de", "f");
        Assert.assertEquals("abc", result);
        Assert.assertEquals("def", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        String result = queue.chompBalanced('(', ')');
        Assert.assertEquals("()()", result);
        Assert.assertEquals("()()", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\abc\\def\\";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("abcdef", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("abc", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc");
        String result = queue.consumeWord();
        Assert.assertEquals("abc", result);
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html>");
        String result = queue.consumeTagName();
        Assert.assertEquals("html", result);
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#myId.class1.class2 span.class3");
        String result = queue.consumeElementSelector();
        Assert.assertEquals("div", result);
        Assert.assertEquals("#myId.class1.class2 span.class3", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: #myRed; border: solid 1px #myBlue;");
        String result = queue.consumeCssIdentifier();
        Assert.assertEquals("color", result);
        Assert.assertEquals(": #myRed; border: solid 1px #myBlue;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"className\" id=\"myId\"");
        String result = queue.consumeAttributeKey();
        Assert.assertEquals("class", result);
        Assert.assertEquals("=\"className\" id=\"myId\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        String result = queue.remainder();
        Assert.assertEquals("def", result);
        Assert.assertEquals("abc", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals("abc", queue.toString());
    }
}