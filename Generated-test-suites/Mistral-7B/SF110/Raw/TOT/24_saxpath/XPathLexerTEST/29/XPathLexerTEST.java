package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testEmptyXPath() {
  lexer = new XPathLexer("");
  Assert.assertNull(lexer.nextToken());
 }

 @Test
 public void testSimpleXPath() {
  lexer = new XPathLexer("/a[1]/b");
  Token token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.NUMBER, token.getTokenType());
  Assert.assertEquals("1", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("b", token.getValue());
  Assert.assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithComments() {
  lexer = new XPathLexer("/a[1]/*comment(())/b");
  Token token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.NUMBER, token.getTokenType());
  Assert.assertEquals("1", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.ASTERISK, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASH, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("b", token.getValue());
  Assert.assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithWhitespace() {
  lexer = new XPathLexer("/a[ 1 ]/b");
  Token token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SPACE, token.getTokenType());
  Assert.assertEquals(" ", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.NUMBER, token.getTokenType());
  Assert.assertEquals("1", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("b", token.getValue());
  Assert.assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithNumbers() {
  lexer = new XPathLexer("/a[1+2]/b");
  Token token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.NUMBER, token.getTokenType());
  Assert.assertEquals("1", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PLUS, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.NUMBER, token.getTokenType());
  Assert.assertEquals("2", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("b", token.getValue());
  Assert.assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithOperators() {
  lexer = new XPathLexer("/a/(b=c)[d>e]/f");
  Token token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType(), 1);
  Assert.assertEquals("b", token.getValue(), 1);
  Assert.assertEquals(TokenType.EQUALS, token.getTokenType(), 2);
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType(), 3);
  Assert.assertEquals("c", token.getValue(), 3);
  Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("d", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.GREATER_THAN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("e", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("f", token.getValue());
  Assert.assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithQuotedStrings() {
  lexer = new XPathLexer("/a[starts-with(./name,'John')]/b[contains(.,'Doe')]/c[contains(.,'Smith')]/text()");
  Token token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("a", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("starts-with", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("./name", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.COMMA, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("contains", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("b", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("c", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.PERIOD, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals(".", token.getValue());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
  token = lexer.nextToken();
  Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
  Assert.assertEquals("text", token.getValue());
  Assert.assertNull(lexer.nextToken());
 }

}