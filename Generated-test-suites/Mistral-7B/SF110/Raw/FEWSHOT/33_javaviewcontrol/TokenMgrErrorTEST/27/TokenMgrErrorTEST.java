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
        assertEquals("foo", TokenMgrError.addEscapes("foo"));
        assertEquals("\\u0005", TokenMgrError.addEscapes("\u0005"));
        assertEquals("\\u007F", TokenMgrError.addEscapes("\u007F"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> () (-1), after : \"\"", TokenMgrError.LexicalError(true, 0, 1, 1, "", (char) 0));
        assertEquals("Lexical error at line 1, column 2. Encountered: \"\\b\" (92), after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 2, "foo", '\b'));
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError("Custom message", 1);
        assertNotNull(error);
        assertEquals("Custom message", error.getMessage());
        assertEquals(1, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "", (char) 0, 1);
        assertNotNull(error);
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> () (-1), after : \"\"", error.getMessage());
        assertEquals(1, error.errorCode);
    }
}