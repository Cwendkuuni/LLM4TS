import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "test data";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals(data, queue.queue);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        String data = "test data";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "test data";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data.charAt(0), queue.peek());
        
        queue.advance();
        assertEquals(data.charAt(1), queue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "test data";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('A');
        assertEquals('A', queue.consume());
        
        queue.addFirst("test");
        assertEquals("test", queue.consumeToIgnoreCase("test"));
    }

    @Test
    public void testMatches() {
        String data = "test data";
        TokenQueue queue = new TokenQueue(data);
        
        assertTrue(queue.matches("test"));
        assertTrue(queue.matches("data"));
        assertFalse(queue.matches("not"));
    }

    @Test
    public void testMatchesCS() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesCS("Test"));
        assertFalse(queue.matchesCS("data"));
    }

    @Test
    public void testMatchesAny() {
        String data = "test data test test test data test test test data test test test data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("test", "data"));
        assertTrue(queue.matchesAny("test", "data", "test"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "test data test test test data test test test data test test test data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny(new char[]{'t', 'd'}));
        assertTrue(queue.matchesAny(new char[]{'t', 'd', 'e'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<test><data>test</data></test>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume first <
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.matchChomp("test");
    }

    @Test
    public void testMatchChomp() {
        String data = "test<test>data<test>test<test>data<test>test<test>data<test>";
        TokenQueue queue = new TokenQueue(data);
        queue.matchChomp("test");
        assertEquals("data<test>data<test>data<test>", queue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " test data ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testWordtestWordtestWordtestWordtestWord";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "test";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals('t', queue.consume());
        queue.advance();
        assertEquals('e', queue.consume());
    }

    @Test
    public void testConsume() {
        String data = "test";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('t', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals("", queue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsume() {
        String data = "test data test data test data test data test data test data test data test data test data";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("test data", queue.consumeToIgnoreCase("test data"));
    }

    @Test
    public void testChompBalanced() {
        String data = "()()()";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("()()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\\\test\\\\\\";
        String unescaped = TokenQueue.unescape(data);
        assertEquals("test\\", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " test data ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String data = "testWordtestWordtestWordtestWordtestWord";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("testWord", queue.consumeWord());
        assertEquals("testWordtestWordtestWordtestWord", queue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<testTag><testTagName>testTagName</testTagName><testTag>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("testTagName", queue.consumeTagName());
        assertEquals("<testTag><testTagName>testTagName</testTagName>", queue.queue);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "testTagName|testTagName2";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("testTagName", queue.consumeElementSelector());
        assertEquals("testTagName|testTagName2", queue.queue);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "testIdentifier testIdentifier2 testIdentifier3";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("testIdentifier", queue.consumeCssIdentifier());
        assertEquals("testIdentifier testIdentifier2 testIdentifier3", queue.queue);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "testAttributeKey testAttributeKey2 testAttributeKey3";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("testAttributeKey", queue.consumeAttributeKey());
        assertEquals("testAttributeKey testAttributeKey2 testAttributeKey3", queue.queue);
    }

    @Test
    public void testRemainder() {
        String data = "test data test data test data test data test data test data test data test data test data test data test data";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("test data test data test data test data test data test data test data test data test data test data test data", queue.remainder());
        assertEquals("", queue.queue);
    }
}