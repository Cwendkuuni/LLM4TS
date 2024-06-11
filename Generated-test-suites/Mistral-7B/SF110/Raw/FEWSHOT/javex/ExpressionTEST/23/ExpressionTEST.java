package org.javex.test;

import static org.junit.Assert.*;
import org.javex.utils.*;
import org.javex.functions.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testExpression() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        FunctionExpression andFunction = Expression.getReservedFunctions("and");
        FunctionExpression greaterFunction = Expression.getReservedFunctions(">");
        ExpressionStack stack = e.getStackCopy();
        FunctionExpression op1 = e.getFunction("*");
        FunctionExpression op2 = e.getFunction("+");
        Expression exp1 = new Expression("3 + 4");
        Expression exp2 = new Expression("2");
        exp1.prepare();
        exp2.prepare();
        FunctionExpression result = op2.apply(exp1.evaluate(), exp2.evaluate());
        assertEquals(7, result.evaluate());

        stack = e.getStackCopy();
        FunctionExpression op3 = e.getFunction("*");
        Expression exp3 = new Expression("1");
        exp3.prepare();
        FunctionExpression op4 = e.getFunction("+");
        Expression exp4 = new Expression("2");
        exp4.prepare();
        FunctionExpression operand1 = e.getFunction("1");
        FunctionExpression operand2 = andFunction;
        FunctionExpression leftOperand = exp4.evaluate();
        FunctionExpression rightOperand = operand1;
        FunctionExpression and = null;
        if (operand2 != null) {
            and = operand2.apply(leftOperand, rightOperand);
        }
        FunctionExpression op5 = e.getFunction("+");
        FunctionExpression operand3 = e.getFunction("(");
        Expression leftExpression = new Expression("1 + 2");
        leftExpression.prepare();
        Expression rightExpression = new Expression("3 + 4");
        rightExpression.prepare();
        FunctionExpression add1 = op5.apply(leftExpression.evaluate(), rightExpression.evaluate());
        stack.push(String.valueOf(add1.evaluate()));
        stack.push(")");
        FunctionExpression expressionToTest = new Expression(stack.toString());
        expressionToTest.prepare();
        assertEquals(7, expressionToTest.evaluate());

        stack = e.getStackCopy();
        Expression exp5 = new Expression("5");
        exp5.prepare();
        FunctionExpression op6 = e.getFunction(">");
        FunctionExpression operand4 = e.getFunction("3");
        FunctionExpression comparison = op6.apply(exp5.evaluate(), operand4);
        FunctionExpression finalResult = and;
        if (and == null) {
            finalResult = comparison;
        }
        assertEquals(true, finalResult.evaluate());
    }

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        assertTrue(e.isReservedFunction("and"));
        assertFalse(e.isReservedFunction("and1"));
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        assertTrue(e.isFunction("*"));
        assertFalse(e.isFunction("*1"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        assertTrue(e.isLogicalFunction("and"));
        assertFalse(e.isLogicalFunction("*"));
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        assertTrue(e.isMathFunction("*"));
        assertFalse(e.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        assertFalse(e.isDateFunction("and"));
        assertFalse(e.isDateFunction("*"));
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("1 + 2 * (3 + 4) and 5 > 3");
        e.prepare();
        assertFalse(e.isStringFunction("and"));
        assertFalse(e.isStringFunction("*"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression e = new Expression("myFunction('arg1', arg2)");
        FunctionExpression function1 = new MyFunctionExpression("myFunction");
        FunctionExpression function2 = new ArgumentFunction("arg1");
        FunctionExpression function3 = new ArgumentFunction("arg2");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(function1);
        functions.add(function2);
        functions.add(function3);
        e.setExternalFunctions(functions);
        e.prepare();
        FunctionExpression result = e.getExternalFunction("myFunction");
        assertEquals(function1, result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression e = new Expression("myFunction('arg1', arg2)");
        FunctionExpression function1 = new MyFunctionExpression("myFunction");
        FunctionExpression function2 = new ArgumentFunction("arg1");
        FunctionExpression function3 = new ArgumentFunction("arg2");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(function1);
        functions.add(function2);
        functions.add(function3);
        e.setExternalFunctions(functions);
        e.prepare();
        assertTrue(e.isExternalFunction("myFunction"));
        assertFalse(e.isExternalFunction("arg1"));
    }
}

class MyFunctionExpression extends FunctionExpression {
    public MyFunctionExpression(String symbol) {
        super(symbol, FunctionExpression.USER_DEFINED);
    }

    @Override
    public Object apply(List args) throws Exception {
        // Implement the logic of the custom function here
        return null;
    }
}

class ArgumentFunction extends FunctionExpression {
    private String argumentSymbol;

    public ArgumentFunction(String symbol) {
        super(symbol, FunctionExpression.ARGUMENT);
        this.argumentSymbol = symbol;
    }

    @Override
    public Object apply(List args) throws Exception {
        Object arg = args.get(0);
        return arg;
    }

    public String getArgumentSymbol() {
        return this.argumentSymbol;
    }
}