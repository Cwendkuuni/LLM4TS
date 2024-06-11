package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("\\0", TokenMgrError.addEscapes("\0"));
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("foo", TokenMgrError.addEscapes("foo"));
        assertEquals("foo\\nbar", TokenMgrError.addEscapes("foo\nbar"));
        assertEquals("foo\\bbar", TokenMgrError.addEscapes("foo\bbar"));
        assertEquals("foo\\fbaz", TokenMgrError.addEscapes("foo\fbaz"));
        assertEquals("foo\\rbaz", TokenMgrError.addEscapes("foo\rbaz"));
        assertEquals("foo\\bbaz", TokenMgrError.addEscapes("foo\bbaz"));
        assertEquals("foo\\\"bar", TokenMgrError.addEscapes("foo\"bar"));
        assertEquals("foo\\'bar", TokenMgrError.addEscapes("foo\'bar"));
        assertEquals("foo\\\\bar", TokenMgrError.addEscapes("foo\\bar"));
        assertEquals("foo\\u1234", TokenMgrError.addEscapes("foo\u1234"));
    }

    @Test
    public void testLexicalError() {
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a', 0));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a', 0).getMessage());

        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "", '}', 0));
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", TokenMgrError.LexicalError(true, 0, 1, 1, "", '}', 0).getMessage());
    }

    @Test
    public void testTokenMgrErrorConstruction() {
        TokenMgrError error = new TokenMgrError("Test Error", 1);
        assertEquals("Test Error", error.getMessage());
        assertEquals(1, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "test", 'a', 1);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"test\"", error.getMessage());
        assertEquals(1, error.errorCode);

        error = new TokenMgrError(true, 0, 1, 1, "test", '}', 1);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", error.getMessage());
        assertEquals(1, error.errorCode);
    }
}