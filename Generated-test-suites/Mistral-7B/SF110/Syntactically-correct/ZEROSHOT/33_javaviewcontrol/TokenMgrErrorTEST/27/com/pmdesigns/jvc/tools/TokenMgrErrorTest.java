package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testNullConstructor() {
 tokenMgrError = new TokenMgrError();
 assertNotNull(tokenMgrError);
 }

 @Test
 public void testStringConstructor() {
 tokenMgrError = new TokenMgrError("Test Error Message", 1);
 assertEquals("Test Error Message", tokenMgrError.getMessage());
 assertEquals(1, tokenMgrError.errorCode);
 }

 @Test
 public void testBooleanIntIntStringCharConstructor() {
 tokenMgrError = new TokenMgrError(true, 1, 1, 1, "errorAfter", 'E', 1);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> (" + (int)'E' + "), after : \"errorAfter\"";
 assertEquals(expectedMessage, tokenMgrError.getMessage());
 assertEquals(1, tokenMgrError.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharInErrorMessage() {
 tokenMgrError = new TokenMgrError(true, 1, 1, 1, "errorAfter", (char)0, 1);
 fail("Should throw IllegalArgumentException with invalid character in error message");
 }

 @Test
 public void testAddEscapes() {
 String str = "abc\ndef\rghij\tkqrs\t\buvwxyz\0z";
 String expected = "abc\\ndef\\rghij\\t\\tkqrs\\t\\buvwxyz\\u0000z";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorWithEOFSeen() {
 String expected = "Lexical error at line 0, column 0. Encountered: <EOF> ";
 tokenMgrError = new TokenMgrError(true, 0, 0, 0, "", (char)0, 0);
 assertEquals(expected, tokenMgrError.getMessage());
 }

 @Test
 public void testLexicalErrorWithoutEOFSeen() {
 char curChar = 'a';
 String errorAfter = "errorAfter";
 String expected = "Lexical error at line 0, column 0. Encountered: \"a\" (" + (int)'a' + "), after : \"errorAfter\"";
 tokenMgrError = new TokenMgrError(false, 0, 0, 0, errorAfter, curChar, 0);
 assertEquals(expected, tokenMgrError.getMessage());
 }
}