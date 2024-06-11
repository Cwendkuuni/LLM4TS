package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for TokenMgrError.
 */
public class TokenMgrErrorTest {

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals("", error.getMessage());
        assertEquals(0, error.getErrorCode());
    }

    /**
     * Test constructor with message and reason.
     */
    @Test
    public void testTokenMgrErrorConstructorWithMessageAndReason() {
        String message = "Test message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.getErrorCode());
    }

    /**
     * Test constructor with lexical error arguments.
     */
    @Test
    public void testTokenMgrErrorConstructorWithLexicalErrorArguments() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "test";
        char curChar = 'a';
        int reason = 1;
        String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> ";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.getErrorCode());
    }

    /**
     * Test method addEscapes.
     */
    @Test
    public void testAddEscapes() {
        String str = "\0\b\t\n\f\r\"'\\\\abcdé";
        String expected = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\\\u0061\\u0062\\u0063\\u0065\\u00E9";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    /**
     * Test method LexicalError.
     */
    @Test
    public void testLexicalError() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "test";
        char curChar = 'a';
        String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> ";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 1);
        String message = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedMessage, error.getMessage());
    }
}