package com.pmdesigns.jvc.tools;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Basic test case for addEscapes method
        String input = "Hello World\n\t\f\b\r\"'\\";
        String expectedOutput = "Hello World\\n\\t\\f\\b\\r\\\"'\\\\";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    // Test case for edge cases and exceptions for addEscapes method
    @Test(expected = IllegalArgumentException.class)
    public void testAddEscapesNullInput() {
        String input = null;
        TokenMgrError.addEscapes(input);
    }

    @Test
    public void testAddEscapesEmptyInput() {
        String input = "";
        String expectedOutput = "";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    // Test case for large input for addEscapes method
    @Test
    public void testAddEscapesLargeInput() {
        String input = "This is a very long string that contains all possible characters, including control characters, and Unicode characters.";
        String expectedOutput = "This is a very long string that contains all possible characters, including control characters, and Unicode characters.\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u0008\\u0009\\u000A\\u000B\\u000C\\u000D\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F\\u007F";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        // Basic test case for LexicalError method
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "identifier";
        char curChar = '=';
        String expectedOutput = "Lexical error at line 5, column 10.  Encountered: = ";
        String output = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, output);
    }

    // Test case for edge cases and exceptions for LexicalError method
    @Test(expected = IllegalArgumentException.class)
    public void testLexicalErrorNullErrorAfter() {
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        char curChar = '=';
        String errorAfter = null;
        TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
    }

    @Test
    public void testLexicalErrorInvalidLexicalState() {
        boolean eofSeen = false;
        int lexState = -1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "identifier";
        char curChar = '=';
        String expectedOutput = "Lexical error at line 5, column 10.  Encountered: = ";
        String output = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertNotEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalErrorEOF() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "";
        char curChar = (char) 0;
        String expectedOutput = "<EOF> ";
        String output = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, output);
    }

}