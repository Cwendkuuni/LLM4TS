package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

@Test
public void testNextToken() {
XPathLexer lexer = new XPathLexer("/test[1]");
Token token = lexer.nextToken();
assertEquals(TokenType.SLASH, token.getTokenType());
}

@Test
public void testEmptyXPath() {
XPathLexer lexer = new XPathLexer("");
Token token = lexer.nextToken();
assertNotNull(token);
assertEquals(TokenType.WHITESPACE, token.getTokenType());
}

@Test
public void testWhitespaceXPath() {
XPathLexer lexer = new XPathLexer("    ");
Token token = lexer.nextToken();
assertNotNull(token);
assertEquals(TokenType.WHITESPACE, token.getTokenType());
}

@Test
public void testXPathWithSpecialChars() {
XPathLexer lexer = new XPathLexer("//$myVar[@id='test']");
Token token = lexer.nextToken();
assertEquals(TokenType.DOLLAR, token.getTokenType());
}

@Test
public void testXPathWithIdentifiersAndOperators() {
XPathLexer lexer = new XPathLexer("//my:element[my:attribute='value']");
Token token = lexer.nextToken();
assertEquals(TokenType.IDENTIFIER, token.getTokenType());
}

@Test
public void testMismatchedParentheses() {
XPathLexer lexer = new XPathLexer("//(test[1])");
try {
lexer.nextToken();
fail("Expected IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected
}
}

@Test
public void testXPathWithUnmatchedBrackets() {
XPathLexer lexer = new XPathLexer("//[test[1]");
try {
lexer.nextToken();
fail("Expected IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected
}
}
}