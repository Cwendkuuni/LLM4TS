package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testTokenMgrErrorConstructor() {
 tokenMgrError = new TokenMgrError();
 assertNull(tokenMgrError.getMessage());
 assertNull(tokenMgrError.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, tokenMgrError.errorCode);
 }

 @Test
 public void testTokenMgrErrorConstructorWithMessageAndReason() {
 tokenMgrError = new TokenMgrError("Custom Message", TokenMgrError.INVALID_LEXICAL_STATE);
 assertEquals("Custom Message", tokenMgrError.getMessage());
 assertEquals("Custom Message", super.getMessage());
 assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, tokenMgrError.errorCode);
 }

 @Test
 public void testTokenMgrErrorConstructorWithBooleanEofSeenLexStateErrorLineErrorColumnErrorAfterCurCharReason() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 2;
 String errorAfter = "errorAfter";
 char curChar = 'c';
 int reason = TokenMgrError.LOOP_DETECTED;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedErrorMessage = "Lexical error at line 1, column 2. Encountered: \"" + curChar + "\" (" + (int)curChar + "), after : \"" + errorAfter + "\"";
 assertEquals(expectedErrorMessage, tokenMgrError.getMessage());
 assertEquals(expectedErrorMessage, super.getMessage());
 assertEquals(TokenMgrError.LOOP_DETECTED, tokenMgrError.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenMgrErrorConstructorWithNullErrorAfter() {
 tokenMgrError = new TokenMgrError(true, 1, 1, 1, null, 'c', TokenMgrError.LOOP_DETECTED);
 fail("Expected IllegalArgumentException but got: " + tokenMgrError.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenMgrErrorConstructorWithNegativeErrorLine() {
 tokenMgrError = new TokenMgrError(true, 1, -1, 1, "errorAfter", 'c', TokenMgrError.LOOP_DETECTED);
 fail("Expected IllegalArgumentException but got: " + tokenMgrError.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenMgrErrorConstructorWithNegativeErrorColumn() {
 tokenMgrError = new TokenMgrError(true, 1, 1, -1, "errorAfter", 'c', TokenMgrError.LOOP_DETECTED);
 fail("Expected IllegalArgumentException but got: " + tokenMgrError.getMessage());
 }

 @Test
 public void testAddEscapes() {
 String inputStr = "abc\ndef\t\0123\"'\\\\";
 String expectedOutput = "abc\\ndef\\\\t\\u000B123\\\"'\\\\\\\\";
 String outputStr = TokenMgrError.addEscapes(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 2;
 String errorAfter = "errorAfter";
 char curChar = 'c';
 String expectedErrorMessage = "Lexical error at line 1, column 2. Encountered: \"" + curChar + "\" (" + (int)curChar + "), after : \"" + errorAfter + "\"";
 String errorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedErrorMessage, errorMessage);
 }
}