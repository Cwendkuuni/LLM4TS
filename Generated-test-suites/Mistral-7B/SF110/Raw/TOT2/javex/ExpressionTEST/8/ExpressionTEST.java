package org.javex.test;

import org.javex.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    // Test cases from all experts
    // ...

    // Bonus test case: test for invalid characters in expression
    @Test(expected = Exception.class)
    public void testInvalidCharacters() throws Exception {
        Expression expression = new Expression("1 + 2 * ^");
        expression.prepare();
        expression.evaluate();
    }
}