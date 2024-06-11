package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 // Test constructor without arguments
 @Test
 public void testTokenMgrError() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertNotNull(error);
 }

 // Test constructor with message and reason
 @Test
 public void testTokenMgrErrorWithMessageAndReason() {
 String message = "This is a test error message";
 int reason = 42;
 TokenMgrError error = new TokenMgrError(message, reason);
 assertEquals(message, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 // Test constructor with all arguments
 @Test
 public void testTokenMgrErrorWithAllArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "errorAfter";
 char curChar = 'C';
 int reason = 43;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 5, column 10. Encountered: \"C\" (67), after : \"errorAfter\"";
 assertEquals(expectedMessage, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 // Test addEscapes method with normal characters
 @Test
 public void testAddEscapesWithNormalCharacters() {
 String input = "Hello World";
 String expectedOutput = "Hello World";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test addEscapes method with special characters
 @Test
 public void testAddEscapesWithSpecialCharacters() {
 String input = "abcd\0efg\b\tt\nn\nf\rr\"'\'\\";
 String expectedOutput = "abcd\\0efg\\b\\t\\n\\n\\f\\r\\\"'\\'\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test LexicalError method with EOFSeen = false
 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithEOFSeenFalse() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "errorAfter";
 char curChar = 'C';
 int reason = 43;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String message = TokenMgrError.LexicalError(false, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(message, error.getMessage());
 }

 // Test LexicalError method with EOFSeen = true
 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithEOFSeenTrue() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "errorAfter";
 char curChar = 'C';
 int reason = 43;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String message = TokenMgrError.LexicalError(true, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(message, error.getMessage());
 }
}