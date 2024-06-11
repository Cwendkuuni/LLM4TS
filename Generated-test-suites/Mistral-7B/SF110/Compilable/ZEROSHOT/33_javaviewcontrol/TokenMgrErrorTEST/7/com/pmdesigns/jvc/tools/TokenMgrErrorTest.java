package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorInstance() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertEquals(0, error.errorCode);
 }

 @Test
 public void testTokenMgrErrorWithMessageAndReason() {
 TokenMgrError error = new TokenMgrError("Custom error message", 1);
 assertEquals("Custom error message", error.getMessage());
 assertEquals(1, error.errorCode);
 }

 @Test
 public void testTokenMgrErrorWithLexicalError() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 2);
 String expectedMessage = "Lexical error at line 5, column 10. Encountered: <EOF> (0), after : \"identifier\"";
 assertEquals(expectedMessage, error.getMessage());
 assertEquals(2, error.errorCode);
 }

 @Test
 public void testAddEscapes() {
 String str = "\u0001abc\nDEF\r\f\"'\\";
 String expected = "\\u0001\\u0061\\u0062\\n\\u0044\\u0065\\u0066\\r\\u000c\"\\'\\\\";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorThrowException() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = TokenMgrError.class)
 public void testStaticLexerErrorThrowException() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.STATIC_LEXER_ERROR);
 }

 @Test(expected = TokenMgrError.class)
 public void testInvalidLexicalStateThrowException() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.INVALID_LEXICAL_STATE);
 }

 @Test(expected = TokenMgrError.class)
 public void testLoopDetectedThrowException() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LOOP_DETECTED);
 }
}