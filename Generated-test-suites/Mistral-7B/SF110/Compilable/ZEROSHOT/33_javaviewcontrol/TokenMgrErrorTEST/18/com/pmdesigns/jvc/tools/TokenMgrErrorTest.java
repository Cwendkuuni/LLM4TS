package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "abcdef\nabcd\t123\fxyz\r'\"\\";
 String expected = "abcdef\\nabcd\\t123\\u000cxyz\\r'\\\"\\\\";
 String result = TokenMgrError.addEscapes(input);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorMessageFormat() {
 boolean eofSeen = false;
 int lexState = 123;
 int errorLine = 45;
 int errorColumn = 67;
 String errorAfter = "expectedToken";
 char curChar = '!';
 int reason = 99;
 String expectedMessage = "Lexical error at line 45, column 67. Encountered: ! (63), after : expectedToken";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualMessage = error.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = TokenMgrError.class)
 public void testTokenMgrErrorConstructionWithNullErrorAfter() {
 boolean eofSeen = false;
 int lexState = 123;
 int errorLine = 45;
 int errorColumn = 67;
 char curChar = '!';
 int reason = 99;
 String errorAfter = null;
 new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test
 public void testTokenMgrErrorWithEOFSeen() {
 boolean eofSeen = true;
 int lexState = 123;
 int errorLine = 45;
 int errorColumn = 67;
 String errorAfter = "expectedToken";
 char curChar = '!';
 int reason = 99;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 45, column 67. Encountered: <EOF> ";
 String actualMessage = error.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testTokenMgrErrorWithReasonOnly() {
 int reason = 99;
 TokenMgrError error = new TokenMgrError("", reason);
 String expectedMessage = "TokenMgrError: 99";
 String actualMessage = error.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testTokenMgrErrorWithReasonAndMessage() {
 String message = "Unexpected token";
 int reason = 99;
 TokenMgrError error = new TokenMgrError(message, reason);
 String expectedMessage = "TokenMgrError: Unexpected token: 99";
 String actualMessage = error.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
}