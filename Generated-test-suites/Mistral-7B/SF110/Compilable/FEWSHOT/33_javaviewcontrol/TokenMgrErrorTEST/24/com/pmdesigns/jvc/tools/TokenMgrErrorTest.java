package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

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
        assertEquals("\u0005", TokenMgrError.addEscapes("\u0005"));
        assertEquals("\u001a", TokenMgrError.addEscapes("\u001a"));
        assertEquals("\u007f", TokenMgrError.addEscapes("\u007f"));
        assertEquals("abc", TokenMgrError.addEscapes("abc"));
        assertEquals("ab\tc", TokenMgrError.addEscapes("ab\tc"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 0, column 0. Encountered: <EOF> (0), after : \"\"",
                TokenMgrError.LexicalError(true, 0, 0, 0, "", (char)0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \"\\\" (22), after : \"\"",
                TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)34));
        assertEquals("Lexical error at line 1, column 1. Encountered: \n (10), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)10));
        assertEquals("Lexical error at line 1, column 1. Encountered: \r (13), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)13));
        assertEquals("Lexical error at line 1, column 1. Encountered: \f (12), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)12));
        assertEquals("Lexical error at line 1, column 1. Encountered: \b (8), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)8));
        assertEquals("Lexical error at line 1, column 1. Encountered: \t (9), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)9));
        assertEquals("Lexical error at line 1, column 1. Encountered: \\ (92), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)92));
        assertEquals("Lexical error at line 1, column 1. Encountered: a (97), after : \"abc\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "abc", (char)97));
        assertEquals("Lexical error at line 1, column 2. Encountered: b (98), after : ab\tc",
                TokenMgrError.LexicalError(false, 0, 1, 2, "ab\tc", (char)98));
    }

    @Test
    public void testConstructor_getMessage() {
        String errorMessage = "Expected error message";
        TokenMgrError tokenMgrError = new TokenMgrError(errorMessage, TokenMgrError.LEXICAL_ERROR);
        assertEquals(errorMessage, tokenMgrError.getMessage());
    }

    @Test
    public void testConstructor_EFOSeen_lexState_errorLine_errorColumn_errorAfter_curChar_reason() {
        int errorLine = 1;
        int errorColumn = 2;
        char curChar = 'a';
        String errorAfter = "abc";
        TokenMgrError tokenMgrError = new TokenMgrError(true, 0, errorLine, errorColumn, errorAfter, curChar,
                TokenMgrError.LEXICAL_ERROR);
        String expectedErrorMessage = String.format(
                "Lexical error at line %d, column %d. Encountered: %s (%d), after : \"%s\"", errorLine,
                errorColumn, curChar, (int)curChar, errorAfter);
        assertEquals(expectedErrorMessage, tokenMgrError.getMessage());
    }
}