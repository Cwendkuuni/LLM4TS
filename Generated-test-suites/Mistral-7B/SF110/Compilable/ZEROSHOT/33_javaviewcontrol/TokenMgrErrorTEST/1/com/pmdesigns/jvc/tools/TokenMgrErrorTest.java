package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "\u0000\b\t\n\f\r\"'\\\\";
 String expected = "\\u0000\\b\\t\\n\\f\\r\\\"\\'\\\\\\\\";
 String result = TokenMgrError.addEscapes(input);
 assertEquals(expected, result);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 11;
 String errorAfter = "abc";
 char curChar = 'x';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 assertEquals("Lexical error at line 1, column 11. Encountered: < x > (88,) after : \"abc\"", error.getMessage());
 }

 @Test
 public void testgetMessage() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 }

 @Test
 public void testTokenMgrError() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 2;
 int errorColumn = 12;
 String errorAfter = "def";
 char curChar = 'y';
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expected = "Lexical error at line 2, column 12. Encountered: < y > (89,) after : \"def\"";
 assertEquals(expected, error.getMessage());
 }

 @Test
 public void testTokenMgrErrorWithMessage() {
 String message = "Custom error message";
 int reason = TokenMgrError.LOOP_DETECTED;
 TokenMgrError error = new TokenMgrError(message, reason);
 String expected = "Custom error message";
 assertEquals(expected, error.getMessage());
 }
}