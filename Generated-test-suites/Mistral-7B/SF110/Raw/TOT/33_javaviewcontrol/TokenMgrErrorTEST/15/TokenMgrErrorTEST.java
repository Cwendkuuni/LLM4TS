
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "\0\b\t\n\f\r\"'\\";
        String expected = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
        
        String inputWithNormalChars = "Hello World!";
        String expectedWithNormalChars = "Hello World!";
        String resultWithNormalChars = TokenMgrError.addEscapes(inputWithNormalChars);
        assertEquals(expectedWithNormalChars, resultWithNormalChars);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 10;
        String errorAfter = "identifier";
        char curChar = '=';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        String expected = "Lexical error at line 1, column 10. Encountered: = ";
        String result = error.getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testCustomError() {
        String message = "Invalid input: Unknown token 'abc'";
        int reason = 42;

        TokenMgrError error = new TokenMgrError(message, reason);
        
        String expected = "Invalid input: Unknown token 'abc'";
        String result = error.getMessage();
        assertEquals(expected, result);
    }
}