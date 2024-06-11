package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

 @Test
 public void testEmptyQueue() {
 TokenQueue queue = new TokenQueue("");
 TokenQueue emptyQueue = new TokenQueue(null);

 Assert.assertEquals(true, queue.isEmpty());
 Assert.assertEquals(true, emptyQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 TokenQueue queue = new TokenQueue("abcde");

 Assert.assertEquals('a', queue.peek());
 Assert.assertEquals('a', queue.consume());
 Assert.assertEquals(4, queue.remainingLength());

 Assert.assertEquals(0, emptyQueue.peek());
 }

 @Test
 public void testAddFirst() {
 TokenQueue queue = new TokenQueue("");

 queue.addFirst('a');
 queue.addFirst("def");

 Assert.assertEquals('d', queue.consume());
 Assert.assertEquals('e', queue.consume());
 Assert.assertEquals('a', queue.consume());
 Assert.assertEquals(1, queue.remainingLength());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("abcdefghijk");

 Assert.assertEquals(true, queue.matches("ab"));
 Assert.assertEquals(true, queue.matches("cd"));
 Assert.assertEquals(false, queue.matches("ef"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("ABCDEFGHIJK");

 Assert.assertEquals(true, queue.matchesCS("ab"));
 Assert.assertEquals(true, queue.matchesCS("CD"));
 Assert.assertEquals(false, queue.matchesCS("EF"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("abcdefghijk");

 Assert.assertEquals(true, queue.matchesAny("ab", "cd"));
 Assert.assertEquals(false, queue.matchesAny("ef", "xy"));
 }

 @Test
 public void testMatchesAnyChars() {
 TokenQueue queue = new TokenQueue("abcdefghijk");

 Assert.assertEquals(true, queue.matchesAny('a', 'b', 'c'));
 Assert.assertEquals(false, queue.matchesAny('x', 'y', 'z'));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");

 Assert.assertEquals(true, queue.matchesStartTag());
 Assert.assertEquals(false, queue.matchesStartTag()); // consume <title>
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("abcdabcdabcdabcd");

 Assert.assertEquals("abcd", queue.matchChomp("abcd"));
 Assert.assertEquals('a', queue.consume());
 Assert.assertEquals(10, queue.remainingLength());
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" \t\r\n abc ");

 Assert.assertEquals(true, queue.matchesWhitespace()); // consume space
 Assert.assertEquals(false, queue.matchesWhitespace()); // consume a
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("abcdefghijk");

 Assert.assertEquals(true, queue.matchesWord()); // consume a
 Assert.assertEquals(true, queue.matchesWord()); // consume b
 Assert.assertEquals(false, queue.matchesWord()); // consume i
 }

 @Test
 public void testAdvance() {
 TokenQueue queue = new TokenQueue("abcde");

 queue.advance();
 Assert.assertEquals('b', queue.consume());
 }

 @Test
 public void testConsume() {
 TokenQueue queue = new TokenQueue("abcde");

 Assert.assertEquals('a', queue.consume());
 Assert.assertEquals('b', queue.consume());
 Assert.assertEquals('e', queue.consume());
 Assert.assertEquals(1, queue.remainingLength());
 }

 @Test
 public void testConsumeSequence() {
 TokenQueue queue = new TokenQueue("abcdabcdabcdabcd");

 String seq = "abcd";
 String consumed = queue.consume(seq);

 Assert.assertEquals(seq.length(), queue.remainingLength());
 Assert.assertEquals(consumed, seq);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceMismatch() {
 TokenQueue queue = new TokenQueue("abcdabcdabcdabcd");

 String seq = "efg";
 queue.consume(seq);
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");

 String consumed = queue.consumeTo("(one (two) three)");

 Assert.assertEquals("one (two) three", consumed);
 Assert.assertEquals(4, queue.remainingLength());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue queue = new TokenQueue("(One (Two) ThRee) Four");

 String consumed = queue.consumeToIgnoreCase("(one (two) three)");

 Assert.assertEquals("One (Two) ThRee", consumed);
 Assert.assertEquals(4, queue.remainingLength());
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");

 String consumed = queue.chompBalanced('(', ')');

 Assert.assertEquals("one (two) three", consumed);
 Assert.assertEquals(4, queue.remainingLength());
 }

 @Test
 public void testUnescape() {
 String in = "\\\\one\\two\\three\\";
 String out = TokenQueue.unescape(in);

 Assert.assertEquals("one\\two\\three", out);
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue("   \tabcd   ");

 boolean seen = queue.consumeWhitespace();

 Assert.assertEquals(true, seen);
 Assert.assertEquals(3, queue.remainingLength());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("abcdefghijk");

 String word = queue.consumeWord();

 Assert.assertEquals("abcdef", word);
 Assert.assertEquals(4, queue.remainingLength());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");

 String tagName = queue.consumeTagName();

 Assert.assertEquals("html", tagName);
 Assert.assertEquals(7, queue.remainingLength());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue queue = new TokenQueue("li.class1.class2:hover span#id test");

 String selector = queue.consumeElementSelector();

 Assert.assertEquals("li.class1.class2", selector);
 Assert.assertEquals(11, queue.remainingLength());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("id class1 class2 class3 id1 class4 class5");

 String identifier = queue.consumeCssIdentifier();

 Assert.assertEquals("id", identifier);
 Assert.assertEquals(10, queue.remainingLength());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("id class1 for=value class2 id2 class3 style='color:red'");

 String key = queue.consumeAttributeKey();

 Assert.assertEquals("for", key);
 Assert.assertEquals(13, queue.remainingLength());
 }

 @Test
 public void testRemainder() {
 TokenQueue queue = new TokenQueue("abcdefghijk");

 String remainder = queue.remainder();
 String remaining = queue.toString();

 Assert.assertEquals(remaining, remainder);
 }
}