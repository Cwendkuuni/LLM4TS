package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {
    // Test addEscapes method here
    @Test
    public void testAddEscapes() {
        String str = "Hello\tWorld!";
        String expected = "Hello\\tWorld!";
        String result = TokenMgrError.addEscapes(str);
        Assert.assertEquals(expected, result);
    }

    // Test LexicalError constructor here
    @Test
    public void testLexicalError() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "asdf";
        char curChar = 'z';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expected = "Lexical error at line 5, column 10.  Encountered: <EOF>  (0), after : asdf";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String actual = error.getMessage();
        Assert.assertEquals(expected, actual);
    }

    // Add more test cases for other methods and edge cases here
}