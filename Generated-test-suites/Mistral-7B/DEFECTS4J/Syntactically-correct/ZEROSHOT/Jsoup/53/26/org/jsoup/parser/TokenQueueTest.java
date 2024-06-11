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
 public void testPeek() {
 TokenQueue queue = new TokenQueue("abcdef");
 Assert.assertEquals('a', queue.peek());
 }

 @Test
 public void testAddFirst() {
 TokenQueue queue = new TokenQueue("abcdef");
 queue.addFirst('x');
 Assert.assertEquals('x', queue.peek());
 }

 @Test
 public void testAddFirstString() {
 TokenQueue queue = new TokenQueue("abcdef");
 queue.addFirst("xyz");
 Assert.assertEquals('x', queue.peek());
 Assert.assertEquals("xyzabcdef", queue.toString());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("abcabca");
 Assert.assertTrue(queue.matches("ab"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("abcabca");
 Assert.assertTrue(queue.matchesCS("Ab"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("12345");
 Assert.assertTrue(queue.matchesAny("1", "2", "3"));
 }

 @Test
 public void testMatchesAnyChar() {
 TokenQueue queue = new TokenQueue("12345");
 Assert.assertTrue(queue.matchesAny('1', '2', '3'));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<html><head><title>");
 Assert.assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("abcabca");
 Assert.assertTrue(queue.matchChomp("ab"));
 Assert.assertEquals("cabca", queue.toString());
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" \t\r\n hello ");
 Assert.assertTrue(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("abcd1234ef");
 Assert.assertTrue(queue.matchesWord());
 }

 @Test
 public void testAdvance() {
 TokenQueue queue = new TokenQueue("abcdef");
 queue.advance();
 Assert.assertEquals('b', queue.peek());
 }

 @Test
 public void testConsume() {
 TokenQueue queue = new TokenQueue("abcdef");
 Assert.assertEquals('a', queue.consume());
 Assert.assertEquals('b', queue.peek());
 }

 @Test
 public void testConsumeString() {
 TokenQueue queue = new TokenQueue("abcdef");
 Assert.assertEquals("abc", queue.consume(2));
 Assert.assertEquals('f', queue.peek());
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", queue.consumeTo("("));
 Assert.assertEquals(" four", queue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", queue.consumeToIgnoreCase("("));
 Assert.assertEquals(" four", queue.toString());
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue queue = new TokenQueue("12345");
 Assert.assertEquals("123", queue.consumeToAny("1", "2"));
 Assert.assertEquals("45", queue.toString());
 }

 @Test
 public void testConsumeToAnyString() {
 TokenQueue queue = new TokenQueue("12345");
 Assert.assertEquals("123", queue.consumeToAny("1", "2"));
 Assert.assertEquals("45", queue.toString());
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
 Assert.assertEquals(" four", queue.toString());
 }
}