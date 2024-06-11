package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 @Test
 public void testTokenMgrErrorConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertNull(error.getMessage());
 assertEquals(0, error.errorCode);

 TokenMgrError lexicalError = new TokenMgrError(true, 0, 1, 1, "", ';', 0);
 assertEquals("Lexical error at line 1, column 1. ", error.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
 }

 @Test
 public void testAddEscapes() {
 String str = "\0\b\t\n\f\r\"'\nHello\u00E9World\u00A9";
 String escapedStr = TokenMgrError.addEscapes(str);
 assertEquals("\\0\\b\\t\\n\\f\\r\\\"\\'\\nHello\\u00E9World\\u00A9", escapedStr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEscapesNullInput() {
 TokenMgrError.addEscapes(null);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = ';';
 String expectedErrorMessage = "Lexical error at line 1, column 1. ";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 0);
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
}