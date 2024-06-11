package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes_NormalCharacters() {
 String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0-9\\/()_+={}[]?:\"'<>,. !\"#$%&'*()+;<=>?@^_-`~";
 String expectedOutput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0-9\\/()_+={}[]?:\"'<>,. !\"" +
 "\"#$%&*()+;<=>?@^_-`~";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapes_SpecialCharacters() {
 String input = "\\0\\b\\t\\n\\f\\r\"'\\\\";
 String expectedOutput = "\\0\\b\\t\\n\\f\\r\\\\\"'\\\\\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapes_InvalidCharacter() {
 String input = "Del";
 String expectedOutput = "\\u007F";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalError_NoEOFSeen() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "abc";
 char curChar = 'd';
 int reason = TokenMgrError.LEXICAL_ERROR;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedOutput = "Lexical error at line 1, column 1. Encountered: d (\\u0064), after : abc";
 String output = error.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalError_EOFSeen() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '\u0000';
 int reason = TokenMgrError.LEXICAL_ERROR;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedOutput = "Lexical error at line 1, column 1. Encountered: <EOF> ";
 String output = error.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInvalidLexicalState() {
 boolean eofSeen = false;
 int lexState = 2;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "abc";
 char curChar = 'd';
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedOutput = "Lexical error at line 1, column 1. Invalid lexical state: " + lexState;
 String output = error.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLoopDetected() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "abc";
 char curChar = 'a';
 int reason = TokenMgrError.LOOP_DETECTED;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedOutput = "Loop detected at line 1, column 1.";
 String output = error.getMessage();
 assertEquals(expectedOutput, output);
 }
}