import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 tokenQueue = new TokenQueue("abcdef<tag>123<tag>456");
 }

 /**
 * Test isEmpty method.
 */
 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.consume('a');
 assertFalse(tokenQueue.isEmpty());
 }

 /**
 * Test peek method.
 */
 @Test
 public void testPeek() {
 assertFalse(tokenQueue.isEmpty());
 assertEquals('a', tokenQueue.peek());
 tokenQueue.consume('a');
 assertEquals('b', tokenQueue.peek());
 }

 /**
 * Test addFirst method with a char.
 */
 @Test
 public void testAddFirstChar() {
 tokenQueue.addFirst('z');
 assertEquals('z', tokenQueue.peek());
 tokenQueue.consume('z');
 }

 /**
 * Test addFirst method with a string.
 */
 @Test
 public void testAddFirstString() {
 tokenQueue.addFirst("xyz");
 assertEquals('x', tokenQueue.peek());
 tokenQueue.consume('x');
 assertEquals('y', tokenQueue.peek());
 tokenQueue.consume('y');
 assertEquals('z', tokenQueue.peek());
 }

 /**
 * Test matches method with a case insensitive string.
 */
 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("ef"));
 tokenQueue.consume('e');
 tokenQueue.consume('f');
 assertFalse(tokenQueue.matches("ef"));
 }

 /**
 * Test matchesCS method with a case sensitive string.
 */
 @Test
 public void testMatchesCS() {
 assertFalse(tokenQueue.matchesCS("Tag"));
 tokenQueue.consume('<');
 tokenQueue.consume('t');
 tokenQueue.consume('a');
 tokenQueue.consume('g');
 assertTrue(tokenQueue.matchesCS("Tag"));
 tokenQueue.consume('>');
 assertFalse(tokenQueue.matchesCS("Tag"));
 }

 /**
 * Test matchesAny method with a case insensitive string array.
 */
 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst('1');
 assertTrue(tokenQueue.matchesAny("1", "2"));
 tokenQueue.consume('1');
 assertFalse(tokenQueue.matchesAny("1", "2"));
 tokenQueue.addFirst('2');
 assertTrue(tokenQueue.matchesAny("1", "2"));
 }

 /**
 * Test matchesAny method with a char array.
 */
 @Test
 public void testMatchesAnyChars() {
 tokenQueue.addFirst('>');
 assertTrue(tokenQueue.matchesAny(new char[]{'<', '>'}));
 tokenQueue.consume('>');
 assertFalse(tokenQueue.matchesAny(new char[]{'<', '>'}));
 tokenQueue.addFirst('<');
 assertTrue(tokenQueue.matchesAny(new char[]{'<', '>'}));
 }

 /**
 * Test matchesStartTag method.
 */
 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume('<');
 assertFalse(tokenQueue.matchesStartTag());
 }

 /**
 * Test matchChomp method with a string.
 */
 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("abcdef<tag>123<tag>456");
 assertTrue(tokenQueue.matchChomp("tag"));
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test matchesWhitespace method.
 */
 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consume(' ');
 assertFalse(tokenQueue.matchesWhitespace());
 }

 /**
 * Test matchesWord method.
 */
 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consume('a');
 assertFalse(tokenQueue.matchesWord());
 }

 /**
 * Test advance method.
 */
 @Test
 public void testAdvance() {
 tokenQueue.consume('a');
 tokenQueue.advance();
 assertFalse(tokenQueue.matches('a'));
 }

 /**
 * Test consume method with a char.
 */
 @Test
 public void testConsumeChar() {
 assertEquals('a', tokenQueue.consume());
 assertFalse(tokenQueue.matches('a'));
 }

 /**
 * Test consume method with a string.
 */
 @Test(expected = IllegalStateException.class)
 public void testConsumeString() {
 tokenQueue.consume("tag");
 }

 /**
 * Test consumeTo method with a string.
 */
 @Test
 public void testConsumeTo() {
 String result = tokenQueue.consumeTo("tag");
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test consumeToIgnoreCase method with a string.
 */
 @Test
 public void testConsumeToIgnoreCase() {
 String result = tokenQueue.consumeToIgnoreCase("TAG");
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test consumeToAny method with a string array.
 */
 @Test
 public void testConsumeToAny() {
 String result = tokenQueue.consumeToAny("tag", "123");
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test chompBalanced method
 */
 @Test
 public void testChompBalanced() {
 String result = tokenQueue.chompBalanced('(', ')');
 assertEquals("one (two) three", result);
 assertEquals("456", tokenQueue.toString());
 }

 /**
 * Test unescape method.
 */
 @Test
 public void testUnescape() {
 String result = TokenQueue.unescape("\\(one \\(two\\) three\\) four");
 assertEquals("(one (two) three) four", result);
 }

 /**
 * Test consumeWhitespace method.
 */
 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 tokenQueue.consume(' ');
 assertFalse(tokenQueue.consumeWhitespace());
 }

 /**
 * Test consumeWord method.
 */
 @Test
 public void testConsumeWord() {
 String result = tokenQueue.consumeWord();
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test consumeTagName method.
 */
 @Test
 public void testConsumeTagName() {
 String result = tokenQueue.consumeTagName();
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test consumeElementSelector method.
 */
 @Test
 public void testConsumeElementSelector() {
 String result = tokenQueue.consumeElementSelector();
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test consumeCssIdentifier method.
 */
 @Test
 public void testConsumeCssIdentifier() {
 String result = tokenQueue.consumeCssIdentifier();
 assertEquals("tag", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test consumeAttributeKey method.
 */
 @Test
 public void testConsumeAttributeKey() {
 String result = tokenQueue.consumeAttributeKey();
 assertEquals("class", result);
 assertEquals("123", tokenQueue.toString());
 }

 /**
 * Test remainder method.
 */
 @Test
 public void testRemainder() {
 String result = tokenQueue.remainder();
 assertEquals("123", result);
 }
}