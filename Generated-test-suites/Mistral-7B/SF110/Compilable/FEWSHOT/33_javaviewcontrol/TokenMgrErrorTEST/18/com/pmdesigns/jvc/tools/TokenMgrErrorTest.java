package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals("abc\\def", TokenMgrError.addEscapes("abcdef"));
        assertEquals("ab\rcdef", TokenMgrError.addEscapes("ab\rcdef"));
        assertEquals("ab\\rdef", TokenMgrError.addEscapes("ab\\rdef"));
        assertEquals("ab\"def", TokenMgrError.addEscapes("ab\"def"));
        assertEquals("ab\'def", TokenMgrError.addEscapes("ab\'def"));
        assertEquals("ab\\def", TokenMgrError.addEscapes("ab\\def"));
        assertEquals("ab\\u0001def", TokenMgrError.addEscapes("ab\u0001def"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 0, column 0. Encountered: <EOF> (0, ) after : ''", TokenMgrError.LexicalError(true, 0, 0, 0, "", (char) 0));
        assertEquals("Lexical error at line 1, column 1. Encountered: \" (34) after : \".\", \"", TokenMgrError.LexicalError(false, 0, 1, 1, ",\".\"", '"'));
        assertEquals("Lexical error at line 1, column 2. Encountered: a after : \".\", \"", TokenMgrError.LexicalError(false, 0, 1, 2, "a", 'a'));
        assertEquals("Lexical error at line 1, column 3. Encountered: b after : \".\", \"", TokenMgrError.LexicalError(false, 0, 1, 3, "b", 'b'));
        assertEquals("Lexical error at line 2, column 1. Encountered: c after : \"abcdef\", \".\"", TokenMgrError.LexicalError(false, 0, 2, 1, "c", 'c'));
    }

    @Test
    public void testConstructor() {
        assertNull(new TokenMgrError());
        String message = "This is a message";
        int reason = 123;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);

        boolean eofSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "after";
        char curChar = 'c';
        int errorReason = 456;
        TokenMgrError error2 = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, errorReason);
        assertEquals(TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar), error2.getMessage());
        assertEquals(errorReason, error2.errorCode);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("This is an error message", 123);
        assertEquals("This is an error message", error.getMessage());
        error = new TokenMgrError(true, 0, 1, 1, "after", '\n', 456);
        assertEquals("Lexical error at line 1, column 1. Encountered: <NEWLINE> (10, ) after : after", error.getMessage());
    }
}