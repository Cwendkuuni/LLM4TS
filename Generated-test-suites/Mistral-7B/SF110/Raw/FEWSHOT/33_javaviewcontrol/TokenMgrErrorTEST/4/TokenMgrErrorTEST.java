package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals("foo\\b\tbar", TokenMgrError.addEscapes("foo\b\tbar"));
        assertEquals("foo\"bar\\nbaz", TokenMgrError.addEscapes("foo\"bar\nbaz"));
        assertEquals("foo\\u000Fbar", TokenMgrError.addEscapes("foo\\u000Fbar"));
        assertEquals("foo\\u007Fbar", TokenMgrError.addEscapes("foo\\u007Fbar"));
    }

    @Test
    public void testLexicalError() {
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a'));
        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "", (char) 0));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", ')'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\\'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\u0001'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\u007F'));
    }

    @Test
    public void testConstructor() {
        String message = "Lexical error at line 1, column 1. Encountered: <EOF> () after : \"foo\"";
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "foo", (char) 0, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());

        message = "Lexical error at line 1, column 1. Encountered: \"<EOF> \" () after : \"foo\"";
        error = new TokenMgrError(false, 0, 1, 1, "foo", (char) -1, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());

        message = "Lexical error at line 1, column 1. Encountered: \"\\r\" (13) after : \"foo\"";
        error = new TokenMgrError(false, 0, 1, 1, "foo", '\r', TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());

        message = "Lexical error at line 1, column 1. Encountered: \"\\u0007\" (7) after : \"foo\"";
        error = new TokenMgrError(false, 0, 1, 1, "foo", '\u0007', TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());
    }
}