package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

private XPathLexer lexer;

@Test
public void testNextToken_EmptyXPath() {
lexer = new XPathLexer("");
Assert.assertEquals(TokenType.WHITESPACE, lexer.nextToken().getTokenType());
Assert.assertEquals(0, lexer.currentPosition());
Assert.assertEquals(0, lexer.endPosition());
}

@Test
public void testNextToken_SimpleXPath() {
lexer = new XPathLexer("/element/child[1]");
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(1, lexer.currentPosition());
Assert.assertEquals(5, lexer.endPosition());
Assert.assertEquals("/", lexer.getXPath());
Assert.assertNull(lexer.previousToken);

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(6, lexer.currentPosition());
Assert.assertEquals(10, lexer.endPosition());
Assert.assertEquals("element", lexer.getXPath().substring(0, 7));
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(11, lexer.currentPosition());
Assert.assertEquals(14, lexer.endPosition());
Assert.assertEquals("/", lexer.getXPath().substring(7, 8));

Assert.assertEquals(TokenType.LEFT_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(15, lexer.currentPosition());
Assert.assertEquals(16, lexer.endPosition());
Assert.assertEquals("[", lexer.getXPath().substring(14, 15));

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(17, lexer.currentPosition());
Assert.assertEquals(19, lexer.endPosition());
Assert.assertEquals("child", lexer.getXPath().substring(16, 21));

Assert.assertEquals(TokenType.LEFT_PARENTHESIS, lexer.nextToken().getTokenType());
Assert.assertEquals(20, lexer.currentPosition());
Assert.assertEquals(21, lexer.endPosition());
Assert.assertEquals("(", lexer.getXPath().substring(21, 22));

Assert.assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
Assert.assertEquals(22, lexer.currentPosition());
Assert.assertEquals(23, lexer.endPosition());
Assert.assertEquals("1", lexer.getXPath().substring(22, 23));

Assert.assertEquals(TokenType.RIGHT_PARENTHESIS, lexer.nextToken().getTokenType());
Assert.assertEquals(24, lexer.currentPosition());
Assert.assertEquals(25, lexer.endPosition());
Assert.assertEquals(")", lexer.getXPath().substring(24, 25));

Assert.assertEquals(TokenType.WHITESPACE, lexer.nextToken().getTokenType());
Assert.assertEquals(26, lexer.currentPosition());
Assert.assertEquals(27, lexer.endPosition());
Assert.assertEquals("", lexer.getXPath().substring(26, 27));

Assert.assertNull(lexer.nextToken());
Assert.assertEquals(-1, lexer.currentPosition());
Assert.assertEquals(-1, lexer.endPosition());
}

@Test
public void testNextToken_XPathWithOperator() {
lexer = new XPathLexer("/element[contains(child::child1, 'test')]");
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(0, lexer.currentPosition(), "Check current position after getting first slash");
Assert.assertEquals(12, lexer.endPosition(), "Check end position after getting first slash");

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(12, lexer.currentPosition());
Assert.assertEquals(17, lexer.endPosition());
Assert.assertEquals("element", lexer.getXPath().substring(0, 7));

Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(18, lexer.currentPosition());
Assert.assertEquals(22, lexer.endPosition());
Assert.assertEquals("/", lexer.getXPath().substring(8, 9));

Assert.assertEquals(TokenType.LEFT_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(23, lexer.currentPosition());
Assert.assertEquals(24, lexer.endPosition());
Assert.assertEquals("[", lexer.getXPath().substring(22, 23));

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(25, lexer.currentPosition());
Assert.assertEquals(32, lexer.endPosition());
Assert.assertEquals("contains", lexer.getXPath().substring(24, 31));

Assert.assertEquals(TokenType.LEFT_PAREN, lexer.nextToken().getTokenType());
Assert.assertEquals(33, lexer.currentPosition());
Assert.assertEquals(34, lexer.endPosition());
Assert.assertEquals("(", lexer.getXPath().substring(32, 33));

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(35, lexer.currentPosition());
Assert.assertEquals(39, lexer.endPosition());
Assert.assertEquals("child", lexer.getXPath().substring(34, 39));

Assert.assertEquals(TokenType.COLON, lexer.nextToken().getTokenType());
Assert.assertEquals(40, lexer.currentPosition());
Assert.assertEquals(41, lexer.endPosition());
Assert.assertEquals(":", lexer.getXPath().substring(39, 40));

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(42, lexer.currentPosition());
Assert.assertEquals(47, lexer.endPosition());
Assert.assertEquals("child1", lexer.getXPath().substring(41, 46));

Assert.assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getTokenType());
Assert.assertEquals(48, lexer.currentPosition());
Assert.assertEquals(52, lexer.endPosition());
Assert.assertEquals("'test'", lexer.getXPath().substring(48, 52));

Assert.assertEquals(TokenType.RIGHT_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(53, lexer.currentPosition());
Assert.assertEquals(54, lexer.endPosition());
Assert.assertEquals("]", lexer.getXPath().substring(52, 53));

Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(55, lexer.currentPosition());
Assert.assertEquals(57, lexer.endPosition());
Assert.assertEquals("/", lexer.getXPath().substring(54, 55));

Assert.assertEquals(TokenType.WHITESPACE, lexer.nextToken().getTokenType());
Assert.assertEquals(58, lexer.currentPosition());
Assert.assertEquals(58, lexer.endPosition());
Assert.assertEquals("", lexer.getXPath().substring(57, 58));

Assert.assertNull(lexer.nextToken());
Assert.assertEquals(-1, lexer.currentPosition());
Assert.assertEquals(-1, lexer.endPosition());
}

@Test(expected = IllegalStateException.class)
public void testNextToken_XPathWithInvalidCharacter() {
lexer = new XPathLexer("/element[@xpath='[abc']");
Assert.assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
Assert.assertEquals(0, lexer.currentPosition());
Assert.assertEquals(12, lexer.endPosition());

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(12, lexer.currentPosition());
Assert.assertEquals(17, lexer.endPosition());
Assert.assertEquals("element", lexer.getXPath().substring(0, 7));

Assert.assertEquals(TokenType.AT, lexer.nextToken().getTokenType());
Assert.assertEquals(18, lexer.currentPosition());
Assert.assertEquals(19, lexer.endPosition());
Assert.assertEquals("@", lexer.getXPath().substring(17, 18));

Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
Assert.assertEquals(20, lexer.currentPosition());
Assert.assertEquals(24, lexer.endPosition());
Assert.assertEquals("xpath", lexer.getXPath().substring(19, 24));

Assert.assertEquals(TokenType.LEFT_BRACKET, lexer.nextToken().getTokenType());
Assert.assertEquals(25, lexer.currentPosition());
Assert.assertEquals(26, lexer.endPosition());
Assert.assertEquals("[", lexer.getXPath().substring(24, 25));

Assert.assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getTokenType());
Assert.assertEquals(27, lexer.currentPosition());
Assert.assertEquals(33, lexer.endPosition());
Assert.assertEquals("'[abc'", lexer.getXPath().substring(26, 32));

Assert.assertFalse(lexer.hasMoreChars());
Assert.assertEquals(34, lexer.currentPosition());
Assert.assertEquals(34, lexer.endPosition());

lexer.nextToken(); // This should throw an exception
}
}