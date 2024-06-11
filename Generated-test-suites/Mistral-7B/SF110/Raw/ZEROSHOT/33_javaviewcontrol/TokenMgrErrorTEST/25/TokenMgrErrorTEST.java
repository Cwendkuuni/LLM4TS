package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testNewInstanceWithLexicalErrorArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "errorAfter";
 char curChar = '\u0001';
 int reason = 1;
 TokenMgrError lexicalError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <\u0001> (1, " +
 "after : \"" + errorAfter + "\"" +
 "";
 assertEquals(expectedMessage, lexicalError.getMessage());
 assertEquals(reason, lexicalError.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithInvalidArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = null;
 char curChar = '\u0001';
 int reason = 1;
 try {
 new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testAddEscapes() {
 String str = "\0\b\t\n\f\r\"'\\\u0001";
 String expected = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\\\u0001";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }
}