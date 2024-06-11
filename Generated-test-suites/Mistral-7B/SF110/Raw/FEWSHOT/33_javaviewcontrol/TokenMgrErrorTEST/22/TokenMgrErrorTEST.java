package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("\\0", TokenMgrError.addEscapes("\u0000"));
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("abc", TokenMgrError.addEscapes("abc"));
        assertEquals("ab\uc2a0cd", TokenMgrError.addEscapes("ab\u200ccd"));
        assertEquals("ab\\uc2a0cd", TokenMgrError.addEscapes("ab\\u200ccd"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (\u0000)", TokenMgrError.LexicalError(true, 0, 1, 1, "", (char) 0));
        assertEquals("Lexical error at line 1, column 2. Encountered: \"foo\" (\u0066\u006f\u006f)", TokenMgrError.LexicalError(false, 0, 1, 2, "foo", 'f'));
        assertEquals("Lexical error at line 1, column 2. Encountered: \"foo\" (\u0066\u006f\u006f)", TokenMgrError.LexicalError(false, 0, 1, 2, "foo", (char) 0x66));
        assertEquals("Lexical error at line 1, column 3. Encountered: \u0001 (\u0001)", TokenMgrError.LexicalError(false, 0, 1, 3, "", (char) 1));
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
        assertEquals(1, error.errorCode);

        error = new TokenMgrError(true, 0, 1, 1, "", 'e', 1);
        assertEquals("Lexical error at line 1, column 1. Encountered: e (\u0065)", error.getMessage());
        assertEquals(1, error.errorCode);

        error = new TokenMgrError(true, 0, 1, 1, "foo", 'e', 1);
        assertEquals("Lexical error at line 1, column 1. Encountered: e (\u0065) after : foo", error.getMessage());
        assertEquals(1, error.errorCode);
    }
}