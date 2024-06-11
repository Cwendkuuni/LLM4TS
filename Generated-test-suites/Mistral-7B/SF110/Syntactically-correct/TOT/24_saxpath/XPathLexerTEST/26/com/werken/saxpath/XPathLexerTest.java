package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

 private static final String VALID_XPATH = "/a[1]/b[2]/c[3]";
 private static final String VALID_XPATH_WITH_DOTS = "a.b.c.d.e";
 private static final String VALID_XPATH_WITH_SLASHES = "/a//b//c//d";
 private static final String VALID_XPATH_WITH_OPERATORS = "a[1] and b[2] or c[3]";
 private static final String VALID_XPATH_WITH_QUOTES = "a['test' and b='test2']";
 private static final String VALID_XPATH_WITH_BRACKETS = "a[1][b='test'][c[1]=d[2]]";
 private static final String VALID_XPATH_WITH_NUMBERS = "/a[1]/b[1]/c[3]/d[4]";
 private static final String VALID_XPATH_WITH_WHITESPACE = "a[1]   b[2]   c[3]";

 private XPathLexer lexer;

 @Test
 public void testValidXPath() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithDots() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_DOTS);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithSlashes() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_SLASHES);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithOperators() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_OPERATORS);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithQuotes() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_QUOTES);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithBrackets() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_BRACKETS);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithNumbers() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_NUMBERS);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test
 public void testValidXPathWithWhitespace() {
 XPathLexer lexer = new XPathLexer(VALID_XPATH_WITH_WHITESPACE);
 Token token = null;
 do {
 token = lexer.nextToken();
 System.out.println("Token: " + token.getTokenType() + " " + token.getValue());
 } while (token.getTokenType() != TokenType.EOF);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXPathWithMissingXPath() {
 XPathLexer lexer = new XPathLexer("");
 Token token = null;
 do {
 token = lexer.nextToken();
 } while (token.getTokenType() != TokenType.EOF);
 Assert.assertTrue(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXPathWithInvalidChar() {
 XPathLexer lexer = new XPathLexer("@#");
 Token token = null;
 do {
 token = lexer.nextToken();
 } while (token.getTokenType() != TokenType.EOF);
 Assert.assertTrue(false);
 }
}