package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testDefaultConstructor() {
 tokenMgrError = new TokenMgrError();
 assertEquals("", tokenMgrError.getMessage());
 assertEquals(0, tokenMgrError.errorCode);
 }

 @Test
 public void testStringConstructor() {
 tokenMgrError = new TokenMgrError("Error message", 0);
 assertEquals("Error message", tokenMgrError.getMessage());
 assertEquals(0, tokenMgrError.errorCode);
 }

 @Test
 public void testLexicalErrorConstructor() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 5, column 10. Encountered: <EOF>  (0), after : \"identifier\"";
 assertEquals(expectedMessage, tokenMgrError.getMessage());
 assertEquals(TokenMgrError.LEXICAL_ERROR, tokenMgrError.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharConstructor() {
 char invalidChar = '\u0001';
 tokenMgrError = new TokenMgrError(false, 0, 0, 0, "", invalidChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test
 public void testAddEscapesMethod() {
 String str = "Hello, World! \t\n\f\r\"'\\/";
 String expected = "Hello, World! \\t\\n\\f\\r\\\"\\'\\\\/";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

}