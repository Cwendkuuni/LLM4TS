
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "abcdef\n\r\t\b\f\0\"'\\";
        String expectedOutput = "abcdef\\n\\r\\t\\b\\f\\0\\\"'\\\\";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEscapesNullInput() {
        String input = null;
        String expectedOutput = null;
        String output = TokenMgrError.addEscapes(input);
        assertNull(output);
    }

    @Test
    public void testAddEscapesEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesStringWithSpecialCharactersOutsideAsciiRange() {
        String input = "\u0000";
        String expectedOutput = "\\u0000";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesStringWithMinAndMaxLength() {
        String input = new String(new char[Integer.MAX_VALUE]);
        String expectedOutput = new String(new char[Integer.MAX_VALUE]);
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesStringWithLeadingAndTrailingWhitespace() {
        String input = "   abcdef   ";
        String expectedOutput = "   abcdef\\s+";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesStringWithEOFCharacter() {
        String input = "\u0000";
        String expectedOutput = "\\u0000";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalErrorConstruction() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = '(';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        String expectedMessage = "Lexical error at line 1, column 1. Encountered: ( ";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testLexicalErrorConstructionWithNullErrorAfter() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = null;
        char curChar = '(';
        int reason = TokenMgrError.LEXICAL_ERROR;

        try {
            TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLexicalErrorConstructionWithNullCurChar() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = '\u0000';
        int reason = TokenMgrError.LEXICAL_ERROR;

        try {
            TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, null, reason);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLexicalErrorConstructionWithReasonOutOfRange() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = '(';
        int reason = 1000;

        try {
            TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLexicalErrorConstructionWithErrorAfterGreaterThan1000Characters() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = new String(new char[Integer.MAX_VALUE + 1]);
        char curChar = '(';
        int reason = TokenMgrError.LEXICAL_ERROR;

        try {
            TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTokenMgrErrorConstruction() {
        TokenMgrError error = new TokenMgrError();
        String expectedMessage = "";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testTokenMgrErrorConstructionWithNullMessage() {
        try {
            TokenMgrError error = new TokenMgrError(null, 0, 0, 0, null, '\u0000', 0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testTokenMgrErrorConstructionWithReasonOutOfRange() {
        try {
            TokenMgrError error = new TokenMgrError(false, 0, 0, 0, null, '\u0000', 1000);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
