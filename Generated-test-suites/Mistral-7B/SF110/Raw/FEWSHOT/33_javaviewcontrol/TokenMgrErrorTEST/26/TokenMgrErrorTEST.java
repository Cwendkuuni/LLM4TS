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
        assertEquals("foo\nbar", TokenMgrError.addEscapes("foo\nbar"));
        assertEquals("foo\b\tbaz", TokenMgrError.addEscapes("foo\b\tbaz"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (0, 0), after : \"\"", TokenMgrError.LexicalError(true, 0, 1, 1, "", 0, 0));
        assertEquals("Lexical error at line 1, column 2. Encountered: \"a\" (\u0061, 97), after : \"b\"", TokenMgrError.LexicalError(false, 0, 1, 2, "b", 'a', 0));
        assertEquals("Lexical error at line 2, column 3. Encountered: \t (9, 11), after : \nfoo", TokenMgrError.LexicalError(false, 0, 2, 3, "\nfoo", '\t', 0));
        assertEquals("Lexical error at line 3, column 5. Encountered: <EOF> (0, 1), after : \u0007bar", TokenMgrError.LexicalError(true, 0, 3, 5, "\u0007bar", (char)7, 0));
    }

    @Test
    public void testTokenMgrErrorConstruction() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (0, 0), after : \"\"", new TokenMgrError(true, 0, 1, 1, "", 0, 0).getMessage());
        assertEquals("Lexical error at line 1, column 2. Encountered: \"a\" (\u0061, 97), after : \"b\"", new TokenMgrError(false, 0, 1, 2, "b", 'a', 0).getMessage());
        assertEquals("Lexical error at line 2, column 3. Encountered: \t (9, 11), after : \nfoo", new TokenMgrError(false, 0, 2, 3, "\nfoo", '\t', 0).getMessage());
        assertEquals("Lexical error at line 3, column 5. Encountered: <EOF> (0, 1), after : \u0007bar", new TokenMgrError(true, 0, 3, 5, "\u0007bar", (char)7, 0).getMessage());
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        assertEquals("Lexical error at line 1, column 1. Reason: 0", new TokenMgrError("", 0).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (0, 0), after : \"\" Reason: 1", new TokenMgrError(true, 0, 1, 1, "", 0, 1).getMessage());
        assertEquals("Lexical error at line 1, column 2. Encountered: \"a\" (\u0061, 97), after : \"b\" Reason: 2", new TokenMgrError(false, 0, 1, 2, "b", 'a', 2).getMessage());
        assertEquals("Lexical error at line 2, column 3. Encountered: \t (9, 11), after : \nfoo Reason: 3", new TokenMgrError(false, 0, 2, 3, "\nfoo", '\t', 3).getMessage());
        assertEquals("Lexical error at line 3, column 5. Encountered: <EOF> (0, 1), after : \u0007bar Reason: 4", new TokenMgrError(true, 0, 3, 5, "\u0007bar", (char)7, 4).getMessage());
    }

    @Test
    public void testgetMessage() {
        TokenMgrError error = new TokenMgrError("Custom error message", 5);
        assertEquals("Custom error message", error.getMessage());
    }
}