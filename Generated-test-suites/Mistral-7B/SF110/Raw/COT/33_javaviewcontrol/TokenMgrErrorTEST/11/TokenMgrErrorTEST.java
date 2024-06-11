package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 // Test constructor with no arguments
 @Test
 public void testTokenMgrError() {
 TokenMgrError err = new TokenMgrError();
 assertEquals("", err.getMessage());
 }

 // Test constructor with message and reason
 @Test
 public void testTokenMgrErrorWithMessageAndReason() {
 String message = "Test error message";
 int reason = 123;
 TokenMgrError err = new TokenMgrError(message, reason);
 assertEquals(message, err.getMessage());
 assertEquals(reason, err.errorCode);
 }

 // Test constructor with all arguments
 @Test
 public void testTokenMgrErrorWithAllArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 10;
 int errorColumn = 20;
 String errorAfter = "errorAfter";
 char curChar = '\'';
 int reason = 456;
 TokenMgrError err = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedErrorMessage = "Lexical error at line 10, column 20. Encountered: '\' (" + (int)curChar + "), after : \"errorAfter\"";
 assertEquals(expectedErrorMessage, err.getMessage());
 assertEquals(reason, err.errorCode);
 }

 // Test addEscapes method with normal characters
 @Test
 public void testAddEscapesWithNormalCharacters() {
 String str = "This is a test string.";
 String escapedString = TokenMgrError.addEscapes(str);
 assertEquals("This is a test string.\n", escapedString);
 }

 // Test addEscapes method with control characters
 @Test
 public void testAddEscapesWithControlCharacters() {
 String str = "\0\b\t\n\f\r\"'\\\\";
 String escapedString = TokenMgrError.addEscapes(str);
 assertEquals("\\0\\b\\t\\n\\f\\r\\\"\\'\\\\\\\\", escapedString);
 }

 // Test addEscapes method with characters outside ASCII range
 @Test
 public void testAddEscapesWithOutOfRangeCharacters() {
 String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0100\u01FF";
 String escapedString = TokenMgrError.addEscapes(str);
 assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0100\u01FF", escapedString); // Escaped characters should not be visible in the test output
 String expectedEscapedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0100\u01FF\u0401\u04FF"; // All Unicode characters in the string should be escaped
 assertEquals(expectedEscapedString, escapedString);
 }

 // Test LexicalError method with EOFSeen set to true
 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithEOFSeen() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 10;
 int errorColumn = 20;
 String errorAfter = "";
 char curChar = (char)0;
 int reason = TokenMgrError.LEXICAL_ERROR;
 try {
 TokenMgrError err = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 } catch (TokenMgrError e) {
 assertEquals(TokenMgrError.LEXICAL_ERROR, e.errorCode);
 }
 }

 // Test LexicalError method with EOFSeen set to false
 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithNoEOFSeen() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 10;
 int errorColumn = 20;
 String errorAfter = "errorAfter";
 char curChar = '\'';
 int reason = TokenMgrError.LEXICAL_ERROR;
 try {
 TokenMgrError err = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 } catch (TokenMgrError e) {
 String expectedErrorMessage = "Lexical error at line 10, column 20. Encountered: '\'' (" + (int)curChar + "), after : \"errorAfter\"";
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

}