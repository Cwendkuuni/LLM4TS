package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String input = "\0\b\t\n\f\r\"'\r\nHello World!\012345\u0061";
 String expectedOutput = "\\0\\b\\t\\n\\f\\r\\\"\\'\\r\\nHello World!\\012345\\u0061";

 tokenMgrError = new TokenMgrError();
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEscapesWithNullInput() {
 tokenMgrError = new TokenMgrError();
 tokenMgrError.addEscapes(null);
 }

 @Test
 public void testGetMessage() {
 String message = "Test error message";
 tokenMgrError = new TokenMgrError(message, 0);
 assertEquals(message, tokenMgrError.getMessage());
 }

 @Test
 public void testConstructors() {
 // Default constructor
 tokenMgrError = new TokenMgrError();

 // Constructor with message and reason
 String errorMessage = "Lexical error at line 1, column 1. Encountered: <EOF>  after : \" \"";
 tokenMgrError = new TokenMgrError(errorMessage, TokenMgrError.LEXICAL_ERROR);

 // All parameters constructor
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '\0';
 int reason = TokenMgrError.LEXICAL_ERROR;
 String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: <EOF>  after : \" \"";
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expectedErrorMessage, tokenMgrError.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorsWithInvalidLexState() {
 int lexState = -1;
 tokenMgrError = new TokenMgrError(true, lexState, 1, 1, "", '\0', TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorsWithInvalidErrorLine() {
 int errorLine = -1;
 tokenMgrError = new TokenMgrError(true, 1, errorLine, 1, "", '\0', TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorsWithInvalidErrorColumn() {
 int errorColumn = -1;
 tokenMgrError = new TokenMgrError(true, 1, 1, errorColumn, "", '\0', TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorsWithNullErrorAfter() {
 tokenMgrError = new TokenMgrError(true, 1, 1, 1, null, '\0', TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorsWithInvalidCurChar() {
 char curChar = '\u0000';
 tokenMgrError = new TokenMgrError(true, 1, 1, 1, "", curChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorsWithInvalidReason() {
 int reason = 4;
 tokenMgrError = new TokenMgrError(true, 1, 1, 1, "", '\0', reason);
 }

}