package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorConstructionWithMessageAndReason() {
 TokenMgrError error = new TokenMgrError("Invalid token detected", 1);
 assertEquals("Invalid token detected", error.getMessage());
 assertEquals(1, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorConstructionWithLexicalErrorArguments() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifer";
 char curChar = '(';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 2);
 String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: (\" (\", 40, after : \"identifer\"";
 assertEquals(expectedErrorMessage, error.getMessage());
 assertEquals(2, error.getErrorCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTokenMgrErrorConstructionWithNegativeErrorCode() {
 new TokenMgrError(false, 0, 5, 10, "identifer", '(', -1);
 }

 @Test
 public void testAddEscapesMethod() {
 String input = "Hello\nWorld!";
 String expectedOutput = "Hello\\nWorld!";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesMethodWithNonPrintableCharacter() {
 String input = "\u0001";
 String expectedOutput = "\\u0001";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesMethodWithPrintableCharacter() {
 String input = "a";
 String expectedOutput = "a";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalErrorMethod() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifer";
 char curChar = '(';
 String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: (\" (\", 40, after : \"identifer\"";
 String errorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedErrorMessage, errorMessage);
 }
}