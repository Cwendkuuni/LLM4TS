import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!\u0021";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\0";
        expectedOutput = "\\0";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\b";
        expectedOutput = "\\b";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\t";
        expectedOutput = "\\t";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\n";
        expectedOutput = "\\n";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\f";
        expectedOutput = "\\f";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\r";
        expectedOutput = "\\r";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\"";
        expectedOutput = "\\\"";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\'";
        expectedOutput = "\\'";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "\\";
        expectedOutput = "\\\\";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "abcd";
        expectedOutput = "abcd";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "efgh\u0061";
        expectedOutput = "efgh\u0061";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "efgh\u0062";
        expectedOutput = "efgh\u0062";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);

        input = "efgh\u1234";
        expectedOutput = "efgh\u1234";
        output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEscapesWithNullString() {
        String input = null;
        TokenMgrError.addEscapes(input);
    }

    @Test(expected = NullPointerException.class)
    public void testAddEscapesWithNullCharArray() {
        char[] input = null;
        TokenMgrError.addEscapes(input);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "errorAfter";
        char curChar = 'a';
        int reason = TokenMgrError.LEXICAL_ERROR;

        String expectedOutput = "Lexical error at line 1, column 2. Encountered: \"a\" (97), after : \"errorAfter\"";
        String output = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedOutput, output);

        eofSeen = true;
        reason = TokenMgrError.EOF_ERROR;
        expectedOutput = "Lexical error at line 1, column 0. Encountered: <EOF> ";
        output = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLexicalErrorWithNullErrorMessage() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = null;
        char curChar = 'a';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
    }

    @Test(expected = NullPointerException.class)
    public void testLexicalErrorWithNullErrorMessageCharArray() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        char[] errorAfter = null;
        char curChar = 'a';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
    }
}