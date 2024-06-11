import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue.addFirst("test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertEquals('t', queue.peek());

        queue.advance();
        Assert.assertEquals('e', queue.peek());

        queue.consume();
        Assert.assertEquals('s', queue.peek());

        queue.consume();
        Assert.assertEquals('t', queue.peek());

        queue.consume();
        Assert.assertEquals('', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("test");

        Assert.assertEquals("testa", queue.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstNull() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst(null);
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test test");
        Assert.assertTrue(queue.matches("test"));
        Assert.assertFalse(queue.matches("test1"));
        Assert.assertFalse(queue.matches("test2"));
        Assert.assertFalse(queue.matches("tes"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Test Test");
        Assert.assertTrue(queue.matchesCS("Test"));
        Assert.assertFalse(queue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("test test test1 test2");
        Assert.assertTrue(queue.matchesAny("test", "test1"));
        Assert.assertTrue(queue.matchesAny("test1", "test"));
        Assert.assertTrue(queue.matchesAny("test1", "test1"));
        Assert.assertFalse(queue.matchesAny("test3"));
        Assert.assertFalse(queue.matchesAny("test1", "test4"));
    }

    @Test
    public void testMatchesAnyEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertFalse(queue.matchesAny());
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertTrue(queue.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(queue.matchesAny('b', 'a', 'c'));
        Assert.assertTrue(queue.matchesAny('c', 'a', 'b'));
        Assert.assertFalse(queue.matchesAny('d'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<test>");
        Assert.assertTrue(queue.matchesStartTag());

        queue.consume();
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("test test");
        Assert.assertTrue(queue.matchChomp("test"));

        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        Assert.assertTrue(queue.matchesWhitespace());

        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("testWord");
        Assert.assertTrue(queue.matchesWord());

        queue.advance();
        Assert.assertTrue(queue.matchesWord());

        queue.consume();
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("test");
        queue.advance();
        Assert.assertEquals('e', queue.peek());

        queue.consume();
        queue.consume();
        Assert.assertEquals('', queue.peek());

        queue.advance();
        Assert.assertEquals('', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNull() {
        TokenQueue queue = new TokenQueue("");
        queue.consume(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNoMatch() {
        TokenQueue queue = new TokenQueue("test");
        queue.consume("test1");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("test test1 test2 test3 test4");
        String result = queue.consumeTo("test1");
        Assert.assertEquals("test1", result);

        Assert.assertEquals("test2 test3 test4", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Test Test1 Test2 Test3 Test4");
        String result = queue.consumeToIgnoreCase("test1");
        Assert.assertEquals("test1", result);

        Assert.assertEquals("Test Test2 Test3 Test4", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("test test1 test2 test3 test4");
        String result = queue.consumeToAny("test", "test1");
        Assert.assertEquals("test", result);

        Assert.assertEquals("test1 test2 test3 test4", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((test) test) test");
        String result = queue.chompBalanced('(', ')');
        Assert.assertEquals("(test) test", result);

        Assert.assertEquals(" test test", queue.toString());
    }

    @Test
    public void testUnescape() {
        TokenQueue queue = new TokenQueue("\\(test\\) \\test\\ ");
        String result = queue.toString();
        Assert.assertEquals("(test) test ", result);

        result = TokenQueue.unescape(result);
        Assert.assertEquals("(test) test ", result);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" test  ");
        boolean result = queue.consumeWhitespace();
        Assert.assertTrue(result);

        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("testWord");
        String result = queue.consumeWord();
        Assert.assertEquals("test", result);

        Assert.assertEquals("Word", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("testTagName");
        String result = queue.consumeTagName();
        Assert.assertEquals("test", result);

        Assert.assertEquals("TagName", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("testElementSelector");
        String result = queue.consumeElementSelector();
        Assert.assertEquals("test", result);

        Assert.assertEquals("ElementSelector", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("testCssIdentifier");
        String result = queue.consumeCssIdentifier();
        Assert.assertEquals("test", result);

        Assert.assertEquals("CssIdentifier", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("testAttributeKey");
        String result = queue.consumeAttributeKey();
        Assert.assertEquals("test", result);

        Assert.assertEquals("AttributeKey", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("test test test test test");
        String result = queue.remainder();
        Assert.assertEquals("test test test test test", queue.toString());
        Assert.assertEquals("test", result);

        queue.consumeTo("test");
        result = queue.remainder();
        Assert.assertEquals("test test test test", queue.toString());
        Assert.assertEquals("test", result);

        queue.consumeTo("test");
        queue.consumeTo("test");
        queue.consumeTo("test");
        result = queue.remainder();
        Assert.assertEquals("test", queue.toString());
        Assert.assertEquals("", result);
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertEquals("test", queue.toString());
    }
}