package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testNextToken_EmptyXPath() {
  lexer = new XPathLexer("");
  Token token = lexer.nextToken();
  Assert.assertEquals(-1, token.getTokenType());
 }

 @Test
 public void testNextToken_SingleCharacterIdentifier() {
  lexer = new XPathLexer("abc");
  Token token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());
 }

 @Test
 public void testNextToken_SingleCharacterOperator() {
  lexer = new XPathLexer("+");
  Token token = lexer.nextToken();
  Assert.assertEquals(5, token.getTokenType());
 }

 @Test
 public void testNextToken_MultipleCharacterIdentifier() {
  lexer = new XPathLexer("myVariableName");
  Token token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("myVariableName", token.getValue());
 }

 @Test
 public void testNextToken_Whitespace() {
  lexer = new XPathLexer("   abc  ");
  Token token = lexer.nextToken();
  Assert.assertEquals(13, token.getTokenType());
 }

 @Test
 public void testNextToken_SingleCharacterLiteral() {
  lexer = new XPathLexer("'a'");
  Token token = lexer.nextToken();
  Assert.assertEquals(27, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
 }

 @Test
 public void testNextToken_MultipleCharacterLiteral() {
  lexer = new XPathLexer("\"abc\"");
  Token token = lexer.nextToken();
  Assert.assertEquals(27, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextToken_InvalidXPath() {
  lexer = new XPathLexer("invalidXPath");
  lexer.nextToken();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextToken_InvalidCharacter() {
  lexer = new XPathLexer("@#");
  lexer.nextToken();
 }

 @Test
 public void testNextToken_SimpleXPath() {
  lexer = new XPathLexer("/abc/def");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("def", token.getValue());
 }

 @Test
 public void testNextToken_ComplexXPath_WithNumber() {
  lexer = new XPathLexer("/abc/123/def");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(31, token.getTokenType());
  Assert.assertEquals("123", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("def", token.getValue());
 }

 @Test
 public void testNextToken_ComplexXPath_WithStar() {
  lexer = new XPathLexer("/abc/*");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(20, token.getTokenType());
 }

 @Test
 public void testNextToken_ComplexXPath_WithAnd() {
  lexer = new XPathLexer("/abc/def/@and");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("def", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(16, token.getTokenType());

  token = lexer.nextToken();
  Assert.assertEquals(28, token.getTokenType());
 }

 @Test
 public void testNextToken_ComplexXPath_WithOr() {
  lexer = new XPathLexer("/abc/def/@or");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("def", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(17, token.getTokenType());
 }

 @Test
 public void testNextToken_ComplexXPath_WithDollar() {
  lexer = new XPathLexer("/abc/@$var");
  Token token = lexer.nextToken();
  Assert.assertEquals(11, token.getTokenType());
  Assert.assertEquals("/", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("abc", token.getValue());

  token = lexer.nextToken();
  Assert.assertEquals(16, token.getTokenType());

  token = lexer.nextToken();
  Assert.assertEquals(26, token.getTokenType());

  token = lexer.nextToken();
  Assert.assertEquals(14, token.getTokenType());

  token = lexer.nextToken();
  Assert.assertEquals(15, token.getTokenType());
  Assert.assertEquals("var", token.getValue());
 }
}