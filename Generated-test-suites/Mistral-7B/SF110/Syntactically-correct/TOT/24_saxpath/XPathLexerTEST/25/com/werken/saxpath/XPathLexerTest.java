package com.werken.saxpath;

import org.junit.Assert; import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class XPathLexerTest {

private XPathLexer lexer;

@Test
public void testEmptyXPath() {
lexer = new XPathLexer("");
Token token = lexer.nextToken();
Assert.assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
}

@Test
public void testSimpleXPath() {
lexer = new XPathLexer("/a/b");
Token token = null;

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'a'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'b'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // END_OF_INPUT
Assert.assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
}

@Test
public void testXPathWithNumber() {
lexer = new XPathLexer("/a[1]");
Token token = null;

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'a'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '['
Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // '1'
Assert.assertEquals(TokenType.NUMBER, token.getTokenType());

token = lexer.nextToken(); // ']'
Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // END_OF_INPUT
Assert.assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
}

@Test
public void testXPathWithComment() {
lexer = new XPathLexer("/a/*[comment()='test']");
Token token = null;

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'a'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '*'
Assert.assertEquals(TokenType.ASTERISK, token.getTokenType());

token = lexer.nextToken(); // '['
Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // 'c'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'o'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'm'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '('
Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

token = lexer.nextToken(); // 'c'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'o'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'm'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '='
Assert.assertEquals(TokenType.EQUALS, token.getTokenType());

token = lexer.nextToken(); // '\''
Assert.assertEquals(TokenType.QUOTE, token.getTokenType());

token = lexer.nextToken(); // 't'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'e'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 's'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 't'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());
Assert.assertEquals("test", token.getText());

token = lexer.nextToken(); // ']'
Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // END_OF_INPUT
Assert.assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
}

@Test
public void testXPathWithQuotedString() {
lexer = new XPathLexer("/a[contains(.,'test')]");
Token token = null;

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'a'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '['
Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // 'c'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'o'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 'n'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // 't'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '('
Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

token = lexer.nextToken(); // '.'
Assert.assertEquals(TokenType.DOT, token.getTokenType());

token = lexer.nextToken(); // '.'
Assert.assertEquals(TokenType.DOT, token.getTokenType());

token = lexer.nextToken(); // '.'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // ']'
Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // ']'
Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

token = lexer.nextToken(); // END_OF_INPUT
Assert.assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
}

@Test
public void testXPathWithError() {
lexer = new XPathLexer("/a/b/c");
Token token = null;

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'a'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

token = lexer.nextToken(); // 'b'
Assert.assertEquals(TokenType.IDENTIFIER, token.getTokenType());

token = lexer.nextToken(); // '/'
Assert.assertEquals(TokenType.SLASH, token.getTokenType());

// Error: missing identifier or operator name
Assert.assertNull(lexer.nextToken());
}
}