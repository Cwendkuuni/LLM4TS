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
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("test");
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("testtest");
        Assert.assertTrue(queue.matches("test"));
        Assert.assertFalse(queue.matches("invalid"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("TestTest");
        Assert.assertTrue(queue.matchesCS("Test"));
        Assert.assertFalse(queue.matchesCS("invalid"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("testtestinvalidtest");
        Assert.assertTrue(queue.matchesAny("test", "test"));
        Assert.assertFalse(queue.matchesAny("invalid"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("testtestinvalidtest");
        Assert.assertTrue(queue.matchesAny('t', 'e', 's', 't'));
        Assert.assertFalse(queue.matchesAny('i'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<test>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // already consumed first character
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("testtesttest");
        Assert.assertTrue(queue.matchChomp("test"));
        Assert.assertEquals("testtesttest", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace()); // already consumed first character

        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("testtesttest");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord()); // already consumed first character
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptySeq() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeLongerSeqThanLeft() {
        TokenQueue queue = new TokenQueue("testtesttest");
        queue.consume("testtesttest");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("testtesttest");
        Assert.assertEquals("testtest", queue.consumeTo("testtest"));
        Assert.assertEquals("test", queue.consumeTo("test"));
        Assert.assertEquals("test", queue.consumeTo("testtest"));
        Assert.assertEquals("", queue.consumeTo("test"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("TestTestTest");
        Assert.assertEquals("TestTest", queue.consumeToIgnoreCase("TestTest"));
        Assert.assertEquals("Test", queue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("Test", queue.consumeToIgnoreCase("TestTest"));
        Assert.assertEquals("", queue.consumeToIgnoreCase("Test"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("testtest", queue.consumeToAny("test", "test"));
        Assert.assertEquals("test", queue.consumeToAny("test"));
        Assert.assertEquals("test", queue.consumeToAny("testtest"));
        Assert.assertEquals("", queue.consumeToAny("test"));
    }

    @Test
    public void testConsumeToAnyChar() {
        TokenQueue queue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("testtest", queue.consumeToAny('t', 'e', 's', 't'));
        Assert.assertEquals("test", queue.consumeToAny('t'));
        Assert.assertEquals("test", queue.consumeToAny("testtest"));
        Assert.assertEquals("", queue.consumeToAny("test"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        Assert.assertEquals("(()))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("((())()\"test\"())");
        Assert.assertEquals("(()))\"test\"", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("((())()\\test()");
        Assert.assertEquals("(()))\\test()", queue.chompBalanced('(', ')'));

        queue = new TokenQueue("((())()\"test\"())\"test\"");
        Assert.assertEquals("(()))\"test\"", queue.chompBalanced('(', ')'));
        Assert.assertEquals("\"test\"", queue.toString());

        queue = new TokenQueue("((())()\"test\"())\"test\"");
        Assert.assertEquals("(()))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("\"test\"\"test\"", queue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        Assert.assertEquals("\\", TokenQueue.unescape("\\"));
        Assert.assertEquals("", TokenQueue.unescape(""));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("test", queue.consumeWord());
        Assert.assertEquals("testtest", queue.toString());

        queue.advance();
        Assert.assertEquals("testtest", queue.consumeWord());
        Assert.assertEquals("test", queue.toString());

        queue.advance();
        Assert.assertEquals("test", queue.consumeWord());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("testtagtesttag");
        Assert.assertEquals("test", queue.consumeTagName());
        Assert.assertEquals("tagtesttag", queue.toString());

        queue.advance();
        Assert.assertEquals("tagtest", queue.consumeTagName());
        Assert.assertEquals("tag", queue.toString());

        queue.advance();
        Assert.assertEquals("tag", queue.consumeTagName());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("testtagtesttag.class:test-class testtagtesttag.class:test-class");
        Assert.assertEquals("testtag", queue.consumeElementSelector());
        Assert.assertEquals("testtag.class:test-class testtag", queue.toString());

        queue.advance();
        Assert.assertEquals("testtag.class:test-class", queue.consumeElementSelector());
        Assert.assertEquals("test", queue.toString());

        queue.advance();
        Assert.assertEquals("test", queue.consumeElementSelector());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("testtestIDtestIDtesttestID");
        Assert.assertEquals("testID", queue.consumeCssIdentifier());
        Assert.assertEquals("testtestIDtestIDtesttestID", queue.toString());

        queue.advance();
        Assert.assertEquals("testtestID", queue.consumeCssIdentifier());
        Assert.assertEquals("testIDtesttestID", queue.toString());

        queue.advance();
        Assert.assertEquals("testID", queue.consumeCssIdentifier());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("testtestattribute:testattribute:testtestattribute:testattribute");
        Assert.assertEquals("testattribute", queue.consumeAttributeKey());
        Assert.assertEquals("testtestattribute:testattribute:testtestattribute:testattribute", queue.toString());

        queue.advance();
        Assert.assertEquals("testtestattribute", queue.consumeAttributeKey());
        Assert.assertEquals("testattribute:testtestattribute", queue.toString());

        queue.advance();
        Assert.assertEquals("testattribute", queue.consumeAttributeKey());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("testtesttest");
        Assert.assertEquals("testtest", queue.toString());
        Assert.assertEquals("test", queue.remainder());
    }
}