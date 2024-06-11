
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld! \t\bBackslash \\ Quotes: \"' Tab: \t";
        String expectedOutput = "Hello\\nWorld! \\t\\bBackslash \\ \\ Quotes: \\\"' Tab: \\t";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "if";
        char curChar = ';';

        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        String expectedOutput = "Lexical error at line 5, column 10. Encountered: <semicolon> (;) , after : if";
        String output = error.getMessage();

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTokenMgrError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "if";
        char curChar = ';';

        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        String expectedOutput = "Lexical error at line 5, column 10. Encountered: <semicolon> (;) , after : if";
        String output = error.getMessage();

        assertEquals(expectedOutput, output);
    }
}