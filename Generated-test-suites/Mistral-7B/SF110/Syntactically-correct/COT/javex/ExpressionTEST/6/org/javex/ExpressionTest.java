package org.javex.test;

import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    private Expression expression;
    private ExpressionStack stack;
    private List<FunctionExpression> externalFunctions;

    @Before
    public void setup() {
        expression = new Expression("(1 + 2) * (3 - 4) and (5 > 4) or (6 < 7)") {
            @Override
            public Object evaluate() throws Exception {
                prepare();
                return stack.get(0);
            }
        };
        stack = expression.getStackCopy();
        externalFunctions = new ArrayList<>();
    }

    @Test
    public void testGetExternalFunctions() {
        // Check empty getExternalFunctions
        Expression emptyExpression = new Expression("");
        Assert.assertNull(emptyExpression.getExternalFunctions());

        // Check getExternalFunctions with defined functions
        Expression withFunctionsExpression = new Expression("myFunction(5)");
        FunctionExpression myFunction = new FunctionExpression("myFunction", FunctionExpression.USER_DEFINED);
        withFunctionsExpression.setExternalFunctions(externalFunctions);
        externalFunctions.add(myFunction);
        Assert.assertEquals(myFunction, withFunctionsExpression.getExternalFunction("myFunction"));
        Assert.assertEquals(externalFunctions, withFunctionsExpression.getExternalFunctions());
    }

    @Test
    public void testConstructor() {
        // Check Expression constructor with empty string
        Assert.assertNotNull(expression);
        Assert.assertEquals(0, expression.stack.size());

        // Check Expression constructor with valid string
        Expression validExpression = new Expression("1 + 2");
        Assert.assertNotNull(validExpression);
        Assert.assertEquals("1 + 2", validExpression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        // Check doEvaluationStack with empty Expression
        Expression emptyExpression = new Expression("");
        emptyExpression.doEvaluationStack();
        Assert.assertEquals(0, emptyExpression.stack.size());

        // Check doEvaluationStack with valid Expression
        Expression validExpression = new Expression("1 + 2");
        validExpression.doEvaluationStack();
        Assert.assertEquals("1 + 2", validExpression.expression);
        Assert.assertEquals(3, validExpression.stack.size());
        Assert.assertEquals("1", validExpression.stack.get(0));
        Assert.assertEquals("2", validExpression.stack.get(1));
        Assert.assertEquals("1 + 2", validExpression.stack.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateWithException() {
        expression = new Expression("(1 + 2) * (3 / 0)");
        expression.prepare();
        expression.evaluate();
    }

    @Test
    public void testIsReservedFunction() {
        // Check isReservedFunction with valid reserved function
        Assert.assertTrue(expression.isReservedFunction("and"));
        Assert.assertTrue(expression.isReservedFunction("or"));

        // Check isReservedFunction with invalid symbol
        Assert.assertFalse(expression.isReservedFunction("myFunction"));
    }

    @Test
    public void testIsLogicalFunction() {
        // Check isLogicalFunction with logical functions
        Assert.assertTrue(expression.isLogicalFunction("and"));
        Assert.assertTrue(expression.isLogicalFunction("or"));

        // Check isLogicalFunction with invalid symbol
        Assert.assertFalse(expression.isLogicalFunction("myFunction"));
    }

    @Test
    public void testIsMathFunction() {
        // Check isMathFunction with math functions
        Assert.assertTrue(expression.isMathFunction("abs"));
        Assert.assertTrue(expression.isMathFunction("sqrt"));

        // Check isMathFunction with invalid symbol
        Assert.assertFalse(expression.isMathFunction("myFunction"));
    }

    @Test
    public void testIsDateFunction() {
        // Add Date functions to Expression
        FunctionExpression dateAddFunction = new DateAddFunction();
        FunctionExpression dateDiffFunction = new DateDiffFunction();
        expression.setExternalFunctions(externalFunctions);
        externalFunctions.addAll(List.of(dateAddFunction, dateDiffFunction));

        // Check isDateFunction with valid symbols
        Assert.assertTrue(expression.isDateFunction("dateadd"));
        Assert.assertTrue(expression.isDateFunction("datediff"));

        // Check isDateFunction with invalid symbols
        Assert.assertFalse(expression.isDateFunction("myFunction"));
    }

    @Test
    public void testIsStringFunction() {
        // Check isStringFunction with string functions
        Assert.assertTrue(expression.isStringFunction("length"));
        Assert.assertTrue(expression.isStringFunction("substring"));

        // Check isStringFunction with invalid symbols
        Assert.assertFalse(expression.isStringFunction("myFunction"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFunctionWithInvalidSymbol() {
        Assert.assertFalse(expression.isFunction("myFunction"));
    }

    @Test
    public void testPrepare() {
        // Check prepare with empty Expression
        Expression emptyExpression = new Expression("");
        emptyExpression.prepare();
        Assert.assertEquals(0, emptyExpression.stack.size());

        // Check prepare with valid Expression
        expression.prepare();
        Assert.assertEquals("1 + 2", expression.expression);
        Assert.assertEquals(3, expression.stack.size());
        Assert.assertEquals("1", expression.stack.get(0));
        Assert.assertEquals("2", expression.stack.get(1));
        Assert.assertEquals("1 + 2", expression.stack.get(2));
    }
}