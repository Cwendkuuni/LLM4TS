import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.*;

public class TokenQueueTest {

 // Test for constructor
 @Test
 public void testTokenQueueConstructor() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 assertEquals("Hello World", tokenQueue.queue);
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 assertTrue(tokenQueue.isEmpty());

 TokenQueue tokenQueue1 = new TokenQueue("Hello World");
 assertFalse(tokenQueue1.isEmpty());
 }

 // Test for peek method
 @Test
 public void testPeek() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 assertEquals('H', tokenQueue.peek());
 }

 // Test for addFirst method (String)
 @Test
 public void testAddFirstString() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 tokenQueue.addFirst("Test");
 assertEquals("TestHello World", tokenQueue.queue);
 }

 // Test for addFirst method (Character)
 @Test
 public void testAddFirstCharacter() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 tokenQueue.addFirst('T');
 assertEquals("THello World", tokenQueue.queue);
 }

 // Test for matches method (String)
 @Test
 public void testMatches() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 assertTrue(tokenQueue.matches("llo"));
 }

 // Test for matches method (String) with case sensitivity
 @Test
 public void testMatchesCS() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 assertFalse(tokenQueue.matchesCS("llo"));
 }

 // Test for matchesAny method (String...) with multiple strings
 @Test
 public void testMatchesAny() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 assertTrue(tokenQueue.matchesAny("llo", "ld"));
 }

 // Test for matchesAny method (char...)
 @Test
 public void testMatchesAnyChar() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 assertTrue(tokenQueue.matchesAny('l', 'd'));
 }

 // Test for matchesStartTag method
 @Test
 public void testMatchesStartTag() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body></body></html>");
 assertTrue(tokenQueue.matchesStartTag());
 }

 // Test for matchChomp method (String)
 @Test
 public void testMatchChomp() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body><div>Test</div></body></html>");
 assertTrue(tokenQueue.matchChomp("div"));
 }

 // Test for matchesWhitespace method
 @Test
 public void testMatchesWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" Hello World ");
 assertTrue(tokenQueue.matchesWhitespace());
 }

 // Test for matchesWord method
 @Test
 public void testMatchesWord() {
 TokenQueue tokenQueue = new TokenQueue("12345WordWordWord");
 assertTrue(tokenQueue.matchesWord());
 }

 // Test for advance method
 @Test
 public void testAdvance() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 tokenQueue.advance();
 assertEquals('H', tokenQueue.queue.charAt(0));
 assertEquals('e', tokenQueue.queue.charAt(tokenQueue.pos));
 }

 // Test for consume method (String)
 @Test
 public void testConsume() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 tokenQueue.consume("llo");
 assertEquals("o World", tokenQueue.queue);
 }

 // Test for consumeTo method (String)
 @Test
 public void testConsumeTo() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body><div>Test</div></body></html>");
 assertEquals("div", tokenQueue.consumeTo("div"));
 assertEquals("</body></html>", tokenQueue.queue);
 }

 // Test for consumeToIgnoreCase method (String)
 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body><div>Test</div></body></html>");
 assertEquals("div", tokenQueue.consumeToIgnoreCase("DIV"));
 assertEquals("</body></html>", tokenQueue.queue);
 }

 // Test for consumeToAny method (String...)
 @Test
 public void testConsumeToAny() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body><div>Test</div></body></html>");
 assertEquals("Test", tokenQueue.consumeToAny("div", "Test"));
 assertEquals("</body></html>", tokenQueue.queue);
 }

 // Test for chompBalanced method (char, char)
 @Test
 public void testChompBalanced() {
 TokenQueue tokenQueue = new TokenQueue("(())()()()");
 assertEquals("()()()", tokenQueue.chompBalanced('(', ')'));
 }

 // Test for unescape method
 @Test
 public void testUnescape() {
 String in = "\\<tag\\>\\<another\\>tag\\:\\/another\\>";
 String out = TokenQueue.unescape(in);
 assertEquals("<tag><another>tag:</another>", out);
 }

 // Test for consumeWhitespace method
 @Test
 public void testConsumeWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" Hello World ");
 assertTrue(tokenQueue.consumeWhitespace());
 assertEquals("Hello World", tokenQueue.queue);
 }

 // Test for consumeWord method
 @Test
 public void testConsumeWord() {
 TokenQueue tokenQueue = new TokenQueue("12345WordWordWord");
 assertEquals("WordWord", tokenQueue.consumeWord());
 assertEquals("Word", tokenQueue.queue);
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body><div>Test</div></body></html>");
 assertEquals("div", tokenQueue.consumeTagName());
 assertEquals("Test", tokenQueue.queue);
 }

 // Test for consumeElementSelector method
 @Test
 public void testConsumeElementSelector() {
 TokenQueue tokenQueue = new TokenQueue("<html xmlns:x=\"http://www.w3.org/2000/xmlns/\"> <x:Test class=\"TestClass\" id=\"testId\"></x:Test>");
 assertEquals("Test", tokenQueue.consumeElementSelector());
 assertEquals("class=\"TestClass\" id=\"testId\"", tokenQueue.queue);
 }

 // Test for consumeCssIdentifier method
 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue tokenQueue = new TokenQueue("div.class1.class2 { color: red; }");
 assertEquals("class1", tokenQueue.consumeCssIdentifier());
 assertEquals("class2", tokenQueue.consumeCssIdentifier());
 assertEquals("{ color: red; }", tokenQueue.queue);
 }

 // Test for consumeAttributeKey method
 @Test
 public void testConsumeAttributeKey() {
 TokenQueue tokenQueue = new TokenQueue("<img src=\"image.jpg\" alt=\"Test\" width=\"300\" height=\"300\">");
 assertEquals("src", tokenQueue.consumeAttributeKey());
 assertEquals("alt", tokenQueue.consumeAttributeKey());
 assertEquals("width=\"300\" height=\"300\"", tokenQueue.queue);
 }

 // Test for remainder method
 @Test
 public void testRemainder() {
 TokenQueue tokenQueue = new TokenQueue("<html><head></head><body><div>Test</div></body></html>");
 assertEquals("</body></html>", tokenQueue.remainder());
 }
}