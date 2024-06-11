package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 String data = "test<tag>test<tag2>test<tag3:attr=\"value\">";
 tokenQueue = new TokenQueue(data);
 }

 @After
 public void tearDown() {
 tokenQueue = null;
 }

 @Test
 public void testEmpty() {
 TokenQueue emptyTokenQueue = new TokenQueue("");
 assertTrue(emptyTokenQueue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.matchChomp("test");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('t', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('a');
 assertEquals('a', tokenQueue.consume());
 }

 @Test
 public void testAddFirstString() {
 tokenQueue.addFirst("test");
 assertEquals("test", tokenQueue.consumeTo("test"));
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("test"));
 assertFalse(tokenQueue.matches("tes"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("Tag"));
 assertFalse(tokenQueue.matchesCS("tag"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'t', 'e', 's', 't'};
 assertTrue(tokenQueue.matchesAny(seq));
 assertFalse(tokenQueue.matchesAny(new String[]{"test2"}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.matchChomp("test");
 assertEquals("tag", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsume() {
 assertEquals('t', tokenQueue.consume());
 }

 @Test
 public void testConsumeString() {
 String consumed = tokenQueue.consumeTo("tag");
 assertEquals("test<tag", consumed);
 }

 @Test
 public void testConsumeToIgnoreCase() {
 String consumed = tokenQueue.consumeToIgnoreCase("TAG");
 assertEquals("test<tag", consumed);
 }

 @Test
 public void testConsumeToAny() {
 String[] seq = {"test", "tag"};
 String consumed = tokenQueue.consumeToAny(seq);
 assertEquals("tag", consumed);
 }

 @Test
 public void testChompBalanced() {
 assertEquals("test<tag", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String in = "\\(one \\(two\\) three\\) four";
 String out = TokenQueue.unescape(in);
 assertEquals("(one (two) three) four", out);
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 assertFalse(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 String word = tokenQueue.consumeWord();
 assertEquals("tag", word);
 }

 @Test
 public void testConsumeTagName() {
 String tagName = tokenQueue.consumeTagName();
 assertEquals("tag", tagName);
 }

 @Test
 public void testConsumeElementSelector() {
 String selector = tokenQueue.consumeElementSelector();
 assertEquals("tag", selector);
 }

 @Test
 public void testConsumeCssIdentifier() {
 String identifier = tokenQueue.consumeCssIdentifier();
 assertEquals("attr", identifier);
 }

 @Test
 public void testConsumeAttributeKey() {
 String key = tokenQueue.consumeAttributeKey();
 assertEquals("attr", key);
 }

 @Test
 public void testRemainder() {
 String remainder = tokenQueue.remainder();
 assertNotNull(remainder);
 }

 @Test
 public void testToString() {
 assertEquals("tag3:attr=\"value\"", tokenQueue.toString());
 }
}