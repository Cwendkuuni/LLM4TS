package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "abc\ndef\tghi\njkl\bmno\rfpq\rst\u0001uvwxyz";
 String expected = "abc\\def\\\tghi\\njkl\\bmno\\rfpq\\rst\\u0001uvwxyz";
 String result = TokenMgrError.addEscapes(input);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorMessage() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = '(';
 String expectedMessage = "Lexical error at line 1, column 5. Encountered: ( " + (int)curChar + " after : if";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 String resultMessage = error.getMessage();
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testStaticLexerErrorMessage() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 2;
 int errorColumn = 10;
 String errorAfter = "else";
 char curChar = '{';
 String expectedMessage = "Static lexical error: Encountered: <EOF> after : else";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.STATIC_LEXER_ERROR);
 String resultMessage = error.getMessage();
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testInvalidLexicalStateMessage() {
 boolean eofSeen = false;
 int lexState = 2;
 int errorLine = 3;
 int errorColumn = 15;
 String errorAfter = ";";
 char curChar = ':';
 String expectedMessage = "Invalid lexical state: Encountered: : after : ;";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.INVALID_LEXICAL_STATE);
 String resultMessage = error.getMessage();
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testLoopDetectedMessage() {
 boolean eofSeen = false;
 int lexState = 3;
 int errorLine = 4;
 int errorColumn = 20;
 String errorAfter = "while";
 char curChar = '(';
 String expectedMessage = "Loop detected: Encountered: ( after : while";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LOOP_DETECTED);
 String resultMessage = error.getMessage();
 assertEquals(expectedMessage, resultMessage);
 }

}