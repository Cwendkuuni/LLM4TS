package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertEquals(0, error.errorCode);

 TokenMgrError lexicalError = new TokenMgrError(true, 0, 1, 1, "", ';', 0);
 assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (0, ) after : \"\"", lexicalError.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, lexicalError.errorCode);
 }

 @Test
 public void testAddEscapes() {
 assertEquals("", TokenMgrError.addEscapes(""));
 assertEquals("\\b", TokenMgrError.addEscapes("\b"));
 assertEquals("\\t", TokenMgrError.addEscapes("\t"));
 assertEquals("\\n", TokenMgrError.addEscapes("\n"));
 assertEquals("\\f", TokenMgrError.addEscapes("\f"));
 assertEquals("\\r", TokenMgrError.addEscapes("\r"));
 assertEquals("\\\"", TokenMgrError.addEscapes("\""));
 assertEquals("\\'", TokenMgrError.addEscapes("'"));
 assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
 assertEquals("\\u0000", TokenMgrError.addEscapes("\u0000"));
 assertEquals("\\u0001", TokenMgrError.addEscapes("\u0001"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharInAddEscapes() {
 TokenMgrError.addEscapes((char)0xFFFF);
 }

 @Test
 public void testInvalidLexicalStateError() {
 TokenMgrError error = new TokenMgrError(false, 1, 1, 1, "", ';', TokenMgrError.INVALID_LEXICAL_STATE);
 assertEquals("Lexical error at line 1, column 1. Encountered: < invalid lexical state >", error.getMessage());
 }

 @Test
 public void testStaticLexerError() {
 TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "", ';', TokenMgrError.STATIC_LEXER_ERROR);
 assertEquals("Static lexer error", error.getMessage());
 }

 @Test
 public void testLoopDetectedError() {
 TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "", ';', TokenMgrError.LOOP_DETECTED);
 assertEquals("Loop detected", error.getMessage());
 }
}