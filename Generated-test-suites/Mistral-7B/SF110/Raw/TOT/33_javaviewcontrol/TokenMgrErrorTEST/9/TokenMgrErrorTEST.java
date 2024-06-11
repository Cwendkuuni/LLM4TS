import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello World! \b\t\n\f\r\"'\\\nHello Again";
        String expected = "Hello World! \\b\\t\\n\\f\\r\\\"'\\\\nHello Again";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testLexicalError() {
        String errorMessage = TokenMgrError.LexicalError(false, 0, 5, 10, "if", ';');
        String expected = "Lexical error at line 5, column 10. Encountered: ; after : if";
        String output = new TokenMgrError(false, 0, 5, 10, "if", ';', 0).getMessage();
        assertEquals(expected, output);
    }

    @Test
    public void testCustomErrorMessage() {
        String errorMessage = new TokenMgrError("Custom Error Message", 1).getMessage();
        String expected = "Custom Error Message";
        assertEquals(expected, errorMessage);
    }

    @Test(expected = TokenMgrError.class)
    public void testExceptionThrown() {
        new TokenMgrError(true, 0, 5, 10, "", 'A', 2);
    }

    @Test
    public void testGetErrorCode() {
        TokenMgrError error = new TokenMgrError(true, 0, 5, 10, "", 'A', 2);
        int expected = 2;
        int actual = error.getErrorCode();
        assertEquals(expected, actual);
    }
}