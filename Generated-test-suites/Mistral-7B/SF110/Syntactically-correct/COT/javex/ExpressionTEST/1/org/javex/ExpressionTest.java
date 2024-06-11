package org.javex.test;

import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.utils.JavexUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expr = new Expression("expression with functions");
        List<FunctionExpression> functions = expr.getExternalFunctions();
        Assert.assertNotNull(functions);
        Assert.assertEquals(0, functions.size());
    }

    @Test
    public void testConstructor() {
        Expression expr = new Expression("expression");
        Assert.assertNotNull(expr);
    }

    @Test
    public void testDoEvaluationStack_EmptyExpression() {
        Expression expr = new Expression("");
        expr.doEvaluationStack();
        Assert.assertEquals(0, expr.getStack().size());
    }

    @Test
    public void testDoEvaluationStack_SimpleExpression() {
        Expression expr = new Expression("5 + 3 * 2");
        expr.prepare();
        expr.doEvaluationStack();
        ExpressionStack expected = new ExpressionStack();
        expected.add("5");
        expected.add("3");
        expected.add("2");
        expected.add("*");
        expected.add("+");
        expected.add("15");
        Assert.assertEquals(expected, expr.getStack());
    }

    @Test
    public void testDoEvaluationStack_ComplexExpression() {
        Expression expr = new Expression("(5 + 3) * 2 + 3");
        expr.prepare();
        expr.doEvaluationStack();
        ExpressionStack expected = new ExpressionStack();
        expected.add("5");
        expected.add("3");
        expected.add("+");
        expected.add("8");
        expected.add("2");
        expected.add("*");
        expected.add("16");
        expected.add("3");
        expected.add("+");
        expected.add("19");
        Assert.assertEquals(expected, expr.getStack());
    }

    @Test
    public void testIsFunction_ReservedFunction() {
        Expression expr = new Expression("and(true, false)");
        Assert.assertTrue(expr.isFunction("and"));
    }

    @Test
    public void testIsFunction_ExternalFunction() {
        Expression expr = new Expression("myFunction(1, 2, 3)");
        FunctionExpression myFunction = new FunctionExpression("myFunction", Arrays.asList("d", "d", "d"), FunctionExpression.FUNCTION);
        expr.setExternalFunctions(Arrays.asList(myFunction));
        Assert.assertTrue(expr.isFunction("myFunction"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFunction_InvalidSymbol() {
        Expression expr = new Expression("expression");
        Assert.assertFalse(expr.isFunction("invalidSymbol"));
    }

    @Test
    public void testIsLogicalFunction_LogicalFunction() {
        Expression expr = new Expression("and(true, false)");
        Assert.assertTrue(expr.isLogicalFunction("and"));
    }

    @Test
    public void testIsLogicalFunction_NonLogicalFunction() {
        Expression expr = new Expression("5 + 3 * 2");
        Assert.assertFalse(expr.isLogicalFunction("*"));
    }

    @Test
    public void testIsMathFunction_MathFunction() {
        Expression expr = new Expression("sqrt(16)");
        Assert.assertTrue(expr.isMathFunction("sqrt"));
    }

    @Test
    public void testIsMathFunction_NonMathFunction() {
        Expression expr = new Expression("and(true, false)");
        Assert.assertFalse(expr.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction_DateFunction() {
        Expression expr = new Expression("DateDiff(day, '2022-01-01', '2022-12-31')");
        Assert.assertTrue(expr.isDateFunction("DateDiff"));
    }

    @Test
    public void testIsDateFunction_NonDateFunction() {
        Expression expr = new Expression("and(true, false)");
        Assert.assertFalse(expr.isDateFunction("and"));
    }

    @Test
    public void testIsStringFunction_StringFunction() {
        Expression expr = new Expression("Length(concat('hello', ' world'))");
        Assert.assertTrue(expr.isStringFunction("Length"));
    }

    @Test
    public void testIsStringFunction_NonStringFunction() {
        Expression expr = new Expression("and(true, false)");
        Assert.assertFalse(expr.isStringFunction("and"));
    }

    @Test
    public void testEvaluate_SimpleExpression() {
        Expression expr = new Expression("5 + 3 * 2");
        expr.prepare();
        Object result = null;
        try {
            result = expr.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(15.0, result);
    }

    @Test
    public void testEvaluate_ComplexExpression() {
        Expression expr = new Expression("(5 + 3) * 2 + 3");
        expr.prepare();
        Object result = null;
        try {
            result = expr.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(19.0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_InvalidExpression() {
        Expression expr = new Expression("invalidExpression");
        expr.prepare();
        Object result = null;
        try {
            result = expr.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.fail();
    }

    @Test
    public void testEvaluate_EmptyExpression() {
        Expression expr = new Expression("");
        Object result = null;
        try {
            result = expr.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetFunction_ReservedFunction() {
        Expression expr = new Expression("and(true, false)");
        FunctionExpression andFunction = Expression.getReservedFunctions("and");
        Assert.assertNotNull(andFunction);
        Assert.assertEquals(FunctionExpression.LOGICAL, andFunction.getType());
    }

    @Test
    public void testGetFunction_NonReservedFunction() {
        Expression expr = new Expression("myFunction(1, 2, 3)");
        FunctionExpression myFunction = new FunctionExpression("myFunction", Arrays.asList("d", "d", "d"), FunctionExpression.FUNCTION);
        expr.setExternalFunctions(Arrays.asList(myFunction));
        FunctionExpression function = expr.getFunction("myFunction");
        Assert.assertNotNull(function);
        Assert.assertEquals(FunctionExpression.FUNCTION, function.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunction_InvalidSymbol() {
        Expression expr = new Expression("expression");
        FunctionExpression function = expr.getFunction("invalidSymbol");
        Assert.fail();
    }

    @Test
    public void testPrepare() {
        Expression expr = new Expression("5 + 3 * 2");
        expr.prepare();
        Assert.assertTrue(expr.isPrepared());
    }

    @Test(expected = IllegalStateException.class)
    public void testPrepare_PreparedExpression() {
        Expression expr = new Expression("5 + 3 * 2");
        expr.prepare();
        expr.prepare();
        Assert.fail();
    }

    @Test
    public void testPrepare_EmptyExpression() {
        Expression expr = new Expression("");
        expr.prepare();
        Assert.assertTrue(expr.isPrepared());
    }

    @Test
    public void testGetStackCopy() {
        Expression expr = new Expression("5 + 3 * 2");
        ExpressionStack original = expr.getStack();
        ExpressionStack copy = expr.getStackCopy();
        Assert.assertNotSame(original, copy);
        Assert.assertEquals(original, copy);
    }
}