package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorDefaultConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertEquals("", error.getMessage());
 assertEquals(0, error.errorCode);
 }

 @Test
 public void testTokenMgrErrorStringConstructor() {
 TokenMgrError error = new TokenMgrError("Test Error", 1);
 assertEquals("Test Error", error.getMessage());
 assertEquals(1, error.errorCode);
 }

 @Test
 public void testTokenMgrErrorLexicalErrorConstructor() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "errorAfter";
 char curChar = 'c';
 int reason = 2;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> " + (int)curChar + " after : \"" + addEscapes(errorAfter) + "\"";
 assertEquals(expectedMessage, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenMgrErrorLexicalErrorConstructorInvalidArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = null;
 char curChar = 'c';
 int reason = 2;
 // The errorAfter argument should not be null
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenMgrErrorAddEscapesNullArgument() {
 String str = null;
 StringBuffer retval = new StringBuffer();
 for (int i = 0; i < str.length(); ++i) {
 // This should not be reachable since str is null
 switch (str.charAt(i)) {
 // ...
 }
 }
 }

 @Test
 public void testTokenMgrErrorAddEscapes() {
 String str = "Test String";
 String expected = "Test String";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testTokenMgrErrorAddEscapesBackslash() {
 String str = "Test\\String";
 String expected = "Test\\\\String";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testTokenMgrErrorAddEscapesQuotes() {
 String str = "Test\"String";
 String expected = "Test\\\"String";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testTokenMgrErrorAddEscapesSpecialChars() {
 String str = "Test\0\b\t\n\f\r\"'\\";
 String expected = "Test\\0\\b\\t\\n\\f\\r\\\"'\\\\";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

}