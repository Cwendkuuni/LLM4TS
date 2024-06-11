package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "\0\b\t\n\f\r\"'\\\\";
 String expected = "\\u0000\\b\\t\\n\\f\\r\\\"\\'\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expected, output);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "identifier";
 char curChar = '(';
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 assertEquals("Lexical error at line 1, column 5. Encountered: ( \" (40), after : identifier", error.getMessage());
 }

 @Test
 public void testConstructors() {
 String message = "Unexpected token";
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 TokenMgrError error1 = new TokenMgrError(message, reason);
 assertEquals(message, error1.getMessage());
 assertEquals(reason, error1.errorCode);

 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 2;
 int errorColumn = 6;
 String errorAfter = "if";
 char curChar = ';';
 int reason2 = TokenMgrError.LOOP_DETECTED;
 TokenMgrError error2 = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason2);
 assertEquals("Lexical error at line 2, column 6. Encountered: ; <semicolon> (59), after : if", error2.getMessage());
 }

 @Test
 public void testGetMessage() {
 String message = "Test error message";
 TokenMgrError error = new TokenMgrError(message, 0);
 assertEquals(message, error.getMessage());
 }

 @Test
 public void testEmptyConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 }
}