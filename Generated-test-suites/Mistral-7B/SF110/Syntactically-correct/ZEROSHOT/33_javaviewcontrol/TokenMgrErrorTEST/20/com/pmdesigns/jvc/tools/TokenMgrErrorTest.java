package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testNewInstance() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertEquals(0, error.errorCode);
 }

 @Test
 public void testNewInstanceWithMessageAndReason() {
 TokenMgrError error = new TokenMgrError("Custom error message", 5);
 assertEquals("Custom error message", error.getMessage());
 assertEquals(5, error.errorCode);
 }

 @Test
 public void testNewInstanceWithLexicalErrorParams() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 10;
 int errorColumn = 20;
 String errorAfter = "errorAfter";
 char curChar = 'c';
 int reason = 6;

 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

 String expectedErrorMessage = LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedErrorMessage, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithNullMessage() {
 String message = null;
 TokenMgrError error = new TokenMgrError(message, 5);
 }

 @Test
 public void testAddEscapes() {
 String input = "abc\ndef\r\t\0\b\f\n\t\'\"".replaceAll("(\\.)", "\\$1");
 String output = TokenMgrError.addEscapes(input);
 String expectedOutput = "abc\\ndef\\r\\t\\u0000\\b\\f\\n\\t\\'\\\"";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 10;
 int errorColumn = 20;
 String errorAfter = "errorAfter";
 char curChar = 'c';
 int reason = 6;
 String expectedErrorMessage = LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);

 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expectedErrorMessage, error.getMessage());
 }
}