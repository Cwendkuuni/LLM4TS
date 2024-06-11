package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testEmptyXPath() {
  lexer = new XPathLexer("");
  assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
 }

 @Test
 public void testValidXPath() {
  lexer = new XPathLexer("/a/b[1]/c[2]");

  Token token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("a", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("b", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[1]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("c", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[2]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals(null, token);
 }

 @Test
 public void testXPathWithWhitespace() {
  lexer = new XPathLexer("  /a/b[1]   /c[2]  ");

  Token token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("a", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("b", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[1]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("c", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[2]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals(null, token);
 }

 @Test
 public void testInvalidXPath() {
  lexer = new XPathLexer("a/b[1]/c[2]/");

  Token token = lexer.nextToken();
  assertEquals("a", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("b", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[1]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("c", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[2]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertNotNull(token);
  assertEquals(-1, token.getType());
  assertEquals("", token.getValue());
 }

 @Test
 public void testInvalidXPathWithSymbols() {
  lexer = new XPathLexer("a/b[1]/c[2]%");

  Token token = lexer.nextToken();
  assertEquals("a", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("b", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[1]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("c", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[2]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertNotNull(token);
  assertEquals(-1, token.getType());
  assertEquals("", token.getValue());
 }

 @Test
 public void testInvalidXPathWithMissingBrackets() {
  lexer = new XPathLexer("a/b[1]c[2]");

  Token token = lexer.nextToken();
  assertEquals("a", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("/", token.getValue());
  assertEquals(1, token.getType());

  token = lexer.nextToken();
  assertEquals("b", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertEquals("[1]", token.getValue());
  assertEquals(13, token.getType());

  token = lexer.nextToken();
  assertNotNull(token);
  assertEquals(-1, token.getType());
  assertEquals("", token.getValue());

  token = lexer.nextToken();
  assertEquals("c", token.getValue());
  assertEquals(15, token.getType());

  token = lexer.nextToken();
  assertNotNull(token);
  assertEquals(-1, token.getType());
  assertEquals("", token.getValue());
 }
}