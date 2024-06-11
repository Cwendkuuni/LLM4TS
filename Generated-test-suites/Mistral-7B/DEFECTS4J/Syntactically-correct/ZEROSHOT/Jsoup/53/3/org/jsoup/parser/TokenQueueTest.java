import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        String data = "test<tag>data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue instanceof TokenQueue);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "test";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "<tag>test";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('<', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "test";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('[');
        assertEquals("[", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        String data = "<tag>test<tag2>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matches("tag"));
        assertFalse(tokenQueue.matches("tag2"));
    }

    @Test
    public void testMatchesCS() {
        String data = "<TAG>test<tag2>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesCS("TAG"));
        assertFalse(tokenQueue.matchesCS("tag2"));
    }

    @Test
    public void testMatchesAny() {
        String data = "test<tag1>test<tag2>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesAny("tag1", "tag2"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "test<tag1>test<tag2>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesAny(new char[]{'a', 'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<tag>test";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<tag>test<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.matchChomp("tag");
        assertEquals("test<tag>", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " test ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "test<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals("test<", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        String data = "<tag>test";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('t', tokenQueue.consume());
        assertEquals("es", tokenQueue.toString());
    }

    @Test
    public void testConsumeSequence() {
        String data = "<tag>test<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "test";
        tokenQueue.consume("tag");
        assertEquals(expected, tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        String data = "<tag>test<tag2>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "test";
        String result = tokenQueue.consumeTo(expected);
        assertEquals(expected, result);
        assertEquals("tag2", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "<Tag>test<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "test";
        String result = tokenQueue.consumeToIgnoreCase(expected);
        assertEquals(expected, result);
        assertEquals("Tag", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "test<tag1>test<tag2>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] expected = {"tag1", "tag2"};
        String[] result = tokenQueue.consumeToAny(expected);
        assertArrayEquals(expected, result);
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "(test)test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "test";
        String result = tokenQueue.chompBalanced('(', ')');
        assertEquals(expected, result);
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\\\test\\\\";
        String expected = "test";
        String result = TokenQueue.unescape(data);
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " test ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "Word";
        String result = tokenQueue.consumeWord();
        assertEquals(expected, result);
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tagName>test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "tagName";
        String result = tokenQueue.consumeTagName();
        assertEquals(expected, result);
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tagName:element|class>test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "tagName:element|class";
        String result = tokenQueue.consumeElementSelector();
        assertEquals(expected, result);
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id:identifier test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "identifier";
        String result = tokenQueue.consumeCssIdentifier();
        assertEquals(expected, result);
        assertEquals("id:test", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attribute:key test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "attribute:key";
        String result = tokenQueue.consumeAttributeKey();
        assertEquals(expected, result);
        assertEquals(" test", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "<tag>test<tag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String expected = "tag";
        tokenQueue.matchChomp("tag");
        String result = tokenQueue.remainder();
        assertEquals(expected, result);
        assertEquals("", tokenQueue.toString());
    }
}