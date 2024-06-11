package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 @Test
 public void testConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertNull(error.getMessage());
 assertNull(error.getErrorMessage());
 assertNull(error.getErrorColumn());
 assertNull(error.getErrorLine());
 assertNull(error.getCurChar());
 assertNull(error.getErrorAfter());
 assertFalse(error.isEOFSeen());
 assertFalse(error.isInvalidState());
 assertFalse(error.isLoopDetected());
 assertFalse(error.isStaticError());
 assertEquals(0, error.getErrorCode());
 }

 @Test
 public void testLexicalErrorConstructor() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = '(';
 int reason = 1;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

 assertEquals("Lexical error at line 5, column 10. Encountered: ( (\", after : identifier", error.getMessage());
 assertTrue(error.isEOFSeen());
 assertFalse(error.isInvalidState());
 assertFalse(error.isLoopDetected());
 assertFalse(error.isStaticError());
 assertEquals(1, error.getErrorCode());
 assertEquals("Lexical error at line 5, column 10. Encountered: ( (\", after : identifier", error.getErrorMessage());
 assertEquals(5, error.getErrorLine());
 assertEquals(10, error.getErrorColumn());
 assertEquals('(', error.getCurChar());
 assertEquals("identifier", error.getErrorAfter());
 assertFalse(error.isInvalidState());
 }

 @Test
 public void testStaticLexerErrorConstructor() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 5;
 int errorColumn = 10;
 int reason = 2;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, null, 0, reason);

 assertEquals("Static lexer error at line 5, column 10", error.getMessage());
 assertFalse(error.isEOFSeen());
 assertFalse(error.isInvalidState());
 assertFalse(error.isLoopDetected());
 assertTrue(error.isStaticError());
 assertEquals(2, error.getErrorCode());
 assertEquals("Static lexer error at line 5, column 10", error.getErrorMessage());
 assertEquals(5, error.getErrorLine());
 assertEquals(10, error.getErrorColumn());
 assertNull(error.getCurChar());
 assertNull(error.getErrorAfter());
 }

 @Test
 public void testInvalidStateConstructor() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 5;
 int errorColumn = 10;
 int reason = 3;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, null, 0, reason);

 assertEquals("Loop detected", error.getMessage());
 assertFalse(error.isEOFSeen());
 assertTrue(error.isInvalidState());
 assertFalse(error.isLoopDetected());
 assertFalse(error.isStaticError());
 assertEquals(3, error.getErrorCode());
 assertEquals("Loop detected", error.getErrorMessage());
 assertEquals(5, error.getErrorLine());
 assertEquals(10, error.getErrorColumn());
 assertNull(error.getCurChar());
 assertNull(error.getErrorAfter());
 }

 @Test
 public void testGetters() {
 boolean eofSeen = true;
 int lexState = 10;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "identifier";
 char curChar = '(';
 int reason = 4;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

 assertTrue(error.isEOFSeen());
 assertFalse(error.isInvalidState());
 assertFalse(error.isLoopDetected());
 assertFalse(error.isStaticError());
 assertEquals(4, error.getErrorCode());
 assertEquals("Lexical error at line 5, column 10. Encountered: ( (\", after : identifier", error.getErrorMessage());
 assertEquals(5, error.getErrorLine());
 assertEquals(10, error.getErrorColumn());
 assertEquals('(', error.getCurChar());
 assertEquals("identifier", error.getErrorAfter());
 }
}