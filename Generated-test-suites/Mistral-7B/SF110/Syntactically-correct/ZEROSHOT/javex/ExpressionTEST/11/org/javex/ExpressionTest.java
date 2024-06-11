package org.javex.Expression;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MaxFunction;
import org.javex.functions.math.MinFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testExpressionConstruction() {
        Expression e = new Expression("1 + 2 * 3");
        assertFalse(e.isPrepared());
        assertEquals(3, e.getStackSize());
        assertEquals("1", e.getStack().get(0));
        assertEquals("+", e.getStack().get(1));
        assertEquals("2", e.getStack().get(2));
        assertEquals("*", e.getStack().get(3));
        assertEquals("3", e.getStack().get(4));
    }

    @Test
    public void testPrepare() throws Exception {
        Expression e = new Expression("1 + 2 * 3");
        e.prepare();
        assertTrue(e.isPrepared());
        assertEquals(1, e.getStackSize());
        assertEquals("1 + 2 * 3", e.getExpression());
        assertEquals(4, e.evaluate().toString().split(" ").length);
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidExpression() throws Exception {
        Expression e = new Expression("1 + 2 * 3)");
        e.prepare();
        fail("Exception expected");
    }

    @Test
    public void testGetIsReservedFunction() {
        assertTrue(Expression.isReservedFunction("and"));
        assertTrue(Expression.isReservedFunction("or"));
        assertFalse(Expression.isReservedFunction("not"));
    }

    @Test
    public void testGetIsLogicalFunction() {
        assertTrue(Expression.isLogicalFunction("and"));
        assertTrue(Expression.isLogicalFunction("or"));
        assertFalse(Expression.isLogicalFunction("add"));
    }

    @Test
    public void testGetIsMathFunction() {
        assertTrue(Expression.isMathFunction("add"));
        assertTrue(Expression.isMathFunction("subtract"));
        assertFalse(Expression.isMathFunction("and"));
    }

    @Test
    public void testGetIsDateFunction() {
        assertTrue(Expression.isDateFunction("dateadd"));
        assertFalse(Expression.isDateFunction("add"));
    }

    @Test
    public void testGetIsStringFunction() {
        assertTrue(Expression.isStringFunction("length"));
        assertFalse(Expression.isStringFunction("add"));
    }

    @Test
    public void testGetFunction() {
        FunctionExpression fe = new AndFunction();
        Expression e = new Expression("and(1, 2)");
        assertEquals(fe, e.getFunction("and"));
        assertEquals(Expression.getReservedFunctions("and"), fe);
    }

    @Test
    public void testGetExternalFunction() {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(1, 2)");
        e.setExternalFunctions(functions);
        assertEquals(functions.get(0), e.getExternalFunction("myfunction"));
    }

    @Test
    public void testEvaluateSimpleExpression() throws Exception {
        Expression e = new Expression("1 + 2 * 3");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(7, result.toString().intValue());
    }

    @Test
    public void testEvaluateComplexExpression() throws Exception {
        Expression e = new Expression("(1 + 2) * (3 + 4)");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(14, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithExternalFunction() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(1, 2)");
        e.setExternalFunctions(functions);
        e.prepare();
        Object result = e.evaluate();
        assertEquals(3, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithReservedFunction() throws Exception {
        Expression e = new Expression("1 and 1");
        e.prepare();
        Object result = e.evaluate();
        assertTrue(result.toString().equals("true"));
    }

    @Test
    public void testEvaluateExpressionWithLogicalFunction() throws Exception {
        Expression e = new Expression("1 and 2 = 3 and 4 > 3");
        e.prepare();
        Object result = e.evaluate();
        assertTrue(result.toString().equals("false"));
    }

    @Test
    public void testEvaluateExpressionWithMathFunction() throws Exception {
        Expression e = new Expression("sqrt(25) + power(2, 2)");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(6.0, result.toString().doubleValue(), 0.001);
    }

    @Test
    public void testEvaluateExpressionWithDateFunction() throws Exception {
        Expression e = new Expression("dateadd(day(now()), 3)");
        e.prepare();
        Object result = e.evaluate();
        DateAddFunction dateAddFunction = (DateAddFunction) Expression.getReservedFunctions("dateadd");
        java.util.Date now = new java.util.Date();
        java.util.Date threeDaysLater = new java.util.Date(now.getTime() + 3 * 24 * 60 * 60 * 1000);
        assertEquals(threeDaysLater.toString(), result.toString());
    }

    @Test
    public void testEvaluateExpressionWithStringFunction() throws Exception {
        Expression e = new Expression("length(substring('test', 1, 3))");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(3, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithCustomFunction() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(1, 2)");
        e.setExternalFunctions(functions);
        e.prepare();
        Object result = e.evaluate();
        assertEquals(3, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionWithArguments() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(1, 2, 'test')");
        e.setExternalFunctions(functions);
        e.prepare();
        Object result = e.evaluate();
        assertEquals("testMyFunction(1, 2, 'test')", e.getExpression());
        assertEquals(5, result.toString().length());
    }

    @Test
    public void testEvaluateExpressionWithComplexExpressionWithCustomFunction() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(1, (2 + 3) * 4, 'test')");
        e.setExternalFunctions(functions);
        e.prepare();
        Object result = e.evaluate();
        assertEquals("testMyFunction(1, 10, 'test')", e.getExpression());
        assertEquals(8, result.toString().length());
    }

    @Test
    public void testEvaluateExpressionWithVariables() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        Expression e = new Expression("x + 1");
        e.setExternalFunctions(functions);
        e.prepare();
        e.setVariableValue("x", 2);
        Object result = e.evaluate();
        assertEquals(3, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithVariablesAndCustomFunction() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(x, 1)");
        e.setExternalFunctions(functions);
        e.prepare();
        e.setVariableValue("x", 2);
        Object result = e.evaluate();
        assertEquals(5, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithVariablesAndComplexExpression() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        Expression e = new Expression("x * (x + 1)");
        e.setExternalFunctions(functions);
        e.prepare();
        e.setVariableValue("x", 2);
        Object result = e.evaluate();
        assertEquals(6, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithVariablesAndCustomFunctionWithArguments() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(x, 1, 'test')");
        e.setExternalFunctions(functions);
        e.prepare();
        e.setVariableValue("x", 2);
        Object result = e.evaluate();
        assertEquals("testMyFunction(2, 1, 'test')", e.getExpression());
        assertEquals(8, result.toString().length());
    }

    @Test
    public void testEvaluateExpressionWithVariablesAndComplexExpressionWithArguments() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(x, (2 + 3), 'test')");
        e.setExternalFunctions(functions);
        e.prepare();
        e.setVariableValue("x", 2);
        Object result = e.evaluate();
        assertEquals("testMyFunction(2, 5, 'test')", e.getExpression());
        assertEquals(11, result.toString().length());
    }

    @Test
    public void testEvaluateExpressionWithNestedParentheses() throws Exception {
        Expression e = new Expression("(1 + 2) * (3 + 4)");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(14, result.toString().intValue());
    }

    @Test
    public void testEvaluateExpressionWithNegativeNumbers() throws Exception {
        Expression e = new Expression("3 + (-2 + 1)");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(4, result.toString().intValue());
    }

    @Test(expected = Exception.class)
    public void testEvaluateExpressionWithInvalidExpression() throws Exception {
        Expression e = new Expression("1 + 2 * 3)");
        e.prepare();
        fail("Exception expected");
    }

    @Test
    public void testEvaluateExpressionWithEmptyExpression() throws Exception {
        Expression e = new Expression("");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(0, result.toString().length());
    }

    @Test
    public void testEvaluateExpressionWithExpressionWithVariablesAndCustomFunctionWithArgumentsAndEmptyArguments() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(x, , 'test')");
        e.setExternalFunctions(functions);
        e.prepare();
        fail("Invalid expression with empty arguments");
    }

    @Test
    public void testEvaluateExpressionWithExpressionWithVariablesAndCustomFunctionWithInvalidArguments() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(x, a, x, 1)");
        e.setExternalFunctions(functions);
        fail("Invalid expression with invalid arguments");
    }

    @Test
    public void testEvaluateExpressionWithExpressionWithVariablesAndCustomFunctionWithInvalidArgumentsType() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myfunction"));
        Expression e = new Expression("myfunction(1, x, 1, 'test')");
        e.setExternalFunctions(functions);
        fail("Invalid expression with invalid arguments type");
    }

    @Test
    public void testEvaluateExpressionWithExpressionWithVariablesAndCustomFunctionWithInvalidFunction() throws Exception {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new VariableExpression("x"));
        functions.add(new MyFunctionExpression("myinvalidfunction"));
        Expression e = new Expression("myinvalidfunction(x, 1)");
        e.setExternalFunctions(functions);
        fail("Invalid function");
    }

    @Test
    public void testEvaluateExpressionWithExpressionWithReservedWordsAsIdentifiers() throws Exception {
        Expression e = new Expression("and(1, 2)");
        e.prepare();
        Object result = e.evaluate();
        assertTrue(result.toString().equals("false"));
    }

    @Test
    public void testEvaluateExpressionWithExpressionWithIdentifiersAsKeywords() throws Exception {
        Expression e = new Expression("if(1, 2, 3)");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(2, result.toString().intValue());
    }

    private static class MyFunctionExpression extends FunctionExpression {
        MyFunctionExpression(String name) {
            super(name, FunctionExpression.CUSTOM);
        }

        @Override
        public Object evaluate(List<Object> arguments) throws Exception {
            if (arguments.size() != 3) {
                throw new Exception("Invalid number of arguments");
            }
            return "testMyFunction(" + arguments.get(0) + ", " + arguments.get(1) + ", " + arguments.get(2) + ")";
        }
    }

    private static class VariableExpression extends FunctionExpression {
        VariableExpression(String name) {
            super(name, FunctionExpression.VARIABLE);
        }

        @Override
        public Object evaluate(List<Object> arguments) throws Exception {
            throw new Exception("Variable expressions are not evaluated");
        }

        public void setVariableValue(String variableName, Object value) {
            if (variableName.equals(this.getName())) {
                this.setValue(value);
            }
        }
    }
}