package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

private XPathLexer lexer;

@Test
public void testNextToken_EmptyString() {
lexer = new XPathLexer("");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.EOF, token.getTokenType());
}

@Test
public void testNextToken_Whitespace() {
lexer = new XPathLexer("    someXPath   ");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.WHITESPACE, token.getTokenType());
}

@Test
public void testNextToken_Identifier() {
lexer = new XPathLexer("myIdentifier");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
Assert.assertEquals("myIdentifier", token.getValue());
}

@Test
public void testNextToken_Operator() {
lexer = new XPathLexer("myIdentifier + myIdentifier");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.PLUS, token.getTokenType());
}

@Test
public void testNextToken_Number() {
lexer = new XPathLexer("123");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.NUMBER, token.getTokenType());
Assert.assertEquals("123", token.getValue());
}

@Test
public void testNextToken_Literal() {
lexer = new XPathLexer("'someString'");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.LITERAL, token.getTokenType());
Assert.assertEquals("someString", token.getValue());
}

@Test
public void testNextToken_Dollar() {
lexer = new XPathLexer("$");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.DOLLAR, token.getTokenType());
}

@Test
public void testNextToken_Comma() {
lexer = new XPathLexer(",");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.COMMA, token.getTokenType());
}

@Test
public void testNextToken_Slashes() {
lexer = new XPathLexer("/");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.SLASHES, token.getTokenType());
}

@Test
public void testNextToken_LeftParen() {
lexer = new XPathLexer("(");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
}

@Test
public void testNextToken_RightParen() {
lexer = new XPathLexer(")");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
}

@Test
public void testNextToken_LeftBracket() {
lexer = new XPathLexer("[");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
}

@Test
public void testNextToken_RightBracket() {
lexer = new XPathLexer("]");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
}

@Test
public void testNextToken_At() {
lexer = new XPathLexer("@");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.AT, token.getTokenType());
}

@Test
public void testNextToken_Colon() {
lexer = new XPathLexer(":");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.COLON, token.getTokenType());
}

@Test
public void testNextToken_DoubleColon() {
lexer = new XPathLexer("::");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
}

@Test
public void testNextToken_Not() {
lexer = new XPathLexer("!");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.NOT, token.getTokenType());
}

@Test
public void testNextToken_NotEquals() {
lexer = new XPathLexer("!=");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
}

@Test
public void testNextToken_Equals() {
lexer = new XPathLexer("=");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.EQUALS, token.getTokenType());
}

@Test
public void testNextToken_Minus() {
lexer = new XPathLexer("-");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.MINUS, token.getTokenType());
}

@Test
public void testNextToken_Plus() {
lexer = new XPathLexer("+");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.PLUS, token.getTokenType());
}

@Test
public void testNextToken_LessThan() {
lexer = new XPathLexer("<");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.LESS_THAN, token.getTokenType());
}

@Test
public void testNextToken_GreaterThan() {lexer = new XPathLexer(">"); Token token = lexer.nextToken(); Assert.assertEquals(TokenType.GREATER_THAN, token.getTokenType()); }

@Test public void testNextToken_LeftBrace() { lexer = new XPathLexer("{"); Token token = lexer.nextToken(); Assert.assertEquals(TokenType.LEFT_BRACE, token.getTokenType()); }

@Test public void testNextToken_RightBrace() { lexer = new XPathLexer("}"); Token token = lexer.nextToken(); Assert.assertEquals(TokenType.RIGHT_BRACE, token.getTokenType()); }

@Test public void testNextToken_Error() { lexer = new XPathLexer("invalidXPath"); Assert.assertEquals(TokenType.ERROR, lexer.nextToken().getTokenType()); }

@Test public void testNextToken_EOF() { lexer = new XPathLexer("myXPath"); lexer.nextToken(); lexer.nextToken(); Assert.assertEquals(TokenType.EOF, lexer.nextToken().getTokenType()); }
}