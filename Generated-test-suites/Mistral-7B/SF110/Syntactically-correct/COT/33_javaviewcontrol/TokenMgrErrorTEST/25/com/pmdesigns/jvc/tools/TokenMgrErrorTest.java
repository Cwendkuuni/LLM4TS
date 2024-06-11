import org.junit.Test;
import static org.junit.Assert.*;

import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes_EmptyString() {
        String result = TokenMgrError.addEscapes("");
        assertEquals("", result);
    }

    @Test
    public void testAddEscapes_NormalCharacter() {
        String result = TokenMgrError.addEscapes("a");
        assertEquals("a", result);
    }

    @Test
    public void testAddEscapes_NullCharacter() {
        String result = TokenMgrError.addEscapes(null);
        fail("addEscapes should not be called with null");
    }

    @Test
    public void testAddEscapes_ControlCharacters() {
        String result = TokenMgrError.addEscapes("\0");
        assertEquals("", result);

        result = TokenMgrError.addEscapes("\b");
        assertEquals("\\b", result);

        result = TokenMgrError.addEscapes("\t");
        assertEquals("\\t", result);

        result = TokenMgrError.addEscapes("\n");
        assertEquals("\\n", result);

        result = TokenMgrError.addEscapes("\f");
        assertEquals("\\f", result);

        result = TokenMgrError.addEscapes("\r");
        assertEquals("\\r", result);

        result = TokenMgrError.addEscapes("\'");
        assertEquals("\\'", result);

        result = TokenMgrError.addEscapes("\"");
        assertEquals("\\\"", result);

        result = TokenMgrError.addEscapes("\\");
        assertEquals("\\\\", result);
    }

    @Test
    public void testAddEscapes_OtherCharacters() {
        String result = TokenMgrError.addEscapes("z");
        assertEquals("z", result);

        result = TokenMgrError.addEscapes("~");
        assertEquals("~", result);

        result = TokenMgrError.addEscapes('&');
        assertEquals("\\&", result);

        result = TokenMgrError.addEscapes('@');
        assertEquals("\\@", result);
    }

    @Test(expected = NullPointerException.class)
    public void testLexicalError_NullErrorAfter() {
        TokenMgrError.LexicalError(false, 0, 1, 1, null, '\0');
    }

    @Test
    public void testLexicalError_ValidArguments() {
        String result = TokenMgrError.LexicalError(false, 0, 1, 1, "invalidToken", '\0');
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"invalidToken\"", result);
    }

    @Test
    public void testLexicalError_EOFSeenTrue() {
        String result = TokenMgrError.LexicalError(true, 0, 1, 1, "invalidToken", '\0');
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", result);
    }

    @Test
    public void testLexicalError_ErrorLineZero() {
        String result = TokenMgrError.LexicalError(false, 0, 0, 1, "invalidToken", '\0');
        assertEquals("Lexical error at line 0, column 1.  Encountered: <EOF>  after : \"invalidToken\"", result);
    }

    @Test
    public void testLexicalError_ErrorColumnZero() {
        String result = TokenMgrError.LexicalError(false, 0, 1, 0, "invalidToken", '\0');
        assertEquals("Lexical error at line 1, column 0.  Encountered: <EOF>  after : \"invalidToken\"", result);
    }

    @Test
    public void testTokenMgrError_NullMessage() {
        TokenMgrError error = new TokenMgrError(null, 0);
        assertNull(error.getMessage());
    }

    @Test
    public void testTokenMgrError_EmptyMessage() {
        TokenMgrError error = new TokenMgrError("", 0);
        assertEquals("", error.getMessage());
    }

    @Test
    public void testTokenMgrError_MessageAndReason() {
        TokenMgrError error = new TokenMgrError("Message", 0);
        assertEquals("Message", error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrError_EOFSeenTrue() {
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "invalidToken", '\0', 0);
        String message = TokenMgrError.LexicalError(error.isEOFSeen(), error.getLexicalState(), error.getErrorLine(), error.getErrorColumn(), error.getErrorAfter(), error.getCharPositionInLine());
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", message);
    }

    @Test
    public void testTokenMgrError_EOFSeenFalse() {
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "invalidToken", '\0', 0);
        String message = TokenMgrError.LexicalError(error.isEOFSeen(), error.getLexicalState(), error.getErrorLine(), error.getErrorColumn(), error.getErrorAfter(), error.getCharPositionInLine());
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\"\"invalidToken\"\"\" (100,) after : \"invalidToken\"", message);
    }
}