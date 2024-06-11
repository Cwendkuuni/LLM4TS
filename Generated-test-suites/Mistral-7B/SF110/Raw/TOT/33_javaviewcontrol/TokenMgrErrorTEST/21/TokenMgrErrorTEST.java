package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "Hello World!\n\tBackslash Test: \0\b\f\r\"'\\";
        String expected = "Hello World!\\n\\tBackslash Test: \\b\\f\\r\\\"\\'\\\\";
        String actual = TokenMgrError.addEscapes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testLexicalErrorConstruction() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "if";
        char curChar = '(';
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        String expected = "Lexical error at line 1, column 5. Encountered: ( after : if";
        String actual = error.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void testLexicalErrorConstructionWithReason() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 6;
        String errorAfter = "else";
        char curChar = ';';
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 2, column 6. Encountered: ; after : else";
        String actual = error.getMessage();
        assertEquals(expected, actual);
    }
}