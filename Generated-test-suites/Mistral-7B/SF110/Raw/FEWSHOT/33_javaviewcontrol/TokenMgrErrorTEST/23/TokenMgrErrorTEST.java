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
        assertEquals("foo\\", TokenMgrError.addEscapes("foo\\"));
        assertEquals("foo\\\\", TokenMgrError.addEscapes("foo\\\\"));
        assertEquals("foo\\\"", TokenMgrError.addEscapes("foo\\\""));
        assertEquals("foo\\' ", TokenMgrError.addEscapes("foo\\' "));
        assertEquals("foo\\n", TokenMgrError.addEscapes("foo\n"));
        assertEquals("foo\\r", TokenMgrError.addEscapes("foo\r"));
        assertEquals("foo\\f", TokenMgrError.addEscapes("foo\f"));
        assertEquals("foo\\b", TokenMgrError.addEscapes("foo\b"));
        assertEquals("foo\\t", TokenMgrError.addEscapes("foo\t"));
        assertEquals("foo\\u000A", TokenMgrError.addEscapes("foo\u000A"));
        assertEquals("foo\\u000B", TokenMgrError.addEscapes("foo\u000B"));
        assertEquals("foo\\u000C", TokenMgrError.addEscapes("foo\u000C"));
        assertEquals("foo\\u000D", TokenMgrError.addEscapes("foo\u000D"));
        assertEquals("foo\\u000E", TokenMgrError.addEscapes("foo\u000E"));
        assertEquals("foo\\u000F", TokenMgrError.addEscapes("foo\u000F"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 0, column 0. Encountered: <EOF> (0)", TokenMgrError.LexicalError(true, 0, 0, 0, "", (char)0, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \"\" (0)", TokenMgrError.LexicalError(false, 0, 0, 0, "\"", (char)0, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \'\\\' (92)", TokenMgrError.LexicalError(false, 0, 0, 0, "", '\'', 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \\ (92)", TokenMgrError.LexicalError(false, 0, 0, 0, "", '\\', 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \b (8)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)8, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \t (9)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)9, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \n (10)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)10, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \f (12)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)12, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \r (13)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)13, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \u000A (10)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)10, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \u000B (8)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)8, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \u000C (3)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)3, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \u000D (13)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)13, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \u000E (10)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)10, 0));
        assertEquals("Lexical error at line 0, column 0. Encountered: \u000F (12)", TokenMgrError.LexicalError(false, 0, 0, 0, "", (char)12, 0));
        assertEquals("Lexical error at line 1, column 1. Encountered: \u001A (26)", TokenMgrError.LexicalError(false, 0, 1, 1, "", (char)26, 0));
        assertNull(TokenMgrError.LexicalError(false, 0, 0, 0, "", '\u0000', 0));
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        assertEquals("TokenMgrError", new TokenMgrError("TokenMgrError", 0).getClass().getSimpleName());
        assertEquals("Message [TokenMgrError]", new TokenMgrError("Message [TokenMgrError]", 0).getMessage());
        assertEquals(0, new TokenMgrError("Message [TokenMgrError]", 0).getErrorCode());
    }

    @Test
    public void testConstructorsWithLexicalErrorParameters() {
        assertEquals("Lexical error at line 0, column 0. Encountered: <EOF> (0)", new TokenMgrError(true, 0, 0, 0, "", (char)0, 0).getMessage());
        assertEquals("Lexical error at line 0, column 0. Encountered: \"\" (0)", new TokenMgrError(false, 0, 0, 0, "\"", (char)0, 0).getMessage());
        assertEquals("Lexical error at line 0, column 1. Encountered: \"\\a\" (1)", new TokenMgrError(false, 0, 0, 1, "\"\\a\"", (char)7, 1).getMessage());
        assertEquals("Lexical error at line 1, column 1. Encountered: \u001A (26)", new TokenMgrError(false, 0, 1, 1, "", (char)26, 0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorParameters() {
        new TokenMgrError(false, 0, 0, 0, "", (char)0, -1);
    }

    @Test
    public void testGetErrorCode() {
        assertEquals(0, new TokenMgrError(true, 0, 0, 0, "", (char)0, 0).getErrorCode());
        assertEquals(0, new TokenMgrError(false, 0, 0, 0, "\"", (char)0, 0).getErrorCode());
        assertEquals(1, new TokenMgrError(false, 0, 0, 1, "\"\\a\"", (char)7, 1).getErrorCode());
        assertEquals(2, new TokenMgrError(false, 0, 0, 0, "", (char)0, 2).getErrorCode());
        assertEquals(3, new TokenMgrError(false, 0, 0, 0, "", (char)0, 3).getErrorCode());
    }
}