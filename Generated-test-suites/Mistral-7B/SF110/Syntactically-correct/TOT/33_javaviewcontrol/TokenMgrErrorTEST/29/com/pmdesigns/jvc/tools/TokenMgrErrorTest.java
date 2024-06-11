package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello World! \b\t\n\f\r\"'\\\n\u0000\u0001";
        String expectedOutput = "Hello World! \\b\\t\\n\\f\\r\\\"'\\\\n\\u0000\\u0001";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalErrorConstructor() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 10;
        String errorAfter = "if";
        char curChar = '(';
        String expectedErrorMessage = "Lexical error at line 1, column 10. Encountered: ( after : if";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        String actualErrorMessage = error.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testErrorConstructor() {
        String message = "Invalid state: 5";
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expectedErrorMessage = "Invalid state: 5";
        String actualErrorMessage = error.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

}