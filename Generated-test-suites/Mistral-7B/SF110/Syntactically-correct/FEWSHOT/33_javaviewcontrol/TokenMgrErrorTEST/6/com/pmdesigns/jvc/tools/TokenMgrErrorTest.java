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
        assertEquals("foo\\u0061", TokenMgrError.addEscapes("foo\u0061")); // 'a' Unicode
        assertEquals("foo\\u0101", TokenMgrError.addEscapes("foo\u0101")); // 'Á' Unicode
    }

    @Test
    public void testLexicalError() {
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a'));
        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "", '\u0000'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'b'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\u0061'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo\"bar", 'd'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo\"bar", '\u0064'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo\"bar", 'e'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo\"bar", '\u0105'));
    }

    @Test
    public void testConstructors() {
        TokenMgrError error = new TokenMgrError("Lexical error", 0);
        assertEquals("Lexical error", error.getMessage());
        assertEquals(0, error.errorCode);

        error = new TokenMgrError(true, 0, 1, 1, "", 'a', 0);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", error.getMessage());
        assertEquals(0, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "foo", 'b', 0);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"b\" (66), after : \"foo\"", error.getMessage());
        assertEquals(0, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "foo\"bar", 'd', 0);
        assertEquals("Lexical error at line 1, column 1.  Encountered: d (\u0064), after : \"foo\"bar\"", error.getMessage());
        assertEquals(0, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "foo\"bar", 'e', 0);
        assertEquals("Lexical error at line 1, column 1.  Encountered: e (\u0105), after : \"foo\"bar\"", error.getMessage());
        assertEquals(0, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "foo\"bar", 'e', 1);
        assertEquals("Lexical error at line 1, column 1.  Encountered: e (\u0105), after : \"foo\"bar\"", error.getMessage());
        assertEquals(1, error.errorCode);

        error = new TokenMgrError(false, 0, 1, 1, "foo\"bar", 'e', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: e (\u0105), after : \"foo\"bar\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }
}