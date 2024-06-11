package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld!";
        String expectedOutput = "Hello\\nWorld!";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEscapesWithNullInput() {
        String input = null;
        String expectedOutput = "";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalErrorWithEOFSeenAndPrintableCharacter() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "abc";
        char curChar = 'a';
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 5, column 10. Encountered: \"a\" (97), after : \"abc\"";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testLexicalErrorWithEOFSeenAndNonPrintableCharacter() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = null;
        char curChar = '\u0001';
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 5, column 10. Encountered: <Control-A> (1, 0), after : null";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Add more test cases for other constructors and methods
}