package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 private TokenMgrError error;

 @Test
 public void testAddEscapes() {
 String input = "abc\ndef\tghi\njkl\r\fmno\bpqr'st\\uvwxyz";
 String expected = "abc\\danb\\ndef\\tghi\\njkl\\r\\rfmno\\bpqr'st\\\\uvwxyz";
 String result = TokenMgrError.addEscapes(input);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = '(';
 String expected = "Lexical error at line 1, column 5. Encountered: ( after : if";
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 String result = error.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testGetMessage() {
 String message = "Invalid input";
 error = new TokenMgrError(message, TokenMgrError.INVALID_LEXICAL_STATE);
 String expected = "Invalid input";
 String result = error.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testConstructorWithAllParameters() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 2;
 int errorColumn = 7;
 String errorAfter = "and";
 char curChar = '&';
 int reason = TokenMgrError.STATIC_LEXER_ERROR;
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expected = "Lexical error at line 2, column 7. Encountered: & after : and";
 String result = error.getMessage();
 assertEquals(expected, result);
 }

 @Test(expected = TokenMgrError.class)
 public void testNullCharInAddEscapes() {
 String input = "ab\0cd";
 String result = TokenMgrError.addEscapes(input);
 fail("Expected TokenMgrError");
 }

 @Test(expected = TokenMgrError.class)
 public void testInvalidCharInAddEscapes() {
 char invalidChar = '\u0001';
 String input = "a" + invalidChar + "b";
 String result = "";
 try {
 result = TokenMgrError.addEscapes(input);
 } catch (TokenMgrError e) {
 //expected
 }
 String expected = "\\u0001";
 assertEquals(expected, result.substring(result.length() - 4));
 }
}