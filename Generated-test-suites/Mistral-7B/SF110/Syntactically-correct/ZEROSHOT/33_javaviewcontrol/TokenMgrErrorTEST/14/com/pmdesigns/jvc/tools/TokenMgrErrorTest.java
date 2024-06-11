package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testTokenMgrErrorConstructor() {
 tokenMgrError = new TokenMgrError();
 assertNull(tokenMgrError.getMessage());
 assertFalse(tokenMgrError.isInstanceOf(Exception.class));
 assertNull(tokenMgrError.errorCode);

 tokenMgrError = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
 assertEquals("Test Message", tokenMgrError.getMessage());
 assertTrue(tokenMgrError.isInstanceOf(Exception.class));
 assertEquals(TokenMgrError.LEXICAL_ERROR, tokenMgrError.errorCode);
 }

 @Test
 public void testLexicalErrorConstructor() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "", ';', TokenMgrError.LEXICAL_ERROR);
 String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: <EOF> (0), after : \"\"";
 assertEquals(expectedErrorMessage, tokenMgrError.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalErrorConstructorInvalidArguments() {
 new TokenMgrError(null, 0, 1, 1, "", ';', TokenMgrError.LEXICAL_ERROR);
 }

 @Test
 public void testAddEscapes() {
 String input = "\0abc\tab\n\fc\rf\"\'\\xyz\u000E";
 String expectedOutput = "\\0\\u0000abc\\b\\t\\n\\f\\r\"\\\"\\'\\\\xyz\\u000E";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }
}