import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("test data");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 tokenQueue = new TokenQueue("test data");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("test data");
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekWhenEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.peek();
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.addFirst('A');
 Assert.assertEquals('A', tokenQueue.peek());
 }

 @Test
 public void testAddFirstString() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.addFirst("ABC");
 Assert.assertEquals('A', tokenQueue.peek());
 Assert.assertEquals("ABCtest data", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("test data");
 Assert.assertTrue(tokenQueue.matches("tes"));
 Assert.assertFalse(tokenQueue.matches("data"));
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchesWhenEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.matches("test");
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("Test Data");
 Assert.assertTrue(tokenQueue.matchesCS("Test"));
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchesCSWhenEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.matchesCS("test");
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.matchesAny("es", 't');
 Assert.assertTrue(tokenQueue.matchesAny("es"));
 Assert.assertTrue(tokenQueue.matchesAny('t'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesAnyNull() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.matchesAny(null);
 }

 @Test
 public void testMatchesAnyEmpty() {
 tokenQueue = new TokenQueue("test data");
 Assert.assertFalse(tokenQueue.matchesAny(new String[]{}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<test data>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("test data<test>");
 Assert.assertTrue(tokenQueue.matchChomp("<test>"));
 Assert.assertFalse(tokenQueue.matches("test"));
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchChompWhenEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.matchChomp("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchChompNull() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.matchChomp(null);
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" test data");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("test word data");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.advance();
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testAdvanceWhenEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.advance();
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("test data");
 char consumed = tokenQueue.consume();
 Assert.assertEquals('t', consumed);
 Assert.assertEquals("est data", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWhenEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.consume();
 }

 @Test
 public void testConsumeSequence() {
 tokenQueue = new TokenQueue("test data<test>");
 String consumed = tokenQueue.consume("test");
 Assert.assertEquals(" data<", tokenQueue.toString());
 Assert.assertEquals("test", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceEmpty() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.consume("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeSequenceNull() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.consume(null);
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("test data<test>");
 String consumed = tokenQueue.consumeTo("test");
 Assert.assertEquals(" data", tokenQueue.toString());
 Assert.assertEquals("test", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToEmpty() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.consumeTo("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNull() {
 tokenQueue = new TokenQueue("test data");
 tokenQueue.consumeTo(null);
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(test (data) test) data");
 String consumed = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("test (data) test data", tokenQueue.toString());
 Assert.assertEquals("(test (data)", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testChompBalancedEmpty() {
 tokenQueue = new TokenQueue("");
 tokenQueue.chompBalanced('(', ')');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChompBalancedMismatch() {
 tokenQueue = new TokenQueue("(test data) test");
 tokenQueue.chompBalanced('(', '}');
 }

 @Test
 public void testUnescape() {
 String in = "\\test\\data\\";
 String out = TokenQueue.unescape(in);
 Assert.assertEquals("testdata", out);
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" test data");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 Assert.assertEquals("est data", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("test word data");
 String consumed = tokenQueue.consumeWord();
 Assert.assertEquals("word data", tokenQueue.toString());
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<test TagName data>");
 String consumed = tokenQueue.consumeTagName();
 Assert.assertEquals("TagName", consumed);
 Assert.assertEquals("test data", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("<test element:TagName data>");
 String consumed = tokenQueue.consumeElementSelector();
 Assert.assertEquals("element:TagName", consumed);
 Assert.assertEquals("test data", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue("test #identifier data");
 String consumed = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals("identifier", consumed);
 Assert.assertEquals("test data", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("test attribute:key data");
 String consumed = tokenQueue.consumeAttributeKey();
 Assert.assertEquals("attribute:key", consumed);
 Assert.assertEquals("test data", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("test data");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("data", remainder);
 tokenQueue = new TokenQueue("test data");
 tokenQueue.consumeTo("test");
 String remainder2 = tokenQueue.remainder();
 Assert.assertEquals("", remainder2);
 }
}