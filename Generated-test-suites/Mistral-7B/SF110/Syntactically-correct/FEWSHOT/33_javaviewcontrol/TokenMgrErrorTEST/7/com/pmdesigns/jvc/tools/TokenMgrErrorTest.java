package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals("abcdefghijklmnopqrstuvwxyz", TokenMgrError.addEscapes("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("\\u0001ABC", TokenMgrError.addEscapes("\u0001ABC"));
        assertEquals("\\u00FF", TokenMgrError.addEscapes("\u00FF"));
        assertEquals("\\u1234", TokenMgrError.addEscapes("\u1234"));
    }

    @Test
    public void testLexicalError() {
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "abc", 'a'));
        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "", 0));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "\"abc\"", 'b'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "\"abc\"", '\"'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "abc\\", 'd'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "abc\\\\", 'e'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "abc\\u0001", 'f'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "abc\\u1234", 'g'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "\u0001abc", 'h'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "\u1234abc", 'i'));
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        assertEquals("Message", new TokenMgrError("Message", 1).getMessage());
        assertEquals("Message", new TokenMgrError("Message", 1).getMessage());
    }

    @Test
    public void testConstructorWithLexicalErrorArguments() {
        String errorMessage = "Lexical error at line 1, column 1. Encountered: <EOF> \"\" (0), after : \"abc\"";
        TokenMgrError lexicalError = new TokenMgrError(false, 0, 1, 1, "abc", 0, 1);
        assertEquals(errorMessage, lexicalError.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorArguments() {
        new TokenMgrError(true, -1, 1, 1, "abc", 0, 1);
    }
}