package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

private XPathLexer lexer;

@Test
public void testEmptyXPath() {
lexer = new XPathLexer("");
Token token = lexer.nextToken();
assertEquals(new Token(-2, "", 0, 0), token);
}

@Test
public void testSimpleXPath() {
lexer = new XPathLexer("/test/child");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("test", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("child", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testXPathWithSpaces() {
lexer = new XPathLexer("// element [ @ attr = 'value' ]");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("element", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SPACE, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.LEFT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("attr", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SPACE, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.EQUALS, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.SPACE, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("value", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.RIGHT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testXPathWithSpecialChars() {
lexer = new XPathLexer("//test[@href='http://example.com/test#fragment']");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("test", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.LEFT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("href", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.EQUALS, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.SPACE, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.QUOTATION_MARK, token.getTokenType());
token = lexer.nextToken();
assertEquals("http://example.com/test#fragment", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.RIGHT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testXPathWithNumbers() {
lexer = new XPathLexer("/1/2/3");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("1", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("2", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("3", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testXPathWithOperators() {
lexer = new XPathLexer("/test/child1/child2[2][3]/@attribute");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("test", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("child1", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("child2", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.LEFT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.NUMBER, token.getTokenType());
assertEquals("2", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.LEFT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.NUMBER, token.getTokenType());
assertEquals("3", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.RIGHT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("attribute", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testXPathWithComments() {
lexer = new XPathLexer("/comment()");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("comment", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testInvalidXPath() {
lexer = new XPathLexer("//test[test");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("test", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.LEFT_SQUARE_BRACKET, token.getTokenType());
token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
assertEquals("test", token.getValue());
token = lexer.nextToken();
assertEquals(TokenType.INVALID_TOKEN, token.getTokenType());
}
}