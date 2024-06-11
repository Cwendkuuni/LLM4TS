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
        assertEquals("ab\tcdef", TokenMgrError.addEscapes("ab\tcdef"));
        assertEquals("ab\fndef", TokenMgrError.addEscapes("ab\fndef"));
        assertEquals("ab\rfdef", TokenMgrError.addEscapes("ab\rfdef"));
        assertEquals("ab\"cddef", TokenMgrError.addEscapes("ab\"cddef"));
        assertEquals("ab\'cddef", TokenMgrError.addEscapes("ab\'cddef"));
        assertEquals("ab\\cddef", TokenMgrError.addEscapes("ab\\cddef"));
        assertEquals("ab\u0001cddef", TokenMgrError.addEscapes("ab\u0001cddef"));
    }

    @Test
    public void testLexicalError() {
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "", ' '));
        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "", -1));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'a'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\n'));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\''));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\u0001', 1));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\u0001', 2));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\u0001', 3));
    }

    @Test
    public void testTokenMgrErrorConstructor() {
        assertEquals("", new TokenMgrError().getMessage());
        String message = "Lexical error at line 1, column 1. Encountered: <EOF> (\u0000), after : \"\"";
        TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "", -1, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());
        message = "Lexical error at line 1, column 1. Encountered: \"\" (\u0000), after : \"foo\"";
        error = new TokenMgrError(false, 0, 1, 1, "foo", ' ', TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());
        message = "Lexical error at line 1, column 1. Encountered: <EOF> (\u0000), after : \"foo\"";
        error = new TokenMgrError(false, 0, 1, 1, "foo", -1, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, error.getMessage());
    }

    @Test
    public void testTokenMgrErrorConstructorsWithArgs() {
        String errorMessage = "Lexical error at line 1, column 1. Encountered: \"\\r\" (\u000d), after : \"foo\"";
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "foo", '\r', TokenMgrError.LEXICAL_ERROR);
        assertEquals(errorMessage, error.getMessage());
        errorMessage = "Lexical error at line 2, column 2. Encountered: <EOF> (\u0000), after : \"bar\"";
        error = new TokenMgrError(true, 1, 2, 2, "bar", -1, TokenMgrError.LEXICAL_ERROR);
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    public void testTokenMgrErrorWithCustomMessage() {
        String errorMessage = "Custom error message";
        TokenMgrError error = new TokenMgrError(errorMessage, TokenMgrError.LEXICAL_ERROR);
        assertEquals(errorMessage, error.getMessage());
    }
}