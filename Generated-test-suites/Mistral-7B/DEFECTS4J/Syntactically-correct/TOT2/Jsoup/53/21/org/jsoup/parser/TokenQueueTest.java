import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private String data = "abcdef<abc>(123)def<xyz>456";
 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue(data);
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyTokenQueue = new TokenQueue("");
 assertTrue(emptyTokenQueue.isEmpty());
 tokenQueue.advance();
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('a', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('x');
 assertEquals('x', tokenQueue.peek());
 tokenQueue.addFirst("hello world");
 assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue.advance();
 assertTrue(tokenQueue.matches("ab"));
 tokenQueue.advance();
 assertTrue(tokenQueue.matches("ef"));
 tokenQueue.advance();
 assertFalse(tokenQueue.matches("xyz"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesCS("ab"));
 tokenQueue.advance();
 assertFalse(tokenQueue.matchesCS("ef"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst('[');
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesAny("(", "[", "{"));
 tokenQueue.advance();
 assertFalse(tokenQueue.matchesAny(")", "]", "}"));
 }

 @Test
 public void testMatchesAnyWithCharacterArray() {
 tokenQueue.addFirst('[');
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesAny(new char[]{'[', '(', '{'}));
 tokenQueue.advance();
 assertFalse(tokenQueue.matchesAny(new char[]{' ', ')'}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume();
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("<abc");
 tokenQueue.advance();
 tokenQueue.addFirst('/');
 tokenQueue.advance();
 assertTrue(tokenQueue.matchChomp("<abc"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(' ');
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.advance();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst('a');
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consumeWord();
 tokenQueue.consumeWord();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyQueue() {
 tokenQueue.consume("abc");
 }

 @Test
 public void testConsumeTo() {
 String expected = "abc(123)def";
 assertEquals(expected, tokenQueue.consumeTo("(123)def"));
 assertEquals("", tokenQueue.consumeTo("xyz"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 String expected = "abc(123)def";
 assertEquals(expected, tokenQueue.consumeToIgnoreCase("(123)DEF"));
 assertEquals("xyz", tokenQueue.consumeToIgnoreCase("XYZ"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst('[');
 tokenQueue.advance();
 assertEquals("abcdef<xyz>456", tokenQueue.consumeToAny("abc", "xyz"));
 }

 @Test
 public void testChompBalanced() {
 String expected = "abc(123)def";
 assertEquals(expected, tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String escaped = "\\(abc\\)def\\<xyz\\>";
 String unescaped = TokenQueue.unescape(escaped);
 assertEquals("(abc)def<xyz>", unescaped);
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 tokenQueue.consumeWhitespace();
 assertFalse(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 assertEquals("abc", tokenQueue.consumeWord());
 tokenQueue.consumeWord();
 assertEquals("def", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("abc", tokenQueue.consumeTagName());
 tokenQueue.consume();
 assertEquals("xyz", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("abc", tokenQueue.consumeElementSelector());
 tokenQueue.consume();
 assertEquals("xyz", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("abc", tokenQueue.consumeCssIdentifier());
 tokenQueue.consume();
 assertEquals("xyz", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("abc", tokenQueue.consumeAttributeKey());
 tokenQueue.consume();
 assertEquals("xyz", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 assertEquals("456", tokenQueue.remainder());
 }
}