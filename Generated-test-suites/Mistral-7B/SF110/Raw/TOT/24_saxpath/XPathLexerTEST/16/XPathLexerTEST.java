package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;
 private String xpath;

 @Before
 public void setUp() {
 xpath = "//div[@id='test']/p[1]/a[@href='#sub']";
 lexer = new XPathLexer(xpath);
 }

 @Test
 public void testNextToken_ValidXPath_ReturnsExpectedToken() {
 Token token;

 token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.GREATER_THAN, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.GREATER_THAN, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.LEFT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.ID, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.EQUALS, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.ID, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.LEFT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.ID, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.RIGHT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.P, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.LEFT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.DIGIT, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.DIGIT, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.RIGHT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SLASH, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.A, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.LEFT_ANGLE_BRACKET, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.HREF, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.EQUALS, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.QUOTE, token.getTokenType());
 token = lexper.nextToken();
 assertEquals(TokenType.HASH, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.SUB, token.getTokenValue());
 token = lexer.nextToken();
 assertEquals(TokenType.QUOTE, token.getTokenType());
 token = lexer.nextToken();
 assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextToken_EmptyXPath_ThrowsException() {
 lexer = new XPathLexer("");
 lexer.nextToken();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextToken_NullXPath_ThrowsException() {
 lexer = new XPathLexer(null);
 lexer.nextToken();
 }

 @Test
 public void testNextToken_InvalidXPath_ReturnsUnexpectedToken() {
 lexer = new XPathLexer("//div/@id=test");
 Token token = lexer.nextToken();
 assertNotEquals(TokenType.ID, token.getTokenValue());
 }
}