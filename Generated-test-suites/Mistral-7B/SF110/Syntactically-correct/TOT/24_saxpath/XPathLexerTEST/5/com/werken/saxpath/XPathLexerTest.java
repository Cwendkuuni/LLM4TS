package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testNextToken_EmptyXPath() {
  lexer = new XPathLexer("");
  assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
 }

 @Test
 public void testNextToken_ValidXPath() {
  lexer = new XPathLexer("/a/b[1]/c[2]");
  Token token;

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("a", token.getText());

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("b", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("1", token.getText());

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("c", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("2", token.getText());

  token = lexer.nextToken();
  assertEquals("]", token.getText());

  token = lexer.nextToken();
  assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
 }

 @Test
 public void testNextToken_InvalidXPath() {
  lexer = new XPathLexer("//a[1]b");
  Token token;

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("//", token.getText());

  token = lexer.nextToken();
  assertEquals("a", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("1", token.getText());

  token = lexer.nextToken();
  assertNull(lexer.nextToken());

  token = lexer.nextToken();
  assertEquals("b", token.getText());

  assertEquals(new Token(-1, "", lexer.currentPosition(), lexer.endPosition()), lexer.getPreviousToken());
 }

 @Test
 public void testNextToken_InvalidCharacters() {
  lexer = new XPathLexer("//a[1]\\/b");
  Token token;

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("//", token.getText());

  token = lexer.nextToken();
  assertEquals("a", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("1", token.getText());

  token = lexer.nextToken();
  assertEquals("\\", token.getText());

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("b", token.getText());

  assertEquals(new Token(-1, "", lexer.currentPosition(), lexer.endPosition()), lexer.getPreviousToken());
 }

 @Test
 public void testNextToken_Whitespace() {
  lexer = new XPathLexer("//a[1]   [2] b  ");
  Token token;

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("//", token.getText());

  token = lexer.nextToken();
  assertEquals("a", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("1", token.getText());

  token = lexer.nextToken();
  assertEquals(" ", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("2", token.getText());

  token = lexer.nextToken();
  assertEquals(" ", token.getText());

  token = lexer.nextToken();
  assertEquals("b", token.getText());

  assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
 }

 @Test
 public void testNextToken_InvalidTokenOrder() {
  lexer = new XPathLexer("//a[1] b[2]");
  Token token;

  token = lexer.nextToken();
  assertEquals("/", token.getText());

  token = lexer.nextToken();
  assertEquals("//", token.getText());

  token = lexer.nextToken();
  assertEquals("a", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertEquals("1", token.getText());

  token = lexer.nextToken();
  assertEquals("b", token.getText());

  token = lexer.nextToken();
  assertEquals("[", token.getText());

  token = lexer.nextToken();
  assertNotNull(lexer.nextToken());

  assertEquals(new Token(-1, "", lexer.currentPosition(), lexer.endPosition()), lexer.getPreviousToken());
 }
}