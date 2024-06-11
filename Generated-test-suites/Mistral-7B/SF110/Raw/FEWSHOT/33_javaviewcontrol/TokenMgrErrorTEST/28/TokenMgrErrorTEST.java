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
        assertEquals("foo\\", TokenMgrError.addEscapes("foo"));
        assertEquals("foo\u0000bar", TokenMgrError.addEscapes("foo\u0000bar"));
        assertEquals("foo\u0007bar", TokenMgrError.addEscapes("foo\u0007bar"));
    }

    @Test
    public void testLexicalError() {
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "", ' '));
        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "", (char) 0));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'a'));
        assertNotNull(TokenMgrError.LexicalError(true, 0, 1, 1, "foo", (char) 0));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'a', 0));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'a', 1));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'a', 2));
        assertNotNull(TokenMgrError.LexicalError(false, 0, 1, 1, "foo", 'a', 3));
    }

    @Test
    public void testConstructor() {
        assertNotNull(new TokenMgrError(""));
        assertNotNull(new TokenMgrError("Message", 0));
        assertNotNull(new TokenMgrError(false, 0, 1, 1, "", ' ', 0));
        assertNotNull(new TokenMgrError(false, 0, 1, 1, "foo", 'a', 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new TokenMgrError(null, 0);
    }
}