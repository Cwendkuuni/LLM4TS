package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorInstance() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertEquals(0, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithMessage() {
 TokenMgrError error = new TokenMgrError("Custom Error Message", 1);
 assertEquals("Custom Error Message", error.getMessage());
 assertEquals(1, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithLexicalError() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 TokenMgrError lexicalError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 0);
 String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: "; // curChar is not printable, so we test only the preceding part of the message
 assertEquals(expectedErrorMessage, lexicalError.getMessage().substring(0, expectedErrorMessage.length()));
 assertEquals(0, lexicalError.getErrorCode());
 }

 @Test(expected = TokenMgrError.class)
 public void testTokenMgrErrorWithInvalidInput() {
 String invalidInput = "\0test";
 String expectedErrorMessage = "Lexical error at line 1, column 1. Encounters: \"\\0\" (0)";
 try {
 new TokenMgrError(false, 1, 1, 1, "", '\0', 0);
 } catch (TokenMgrError error) {
 assertEquals(expectedErrorMessage, error.getMessage());
 }
 }
}