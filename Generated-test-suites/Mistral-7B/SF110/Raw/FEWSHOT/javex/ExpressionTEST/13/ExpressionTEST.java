package org.javex;

import static org.junit.Assert.*;
import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.junit.Test;
import org.javex.Expression;

import java.util.*;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("and(1, 2)");
        assertTrue(e.isReservedFunction("and"));

        e = new Expression("or(1, 2)");
        assertTrue(e.isReservedFunction("or"));

        e = new Expression("equals(1, 2)");
        assertTrue(e.isReservedFunction("equals"));

        assertFalse(e.isReservedFunction("not"));

        e = new Expression("not(1, 2)");
        assertTrue(e.isReservedFunction("not"));

        e = new Expression("isFunction(x)");
        assertTrue(e.isReservedFunction("isFunction"));

        e = new Expression("isNull(x)");
        assertTrue(e.isReservedFunction("isNull"));

        e = new Expression("isNullFunction()");
        assertFalse(e.isReservedFunction("isNullFunction"));

        e = new Expression("length(x)");
        assertTrue(e.isReservedFunction("length"));

        e = new Expression("substring(x, y, z)");
        assertTrue(e.isReservedFunction("substring"));

        e = new Expression("toUpper(x)");
        assertTrue(e.isReservedFunction("toUpper"));

        e = new Expression("toLower(x)");
        assertTrue(e.isReservedFunction("toLower"));

        e = new Expression("abs(x)");
        assertTrue(e.isReservedFunction("abs"));

        e = new Expression("sqrt(x)");
        assertTrue(e.isReservedFunction("sqrt"));

        e = new Expression("power(x, y)");
        assertTrue(e.isReservedFunction("power"));

        e = new Expression("powerChar(x, y)");
        assertTrue(e.isReservedFunction("powerChar"));

        e = new Expression("dateAdd(x, y, z)");
        assertTrue(e.isReservedFunction("dateAdd"));

        e = new Expression("dateDiff(x, y, z)");
        assertTrue(e.isReservedFunction("dateDiff"));

        e = new Expression("dateFormat(x, y)");
        assertTrue(e.isReservedFunction("dateFormat"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("and(1, 2)");
        assertTrue(e.isLogicalFunction());

        e = new Expression("or(1, 2)");
        assertTrue(e.isLogicalFunction());

        e = new Expression("not(1)");
        assertTrue(e.isLogicalFunction());

        e = new Expression("x > y");
        assertFalse(e.isLogicalFunction());

        e = new Expression("x == y");
        assertFalse(e.isLogicalFunction());

        e = new Expression("contains(x, y)");
        assertTrue(e.isLogicalFunction());

        e = new Expression("startsWith(x, y)");
        assertTrue(e.isLogicalFunction());

        e = new Expression("endsWith(x, y)");
        assertTrue(e.isLogicalFunction());
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("abs(1)");
        assertTrue(e.isMathFunction());

        e = new Expression("sqrt(1)");
        assertTrue(e.isMathFunction());

        e = new Expression("power(1, 2)");
        assertTrue(e.isMathFunction());

        e = new Expression("powerChar(x, y)");
        assertTrue(e.isMathFunction());

        e = new Expression("sin(x)");
        assertFalse(e.isMathFunction());

        e = new Expression("cos(x)");
        assertFalse(e.isMathFunction());

        e = new Expression("tan(x)");
        assertFalse(e.isMathFunction());
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("dateFormat(x, y)");
        assertTrue(e.isDateFunction());

        e = new Expression("dateAdd(x, y, z)");
        assertTrue(e.isDateFunction());

        e = new Expression("dateDiff(x, y, z)");
        assertTrue(e.isDateFunction());

        e = new Expression("now()");
        assertTrue(e.isDateFunction());

        e = new Expression("currentDate()");
        assertTrue(e.isDateFunction());

        e = new Expression("currentTime()");
        assertTrue(e.isDateFunction());

        e = new Expression("today()");
        assertTrue(e.isDateFunction());

        e = new Expression("month(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("dayOfMonth(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("dayOfWeek(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("year(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("quarter(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("hour(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("minute(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("second(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("millisecond(x)");
        assertTrue(e.isDateFunction());

        e = new Expression("isDate(x)");
        assertFalse(e.isDateFunction());
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("length(x)");
        assertTrue(e.isStringFunction());

        e = new Expression("substring(x, y, z)");
        assertTrue(e.isStringFunction());

        e = new Expression("toUpper(x)");
        assertTrue(e.isStringFunction());

        e = new Expression("toLower(x)");
        assertTrue(e.isStringFunction());

        e = new Expression("trim(x)");
        assertTrue(e.isStringFunction());

        e = new Expression("repeat(x, y)");
        assertTrue(e.isStringFunction());

        e = new Expression("concat(x, y)");
        assertTrue(e.isStringFunction());

        e = new Expression("concatenate(x, y)");
        assertTrue(e.isStringFunction());

        e = new Expression("replace(x, y, z)");
        assertTrue(e.isStringFunction());

        e = new Expression("isNull(x)");
        assertFalse(e.isStringFunction());

        e = new Expression("isAlpha(x)");
        assertFalse(e.isStringFunction());

        e = new Expression("isNumeric(x)");
        assertFalse(e.isStringFunction());

        e = new Expression("isDate(x)");
        assertFalse(e.isStringFunction());
    }

    @Test
    public void testTokenize() {
        Expression e = new Expression("1 + 2 * (3 - 4) / 5");
        ExpressionStack stack = e.getStackCopy();

        List<String> expected = Arrays.asList("1", "+", "2", "*", "(", "3", "-", "4", ")", "/", "5");
        List<String> actual = new ArrayList<>();

        for (String s : stack) {
            actual.add(s.trim());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testPrepare() {
        Expression e = new Expression("1 + 2 * (3 - 4) / 5");
        e.prepare();

        ExpressionStack stack = e.getStackCopy();
        List<Object> expectedValues = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        List<String> expectedOperators = Arrays.asList("+", "*", "/");

        List<Object> actualValues = new ArrayList<>();
        List<String> actualOperators = new ArrayList<>();

        for (int i = 0; i < stack.size(); i++) {
            String s = stack.get(i);
            if (JavexUtils.isNumber(s)) {
                actualValues.add(Double.parseDouble(s));
            } else {
                actualOperators.add(s);
            }
        }

        assertEquals(expectedValues, actualValues);
        assertEquals(expectedOperators, actualOperators);
    }
}