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
        assertEquals("bar\nabcd", TokenMgrError.addEscapes("bar\nabcd"));
        assertEquals("0x12", TokenMgrError.addEscapes("\u0012"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (\u0000)", TokenMgrError.LexicalError(true, 0, 1, 1, "", (char) 0));
        assertEquals("Lexical error at line 1, column 2. Encountered: \"foo\" (\u0066)", TokenMgrError.LexicalError(false, 0, 1, 2, "foo", 'f'));
        assertEquals("Lexical error at line 1, column 3. Encountered: \"bar\" (\u0062)", TokenMgrError.LexicalError(false, 0, 1, 3, "bar", 'b'));
        assertEquals("Lexical error at line 2, column 1. Encountered: <EOF> (\u0000)", TokenMgrError.LexicalError(true, 0, 2, 1, "", (char) 0));
        assertEquals("Lexical error at line 2, column 2. Encountered: \"foo\" (\u0066) after : \"bar\" (\u0062)", TokenMgrError.LexicalError(false, 0, 2, 2, "barfoo", 'f'));
    }

    @Test
    public void testTokenMgrError() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (\u0000)", new TokenMgrError(true, 0, 1, 1, "", (char) 0, 0).getMessage());
        assertEquals("Lexical error at line 1, column 2. Encountered: \"foo\" (\u0066)", new TokenMgrError(false, 0, 1, 2, "foo", 'f', 0).getMessage());
        assertEquals("Lexical error at line 1, column 3. Encountered: \"bar\" (\u0062) after : \"foo\" (\u0066)", new TokenMgrError(false, 0, 1, 3, "foobar", 'b', 0).getMessage());
        assertEquals("Lexical error at line 2, column 1. Encountered: <EOF> (\u0000)", new TokenMgrError(true, 0, 2, 1, "", (char) 0, 0).getMessage());
        assertEquals("Lexical error at line 2, column 2. Encountered: \"foo\" (\u0066) after : \"bar\" (\u0062)", new TokenMgrError(false, 0, 2, 2, "barfoo", 'f', 0).getMessage());
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> (\u0000)", new TokenMgrError(true, 0, 1, 1, "", (char) 0, TokenMgrError.LEXICAL_ERROR).getMessage());
        assertEquals("Lexical error at line 1, column 2. Encountered: \"foo\" (\u0066)", new TokenMgrError(false, 0, 1, 2, "foo", 'f', TokenMgrError.LEXICAL_ERROR).getMessage());
        assertEquals("Lexical error at line 1, column 3. Encountered: \"bar\" (\u0062) after : \"foo\" (\u0066)", new TokenMgrError(false, 0, 1, 3, "foobar", 'b', TokenMgrError.LEXICAL_ERROR).getMessage());
        assertEquals("Lexical error at line 2, column 1. Encountered: <EOF> (\u0000)", new TokenMgrError(true, 0, 2, 1, "", (char) 0, TokenMgrError.STATIC_LEXER_ERROR).getMessage());
        assertEquals("Lexical error at line 2, column 2. Encountered: \"foo\" (\u0066) after : \"bar\" (\u0062)", new TokenMgrError(false, 0, 2, 2, "barfoo", 'f', TokenMgrError.INVALID_LEXICAL_STATE).getMessage());
        assertEquals("Lexical error at line 2, column 2. Encountered: \"foo\" (\u0066) after : \"bar\" (\u0062)", new TokenMgrError(false, 0, 2, 2, "barfoo", 'f', TokenMgrError.LOOP_DETECTED).getMessage());
    }
}