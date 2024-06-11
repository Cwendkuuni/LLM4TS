package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

 @Test
 public void testConstructor() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertEquals("hello world", queue.queue);
 Assert.assertEquals(0, queue.pos);
 }

 @Test
 public void testIsEmpty() {
 TokenQueue queue = new TokenQueue("");
 Assert.assertTrue(queue.isEmpty());

 queue = new TokenQueue("hello");
 Assert.assertFalse(queue.isEmpty());
 }

 @Test
 public void testPeek() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertEquals('h', queue.peek());
 }

 @Test
 public void testAddFirst() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.addFirst('X');
 Assert.assertEquals('X', queue.peek());
 Assert.assertEquals(11, queue.remainingLength());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertTrue(queue.matches("ll"));
 Assert.assertFalse(queue.matches("xx"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertTrue(queue.matchesCS("He"));
 Assert.assertFalse(queue.matchesCS("XX"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertTrue(queue.matchesAny("ll", "ld"));
 Assert.assertFalse(queue.matchesAny("xx", "ld"));
 }

 @Test
 public void testMatchesAnyChar() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertTrue(queue.matchesAny(new char[]{'l', 'd'}));
 Assert.assertFalse(queue.matchesAny(new char[]{'x', 'd'}));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
 Assert.assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("hello <world>");
 Assert.assertTrue(queue.matchChomp("hello "));
 Assert.assertEquals(" <world>", queue.queue);
 Assert.assertEquals(7, queue.pos);
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue("    hello world   ");
 Assert.assertTrue(queue.matchesWhitespace());
 Assert.assertTrue(queue.consumeWhitespace());
 Assert.assertTrue(queue.isEmpty());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("hello world");
 Assert.assertTrue(queue.matchesWord());
 Assert.assertEquals("world", queue.consumeWord());
 Assert.assertEquals("", queue.queue);
 Assert.assertTrue(queue.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeEmptyQueue() {
 TokenQueue queue = new TokenQueue("");
 queue.consume("hello");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeNotMatchedSequence() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.consume("xoxo");
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
 Assert.assertEquals(" four", queue.queue);
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue queue = new TokenQueue("(one (TWO) three) four");
 Assert.assertEquals("one (TWO) three", queue.chompBalanced('(', ')'));
 Assert.assertEquals(" four", queue.queue);
 }
}