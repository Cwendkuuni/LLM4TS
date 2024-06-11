package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

 private XPathLexer lexer;
 private String xpath;

 @Before
 public void setUp() {
 xpath = "//a[b='c']/d[@e='f']/g[h='i' and j='k']";
 lexer = new XPathLexer(xpath);
 }

 @After
 public void tearDown() {
 lexer = null;
 xpath = null;
 }

 @Test
 public void testNextToken_emptyXPath() {
 XPathLexer emptyLexer = new XPathLexer("");
 assertEquals(new Token(-1, "", 0, 0), emptyLexer.nextToken());
 }

 @Test
 public void testNextToken_identifer() {
 Token token = lexer.nextToken();
 assertEquals(new Token(15, xpath, lexer.currentPosition(), lexer.currentPosition()), token);
 }

 @Test
 public void testNextToken_operator() {
 Token token = lexer.nextToken();
 switch (token.getTokenType()) {
 case 7:
 assertEquals("/", token.getValue());
 break;
 case 17:
 assertEquals("|", token.getValue());
 break;
 case 18:
 assertEquals(":", token.getValue());
 break;
 }
 }

 @Test
 public void testNextToken_number() {
 Token token = lexer.nextToken();
 switch (token.getTokenType()) {
 case 30:
 assertEquals("", token.getValue());
 break;
 case 31:
 assertEquals("0", token.getValue());
 break;
 }
 }

 @Test
 public void testNextToken_whitespace() {
 Token token = lexer.nextToken();
 assertEquals(new Token(-2, xpath, 0, 0), token);
 }

 @Test
 public void testNextToken_literal() {
 Token token = lexer.nextToken();
 switch (token.getTokenType()) {
 case 27:
 assertEquals("//", token.getValue());
 break;
 }
 }

 @Test
 public void testNextToken_invalidChar() {
 XPathLexer invalidLexer = new XPathLexer("//a[b='c'x]");
 assertNull(invalidLexer.nextToken());
 }

 @Test
 public void testNextToken_endOfXPath() {
 Token token = lexer.nextToken();
 assertNull(token);
 }

}