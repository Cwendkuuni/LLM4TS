package com.pmdesigns.jvc.tools;

import org.junit.Test;
import org.junit.Assert.*;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorInstance() {
 TokenMgrError error = new TokenMgrError();
 Assert.assertEquals("", error.getMessage());
 Assert.assertEquals(0, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithMessageAndReason() {
 TokenMgrError error = new TokenMgrError("Custom Error Message", 1);
 Assert.assertEquals("Custom Error Message", error.getMessage());
 Assert.assertEquals(1, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "semicolon";
 char curChar = ';';
 TokenMgrError lexicalError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);

 String expectedMessage = LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 Assert.assertEquals(expectedMessage, lexicalError.getMessage());
 Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, lexicalError.getErrorCode());
 }

 @Test
 public void testLexicalErrorMessage() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "invalidToken";
 char curChar = '_';
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> () after : \"invalidToken\"";

 if (eofSeen) {
 String message = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 TokenMgrError lexicalError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
 Assert.assertEquals(expectedMessage, lexicalError.getMessage());
 } else {
 String message = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 Assert.assertEquals(expectedMessage, message);
 }
}

 @Test
 public void testInvalidLexicalStateErrorMessage() {
 boolean eofSeen = false;
 int lexState = -1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = 'a';
 String expectedMessage = "Error: Invalid lexical state";

 TokenMgrError invalidLexicalStateError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.INVALID_LEXICAL_STATE);
 Assert.assertEquals(expectedMessage, invalidLexicalStateError.getMessage());
 Assert.assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, invalidLexicalStateError.getErrorCode());
 }

 @Test
 public void testStaticLexerErrorMessage() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '^';
 String expectedMessage = "Static lexical error";

 TokenMgrError staticLexerError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.STATIC_LEXER_ERROR);
 Assert.assertEquals(expectedMessage, staticLexerError.getMessage());
 Assert.assertEquals(TokenMgrError.STATIC_LEXER_ERROR, staticLexerError.getErrorCode());
 }

 @Test
 public void testLoopDetectedErrorMessage() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '<';
 String expectedMessage = "Loop detected";

 TokenMgrError loopDetectedError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LOOP_DETECTED);
 Assert.assertEquals(expectedMessage, loopDetectedError.getMessage());
 Assert.assertEquals(TokenMgrError.LOOP_DETECTED, loopDetectedError.getErrorCode());
 }

 @Test
 public void testAddEscapesFunction() {
 String input = "Hello \nWorld!";
 String expectedOutput = "Hello \\nWorld!";
 String output = TokenMgrError.addEscapes(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesFunctionWithNullString() {
 String input = null;
 String expectedOutput = "";
 String output = TokenMgrError.addEscapes(input);
 Assert.assertEquals(expectedOutput, output);
 }
}