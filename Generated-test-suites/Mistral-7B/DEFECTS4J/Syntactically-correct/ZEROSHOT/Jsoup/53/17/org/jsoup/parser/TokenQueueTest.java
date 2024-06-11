import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("test<abc>123<def>");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.addFirst("test");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst("test");
 assertEquals("testtest<abc>123<def>", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("test"));
 assertFalse(tokenQueue.matches("def"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("test"));
 assertFalse(tokenQueue.matchesCS("DEF"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(tokenQueue.matchesAny("test", "def"));
 assertFalse(tokenQueue.matchesAny("xyz"));
 }

 @Test
 public void testMatchesAnyChar() {
 assertTrue(tokenQueue.matchesAny('a', 'b'));
 assertFalse(tokenQueue.matchesAny('x'));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.matchChomp("test");
 assertEquals("<abc>123<def>", tokenQueue.toString());
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consumeWord();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.advance();
 assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 assertEquals('t', tokenQueue.consume());
 assertEquals("est<abc>123<def>", tokenQueue.toString());
 }

 @Test
 public void testConsumeString() {
 tokenQueue.consume("test");
 assertEquals("<abc>123<def>", tokenQueue.toString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("abc", tokenQueue.consumeTo("abc"));
 assertEquals("def", tokenQueue.consumeTo("def"));
 assertEquals("123", tokenQueue.consumeTo("123"));
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("abc", tokenQueue.consumeToIgnoreCase("AbC"));
 assertEquals("def", tokenQueue.consumeToIgnoreCase("Def"));
 assertEquals("123", tokenQueue.consumeToIgnoreCase("123"));
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("abc", tokenQueue.consumeToAny("abc", "def"));
 assertEquals("123", tokenQueue.consumeToAny("123", "xyz"));
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 assertEquals("abc", tokenQueue.chompBalanced('(', ')'));
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testUnescape() {
 assertEquals("test<abc>123<def>", TokenQueue.unescape(tokenQueue.toString()));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" test   ");
 assertTrue(tokenQueue.consumeWhitespace());
 assertEquals("test", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 assertEquals("est", tokenQueue.consumeWord());
 assertEquals("abc<def>", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("abc", tokenQueue.consumeTagName());
 assertEquals("<def>", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("abc", tokenQueue.consumeElementSelector());
 assertEquals("<def>", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("abc", tokenQueue.consumeCssIdentifier());
 assertEquals("<def>", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("abc", tokenQueue.consumeAttributeKey());
 assertEquals("<def>", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 assertEquals("def", tokenQueue.remainder());
 assertEquals("", tokenQueue.toString());
 }
}