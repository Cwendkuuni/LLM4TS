package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;
 private String xpath;

 @Test
 public void testEmptyXPath() {
 xpath = "";
 lexer = new XPathLexer(xpath);
 Token token = lexer.nextToken();
 assertEquals(TokenType.WHITESPACE, token.getTokenType());
 }

 @Test
 public void testSimpleXPath() {
 xpath = "/a";
 lexer = new XPathLexer(xpath);
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 }

 @Test
 public void testXPathWithNumber() {
 xpath = "/a[1]";
 lexer = new XPathLexer(xpath);
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.AT, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.NUMBER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
 }

 @Test
 public void testXPathWithIdentifier() {
 xpath = "/a/b";
 lexer = new XPathLexer(xpath);
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXPath() {
 xpath = "/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/z";
 lexer = new XPathLexer(xpath);
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 lexer.nextToken();
 }

 @Test
 public void testXPathWithWhitespace() {
 xpath = "/a/b/ c/d";
 lexer = new XPathLexer(xpath);
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.WHITESPACE, token.getTokenType());
 assertEquals(" ", token.getValue());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 }

 @Test
 public void testXPathWithComment() {
 xpath = "/a/b/*comment(a)";
 lexer = new XPathLexer(xpath);
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.LEFT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.RIGHT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASHES, token.getTokenType());
 }
}