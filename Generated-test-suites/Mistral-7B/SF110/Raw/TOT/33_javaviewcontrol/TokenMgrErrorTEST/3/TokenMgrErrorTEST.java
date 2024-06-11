
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello	World\n\r\fBackslash\\Quote\"Single'Tab(\\0)";
        String expectedOutput = "Hello\\tWorld\\n\\r\\fBackslash\\\\Quote\\\"Single\\'Tab(\\0)";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 10;
        String errorAfter = "input";
        char curChar = ';';
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        String expectedErrorMessage = "Lexical error at line 1, column 10. Encountered: input (63), after : input";
        String actualErrorMessage = error.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testConstructors() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "input";
        char curChar = 'A';
        int reason = 4;

        // Testing the default constructor
        TokenMgrError error1 = new TokenMgrError();
        assertEquals(0, error1.getErrorCode());

        // Testing the constructor with message and reason
        String message = "Message";
        TokenMgrError error2 = new TokenMgrError(message, reason);
        assertEquals(reason, error2.getErrorCode());
        assertEquals(message, error2.getMessage());

        // Testing the constructor with LexicalError
        TokenMgrError lexicalErrorMock = mock(TokenMgrError.class);
        String lexicalErrorMessage = "Lexical error message";
        when(lexicalErrorMock.getMessage()).thenReturn(lexicalErrorMessage);
        TokenMgrError error3 = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(reason, error3.getErrorCode());
        assertEquals(lexicalErrorMessage, error3.getMessage());
    }
}
