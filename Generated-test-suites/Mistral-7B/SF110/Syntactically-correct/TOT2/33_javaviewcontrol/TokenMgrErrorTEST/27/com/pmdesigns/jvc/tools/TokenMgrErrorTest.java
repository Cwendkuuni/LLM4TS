import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "abc\ndef\n123\b\t\n\n\f\r\"'\\";
        String expected = "abc\\ndef\\n123\\b\\t\\n\\n\\f\\r\\\"'\\\\";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "identifier";
        char curChar = ';';
        String expectedErrorMessage = "Lexical error at line 5, column 10.  Encountered: <EOF> () after : identifier";
        String resultErrorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar).replaceAll("\\s+", "");
        assertEquals(expectedErrorMessage, resultErrorMessage);
    }

    @Test
    public void testConstructorWithoutArguments() {
        String message = "Test error message";
        TokenMgrError error = new TokenMgrError();
        assertEquals(message, error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullErrorMessage() {
        TokenMgrError error = new TokenMgrError(null, TokenMgrError.LEXICAL_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeErrorLine() {
        TokenMgrError error = new TokenMgrError(true, TokenMgrError.LEXICAL_ERROR, -1, 10, "", ';', TokenMgrError.LEXICAL_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullErrorAfter() {
        TokenMgrError error = new TokenMgrError(true, TokenMgrError.LEXICAL_ERROR, 5, 10, null, ';', TokenMgrError.LEXICAL_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonIntegerErrorLine() {
        TokenMgrError error = new TokenMgrError(true, TokenMgrError.LEXICAL_ERROR, "five", 10, "", ';', TokenMgrError.LEXICAL_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonIntegerErrorColumn() {
        TokenMgrError error = new TokenMgrError(true, TokenMgrError.LEXICAL_ERROR, 5, "ten", "", ';', TokenMgrError.LEXICAL_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCurChar() {
        TokenMgrError error = new TokenMgrError(true, TokenMgrError.LEXICAL_ERROR, 5, 10, "", null, TokenMgrError.LEXICAL_ERROR);
    }

    @Test
    public void testAddEscapesWithEmptyString() {
        String str = "";
        String expected = "";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testAddEscapesWithNullString() {
        String str = null;
        String expected = null;
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    // Add other test cases here
}