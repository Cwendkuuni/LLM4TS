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
  Assert.assertEquals("", lexer.getXPath());
  Assert.assertEquals(0, lexer.currentPosition());
  Assert.assertEquals(0, lexer.endPosition());
 }

 @Test
 public void testNextToken_SimpleXPath() {
  lexer = new XPathLexer("/test");
  Token token = lexer.nextToken();
  Assert.assertEquals(12, token.getTokenType()); // SLASH
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(0, lexer.currentPosition());
  Assert.assertEquals(5, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("test", token.getValue());
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(6, lexer.currentPosition());
  Assert.assertEquals(5, lexer.endPosition());
 }

 @Test
 public void testNextToken_XPathWithWhitespace() {
  lexer = new XPathLexer("/test    /foo   ");
  Token token = lexer.nextToken();
  Assert.assertEquals(12, token.getTokenType()); // SLASH
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(0, lexer.currentPosition());
  Assert.assertEquals(6, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("test", token.getValue());
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(7, lexer.currentPosition());
  Assert.assertEquals(6, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(12, token.getTokenType()); // SLASH
  Assert.assertEquals("/foo", lexer.getXPath());
  Assert.assertEquals(8, lexer.currentPosition());
  Assert.assertEquals(11, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("foo", token.getValue());
  Assert.assertEquals("/foo", lexer.getXPath());
  Assert.assertEquals(10, lexer.currentPosition());
  Assert.assertEquals(11, lexer.endPosition());
 }

 @Test
 public void testNextToken_XPathWithComments() {
  lexer = new XPathLexer("/test/*[comment()=' This is a comment ']");
  Token token = lexer.nextToken();
  Assert.assertEquals(12, token.getTokenType()); // SLASH
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(0, lexer.currentPosition());
  Assert.assertEquals(12, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("test", token.getValue());
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(13, lexer.currentPosition());
  Assert.assertEquals(12, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(32, token.getTokenType()); // COMMA
  Assert.assertEquals("/test", lexer.getXPath());
  Assert.assertEquals(14, lexer.currentPosition());
  Assert.assertEquals(14, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(54, token.getTokenType()); // COMMENT
  Assert.assertEquals(" This is a comment ", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']", lexer.getXPath());
  Assert.assertEquals(15, lexer.currentPosition());
  Assert.assertEquals(37, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("[comment()", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']", lexer.getXPath());
  Assert.assertEquals(38, lexer.currentPosition());
  Assert.assertEquals(44, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(32, token.getTokenType()); // COMMA
  Assert.assertEquals("/test[comment()=' This is a comment ']", lexer.getXPath());
  Assert.assertEquals(45, lexer.currentPosition());
  Assert.assertEquals(45, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("*", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']/*", lexer.getXPath());
  Assert.assertEquals(46, lexer.currentPosition());
  Assert.assertEquals(46, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("[", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[", lexer.getXPath());
  Assert.assertEquals(47, lexer.currentPosition());
  Assert.assertEquals(48, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(32, token.getTokenType()); // COMMA
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[", lexer.getXPath());
  Assert.assertEquals(49, lexer.currentPosition());
  Assert.assertEquals(49, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("comment()", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[comment()]", lexer.getXPath());
  Assert.assertEquals(50, lexer.currentPosition());
  Assert.assertEquals(71, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(40, token.getTokenType()); // LEFT_PAREN
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[comment()]=", lexer.getXPath());
  Assert.assertEquals(51, lexer.currentPosition());
  Assert.assertEquals(51, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(21, token.getTokenType()); // EQUALS
  Assert.assertEquals("/test[comment()=' This is a comment '[", lexer.getXPath());
  Assert.assertEquals(52, lexer.currentPosition());
  Assert.assertEquals(52, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(32, token.getTokenType()); // COMMA
  Assert.assertEquals("/test[comment()=' This is a comment '[", lexer.getXPath());
  Assert.assertEquals(53, lexer.currentPosition());
  Assert.assertEquals(53, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(27, token.getTokenType()); // LITERAL
  Assert.assertEquals("'", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[comment()=']", lexer.getXPath());
  Assert.assertEquals(54, lexer.currentPosition());
  Assert.assertEquals(55, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(32, token.getTokenType()); // COMMA
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[comment()=']", lexer.getXPath());
  Assert.assertEquals(56, lexer.currentPosition());
  Assert.assertEquals(56, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(1, token.getTokenType()); // IDENTIFIER
  Assert.assertEquals("]", token.getValue());
  Assert.assertEquals("/test[comment()=' This is a comment ']/*[comment()=']", lexer.getXPath());
  Assert.assertEquals(57, lexer.currentPosition());
  Assert.assertEquals(57, lexer.endPosition());
  token = lexer.nextToken();
  Assert.assertEquals(-1, token.getTokenType());
  Assert.assertEquals("/test[comment()=' This is a comment ']", lexer.getXPath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextToken_InvalidXPath() {
  lexer = new XPathLexer("test");
  lexer.nextToken();
 }

}