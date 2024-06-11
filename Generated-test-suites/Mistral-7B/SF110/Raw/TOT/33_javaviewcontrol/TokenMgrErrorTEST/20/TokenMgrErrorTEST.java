
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test common characters
        assertEquals("abcd", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("abcd"));
        assertEquals("ab\tcde", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("ab\tcde"));
        assertEquals("ab\fn", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("ab\fn"));
        assertEquals("ab\r", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("ab\r"));

        // Test special characters
        assertEquals("\"abcd\"", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("\"abcd\""));
        assertEquals("\'abcd\'", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("\'abcd\'"));
        assertEquals("\\abcd", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("\\abcd"));

        // Test edge cases
        assertEquals("\\u0000", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes(""));
        assertEquals("\\u0001abcd", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("\u0001abcd"));
        assertEquals("ab\u0001cd", com.pmdesigns.jvc.tools.TokenMgrError.addEscapes("ab\u0001cd"));
    }

    @Test
    public void testLexicalError() {
        // Test with EOFSeen = false and different error states
        assertEquals("Lexical error at line 1, column 1. Encountered: \"  (32), after : \"\n\"",
                com.pmdesigns.jvc.tools.TokenMgrError.LexicalError(false, 0, 1, 1, "", ' ', 0));

        assertEquals("Lexical error at line 1, column 1. Encountered: \"<EOF> \" after : \"abcd\"",
                com.pmdesigns.jvc.tools.TokenMgrError.LexicalError(true, 0, 1, 1, "abcd", (char)0, 0));

        // Test with EOFSeen = false and different lexical states
        assertEquals("Lexical error at line 1, column 1. Encountered: \"\\r\" (13), after : \"abcd\"",
                com.pmdesigns.jvc.tools.TokenMgrError.LexicalError(false, 1, 1, 1, "abcd", '\r', 0));

        // Test with EOFSeen = false and different error reasons
        assertEquals("Lexical error at line 1, column 1. Encountered: \"\\r\" (13), after : \"abcd\"",
                com.pmdesigns.jvc.tools.TokenMgrError.LexicalError(false, 0, 1, 1, "abcd", '\r', 1));
    }

    @Test
    public void testTokenMgrErrorConstructors() {
        // Test default constructor
        TokenMgrError error1 = new TokenMgrError();
        assertEquals("", error1.getMessage());
        assertEquals(0, error1.errorCode);

        // Test constructor with message and reason
        TokenMgrError error2 = new TokenMgrError("Test error message", 1);
        assertEquals("Test error message", error2.getMessage());
        assertEquals(1, error2.errorCode);

        // Test constructor with lexical error parameters
        TokenMgrError error3 = new TokenMgrError(true, 0, 1, 1, "abcd", ' ', 0, 1);
        assertEquals("Lexical error at line 1, column 1. Encountered: \"  (32), after : \"abcd\"", error3.getMessage());
        assertEquals(1, error3.errorCode);
    }
}