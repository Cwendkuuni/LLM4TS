import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "HelloWorld\nHello\tbWorld\fHello\rHello\"Hello'\bHello\\Hello\u0001";
        String expectedOutput = "HelloWorld\\nHello\\tWorld\\fHello\\rHello\\\"Hello\\'\\bHello\\\\Hello\\u0001";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalErrorConstructor() {
        String errorMessage = TokenMgrError.LexicalError(true, 0, 5, 10, "if", ';');
        String expectedOutput = "Lexical error at line 5, column 10. Encountered: <EOF> (0), after : \"if\"";
        assertEquals(expectedOutput, errorMessage);
    }

    @Test
    public void testCustomErrorMessage() {
        TokenMgrError error = new TokenMgrError("Custom error message", 1);
        String expectedOutput = "Custom error message";
        String output = error.getMessage();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testErrorCodes() {
        TokenMgrError error1 = new TokenMgrError(true, 0, 5, 10, "if", ';', TokenMgrError.LEXICAL_ERROR);
        TokenMgrError error2 = new TokenMgrError(true, 0, 5, 10, "if", ';', TokenMgrError.STATIC_LEXER_ERROR);
        TokenMgrError error3 = new TokenMgrError(true, 0, 5, 10, "if", ';', TokenMgrError.INVALID_LEXICAL_STATE);

        int expectedErrorCode1 = TokenMgrError.LEXICAL_ERROR;
        int expectedErrorCode2 = TokenMgrError.STATIC_LEXER_ERROR;
        int expectedErrorCode3 = TokenMgrError.INVALID_LEXICAL_STATE;

        int actualErrorCode1 = error1.errorCode;
        int actualErrorCode2 = error2.errorCode;
        int actualErrorCode3 = error3.errorCode;

        assertEquals(expectedErrorCode1, actualErrorCode1);
        assertEquals(expectedErrorCode2, actualErrorCode2);
        assertEquals(expectedErrorCode3, actualErrorCode3);
    }

    @Test
    public void testToString() {
        TokenMgrError error = new TokenMgrError("Custom error message", 1);
        String expectedOutput = "Custom error message";
        String output = error.toString();
        assertEquals(expectedOutput, output);
    }
}