package org.javex.test;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.utils.ExpressionStack;
import org.javex.Expression;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("1 + 2 * (3 + 4)");
        expression.prepare();
        assertTrue(expression.isFunction("+"));
        assertTrue(expression.isFunction("*"));
        assertTrue(expression.isFunction("("));
        assertTrue(expression.isFunction(")"));
        assertFalse(expression.isFunction("!"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("1 + 2 * (3 + 4)");
        expression.prepare();
        FunctionExpression addFunction = Expression.getReservedFunctions("+");
        FunctionExpression multiplyFunction = Expression.getReservedFunctions("*");
        FunctionExpression leftParenthesisFunction = Expression.getReservedFunctions("(");
        FunctionExpression rightParenthesisFunction = Expression.getReservedFunctions(")");

        assertEquals(addFunction, expression.getFunction("+"));
        assertEquals(multiplyFunction, expression.getFunction("*"));
        assertEquals(leftParenthesisFunction, expression.getFunction("("));
        assertEquals(rightParenthesisFunction, expression.getFunction(")"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("1 + 2 * (3 or 4)");
        expression.prepare();
        assertTrue(expression.isLogicalFunction("or"));
        assertFalse(expression.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("sqrt(16)");
        expression.prepare();
        assertTrue(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("+"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("DateAdd(now(), interval '1 day')");
        expression.prepare();
        assertTrue(expression.isDateFunction("DateAdd"));
        assertFalse(expression.isDateFunction("+"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("length(name)");
        expression.prepare();
        assertTrue(expression.isStringFunction("length"));
        assertFalse(expression.isStringFunction("+"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("myFunction(a, b)");
        expression.prepare();
        expression.setExternalFunctions(new ArrayList<Expression>() {{
            add(new Expression("myFunction", new String[]{"a", "b"}));
        }});

        assertTrue(expression.isExternalFunction("myFunction"));
        assertFalse(expression.isExternalFunction("+"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("myFunction(a, b)");
        expression.prepare();
        expression.setExternalFunctions(new ArrayList<Expression>() {{
            add(new Expression("myFunction", new String[]{"a", "b"}));
        }});

        Expression myFunctionExpression = expression.getExternalFunction("myFunction");
        assertEquals("myFunction", myFunctionExpression.getFunctionName());
        assertEquals(2, myFunctionExpression.getArguments().size());
    }

    @Test
    public void testEvaluateSimpleExpression() throws Exception {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        assertEquals(3, expression.evaluate().intValue());
    }

    @Test
    public void testEvaluateComplexExpression() throws Exception {
        Expression expression = new Expression("1 + 2 * (3 + 4)");
        expression.prepare();
        assertEquals(7, expression.evaluate().intValue());
    }

    @Test
    public void testEvaluateLogicalExpression() throws Exception {
        Expression expression = new Expression("(5 > 3) and (10 < 20)");
        expression.prepare();
        assertTrue(expression.evaluate().booleanValue());
    }

    @Test
    public void testEvaluateStringFunction() throws Exception {
        Expression expression = new Expression("upper(lower('HELLO WORLD'))");
        expression.prepare();
        assertEquals("HELLO WORLD", expression.getStack().get(0).toString());
        expression.prepare();
        expression.tokenize();
        List<String> arguments = expression.getArguments();
        FunctionExpression upperFunction = Expression.getReservedFunctions("upper");
        FunctionExpression lowerFunction = Expression.getReservedFunctions("lower");

        expression.stack(lowerFunction.getFunctionName() + "(" + arguments.get(0) + ")", expression.getStack());
        expression.prepare();
        expression.tokenize();
        expression.stack(upperFunction.getFunctionName() + "(" + arguments.get(0) + ")", expression.getStack());
        expression.prepare();
        assertEquals("HELLO WORLD".toUpperCase(), expression.evaluate().toString());
    }

    @Test
    public void testEvaluateMathFunction() throws Exception {
        Expression expression = new Expression("sqrt(16)");
        expression.prepare();
        assertEquals(4.0, expression.evaluate().doubleValue(), 0.001);
    }

    @Test
    public void testEvaluateDateFunction() throws Exception {
        Expression expression = new Expression("DateAdd(now(), interval '1 day')");
        expression.prepare();
        expression.tokenize();
        FunctionExpression nowFunction = Expression.getReservedFunctions("now");
        List<String> arguments = expression.getArguments();
        expression.stack(nowFunction.getFunctionName() + "()", expression.getStack());
        expression.prepare();
        expression.tokenize();
        FunctionExpression dateAddFunction = Expression.getReservedFunctions("DateAdd");
        expression.stack(dateAddFunction.getFunctionName() + "(" + nowFunction.getFunctionName() + "(), interval '1 day')", expression.getStack());
        expression.prepare();
        assertEquals(System.currentTimeMillis() + 86400000L, expression.evaluate().longValue());
    }

    @Test
    public void testEvaluateComplexExpressionWithExternalFunction() throws Exception {
        Expression expression = new Expression("myFunction(5, 10)");
        expression.prepare();
        expression.setExternalFunctions(new ArrayList<Expression>() {{
            add(new Expression("myFunction", new String[]{"a", "b"}));
        }});
        expression.getExternalFunction("myFunction").setFunctionBody(new AndFunction());

        assertTrue(expression.evaluate().booleanValue());
    }

    @Test
    public void testEvaluateExpressionWithEmptyStack() throws Exception {
        Expression expression = new Expression("");
        expression.prepare();
        assertNull(expression.evaluate());
    }
}