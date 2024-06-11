package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testNextToken_EmptyString() {
 lexer = new XPathLexer("");
 Token token = lexer.nextToken();
 assertEquals(TokenType.EOF, token.getTokenType());
 }

 @Test
 public void testNextToken_SingleCharacter() {
 lexer = new XPathLexer("/a");
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 }

 @Test
 public void testNextToken_Identifier() {
 lexer = new XPathLexer("a:b/c:d");
 Token token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 assertEquals("a", token.getValue());
 }

 @Test
 public void testNextToken_Whitespace() {
 lexer = new XPathLexer("//a \t/b[1]");
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 }

 @Test
 public void testNextToken_Number() {
 lexer = new XPathLexer("1.2.3");
 Token token = lexer.nextToken();
 assertEquals(TokenType.NUMBER, token.getTokenType());
 assertEquals("1.2.3", token.getValue());
 }

 @Test
 public void testNextToken_Operator() {
 lexer = new XPathLexer("a/b=c");
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 }

 @Test
 public void testNextToken_Literal() {
 lexer = new XPathLexer("'abc'");
 Token token = lexer.nextToken();
 assertEquals(TokenType.LITERAL, token.getTokenType());
 assertEquals("abc", token.getValue());
 }

 @Test
 public void testNextToken_Dollar() {
 lexer = new XPathLexer("$x");
 Token token = lexer.nextToken();
 assertEquals(TokenType.DOLLAR, token.getTokenType());
 }

 @Test
 public void testNextToken_Comma() {
 lexer = new XPathLexer(",");
 Token token = lexer.nextToken();
 assertEquals(TokenType.COMMA, token.getTokenType());
 }

 @Test
 public void testNextToken_LeftBracket() {
 lexer = new XPathLexer("[");
 Token token = lexer.nextToken();
 assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
 }

 @Test
 public void testNextToken_RightBracket() {
 lexer = new XPathLexer("]");
 Token token = lexer.nextToken();
 assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
 }

 @Test
 public void testNextToken_LeftParen() {
 lexer = new XPathLexer("(");
 Token token = lexer.nextToken();
 assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
 }

 @Test
 public void testNextToken_RightParen() {
 lexer = new XPathLexer(")");
 Token token = lexer.nextToken();
 assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
 }

 @Test
 public void testNextToken_Plus() {
 lexer = new XPathLexer("+");
 Token token = lexer.nextToken();
 assertEquals(TokenType.PLUS, token.getTokenType());
 }

 @Test
 public void testNextToken_Minus() {
 lexer = new XPathLexer("-");
 Token token = lexer.nextToken();
 assertEquals(TokenType.MINUS, token.getTokenType());
 }

 @Test
 public void testNextToken_RelationalOperator() {
 lexer = new XPathLexer(">");
 Token token = lexer.nextToken();
 assertEquals(TokenType.GREATER_THAN, token.getTokenType());
 }

 @Test
 public void testNextToken_Equals() {
 lexer = new XPathLexer("=");
 Token token = lexer.nextToken();
 assertEquals(TokenType.EQUALS, token.getTokenType());
 }

 @Test
 public void testNextToken_NotEquals() {
 lexer = new XPathLexer("!=");
 Token token = lexer.nextToken();
 assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
 }

 @Test
 public void testNextToken_Not() {
 lexer = new XPathLexer("!");
 Token token = lexer.nextToken();
 assertEquals(TokenType.NOT, token.getTokenType());
 }

 @Test
 public void testNextToken_And() {
 lexer = new XPathLexer("and");
 Token token = lexer.nextToken();
 assertEquals(TokenType.AND, token.getTokenType());
 }

 @Test
 public void testNextToken_Or() {
 lexer = new XPathLexer("or");
 Token token = lexer.nextToken();
 assertEquals(TokenType.OR, token.getTokenType());
 }

 @Test
 public void testNextToken_Div() {
 lexer = new XPathLexer("div");
 Token token = lexer.nextToken();
 assertEquals(TokenType.DIV, token.getTokenType());
 }

 @Test
 public void testNextToken_Mod() {
 lexer = new XPathLexer("mod");
 Token token = lexer.nextToken();
 assertEquals(TokenType.MOD, token.getTokenType());
 }

 @Test
 public void testNextToken_Star() {
 lexer = new XPathLexer("*");
 Token token = lexer.nextToken();
 assertEquals(TokenType.STAR, token.getTokenType());
 }

 @Test
 public void testNextToken_Slashes() {
 lexer = new XPathLexer("/");
 Token token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 }

 @Test
 public void testNextToken_At() {
 lexer = new XPathLexer("@");
 Token token = lexer.nextToken();
 assertEquals(TokenType.AT, token.getTokenType());
 }

 @Test
 public void testNextToken_DoubleColon() {
 lexer = new XPathLexer("::");
 Token token = lexer.nextToken();
 assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
 }

 @Test
 public void testNextToken_Colon() {
 lexer = new XPathLexer(":");
 Token token = lexer.nextToken();
 assertEquals(TokenType.COLON, token.getTokenType());
 }

 @Test
 public void testNextToken_Dot() {
 lexer = new XPathLexer(".");
 Token token = lexer.nextToken();
 assertEquals(TokenType.DOT, token.getTokenType());
 }

 @Test
 public void testNextToken_MultipleTokens() {
 lexer = new XPathLexer("a:b/c:d and e:f or g:h");
 Token token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 assertEquals("a", token.getValue());
 token = lexer.nextToken();
 assertEquals(TokenType.COLON, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 assertEquals("b", token.getValue());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.IDENTIFIER, token.getTokenType());
 assertEquals("c", token.getValue());
 token = lexer.nextToken();
 equipped(TokenType.COLON, token);
 token = lexer.nextToken();
 equipped(TokenType.IDENTIFIER, token);
 assertEquals("d", token.getValue());
 token = lexer.nextToken();
 equipped(TokenType.AND, token);
 token = lexer.nextToken();
 equipped(TokenType.IDENTIFIER, token);
 assertEquals("e", token.getValue());
 token = lexer.nextToken();
 equipped(TokenType.COLON, token);
 token = lexer.nextToken();
 equipped(TokenType.IDENTIFIER, token);
 assertEquals("f", token.getValue());
 token = lexer.nextToken();
 equipped(TokenType.OR, token);
 token = lexer.nextToken();
 equipped(TokenType.IDENTIFIER, token);
 assertEquals("g", token.getValue());
 token = lexer.nextToken();
 equipped(TokenType.COLON, token);
 token = lexer.nextToken();
 equipped(TokenType.IDENTIFIER, token);
 assertEquals("h", token.getValue());
 }

 private void assertEquals(final int expected, final int actual) {
 assertEquals(Integer.toString(expected), Integer.toString(actual));
 }

 private void assertEquals(final TokenType expected, final TokenType actual) {
 assertEquals(expected.name(), actual.name());
 }

 private void assertEquals(final String expected, final String actual) {
 assertEquals(expected, actual);
 }

}