package com.werken.saxpath;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;
 private String xpath;

 @Before
 public void setup() {
 xpath = "//a[b='c' and d=1][e or f]/g[h='i' and j='k']";
 lexer = new XPathLexer(xpath);
 }

 @Test
 public void testNextToken_Empty() {
 XPathLexer emptyLexer = new XPathLexer("");
 assertEquals(new Token(-2, "", 0, 0), emptyLexer.nextToken());
 }

 @Test
 public void testNextToken_Identifier() {
 Token token = lexer.nextToken();
 assertEquals(15, token.getTokenType());
 assertEquals("xpath", token.getText());
 }

 @Test
 public void testNextToken_Operator() {
 Token token = lexer.nextToken();
 assertEquals(28, token.getTokenType());
 }

 @Test
 public void testNextToken_SpecialChar() {
 Token token = lexer.nextToken();
 assertEquals(11, token.getTokenType());
 }

 @Test
 public void testNextToken_Whitespace() {
 Token token = lexer.nextToken();
 assertEquals(-2, token.getTokenType());
 }

 @Test
 public void testNextToken_Number() {
 Token token = lexer.nextToken();
 assertEquals(31, token.getTokenType());
 assertEquals("1", token.getText());
 }

 @Test
 public void testNextToken_InvalidChar() {
 XPathLexer invalidLexer = new XPathLexer("//a[b='c' and d=1][e or f]/g[h='i' and j='k']%");
 Token token = invalidLexer.nextToken();
 assertEquals(-1, token.getTokenType());
 }

 @Test
 public void testNextToken_Error_UnexpectedEndOfString() {
 XPathLexer errorLexer = new XPathLexer("//a[b='c' and d=1][e or f]/g[h='i' and j='k']/");
 Token token = errorLexer.nextToken();
 assertEquals(-1, token.getTokenType());
 }

 @Test
 public void testNextToken_Error_UnmatchedParentheses() {
 XPathLexer errorLexer = new XPathLexer("//a[b='c' and d=1][e or f/g]");
 Token token = errorLexer.nextToken();
 assertEquals(-1, token.getTokenType());
 }

 @Test
 public void testNextToken_Error_UnmatchedBrackets() {
 XPathLexer errorLexer = new XPathLexer("//a[b='c' and [d=1] e]");
 Token token = errorLexer.nextToken();
 assertEquals(-1, token.getTokenType());
 }
}