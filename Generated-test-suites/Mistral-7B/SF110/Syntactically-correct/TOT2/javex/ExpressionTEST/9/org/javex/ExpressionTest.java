package org.junit.test;

import org.junit.Test;
import org.javex.Expression;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ExpressionTest {
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List expectedList = new ArrayList<>();
        List actualList = expression.getExternalFunctions();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("test");
        assertTrue(expression instanceof Expression);
    }

    @Test
    public void testGetExternalFunctionsWithNoExternalFunctions() {
        Expression expression = new Expression("1 + 2");
        List expectedList = new ArrayList<>();
        List actualList = expression.getExternalFunctions();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testGetExternalFunctionsWithMultipleFunctions() {
        Expression expression = new Expression("sin(x) + cos(x)");
        List expectedList = new ArrayList<>();
        expectedList.add(new FunctionExpression("sin", FunctionExpression.TYPE_MATH));
        expectedList.add(new FunctionExpression("cos", FunctionExpression.TYPE_MATH));
        List actualList = expression.getExternalFunctions();
        assertEquals(expectedList, actualList);
    }
}