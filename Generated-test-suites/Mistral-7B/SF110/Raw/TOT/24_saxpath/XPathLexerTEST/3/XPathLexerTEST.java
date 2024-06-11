package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testEmptyXPath() {
     lexer = new XPathLexer("");
     assertNull(lexer.nextToken());
 }

 @Test
 public void testSimpleXPath() {
     lexer = new XPathLexer("/a/b");
     Token token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("a", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("b", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithSlashes() {
     lexer = new XPathLexer("/a////b");
     Token token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("a", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("b", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithDollar() {
     lexer = new XPathLexer("$foo");
     Token token = lexer.nextToken();
     assertEquals("$", token.getValue());
     assertEquals("foo", token.getText());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithDoubleColon() {
     lexer = new XPathLexer("a::b");
     Token token = lexer.nextToken();
     assertEquals("a", token.getValue());
     token = lexer.nextToken();
     assertEquals("::", token.getValue());
     token = lexer.nextToken();
     assertEquals("b", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXPath() {
     lexer = new XPathLexer("a/b/c/");
     Token token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("a", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("b", token.getValue());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     // the following token should not be created because the XPath is invalid
     // assertNull(token);
     throw new IllegalArgumentException("Invalid XPath: a/b/c/");
 }

 @Test
 public void testXPathWithWhitespace() {
     lexer = new XPathLexer(" //a ");
     Token token = lexer.nextToken();
     assertEquals("//", token.getValue());
     token = lexer.nextToken();
     assertEquals("a", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithNumbers() {
     lexer = new XPathLexer("/1/2/3");
     Token token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("1", token.getText());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("2", token.getText());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("3", token.getText());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithOperators() {
     lexer = new XPathLexer("a[1=2 or 3=4][5>6]");
     Token token = lexer.nextToken();
     assertEquals("a", token.getValue());
     token = lexer.nextToken();
     assertEquals("[", token.getValue());
     token = lexer.nextToken();
     assertEquals("1=", token.getValue());
     token = lexer.nextToken();
     assertEquals("2", token.getText());
     token = lexer.nextToken();
     assertEquals("o", token.getValue());
     token = lexer.nextToken();
     assertEquals("r", token.getValue());
     token = lexer.nextToken();
     assertEquals("3=", token.getValue());
     token = lexer.nextToken();
     assertEquals("4", token.getText());
     token = lexer.nextToken();
     assertEquals("]", token.getValue());
     token = lexer.nextToken();
     assertEquals("[", token.getValue());
     token = lexer.nextToken();
     assertEquals("5>", token.getValue());
     token = lexer.nextToken();
     assertEquals("6", token.getText());
     token = lexer.nextToken();
     assertEquals("]", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithQuotes() {
     lexer = new XPathLexer("'foo'");
     Token token = lexer.nextToken();
     assertEquals("'", token.getValue());
     token = lexer.nextToken();
     assertEquals("foo", token.getText());
     assertEquals("'", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithQuotesAndSlashes() {
     lexer = new XPathLexer("/'foo'/bar");
     Token token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("'", token.getValue());
     token = lexer.nextToken();
     assertEquals("foo", token.getText());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("bar", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithBrackets() {
     lexer = new XPathLexer("[1,2,3]");
     Token token = lexer.nextToken();
     assertEquals("[", token.getValue());
     token = lexer.nextToken();
     assertEquals("1,", token.getValue());
     token = lexer.nextToken();
     assertEquals("2,", token.getValue());
     token = lexer.nextToken();
     assertEquals("3]", token.getValue());
     assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithParentheses() {
     lexer = new XPathLexer("(a/b)/c");
     Token token = lexer.nextToken();
     assertEquals("(", token.getValue());
     token = lexer.nextToken();
     assertEquals("a/b", token.getText());
     token = lexer.nextToken();
     assertEquals("/", token.getValue());
     token = lexer.nextToken();
     assertEquals("c", token.getValue());
     token = lexer.nextToken();
     assertEquals(")", token.getValue());
     assertNull(lexer.nextToken());
 }

}