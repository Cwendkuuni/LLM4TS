package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "Hello\tWorld\nWith\bSpecial\fCharacters\rLike \":\"'\\/";
 String expectedOutput = "Hello\\tWorld\\nWith\\bSpecial\\fCharacters\\rLike \\\"\\\":\"'\\/";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "expected";
 char curChar = '!';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError_EOFSeen() {
 boolean eofSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '\0';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError_InvalidLexicalState() {
 boolean eofSeen = false;
 int lexState = 5; // invalid lexical state
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "after";
 char curChar = 'a';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError_LoopDetected() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "after";
 char curChar = '(';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LOOP_DETECTED);
 }

 @Test
 public void testGetMessage() {
 String message = "This is a test message";
 TokenMgrError error = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
 assertEquals(message, error.getMessage());
 }

 @Test
 public void testConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
 }

 @Test
 public void testConstructor_Message() {
 String message = "This is a test message";
 TokenMgrError error = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
 assertEquals(message, error.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
 }

 @Test
 public void testConstructor_LexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "after";
 char curChar = 'a';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF>  after";
 assertEquals(expectedMessage, error.getMessage());
 }

}