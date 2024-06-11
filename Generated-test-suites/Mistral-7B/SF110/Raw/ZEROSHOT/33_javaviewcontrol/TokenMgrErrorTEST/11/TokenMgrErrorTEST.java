package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 @Test
 public void testEmptyConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertNull(error.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, error.getErrorCode());
 }

 @Test
 public void testStringConstructor() {
 TokenMgrError error = new TokenMgrError("Test Error", TokenMgrError.LEXICAL_ERROR);
 assertEquals("Test Error", error.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, error.getErrorCode());
 }

 @Test
 public void testBooleanIntIntStringCharConstructor() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "errorAfter";
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedErrorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedErrorMessage, error.getMessage());
 assertEquals(reason, error.getErrorCode());
 }

 @Test
 public void testAddEscapes() {
 String input = "abc\ndef\n\r\t\b\0\"'\\/\b\f\n\r";
 String expectedOutput = "abc\\ndef\\r\\t\\b\\0\\\"'\\\\/\\b\\f\\n\\r";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputInAddEscapes() {
 String input = null;
 TokenMgrError.addEscapes(input);
 }

}