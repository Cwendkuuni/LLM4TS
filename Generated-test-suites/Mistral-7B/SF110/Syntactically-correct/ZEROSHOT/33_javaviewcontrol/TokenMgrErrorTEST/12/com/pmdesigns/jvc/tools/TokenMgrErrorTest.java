package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "Hello\tWorld\n\r\f\b\n'ABC\\\\DEF\"GHI";
 String expected = "Hello\\tWorld\\n\\r\\f\\b\\n'ABC\\'ABC\\\\DEF\\\"GHI";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEscapesWithNullInput() {
 String input = null;
 String output = TokenMgrError.addEscapes(input);
 fail("Expected IllegalArgumentException, but got " + output);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = '(';
 String expectedErrorMessage = "Lexical error at line 1, column 5. Encountered: (\" after : if";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalErrorWithNullErrorAfter() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = null;
 char curChar = '(';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 fail("Expected IllegalArgumentException, but got " + error.getMessage());
 }

 @Test
 public void testCreateTokenMgrError() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = '(';
 int reason = TokenMgrError.LEXICAL_ERROR;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedErrorMessage = "Lexical error at line 1, column 5. Encountered: (\" after : if";
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTokenMgrErrorWithInvalidReason() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = '(';
 int reason = 4; // Invalid reason code
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 fail("Expected IllegalArgumentException, but got " + error.getMessage());
 }
}