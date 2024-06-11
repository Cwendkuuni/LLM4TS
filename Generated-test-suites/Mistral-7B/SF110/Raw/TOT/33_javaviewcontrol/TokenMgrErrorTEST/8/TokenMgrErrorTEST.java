package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "Hello\nWorld!";
 String expectedOutput = "Hello\\nWorld!";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalError() {
 String errorMessage = TokenMgrError.LexicalError(false, 0, 1, 5, "if", ';');
 String expectedOutput = "Lexical error at line 1, column 5.  Encountered: ;  after : if";
 assertEquals(expectedOutput, errorMessage);
 }

 @Test
 public void testConstructor() {
 String errorMessage = "Custom Error Message";
 int errorReason = 123;
 TokenMgrError error = new TokenMgrError(errorMessage, errorReason);
 String actualMessage = error.getMessage();
 String expectedMessage = errorMessage;
 assertEquals(expectedMessage, actualMessage);
 assertEquals(errorReason, error.getErrorCode());
 }

 @Test
 public void testLexicalErrorConstructor() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = ';';
 int errorReason = 123;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, errorReason);
 String actualMessage = error.getMessage();
 String expectedMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedMessage, actualMessage);
 assertEquals(errorReason, error.getErrorCode());
 }
}