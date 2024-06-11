package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        // Test case proposed by Expert 1
        String input = "Hello,\\world!";
        String expectedOutput = "Hello,\\world!";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        // ... other test cases for addEscapes ...
    }

    @Test
    public void testLexicalError() {
        // Test case proposed by Expert 2
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "errorAfter";
        char curChar = ';';
        int reason = TokenMgrError.LEXICAL_ERROR;

        String expectedOutput = "Lexical error at line 1, column 1. Encountered: <EOF> (0), after : errorAfter";
        String output = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason).getMessage();
        assertEquals(expectedOutput, output);

        // ... other test cases for LexicalError ...
    }
}