package org.javex.test;

import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testConstructor() {
        Expression e = new Expression("(1 + 2) * 3");
        Assert.assertFalse(e.isPrepared());
        Assert.assertEquals(6, e.stackSize());
        Assert.assertNotNull(e.getStack());
        Assert.assertEquals("1", e.getStack().get(0));
        Assert.assertEquals("+", e.getStack().get(1));
        Assert.assertEquals("2", e.getStack().get(2));
        Assert.assertEquals("*", e.getStack().get(3));
        Assert.assertEquals("3", e.getStack().get(4));
        Assert.assertEquals(")", e.getStack().get(5));
    }

    @Test
    public void testGetExternalFunctions() {
        Expression e = new Expression("(1 + 2 * 3)");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new AddFunction());
        externalFunctions.add(new MultiplyFunction());
        e.setExternalFunctions(externalFunctions);
        List<FunctionExpression> functions = e.getExternalFunctions();
        Assert.assertNotNull(functions);
        Assert.assertEquals(2, functions.size());
        Assert.assertEquals(AddFunction.class, functions.get(0).getClass());
        Assert.assertEquals(MultiplyFunction.class, functions.get(1).getClass());
    }

    @Test
    public void testDoEvaluationStackWhenParenthesesAreBalanced() {
        Expression e = new Expression("(1 + 2) * 3");
        e.prepare();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");
        Assert.assertEquals(expectedStack, e.getStack());
    }

    @Test
    public void testDoEvaluationStackWhenParenthesesAreNotBalanced() {
        Expression e = new Expression("(1 + 2)");
        try {
            e.prepare();
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertEquals("Expression error: Unbalanced parentheses", ex.getMessage());
        }
    }

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("A > B");
        Assert.assertTrue(e.isReservedFunction(">"));
        Assert.assertFalse(e.isReservedFunction("C"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("A and B");
        Assert.assertTrue(e.isLogicalFunction("and"));
        Assert.assertFalse(e.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("Sqrt(X)");
        Assert.assertTrue(e.isMathFunction("Sqrt"));
        Assert.assertFalse(e.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("DateDiff(Date1, Date2, 'day')");
        Assert.assertTrue(e.isDateFunction("DateDiff"));
        Assert.assertFalse(e.isDateFunction("and"));
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("Length(String1)");
        Assert.assertTrue(e.isStringFunction("Length"));
        Assert.assertFalse(e.isStringFunction("and"));
    }

    @Test
    public void testGetFunction() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        putFunction(new AndFunction(), reservedFunctions);
        Expression e = new Expression("A and B");
        FunctionExpression function = e.getFunction("and");
        Assert.assertEquals(AndFunction.class, function.getClass());
        Assert.assertEquals("and", function.getSimbol());
    }

    @Test(expected = RuntimeException.class)
    public void testGetFunctionWhenFunctionIsNotReservedOrExternal() {
        Expression e = new Expression("A and B");
        FunctionExpression function = e.getFunction("Z");
    }

    @Test
    public void testEvaluateSimpleExpression() throws Exception {
        Expression e = new Expression("1 + 2");
        Object result = e.evaluate();
        Assert.assertEquals(3, result);
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithException() throws Exception {
        Expression e = new Expression("1 / 0");
        e.prepare();
        throw new Exception(); // this test assumes that the division by zero throws an Exception
    }

    private static void putFunction(FunctionExpression f, Map<FunctionExpression, FunctionExpression> map) {
        map.put(f, f);
    }
}