import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "\'abc\t123\ndef\r\fgh\b\0ijkl\"m\nOPQ\rST\fUVW\n";
        String expectedOutput = "\\\'abc\\t123\\ndef\\r\\fgh\\b\\0ijkl\\\"m\\nOPQ\\rST\\fUVW\\n";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "if";
        char curChar = '(';
        String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: ( after : if";
        String errorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar).replaceAll("\\s", " ");
        assertEquals(expectedErrorMessage, errorMessage);
    }

    // Add test cases for other methods here.
}