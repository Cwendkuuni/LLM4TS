package org.javex.expression;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("1 + 2 * (3 + 4) and (5 > 4) and (7 < 8) or (9 > 10)");
        expression.prepare();
    }

    @Test
    public void testIsReservedFunction() {
        Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctionsMap();

        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("or"));
        assertTrue(expression.isReservedFunction(">"));
        assertTrue(expression.isReservedFunction(">="));
        assertTrue(expression.isReservedFunction("<"));
        assertTrue(expression.isReservedFunction("<="));
        assertTrue(expression.isReservedFunction("="));
        assertTrue(expression.isReservedFunction("not"));
        assertTrue(expression.isReservedFunction("is"));
        assertTrue(expression.isReservedFunction("isempty"));
        assertTrue(expression.isReservedFunction("isnull"));
        assertTrue(expression.isReservedFunction("notchar"));
        assertTrue(expression.isReservedFunction("length"));
        assertTrue(expression.isReservedFunction("substring"));
        assertTrue(expression.isReservedFunction("tolower"));
        assertTrue(expression.isReservedFunction("toupper"));
        assertTrue(expression.isReservedFunction("abs"));
        assertTrue(expression.isReservedFunction("divide"));
        assertTrue(expression.isReservedFunction("multiply"));
        assertTrue(expression.isReservedFunction("plus"));
        assertTrue(expression.isReservedFunction("sqrt"));
        assertTrue(expression.isReservedFunction("subtract"));
        assertTrue(expression.isReservedFunction("power"));
        assertTrue(expression.isReservedFunction("powerchar"));
        assertTrue(expression.isReservedFunction("dateadd"));
        assertTrue(expression.isReservedFunction("datediff"));

        assertFalse(expression.isReservedFunction(""));
        assertFalse(expression.isReservedFunction("1"));
        assertFalse(expression.isReservedFunction("123"));
        assertFalse(expression.isReservedFunction("abc"));
    }

    @Test
    public void testIsFunction() {
        Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctionsMap();
        Map<String, FunctionExpression> externalFunctions = new HashMap<>();

        FunctionExpression andFunction = new AndFunction();
        FunctionExpression orFunction = new OrFunction();
        FunctionExpression absFunction = new AbsFunction();
        FunctionExpression divideFunction = new DivideFunction();
        FunctionExpression multiplyFunction = new MultiplyFunction();
        FunctionExpression powerFunction = new PowerFunction();
        FunctionExpression subtractFunction = new SubstractFunction();
        FunctionExpression addFunction = new PlusFunction();
        FunctionExpression sqrtFunction = new SqrtFunction();
        FunctionExpression containsFunction = new ContainsFunction();
        FunctionExpression endsWithFunction = new EndsWithFunction();
        FunctionExpression lengthFunction = new LengthFunction();
        FunctionExpression substringFunction = new SubStringFunction();
        FunctionExpression toLowerFunction = new ToLowerFunction();
        FunctionExpression toUpperFunction = new ToUpperFunction();

        putFunction(andFunction, reservedFunctions);
        putFunction(orFunction, reservedFunctions);
        putFunction(absFunction, reservedFunctions);
        putFunction(divideFunction, reservedFunctions);
        putFunction(multiplyFunction, reservedFunctions);
        putFunction(powerFunction, reservedFunctions);
        putFunction(subtractFunction, reservedFunctions);
        putFunction(addFunction, reservedFunctions);
        putFunction(sqrtFunction, reservedFunctions);
        putFunction(containsFunction, externalFunctions);
        putFunction(endsWithFunction, externalFunctions);
        putFunction(lengthFunction, externalFunctions);
        putFunction(substringFunction, externalFunctions);
        putFunction(toLowerFunction, externalFunctions);
        putFunction(toUpperFunction, externalFunctions);

        expression = new Expression("1 + 2 * (3 + 4) and (5 > 4) and (7 < 8) or (9 > 10)", externalFunctions);

        assertTrue(expression.isFunction("contains"));
        assertTrue(expression.isFunction("endsWith"));
        assertTrue(expression.isFunction("length"));
        assertTrue(expression.isFunction("substring"));
        assertTrue(expression.isFunction("tolower"));
        assertTrue(expression.isFunction("toupper"));

        assertFalse(expression.isFunction(""));
        assertFalse(expression.isFunction("1"));
        assertFalse(expression.isFunction("123"));
        assertFalse(expression.isFunction("abc"));
        assertFalse(expression.isFunction("and"));
        assertFalse(expression.isFunction("or"));
        assertFalse(expression.isFunction("abs"));
        assertFalse(expression.isFunction("divide"));
        assertFalse(expression.isFunction("multiply"));
        assertFalse(expression.isFunction("power"));
        assertFalse(expression.isFunction("subtract"));
        assertFalse(expression.isFunction("add"));
        assertFalse(expression.isFunction("sqrt"));
    }

    @Test
    public void testIsLogicalFunction() {
        Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctionsMap();

        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));

        assertFalse(expression.isLogicalFunction(""));
        assertFalse(expression.isLogicalFunction("1"));
        assertFalse(expression.isLogicalFunction("123"));
        assertFalse(expression.isLogicalFunction("abc"));
        assertFalse(expression.isLogicalFunction("not"));
        assertFalse(expression.isLogicalFunction("is"));
        assertFalse(expression.isLogicalFunction("isempty"));
        assertFalse(expression.isLogicalFunction("isnull"));
        assertFalse(expression.isLogicalFunction("notchar"));
        assertFalse(expression.isLogicalFunction("abs"));
        assertFalse(expression.isLogicalFunction("divide"));
        assertFalse(expression.isLogicalFunction("multiply"));
        assertFalse(expression.isLogicalFunction("power"));
        assertFalse(expression.isLogicalFunction("subtract"));
        assertFalse(expression.isLogicalFunction("add"));
        assertFalse(expression.isLogicalFunction("sqrt"));
        assertFalse(expression.isLogicalFunction("dateadd"));
        assertFalse(expression.isLogicalFunction("datediff"));
        assertFalse(expression.isLogicalFunction("contains"));
        assertFalse(expression.isLogicalFunction("endsWith"));
        assertFalse(expression.isLogicalFunction("length"));
        assertFalse(expression.isLogicalFunction("substring"));
        assertFalse(expression.isLogicalFunction("tolower"));
        assertFalse(expression.isLogicalFunction("toupper"));
    }

    @Test
    public void testIsMathFunction() {
        Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctionsMap();

        assertTrue(expression.isMathFunction("abs"));
        assertTrue(expression.isMathFunction("divide"));
        assertTrue(expression.isMathFunction("multiply"));
        assertTrue(expression.isMathFunction("power"));
        assertTrue(expression.isMathFunction("sqrt"));
        assertTrue(expression.isMathFunction("subtract"));
        assertTrue(expression.isMathFunction("add"));

        assertFalse(expression.isMathFunction(""));
        assertFalse(expression.isMathFunction("1"));
        assertFalse(expression.isMathFunction("123"));
        assertFalse(expression.isMathFunction("abc"));
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("or"));
        assertFalse(expression.isMathFunction("is"));
        assertFalse(expression.isMathFunction("isempty"));
        assertFalse(expression.isMathFunction("isnull"));
        assertFalse(expression.isMathFunction("not"));
        assertFalse(expression.isMathFunction("notchar"));
        assertFalse(expression.isMathFunction("isfunction"));
        assertFalse(expression.isMathFunction("isnullfunction"));
        assertFalse(expression.isMathFunction("notfunction"));
        assertFalse(expression.isMathFunction("notcharfunction"));
        assertFalse(expression.isMathFunction("length"));
        assertFalse(expression.isMathFunction("substring"));
        assertFalse(expression.isMathFunction("tolower"));
        assertFalse(expression.isMathFunction("toupper"));
        assertFalse(expression.isMathFunction("contains"));
        assertFalse(expression.isMathFunction("endsWith"));
    }

    @Test
    public void testIsDateFunction() {
        Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctionsMap();

        assertTrue(expression.isDateFunction("dateadd"));
        assertTrue(expression.isDateFunction("datediff"));

        assertFalse(expression.isDateFunction(""));
        assertFalse(expression.isDateFunction("1"));
        assertFalse(expression.isDateFunction("123"));
        assertFalse(expression.isDateFunction("abc"));
        assertFalse(expression.isDateFunction("and"));
        assertFalse(expression.isDateFunction("or"));
        assertFalse(expression.isDateFunction("is"));
        assertFalse(expression.isDateFunction("isempty"));
        assertFalse(expression.isDateFunction("isnull"));
        assertFalse(expression.isDateFunction("not"));
        assertFalse(expression.isDateFunction("notchar"));
        assertFalse(expression.isDateFunction("abs"));
        assertFalse(expression.isDateFunction("divide"));
        assertFalse(expression.isDateFunction("multiply"));
        assertFalse(expression.isDateFunction("power"));
        assertFalse(expression.isDateFunction("sqrt"));
        assertFalse(expression.isDateFunction("subtract"));
        assertFalse(expression.isDateFunction("add"));
        assertFalse(expression.isDateFunction("length"));
        assertFalse(expression.isDateFunction("substring"));
        assertFalse(expression.isDateFunction("tolower"));
        assertFalse(expression.isDateFunction("toupper"));
        assertFalse(expression.isDateFunction("contains"));
        assertFalse(expression.isDateFunction("endsWith"));
        assertFalse(expression.isDateFunction("isfunction"));
        assertFalse(expression.isDateFunction("isnullfunction"));
        assertFalse(expression.isDateFunction("notfunction"));
        assertFalse(expression.isDateFunction("notcharfunction"));
    }

    @Test
    public void testIsStringFunction() {
        Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctionsMap();

        assertTrue(expression.isStringFunction("contains"));
        assertTrue(expression.isStringFunction("endsWith"));
        assertTrue(expression.isStringFunction("length"));
        assertTrue(expression.isStringFunction("substring"));
        assertTrue(expression.isStringFunction("tolower"));
        assertTrue(expression.isStringFunction("toupper"));

        assertFalse(expression.isStringFunction(""));
        assertFalse(expression.isStringFunction("1"));
        assertFalse(expression.isStringFunction("123"));
        assertFalse(expression.isStringFunction("abc"));
        assertFalse(expression.isStringFunction("and"));
        assertFalse(expression.isStringFunction("or"));
        assertFalse(expression.isStringFunction("is"));
        assertFalse(expression.isStringFunction("isempty"));
        assertFalse(expression.isStringFunction("isnull"));
        assertFalse(expression.isStringFunction("not"));
        assertFalse(expression.isStringFunction("notchar"));
        assertFalse(expression.isStringFunction("abs"));
        assertFalse(expression.isStringFunction("divide"));
        assertFalse(expression.isStringFunction("multiply"));
        assertFalse(expression.isStringFunction("power"));
        assertFalse(expression.isStringFunction("sqrt"));
        assertFalse(expression.isStringFunction("subtract"));
        assertFalse(expression.isStringFunction("add"));
        assertFalse(expression.isStringFunction("dateadd"));
        assertFalse(expression.isStringFunction("datediff"));
        assertFalse(expression.isStringFunction("isfunction"));
        assertFalse(expression.isStringFunction("isnullfunction"));
        assertFalse(expression.isStringFunction("notfunction"));
        assertFalse(expression.isStringFunction("notcharfunction"));
    }

    @Test
    public void testEvaluate() throws Exception {
        Map<String, FunctionExpression> externalFunctions = new HashMap<>();
        FunctionExpression containsFunction = new ContainsFunction();
        FunctionExpression endsWithFunction = new EndsWithFunction();
        FunctionExpression lengthFunction = new LengthFunction();
        FunctionExpression subStringFunction = new SubStringFunction();
        FunctionExpression toLowerFunction = new ToLowerFunction();
        FunctionExpression toUpperFunction = new ToUpperFunction();
        putFunction(containsFunction, externalFunctions);
        putFunction(endsWithFunction, externalFunctions);
        putFunction(lengthFunction, externalFunctions);
        putFunction(subStringFunction, externalFunctions);
        putFunction(toLowerFunction, externalFunctions);
        putFunction(toUpperFunction, externalFunctions);

        expression = new Expression("1 + 2 * (3 + 4) and (5 > 4) and (7 < 8) or (9 > 10)", externalFunctions);
        expression.prepare();

        List<Object> result = expression.evaluate();

        assertEquals(1, result.size());
        assertEquals(true, result.get(0));
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithInvalidExpression() throws Exception {
        expression = new Expression("1 + 2 * (3 + 4) and (5 > 4) and (7 < 8) or (9 > 10 + x");
        expression.prepare();

        expression.evaluate();
    }

    private void putFunction(FunctionExpression function, Map<String, FunctionExpression> map) {
        map.put(function.getSymbol(), function);
    }
}