package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld!";
        String expectedOutput = "Hello\\nWorld!";

        String result = TokenMgrError.addEscapes(input);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "identifier";
        char curChar = ';';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        String expectedOutput = "Lexical error at line 1, column 5. Encountered: ;  after : identifier";
        String actualOutput = error.getMessage();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConstructors() {
        // Test empty constructor
        TokenMgrError error1 = new TokenMgrError();
        assertEquals("", error1.getMessage());

        // Test constructor with message and reason
        String message = "This is a test error";
        int reason = 42;
        TokenMgrError error2 = new TokenMgrError(message, reason);
        assertEquals(message, error2.getMessage());
        assertEquals(reason, error2.errorCode);
    }

}