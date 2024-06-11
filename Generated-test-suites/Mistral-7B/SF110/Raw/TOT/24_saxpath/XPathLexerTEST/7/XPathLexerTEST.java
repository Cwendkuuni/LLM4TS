package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

private XPathLexer lexer;

@Test
public void testEmptyString() {
lexer = new XPathLexer("");
Assert.assertNull(lexer.nextToken());
}

@Test
public void testValidXPath() {
lexer = new XPathLexer("/a[1]/b/c[2]");

Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());

// Test remaining tokens are consumed
Assert.assertNull(lexer.nextToken());
}

@Test
public void testInvalidXPath() {
lexer = new XPathLexer("//a[1]b/c[2");

Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertNotNull(lexer.nextToken()); // Assert error token
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertNull(lexer.nextToken()); // Assert end of string
}

@Test
public void testValidKeywords() {
lexer = new XPathLexer("and or div mod id div id and and and and");

Assert.assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.OR, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.DIV, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.MOD, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.ID, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.ID, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.AND, lexer.nextToken().getTokenType());

// Test remaining tokens are consumed
Assert.assertNull(lexer.nextToken());
}

@Test
public void testValidNumbers() {
lexer = new XPathLexer("1 2 3.5 4.1E-2 5.0E+3");

Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());

// Test remaining tokens are consumed
Assert.assertNull(lexer.nextToken());
}

@Test
public void testValidIdentifiers() {
lexer = new XPathLexer("a b c d _abc123 _1aBc _abc-123 _abc_123 _abc123_");

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());

// Test remaining tokens are consumed
Assert.assertNull(lexer.nextToken());
}

@Test
public void testWhitespace() {
lexer = new XPathLexer("//a[1] /b/ c[2]");

Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());

// Test whitespace tokens are consumed
Assert.assertNull(lexer.nextToken());
}

@Test
public void testValidOperators() {
lexer = new XPathLexer("//a[1] = b/c[2] <> d/e[3] and f/g[4] or h/i[5]");

Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.ASSIGN, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NOT_EQUALS, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.OR, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.LEFT_SQUARE_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(TokenType.RIGHT_SQUARE_BRACKET, lexer.nextToken().getTokenType());

// Test remaining tokens are consumed
Assert.assertNull(lexer.nextToken());
}
}