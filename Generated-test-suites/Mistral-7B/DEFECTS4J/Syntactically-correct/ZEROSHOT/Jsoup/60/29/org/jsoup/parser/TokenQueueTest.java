package org.jsoup.parser;

import org.junit.Assert;import org.junit.Test;import org.jsoup.helper.StringUtil;

/**
 * Unit tests for TokenQueue class.
 */
public class TokenQueueTest {

 @Test
 public void testTokenQueueConstructor() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 Assert.assertFalse("Queue should not be empty", tokenQueue.isEmpty());
 Assert.assertEquals("hello", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueIsEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertTrue("Queue should be empty", tokenQueue.isEmpty());
 }

 @Test
 public void testTokenQueueMatches() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue("'hello' should match", tokenQueue.matches("hello"));
 Assert.assertFalse("'foo' should not match", tokenQueue.matches("foo"));
 }

 @Test
 public void testTokenQueueMatchesCaseSensitive() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 Assert.assertTrue("'Hello' should match case sensitive", tokenQueue.matchesCS("Hello"));
 Assert.assertFalse("'foo' should not match case sensitive", tokenQueue.matchesCS("foo"));
 }

 @Test
 public void testTokenQueueMatchesAny() {
 TokenQueue tokenQueue = new TokenQueue("123hello world 456");
 Assert.assertTrue("'1' or '2' or '3' should match", tokenQueue.matchesAny('1', '2', '3'));
 Assert.assertTrue("'h' or 'e' or 'l' or 'l' or 'o' should match", tokenQueue.matchesAny("h", "e", "l", "l", "o"));
 Assert.assertFalse("'7' should not match", tokenQueue.matchesAny('7'));
 }

 @Test
 public void testTokenQueueMatchesAnyChar() {
 TokenQueue tokenQueue = new TokenQueue("123hello world 456");
 Assert.assertTrue("'1' or '2' or '3' should match", tokenQueue.matchesAny(new char[]{'1', '2', '3'}));
 Assert.assertTrue("'h' or 'e' or 'l' or 'l' or 'o' should match", tokenQueue.matchesAny(new char[]{'h', 'e', 'l', 'l', 'o'}));
 Assert.assertFalse("'7' should not match", tokenQueue.matchesAny(new char[]{'7'}));
 }

 @Test
 public void testTokenQueueMatchesStartTag() {
 TokenQueue tokenQueue = new TokenQueue("<html>");
 Assert.assertTrue("'<html>' should match start tag", tokenQueue.matchesStartTag());
 }

 @Test
 public void testTokenQueueMatchChomp() {
 TokenQueue tokenQueue = new TokenQueue("hello <world>");
 Assert.assertTrue("'hello' should match and be removed", tokenQueue.matchChomp("hello"));
 Assert.assertFalse("'world' should not match", tokenQueue.matches("world"));
 }

 @Test
 public void testTokenQueueMatchesWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" \t hello world \t ");
 Assert.assertTrue("Queue should start with whitespace", tokenQueue.matchesWhitespace());
 }

 @Test
 public void testTokenQueueMatchesWord() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue("'hello' or 'world' should match a word character", tokenQueue.matchesWord());
 }

 @Test
 public void testTokenQueueAdvance() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 tokenQueue.advance();
 Assert.assertEquals("'e'", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsume() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 char c = tokenQueue.consume();
 Assert.assertEquals("'h'", c);
 Assert.assertFalse("Queue should be empty after consuming a character", tokenQueue.isEmpty());
 }

 @Test
 public void testTokenQueueConsumeString() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 String str = tokenQueue.consume(2, "ll");
 Assert.assertEquals("'lo'", str);
 Assert.assertEquals("'or' ", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsumeTo() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head></html>");
 String str = tokenQueue.consumeTo("</html>");
 Assert.assertEquals("<html><head><title>Test</title></head>", str);
 }

 @Test
 public void testTokenQueueConsumeToIgnoreCase() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head></html>");
 String str = tokenQueue.consumeToIgnoreCase("</head>");
 Assert.assertEquals("<html><title>Test</title>", str);
 }

 @Test
 public void testTokenQueueConsumeToAny() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
 String str = tokenQueue.consumeToAny("</html>", "</head>");
 Assert.assertEquals("<body><p>Hello World</p>", str);
 }

 @Test
 public void testTokenQueueChompBalanced() {
 TokenQueue tokenQueue = new TokenQueue("(hello (world (foo)))");
 String str = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("(hello (world foo)", str);
 }

 @Test
 public void testTokenQueueChompBalancedNested() {
 TokenQueue tokenQueue = new TokenQueue("((hello (world (foo))))");
 String str = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("(hello (world foo)", str);
 }

 @Test
 public void testTokenQueueUnescape() {
 String str = TokenQueue.unescape("\\(hello\\ world\\)");
 Assert.assertEquals("(hello world)", str);
 }

 @Test
 public void testTokenQueueConsumeWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" \t hello world \t ");
 boolean consumed = tokenQueue.consumeWhitespace();
 Assert.assertTrue("Whitespace was consumed", consumed);
 Assert.assertEquals("hello", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsumeWord() {
 TokenQueue tokenQueue = new TokenQueue("hello world");
 String word = tokenQueue.consumeWord();
 Assert.assertEquals("hello", word);
 Assert.assertEquals("world", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsumeTagName() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head></html>");
 String tagName = tokenQueue.consumeTagName();
 Assert.assertEquals("html", tagName);
 Assert.assertEquals("<head><title>Test</title>", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsumeElementSelector() {
 TokenQueue tokenQueue = new TokenQueue("div.class p#id span.anotherClass");
 String selector = tokenQueue.consumeElementSelector();
 Assert.assertEquals("div", selector);
 Assert.assertEquals(".class p#id span.anotherClass", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsumeCssIdentifier() {
 TokenQueue tokenQueue = new TokenQueue("class:class class:anotherClass id:id class:class");
 String identifier = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals("class", identifier);
 Assert.assertEquals("class:anotherClass id:id class:", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueConsumeAttributeKey() {
 TokenQueue tokenQueue = new TokenQueue("class:class class:anotherClass id:id class:class class:class class:class class:class class:class");
 String attributeKey = tokenQueue.consumeAttributeKey();
 Assert.assertEquals("class", attributeKey);
 Assert.assertEquals("class:class class:anotherClass class:class class:class class:class class:class class:class class:class", tokenQueue.peek());
 }

 @Test
 public void testTokenQueueRemainder() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head></html>");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("", remainder);
 }
}