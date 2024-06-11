package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testEmptyXPath() {
  lexer = new XPathLexer("");
  assertNull(lexer.nextToken());
 }

 @Test
 public void testSimpleXPath() {
  lexer = new XPathLexer("/test");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithNumber() {
  lexer = new XPathLexer("/test/1");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.NUMBER, token.getTokenType());
  assertEquals("1", token.getValue());
  assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithWhitespace() {
  lexer = new XPathLexer("/test /123");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.WHITESPACE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.NUMBER, token.getTokenType());
  assertEquals("123", token.getValue());
  assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithComment() {
  lexer = new XPathLexer("/test/*[comment()='this is a comment']");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.ASTERISK, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("comment", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.EQUALS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  assertEquals("'this is a comment'", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());
  assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithInvalidCharacter() {
  lexer = new XPathLexer("/test%");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  Assert.assertThrows(IllegalArgumentException.class, () -> lexer.nextToken());
 }

 @Test
 public void testXPathWithMixedTokens() {
  lexer = new XPathLexer("/test/1+2/@attrib[.='value']");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.NUMBER, token.getTokenType());
  assertEquals("1", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.PLUS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("2", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.AT_SIGN, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("attrib", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.DOT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EQUALS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  assertEquals("'value'", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());
  assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithPredicate() {
  lexer = new XPathLexer("/test[1]/descendant::*[.='value']");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.NUMBER, token.getTokenType());
  assertEquals("1", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.DESCENDANT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.ASTERISK, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.COLON, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.DOT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EQUALS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  assertEquals("'value'", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());
  assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithFunctionCall() {
  lexer = new XPathLexer("/descendant::*[count(.)='3']");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.DESCENDANT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.ASTERISK, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.COLON, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals("count", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.OPEN_PARENTHESIS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.DOT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  assertEquals(".", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.EQUALS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  assertEquals("'3'", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.CLOSE_PARENTHESIS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());
  assertNull(lexer.nextToken());
 }

}