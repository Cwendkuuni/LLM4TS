import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("Hello World");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("Hello World");
 Assert.assertEquals('H', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("Hello World");
 tokenQueue.addFirst('X');
 tokenQueue.addFirst("ABC");
 Assert.assertEquals("ABCHello World", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("Hello World");
 Assert.assertTrue(tokenQueue.matches("World"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("Hello World");
 Assert.assertFalse(tokenQueue.matchesCS("worLd"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("abcdefg");
 Assert.assertTrue(tokenQueue.matchesAny("efg"));
 }

 @Test
 public void testMatchesAnyCharArray() {
 tokenQueue = new TokenQueue("abcdefg");
 Assert.assertTrue(tokenQueue.matchesAny(new char[]{'e', 'f'}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<html><head><title>Test</title></head>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("abc<def>g");
 Assert.assertTrue(tokenQueue.matchChomp("<def>"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" \t\r\n Hello World ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("Hello World");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("Hello World");
 tokenQueue.advance();
 Assert.assertEquals('H', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("Hello World");
 Assert.assertEquals('H', tokenQueue.consume());
 }

 @Test
 public void testConsumeString() {
 tokenQueue = new TokenQueue("Hello World");
 tokenQueue.consume("llo ");
 Assert.assertEquals("World", tokenQueue.toString());
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("abcdefg<xyz>");
 Assert.assertEquals("abcdef", tokenQueue.consumeTo("def"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("ABCD<efg>EFG");
 Assert.assertEquals("ABCD<efg>", tokenQueue.consumeToIgnoreCase("EFG"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("abcXYZxyz");
 Assert.assertEquals("abcXYZ", tokenQueue.consumeToAny("XY", "z"));
 }

 @Test
 public void testConsumeToAnyStringArray() {
 tokenQueue = new TokenQueue("abcXYZxyz");
 Assert.assertEquals("abcXYZ", tokenQueue.consumeToAny(new String[]{"XY", "z"}));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("((()))");
 Assert.assertEquals("((()))", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 tokenQueue = new TokenQueue("\\(foo\\)bar");
 Assert.assertEquals("(foo)bar", TokenQueue.unescape(tokenQueue.toString()));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" \t\r\n Hello World ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 Assert.assertEquals("Hello World", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNotFound() {
 tokenQueue = new TokenQueue("Hello World");
 tokenQueue.consume("foo");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNotLongEnough() {
 tokenQueue = new TokenQueue("Hello World");
 tokenQueue.consume("abcd");
 tokenQueue.consume("efg");
 }
}