package com.pmdesigns.jvc.tools.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello World! \t\n\r\f\b\0\"'\\";
        String expected = "Hello World! \\t\\n\\r\\f\\b\\0\\\"'\\\\";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError() {
        String errorMessage = TokenMgrError.LexicalError(true, 0, 1, 1, "if", ';');
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : if";
        String result = new TokenMgrError(true, 0, 1, 1, "if", ';', TokenMgrError.LEXICAL_ERROR).getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testCustomError() {
        String errorMessage = TokenMgrError.LexicalError(false, 1, 2, 3, "Identifier: myIdentifier", '=');
        String expected = "Lexical error at line 2, column 3.  Encountered: = after : Identifier: myIdentifier";
        String result = new TokenMgrError(false, 1, 2, 3, "Identifier: myIdentifier", '=', TokenMgrError.LEXICAL_ERROR).getMessage();
        assertEquals(expected, result);
    }
}