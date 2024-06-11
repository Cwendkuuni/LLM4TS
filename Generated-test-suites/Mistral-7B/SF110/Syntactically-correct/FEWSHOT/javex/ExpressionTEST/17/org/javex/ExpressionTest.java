package org.javex.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.javex.utils.JavexUtils.isQuote;
import static org.javex.utils.JavexUtils.isReservedChar;
import static org.javex.utils.JavexUtils.isSpace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.junit.Test;
import org.javex.utils.Expression;
import org.javex.utils.ExpressionStack;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        assertTrue(Expression.isReservedFunction("and"));
        assertTrue(Expression.isReservedFunction("or"));
        assertTrue(Expression.isReservedFunction("is"));
        assertTrue(Expression.isReservedFunction("not"));
        assertFalse(Expression.isReservedFunction("+"));
        assertFalse(Expression.isReservedFunction("abc"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertFalse(Expression.isLogicalFunction("+"));
        assertFalse(Expression.isLogicalFunction("abc"));
        assertFalse(Expression.isLogicalFunction("not"));
        assertTrue(Expression.isLogicalFunction("and"));
        assertTrue(Expression.isLogicalFunction("or"));
    }

    @Test
    public void testIsMathFunction() {
        assertFalse(Expression.isMathFunction("and"));
        assertFalse(Expression.isMathFunction("or"));
        assertFalse(Expression.isMathFunction("not"));
        assertTrue(Expression.isMathFunction("abs"));
        assertTrue(Expression.isMathFunction("divide"));
        assertTrue(Expression.isMathFunction("sqrt"));
    }

    @Test
    public void testIsDateFunction() {
        assertFalse(Expression.isDateFunction("and"));
        assertFalse(Expression.isDateFunction("or"));
        assertFalse(Expression.isDateFunction("not"));
        assertTrue(Expression.isDateFunction("dateadd"));
        assertTrue(Expression.isDateFunction("datediff"));
    }

    @Test
    public void testIsStringFunction() {
        assertFalse(Expression.isStringFunction("and"));
        assertFalse(Expression.isStringFunction("or"));
        assertFalse(Expression.isStringFunction("not"));
        assertTrue(Expression.isStringFunction("length"));
        assertTrue(Expression.isStringFunction("substring"));
    }

    @Test
    public void testGetReservedFunctions() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = Expression.getReservedFunctions("and");
        assertEquals(AndFunction.class, reservedFunctions.keySet().iterator().next().getClass());
        assertEquals(AndFunction.class, reservedFunctions.values().iterator().next().getClass());
    }

    @Test
    public void testTokenize() {
        Expression e = new Expression("(1 + 2) * (3 and 4 > 5)");
        Stack<String> stack = e.getStackCopy();
        List<String> expected = Arrays.asList(
            "1", "+", "2", "*", "3", "and", "4", ">", "5"
        );
        List<String> actual = new ArrayList<>();
        String s = stack.pop();
        while (!stack.isEmpty()) {
            actual.add(s);
            s = stack.pop();
        }
        Collections.reverse(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrepare() throws Exception {
        Expression e = new Expression("(1 + 2) * (3 and 4 > 5)");
        e.prepare();
        ExpressionStack stack = e.getStack();
        List<Object> expected = Arrays.asList(
            new Double(1.0), new Integer(2), new Operator("+"),
            new Double(3.0), new Function(new AndFunction(), new Operator("and"), new Operator(">"), new Integer(5), new Integer(4)),
            new Operator("*")
        );
        List<Object> actual = new ArrayList<>();
        for (int i = 0; i < stack.size(); ++i) {
            String s = stack.get(i).toString();
            if (isNumber(s)) {
                actual.add(new Double(Double.parseDouble(s)));
            } else if (isOperator(s)) {
                actual.add(new Operator(s.charAt(0) == '(' ? new Parenthesis(true) : s.charAt(0) == ')' ? new Parenthesis(false) : new Operator(s)));
            } else {
                FunctionExpression f = Expression.getFunction(s);
                actual.add(new Function(f, null));
            }
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression e = new Expression("(1 + 2) * (3 and 4 > 5)");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(6.0, result);
    }

    @Test
    public void testEvaluateWithExternalFunctions() throws Exception {
        Expression e = new Expression("len(substring(dateadd(now(), 1), 5, 10)) > 5");
        Map<String, FunctionExpression> functions = new TreeMap<>();
        functions.put("len", new LengthFunction());
        functions.put("substring", new SubStringFunction());
        functions.put("dateadd", new DateAddFunction());
        e.setExternalFunctions(new ArrayList<>(functions.values()));
        e.prepare();
        Object result = e.evaluate();
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }

    private static boolean isNumber(String s) {
        return s.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?") || s.equals("(") || s.equals(")") || s.equals("");
    }

    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("and") || s.equals("or") || s.equals(">") || s.equals("<") || s.equals("=") || s.equals("not") || s.equals("(") || s.equals(")") || s.equals(",");
    }

}