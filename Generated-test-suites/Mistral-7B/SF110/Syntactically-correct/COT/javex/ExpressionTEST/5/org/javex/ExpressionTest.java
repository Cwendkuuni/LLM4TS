package org.javex.test;

import static org.junit.Assert.*;

import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.Utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
        assertNotNull(externalFunctions);
        assertTrue(externalFunctions.size() == 0);
    }

    @Test
    public void testConstructor() {
        Expression expression = new Expression("test");
        assertNotNull(expression);
        assertFalse(expression.isPrepared());
        assertEquals(new ExpressionStack(), expression.getStack());
        List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
        assertTrue(externalFunctions.isEmpty());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("or"));
        assertFalse(expression.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isLogicalFunction("test"));
        assertFalse(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("andFunction"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isMathFunction("test"));
        assertFalse(expression.isMathFunction("sqrt"));
        assertTrue(expression.isMathFunction("sqrtFunction"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isDateFunction("test"));
        assertFalse(expression.isDateFunction("dateAdd"));
        assertTrue(expression.isDateFunction("dateAddFunction"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isStringFunction("test"));
        assertFalse(expression.isStringFunction("length"));
        assertTrue(expression.isStringFunction("lengthFunction"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("test");
        FunctionExpression function = expression.getFunction("and");
        assertNull(function);
        function = expression.getFunction("andFunction");
        assertNotNull(function);
        assertEquals(FunctionExpression.LOGICAL, function.getType());
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isFunction("test"));
        assertFalse(expression.isFunction("andFunction"));
        assertTrue(expression.isFunction("andFunction1"));
    }

    @Test(expected = Exception.class)
    public void testPrepareException() {
        Expression expression = new Expression("test + test");
        expression.prepare();
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("(test + test) and (test = test)");
        expression.prepare();
        assertTrue(expression.isPrepared());
        ExpressionStack stack = expression.getStack();
        assertEquals(Arrays.asList("(", "test", "+", "test", ")", "and", "(", "test", "=", "test", ")", ")", ""), stack);
    }

    @Test
    public void testEvaluateBasic() {
        Expression expression = new Expression("test = test");
        expression.prepare();
        try {
            Object result = expression.evaluate();
            assertTrue(result instanceof Boolean);
            assertTrue((Boolean) result);
        } catch (Exception e) {
            fail("Evaluation failed: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateBasicMath() {
        Expression expression = new Expression("sqrt(pow(2, 2))");
        expression.prepare();
        try {
            Object result = expression.evaluate();
            assertEquals(4.0, result);
        } catch (Exception e) {
            fail("Evaluation failed: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateBasicLogical() {
        Expression expression = new Expression("test1 and test2");
        expression.prepare();
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new AndFunction());
        ExpressionStack stack = new ExpressionStack();
        stack.addAll(functions);
        Expression externalExpression = new Expression("") {
            @Override
            public List<FunctionExpression> getExternalFunctions() {
                return stack;
            }
        };
        externalExpression.prepare();
        FunctionExpression andFunction = expression.getFunction("and");
        FunctionExpression testFunction1 = externalExpression.getFunction("test1");
        FunctionExpression testFunction2 = externalExpression.getFunction("test2");
        when(testFunction1.evaluate()).thenReturn(true);
        when(testFunction2.evaluate()).thenReturn(true);
        try {
            Object result = expression.evaluate();
            assertTrue(result instanceof Boolean);
            assertTrue((Boolean) result);
        } catch (Exception e) {
            fail("Evaluation failed: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateBasicDate() {
        Expression expression = new Expression("dateAdd(dateDiff(now(), date('2022-01-01')), 365) = date('2023-01-01')");
        ExpressionStack stack = new ExpressionStack();
        stack.add(new DateAddFunction());
        stack.add(new DateDiffFunction());
        stack.add(new NowFunction());
        stack.add(new DateFunction());
        Expression externalExpression = new Expression("") {
            @Override
            public List<FunctionExpression> getExternalFunctions() {
                return Arrays.asList(new NowFunction(), new DateFunction());
            }
        };
        externalExpression.prepare();
        FunctionExpression nowFunction = expression.getFunction("now");
        FunctionExpression dateFunction = expression.getFunction("date");
        when(nowFunction.evaluate()).thenReturn(new Date(2022, 11, 30));
        when(dateFunction.evaluate(anyString())).thenAnswer(invocation -> {
            String[] dateParts = invocation.getArgumentAt(0, String[].class);
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            return new Date(year, month, day);
        });
        try {
            Object result = expression.evaluate();
            assertTrue(result instanceof Boolean);
            assertTrue((Boolean) result);
        } catch (Exception e) {
            fail("Evaluation failed: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateBasicString() {
        Expression expression = new Expression("length(substring(test, 3, 4)) > 2");
        FunctionExpression lengthFunction = new LengthFunction();
        FunctionExpression substringFunction = new SubStringFunction();
        Expression externalExpression = new Expression("") {
            @Override
            public List<FunctionExpression> getExternalFunctions() {
                return Arrays.asList(lengthFunction, substringFunction);
            }
        };
        externalExpression.prepare();
        when(lengthFunction.evaluate(anyString())).thenReturn(4);
        when(substringFunction.evaluate(any(String.class), anyInt(), anyInt())).thenReturn("tes");
        try {
            Object result = expression.evaluate();
            assertTrue(result instanceof Boolean);
            assertTrue((Boolean) result);
        } catch (Exception e) {
            fail("Evaluation failed: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateEdgeCases() {
        Expression expression = new Expression("(test1 and test2) or (test3 or test4) or test5");
        ExpressionStack stack = new ExpressionStack();
        stack.add(new AndFunction());
        stack.add(new AndFunction());
        stack.add(new OrFunction());
        stack.add(new OrFunction());
        stack.add(new OrFunction());
        stack.addAll(Arrays.asList(new FunctionExpression("test1", FunctionExpression.LOGICAL),
                new FunctionExpression("test2", FunctionExpression.LOGICAL),
                new FunctionExpression("test3", FunctionExpression.LOGICAL),
                new FunctionExpression("test4", FunctionExpression.LOGICAL),
                new FunctionExpression("test5", FunctionExpression.LOGICAL)));
        Expression externalExpression = new Expression("") {
            @Override
            public List<FunctionExpression> getExternalFunctions() {
                return stack;
            }
        };
        externalExpression.prepare();
        FunctionExpression andFunction1 = expression.getFunction("and");
        FunctionExpression andFunction2 = externalExpression.getFunction("test1");
        FunctionExpression orFunction1 = externalExpression.getFunction("test3");
        FunctionExpression orFunction2 = externalExpression.getFunction("test4");
        when(andFunction1.evaluate()).thenReturn(true);
        when(andFunction2.evaluate()).thenReturn(false);
        when(orFunction1.evaluate()).thenReturn(true);
        when(orFunction2.evaluate()).thenReturn(true);
        try {
            Object result = expression.evaluate();
            assertTrue(result instanceof Boolean);
            assertTrue((Boolean) result);
        } catch (Exception e) {
            fail("Evaluation failed: " + e.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluateExceptionNull() {
        Expression expression = new Expression("test = null");
        expression.prepare();
        try {
            Object result = expression.evaluate();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid function call"));
            throw e;
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluateExceptionNullParameter() {
        Expression expression = new Expression("test = nullFunction(test)");
        expression.prepare();
        try {
            Object result = expression.evaluate();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid function call"));
            assertTrue(e.getMessage().contains("Function 'nullFunction' not found"));
            throw e;
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluateExceptionTypeMismatch() {
        Expression expression = new Expression("test = 5 and test = 'five'");
        expression.prepare();
        try {
            Object result = expression.evaluate();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Type mismatch"));
            throw e;
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluateExceptionInvalidExpression() {
        Expression expression = new Expression("test = test");
        expression.prepare();
        try {
            Object result = expression.evaluate();
            fail("Evaluation should have failed");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid expression"));
            throw e;
        }
    }

    private void assertListEquals(List expected, List actual) {
        List expectedCopy = new ArrayList(expected);
        List actualCopy = new ArrayList(actual);
        Collections.sort(expectedCopy);
        Collections.sort(actualCopy);
        assertEquals(expectedCopy, actualCopy);
    }

    private static FunctionExpression getFunctionMock(String name, FunctionExpression.Type type) {
        return mock(FunctionExpression.class)
                .withCalls(on(FunctionExpression.class).method("getType").willReturn(type))
                .withCalls(on(FunctionExpression.class).method("getSimbol").willReturn(name));
    }
}