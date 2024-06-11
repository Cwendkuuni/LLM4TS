import org.junit.Test;
import static org.junit.Assert.*;

import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "abcd\u0001efg\nijkl\tmnop\bqrst";
        String expected = "abcd\\u0001efg\\nijkl\\tmnop\\bqrst";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError_EOFSeen() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = '\u0001';
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
        String result = error.getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError_notEOFSeen() {
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abcd";
        char curChar = 'e';
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        String expected = "Lexical error at line 1, column 1.  Encountered: \"e\" (101), after : \"abcd\"";
        String result = error.getMessage();
        assertEquals(expected, result);
    }

    @Test(expected = TokenMgrError.class)
    public void testLexicalError_invalidState() {
        boolean eofSeen = false;
        int lexState = -1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abcd";
        char curChar = 'e';
        try {
            new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
        } catch (TokenMgrError error) {
            // expected
        }
    }

    @Test
    public void testConstructor_getMessage() {
        String message = "custom error message";
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expected = "custom error message";
        String result = error.getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testConstructor_LexicalError() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = '\u0001';
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
        String result = error.getMessage();
        assertEquals(expected, result);
    }
}