package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapesNormal() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesNull() {
        String input = null;
        String expectedOutput = null;
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddEscapesInvalidCharacter() {
        String input = "ABC\u0000";
        String expectedOutput = "ABC\\u0000";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        // Test for exception
        String invalidInput = "ABC\uffff";
        TokenMgrError exception = assertThrows(IllegalArgumentException.class, () -> TokenMgrError.addEscapes(invalidInput));
        assertEquals("Illegal argument: Unicode escape sequence is not a printable character", exception.getMessage());
    }

    @Test
    public void testLexicalErrorNullInputs() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 0;
        int errorColumn = 0;
        String errorAfter = null;
        char curChar = '\u0000';
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);

        String expectedMessage = null;
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testLexicalErrorValidInputs() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 123;
        int errorColumn = 45;
        String errorAfter = "errorAfter";
        char curChar = '{' ;
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);

        String expectedMessage = "Lexical error at line 123, column 45. Encountered: <EOF>  (0), after : \"errorAfter\"";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testLexicalErrorInvalidInputs() {
        boolean eofSeen = true;
        int lexState = -1;
        int errorLine = 0;
        int errorColumn = 0;
        String errorAfter = null;
        char curChar = '\u0000';
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);

        String expectedMessage = "Illegal argument: Invalid lexical state";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}