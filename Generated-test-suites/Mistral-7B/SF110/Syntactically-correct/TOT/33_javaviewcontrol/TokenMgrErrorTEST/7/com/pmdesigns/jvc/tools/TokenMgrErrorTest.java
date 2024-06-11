package com.pmdesigns.jvc.tools.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello World! \t\n\fBackslash \bQuotes: \"'\\\n\r";
        String expected = "Hello World! \\t\\n\\fBackslash \\bQuotes: \\\"'\\\\";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testLexicalError() {
        String errorMessage = TokenMgrError.LexicalError(false, 0, 5, 10, "if", '(');
        String expected = "Lexical error at line 5, column 10. Encountered: ( after : if";
        assertEquals(expected, errorMessage);
    }

    @Test
    public void testConstructors() {
        TokenMgrError error1 = new TokenMgrError("Message 1", 1);
        TokenMgrError error2 = new TokenMgrError(true, 1, 5, 10, "if", '(', 1);

        String errorMessage1 = error1.getMessage();
        String errorMessage2 = error2.getMessage();

        String expectedMessage1 = "Message 1";
        String expectedMessage2 = "Lexical error at line 5, column 10. Encountered: ( after : if";

        assertEquals(expectedMessage1, errorMessage1);
        assertEquals(expectedMessage2, errorMessage2);

        int errorCode1 = error1.getErrorCode();
        int errorCode2 = error2.getErrorCode();

        int expectedErrorCode1 = 1;
        int expectedErrorCode2 = 0;

        assertEquals(expectedErrorCode1, errorCode1);
        assertEquals(expectedErrorCode2, errorCode2);
    }
}