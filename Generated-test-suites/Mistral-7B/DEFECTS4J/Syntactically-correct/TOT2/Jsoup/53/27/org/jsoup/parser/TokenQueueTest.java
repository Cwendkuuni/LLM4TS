import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testNotEmpty() {
 tokenQueue = new TokenQueue("abc");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("abc");
 assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("");
 tokenQueue.addFirst('a');
 tokenQueue.addFirst("def");
 assertEquals("defa", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("abcdef");
 assertTrue(tokenQueue.matches("ab"));
 }

 @Test
 public void testMatchesCaseSensitive() {
 tokenQueue = new TokenQueue("ABCdef");
 assertFalse(tokenQueue.matchesCS("ab"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("abcdef");
 assertTrue(tokenQueue.matchesAny("ab", "cd"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue = new TokenQueue("abcdef");
 assertTrue(tokenQueue.matchesAny(new char[]{'a', 'e'}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<html>");
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("abc<def>");
 assertTrue(tokenQueue.matchChomp("abc"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" abc ");
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("abcdef");
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("abc");
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("abc");
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.consume());
 assertEquals('c', tokenQueue.consume());
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeSequence() {
 tokenQueue = new TokenQueue("abcdef");
 tokenQueue.consume("ab");
 assertEquals("def", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("ABCdef");
 String consumed = tokenQueue.consumeToIgnoreCase("Ab");
 assertEquals("Ab", consumed);
 assertEquals("def", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("abcdef");
 String consumed = tokenQueue.consumeToAny("ab", "cd");
 assertEquals("cd", consumed);
 assertEquals("abdef", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(abc)def");
 assertEquals("abc", tokenQueue.chompBalanced('(', ')').toString());
 }

 @Test
 public void testUnescape() {
 String in = "\\abc\\def\\\\";
 String out = TokenQueue.unescape(in);
 assertEquals("abcdef\\", out);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeInvalidSequence() {
 tokenQueue = new TokenQueue("abcdef");
 tokenQueue.consume("xyz");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceLongerThanAvailable() {
 tokenQueue = new TokenQueue("abc");
 tokenQueue.consume("xyz");
 }
}