package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testEmptyXPath() {
  lexer = new XPathLexer("");
  Token token = lexer.nextToken();
  Assert.assertEquals(-1, token.getTokenType());
 }

 @Test
 public void testXPathWithSingleCharacter() {
  lexer = new XPathLexer("/a");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());
 }

 @Test
 public void testXPathWithSingleIdentifier() {
  lexer = new XPathLexer("/id");
  Token token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("id", token.getValue());
 }

 @Test
 public void testXPathWithSingleOperator() {
  lexer = new XPathLexer("/= ");
  Token token = lexer.nextToken();
  Assert.assertEquals(21, token.getTokenType());
  Assert.assertEquals("=", token.getValue());
 }

 @Test
 public void testXPathWithSingleLiteral() {
  lexer = new XPathLexer("/'abc'");
  Token token = lexer.nextToken();
  Assert.assertEquals(27, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());
 }

 @Test
 public void testXPathWithSingleNumber() {
  lexer = new XPathLexer("/123");
  Token token = lexer.nextToken();
  Assert.assertEquals(31, token.getTokenType());
  Assert.assertEquals("123", token.getValue());
 }

 @Test
 public void testXPathWithMultipleTokens() {
  lexer = new XPathLexer("/a/b/1");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("a", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("b", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(31, token.getTokenType());
  Assert.assertEquals("1", token.getValue());
 }

 @Test
 public void testXPathWithWhitespace() {
  lexer = new XPathLexer("/ a / b");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("a", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(-2, token.getTokenType());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("b", token.getValue());
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testXPathWithUnexpectedCharacter() {
  lexer = new XPathLexer("/a/x");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("a", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType());
  Assert.assertEquals("x", token.getValue());

  token = lexer.nextToken(); // should throw an exception
 }

 @Test
 public void testXPathWithComment() {
  lexer = new XPathLexer("/--this is a comment--/a");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(-2, token.getTokenType());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
 }

 @Test
 public void testXPathWithQuotedIdentifier() {
  lexer = new XPathLexer("/'identifier'");
  Token token = lexer.nextToken();
  Assert.assertEquals(27, token.getTokenType());
  Assert.assertEquals("identifier", token.getValue());
 }

 @Test
 public void testXPathWithMultiCharacterOperator() {
  lexer = new XPathLexer("/!= ");
  Token token = lexer.nextToken();
  Assert.assertEquals(22, token.getTokenType());
  Assert.assertEquals("!=", token.getValue());
 }

 @Test
 public void testXPathWithDoubleColon() {
  lexer = new XPathLexer("/::node()");
  Token token = lexer.nextToken();
  Assert.assertEquals(19, token.getTokenType());
  Assert.assertEquals("::", token.getValue());
 }

 @Test
 public void testXPathWithStar() {
  lexer = new XPathLexer("/*/a");
  Token token = lexer.nextToken();
  Assert.assertEquals(20, token.getTokenType());
  Assert.assertEquals("*", token.getValue());
 }
}