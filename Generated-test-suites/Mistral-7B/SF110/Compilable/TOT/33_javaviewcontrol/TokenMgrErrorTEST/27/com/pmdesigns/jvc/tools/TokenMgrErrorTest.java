package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld!";
        String expectedOutput = "Hello\\nWorld!";

        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTokenMgrErrorConstruction() {
        String errorAfter = "invalid_token";
        char curChar = '+';
        TokenMgrError error = new TokenMgrError(true, 1, 5, 10, errorAfter, curChar, TokenMgrError.INVALID_LEXICAL_STATE);
        String expectedMessage = "Lexical error at line 5, column 10. Encountered: <EOF> (\u0003), after : \"invalid_token\"";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testTokenMgrErrorStringConstructor() {
        String expectedMessage = "Unexpected error occurred!";
        TokenMgrError error = new TokenMgrError(expectedMessage, TokenMgrError.LEXICAL_ERROR);
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}