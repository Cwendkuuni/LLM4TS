package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String input = "Hello\tWorld!";
 String expectedOutput = "Hello\\tWorld!";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithEOFSeenAndValidParams() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithInvalidParams() {
 boolean eofSeen = false;
 int lexState = -1;
 int errorLine = 0;
 int errorColumn = 0;
 String errorAfter = null;
 char curChar = '\u0000';
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithInvalidChar() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = '\uFFFF';
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test
 public void testgetMessage() {
 String message = "Test error message";
 int reason = 1;
 tokenMgrError = new TokenMgrError(message, reason);
 String output = tokenMgrError.getMessage();
 assertEquals(message, output);
 }

 @Test
 public void testTokenMgrErrorWithValidParams() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = ';';
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedOutput = "Lexical error at line 5, column 10. Encountered: <semicolon> (;), after : identifier";
 String output = tokenMgrError.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTokenMgrErrorWithNullMessage() {
 String message = null;
 int reason = 1;
 tokenMgrError = new TokenMgrError(message, reason);
 String output = tokenMgrError.getMessage();
 assertEquals("", output);
 }
}