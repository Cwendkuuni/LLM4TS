import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private String data = "abcdef<tag>123<tag2>456<tag3>789";
 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 tokenQueue = new TokenQueue(data);
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyQueue = new TokenQueue("");
 assertTrue(emptyQueue.isEmpty());
 tokenQueue = new TokenQueue("abcdef");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('z');
 assertEquals('z', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue.advance();
 assertTrue(tokenQueue.matches("bcdef"));
 tokenQueue.advance();
 assertFalse(tokenQueue.matches("def"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesCS("<tag>"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();

 Character[] seq = new Character[]{'<', 't', 'a', 'g'};
 assertTrue(tokenQueue.matchesAny(seq));
 }

 @Test
 public void testMatchesAnyWithString() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();

 String[] seq = new String[]{"tag", "TAG"};
 assertTrue(tokenQueue.matchesAny(seq));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst(''); // add empty string to test the edge case
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchChomp("<tag>"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(' ');
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst('a');
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue.consume();
 assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testConsumeString() {
 String expected = "bcdef";
 tokenQueue.consume("bcdef");
 assertEquals("", tokenQueue.toString());
 assertEquals(expected, remainder());
 }

 @Test
 public void testConsumeTo() {
 String expected = "one (two) three";
 tokenQueue.consumeTo("<(one (two) three)");
 assertEquals(expected, chompBalanced('(', ')'));
 }

 @Test
 public void testChompBalanced() {
 String expected = "one (two) three";
 assertEquals(expected, chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String escaped = "\\<tag\\>";
 String expected = "<tag>";
 assertEquals(expected, unescape(escaped));
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 String expected = "tagname";
 assertEquals(expected, consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 String expected = "tag";
 assertEquals(expected, consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 String expected = "tag|namespace";
 assertEquals(expected, consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 String expected = "identifier";
 assertEquals(expected, consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 String expected = "attributeKey";
 assertEquals(expected, consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 String expected = "456<tag2>789";
 assertEquals(expected, remainder());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToWithInvalidSequence() {
 tokenQueue.consumeTo("invalidSequence");
 }

 @Test
 public void testConsumeToAny() {
 String expected = "123<tag2>";
 assertEquals(expected, consumeToAny("tag2"));
 }

 @Test
 public void testConsumeToAnyWithEmptySequence() {
 assertEquals("", consumeToAny());
 }

 @Test
 public void testChompTo() {
 String expected = "one (two) three";
 assertEquals(expected, chompTo("<(one (two) three)"));
 }

 @Test
 public void testChompToIgnoreCase() {
 String expected = "one (two) three";
 assertEquals(expected, chompToIgnoreCase("<(ONE (TWO) THREE)"));
 }

 @Test
 public void testChompBalancedWithInvalidOpenOrClose() {
 assertEquals("", chompBalanced('a', ')'));
 assertEquals("", chompBalanced('(', 'z'));
 }

 @Test
 public void testChompBalancedWithUnescapedOpenOrClose() {
 assertEquals("", chompBalanced('(', '{'));
 assertEquals("", chompBalanced(')', ']'));
 }
}