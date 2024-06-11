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
    private List externalFunctions;

    @Before
    public void setUp() {
        expression = new Expression("(a + b) and (c > 10 and d = 5)") {
            @Override
            public Object evaluate() throws Exception {
                prepare();
                return stack.pop();
            }
        };
        stack = new ExpressionStack();
        externalFunctions = new ArrayList<>();
    }

    @Test
    public void testGetExternalFunctions() {
        Assert.assertEquals(expression.getExternalFunctions(), externalFunctions);
    }

    @Test
    public void testExpressionConstructor() {
        Assert.assertNotNull(expression);
        Assert.assertFalse(expression.isPrepared());
    }

    @Test
    public void testDoEvaluationStackBasic() {
        expression.doEvaluationStack();
        Assert.assertFalse(expression.isPrepared());
        Assert.assertEquals(expression.getStackSize(), 14);

        final String[] expected = new String[]{"a", "b", "+", "(", "c", ">", "10", "and", "(", "d", "=", "5", ")", ")"};
        for (int i = 0; i < expression.getStackSize(); ++i) {
            Assert.assertEquals(expression.getStack().get(i), expected[i]);
        }
    }

    @Test(expected = AssertionError.class)
    public void testDoEvaluationStackException() {
        expression = new Expression("(a + b) and (c > 10 and d = 5)") {
            @Override
            public Object evaluate() throws Exception {
                prepare();
                throw new Exception("Test Exception");
            }
        };
        expression.doEvaluationStack();
    }

    @Test
    public void testIsReservedFunctionBasic() {
        Assert.assertTrue(expression.isReservedFunction("and"));
        Assert.assertFalse(expression.isReservedFunction("notand"));
    }

    @Test
    public void testIsLogicalFunctionBasic() {
        Assert.assertTrue(expression.isLogicalFunction("and"));
        Assert.assertFalse(expression.isLogicalFunction("abs"));
    }

    @Test
    public void testIsMathFunctionBasic() {
        Assert.assertFalse(expression.isMathFunction("and"));
        Assert.assertTrue(expression.isMathFunction("abs"));
    }

    @Test
    public void testIsDateFunctionBasic() {
        Assert.assertFalse(expression.isDateFunction("and"));
        Assert.assertTrue(expression.isDateFunction("dateadd"));
    }

    @Test
    public void testIsStringFunctionBasic() {
        Assert.assertFalse(expression.isStringFunction("and"));
        Assert.assertTrue(expression.isStringFunction("tolower"));
    }

    @Test
    public void testGetFunctionBasic() {
        Expression.putFunction(new AndFunction(), Expression.reservedFunctions);
        FunctionExpression function = expression.getFunction("and");
        Assert.assertEquals(function.getType(), FunctionExpression.LOGICAL);
        Assert.assertEquals(function.getSymbol(), "and");
    }

    @Test(expected = NullPointerException.class)
    public void testGetFunctionNullSymbol() {
        expression.getFunction(null);
    }

    @Test
    public void testGetFunctionUnknownSymbol() {
        Assert.assertNull(expression.getFunction("unknown"));
    }

    @Test
    public void testIsFunctionBasic() {
        Assert.assertTrue(expression.isFunction("and"));
        Assert.assertFalse(expression.isFunction("unknown"));
    }

    @Test
    public void testPrepareBasic() {
        expression.prepare();
        Assert.assertTrue(expression.isPrepared());
    }

    @Test(expected = RuntimeException.class)
    public void testPrepareException() {
        expression = new Expression("(a + b) and (c > 10 and d = 5)") {
            @Override
            public Object evaluate() throws Exception {
                throw new RuntimeException("Test Exception");
            }
        };
        expression.prepare();
    }

    @Test
    public void testEvaluateBasic() {
        Expression expression2 = new Expression("2 + 3") {
            @Override
            public Object evaluate() throws Exception {
                prepare();
                return stack.pop();
            }
        };
        expression2.prepare();
        Assert.assertEquals(expression2.evaluate(), 5.0);
    }

    @Test(expected = Exception.class)
    public void testEvaluateException() {
        Expression expression2 = new Expression("2 + 3") {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception("Test Exception");
            }
        };
        expression2.prepare();
        expression2.evaluate();
    }
}