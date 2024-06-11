package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

	@Test
	public void testNextToken_EmptyXPath() {
		XPathLexer lexer = new XPathLexer("");
		Token token = lexer.nextToken();
		assertNull(token);
	}

	@Test
	public void testNextToken_Whitespace() {
		XPathLexer lexer = new XPathLexer("   /a[1]   ");
		Token token = lexer.nextToken();
		assertEquals(TokenType.SLASH, token.getTokenType());
	}

	@Test
	public void testNextToken_Identifier() {
		XPathLexer lexer = new XPathLexer("a");
		Token token = lexer.nextToken();
		assertEquals(TokenType.IDENTIFIER, token.getTokenType());
		assertEquals("a", token.getValue());
	}

	@Test
	public void testNextToken_OperatorName() {
		XPathLexer lexer = new XPathLexer("and");
		Token token = lexer.nextToken();
		assertEquals(TokenType.AND, token.getTokenType());
		assertEquals("and", token.getValue());
	}

	@Test
	public void testNextToken_Number() {
		XPathLexer lexer = new XPathLexer("123");
		Token token = lexer.nextToken();
		assertEquals(TokenType.NUMBER, token.getTokenType());
		assertEquals("123", token.getValue());
	}

	@Test
	public void testNextToken_Dot() {
		XPathLexer lexer = new XPathLexer(".");
		Token token = lexer.nextToken();
		assertEquals(TokenType.DOTS, token.getTokenType());
		assertEquals(".", token.getValue());
	}

	@Test
	public void testNextToken_Slash() {
		XPathLexer lexer = new XPathLexer("/");
		Token token = lexer.nextToken();
		assertEquals(TokenType.SLASH, token.getTokenType());
		assertEquals("/", token.getValue());
	}

	@Test
	public void testNextToken_Comma() {
		XPathLexer lexer = new XPathLexer(",");
		Token token = lexer.nextToken();
		assertEquals(TokenType.COMMA, token.getTokenType());
		assertEquals(",", token.getValue());
	}

	@Test
	public void testNextToken_Dollar() {
		XPathLexer lexer = new XPathLexer("$");
		Token token = lexer.nextToken();
		assertEquals(TokenType.DOLLAR, token.getTokenType());
		assertEquals("$", token.getValue());
	}

	@Test
	public void testNextToken_At() {
		XPathLexer lexer = new XPathLexer("@");
		Token token = lexer.nextToken();
		assertEquals(TokenType.AT, token.getTokenType());
		assertEquals("@", token.getValue());
	}

	@Test
	public void testNextToken_Colon() {
		XPathLexer lexer = new XPathLexer(":");
		Token token = lexer.nextToken();
		assertEquals(TokenType.COLON, token.getTokenType());
		assertEquals(":", token.getValue());
	}

	@Test
	public void testNextToken_DoubleColon() {
		XPathLexer lexer = new XPathLexer("::");
		Token token = lexer.nextToken();
		assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
		assertEquals("::", token.getValue());
	}

	@Test
	public void testNextToken_Plus() {
		XPathLexer lexer = new XPathLexer("+");
		Token token = lexer.nextToken();
		assertEquals(TokenType.PLUS, token.getTokenType());
		assertEquals("+", token.getValue());
	}

	@Test
	public void testNextToken_Minus() {
		XPathLexer lexer = new XPathLexer("-");
		Token token = lexer.nextToken();
		assertEquals(TokenType.MINUS, token.getTokenType());
		assertEquals("-", token.getValue());
	}

	@Test
	public void testNextToken_RelationalOperator() {
		XPathLexer lexer = new XPathLexer("<=");
		Token token = lexer.nextToken();
		assertEquals(TokenType.LE, token.getTokenType());
		assertEquals("<=", token.getValue());
	}

	@Test
	public void testNextToken_Equals() {
		XPathLexer lexer = new XPathLexer("=");
		Token token = lexer.nextToken();
		assertEquals(TokenType.EQ, token.getTokenType());
		assertEquals("=", token.getValue());
	}

	@Test	
	public void testNextToken_NotEquals() {
		XPathLexer lexer = new XPathLexer("!=");
		Token token = lexer.nextToken();
		assertEquals(TokenType.NE, token.getTokenType());
		assertEquals("!=", token.getValue());
	}

	@Test
	public void testNextToken_Not() {
		XPathLexer lexer = new XPathLexer("not");
		Token token = lexer.nextToken();
		assertEquals(TokenType.NOT, token.getTokenType());
		assertEquals("not", token.getValue());
	}

	@Test
	public void testNextToken_Or() {
		XPathLexer lexer = new XPathLexer("or");
		Token token = lexer.nextToken();
		assertEquals(TokenType.OR, token.getTokenType());
		assertEquals("or", token.getValue());
	}

	@Test
	public void testNextToken_XPathWithSpaces() {
		XPathLexer lexer = new XPathLexer("  /a[1]   ");
		Token token = lexer.nextToken();
		assertEquals(TokenType.SLASH, token.getTokenType());
	}

	@Test(expected = IllegalStateException.class)
	public void testNextToken_InvalidXPath() {
		XPathLexer lexer = new XPathLexer("xpathError");
		lexer.nextToken();
	}

	@Test(expected = IllegalStateException.class)
	public void testNextToken_EOF() {
		XPathLexer lexer = new XPathLexer("a");
		Token token = lexer.nextToken();
		assertEquals(TokenType.IDENTIFIER, token.getTokenType());
		assertEquals("a", token.getValue());
		lexer.nextToken(); // consume identifier token
		assertNull(lexer.nextToken());
	}

	@Test
	public void testNextToken_Literals() {
		XPathLexer lexer = new XPathLexer("'foo'");
		Token token = lexer.nextToken();
		assertEquals(TokenType.LITERAL, token.getTokenType());
		assertEquals("'foo'", token.getValue());
		lexer.nextToken(); // consume literal token
	}

}