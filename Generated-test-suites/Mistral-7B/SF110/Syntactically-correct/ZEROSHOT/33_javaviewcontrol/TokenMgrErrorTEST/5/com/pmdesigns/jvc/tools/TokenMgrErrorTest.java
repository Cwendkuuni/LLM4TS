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
 TokenMgrError error = new TokenMgrError("Custom Message", 1);
 assertEquals("Custom Message", error.getMessage());
 assertEquals(1, error.errorCode);
 }

 @Test
 public void testNewInstanceWithLexicalErrorParameters() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "errorAfter";
 char curChar = 'a';
 int reason = 1;
 TokenMgrError lexicalError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> (0), after : \"" + addEscapes(errorAfter) + "\"";
 assertEquals(expectedMessage, lexicalError.getMessage());
 assertEquals(reason, lexicalError.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithNullErrorAfter() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = null;
 char curChar = 'a';
 int reason = 1;
 TokenMgrError lexicalError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test
 public void testAddEscapes() {
 String input = "ab\tc\nef\rghi\nj\tklm\nopq\rst\fuvwxyz";
 String expectedOutput = "ab\\tc\\nef\\rghi\\nj\\tklm\\nopq\\rst\\fuvwxyz";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

}