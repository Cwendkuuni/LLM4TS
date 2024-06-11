
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld!";
        String expectedOutput = "Hello\\nWorld!";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 10;
        String errorAfter = "abc";
        char curChar = '+';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expectedOutput = "Lexical error at line 1, column 10. Encountered: \"+\" (43), after : \"abc\"";
        String output = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason).getMessage();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTokenMgrErrorConstruction() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 15;
        String errorAfter = "def";
        char curChar = '(';
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        String expectedOutput = "Lexical error at line 5, column 15. Encountered: \"(\" (40), after : \"def\"";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedOutput, error.getMessage());
    }
}
