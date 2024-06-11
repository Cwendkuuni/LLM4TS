package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("\\0", TokenMgrError.addEscapes(""));
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("foo", TokenMgrError.addEscapes("foo"));
        assertEquals("\\u0005", TokenMgrError.addEscapes("\u0005"));
        assertEquals("\\u007F", TokenMgrError.addEscapes("\u007F"));
    }

    @Test
    public void testLexicalError() {
        String eofSeen = "true";
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: <EOF> (0), after : \"abc\"";

        if (eofSeen.equals("true")) {
            TokenMgrError error = new TokenMgrError(true, lexState, errorLine, errorColumn, errorAfter, '\0', TokenMgrError.LEXICAL_ERROR);
            assertEquals(expectedErrorMessage, error.getMessage());
        } else {
            TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
            String errorMessage = error.getMessage();
            String messageWithoutEscapes = errorMessage.substring(errorMessage.indexOf("Encountered: ") + 13);
            String escapedCurChar = TokenMgrError.addEscapes(Character.toString(curChar));
            String expectedErrorMessageWithoutEscapes = "Lexical error at line " + errorLine + ", column " + errorColumn + ". Encountered: " + escapedCurChar + " (" + (int)curChar + "), after : \"" + errorAfter + "\"";
            assertEquals(expectedErrorMessageWithoutEscapes, messageWithoutEscapes);
        }
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        String eofSeen = "true";
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        int reason = 4;
        String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: <EOF> (0), after : \"abc\"";

        if (eofSeen.equals("true")) {
            TokenMgrError error = new TokenMgrError(true, lexState, errorLine, errorColumn, errorAfter, '\0', reason);
            assertEquals(expectedErrorMessage, error.getMessage());
            assertEquals(reason, error.getErrorCode());
        } else {
            TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
            String errorMessage = error.getMessage();
            String messageWithoutEscapes = errorMessage.substring(errorMessage.indexOf("Encountered: ") + 13);
            String escapedCurChar = TokenMgrError.addEscapes(Character.toString(curChar));
            String expectedErrorMessageWithoutEscapes = "Lexical error at line " + errorLine + ", column " + errorColumn + ". Encountered: " + escapedCurChar + " (" + (int)curChar + "), after : \"" + errorAfter + "\"";
            assertEquals(expectedErrorMessageWithoutEscapes, messageWithoutEscapes);
            assertEquals(reason, error.getErrorCode());
        }
    }

    @Test
    public void testTokenMgrErrorWithInvalidLexicalState() {
        String eofSeen = "false";
        int lexState = 5;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        int reason = 4;
        String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: \u0058 ('x'), after : \"abc\"";

        TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String errorMessage = error.getMessage();
        String messageWithoutEscapes = errorMessage.substring(errorMessage.indexOf("Encountered: ") + 13);
        String escapedCurChar = TokenMgrError.addEscapes(Character.toString(curChar));
        String expectedErrorMessageWithoutEscapes = "Lexical error at line 1, column 1. Encountered: \u0058 ('x'), after : \"abc\"";
        assertEquals(expectedErrorMessageWithoutEscapes, messageWithoutEscapes);
        assertEquals(reason, error.getErrorCode());
    }

    @Test
    public void testTokenMgrErrorWithStaticLexerError() {
        String eofSeen = "false";
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        int reason = 1;
        String expectedErrorMessage = "Static lexer error";

        TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.getErrorCode());
    }

    @Test
    public void testTokenMgrErrorWithInvalidLexicalStateAndStaticLexerError() {
        String eofSeen = "false";
        int lexState = 5;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        int reason = 5;
        String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: \u0058 ('x'), after : \"abc\"";

        TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String errorMessage = error.getMessage();
        String messageWithoutEscapes = errorMessage.substring(errorMessage.indexOf("Encountered: ") + 13);
        String escapedCurChar = TokenMgrError.addEscapes(Character.toString(curChar));
        String expectedErrorMessageWithoutEscapes = "Lexical error at line 1, column 1. Encountered: \u0058 ('x'), after : \"abc\"";
        assertEquals(expectedErrorMessageWithoutEscapes, messageWithoutEscapes);
        assertEquals(reason, error.getErrorCode());
    }

    @Test
    public void testTokenMgrErrorWithLoopDetected() {
        String eofSeen = "false";
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        int reason = 3;
        String expectedErrorMessage = "Loop detected";

        TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.getErrorCode());
    }

    @Test
    public void testTokenMgrErrorWithLoopDetectedAndLexicalError() {
        String eofSeen = "false";
        int lexState = 5;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'x';
        int reason = 4;
        String expectedErrorMessage = "Lexical error at line 1, column 1. Encountered: \u0058 ('x'), after : \"abc\"";

        TokenMgrError error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String errorMessage = error.getMessage();
        String messageWithoutEscapes = errorMessage.substring(errorMessage.indexOf("Encountered: ") + 13);
        String escapedCurChar = TokenMgrError.addEscapes(Character.toString(curChar));
        String expectedErrorMessageWithoutEscapes = "Lexical error at line 1, column 1. Encountered: \u0058 ('x'), after : \"abc\"";
        assertEquals(expectedErrorMessageWithoutEscapes, messageWithoutEscapes);
        assertEquals(reason, error.getErrorCode());

        error = new TokenMgrError(false, lexState, errorLine, errorColumn, errorAfter, curChar, reason | TokenMgrError.LOOP_DETECTED);
        expectedErrorMessage += " Loop detected";
        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason | TokenMgrError.LOOP_DETECTED, error.getErrorCode());
    }
}