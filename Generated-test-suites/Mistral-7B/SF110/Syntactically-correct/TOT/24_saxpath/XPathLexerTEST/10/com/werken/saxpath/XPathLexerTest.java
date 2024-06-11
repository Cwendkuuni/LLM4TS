package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testNextToken_EmptyXPath() {
  lexer = new XPathLexer("");
  Token token = lexer.nextToken();
  assertEquals(TokenType.EOF, token.getTokenType());
 }

 @Test
 public void testNextToken_WhitespaceXPath() {
  lexer = new XPathLexer("   hello   ");
  Token token = lexer.nextToken();
  assertEquals(TokenType.WHITESPACE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.WHITESPACE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.WHITESPACE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.WHITESPACE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EOF, token.getTokenType());
 }

 @Test
 public void testNextToken_SimpleXPath() {
  lexer = new XPathLexer("/test");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EOF, token.getTokenType());
 }

 @Test
 public void testNextToken_XPathWithNumericLiteral() {
  lexer = new XPathLexer("/test[1]");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.NUMBER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EOF, token.getTokenType());
 }

 @Test
 public void testNextToken_XPathWithIdentifier() {
  lexer = new XPathLexer("/test[@id='test']");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.AT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EQUALS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EOF, token.getTokenType());
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_InvalidXPath() {
  lexer = new XPathLexer("/test[1]@id='test'");
  Token token = lexer.nextToken();
  assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.NUMBER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.AT, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.EQUALS, token.getTokenType());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  token = lexer.nextToken();
  assertEquals("test", token.getValue());
  token = lexer.nextToken();
  assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
  token = lexer.nextToken(); // this should throw an IllegalStateException
 }
}