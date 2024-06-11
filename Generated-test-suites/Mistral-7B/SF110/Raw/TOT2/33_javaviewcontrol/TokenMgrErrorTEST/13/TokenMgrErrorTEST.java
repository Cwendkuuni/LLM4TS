
package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testEmptyConstructor() {
        // ...
    }

    @Test
    public void testCustomErrorConstructor() {
        // ...
    }

    @Test
    public void testComplexErrorConstructor() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLexicalState() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidErrorLineOrColumn() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidErrorLineOrColumn2() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacter() {
        // ...
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError(true, 1, 5, 10, "errorAfter", 'e', 2);
        String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: <EOF> \"e\" (271, " +
                " after : \"errorAfter\"";
        assertEquals(expectedErrorMessage, error.getMessage());
    }

    @Test
    public void testGetErrorCode() {
        TokenMgrError error = new TokenMgrError(true, 1, 5, 10, "errorAfter", 'e', 2);
        assertEquals(2, error.errorCode);
    }
}
