import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("test");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("test");
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testPeekWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertEquals((char) 0, tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("test");
 tokenQueue.addFirst('a');
 Assert.assertEquals('a', tokenQueue.consume());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("test");
 Assert.assertTrue(tokenQueue.matches("est"));
 }

 @Test
 public void testMatchesWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matches("test"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("Test");
 Assert.assertTrue(tokenQueue.matchesCS("Test"));
 }

 @Test
 public void testMatchesCSWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesCS("Test"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("test");
 Assert.assertTrue(tokenQueue.matchesAny("est"));
 }

 @Test
 public void testMatchesAnyWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesAny("test"));
 }

 @Test
 public void testMatchesAnyWithSingleSequence() {
 tokenQueue = new TokenQueue("test");
 Assert.assertTrue(tokenQueue.matchesAny("t"));
 }

 @Test
 public void testMatchesAnyWithMultipleSequences() {
 tokenQueue = new TokenQueue("test");
 Assert.assertTrue(tokenQueue.matchesAny("es", "est"));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<test>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchesStartTagWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("testtest");
 Assert.assertTrue(tokenQueue.matchChomp("test"));
 }

 @Test
 public void testMatchChompWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchChomp("test"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" test ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWhitespaceWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("test");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testMatchesWordWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("test");
 tokenQueue.advance();
 Assert.assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("test");
 Assert.assertEquals('t', tokenQueue.consume());
 Assert.assertEquals('e', tokenQueue.consume());
 Assert.assertEquals('s', tokenQueue.consume());
 Assert.assertEquals('t', tokenQueue.consume());
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeWithSingleSequence() {
 tokenQueue = new TokenQueue("test");
 String consumed = tokenQueue.consume("est");
 Assert.assertEquals("es", consumed);
 }

 @Test
 public void testConsumeWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.consume("test");
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("testtesttest");
 String consumed = tokenQueue.consumeTo("test");
 Assert.assertEquals("testtest", consumed);
 Assert.assertEquals("test", tokenQueue.consume("test"));
 Assert.assertEquals("test", tokenQueue.consume("test"));
 Assert.assertEquals("test", tokenQueue.consume("test"));
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("TestTeStTeSt");
 String consumed = tokenQueue.consumeToIgnoreCase("Test");
 Assert.assertEquals("TestTeStTeSt", consumed);
 Assert.assertEquals("Test", tokenQueue.consume("Test"));
 Assert.assertEquals("TeStTeSt", tokenQueue.consumeToIgnoreCase("TeSt"));
 Assert.assertEquals("TeSt", tokenQueue.consume("TeSt"));
 Assert.assertEquals("TeSt", tokenQueue.consume("TeSt"));
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("testtesttest");
 String consumed = tokenQueue.consumeToAny("est", "es");
 Assert.assertEquals("testtesttest", consumed);
 Assert.assertEquals("est", tokenQueue.consume("est"));
 Assert.assertEquals("est", tokenQueue.consume("est"));
 Assert.assertEquals("es", tokenQueue.consume("es"));
 Assert.assertEquals("", tokenQueue.consumeToAny("test"));
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(test)(test)");
 String consumed = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("test(test)", consumed);
 }

 @Test
 public void testChompBalancedWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testChompBalancedWithUnmatchedParentheses() {
 tokenQueue = new TokenQueue("(test)");
 String consumed = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("test", consumed);
 Assert.assertEquals("(", tokenQueue.peek());
 }

 @Test
 public void testChompBalancedWithQuotedParentheses() {
 tokenQueue = new TokenQueue("(\\(test\\))");
 String consumed = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testChompBalancedWithEscapedParentheses() {
 tokenQueue = new TokenQueue("\\(test\\)");
 String consumed = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testUnescape() {
 String in = "\\(test\\)";
 String out = TokenQueue.unescape(in);
 Assert.assertEquals("(test)", out);
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" test test ");
 boolean seen = tokenQueue.consumeWhitespace();
 Assert.assertTrue(seen);
 }

 @Test
 public void testConsumeWhitespaceWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 boolean seen = tokenQueue.consumeWhitespace();
 Assert.assertFalse(seen);
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("testwordtest");
 String consumed = tokenQueue.consumeWord();
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeWordWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.consumeWord();
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<tagname>");
 String consumed = tokenQueue.consumeTagName();
 Assert.assertEquals("tagname", consumed);
 }

 @Test
 public void testConsumeTagNameWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.consumeTagName();
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("tagname:foo");
 String consumed = tokenQueue.consumeElementSelector();
 Assert.assertEquals("tagname", consumed);
 }

 @Test
 public void testConsumeElementSelectorWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.consumeElementSelector();
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue("id:test");
 String consumed = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeCssIdentifierWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("name:test");
 String consumed = tokenQueue.consumeAttributeKey();
 Assert.assertEquals("name", consumed);
 }

 @Test
 public void testConsumeAttributeKeyWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 String consumed = tokenQueue.consumeAttributeKey();
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("testtesttest");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("testtesttest", tokenQueue.toString());
 Assert.assertEquals("testtest", remainder);
 }

 @Test
 public void testToString() {
 tokenQueue = new TokenQueue("test");
 Assert.assertEquals("test", tokenQueue.toString());
 }
}