package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "\0ab\tcdef\n\r\bquotedString\"stringWithSingleQuote'stringWithBackslash\\backslash";
        String expected = "\\0\\ab\\t\\cdef\\n\\r\\b\\quotedString\\\"stringWithSingleQuote\\'stringWithBackslash\\\\backslash";

        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 123;
        int errorLine = 45;
        int errorColumn = 78;
        String errorAfter = "identifier";
        char curChar = ';';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        String expectedMessage = "Lexical error at line 45, column 78. Encountered: ; (" + (int)curChar + ") after : identifier";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testTokenMgrError() {
        boolean eofSeen = true;
        int lexState = 42;
        int errorLine = 81;
        int errorColumn = 123;
        String errorAfter = "keyWord";
        char curChar = '(';
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;

        String expectedMessage = "Lexical error at line 81, column 123. Encountered: ( after : keyWord";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);

        eofSeen = false;
        lexState = 0;
        errorLine = 10;
        errorColumn = 20;
        errorAfter = "number";
        curChar = '+';
        reason = TokenMgrError.LOOP_DETECTED;

        expectedMessage = "Lexical error at line 10, column 20. Encountered: + after : number";
        error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}