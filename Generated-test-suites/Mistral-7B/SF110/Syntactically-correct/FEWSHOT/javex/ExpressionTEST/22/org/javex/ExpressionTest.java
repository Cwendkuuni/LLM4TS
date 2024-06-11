package org.javex;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("");
        assertTrue(e.isReservedFunction("and"));
        assertTrue(e.isReservedFunction("or"));
        assertTrue(e.isReservedFunction("not"));
        assertTrue(e.isReservedFunction("between"));
        assertTrue(e.isReservedFunction("contains"));
        assertTrue(e.isReservedFunction("endsWith"));
        assertTrue(e.isReservedFunction("equals"));
        assertTrue(e.isReservedFunction("isFunction"));
        assertTrue(e.isReservedFunction("isNull"));
        assertTrue(e.isReservedFunction("isNot"));
        assertTrue(e.isReservedFunction("isTrue"));
        assertTrue(e.isReservedFunction("isFalse"));
        assertTrue(e.isReservedFunction("greaterEqThan"));
        assertTrue(e.isReservedFunction("greaterThan"));
        assertTrue(e.isReservedFunction("lessEqThan"));
        assertTrue(e.isReservedFunction("lessThan"));
        assertTrue(e.isReservedFunction("notEquals"));
        assertTrue(e.isReservedFunction("abs"));
        assertTrue(e.isReservedFunction("divide"));
        assertTrue(e.isReservedFunction("multiply"));
        assertTrue(e.isReservedFunction("add"));
        assertTrue(e.isReservedFunction("subtract"));
        assertTrue(e.isReservedFunction("power"));
        assertTrue(e.isReservedFunction("sqrt"));
        assertTrue(e.isReservedFunction("dateAdd"));
        assertTrue(e.isReservedFunction("dateDiff"));
        assertTrue(e.isReservedFunction("length"));
        assertTrue(e.isReservedFunction("substring"));
        assertTrue(e.isReservedFunction("toLower"));
        assertTrue(e.isReservedFunction("toUpper"));
        assertFalse(e.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("");
        assertFalse(e.isLogicalFunction(""));
        assertFalse(e.isLogicalFunction("test"));
        assertTrue(e.isLogicalFunction("and"));
        assertTrue(e.isLogicalFunction("or"));
        assertTrue(e.isLogicalFunction("not"));
    }

    @Test
    public void testTokenize() {
        Expression e = new Expression("1 + 2 * (3 - 4) and 5 > 6 or (7 < 8 and 9 > 10)");
        List<String> expected = Arrays.asList("1", "+", "2", "*", "(", "3", "-", "4", ")", "and", "5", ">", "6", "or", "(", "7", "<", "8", "and", "9", ">", "10", ")");
        List<String> actual = new ArrayList<>();
        for (String s : e.getStackCopy().toArray(new String[0])) {
            actual.add(s);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("");
        assertFalse(e.isFunction(""));
        assertFalse(e.isFunction("test"));
        assertTrue(e.isFunction("and"));
        assertTrue(e.isFunction("or"));
        assertTrue(e.isFunction("not"));
        assertTrue(e.isFunction("abs"));
        assertTrue(e.isFunction("divide"));
        assertTrue(e.isFunction("multiply"));
        assertTrue(e.isFunction("add"));
        assertTrue(e.isFunction("subtract"));
        assertTrue(e.isFunction("power"));
        assertTrue(e.isFunction("sqrt"));
        assertTrue(e.isFunction("dateAdd"));
        assertTrue(e.isFunction("dateDiff"));
        assertTrue(e.isFunction("length"));
        assertTrue(e.isFunction("substring"));
        assertTrue(e.isFunction("toLower"));
        assertTrue(e.isFunction("toUpper"));
        assertFalse(e.isFunction("notAnd"));
    }

    @Test
    public void testEvaluate_LogicalFunctions() throws Exception {
        Expression e = new Expression("true and false");
        FunctionExpression and = Expression.getReservedFunctions("and");
        Object res = e.evaluate();
        assertTrue(res instanceof Boolean);
        assertEquals(false, res);

        e = new Expression("true or false");
        FunctionExpression or = Expression.getReservedFunctions("or");
        res = e.evaluate();
        assertTrue(res instanceof Boolean);
        assertTrue(res);

        e = new Expression("not (true and false)");
        FunctionExpression not = Expression.getReservedFunctions("not");
        FunctionExpression and1 = Expression.getReservedFunctions("and");
        res = e.evaluate();
        assertTrue(res instanceof Boolean);
        assertFalse(res);

        e = new Expression("not (false or true)");
        Object notRes = e.evaluate();
        assertTrue(notRes instanceof Boolean);
        assertTrue(notRes);

        e = new Expression("not (not (true))");
        res = e.evaluate();
        assertTrue(res instanceof Boolean);
        assertFalse(res);

        e = new Expression("not (not (false))");
        res = e.evaluate();
        assertTrue(res instanceof Boolean);
        assertTrue(res);
    }

    @Test
    public void testEvaluate_CommonFunctions() throws Exception {
        Expression e = new Expression("abs(-5)");
        FunctionExpression abs = Expression.getReservedFunctions("abs");
        Object res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(5, res.doubleValue());

        e = new Expression("multiply(2,3)");
        FunctionExpression multiply = Expression.getReservedFunctions("multiply");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(6, res.doubleValue());

        e = new Expression("add(2,3)");
        FunctionExpression add = Expression.getReservedFunctions("add");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(5, res.doubleValue());

        e = new Expression("subtract(5,3)");
        FunctionExpression subtract = Expression.getReservedFunctions("subtract");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(2, res.doubleValue());

        e = new Expression("power(2,3)");
        FunctionExpression power = Expression.getReservedFunctions("power");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(8, res.doubleValue());

        e = new Expression("sqrt(16)");
        FunctionExpression sqrt = Expression.getReservedFunctions("sqrt");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(4, res.doubleValue(), 0.001);

        e = new Expression("length(\"test\")");
        FunctionExpression length = Expression.getReservedFunctions("length");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(5, res.doubleValue());

        e = new Expression("substring(\"test\", 2, 3)");
        FunctionExpression subString = Expression.getReservedFunctions("substring");
        res = e.evaluate();
        assertTrue(res instanceof String);
        assertEquals("es", res.toString());

        e = new Expression("toUpper(\"test\")");
        FunctionExpression toUpper = Expression.getReservedFunctions("toUpper");
        res = e.evaluate();
        assertTrue(res instanceof String);
        assertEquals("TEST", res.toString());

        e = new Expression("toLower(\"Test\")");
        FunctionExpression toLower = Expression.getReservedFunctions("toLower");
        res = e.evaluate();
        assertTrue(res instanceof String);
        assertEquals("test", res.toString());

        e = new Expression("dateAdd(\"2022-01-01\", 1, \"day\")");
        FunctionExpression dateAdd = Expression.getReservedFunctions("dateAdd");
        res = e.evaluate();
        assertTrue(res instanceof String);
        assertEquals("2022-01-02", res.toString());

        e = new Expression("dateDiff(\"2022-01-02\", \"2022-01-01\", \"day\")");
        FunctionExpression dateDiff = Expression.getReservedFunctions("dateDiff");
        res = e.evaluate();
        assertTrue(res instanceof Number);
        assertEquals(1, res.doubleValue());
    }
}