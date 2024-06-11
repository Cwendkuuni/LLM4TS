import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertNotNull(tokenQueue);
        Assert.assertEquals(data, tokenQueue.queue);
        Assert.assertEquals(0, tokenQueue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.isEmpty());
        
        data = "abc";
        tokenQueue = new TokenQueue(data);
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeekAndConsume() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals((char) 'a', tokenQueue.peek());
        Assert.assertEquals("abcd", tokenQueue.consume(2).toString());
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("123");
        Assert.assertEquals("123abcdef", tokenQueue.queue);
        Assert.assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testMatches() {
        String data = "abcabc";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matches("ab"));
        Assert.assertFalse(tokenQueue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        String data = "ABCabc";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesCS("ABC"));
        Assert.assertFalse(tokenQueue.matchesCS("ABc"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcabc";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesAny("ab", "cd"));
        Assert.assertFalse(tokenQueue.matchesAny("xy", "cd"));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<abc><def";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "abc<def>";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchChomp("<def>"));
        Assert.assertEquals(2, tokenQueue.pos);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("ab");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consumeTo("ab");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithInsufficientData() {
        String data = "abcd";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consumeTo("efgh");
    }

    @Test
    public void testConsumeTo() {
        String data = "abcdabcd";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("abcd", tokenQueue.consumeTo("abcd").toString());
        Assert.assertEquals("abcd", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "AbCdAbCd";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("AbCd", tokenQueue.consumeToIgnoreCase("abcd").toString());
        Assert.assertEquals("AbCd", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcdabcd";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("abcd", tokenQueue.consumeToAny("ab", "ef").toString());
        Assert.assertEquals("cd", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testChompBalanced() {
        String data = "((())())";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("((())", tokenQueue.chompBalanced('(', ')').toString());
        Assert.assertEquals("()))", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testUnescape() {
        String data = "\\\\\\\\\\\\";
        String escaped = TokenQueue.unescape(data);
        Assert.assertEquals("\\\\\\\\", escaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc   ";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("abc", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeWord() {
        String data = "abcdabcd";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("abcd", tokenQueue.consumeWord().toString());
        Assert.assertEquals("abcd", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tagname attr=\"value\" />";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("tagname", tokenQueue.consumeTagName().toString());
        Assert.assertEquals(" attr=\"value\" />", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div#id.class:hover";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("div", tokenQueue.consumeElementSelector().toString());
        Assert.assertEquals("#id.class:hover", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".class1.class2";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("class1", tokenQueue.consumeCssIdentifier().toString());
        Assert.assertEquals(".class2", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attr=\"value\"";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("attr", tokenQueue.consumeAttributeKey().toString());
        Assert.assertEquals("=\"value\"", tokenQueue.queue.substring(tokenQueue.pos));
    }

    @Test
    public void testRemainder() {
        String data = "abcdabcd";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("cd", tokenQueue.remainder().toString());
    }

    @Test
    public void testToString() {
        String data = "abcdabcd";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("cd", tokenQueue.toString().toString());
    }
}