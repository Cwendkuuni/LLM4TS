import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 @Test
 public void testConstructor() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matches("hello"));
 }

 @Test
 public void testIsEmpty() {
 TokenQueue queue = new TokenQueue("");
 assertTrue(queue.isEmpty());
 TokenQueue queue2 = new TokenQueue("hello world");
 assertFalse(queue2.isEmpty());
 }

 @Test
 public void testPeek() {
 TokenQueue queue = new TokenQueue("hello world");
 assertEquals('h', queue.peek());
 }

 @Test
 public void testAddFirst() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.addFirst('a');
 assertEquals('a', queue.peek());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matches("hello"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("hello world");
 assertFalse(queue.matchesCS("HELLO"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matchesAny("hello", "world"));
 }

 @Test
 public void testMatchesAnyChar() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matchesAny(new char[]{'h', 'w'}));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
 assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
 assertTrue(queue.matchChomp("<html>"));
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" hello world ");
 assertTrue(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matchesWord());
 }

 @Test
 public void testAdvance() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.advance();
 assertEquals('h', queue.peek());
 }

 @Test
 public void testConsume() {
 TokenQueue queue = new TokenQueue("hello world");
 assertEquals('h', queue.consume());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithInvalidSequence() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.consume("not valid sequence");
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("hello world <html>");
 assertEquals("hello world", queue.consumeTo("<html>"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue queue = new TokenQueue("Hello World <HTML>");
 assertEquals("Hello World", queue.consumeToIgnoreCase("html"));
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue queue = new TokenQueue("hello world <html>");
 assertEquals("hello world", queue.consumeToAny("html", "world"));
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("(hello (world) (world))");
 assertEquals("hello (world) (world)", queue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String in = "\\\\\\";
 String out = TokenQueue.unescape(in);
 assertEquals("\\\\\\\\", out);
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue(" hello world ");
 assertTrue(queue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("hello world");
 assertEquals("hello", queue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
 assertEquals("html", queue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
 assertEquals("html", queue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("class:myClass id:myId");
 assertEquals("class", queue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("class:myClass id:myId");
 assertEquals("class", queue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 TokenQueue queue = new TokenQueue("hello world <html>");
 assertEquals("html", queue.remainder());
 }
}