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
        assertEquals("foo", TokenMgrError.addEscapes("foo"));
        assertEquals("abc\0def", TokenMgrError.addEscapes("abc\0def"));
        assertEquals("abc\\bdef", TokenMgrError.addEscapes("abc\\bdef"));
        assertEquals("abc\\tdef", TokenMgrError.addEscapes("abc\\tdef"));
        assertEquals("abc\\ndef", TokenMgrError.addEscapes("abc\\ndef"));
        assertEquals("abc\\fdef", TokenMgrError.addEscapes("abc\\fdef"));
        assertEquals("abc\\rdef", TokenMgrError.addEscapes("abc\\rdef"));
        assertEquals("abc\\\"def", TokenMgrError.addEscapes("abc\"def"));
        assertEquals("abc\\'def", TokenMgrError.addEscapes("abc\'def"));
        assertEquals("abc\\\\def", TokenMgrError.addEscapes("abc\\def"));
        assertEquals("abc\u0001def", TokenMgrError.addEscapes("abc\u0001def"));
        assertEquals("abc\u0010def", TokenMgrError.addEscapes("abc\u0010def"));
        assertEquals("abc\ufffddef", TokenMgrError.addEscapes("abc\ufffddef"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 0, column 0. Encountered: <EOF>  (0), after : \"\"", TokenMgrError.LexicalError(true, 0, 0, 0, "", (char)0));
        assertEquals("Lexical error at line 1, column 1. Encountered: \"\" (34), after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 1, "foo", ' '));
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\' (92), after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\''));
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\\\' (92), after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\\'));
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\u' (85), after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'u'));
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\u0001' (1), after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'u', (char)1));
    }

    @Test
    public void testTokenMgrErrorConstruction() {
        assertEquals("Lexical error at line 0, column 0. Encountered: <EOF>  (0), after : \"\"", new TokenMgrError(true, 0, 0, 0, "", (char)0, 0).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: \"\" (34), after : \"foo\"", new TokenMgrError(false, 0, 1, 1, "foo", ' ', 0).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\' (92), after : \"foo\"", new TokenMgrError(false, 0, 1, 1, "foo", '\'', 0).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\\\' (92), after : \"foo\"", new TokenMgrError(false, 0, 1, 1, "foo", '\\', 0).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\u' (85), after : \"foo\"", new TokenMgrError(false, 0, 1, 1, "foo", 'u', 0).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: '\\u0001' (1), after : \"foo\"", new TokenMgrError(false, 0, 1, 1, "foo", 'u', (char)1).getMessage());
    }
}