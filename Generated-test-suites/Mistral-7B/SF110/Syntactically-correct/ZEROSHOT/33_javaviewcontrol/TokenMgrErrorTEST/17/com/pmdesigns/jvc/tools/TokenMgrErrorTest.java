package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

 private static final int EOF_SEEN = 1;
 private static final int VALID_LEXICAL_STATE = 0;
 private static final int INVALID_LEXICAL_STATE_VALUE = 10;
 private static final int LOOP_DETECTED_VALUE = 5;
 private static final int ERROR_LINE = 1;
 private static final int ERROR_COLUMN = 2;
 private static final String ERROR_AFTER = "errorAfter";
 private static final char CUR_CHAR = 'c';
 private static final String MESSAGE = "Lexical error at line 1, column 2. Encountered: <EOF> () after : \"errorAfter\"";
 private static final String MESSAGE_WITH_CHAR = "Lexical error at line 1, column 2. Encountered: \"c\" (67), after : \"errorAfter\"";
 private static final String ESCAPED_MESSAGE = "Lexical error at line 1, column 2. Encountered: \\u0063 after : \"errorAfter\"";

 @Test
 public void testTokenMgrErrorInstance() {
 TokenMgrError error = new TokenMgrError();
 assertNull(error.getMessage());
 assertNull(error.getErrorMessage());
 assertNull(error.getErrorColumn());
 assertNull(error.getErrorLine());
 assertNull(error.getErrorAfter());
 assertNull(error.getCurChar());
 assertNull(error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithMessageAndReason() {
 TokenMgrError error = new TokenMgrError("custom message", 1);
 assertEquals("custom message", error.getMessage());
 assertEquals(1, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithLexicalError() {
 TokenMgrError error = new TokenMgrError(EOF_SEEN, VALID_LEXICAL_STATE, ERROR_LINE, ERROR_COLUMN, ERROR_AFTER, CUR_CHAR, LEXICAL_ERROR);
 assertEquals(MESSAGE, error.getMessage());
 assertEquals(LEXICAL_ERROR, error.getErrorCode());
 assertEquals(ERROR_LINE, error.getErrorLine());
 assertEquals(ERROR_COLUMN, error.getErrorColumn());
 assertEquals(ERROR_AFTER, error.getErrorAfter());
 assertEquals(CUR_CHAR, error.getCurChar());
 }

 @Test
 public void testTokenMgrErrorWithLexicalErrorWithChar() {
 TokenMgrError error = new TokenMgrError(EOF_SEEN, VALID_LEXICAL_STATE, ERROR_LINE, ERROR_COLUMN, ERROR_AFTER, CUR_CHAR, LEXICAL_ERROR);
 assertEquals(MESSAGE_WITH_CHAR, error.getErrorMessage());
 }

 @Test
 public void testAddEscapesFunction() {
 String str = "abcdef\u0061\u0062\u0063\u0064\u0065\u0066";
 String escapedStr = TokenMgrError.addEscapes(str);
 assertEquals("abcdef\\u0061\\u0062\\u0063\\u0064\\u0065\\u0066", escapedStr);
 }

 @Test
 public void testAddEscapesFunctionWithInvalidCharacter() {
 String str = "\u0001";
 String escapedStr = TokenMgrError.addEscapes(str);
 assertEquals("\\u0001", escapedStr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEscapesFunctionWithNullString() {
 TokenMgrError.addEscapes(null);
 fail("No IllegalArgumentException was thrown");
 }
}