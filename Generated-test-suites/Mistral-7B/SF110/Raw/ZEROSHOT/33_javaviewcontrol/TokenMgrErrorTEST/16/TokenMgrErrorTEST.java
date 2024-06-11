package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testConstructor() {
 tokenMgrError = new TokenMgrError();
 assertNull(tokenMgrError.getMessage());
 assertFalse(tokenMgrError.isInstanceOf(TokenMgrError.class));
 assertNull(tokenMgrError.errorCode);

 tokenMgrError = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
 assertEquals("Test Message", tokenMgrError.getMessage());
 assertTrue(tokenMgrError.isInstanceOf(TokenMgrError.class));
 assertEquals(TokenMgrError.LEXICAL_ERROR, tokenMgrError.errorCode);
 }

 @Test
 public void testLexicalErrorConstructor() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "test", 'a', TokenMgrError.LEXICAL_ERROR);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"a\" (97), after : \"test\"";
 assertEquals(expectedMessage, tokenMgrError.getMessage());
 }

 @Test(expected = NullPointerException.class)
 public void testNullErrorAfter() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, null, 'a', TokenMgrError.LEXICAL_ERROR);
 }

 @Test
 public void testGetMessage() {
 tokenMgrError = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
 assertEquals("Test Message", tokenMgrError.getMessage());

 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "test", 'a', TokenMgrError.LEXICAL_ERROR);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"a\" (97), after : test";
 assertEquals(expectedMessage, tokenMgrError.getMessage());
 }

 @Test
 public void testAddEscapes() {
 String input = "\0\b\t\n\f\r\"'\\\\";
 String expectedOutput = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesWithUnicode() {
 String input = "\u0001";
 String expectedOutput = "\\u0001";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesWithString() {
 String input = "test";
 String expectedOutput = "test";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }
}