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
 public void testPeekAndAddFirst() {
 TokenQueue queue = new TokenQueue("abcdef");
 Assert.assertEquals('a', queue.peek());
 queue.addFirst('X');
 Assert.assertEquals('X', queue.peek());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("abc123def");
 Assert.assertTrue(queue.matches("123"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAddFirst() {
 TokenQueue queue = new TokenQueue("abc");
 queue.addFirst(null);
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("abc123def");
 Assert.assertTrue(queue.matchChomp("123"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("123abc123");
 Assert.assertTrue(queue.matchesAny("1", "2"));
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" abc ");
 Assert.assertTrue(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("abcd123efg");
 Assert.assertTrue(queue.matchesWord());
 }

 @Test
 public void testConsumeAndAdvance() {
 TokenQueue queue = new TokenQueue("abcdef");
 Assert.assertEquals('a', queue.consume());
 Assert.assertEquals('b', queue.consume());
 Assert.assertEquals('c', queue.consume());
 Assert.assertEquals(3, queue.remainingLength());
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String escaped = "\\\\abc\\\\def\\\\\\\\";
 Assert.assertEquals("abcdef\\", TokenQueue.unescape(escaped));
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue(" abc def ");
 Assert.assertTrue(queue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("abcdef");
 Assert.assertEquals("def", queue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("div id=\"class1\" class=\"class2\"");
 Assert.assertEquals("div", queue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue queue = new TokenQueue("div#id1.class1.class2");
 Assert.assertEquals("div", queue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("id1.class1.class2");
 Assert.assertEquals("id1", queue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("class=\"class1\" id=\"id1\"");
 Assert.assertEquals("class", queue.consumeAttributeKey());
 }
}