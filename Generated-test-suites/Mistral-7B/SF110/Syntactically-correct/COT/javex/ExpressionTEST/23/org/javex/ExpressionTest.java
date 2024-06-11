package org.javex.test;

import static org.junit.Assert.*;

import org.javex.utils.*;
import org.javex.functions.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Test;

import java.util.*;

public class ExpressionTest {

    @Test
    public void testExpressionConstructors() {
        Expression e = new Expression("1+1");
        assertFalse(e.isPrepared());
        assertEquals(2, e.getStack().size());
        Expression e1 = new Expression("(1+1)");
        assertTrue(e1.isPrepared());
        assertEquals(1, e1.getStack().size());
        Expression e2 = new Expression("");
        assertFalse(e2.isPrepared());
        assertEquals(0, e2.getStack().size());
    }

    @Test
    public void testGetExternalFunctions() {
        Expression e = new Expression("1+1");
        List<FunctionExpression> functions = e.getExternalFunctions();
        assertTrue(functions.isEmpty());
    }

    @Test
    public void testDoEvaluationStackBasic() {
        Expression e = new Expression("1+1");
        e.prepare();
        ExpressionStack stack = e.getStackCopy();
        assertEquals(1, stack.size());
        assertEquals("1+1", stack.get(0));
        ExpressionStack values = new ExpressionStack();
        Stack<String> operands = new Stack<>();
        int input = 0;
        int parentesis = 0;
        while (input < stack.size()) {
            String expression = stack.get(input);
            if (JavexUtils.isReservedChar(expression) || e.isFunction(expression)) {
                if (expression.equals("(")) {
                    ++parentesis;
                    operands.push(expression);
                }
                else if (expression.equals(")")) {
                    boolean b = true;
                    while (!operands.isEmpty()) {
                        if (!b) {
                            break;
                        }
                        String op = operands.peek();
                        if (!op.equals("(")) {
                            values.push(operands.pop());
                        }
                        else {
                            operands.pop();
                            b = false;
                            --parentesis;
                        }
                    }
                }
                else if (expression.equals("+") || expression.equals("-")) {
                    boolean b = true;
                    while (!operands.isEmpty() && b) {
                        String op = operands.peek();
                        if (op.equals("+") || op.equals("-") || op.equals("(")) {
                            b = false;
                        }
                        if (!op.equals("(")) {
                            values.push(operands.pop());
                        }
                    }
                    operands.push(expression);
                }
                // other cases ...
            }
            else {
                values.push(expression);
            }
            ++input;
        }
        ExpressionStack expectedValues = new ExpressionStack();
        expectedValues.push("1");
        expectedValues.push("1");
        assertEquals(expectedValues, values);
    }

    @Test(expected = Exception.class)
    public void testDoEvaluationStackException() {
        Expression e = new Expression("1+1/0");
        e.prepare();
    }

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("");
        assertFalse(e.isReservedFunction(""));
        FunctionExpression function = new AndFunction();
        Expression e1 = new Expression("and");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        assertTrue(e1.isReservedFunction("and"));
        assertFalse(e1.isReservedFunction("and1"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("");
        assertFalse(e.isLogicalFunction(""));
        FunctionExpression function = new AndFunction();
        Expression e1 = new Expression("and");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        assertTrue(e1.isLogicalFunction("and"));
        assertFalse(e1.isLogicalFunction("and1"));
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("");
        assertFalse(e.isMathFunction(""));
        FunctionExpression function = new AddFunction();
        Expression e1 = new Expression("+");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        assertTrue(e1.isMathFunction("+"));
        assertFalse(e1.isMathFunction("+1"));
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("");
        assertFalse(e.isDateFunction(""));
        FunctionExpression function = new DateAddFunction();
        Expression e1 = new Expression("dateAdd");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        assertTrue(e1.isDateFunction("dateAdd"));
        assertFalse(e1.isDateFunction("dateAdd1"));
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("");
        assertFalse(e.isStringFunction(""));
        FunctionExpression function = new SubStringFunction();
        Expression e1 = new Expression("substring");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        assertTrue(e1.isStringFunction("substring"));
        assertFalse(e1.isStringFunction("substring1"));
    }

    @Test
    public void testGetFunction() {
        Expression e = new Expression("");
        FunctionExpression function = new AndFunction();
        Expression e1 = new Expression("and");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        FunctionExpression result = e1.getFunction("and");
        assertEquals(function, result);
        assertNull(e1.getFunction("and1"));
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("");
        FunctionExpression function = new AddFunction();
        Expression e1 = new Expression("+");
        e1.externalFunctions = new ArrayList<>();
        e1.externalFunctions.add(function);
        assertTrue(e1.isFunction("+"));
        assertFalse(e1.isFunction("+1"));
    }
}