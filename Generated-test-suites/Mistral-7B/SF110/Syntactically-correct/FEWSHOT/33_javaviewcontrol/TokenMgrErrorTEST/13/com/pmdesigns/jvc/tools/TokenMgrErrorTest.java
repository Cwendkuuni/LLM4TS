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
        assertEquals("foo\\u0061foo", TokenMgrError.addEscapes("foo\u0061foo"));
        assertEquals("foo\\u0066foo", TokenMgrError.addEscapes("foo\u0066foo"));
    }

    @Test
    public void testConstructors() {
        assertNotNull(new TokenMgrError());
        assertNotNull(new TokenMgrError("Message", 0));
        assertNotNull(new TokenMgrError(true, 0, 1, 1, "", 'a', 0));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> () \"\", after : \"\"", TokenMgrError.LexicalError(true, 0, 1, 1, "", (char)0));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\"\" (\", after : \"foo\"", TokenMgrError.LexicalError(false, 0, 1, 1, "foo", '\"'));
    }

    @Test
    public void testgetMessage() {
        TokenMgrError error = new TokenMgrError("Message", 0);
        assertEquals("Message", error.getMessage());
    }
}