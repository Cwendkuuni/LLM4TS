package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\tWorld\n\fThis is a test\b\n\r\nHello\"Hello'Hello\\Hello\u0007World";
        String expectedOutput = "Hello\\tWorld\\n\\fThis is a test\\b\\n\\r\\nHello\\\"Hello'Hello\\\\Hello\\u0007World";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapes_EmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testAddEscapes_NullString() {
        String input = null;
        TokenMgrError.addEscapes(input);
    }

    @Test
    public void testAddEscapes_SpecialCharactersOnly() {
        String input = "\\\\\\b\\t\\f\\n\\r\"'\\\\\\u0007";
        String expectedOutput = "\\\\\\\\\\b\\\\t\\\\f\\\\n\\\\r\\\\\"'\\\\\\\\\\u0007";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 7;
        String errorAfter = "Identifier";
        char curChar = ';';
        String expectedErrorMessage = "Lexical error at line 2, column 7.  Encountered: <EOF> ";
        String errorMessage = TokenMgrError.LexicalError(EFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @Test
    public void testTokenMgrError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "invalidToken";
        char curChar = ';';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expectedMessage = "Lexical error at line 1, column 5.  Encountered: <EOF>  (0), after : \"invalidToken\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testCustomError() {
        String message = "customError";
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expectedMessage = "customError";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testCustomErrorMessageWithSpecialCharacters() {
        String message = "customError\\\\\\b\\\\t\\\\f\\\\n\\\\r\"'\\\\\\u0007";
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expectedMessage = "customError\\\\\\\\\\b\\\\t\\\\f\\\\n\\\\r\\\\\"'\\\\\\\\\\u0007";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorArguments() {
        Object[] constructorsArgs = {null, true, null, null, null, (char) 15, 0};
        for (Object arg : constructorsArgs) {
            try {
                new TokenMgrError(constructorsArgs);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testGetMessage_NullMessage() {
        TokenMgrError error = new TokenMgrError();
        String expectedMessage = null;
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_EmptyMessage() {
        TokenMgrError error = new TokenMgrError();
        String expectedMessage = "";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_CustomMessage() {
        String message = "customMessage";
        TokenMgrError error = new TokenMgrError(message);
        String expectedMessage = "customMessage";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}