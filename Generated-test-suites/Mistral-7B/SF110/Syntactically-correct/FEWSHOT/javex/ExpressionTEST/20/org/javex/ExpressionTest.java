package org.javex.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.date.LengthFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.EqualsFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("");
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("or"));
        assertTrue(expression.isReservedFunction("not"));
        assertTrue(expression.isReservedFunction("equals"));
        assertTrue(expression.isReservedFunction("notequals"));
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("or"));
        assertTrue(expression.isReservedFunction("greaterthan"));
        assertTrue(expression.isReservedFunction("lessthan"));
        assertTrue(expression.isReservedFunction("lessequalsto"));
        assertTrue(expression.isReservedFunction("greaterthanorequalsto"));
        assertTrue(expression.isReservedFunction("isempty"));
        assertTrue(expression.isReservedFunction("isnull"));
        assertTrue(expression.isReservedFunction("startswith"));
        assertTrue(expression.isReservedFunction("endswith"));
        assertTrue(expression.isReservedFunction("contains"));
        assertTrue(expression.isReservedFunction("matches"));
        assertTrue(expression.isReservedFunction("inlist"));
        assertTrue(expression.isReservedFunction("abs"));
        assertTrue(expression.isReservedFunction("divide"));
        assertTrue(expression.isReservedFunction("multiply"));
        assertTrue(expression.isReservedFunction("plus"));
        assertTrue(expression.isReservedFunction("sqrt"));
        assertTrue(expression.isReservedFunction("subtract"));
        assertTrue(expression.isReservedFunction("power"));
        assertTrue(expression.isReservedFunction("powerchar"));
        assertTrue(expression.isReservedFunction("dateadd"));
        assertTrue(expression.isReservedFunction(" datediff"));
        assertTrue(expression.isReservedFunction("length"));
        assertTrue(expression.isReservedFunction("substring"));
        assertTrue(expression.isReservedFunction("tolower"));
        assertTrue(expression.isReservedFunction("toupper"));

        assertFalse(expression.isReservedFunction(""));
        assertFalse(expression.isReservedFunction("foo"));
        assertFalse(expression.isReservedFunction("123"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("");
        assertFalse(expression.isLogicalFunction(""));
        assertFalse(expression.isLogicalFunction("foo"));
        assertFalse(expression.isLogicalFunction("123"));
        assertFalse(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("or"));
        assertFalse(expression.isLogicalFunction("not"));
        assertTrue(expression.isLogicalFunction(new AndFunction().getSimbol()));
        assertTrue(expression.isLogicalFunction(new OrFunction().getSimbol()));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("");
        assertFalse(expression.isMathFunction(""));
        assertFalse(expression.isMathFunction("foo"));
        assertFalse(expression.isMathFunction("123"));
        assertFalse(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("add"));
        assertFalse(expression.isMathFunction("div"));
        assertFalse(expression.isMathFunction("mul"));
        assertFalse(expression.isMathFunction("pow"));
        assertFalse(expression.isMathFunction("sqr"));
        assertFalse(expression.isMathFunction("sub"));
        assertTrue(expression.isMathFunction(new AbsFunction().getSimbol()));
        assertTrue(expression.isMathFunction(new DivideFunction().getSimbol()));
        assertTrue(expression.isMathFunction(new MultiplyFunction().getSimbol()));
        assertTrue(expression.isMathFunction(new PowerFunction().getSimbol()));
        assertTrue(expression.isMathFunction(new SqrtFunction().getSimbol()));
        assertTrue(expression.isMathFunction(new SubstractFunction().getSimbol()));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("");
        assertFalse(expression.isDateFunction(""));
        assertFalse(expression.isDateFunction("foo"));
        assertFalse(expression.isDateFunction("123"));
        assertFalse(expression.isDateFunction("dateadd"));
        assertFalse(expression.isDateFunction("datediff"));
        assertFalse(expression.isDateFunction("length"));
        assertFalse(expression.isDateFunction("substring"));
        assertFalse(expression.isDateFunction("tolower"));
        assertFalse(expression.isDateFunction("toupper"));
        assertTrue(expression.isDateFunction(new DateAddFunction().getSimbol()));
        assertTrue(expression.isDateFunction(new DateDiffFunction().getSimbol()));
        assertTrue(expression.isDateFunction(new LengthFunction().getSimbol()));
        assertTrue(expression.isDateFunction(new SubStringFunction().getSimbol()));
        assertTrue(expression.isDateFunction(new ToLowerFunction().getSimbol()));
        assertTrue(expression.isDateFunction(new ToUpperFunction().getSimbol()));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("");
        assertFalse(expression.isStringFunction(""));
        assertFalse(expression.isStringFunction("foo"));
        assertFalse(expression.isStringFunction("123"));
        assertFalse(expression.isStringFunction("contains"));
        assertFalse(expression.isStringFunction("endswith"));
        assertFalse(expression.isStringFunction("equals"));
        assertFalse(expression.isStringFunction("startswith"));
        assertFalse(expression.isStringFunction("substring"));
        assertFalse(expression.isStringFunction("tolower"));
        assertFalse(expression.isStringFunction("toupper"));
        assertTrue(expression.isStringFunction(new ContainsFunction().getSimbol()));
        assertTrue(expression.isStringFunction(new EndsWithFunction().getSimbol()));
        assertTrue(expression.isStringFunction(new EqualsFunction().getSimbol()));
        assertTrue(expression.isStringFunction(new StartsWithFunction().getSimbol()));
        assertTrue(expression.isStringFunction(new SubStringFunction().getSimbol()));
        assertTrue(expression.isStringFunction(new ToLowerFunction().getSimbol()));
        assertTrue(expression.isStringFunction(new ToUpperFunction().getSimbol()));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("");
        FunctionExpression andFunction = new AndFunction();
        FunctionExpression orFunction = new OrFunction();
        FunctionExpressionabsFunction = new AbsFunction();
        FunctionExpression divideFunction = new DivideFunction();
        FunctionExpression multiplyFunction = new MultiplyFunction();
        FunctionExpression powerFunction = new PowerFunction();
        FunctionExpression sqrtFunction = new SqrtFunction();
        FunctionExpression subtractFunction = new SubstractFunction();
        FunctionExpression dateAddFunction = new DateAddFunction();
        FunctionExpression dateDiffFunction = new DateDiffFunction();
        FunctionExpression lengthFunction = new LengthFunction();
        FunctionExpression subStringFunction = new SubStringFunction();
        FunctionExpression toLowerFunction = new ToLowerFunction();
        FunctionExpression toUpperFunction = new ToUpperFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(andFunction);
        exp.getExternalFunctions().add(orFunction);
        exp.getExternalFunctions().add(absFunction);
        exp.getExternalFunctions().add(divideFunction);
        exp.getExternalFunctions().add(multiplyFunction);
        exp.getExternalFunctions().add(powerFunction);
        exp.getExternalFunctions().add(sqrtFunction);
        exp.getExternalFunctions().add(subtractFunction);
        exp.getExternalFunctions().add(dateAddFunction);
        exp.getExternalFunctions().add(dateDiffFunction);
        exp.getExternalFunctions().add(lengthFunction);
        exp.getExternalFunctions().add(subStringFunction);
        exp.getExternalFunctions().add(toLowerFunction);
        exp.getExternalFunctions().add(toUpperFunction);

        FunctionExpression result = expression.getFunction("and");
        assertNotNull(result);
        assertEquals(andFunction, result);

        result = expression.getFunction("or");
        assertNotNull(result);
        assertEquals(orFunction, result);

        result = expression.getFunction("abs");
        assertNotNull(result);
        assertEquals(absFunction, result);

        result = expression.getFunction("divide");
        assertNotNull(result);
        assertEquals(divideFunction, result);

        result = expression.getFunction("multiply");
        assertNotNull(result);
        assertEquals(multiplyFunction, result);

        result = expression.getFunction("power");
        assertNotNull(result);
        assertEquals(powerFunction, result);

        result = expression.getFunction("sqrt");
        assertNotNull(result);
        assertEquals(sqrtFunction, result);

        result = expression.getFunction("subtract");
        assertNotNull(result);
        assertEquals(subtractFunction, result);

        result = expression.getFunction("dateadd");
        assertNotNull(result);
        assertEquals(dateAddFunction, result);

        result = expression.getFunction("datediff");
        assertNotNull(result);
        assertEquals(dateDiffFunction, result);

        result = expression.getFunction("length");
        assertNotNull(result);
        assertEquals(lengthFunction, result);

        result = expression.getFunction("substring");
        assertNotNull(result);
        assertEquals(subStringFunction, result);

        result = expression.getFunction("tolower");
        assertNotNull(result);
        assertEquals(toLowerFunction, result);

        result = expression.getFunction("toupper");
        assertNotNull(result);
        assertEquals(toUpperFunction, result);

        result = expression.getFunction("foo");
        assertNull(result);

        result = expression.getFunction("");
        assertNull(result);

        result = expression.getFunction("123");
        assertNull(result);
    }

    @Test
    public void testGetFunctionGreaterThan() {
        Expression expression = new Expression("");
        FunctionExpression greaterThanFunction = new GreaterThanFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(greaterThanFunction);

        FunctionExpression result = expression.getFunction(">");
        assertNotNull(result);
        assertEquals(greaterThanFunction, result);
    }

    @Test
    public void testGetFunctionLessThan() {
        Expression expression = new Expression("");
        FunctionExpression lessThanFunction = new LessThanFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(lessThanFunction);

        FunctionExpression result = expression.getFunction("<");
        assertNotNull(result);
        assertEquals(lessThanFunction, result);
    }

    @Test
    public void testGetFunctionLessThanOrEqual() {
        Expression expression = new Expression("");
        FunctionExpression lessThanOrEqualFunction = new LessEqThanFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(lessThanOrEqualFunction);

        FunctionExpression result = expression.getFunction("<=");
        assertNotNull(result);
        assertEquals(lessThanOrEqualFunction, result);
    }

    @Test
    public void testGetFunctionGreaterThanOrEqual() {
        Expression expression = new Expression("");
        FunctionExpression greaterThanOrEqualFunction = new GreaterThanOrEqualFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(greaterThanOrEqualFunction);

        FunctionExpression result = expression.getFunction(">=");
        assertNotNull(result);
        assertEquals(greaterThanOrEqualFunction, result);
    }

    @Test
    public void testGetFunctionIsEmpty() {
        Expression expression = new Expression("");
        FunctionExpression isEmptyFunction = new IsEmptyFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(isEmptyFunction);

        FunctionExpression result = expression.getFunction("isEmpty");
        assertNotNull(result);
        assertEquals(isEmptyFunction, result);
    }

    @Test
    public void testGetFunctionIsNull() {
        Expression expression = new Expression("");
        FunctionExpression isNullFunction = new IsNullFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(isNullFunction);

        FunctionExpression result = expression.getFunction("isnull");
        assertNotNull(result);
        assertEquals(isNullFunction, result);
    }

    @Test
    public void testGetFunctionStartsWith() {
        Expression expression = new Expression("");
        FunctionExpression startsWithFunction = new StartsWithFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(startsWithFunction);

        FunctionExpression result = expression.getFunction("startswith");
        assertNotNull(result);
        assertEquals(startsWithFunction, result);
    }

    @Test
    public void testGetFunctionEndsWith() {
        Expression expression = new Expression("");
        FunctionExpression endsWithFunction = new EndsWithFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(endsWithFunction);

        FunctionExpression result = expression.getFunction("endswith");
        assertNotNull(result);
        assertEquals(endsWithFunction, result);
    }

    @Test
    public void testGetFunctionContains() {
        Expression expression = new Expression("");
        FunctionExpression containsFunction = new ContainsFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(containsFunction);

        FunctionExpression result = expression.getFunction("contains");
        assertNotNull(result);
        assertEquals(containsFunction, result);
    }

    @Test
    public void testGetFunctionMatches() {
        Expression expression = new Expression("");
        FunctionExpression matchesFunction = new MatchesFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(matchesFunction);

        FunctionExpression result = expression.getFunction("matches");
        assertNotNull(result);
        assertEquals(matchesFunction, result);
    }

    @Test
    public void testGetFunctionInList() {
        Expression expression = new Expression("");
        FunctionExpression inListFunction = new InListFunction();

        Expression exp = new Expression("");
        exp.getExternalFunctions(new ArrayList<>());
        exp.getExternalFunctions().add(inListFunction);

        FunctionExpression result = expression.getFunction("inlist");
        assertNotNull(result);
        assertEquals(inListFunction, result);
    }
}