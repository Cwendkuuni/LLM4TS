package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

private XPathLexer lexer;

@Test
public void testNextTokenEmptyString() {
lexer = new XPathLexer("");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenSingleCharacter() {
lexer = new XPathLexer("a");
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
}

@Test
public void testNextTokenMultipleCharacters() {
lexer = new XPathLexer("abc");
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("b", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(2, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("c", lexer.getXPath());
assertEquals(2, lexer.currentPosition());
assertEquals(3, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenWhitespace() {
lexer = new XPathLexer(" \t Hello World!");
assertEquals(-2, lexer.nextToken().getTokenType());
assertEquals("", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(0, lexer.endPosition());
assertEquals(-2, lexer.nextToken().getTokenType());
assertEquals("", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(0, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("Hello", lexer.getXPath());
assertEquals(2, lexer.currentPosition());
assertEquals(7, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenOperator() {
lexer = new XPathLexer("a + b");
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(5, lexer.nextToken().getTokenType());
assertEquals("", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(5, lexer.nextToken().getTokenType());
assertEquals("+", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(2, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("b", lexer.getXPath());
assertEquals(2, lexer.currentPosition());
assertEquals(3, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenIdentifier() {
lexer = new XPathLexer("a.b c.d e");
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals(".", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(2, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("b", lexer.getXPath());
assertEquals(2, lexer.currentPosition());
assertEquals(3, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals(".", lexer.getXPath());
assertEquals(3, lexer.currentPosition());
assertEquals(4, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("c", lexer.getXPath());
assertEquals(4, lexer.currentPosition());
assertEquals(5, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals(".", lexer.getXPath());
assertEquals(5, lexer.currentPosition());
assertEquals(6, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("d", lexer.getXPath());
assertEquals(6, lexer.currentPosition());
assertEquals(7, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals(".", lexer.getXPath());
assertEquals(7, lexer.currentPosition());
assertEquals(8, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("e", lexer.getXPath());
assertEquals(8, lexer.currentPosition());
assertEquals(9, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenStringLiteral() {
lexer = new XPathLexer("'abc'");
assertEquals(27, lexer.nextToken().getTokenType());
assertEquals("'abc'", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(6, lexer.endPosition());
assertEquals("-1", lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenComment() {
lexer = new XPathLexer("/* This is a comment */ a + b");
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(5, lexer.nextToken().getTokenType());
assertEquals("", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(2, lexer.endPosition());
assertEquals(5, lexer.nextToken().getTokenType());
assertEquals("+", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(3, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("b", lexer.getXPath());
assertEquals(3, lexer.currentPosition());
assertEquals(4, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenNumbers() {
lexer = new XPathLexer("123.456.789");
assertEquals(31, lexer.nextToken().getTokenType());
assertEquals("123.456.789", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(11, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenIdenticalTokens() {
lexer = new XPathLexer("aaa");
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(1, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(1, lexer.currentPosition());
assertEquals(2, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(2, lexer.currentPosition());
assertEquals(3, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenMixedTokens() {
lexer = new XPathLexer("123 + a.b*c'def'.");
assertEquals(31, lexer.nextToken().getTokenType());
assertEquals("123", lexer.getXPath());
assertEquals(0, lexer.currentPosition());
assertEquals(4, lexer.endPosition());
assertEquals(5, lexer.nextToken().getTokenType());
assertEquals("", lexer.getXPath());
assertEquals(4, lexer.currentPosition());
assertEquals(4, lexer.endPosition());
assertEquals(5, lexer.nextToken().getTokenType());
assertEquals("+", lexer.getXPath());
assertEquals(4, lexer.currentPosition());
assertEquals(5, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("a", lexer.getXPath());
assertEquals(5, lexer.currentPosition());
assertEquals(6, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals(".", lexer.getXPath());
assertEquals(6, lexer.currentPosition());
assertEquals(7, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("b", lexer.getXPath());
assertEquals(7, lexer.currentPosition());
assertEquals(8, lexer.endPosition());
assertEquals(15, lexer.nextToken().getTokenType());
assertEquals("*", lexer.getXPath());
assertEquals(8, lexer.currentPosition());
assertEquals(9, lexer.endPosition());
assertEquals(27, lexer.nextToken().getTokenType());
assertEquals("'def'", lexer.getXPath());
assertEquals(9, lexer.currentPosition());
assertEquals(12, lexer.endPosition());
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens() {
lexer = new XPathLexer("123xyz");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens2() {
lexer = new XPathLexer("123<45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens3() {
lexer = new XPathLexer("123!45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens4() {
lexer = new XPathLexer("123++45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens5() {
lexer = new XPathLexer("123--45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens6() {
lexer = new XPathLexer("123**45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens7() {
lexer = new XPathLexer("123&&45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens8() {
lexer = new XPathLexer("123||45");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens9() {
lexer = new XPathLexer("123||");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens10() {
lexer = new XPathLexer("123&&");
assertEquals(-1, lexer.nextToken().getTokenType());
}

@Test
public void testNextTokenInvalidTokens11() {
lexer = new XPathLexer("123!=");
assertEquals(-1, lexer.nextToken().getTokenType());
}
}