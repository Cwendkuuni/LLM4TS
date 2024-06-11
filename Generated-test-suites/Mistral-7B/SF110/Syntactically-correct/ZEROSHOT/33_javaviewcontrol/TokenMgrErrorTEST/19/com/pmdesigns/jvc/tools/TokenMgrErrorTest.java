package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 @Test
 public void testErrorConstructor() {
 TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "abc", ';', 1);
 assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> \"\" (0), after : \"abc\"", error.getMessage());
 }

 @Test
 public void testErrorConstructorWithMessage() {
 TokenMgrError error = new TokenMgrError("Custom Error Message", 2);
 assertEquals("Custom Error Message", error.getMessage());
 }

 @Test
 public void testAddEscapes() {
 String input = "\"\0\b\t\n\f\r\\\"\\'\u0001\u007F";
 String expectedOutput = "\\\"\\\\\\0\\b\\t\\n\\f\\r\\\\\\\"\\\\'\\u0001\\u007F";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError() {
 throw new RuntimeException("Expected LexicalError");
 }

 @Test
 public void testGetMessage() {
 TokenMgrError error = new TokenMgrError();
 error.setMessage("Custom Error Message");
 assertEquals("Custom Error Message", error.getMessage());
 }

 @Test
 public void testSetErrorCode() {
 TokenMgrError error = new TokenMgrError();
 error.setErrorCode(1);
 assertEquals(1, error.getErrorCode());
 }
}